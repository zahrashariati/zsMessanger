package Client;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import UI.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class newReceiver {
    public static void receive(Socket socket, TextFlow textFlow) throws IOException, ClassNotFoundException {
        Message message= new Message();
        //while (true){
            Object obj= Client.objectIn.readObject();
            message = (Message) obj;
            storeMessages(message);
            fileManager(message);
            displayMessage(message,textFlow);
        //}
    }

    private static void storeMessages(Message message){
        if(message.getType().equals("private")){
            boolean discovered = false;
            ArrayList <PrivateChat> privateChats = controller.pi.getPrivateChats();
            for (PrivateChat pc: privateChats){
                if (pc.getContactName().equals(message.getSenderName()) ){
                    pc.getMessages().add(message);
                    discovered = true;
                }
            }
            if(!discovered){
                ArrayList <Message> messageArrayList = new ArrayList<>();
                messageArrayList.add(message);
                privateChats.add(new PrivateChat(message.getSenderName(),messageArrayList));
            }

        }else if(message.getType().equals("groups")){
            ArrayList <Group> groups = controller.pi.getGroups();
            for (Group group : groups){
                if(group.getGroupName().equals(message.getReceiverName())){
                    group.getMessages().add(message);
                }
            }

        }else if(message.getType().equals("channels")){
            ArrayList <Channel> channels = controller.pi.getChannels();
            for (Channel channel : channels){
                if (channel.getChannelName().equals(message.getReceiverName())){
                    channel.getMessages().add(message);
                }
            }

        }
    }

    public static void fileManager(Message message) throws IOException {
        if (!message.getFilePath().equals("")){
            saveFile(message);
        }
    }

    private static void saveFile(Message message) throws IOException {
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
    }

    private static void displayMessage(Message message, TextFlow textFlow) {
        if (message.getSenderName().equals(mainPageController.previousPvUsername) || message.getSenderName().equals(mainPageController.newPVChatUsername) || message.getReceiverName().equals(mainPageController.previousGPName) || message.getReceiverName().equals(makeGroupController.nameOfGroup)) {
            if (!message.getFilePath().equals("")) {
                File file1 = new File(message.getFilePath());
                String fileFormat = file1.getName().toLowerCase();
                if (fileFormat.contains(".png") || fileFormat.contains(".jpeg") || fileFormat.contains(".jpg") || fileFormat.contains(".gif") || fileFormat.contains(".raw") || fileFormat.contains(".tiff") || fileFormat.contains(".psd")) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            displayPicFile(file1, message.getSenderName(), textFlow);
                        }
                    });
                } else{
                    Text text = new Text(message.getSenderName() + " : Send a file!\n");
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            textFlow.getChildren().add(text);
                        }
                    });
                    // textFlow.getChildren().add(text);
                }
            } else {
                Text text = new Text(message.getSenderName() + " : " + message.getText() + "\n");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        textFlow.getChildren().add(text);
                    }
                });
                // textFlow.getChildren().add(text);
            }
        } else if (message.getReceiverName().equals(mainPageController.previousChName) || message.getReceiverName().equals(makeChannelController.nameOfChannel)) {
            if (!message.getFilePath().equals("")) {
                File file1 = new File(message.getFilePath());
                String fileFormat = file1.getName().toLowerCase();
                if (fileFormat.contains(".png") || fileFormat.contains(".jpeg") || fileFormat.contains(".jpg") || fileFormat.contains(".gif") || fileFormat.contains(".raw") || fileFormat.contains(".tiff") || fileFormat.contains(".psd")) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            displayPicFile(file1, message.getReceiverName() + " (" + message.getSenderName() + ")" , textFlow);
                        }
                    });
                } else{
                    Text text = new Text(message.getReceiverName() + " (" + message.getSenderName() + ") : Send a file!\n");
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            textFlow.getChildren().add(text);
                        }
                    });
                    // textFlow.getChildren().add(text);
                }
            } else {
                Text text = new Text(message.getReceiverName() + " (" + message.getSenderName() + ") : " + message.getText() + "\n");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        textFlow.getChildren().add(text);
                    }
                });
                //textFlow.getChildren().add(text);
            }
        }
    }

    public static void displayPicFile(File file, String name, TextFlow textFlow){
        File imageFile = new File(file.getAbsolutePath());
        Image image = new Image(imageFile.toURI().toString());
        ImageView iv = new ImageView(image);
        iv.setFitHeight(100);
        iv.setFitWidth(100);
        Text lineSpace = new Text("\n");
        Text username = new Text(name + " : ");
        textFlow.getChildren().addAll(username, iv, lineSpace);
    }
}
