package UI;

import Client.ChatManager;
import Client.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
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

public class channelWithAdminController implements Initializable {

    @FXML public TextFlow channelAdminTextFlow;
    @FXML public TextField channelMessage;
    @FXML public Label channelAdminName;
    @FXML public ComboBox<String> channelAdmins;
    @FXML public ComboBox<String> channelMembers;

    ObservableList<String> channelMemberList = FXCollections.observableArrayList();
    ObservableList<String> adminList = FXCollections.observableArrayList();
    ChatManager chatManager = new ChatManager(Client.getSocket());
    File file = null;
    threadHandler threadHandler;

    public channelWithAdminController() throws IOException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(mainPageController.CHU == 1){
            channelAdminName.setText(mainPageController.previousChName);
            for (Channel channel : controller.pi.getChannels()){
                if(channel.getChannelName().equals(mainPageController.previousChName)){
                    channelMemberList.addAll(channel.getMembers());
                    adminList.addAll(channel.getAdmins());
                    displayOldMessage(channel.getMessages(),channelAdminTextFlow);
                }
            }
            channelMembers.setItems(channelMemberList);
            channelAdmins.setItems(adminList);
        }
        else {
            channelAdminName.setText(makeChannelController.nameOfChannel);
            channelMembers.setItems(makeChannelController.channelMemberList);
            channelAdmins.setItems(makeChannelController.adminList);
            ArrayList<String> members = new ArrayList<>(makeChannelController.channelMemberList);
            ArrayList<String> admins = new ArrayList<>(makeChannelController.adminList);
            Channel channel = new Channel();
            channel.setChannelName(makeChannelController.nameOfChannel);
            channel.setAdmins(admins);
            channel.setMembers(members);
            try {
                chatManager.sendNewChannel(channel);
            } catch (IOException e) {
                e.printStackTrace();
            }
            controller.pi.getChannels().add(channel);
        }
        threadHandler = new threadHandler(Client.getSocket(),channelAdminTextFlow);
        threadHandler.start();
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
        adminList.clear();
        channelMemberList.clear();
    }

    public void sendMessageInChannel(MouseEvent event)throws IOException{
       sendMessage(channelAdminTextFlow,channelMessage,channelAdminName.getText());
    }

    public void sendMessageInChannelWithKey(KeyEvent event)throws IOException{
        if(event.getCode() == KeyCode.ENTER){
            sendMessage(channelAdminTextFlow,channelMessage,channelAdminName.getText());
        }
    }

    public void sendFileInChannel(MouseEvent event)throws IOException{
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        if(file != null){
            channelMessage.setText("");
            channelMessage.setPromptText("file name");
        }
    }

    public void displayOldMessage (ArrayList<Message> messages , TextFlow textFlow){
        for (Message message : messages) {
            if (message.getSenderName().equals(Authenticate.getUsername())) {
                if (!message.getFilePath().equals("")) {
                    File file1 = new File(message.getFilePath());
                    String fileFormat = file1.getName().toLowerCase();
                    if (photoInChannel(fileFormat)) {
                        displayPicFile(file1, channelAdminName.getText()+" (Me) ", textFlow);
                    } else {
                        Text text = new Text(channelAdminName.getText() + " (Me) : Send a file!\n");
                        textFlow.getChildren().add(text);
                    }
                } else {
                    Text text = new Text(channelAdminName.getText() + " (Me) : " + message.getText() + "\n");
                    textFlow.getChildren().add(text);
                }
            } else {
                if (!message.getFilePath().equals("")) {
                    File file1 = new File(message.getFilePath());
                    String fileFormat = file1.getName().toLowerCase();
                    if (photoInChannel(fileFormat)) {
                        displayPicFile(file1, channelAdminName.getText() + " (" +message.getSenderName() + ")", textFlow);
                    } else {
                        Text text = new Text(channelAdminName.getText() + " (" + message.getSenderName() + ") : Send a file!\n");
                        textFlow.getChildren().add(text);
                    }
                } else {
                    Text text = new Text(channelAdminName.getText() + " (" + message.getSenderName() + ") : " + message.getText() + "\n");
                    textFlow.getChildren().add(text);
                }
            }
        }
    }

    public void sendMessage (TextFlow textFlow , TextField textField , String name) throws IOException {
        if (file == null && !textField.getText().isEmpty()) {
            Text text = new Text(name + " (Me) : " + textField.getText() + "\n");
            textFlow.getChildren().add(text);
            Message message = new Message(Authenticate.getUsername(), name, "channels", textField.getText());
            textField.setText("");
            textField.setPromptText("type a message");
            chatManager.sendMessage(message);
        } else if (file != null && !textField.getText().isEmpty()) {
            String fileFormat = file.getName().toLowerCase();
            if (photoInChannel(fileFormat)){
                displayPicFile(file, name + " (Me)" , textFlow);
            } else {
                Text text = new Text(name + " (Me) : Send a file!" + "\n");
                textFlow.getChildren().add(text);
            }
            Message message = new Message(Authenticate.getUsername(), name, "channels", textField.getText(), file.getAbsolutePath());
            textField.setText("");
            textField.setPromptText("type a message");
            file = null;
            chatManager.sendMessage(message);
        }
    }

    public void displayPicFile(File file , String name , TextFlow textFlow){
        File imageFile = new File(file.getAbsolutePath());
        Image image = new Image(imageFile.toURI().toString());
        ImageView iv = new ImageView(image);
        iv.setFitHeight(60);
        iv.setFitWidth(60);
        Text lineSpace = new Text("\n");
        Text username = new Text(name + " : ");
        textFlow.getChildren().addAll(username, iv, lineSpace);
    }

    public Boolean photoInChannel(String format){
        if (format.contains(".png") || format.contains(".jpeg") || format.contains(".jpg") || format.contains(".gif") || format.contains(".raw") || format.contains(".tiff") || format.contains(".psd")){
            return true;
        }
        return false;
    }
}
