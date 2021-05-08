package com.example.ErlTorrent;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class HandshakeMessage {
    private String peerID;

    public HandshakeMessage(String peerID) {
        this.peerID = peerID;
    }

    public String getPeerID(){
        return this.peerID;
    }

    public byte[] buildHandShakeMessage() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            String handshakeHeader = "ERLTORRENT";
            stream.write(handshakeHeader.getBytes(StandardCharsets.UTF_8));
            stream.write(new byte[6]);
            stream.write(this.peerID.getBytes(StandardCharsets.UTF_8));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return stream.toByteArray();
    }

    public void readHandShakeMessage(byte[] message){
        String msg = new String(message,StandardCharsets.UTF_8);
        this.peerID = msg.substring(16,40);
    }
}
