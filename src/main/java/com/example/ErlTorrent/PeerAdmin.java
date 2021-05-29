package com.example.ErlTorrent;

import java.io.*;
import java.lang.*;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.ErlTorrent.PeerHandler;
import com.example.ErlTorrent.PeerInfoConfig;
import com.example.ErlTorrent.PeerLogger;
import com.example.ErlTorrent.PeerServer;
import com.example.ErlTorrent.RemotePeerInfo;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.json.stream.JsonParser;

public class PeerAdmin {
    private String peerID;
    public RemotePeerInfo myConfig;
    public HashMap<String, RemotePeerInfo> peerInfoMap;
    private ArrayList<String> peerList;
    private volatile HashMap<String, PeerHandler> joinedPeers;
    private volatile ServerSocket listener;
    private PeerServer server;
    private PeerInfoConfig peerInfoConfig;
    private volatile PeerLogger logger;
    private volatile HashMap<String, BitSet> piecesAvailability;
    private volatile String[] requestedInfo;
    private int pieceCount;
    private volatile RandomAccessFile fileRaf;
    private Thread serverThread;
    public volatile Boolean iamDone;
    public CommonConfig commonConfig;
    private String token;

    public String getToken() {
        return token;
    }

    public String getTracker_addr_port() {
        return tracker_addr_port;
    }

    private String tracker_addr_port;
    HttpConnection conn = new HttpConnection();
    public ExecutorService pool_threads = Executors.newCachedThreadPool();

    public PeerAdmin(String jsonFile) throws IOException, ParseException {
        this.peerInfoMap = new HashMap<>();
        this.piecesAvailability = new HashMap<>();
        this.peerList = new ArrayList<>();
        this.joinedPeers = new HashMap<>();
        this.peerInfoConfig = new PeerInfoConfig();
        loadConfig(jsonFile);
        this.logger = new PeerLogger(this.peerID);
        this.iamDone = false;
        this.initPeer();
    }

    private void loadConfig(String filename) throws IOException, ParseException {
        this.commonConfig = new CommonConfig(filename);
        Object obj = new JSONParser().parse(new FileReader(filename));
        JSONObject json = (JSONObject) obj;
        this.peerID = (String) json.get("pid");
        String port = (String) json.get("listeningPort");
        String b = (String) json.get("ContainsFile");
        this.token = (String) json.get("API_token");
        this.tracker_addr_port = (String) json.get("tracker_addr_port");
        RemotePeerInfo myPeerInfo = new RemotePeerInfo(this.peerID, "127.0.0.1", port, b);
        this.peerInfoConfig.addPeer(myPeerInfo);
    }

