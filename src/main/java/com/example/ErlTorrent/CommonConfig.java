package com.example.ErlTorrent;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class CommonConfig {

    private final String jsonFileName;
    public String FileName;
    public int FileSize;
    public static final int PieceSize = 256000;

    public CommonConfig(String jsonFileName) throws IOException, ParseException {
        this.jsonFileName = jsonFileName;
        loadCommonFile();
    }

    public void loadCommonFile() throws IOException, ParseException {
        Object obj = new JSONParser().parse(new FileReader(jsonFileName));
        JSONObject json = (JSONObject) obj;

        this.FileName = (String) json.get("FileName");
        //this.PieceSize = Integer.parseInt((String) json.get("PieceSize"));
        this.FileSize = Integer.parseInt((String) json.get("FileSize"));
    }
}
