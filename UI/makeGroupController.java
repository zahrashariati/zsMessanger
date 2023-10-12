package UI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class makeGroupController implements Initializable {

    @FXML public ListView<String> groupMemberAdding;
    @FXML public Label warning;
    @FXML public TextField groupName;

    static ObservableList<String> groupMemberList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        groupMemberAdding.getItems().addAll(mainPageController.contactList);
        groupMemberAdding.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void backToMainMenu(MouseEvent event)throws IOException{
        showStage2("UI/mainMenu.fxml");
    }

    public static String nameOfGroup = "";
    public void continueToGroup(ActionEvent event)throws IOException{
        warning.setText("");
        if(groupName.getText().isEmpty()){
            warning.setText("You have to name your group first!");
        }
        else if(groupMemberList.isEmpty()){
            warning.setText("You have to choose your members first!");
        }
        else {
            nameOfGroup = groupName.getText();
            showStage2("UI/groupChat.fxml");
        }
    }

    public void addGroupMembers(ActionEvent event)throws IOException{
        warning.setText("");
        if(groupMemberAdding.getSelectionModel().getSelectedItems().isEmpty()){
            warning.setText("You have to choose at least one member including you!");
        }
        else {
            groupMemberList.addAll(groupMemberAdding.getSelectionModel().getSelectedItems());
        }
    }

    public void showStage2(String name)throws IOException{
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
