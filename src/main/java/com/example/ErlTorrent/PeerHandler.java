package com.example.ErlTorrent;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.*;
import java.util.Arrays;
import java.util.BitSet;
import java.nio.*;
import java.lang.*;

public class PeerHandler implements Runnable {
    private Socket listener;
    private PeerAdmin peerAdmin;
    private boolean first;
    private String endPeerID;
    private boolean connectionEstablished = false;
    private boolean initializer = false;
    private HandshakeMessage hsm;
    //private volatile int downloadRate = 0;
    private volatile ObjectOutputStream out;
    private volatile ObjectInputStream in;

    public PeerHandler(Socket listener, PeerAdmin admin, boolean first) {
        this.listener = listener;   //Il vicino da cui ho ricevuto la richiesta
        this.peerAdmin = admin;
        this.first = first;
        initStreams();
        this.hsm = new HandshakeMessage(this.peerAdmin.getPeerID());

    }

    public PeerAdmin getPeerAdmin() {
        return this.peerAdmin;
    }

    public void initStreams() {
        try {
            this.out = new ObjectOutputStream(this.listener.getOutputStream());
            this.out.flush();
            this.in = new ObjectInputStream(this.listener.getInputStream());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized Socket getListener() {
        return this.listener;
    }

    public void setEndPeerID(String pid) {
        this.endPeerID = pid;
        this.initializer = true;
    }

    public void run() {
        try {
            byte[] msg = this.hsm.buildHandShakeMessage();
            this.out.write(msg);
            this.out.flush();
            System.out.println("Handshake message sent.");
            while (true) {
                if (!this.connectionEstablished) {
                    byte[] response = readBytes(40);
                    System.out.println("Handshake message received.");
                    this.processHandShakeMessage(response);
                    if (this.peerAdmin.hasFile() || this.peerAdmin.getAvailabilityOf(this.peerAdmin.getPeerID()).cardinality() > 0) {
                        this.sendBitField();   //se ho qualche chunk lo avverto
                    }
                } else {
                    while (this.in.available() > 0) {
                        int respLen = this.in.readInt();
                        byte[] response = readBytes(respLen);
                        char messageType = (char) response[0]; //vedere il formato dei messaggi
                        ActualMessage am = new ActualMessage();
                        am.readActualMessage(respLen, response);
                        if (messageType == '4') {
                            // Handles Have Message
                            int pieceIndex = am.getPieceIndexFromPayload();
                            this.peerAdmin.updatePieceAvailability(this.endPeerID, pieceIndex);
                            if (this.peerAdmin.checkIfAllPeersAreDone()) {
                                this.peerAdmin.cancelChokes();
                            }
                            if (this.peerAdmin.checkIfInterested(this.endPeerID)) {

                            } else {

                            }
                            this.peerAdmin.getLogger().receiveHave(this.endPeerID, pieceIndex);
                        } else if (messageType == '5') {
                            // Handles BitField message
                            BitSet bset = am.getBitFieldMessage();
                            this.processBitFieldMessage(bset);
                            if (!this.peerAdmin.hasFile()) {
                                if (this.peerAdmin.checkIfInterested(this.endPeerID)) {
                                    int requestindex = this.peerAdmin.checkForRequested(this.endPeerID);
                                    if (requestindex != -1) {
                                        this.sendRequestMessage(requestindex);
                                    }
                                }
                            }
                        } else if (messageType == '6') {
                            // Handles Request Message
                            int pieceIndex = am.getPieceIndexFromPayload();
                            this.sendPieceMessage(pieceIndex, this.peerAdmin.readFromFile(pieceIndex));
                        } else if (messageType == '7') {
                            // Handle Piece Message
                            int pieceIndex = am.getPieceIndexFromPayload();
                            byte[] piece = am.getPieceFromPayload();
                            this.peerAdmin.writeToFile(piece, pieceIndex);
                            this.peerAdmin.updatePieceAvailability(this.peerAdmin.getPeerID(), pieceIndex);
                            boolean alldone = this.peerAdmin.checkIfAllPeersAreDone();
                            this.peerAdmin.getLogger().downloadPiece(this.endPeerID, pieceIndex,
                                    this.peerAdmin.getCompletedPieceCount());
                            this.peerAdmin.setRequestedInfo(pieceIndex, null);
                            this.peerAdmin.broadcastHave(pieceIndex);
                            // Progress bar
                            int totalPieces = this.peerAdmin.getPieceCount();
                            int havePieces = this.peerAdmin.getCompletedPieceCount();
                            progressBar(havePieces, totalPieces);
                            if (this.peerAdmin.getAvailabilityOf(this.peerAdmin.getPeerID()).cardinality() != this.peerAdmin
                                    .getPieceCount()) {
                                int requestindex = this.peerAdmin.checkForRequested(this.endPeerID);
                                if (requestindex != -1) {
                                    this.sendRequestMessage(requestindex);
                                }
                            } else {
                                this.peerAdmin.getLogger().downloadComplete();
                                if (alldone) {
                                    System.out.println("Download complete.");
                                    this.peerAdmin.cancelChokes();
                                }

                            }
                        } else {
                            System.out.println("Received other message");
                        }
                    }
                }
            }
        }
        catch (SocketException e) {
            System.out.println("Socket exception");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void progressBar(int done, int total) {
        int size = 20;
        String iconLeftBoundary = "Downloading file... [";
        String iconDone = "#";
        String iconRemain = " ";
        String iconRightBoundary = "]";

        if (done > total) {
            throw new IllegalArgumentException();
        }
        int donePercent = (100 * done) / total;
        int doneLength = size * donePercent / 100;

        StringBuilder bar = new StringBuilder(iconLeftBoundary);
        for (int i = 0; i < size; i++) {
            if (i < doneLength) {
                bar.append(iconDone);
            } else {
                bar.append(iconRemain);
            }
        }
        bar.append(iconRightBoundary);

        System.out.print("\r" + bar + " " + done + " of " + total + " pieces (" + donePercent + "%)");

        if (done == total) {
            System.out.print("\n");
        }
    }

    public synchronized byte[] readBytes(int len) throws IOException {
        byte[] message = new byte[len];
        int count = this.in.read(message);
        while (count != len) {
            int res = this.in.read(message, count, len - count);
            if (res == -1) {
                throw new IOException("Read error");
            } else {
                count += res;
            }
        }
        return message;
    }

    public synchronized void send(byte[] obj) {
        try {
            this.out.write(obj);
            this.out.flush();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendHaveMessage(int pieceIndex) {
        try {
            byte[] bytes = ByteBuffer.allocate(4).putInt(pieceIndex).array();
            ActualMessage am = new ActualMessage('4', bytes);
            this.send(am.buildActualMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendBitField() {
        try {
            BitSet myAvailability = this.peerAdmin.getAvailabilityOf(this.peerAdmin.getPeerID());
            ActualMessage am = new ActualMessage('5', myAvailability.toByteArray());
            this.send(am.buildActualMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendRequestMessage(int pieceIndex) {
        try {
            byte[] bytes = ByteBuffer.allocate(4).putInt(pieceIndex).array();
            ActualMessage am = new ActualMessage('6', bytes);
            this.send(am.buildActualMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPieceMessage(int pieceIndex, byte[] payload) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            byte[] bytes = ByteBuffer.allocate(4).putInt(pieceIndex).array();
            stream.write(bytes);
            stream.write(payload);
            ActualMessage am = new ActualMessage('7', stream.toByteArray());
            this.send(am.buildActualMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void processHandShakeMessage(byte[] message) {
        try {
            this.hsm.readHandShakeMessage(message);
            this.endPeerID = this.hsm.getPeerID();
            this.peerAdmin.addJoinedPeer(this, this.endPeerID);
            this.peerAdmin.addJoinedThreads(this.endPeerID, Thread.currentThread());
            this.connectionEstablished = true;
            if (this.initializer) {
                this.peerAdmin.getLogger().genTCPConnLogSender(this.endPeerID);
            }
            else {
                this.peerAdmin.getLogger().genTCPConnLogReceiver(this.endPeerID);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void processBitFieldMessage(BitSet b) {
        this.peerAdmin.updateBitset(this.endPeerID, b);
    }

}
