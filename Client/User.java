package Client;

import java.io.Serializable;

public class User implements Serializable {

    private String action;
    private String userName;
    private String password;

    public User(String action,String userName, String password) {
        this.action = action;
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}


