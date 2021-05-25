package db.entities;
import org.bson.types.ObjectId;

public class dbUser {

    private ObjectId id;
    private String pwd;
    private String username;
    private String token;
    private String address;
    private String listening_port;

    public dbUser(){

    }

    public dbUser(ObjectId id,  String pwd, String username, String token ,String address, String listening_port){
        this.id = id;
        this.pwd = pwd;
        this.username = username;
        this.token = token;
        this.address = address;
        this.listening_port = listening_port;
    }

    public String getUsername(){
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token){ this.token = token;}

    public String getPwd(){
        return this.pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getAddress(){
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getListeningPort(){
        return this.listening_port;
    }

    public void setListeningPort(String listeningPort) {
        this.listening_port = listeningPort;
    }
}
