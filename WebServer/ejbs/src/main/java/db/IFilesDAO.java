package db;

import java.util.Map;

public interface IFilesDAO {

    Map<String, Map<String, String>> getFilesInfo(String search);

    void insertFile(String filename, String size);

    void deleteFile (String filename);
}
