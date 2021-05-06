package com.example.ErlTorrent;

import java.io.*;
import java.lang.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.example.ErlTorrent.PeerHandler;
import com.example.ErlTorrent.PeerInfoConfig;
import com.example.ErlTorrent.PeerLogger;
import com.example.ErlTorrent.PeerServer;
import com.example.ErlTorrent.RemotePeerInfo;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PeerAdmin {
    private String peerID;
    private RemotePeerInfo myConfig;
    private HashMap<String, RemotePeerInfo> peerInfoMap;
    private ArrayList<String> peerList;
    private volatile HashMap<String, PeerHandler> joinedPeers;
    private volatile HashMap<String, Thread> joinedThreads;
    private volatile ServerSocket listener;
    private PeerServer server;
    private PeerInfoConfig peerInfoConfig;
    private volatile PeerLogger logger;
    private volatile HashMap<String, BitSet> piecesAvailability;
    private volatile String[] requestedInfo;
    private int pieceCount;
    private volatile RandomAccessFile fileRaf;
    private Thread serverThread;
    private volatile Boolean iamDone;
    private CommonConfig commonConfig;

    public PeerAdmin() throws IOException, ParseException {
        this.peerInfoMap = new HashMap<>();
        this.piecesAvailability = new HashMap<>();
        this.peerList = new ArrayList<>();
        this.joinedPeers = new HashMap<>();
        this.joinedThreads = new HashMap<>();
        this.peerInfoConfig = new PeerInfoConfig();
        this.logger = new PeerLogger(this.peerID);
        this.iamDone = false;
        loadConfig("config.json");
        this.initPeer();
    }

    private void loadConfig(String filename) throws IOException, ParseException {
        this.commonConfig = new CommonConfig(filename);
        Object obj = new JSONParser().parse(new FileReader(filename));
        JSONObject json = (JSONObject) obj;
        this.peerID = (String) json.get("pid");
        String port = (String) json.get("listeningPort");
        RemotePeerInfo myPeerInfo = new RemotePeerInfo(this.peerID, "127.0.0.1", port, "0");
        this.peerInfoConfig.addPeer(myPeerInfo);
    }

    public void initPeer() {
        try {
            // GET JSON from REST API
            Object obj = new JSONParser().parse(new FileReader("peerList.json"));
            JSONObject peerList = (JSONObject) obj;
            this.peerInfoConfig.loadConfigFile(peerList);
            this.pieceCount = this.calcPieceCount();    //calcola il numero di pezzi del file da ricchiedere
            this.requestedInfo = new String[this.pieceCount];
            this.myConfig = this.peerInfoConfig.getPeerConfig(this.peerID); //Ricavo la mia configurazione
            this.peerInfoMap = this.peerInfoConfig.getPeerInfoMap();
            this.peerList = this.peerInfoConfig.getPeerList();
            String filepath = "peer_" + this.peerID;
            File file = new File(filepath);
            file.mkdir();
            String filename = filepath + "/" + getFileName();
            file = new File(filename);
            if (!hasFile()) {
                file.createNewFile();
            }
            this.fileRaf = new RandomAccessFile(file, "rw");
            if (!hasFile()) {
                this.fileRaf.setLength(this.getFileSize());    //Lunghezza del file che voglio ricevere
            }
            this.initializePieceAvailability();
            this.startServer();
            this.createNeighbourConnections();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void initializePieceAvailability() {         //Mappa della disponibilità tra i vari peer
        for (String pid : this.peerInfoMap.keySet()) {
            BitSet availability = new BitSet(this.pieceCount);
            if (this.peerInfoMap.get(pid).containsFile == 1) {
                availability.set(0, this.pieceCount);
                this.piecesAvailability.put(pid, availability);
            } else {
                availability.clear();
                this.piecesAvailability.put(pid, availability);
            }
        }
    }

    public void startServer() {
        // A server socket waits for requests to come in over the network. It performs some operation based on that request,
        // and then possibly returns a result to the requester.
        try {
            this.listener = new ServerSocket(this.myConfig.peerPort);  //Porta dove ascolto le richieste (associata con il mio indirizzo e porta)
            this.server = new PeerServer(this.peerID, this.listener, this);
            this.serverThread = new Thread(this.server);
            this.serverThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createNeighbourConnections() {  //il peer crea connessioni con i vicini che sono nella lista
        // viene creato un thread per ogni vicino con cui stabilisco una connessione
        try {
            Thread.sleep(5000);
            for (String pid : this.peerList) {
                if (pid.equals(this.peerID)) {
                    break;
                }
                else {
                    RemotePeerInfo peer = this.peerInfoMap.get(pid);
                    Socket temp = new Socket(peer.peerAddress, peer.peerPort);
                    PeerHandler p = new PeerHandler(temp, this);
                    p.setEndPeerID(pid);
                    this.addJoinedPeer(p, pid);
                    Thread t = new Thread(p);
                    this.addJoinedThreads(pid, t);
                    t.start();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void writeToFile(byte[] data, int pieceindex) {
        try {
            int position = this.getPieceSize() * pieceindex;
            this.fileRaf.seek(position);
            this.fileRaf.write(data);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized byte[] readFromFile(int pieceindex) {
        try {
            int position = this.getPieceSize() * pieceindex;
            int size = this.getPieceSize();
            if (pieceindex == getPieceCount() - 1) {
                size = this.getFileSize() % this.getPieceSize();
            }
            this.fileRaf.seek(position);
            byte[] data = new byte[size];
            this.fileRaf.read(data);
            return data;
        }
        catch (Exception e) {
            e.printStackTrace();

        }
        return new byte[0];
    }

    public synchronized void broadcastHave(int pieceIndex) {
        for (String key : this.joinedPeers.keySet()) {
            this.joinedPeers.get(key).sendHaveMessage(pieceIndex);
        }
    }

    public synchronized void updatePieceAvailability(String peerID, int index) {
        this.piecesAvailability.get(peerID).set(index);
    }

    public synchronized void updateBitset(String peerID, BitSet b) {
        this.piecesAvailability.remove(peerID);
        this.piecesAvailability.put(peerID, b);
    }

    public synchronized void addJoinedPeer(PeerHandler p, String endpeerid) {
        this.joinedPeers.put(endpeerid, p);
    }

    public synchronized void addJoinedThreads(String epeerid, Thread th) {
        this.joinedThreads.put(epeerid, th);
    }

    public synchronized HashMap<String, Thread> getJoinedThreads() {
        return this.joinedThreads;
    }

    public PeerHandler getPeerHandler(String peerid) {
        return this.joinedPeers.get(peerid);
    }

    public BitSet getAvailabilityOf(String pid) {
        return this.piecesAvailability.get(pid);
    }

    public synchronized boolean checkIfInterested(String endpeerid) {  //non viene implementato il Rarest First
        BitSet end = this.getAvailabilityOf(endpeerid);
        BitSet mine = this.getAvailabilityOf(this.peerID);
        for (int i = 0; i < end.size() && i < this.pieceCount; i++) {
            if (end.get(i) == true && mine.get(i) == false) {
                return true;
            }
        }
        return false;
    }

    public synchronized void setRequestedInfo(int id, String peerID) {
        this.requestedInfo[id] = peerID;
    }

    public synchronized int checkForRequested(String endpeerid) {
        BitSet end = this.getAvailabilityOf(endpeerid);
        BitSet mine = this.getAvailabilityOf(this.peerID);
        for (int i = 0; i < end.size() && i < this.pieceCount; i++) {
            if (end.get(i) == true && mine.get(i) == false && this.requestedInfo[i] == null) {
                setRequestedInfo(i, endpeerid);
                return i;
            }
        }
        return -1;
    }

    public synchronized void resetRequested(String endpeerid) {
        for (int i = 0; i < this.requestedInfo.length; i++) {
            if (this.requestedInfo[i] != null && this.requestedInfo[i].compareTo(endpeerid) == 0) {
                setRequestedInfo(i, null);
            }
        }
    }

    public String getPeerID() {
        return this.peerID;
    }

    public PeerLogger getLogger() {
        return this.logger;
    }

    public boolean hasFile() {
        return this.myConfig.containsFile == 1;
    }

    public int calcPieceCount() {
        int len = (getFileSize() / getPieceSize());
        if (getFileSize() % getPieceSize() != 0) {
            len += 1;
        }
        return len;
    }

    public int getPieceCount() {
        return this.pieceCount;
    }

    public int getCompletedPieceCount() {
        return this.piecesAvailability.get(this.peerID).cardinality();
    }

    public synchronized boolean checkIfAllPeersAreDone() {
        for (String peer : this.piecesAvailability.keySet()) {
            if (this.piecesAvailability.get(peer).cardinality() != this.pieceCount) {
                return false;
            }
        }
        return true;
    }

    public synchronized RandomAccessFile getRefFile() {
        return this.fileRaf;
    }

    public synchronized ServerSocket getListener() {
        return this.listener;
    }

    public synchronized Thread getServerThread() {
        return this.serverThread;
    }

    public synchronized Boolean checkIfDone() {
        return this.iamDone;
    }

    public synchronized void closeHandlers() {
        for (String peer : this.joinedThreads.keySet()) {
            this.joinedThreads.get(peer).stop();
        }
    }

    public synchronized void cancelChokes() {
        try {
            this.getRefFile().close();
            this.getLogger().closeLogger();
            this.getListener().close();
            this.getServerThread().stop();
            this.iamDone = true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFileName() {
        return this.commonConfig.FileName;
    }

    public int getFileSize() {
        return this.commonConfig.FileSize;
    }

    public int getPieceSize() {
        return this.commonConfig.PieceSize;
    }
}