package Server;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DataBase {
    //private Message message;
    private Date date;
    //private Connection connection= Server.SetupConnection.initializeDatabase("");
    private  String Key="jojo";
    private final String sender= "sender varchar(200)";
    private final String receiver ="receiver varchar(200)";
    private final String message ="message text";
    private final String fileAddress="fileAddress text";
    private final String dateAndTime="DateAndTime datetime";
    private final String members="members varchar(200)";
    private final String admins="admins varchar(200)";

    public DataBase() throws SQLException {
    }

    public  void saveChatsMessage(String database,String sender,String receiver ,String message,String fileAddress ) throws Exception {
        createChatsTable(sender,database);// if doesnt exist table creates it first
        date= new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        String query="insert into "+sender;
        PreparedStatement statement = SetupConnection.initializeDatabase(database).prepareStatement(query+" values(?,?,?,?,?)");
        statement.setString(1,Security.encrypt(sender,Key));
        statement.setString(2,Security.encrypt(receiver,Key));
        statement.setString(3,Security.encrypt(message,Key));

        statement.setString(4,fileAddress);
        statement.setString(5,ft.format(date));
        statement.executeUpdate();
        System.out.println("message saved");

    }
    public void saveMessage(String database,String name,String sender,String message,String fileAddress) throws Exception {
        date= new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String query="insert into "+name;
        PreparedStatement statement = SetupConnection.initializeDatabase(database).prepareStatement(query+" values(?,?,?,?)");
        statement.setString(1,Security.encrypt(sender,Key));
        statement.setString(2,Security.encrypt(message,Key));
        statement.setString(3,fileAddress);
        statement.setString(4,ft.format(date));
        statement.executeUpdate();

    }
    private void createChatsTable(String name,String database) throws SQLException {
        String query="create table if not exists "+ name +" ("+sender+
                ","+receiver+","+message+","+fileAddress+","+dateAndTime+")";

        PreparedStatement statement= SetupConnection.initializeDatabase(database).prepareStatement(query);

        statement.execute();
    }

    public void createGroupTable(String tableName, ArrayList<String> users) throws SQLException {
        String query="create table if not exists "+ tableName +" ("+sender+
                ","+message+","+fileAddress+","+dateAndTime+")";
        PreparedStatement statement= SetupConnection.initializeDatabase("groups").prepareStatement(query);
        statement.execute();/// create a table for message of groups
        createUsersTable(tableName,"groups");

        addUsers(users,tableName,"groups");

    }
    private void createUsersTable(String name , String type) throws SQLException {/// create a table for users of groups or channels
        String query="create table if not exists "+ name+"Users" +" ("+ members +")";
        PreparedStatement statement= SetupConnection.initializeDatabase(type).prepareStatement(query);
        statement.execute();

    }
    private void addUsers(ArrayList<String> users,String name,String type) throws SQLException {

        String query= "insert into "+name+ "users ";
        for(int i=0;i<users.size();i++){
            PreparedStatement statement = SetupConnection.initializeDatabase(type).prepareStatement(query+"VALUES (?)");
            statement.setString(1,users.get(i));
            statement.executeUpdate();
        }

    }

    public void createChannelTable(String channelName,ArrayList<String> users,ArrayList<String> admins) throws SQLException {
        String query="create table if not exists "+ channelName +" ("+sender+
                ","+message+","+fileAddress+","+dateAndTime+")";
        PreparedStatement statement= SetupConnection.initializeDatabase("channels").prepareStatement(query);
        statement.execute();/// create a table for message of channels
        createUsersTable(channelName,"channels");
        addUsers(users,channelName,"channels");
        createAdminsTable(channelName);
        addAdmins(admins,channelName);


    }
    private void createAdminsTable(String name) throws SQLException {
        String query="create table if not exists "+ name+"Admins" +" ("+ admins +")";
        PreparedStatement statement= SetupConnection.initializeDatabase("channels").prepareStatement(query);
        statement.execute();

    }
    private void addAdmins(ArrayList<String> admins,String name) throws SQLException {
        PreparedStatement statement;
        String query= "insert into "+name+ "admins ";
        for(int i=0;i<admins.size();i++){
            statement = SetupConnection.initializeDatabase("channels").prepareStatement(query+"VALUES (?)");
            statement.setString(1,admins.get(i));
            statement.executeUpdate();
        }
    }
    public void addGroups(ArrayList<String> users,String name) throws SQLException {
        PreparedStatement statement=SetupConnection.initializeDatabase("main").prepareStatement("update users set `groups`= concat(COALESCE(`groups`,''),',',?) where username=? and `groups`is not null ");
        statement.setString(1,name);
        for(String user:users){
            statement.setString(2,user);
            statement.executeUpdate();
        }

        PreparedStatement statement1=SetupConnection.initializeDatabase("main").prepareStatement("update users set `groups`= concat(COALESCE(`groups`,''),?) where username=? and `groups`is  null");
        statement1.setString(1,name);
        for(String user:users){
            statement1.setString(2,user);
            statement1.executeUpdate();
        }
    }
    public void addChannels(ArrayList<String> users,String name) throws SQLException {
        PreparedStatement statement=SetupConnection.initializeDatabase("main").prepareStatement("update users set `channels`= concat(COALESCE(`channels`,''),',',?) where username=? and `channels`is not null ");
        statement.setString(1,name);
        for(String user:users){
            statement.setString(2,user);
            statement.executeUpdate();
        }

        PreparedStatement statement1=SetupConnection.initializeDatabase("main").prepareStatement("update users set `channels`= concat(COALESCE(`channels`,''),?) where username=? and `channels`is  null");
        statement1.setString(1,name);
        for(String user:users){
            statement1.setString(2,user);
            statement1.executeUpdate();
        }
    }

    public void addPvUsers(String userName, String name) throws SQLException {
        PreparedStatement statement=SetupConnection.initializeDatabase("main").prepareStatement("update users set `chats`= concat(COALESCE(`chats`,''),?) where username=? and `chats`is null ");
        statement.setString(1,name);
        statement.setString(2,userName);
        statement.executeUpdate();
        PreparedStatement statement1=SetupConnection.initializeDatabase("main").prepareStatement("update users set `chats`= concat(COALESCE(`chats`,''),',',?) where username=? and `channels`is not null and `chats` not LIKE ? ");
        statement1.setString(1,name);
        statement1.setString(2,userName);
        statement1.setString(3,"%"+name+"%");
        statement1.executeUpdate();
    }

}
