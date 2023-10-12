package Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Authenticate {
    private Socket socket;
    private static String username;
    private ObjectInputStream objectIn = Client.getObjectIn();
    private ObjectOutputStream objectOut = Client.getObjectOut();


    public Authenticate(Socket socket) throws IOException {
        this.socket = socket;
    }

    public PrimaryInfo getPrimaryInfo() throws IOException, ClassNotFoundException {
        //ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
        //Object object= is.readObject();
        PrimaryInfo primaryInfo = (PrimaryInfo) objectIn.readObject();
        System.out.println(primaryInfo.getRegisteredUserNames());
        //PrimaryInfo primaryInfo = (PrimaryInfo)object;
        return primaryInfo;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        Authenticate.username = username;
    }


    public boolean checkInputsMatch(String user,String pass) throws IOException, ClassNotFoundException {
        User user1 = new User("login",user,pass);
        setUsername(user);
        //ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
        objectOut.writeObject(user1);
        objectOut.flush();
        System.out.println("sent");
        //ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
        boolean answer = (Boolean)objectIn.readObject();

        System.out.println(answer);
        return answer;
    }

    public boolean saveNewUser(String user, String pass) throws IOException, ClassNotFoundException {
        User user1 = new User("signUp",user,pass);
        setUsername(user);
        //ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
        objectOut.writeObject(user1);
        objectOut.flush();
        System.out.println(user);
        //ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
        boolean answer = (Boolean) objectIn.readObject();
        System.out.println(answer);
        return answer;
    }

//    public void register(String username,String password) throws IOException {
//        ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
//        user user1= new user("unknown",username,password);
//        os.writeObject(user1);
//        socket.close();
//    }
//    public void checkUsername
//    public void login(String username,String password) throws IOException {
//        Socket socket = new Socket(address,port);
//        ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
//        ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
//        user user2 = new user("known",username,password);
//        os.writeObject(user2);
//        String answer = is.readUTF();
//        if(answer.equals("true")){
//            // TODO show next scene
//
//        }else{
//            // TODO print invalid! on the label
//        }
//        socket.close();
//    }
}
