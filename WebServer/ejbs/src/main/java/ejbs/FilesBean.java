package ejbs;

import db.DAO.FilesDAO;
import db.IFilesDAO;
import db.dbConnector;
import interfaces.IFilesBean;

import javax.ejb.Stateless;
import java.util.Map;

@Stateless
public class FilesBean implements IFilesBean  {

    private final IFilesDAO iFilesDAO = new FilesDAO(dbConnector.getInstance());

    public FilesBean() {}

    @Override
    public Map<String, Map<String, String>> getFilesInfo(String search) {
        return iFilesDAO.getFilesInfo(search);
    }

}
