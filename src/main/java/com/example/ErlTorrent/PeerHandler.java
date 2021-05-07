package com.example.ErlTorrent;

import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
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
            byte[] response = new byte[32];
            if (this.first) {
                byte[] msg = this.hsm.buildHandShakeMessage();
                this.out.write(msg);
                this.out.flush();
                System.out.println(Arrays.toString(msg));

                this.in.readFully(response);
            } else {
                System.out.println("Son qui");
                this.in.readFully(response);
                System.out.println(Arrays.toString(response));
                byte[] msg = this.hsm.buildHandShakeMessage();
                this.out.write(msg);
                this.out.flush();
            }
            while (true) {
                if (!this.connectionEstablished) {
                    this.processHandShakeMessage(response);
                    if (this.peerAdmin.hasFile() || this.peerAdmin.getAvailabilityOf(this.peerAdmin.getPeerID()).cardinality() > 0) {
                        this.sendBitField();   //se ho qualche chunk lo avverto
                    }
                } else {
                    while (this.in.available() < 4) {

                        int respLen = this.in.readInt();
                        byte[] response_ = new byte[respLen];
                        this.in.readFully(response_);
                        char messageType = (char) response_[0]; //vedere il formato dei messaggi
                        ActualMessage am = new ActualMessage();
                        am.readActualMessage(respLen, response_);
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

                                } else {

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
                            Boolean alldone = this.peerAdmin.checkIfAllPeersAreDone();
                            this.peerAdmin.getLogger().downloadPiece(this.endPeerID, pieceIndex,
                                    this.peerAdmin.getCompletedPieceCount());
                            this.peerAdmin.setRequestedInfo(pieceIndex, null);
                            this.peerAdmin.broadcastHave(pieceIndex);
                            if (this.peerAdmin.getAvailabilityOf(this.peerAdmin.getPeerID()).cardinality() != this.peerAdmin
                                    .getPieceCount()) {
                                int requestindex = this.peerAdmin.checkForRequested(this.endPeerID);
                                if (requestindex != -1) {
                                    this.sendRequestMessage(requestindex);
                                } else {

                                }
                            } else {
                                this.peerAdmin.getLogger().downloadComplete();
                                if (alldone) {
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
