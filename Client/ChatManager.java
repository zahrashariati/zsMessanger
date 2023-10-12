package Client;

import java.io.*;
import java.net.Socket;

public class ChatManager {
    private Socket socket;
    private Message message;
    private ObjectInputStream objectIn = Client.getObjectIn();
    private ObjectOutputStream objectOut = Client.getObjectOut();

    public ChatManager(Socket socket) throws IOException {
        this.socket = socket;
    }

    public boolean getContactStatus(String username) throws IOException, ClassNotFoundException {
        //ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
        objectOut.writeObject(username);
        objectOut.flush();
        System.out.println("sent status");
        //ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
        boolean answer =(Boolean) objectIn.readObject();
        return answer;
    }

    public void sendNewGroup(Group group) throws IOException {
        //ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
        objectOut.writeObject(group);
        objectOut.flush();
    }

    public void sendNewChannel(Channel channel) throws IOException {
        //ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
        objectOut.writeObject(channel);
        objectOut.flush();
    }

    public void sendMessage(Message m) throws IOException {
        //ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
        objectOut.writeObject(m);
        objectOut.flush();
    }

    /*public Message receiveMessage() throws IOException, ClassNotFoundException {
        ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
        message = (Message) is.readObject();
        if (!message.getFilePath().equals("")){
            saveFile(message.getFile() , message.getFileName());
        }
        return message;
    }

    private void saveFile(File file, String fileName) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(message.getFile()));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(message.getFileName()));
        byte[] b = new byte[(int) message.getFile().length()];
        int counter;
        while ((counter = bis.read(b)) > 0){
            bos.write(b,0,counter);
        }
        bos.flush();
        bos.close();
        bis.close();
    }*/
}
