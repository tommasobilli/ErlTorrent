package entities;

import java.io.Serializable;

public class User implements Serializable {

    private String pid;
    private String username;
    private String pwd;
    private String APIToken;
    private String address;
    private String listeningPort;

    public User(String username, String pwd, String APIToken){
        this.username = username;
        this.pwd = pwd;
        this.APIToken = APIToken;
    }

    public User(String pid, String username, String pwd, String APIToken, String address, String listeningPort){
        this.pid = pid;
        this.username = username;
        this.pwd = pwd;
        this.APIToken = APIToken;
        this.address = address;
        this.listeningPort = listeningPort;
    }

    public String getUsername(){
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }


    public String getPwd(){
        return this.pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getAPIToken(){
        return this.APIToken;
    }

    public void setAPIToken(String APIToken) {
        this.APIToken = APIToken;
    }

    public String getAddress(){
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getListeningPort(){
        return this.listeningPort;
    }

    public void setListeningPort(String listeningPort) {
        this.listeningPort = listeningPort;
    }
}
