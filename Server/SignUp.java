package Server;

import java.sql.Connection;
import java.sql.*;

public class SignUp {
    final static String key="jojo";
    public static boolean insertUserPass(String username,String password) throws Exception {
        if(checkSameUser(username)) {
            PreparedStatement statement = SetupConnection.initializeDatabase("main").prepareStatement("insert into users values(?,?,?,?,?)");
            statement.setString(1, username);
            statement.setString(2, Security.encrypt(password,key));
            statement.setString(3,null);
            statement.setString(4,null);
            statement.setString(5,null);
            statement.executeUpdate();
            return true;
        }
        else {
            System.out.println("Try another username");
            return false;
        }
    }
    private static boolean checkSameUser(String username) throws SQLException {
        PreparedStatement statement= SetupConnection.initializeDatabase("main").prepareStatement("select username from users");
        ResultSet result= statement.executeQuery();
        for(int i =1 ;result.next();i++){
            if(username.equals(result.getString("username"))){
                return false;
            }

        }
        return true;

    }


}
class SetupConnection{
    private static String URL = "jdbc:mysql://127.0.0.1:3306/";
    private static String Name = "main";
    private static String Username = "root";
    private static String Password = "admin";

    protected static Connection initializeDatabase(String database) throws SQLException {
        Connection connection=  DriverManager.getConnection(URL+database,Username,Password);
        System.out.println("connected to database.");
        return connection;
    }

}
