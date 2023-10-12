package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

import Client.*;
import Client.PrimaryInfo;

class Server{
    private Socket socket;
    private ServerSocket server;
    static ArrayList<MultiUser> users= new ArrayList<>();
    public Server(int port) throws IOException {
        this.server= new ServerSocket(port);
    }

    public void acceptClient() throws IOException, SQLException {

        while (true){
            socket=server.accept();
            System.out.println("client accepted.");
            MultiUser user = new MultiUser(socket);
            Thread t = new Thread(user);
            users.add(user);
            t.start();
            System.out.println("server is ready");

        }

    }
    public static void main(String[] args) throws IOException, SQLException {
        Server server= new Server(8885);
        server.acceptClient();
    }


}
class MultiUser extends Thread {
    private Socket socket;
    private ObjectInputStream objectIn;
    private ObjectOutputStream objectOut;
    String userName;
    private int ID;
    boolean isOnline = false;
    DataBase dataBase = new DataBase();

    public MultiUser(Socket socket) throws IOException, SQLException {
        this.socket = socket;
        this.objectOut = new ObjectOutputStream(socket.getOutputStream());
        this.objectIn = new ObjectInputStream(socket.getInputStream());

    }
    public Object receiver() throws IOException, ClassNotFoundException {
        //ObjectInputStream objectIn= new ObjectInputStream(socket.getInputStream());
        Object object= objectIn.readObject();
        System.out.println("object received");
        return object;
    }
    public void sender(Message message) throws IOException {
        for(MultiUser user: Server.users){
            if(user.userName.equals(message.receiver) && user.isOnline){
                user.objectOut.writeObject(message);
                user.objectOut.flush();
                System.out.println("message sent to "+user.userName);
            }
        }
    }
    private void gpSender(Message message, String type) throws SQLException, IOException {
        PrimaryData primaryData= new PrimaryData();
        String Name=message.getReceiverName();
        for(String member : primaryData.getMembers(Name,type)){
            for(MultiUser user : Server.users){
                if(user.userName.equals(member) && user.isOnline && !user.userName.equals(message.sender)){
                    user.objectOut.writeObject(message);
                    user.objectOut.flush();
                    System.out.println("message sent to "+user.userName);
                }
            }
        }
    }

    public ObjectOutputStream getObjectOut() throws IOException {
        ObjectOutputStream objectOut= new ObjectOutputStream(socket.getOutputStream());
        return objectOut;
    }
    public boolean login() throws Exception {
        Login login= new Login();
        User user = (User) receiver();
        if(user.getAction().equals("login")){
            boolean flag=true;
            while (flag && user.getAction().equals("login")){

                if(login.login(user.getUserName(), user.getPassword())){
                    //ObjectOutputStream objectOut= new ObjectOutputStream(socket.getOutputStream());
                    objectOut.writeObject(true);
                    objectOut.flush();
                    userName=user.getUserName();// assign the username to thread
                    isOnline=true;
                    return true;
                }
                else {
                    //ObjectOutputStream objectOut= new ObjectOutputStream(socket.getOutputStream());
                    objectOut.writeObject(false);
                    objectOut.flush();
                    user= (User) receiver();
                }
            }
        }
        else if(user.getAction().equals("signUp")) {
            signUp(user);
            return true;

        }
        return false;

    }
    private void signUp(User user) throws Exception {

        while (user.getAction().equals("signUp")){

            if (SignUp.insertUserPass(user.getUserName(),user.getPassword())){
                //ObjectOutputStream objectOut= new ObjectOutputStream(socket.getOutputStream());
                    objectOut.writeObject(true);
                    objectOut.flush();
                    userName=user.getUserName();// assign the username to thread
                    isOnline=true;
                    return;
            }
            else {
                //ObjectOutputStream objectOut= new ObjectOutputStream(socket.getOutputStream());
                    objectOut.writeObject(false);
                    objectOut.flush();
                    user= (User) receiver();
                }
            }

    }
    private void setStatus(String userName) throws IOException {
        for(MultiUser user:Server.users){
            if(user.userName.equals(userName)){
                //ObjectOutputStream objectOut= new ObjectOutputStream(socket.getOutputStream());
                objectOut.writeObject(true);// send a true or false flag to declare user logged in.
                objectOut.flush();
                System.out.println("flag sent "+true);
                return;
            }
        }
     //   ObjectOutputStream objectOut= new ObjectOutputStream(socket.getOutputStream());
        objectOut.writeObject(false);
        objectOut.flush();
        System.out.println("flag sent"+ false);
    }
    private void sendPrimaryInfo(String userName) throws Exception {
   //     ObjectOutputStream objectOut= new ObjectOutputStream(socket.getOutputStream());
        PrimaryData primaryData= new PrimaryData();
        PrimaryInfo primaryInfo=primaryData.exportPrimaryInfo(userName);
       // PrimaryInfo primaryInfo = primaryData.exportPrimaryInfo(userName);
        objectOut.writeObject(primaryInfo);
        objectOut.flush();
        System.out.println("primary info was sent");

    }
    private void saveOnDB(Message message) throws Exception {
        if(message.type.equals("groups") ){
            dataBase.saveMessage("groups",message.receiver,message.sender,message.message,message.fileAddress);
        }
        else if(message.type.equals("channels")){
            dataBase.saveMessage("channels",message.receiver,message.sender,message.message,message.fileAddress);
        }
        else if(message.type.equals("private")){
            dataBase.saveChatsMessage("chats",message.sender,message.receiver,message.message,message.fileAddress);
            dataBase.addPvUsers(message.sender,message.receiver);// save users to chats column in users table
        }

    }


    @Override
    public void run() {
       boolean flag= false;
        try {
            flag = login();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(flag){
            try {
                sendPrimaryInfo(userName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(MultiUser user1:Server.users){
            System.out.print(user1.userName);
            System.out.print("  ");
        }
        System.out.println("go to messaging");
        while(flag){
            try {

                Object order= receiver();
                if(order instanceof String){
                    setStatus((String) order);
                    //ObjectOutputStream objectOut= new ObjectOutputStream(socket.getOutputStream());
                }
                else if(order instanceof Message){
                    Message message = (Message) order;
                    System.out.println("rec"+message.receiver);
                    System.out.println("send"+message.sender);
                    if(message.type.equals("groups")){
                        gpSender(message,"groups");
                    }else if(message.type.equals("channels")){
                        gpSender(message,"channels");
                    }
                    else {
                        sender(message);//send message to receiver
                    }
                    saveOnDB(message);// save message in db due to type of it

                }
                else if(order instanceof Group){
                    Group group= (Group) order;
                    System.out.println(group.getMembers());

                    dataBase.createGroupTable(group.getGroupName(),group.getMembers());// create table in db for groups
                    dataBase.addGroups(group.getMembers(),group.getGroupName());// save group name to users table for each one
                    System.out.println("gp info saved");
                }
                else if(order instanceof Channel){
                    Channel channel= (Channel) order;
                    dataBase.createChannelTable(channel.getChannelName(),channel.getMembers(),channel.getAdmins());// create table in db for
                    dataBase.addChannels(channel.getMembers(),channel.getChannelName());//save channel name to users table for each one
                }


            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            objectOut.close();
            objectIn.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}