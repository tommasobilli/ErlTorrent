package com.example.ErlTorrent;


import org.json.simple.parser.ParseException;

import java.io.IOException;

public class PeerProcess {
    public static void main(String[] args) throws IOException, ParseException {
        String jsonFile = args[1];
        PeerAdmin admin = new PeerAdmin(jsonFile);
    }
}
