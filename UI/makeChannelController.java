package UI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class makeChannelController implements Initializable {

    @FXML public ListView<String> channelMemberAdding;
    @FXML public ListView<String> channelAdminAdding;
    @FXML public TextField channelName;
    @FXML public Label warning;

    static ObservableList<String> channelMemberList = FXCollections.observableArrayList();
    static ObservableList<String> adminList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        channelMemberAdding.getItems().addAll(mainPageController.contactList);
        channelAdminAdding.getItems().addAll(mainPageController.contactList);
        channelAdminAdding.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        channelMemberAdding.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void backToMainMenu(MouseEvent event)throws IOException{
        showStage1("UI/mainMenu.fxml");
    }

    public static String nameOfChannel = "";
    public void continueToChannel(ActionEvent event)throws IOException {
        warning.setText("");
        if(channelName.getText().isEmpty()){
            warning.setText("You have to name your channel first!");
        }
        else if(adminList.isEmpty()){
            warning.setText("You have to choose your admins first!");
        }
        else if(channelMemberList.isEmpty()){
            warning.setText("You have to choose your members first!");
        }
        else {
            nameOfChannel = channelName.getText();
            showStage1("UI/channelWithAdmin.fxml");
        }
    }

    public void addChannelAdmins(ActionEvent event)throws IOException{
        warning.setText("");
        if(channelAdminAdding.getSelectionModel().getSelectedItems().isEmpty()){
            warning.setText("You have to choose at least one admin including you!");
        }
        else {
            adminList.addAll(channelAdminAdding.getSelectionModel().getSelectedItems());
        }
    }

    public void addChannelMembers(ActionEvent event)throws IOException{
        warning.setText("");
        if(channelMemberAdding.getSelectionModel().getSelectedItems().isEmpty()){
            warning.setText("You have to choose at least one member!");
        }
        else {
            channelMemberList.addAll(channelMemberAdding.getSelectionModel().getSelectedItems());
        }
    }

    public void showStage1(String name)throws IOException{
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
