package UI;

import Client.ChatManager;
import Client.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import Client.*;

public class pvChatController implements Initializable {

    @FXML public TextField pvMessage;
    @FXML public Label onlineOffline;
    @FXML public Label username;
    @FXML public TextFlow pvTextFlow;

    ChatManager chatManager = new ChatManager(Client.getSocket());
    File file = null;
    threadHandler threadHandler = null;

    public pvChatController() throws IOException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(mainPageController.PVU == 1){
            username.setText(mainPageController.previousPvUsername);
            try {
                if (chatManager.getContactStatus(mainPageController.previousPvUsername)) {
                    onlineOffline.setText("Online");
                } else {
                    onlineOffline.setText("Offline");
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            for (PrivateChat privateChat : controller.pi.getPrivateChats()) {
                if (mainPageController.previousPvUsername.equals(privateChat.getContactName())) {
                    displayOldMessage(privateChat.getMessages() , pvTextFlow);
                }
            }
        }
        else {
            username.setText(mainPageController.newPVChatUsername);
            PrivateChat privateChat=new PrivateChat();
            privateChat.setContactName(mainPageController.newPVChatUsername);
            try {
                if (chatManager.getContactStatus(mainPageController.newPVChatUsername)) {
                    onlineOffline.setText("Online");
                } else {
                    onlineOffline.setText("Offline");
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            controller.pi.getPrivateChats().add(privateChat);
        }
        threadHandler = new threadHandler(Client.getSocket(),pvTextFlow);
        threadHandler.start();
    }

    public void sendMessageInPv(MouseEvent event)throws IOException{
        sendMessage(pvTextFlow , pvMessage , username.getText());
    }

    public void sendMessageInPvWithKey(KeyEvent event)throws IOException{
        if(event.getCode() == KeyCode.ENTER){
            sendMessage(pvTextFlow , pvMessage , username.getText());
        }
    }

    public void sendFileInPv(MouseEvent event)throws IOException{
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            pvMessage.setText("");
            pvMessage.setPromptText("file name");
        }
    }

    public void back(MouseEvent event)throws IOException{
        Stage primaryStage = new Stage();
        File file5 = new File("src", "UI/mainMenu.fxml");
        Parent root = FXMLLoader.load(file5.toURI().toURL());
        Scene scene = new Scene(root);
        scene.getStylesheets().add("UI/style.css");
        primaryStage.setScene(scene);
        main.primaryStage2.close();
        main.primaryStage2 = primaryStage;
        primaryStage.setResizable(false);
        primaryStage.show();
        threadHandler.interrupt();
    }

    public void displayOldMessage (ArrayList<Message> messages , TextFlow textFlow){
        for (Message message : messages) {
            if (message.getSenderName().equals(Authenticate.getUsername())) {
                if (!message.getFilePath().equals("")) {
                    File file1 = new File(message.getFilePath());
                    String fileFormat = file1.getName().toLowerCase();
                    if (photo(fileFormat)){
                        displayPicFile(file1, "Me", textFlow);
                    } else {
                        Text text = new Text("Me : Send a file!\n");
                        textFlow.getChildren().add(text);
                    }
                } else {
                    Text text = new Text("Me : " + message.getText() + "\n");
                    textFlow.getChildren().add(text);
                }
            } else {
                if (!message.getFilePath().equals("")) {
                    File file1 = new File(message.getFilePath());
                    String fileFormat = file1.getName().toLowerCase();
                    if (photo(fileFormat)) {
                        displayPicFile(file1, message.getSenderName(), textFlow);
                    } else {
                        Text text = new Text(message.getSenderName() + " : Send a file!\n");
                        textFlow.getChildren().add(text);
                    }
                } else {
                    Text text = new Text(message.getSenderName() + " : " + message.getText() + "\n");
                    textFlow.getChildren().add(text);
                }
            }
        }
    }

    public void sendMessage(TextFlow textFlow , TextField textField , String name) throws IOException {
        if (file == null && !textField.getText().isEmpty()) {
            Text text = new Text("Me : " + textField.getText() + "\n");
            textFlow.getChildren().add(text);
            Message message = new Message(Authenticate.getUsername(), name, "private", textField.getText());
            textField.setText("");
            textField.setPromptText("type a message");
            chatManager.sendMessage(message);
        } else if (file != null && !textField.getText().isEmpty()) {
            String fileFormat = file.getName().toLowerCase();
            if(photo(fileFormat)){
                displayPicFile(file, "Me" , textFlow);
            } else {
                Text text = new Text("Me : Send a file!" + "\n");
                textFlow.getChildren().add(text);
            }
            Message message = new Message(Authenticate.getUsername(), name, "private", textField.getText(), file.getAbsolutePath());
            textField.setText("");
            textField.setPromptText("type a message");
            file = null;
            chatManager.sendMessage(message);
        }
    }

    public static void displayPicFile(File file, String name, TextFlow textFlow){
        File imageFile = new File(file.getAbsolutePath());
        Image image = new Image(imageFile.toURI().toString());
        ImageView iv = new ImageView(image);
        iv.setFitHeight(60);
        iv.setFitWidth(60);
        Text lineSpace = new Text("\n");
        Text username = new Text(name + " : ");
        textFlow.getChildren().addAll(username, iv, lineSpace);
    }

    public Boolean photo(String format){
        if (format.contains(".png") || format.contains(".jpeg") || format.contains(".jpg") || format.contains(".gif") || format.contains(".raw") || format.contains(".tiff") || format.contains(".psd")){
            return true;
        }
        return false;
    }
}