    public void initPeer() {
        try {
            // GET JSON from REST API ---------------------
            JSONObject peerList = conn.make_GET_request(this.commonConfig.FileName, this.tracker_addr_port, this.token);
            //Object obj = new JSONParser().parse(new FileReader("peerList.json"));
            //JSONObject peerList = (JSONObject) obj;
            System.out.println(peerList);
            this.peerInfoConfig.loadConfigFile(peerList);
            this.pieceCount = this.calcPieceCount();    //calcola il numero di pezzi del file da ricchiedere
            this.requestedInfo = new String[this.pieceCount];
            this.myConfig = this.peerInfoConfig.getPeerConfig(this.peerID); //Ricavo la mia configurazione
            this.peerInfoMap = this.peerInfoConfig.getPeerInfoMap();
            this.peerList = this.peerInfoConfig.getPeerList();
            String filepath = "files";
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
            if (this.peerInfoMap.get(this.myConfig.peerId).containsFile == 0) {
                this.createNeighbourConnections();
                while (!this.checkIfDone()) {      //!iamdone
                    Thread.sleep(2000);
                    //get per richiedere la lista
                    JSONObject peerList_new = conn.make_GET_request(this.commonConfig.FileName, this.tracker_addr_port, this.token);
                    HashMap<String, RemotePeerInfo> newPeerMap = new HashMap<>();
                    newPeerMap = this.peerInfoConfig.get_new_peers(peerList_new);
                    for (String key : newPeerMap.keySet()) {
                        RemotePeerInfo peer = newPeerMap.get(key);
                        try {
                            Socket temp = new Socket(peer.peerAddress, peer.peerPort);
                            PeerHandler p = new PeerHandler(temp, this, true);
                            p.setEndPeerID(key);
                            this.addJoinedPeer(p, key);
                            //Thread t = new Thread(p);
                            pool_threads.execute(p);
                            //this.addJoinedThreads(key, p);
                            //t.start();
                            //System.out.println("Creata nuova connessione");
                            //System.out.println("Started PeerHandler on " + peer.peerAddress + ":" + peer.peerPort + ".");
                        } catch (ConnectException e) {
                            this.peerInfoMap.remove(key);
                            this.peerList.remove(key);
                            //System.out.println("Rimosso peer dalla peerinfomap");
                        }
                    }
                    //craere le connessioni solo per quelli che non c'erano prima
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
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
            //this.server = new PeerServer(this.peerID, this.listener, this);
            //this.serverThread = new Thread(this.server);
            //this.serverThread.start();
            this.pool_threads.execute(new PeerServer(this.peerID, this.listener, this));
            System.out.println("Started listening socket on port " + this.myConfig.peerPort + ".");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createNeighbourConnections() throws InterruptedException {  //il peer crea connessioni con i vicini che sono nella lista
        // viene creato un thread per ogni vicino con cui stabilisco una connessione
            Thread.sleep(2000);
            for (String pid : this.peerList) {
                if (!pid.equals(this.peerID)) {
                    RemotePeerInfo peer = this.peerInfoMap.get(pid);
                    try {
                        Socket temp = new Socket(peer.peerAddress, peer.peerPort);
                        PeerHandler p = new PeerHandler(temp, this, true);
                        p.setEndPeerID(pid);
                        this.addJoinedPeer(p, pid);
                        //Thread t = new Thread(p);
                        pool_threads.execute(p);
                        //this.addJoinedThreads(pid, t);
                        //t.start();
                        //System.out.println("Started PeerHandler on " + peer.peerAddress + ":" + peer.peerPort + ".");
                    } catch (ConnectException e) {
                        this.peerInfoMap.remove(pid);
                        this.peerList.remove(pid);
                        //System.out.println("Rimosso peer dalla peerinfomap");
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
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

    /*
    public synchronized void broadcastHave() { //non necessaria
        for (String key : this.joinedPeers.keySet()) {
            if (peerInfoMap.get(key).containsFile == 1)
                this.joinedPeers.get(key).sendHaveMessage();
        }
    }
       */

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

    /*
    public synchronized HashMap<String, Thread> getJoinedThreads() {
        return this.joinedThreads;
    } */

    public PeerHandler getPeerHandler(String peerid) {
        return this.joinedPeers.get(peerid);
    }

    public BitSet getAvailabilityOf(String pid) {
        return this.piecesAvailability.get(pid);
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

    public synchronized int checkHaveAll(String endpeerid) {
        BitSet end = this.getAvailabilityOf(endpeerid);
        BitSet mine = this.getAvailabilityOf(this.peerID);
        int miss_index = mine.nextClearBit(0);
        if (end.get(miss_index) == true) {
            setRequestedInfo(miss_index, endpeerid);
            return miss_index;
        }
        return -1;
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

    public synchronized void setIamDone() {
        this.iamDone = true;
    }

    /*
    public synchronized void closeHandlers() {
        for (String peer : this.joinedThreads.keySet()) {
            this.joinedThreads.get(peer).interrupt();
        }
    } */

    public synchronized void cancelChokes() {
        try {
            //this.getRefFile().close();
            this.getLogger().closeLogger();
            //this.getListener().close(); //chiudo il server del peer
            //this.getServerThread().interrupt(); //interrupt???
            //for (String key : this.joinedPeers.keySet())
                //if (!this.joinedPeers.get(key).getListener().isClosed())//chiude i socket degli handler connessi a peer
                    //this.joinedPeers.get(key).getListener().close();
            pool_threads.shutdownNow(); //chiudo tutti i thread
            //pool_receivers.shutdownNow();
            //this.closeHandlers();  //chiude tutti gli handler creati conessi con i vari peer
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