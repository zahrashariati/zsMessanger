package Server;

import Client.*;

import java.sql.*;
import java.util.ArrayList;

public class PrimaryData {

    private String key="jojo";

    private ArrayList<String> getRegisteredUsers() throws SQLException {
        ArrayList<String> registeredUsers= new ArrayList<>();
        PreparedStatement statement= SetupConnection.initializeDatabase("main").prepareStatement("select username from users ");
        ResultSet result= statement.executeQuery();
        for(int i=1;result.next();i++){
            registeredUsers.add(result.getString("username"));
        }
        return registeredUsers;
    }
   /* private ArrayList<Message> getPrivateMessages(String userName) throws Exception {
        ArrayList<Message> messages= new ArrayList<>();
        PreparedStatement statement = Server.SetupConnection.initializeDatabase("chats").prepareStatement("select * from "+userName);

        ResultSet result= statement.executeQuery();

        for(int i=1;result.next();i++){
                Message message = new Message();
                message.sender=Server.Security.decrypt(result.getString("sender"),key);
                message.receiver=Server.Security.decrypt(result.getString("receiver"),key);
                message.message=Server.Security.decrypt(result.getString("message"),key);
                message.fileAddress=result.getString("fileAddress");
                message.date=result.getDate("DateAndTime");
                messages.add(message);
        }
        return messages;

    }*/
    private ArrayList<Message> getMessages(String Name,String type) throws Exception {
        ArrayList<Message> messages= new ArrayList<>();
        PreparedStatement statement = SetupConnection.initializeDatabase(type).prepareStatement("select * from "+Name);

        ResultSet result= statement.executeQuery();

        for(int i=1;result.next();i++){
            Message message = new Message();
            message.sender=Security.decrypt(result.getString("sender"),key);

            message.message=Security.decrypt(result.getString("message"),key);
            message.fileAddress=result.getString("fileAddress");
            message.date=result.getDate("DateAndTime");
            messages.add(message);
        }
        return messages;

    } // this method get message from channels and groups
    public ArrayList<String> getMembers(String Name, String type) throws SQLException {
        ArrayList<String> members= new ArrayList<>();
        PreparedStatement statement= SetupConnection.initializeDatabase(type).prepareStatement("select members from "+Name+"Users");
        ResultSet result= statement.executeQuery();
        for (int i=1;result.next();i++){
            members.add(result.getString("members"));
        }
        return members;
    }
    private ArrayList<String> getAdmins(String channelName) throws SQLException {
        ArrayList<String> admins= new ArrayList<>();
        PreparedStatement statement= SetupConnection.initializeDatabase("channels").prepareStatement("select admins from "+channelName+"Admins");
        ResultSet result= statement.executeQuery();
        for (int i=1;result.next();i++){
            admins.add(result.getString("admins"));
        }
        return admins;
    }
    private ArrayList<Group> getGroups(String[] groupsName) throws Exception {
        ArrayList<Group> groups= new ArrayList<>();
        for(int i=0;i<groupsName.length ;i++){
            if(groupsName[i]!=null){
                Group group= new Group();
                group.setGroupName(groupsName[i]);
                group.setMembers(getMembers(groupsName[i],"groups"));
                group.setMessages(getMessages(groupsName[i],"groups"));
                groups.add(group);
            }

        }
        return groups;
    }
    private String[] setUserGroups(String userName) throws SQLException {
        String[] groups = new String[100];
        PreparedStatement statement= SetupConnection.initializeDatabase("main").prepareStatement("select * from users");
        ResultSet groupResult= statement.executeQuery();
        for(int i=1;groupResult.next();i++){
            if(userName.equals(groupResult.getString("username")) && groupResult.getString("groups") != null){
                groups= groupResult.getString("groups").split(",");
            }
        }
        return groups;

    }
    private String[] setUserChannels(String userName) throws SQLException {
        String[] channels = new String[100];
        PreparedStatement statement= SetupConnection.initializeDatabase("main").prepareStatement("select * from users");
        ResultSet groupResult= statement.executeQuery();
        for(int i=1;groupResult.next();i++){
            if(userName.equals(groupResult.getString("username")) && groupResult.getString("channels")!= null ){
                channels= groupResult.getString("channels").split(",");
            }
        }
        return channels;
    }
    private ArrayList<Channel> getChannels(String[] channelsName) throws Exception {
        ArrayList<Channel> channels= new ArrayList<>();
        for(int i=0;i<channelsName.length;i++){
            if(channelsName[i]!=null){
                Channel channel= new Channel();
                channel.setChannelName(channelsName[i]);
                channel.setMembers(getMembers(channelsName[i],"channels"));
                channel.setAdmins(getAdmins(channelsName[i]));
                channel.setMessages(getMessages(channelsName[i],"channels"));
                channels.add(channel);
            }
        }
        return channels;
    }
    private String[] setUserPrivateChats(String userName) throws SQLException {
        String[] users = new String[100];
        PreparedStatement statement= SetupConnection.initializeDatabase("main").prepareStatement("select * from users");
        ResultSet groupResult= statement.executeQuery();
        for(int i=1;groupResult.next();i++){
            if(userName.equals(groupResult.getString("username"))  && groupResult.getString("chats")!= null ){
                users= groupResult.getString("chats").split(",");
            }
        }
        return users;
    }
    private ArrayList<PrivateChat> getPrivateChat(String userName,String[] contacts) throws Exception {
        ArrayList<PrivateChat> privateChats = new ArrayList<>();

        for (int i = 0; i < contacts.length; i++) {
            if (contacts[i] != null) {
            PrivateChat privateChat = new PrivateChat();
            privateChat.setContactName(contacts[i]);
            PreparedStatement statement = SetupConnection.initializeDatabase("chats").prepareStatement("select * from " + userName);
            ResultSet result = statement.executeQuery();
            ArrayList<Message> messages = new ArrayList<>();
            for (int j = 1; result.next(); j++) {
                if (contacts[i].equals(Security.decrypt(result.getString("receiver"), key))) {
                    Message message = new Message();
                    message.sender = Security.decrypt(result.getString("sender"), key);
                    message.receiver = Security.decrypt(result.getString("receiver"), key);
                    message.message = Security.decrypt(result.getString("message"), key);
                    message.fileAddress = result.getString("fileAddress");
                    message.date = result.getDate("DateAndTime");
                    messages.add(message);
                }

            }
            privateChat.setMessages(messages);
            privateChats.add(privateChat);

        }
        }
        return privateChats;
    }
    public PrimaryInfo exportPrimaryInfo(String userName) throws Exception {
        PrimaryInfo primaryInfo= new PrimaryInfo();
        primaryInfo.setRegisteredUserNames(getRegisteredUsers());
        primaryInfo.setGroups(getGroups(setUserGroups(userName)));
        primaryInfo.setChannels(getChannels(setUserChannels(userName)));
        primaryInfo.setPrivateChats(getPrivateChat(userName,setUserPrivateChats(userName)));

        return primaryInfo;
    }


}
