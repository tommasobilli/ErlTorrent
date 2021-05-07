package com.example.ErlTorrent;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class PeerInfoConfig {
    private HashMap<String, RemotePeerInfo> peerInfoMap;
    private ArrayList<String> peerList;

    public PeerInfoConfig() {
        this.peerInfoMap = new HashMap<>();
        this.peerList = new ArrayList<>();
    }

    public void loadConfigFile(JSONObject peerList)    //Carico la lista dei peer dal JSON richiesto al tracker nella HASHMAP
    {
        JSONArray peers = (JSONArray) peerList.get("peers");
        for (JSONObject peer : (Iterable<JSONObject>) peers) {
            String pid = (String) peer.get("pid");
            String address = (String) peer.get("address");
            String port = (String) peer.get("port");
            String containsFile = (String) peer.get("containsFile");
            this.peerInfoMap.put(pid, new RemotePeerInfo(pid, address, port, containsFile));
            this.peerList.add(pid);
        }
    }

    public RemotePeerInfo getPeerConfig(String peerID) {
        return this.peerInfoMap.get(peerID);
    }

    public HashMap<String, RemotePeerInfo> getPeerInfoMap() {
        return this.peerInfoMap;
    }

    public ArrayList<String> getPeerList() {
        return this.peerList;
    }

    public void addPeer(RemotePeerInfo remotePeerInfo) {
        this.peerInfoMap.put(remotePeerInfo.peerId, remotePeerInfo);
        this.peerList.add(remotePeerInfo.peerId);
    }
}
