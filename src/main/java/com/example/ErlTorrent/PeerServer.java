package com.example.ErlTorrent;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.rmi.Remote;
import java.util.HashMap;

import com.example.ErlTorrent.PeerAdmin;
import com.example.ErlTorrent.PeerHandler;
import com.example.ErlTorrent.PeerInfoConfig;
import com.example.ErlTorrent.RemotePeerInfo;

public class PeerServer implements Runnable {
    private String peerID;
    private ServerSocket listener;
    private PeerAdmin peerAdmin;

    public PeerServer(String peerID, ServerSocket listener, PeerAdmin admin) {
        this.peerID = peerID;
        this.listener = listener;
        this.peerAdmin = admin;
    }

    public void run() {
        while (true) {
            try {
                Socket neighbour = this.listener.accept();
                //System.out.println(neighbour.getPort());
                //possibile mettere un Executor con un Thread pool
                PeerHandler neighbourHandler = new PeerHandler(neighbour, this.peerAdmin, false); //Accetta connessione dai vicini
                //new Thread(neighbourHandler).start();     //Associa un thread per ogni vicino
                this.peerAdmin.pool_receivers.execute(neighbourHandler);
                //String addr = neighbour.getInetAddress().toString();
                //int port = neighbour.getPort();
            }
            catch (SocketException e) {
                break;
            }
            catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }
}

