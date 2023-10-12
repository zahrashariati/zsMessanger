package UI;

import Client.Authenticate;
import Client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.lang.System;
import java.net.URL;
import java.util.ResourceBundle;

public class mainPageController implements Initializable{

    @FXML public ComboBox<String> contacts;
    @FXML public ComboBox<String> previousChats;
    @FXML public ComboBox<String> previousGroups;
    @FXML public ComboBox<String> previousChannels;
    @FXML public Label contactName;
    @FXML public Label previousChat;
    @FXML public Label previousGroup;
    @FXML public Label previousChannel;
    @FXML public Label warningOfPrev;
    @FXML public Label warningOfContact;
    @FXML public ImageView chats;
    @FXML public ImageView newChat;
    @FXML public ImageView info;
    @FXML public ImageView exit;
    @FXML public AnchorPane chatsAnchor;
    @FXML public AnchorPane newChatsAnchor;
    @FXML public AnchorPane infoAnchor;

    static ObservableList<String> contactList = FXCollections.observableArrayList(controller.pi.getRegisteredUserNames());
    ObservableList<String> chatsList = FXCollections.observableArrayList(controller.pi.getPrivateNames());
    ObservableList<String> groupList = FXCollections.observableArrayList(controller.pi.getGroupNames());
    ObservableList<String> channelList = FXCollections.observableArrayList(controller.pi.getChannelNames());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chatsAnchor.setVisible(false);
        newChatsAnchor.setVisible(false);
        infoAnchor.setVisible(false);
        contacts.setItems(contactList);
        System.out.println(channelList);
        previousChats.setItems(chatsList);
        previousGroups.setItems(groupList);
        previousChannels.setItems(channelList);
    }

    public void contactComboBoxChanged(ActionEvent event)throws IOException{
        contactName.setText(contacts.getValue());
    }

    public void previousChatsComboChanged(ActionEvent event)throws IOException{
        if(chatsAnchor.isVisible()) {
            previousChat.setText(previousChats.getValue());
        }
    }

    public void previousGroupsComboChanged(ActionEvent event)throws IOException{
        if(chatsAnchor.isVisible()) {
            previousGroup.setText(previousGroups.getValue());
        }
    }

    public void previousChannelsComboChanged(ActionEvent event)throws IOException{
        if(chatsAnchor.isVisible()) {
            previousChannel.setText(previousChannels.getValue());
        }
    }

    public void handleMenubarButtons(MouseEvent event){
        if(event.getTarget() == chats){
            chatsAnchor.setVisible(true);
            newChatsAnchor.setVisible(false);
            infoAnchor.setVisible(false);
        }
        else if(event.getTarget() == newChat){
            chatsAnchor.setVisible(false);
            newChatsAnchor.setVisible(true);
            infoAnchor.setVisible(false);
        }
        else if(event.getTarget() == info){
            chatsAnchor.setVisible(false);
            newChatsAnchor.setVisible(false);
            infoAnchor.setVisible(true);
        }
    }

    public void EXIT(MouseEvent event)throws IOException{
        Client.getSocket().close();
        System.exit(0);
    }

    public void makeANewGroup(MouseEvent event)throws IOException{
        goToStage("UI/makeGroup.fxml");
    }

    public void makeANewGroupButton(ActionEvent event)throws IOException{
        goToStage("UI/makeGroup.fxml");
    }

    public void makeANewChannel(MouseEvent event)throws IOException{
        goToStage("UI/makeChannel.fxml");
    }

    public void makeANewChannelButton(ActionEvent event)throws IOException{
        goToStage("UI/makeChannel.fxml");
    }

    public static String newPVChatUsername = "";
    public void newChatOk(MouseEvent event)throws IOException{
        if(newChatsAnchor.isVisible()) {
            if (contactName.getText().isEmpty()) {
                warningOfContact.setText("You have to choose a contact first!");
            }
            else {
                newPVChatUsername = contactName.getText();
                PVU = 0;
                goToStage("UI/pvChat.fxml");
            }
        }
    }


    public static String previousPvUsername = "";
    static int PVU = 0;
    public void goToChat(MouseEvent event)throws IOException{
        if(chatsAnchor.isVisible()){
            if(previousChat.getText().isEmpty()){
                warningOfPrev.setText("You have to choose a chat first!");
            }
            else{
                previousPvUsername = previousChat.getText();
                PVU = 1;
                goToStage("UI/pvChat.fxml");
            }
        }
    }


    public static String previousGPName = "";
    static int GPU = 0;
    public void goToGroup(MouseEvent event)throws IOException{
        if(chatsAnchor.isVisible()){
            if(previousGroup.getText().isEmpty()){
                warningOfPrev.setText("You have to choose a group first!");
            }
            else {
                previousGPName = previousGroup.getText();
                GPU = 1;
                goToStage("UI/groupChat.fxml");
            }
        }
    }


    public static String previousChName = "";
    static int CHU = 0;
    public void goToChannel(MouseEvent event)throws IOException{
        if(chatsAnchor.isVisible()){
            if(previousChannel.getText().isEmpty()){
                warningOfPrev.setText("You have to choose a channel first!");
            }
            else{
                boolean flag = true;
                for (int i = 0; i < controller.pi.getChannels().size(); i++) {
                    if(controller.pi.getChannels().get(i).getChannelName().equals(previousChannel.getText())){
                        makeChannelController.adminList.addAll(controller.pi.getChannels().get(i).getAdmins());
                    }
                }
                for (String admins : makeChannelController.adminList){
                    if(admins.equals(Authenticate.getUsername())){
                        previousChName = previousChannel.getText();
                        CHU = 1;
                        goToStage("UI/channelWithAdmin.fxml");
                        flag = false;
                    }
                }
                if(flag){
                    previousChName = previousChannel.getText();
                    goToStage("UI/channelWithoutAdmin.fxml");
                }
            }
        }
    }

    public void goToStage(String name) throws IOException {
        Stage primaryStage = new Stage();
        File file5 = new File("src", name);
        Parent root = FXMLLoader.load(file5.toURI().toURL());
        Scene scene = new Scene(root);
        scene.getStylesheets().add("UI/style.css");
        primaryStage.setScene(scene);
        main.primaryStage2.close();
        main.primaryStage2 = primaryStage;
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
