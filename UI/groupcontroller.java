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

public class groupcontroller implements Initializable {

    @FXML public TextFlow groupTextFlow;
    @FXML public TextField groupMessage;
    @FXML public Label groupName;
    @FXML public ComboBox<String> groupMembers;

    ObservableList<String> groupMemberList = FXCollections.observableArrayList();
    ChatManager chatManager = new ChatManager(Client.getSocket());
    File file = null;
    threadHandler threadHandler;

    public groupcontroller() throws IOException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(mainPageController.GPU == 1){
            groupName.setText(mainPageController.previousGPName);
            for(Group group : controller.pi.getGroups()){
                if(group.getGroupName().equals(mainPageController.previousGPName)){
                    groupMemberList.addAll(group.getMembers());
                    displayOldMessage(group.getMessages(),groupTextFlow);
                }
            }
            groupMembers.setItems(groupMemberList);
        }
        else {
            groupName.setText(makeGroupController.nameOfGroup);
            groupMembers.setItems(makeGroupController.groupMemberList);
            ArrayList<String> members = new ArrayList<>();
            for (String member : makeGroupController.groupMemberList){
                members.add(member);
            }
            Group group = new Group();
            group.setGroupName(makeGroupController.nameOfGroup);
            group.setMembers(members);
            try {
                chatManager.sendNewGroup(group);
            } catch (IOException e) {
                e.printStackTrace();
            }
            controller.pi.getGroups().add(group);
        }
        threadHandler = new threadHandler(Client.getSocket(),groupTextFlow);
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
        groupMemberList.clear();
    }

    public void sendMessageInGroup(MouseEvent event)throws IOException{
        sendMessage(groupTextFlow,groupMessage,groupName.getText());
//        if(!groupMessage.getText().isEmpty()) {
//            Text text = new Text("username : " + groupMessage.getText() + "\n\n");
//            groupTextFlow.getChildren().add(text);
//            groupMessage.setText("");
//            groupMessage.setPromptText("type a message");
//        }
    }

    public void sendMessageInGroupWithKey(KeyEvent event)throws IOException{
        if(event.getCode() == KeyCode.ENTER){
            sendMessage(groupTextFlow,groupMessage,groupName.getText());
//            if(!groupMessage.getText().isEmpty()) {
//                Text text = new Text("username : " + groupMessage.getText() + "\n\n");
//                groupTextFlow.getChildren().add(text);
//                groupMessage.setText("");
//                groupMessage.setPromptText("type a message");
//            }
        }
    }

    public void sendFileInGroup(MouseEvent event)throws IOException{
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        if(file != null){
            groupMessage.setText("");
            groupMessage.setPromptText("file name");
//            String fileFormat = file.getName().toLowerCase();
//            if(fileFormat.contains(".png") || fileFormat.contains(".jpeg") || fileFormat.contains(".jpg") || fileFormat.contains(".gif") || fileFormat.contains(".raw") || fileFormat.contains(".tiff") || fileFormat.contains(".psd")){
//                File imageFile = new File(file.getAbsolutePath());
//                Image image = new Image(imageFile.toURI().toString());
//                ImageView iv = new ImageView(image);
//                iv.setFitHeight(50);
//                iv.setFitWidth(50);
//                Text lineSpace = new Text("\n");
//                Text username = new Text("username : ");
//                groupTextFlow.getChildren().addAll(username , iv , lineSpace);
//            }
//            else {
//                groupMessage.setText(file.getAbsolutePath());
//            }
        }
    }

    public void displayOldMessage (ArrayList<Message> messages , TextFlow textFlow){
        for (Message message : messages) {
            if (message.getSenderName().equals(Authenticate.getUsername())) {
                if (!message.getFilePath().equals("")) {
                    File file1 = new File(message.getFilePath());
                    String fileFormat = file1.getName().toLowerCase();
                    if (photoInGroup(fileFormat)) {
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
                    if (photoInGroup(fileFormat)) {
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

    public void sendMessage (TextFlow textFlow , TextField textField , String name) throws IOException {
        if (file == null && !textField.getText().isEmpty()) {
            Text text = new Text("Me : " + textField.getText() + "\n");
            textFlow.getChildren().add(text);
            Message message = new Message(Authenticate.getUsername(), name, "groups", textField.getText());
            textField.setText("");
            textField.setPromptText("type a message");
            chatManager.sendMessage(message);
        } else if (file != null && !textField.getText().isEmpty()) {
            String fileFormat = file.getName().toLowerCase();
            if (photoInGroup(fileFormat)){
                displayPicFile(file, "Me" , textFlow);
            } else {
                Text text = new Text("Me : Send a file!" + "\n");
                textFlow.getChildren().add(text);
            }
            Message message = new Message(Authenticate.getUsername(), name, "groups", textField.getText(), file.getAbsolutePath());
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

    public Boolean photoInGroup(String fileFormat){
        if (fileFormat.contains(".png") || fileFormat.contains(".jpeg") || fileFormat.contains(".jpg") || fileFormat.contains(".gif") || fileFormat.contains(".raw") || fileFormat.contains(".tiff") || fileFormat.contains(".psd")){
            return true;
        }
        return false;
    }
}
