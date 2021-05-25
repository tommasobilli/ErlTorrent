package interfaces;

import javax.ejb.Remote;
import java.util.Map;

@Remote
public interface IFilesBean {

    Map<String, Map<String, String>> getFilesInfo(String search);

}