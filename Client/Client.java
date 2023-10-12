package Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private static Socket socket;
    public static ObjectInputStream objectIn;
    public static ObjectOutputStream objectOut;

    public static ObjectInputStream getObjectIn() {
        return objectIn;
    }

    public static ObjectOutputStream getObjectOut() {
        return objectOut;
    }

    static {
        try {
            socket = new Socket("localhost",8885);
            objectIn= new ObjectInputStream(socket.getInputStream());
            objectOut= new ObjectOutputStream(socket.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Socket getSocket() {
        return socket;
    }
}
