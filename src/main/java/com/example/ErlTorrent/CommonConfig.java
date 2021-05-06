package com.example.ErlTorrent;

import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class CommonConfig {

    private final String jsonFileName;
    public String FileName;
    public int FileSize;
    public int PieceSize;

    public static void main(String[] args) throws IOException, ParseException {
        CommonConfig cc = new CommonConfig("config.json");
    }

    public CommonConfig(String jsonFileName) throws IOException, ParseException {
        this.jsonFileName = jsonFileName;
        loadCommonFile();
    }

    public void loadCommonFile() throws IOException, ParseException {
        Object obj = new JSONParser().parse(new FileReader(jsonFileName));
        JSONObject json = (JSONObject) obj;

        this.FileName = (String) json.get("FileName");
        this.PieceSize = Integer.parseInt((String) json.get("PieceSize"));
        File file = new File(this.FileName);
        if (!file.exists() || !file.isFile()) {
            throw new FileNotFoundException();
        }
        this.FileSize = (int) file.length();
    }
}
