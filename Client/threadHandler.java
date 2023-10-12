package Client;

import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class threadHandler extends Thread{
    Socket socket;
    TextFlow textFlow;
    private Lock lock;

    public threadHandler(Socket socket,TextFlow textFlow) {
        this.socket = socket;
        this.textFlow = textFlow;
        lock = new ReentrantLock();
    }

    @Override
    public void run() {

        while (socket.isConnected() && !Thread.interrupted()) {
           lock.lock();
            try {
               newReceiver.receive(socket, textFlow);

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
           }
            finally {
                lock.unlock();
           }
        }
    }
}
