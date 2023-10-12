package Server;

import java.sql.*;

public class Login {

    private  Connection connection= SetupConnection.initializeDatabase("main");
    final static String key="jojo";
    public Login() throws SQLException {}

    public boolean login(String username,String password) throws Exception {
        PreparedStatement statement= connection.prepareStatement("select * from users where username=?");
        statement.setString(1,username);
        ResultSet result= statement.executeQuery();
       /* if(result.wasNull()){
            return false;
        }*/
        String pass = null;
        while (result.next()) {
            pass=Security.decrypt(result.getString(2),key);
        }
        if(pass.equals(password)){
            return true;
        }
        return false;
    }

}
