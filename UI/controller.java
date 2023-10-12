package UI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.lang.System;
import Client.*;

public class controller {

    @FXML public Label signLabel ;
    @FXML public Label loginLabel;
    @FXML public TextField signUsername;
    @FXML public TextField loginUsername;
    @FXML public PasswordField signPass ;
    @FXML public PasswordField signConfirmedPass ;
    @FXML public PasswordField loginPass;

    Authenticate authenticate = new Authenticate(Client.getSocket());
    public static PrimaryInfo pi;

    public controller() throws IOException {
    }

    public void Welcome(MouseEvent event)throws IOException{
        showTheStage("UI/welcome.fxml");
    }

    public void exit(MouseEvent event)throws IOException{
        Client.getSocket().close();
        System.exit(0);
    }

    public void login(MouseEvent event)throws IOException{
        showTheStage("UI/login.fxml");
    }

    public void signUp(ActionEvent event) throws IOException, ClassNotFoundException {
        signLabel.setText("");
        if(signUsername.getText().isEmpty()) {
            signLabel.setText("You have to fill the blanks!");
        }
        else if(signPass.getText().isEmpty()){
            signLabel.setText("You have to fill the blanks!");
        }
        else if(signConfirmedPass.getText().isEmpty()){
            signLabel.setText("You have to fill the blanks!");
        }
        else {
            String username = signUsername.getText().toLowerCase();
            String password = signPass.getText().toLowerCase();
            if(signPass.getText().toLowerCase().equals(signConfirmedPass.getText().toLowerCase())) {
                if (authenticate.saveNewUser(username,password)) {
                    pi = authenticate.getPrimaryInfo();
                    showTheStage("UI/mainMenu.fxml");
                } else {
                    signLabel.setText("This username already exits! Either login with it or try again.");
                    signUsername.setText("");
                    signUsername.setPromptText("enter your username");
                    signPass.setText("");
                    signPass.setPromptText("enter your password");
                    signConfirmedPass.setText("");
                    signConfirmedPass.setPromptText("confirm your password");
                }
            } else {
                signLabel.setText("You didn't confirm your password correctly! try again.");
                signUsername.setText("");
                signUsername.setPromptText("enter your username");
                signPass.setText("");
                signPass.setPromptText("enter your password");
                signConfirmedPass.setText("");
                signConfirmedPass.setPromptText("confirm your password");
            }
        }
    }

    public void signUpKey(KeyEvent event) throws IOException, ClassNotFoundException {
        signLabel.setText("");
        if(signUsername.getText().isEmpty()) {
            signLabel.setText("You have to fill the blanks!");
        }
        else if(signPass.getText().isEmpty()){
            signLabel.setText("You have to fill the blanks!");
        }
        else if(signConfirmedPass.getText().isEmpty()){
            signLabel.setText("You have to fill the blanks!");
        }
        else {
            if (event.getCode() == KeyCode.ENTER) {
                String username = signUsername.getText().toLowerCase();
                String password = signPass.getText().toLowerCase();
                if(signPass.getText().toLowerCase().equals(signConfirmedPass.getText().toLowerCase())) {
                    if (authenticate.saveNewUser(username,password)) {
                        pi = authenticate.getPrimaryInfo();
                        showTheStage("UI/mainMenu.fxml");
                    } else {
                        signLabel.setText("This username already exits! Either login with it or try again.");
                        signUsername.setText("");
                        signUsername.setPromptText("enter your username");
                        signPass.setText("");
                        signPass.setPromptText("enter your password");
                        signConfirmedPass.setText("");
                        signConfirmedPass.setPromptText("confirm your password");

                    }
                } else {
                    signLabel.setText("You didn't confirm your password correctly! try again.");
                    signUsername.setText("");
                    signUsername.setPromptText("enter your username");
                    signPass.setText("");
                    signPass.setPromptText("enter your password");
                    signConfirmedPass.setText("");
                    signConfirmedPass.setPromptText("confirm your password");
                }
            }
        }
    }

    public void loginContinued(ActionEvent event) throws IOException, ClassNotFoundException {
        loginLabel.setText("");
        if(loginUsername.getText().isEmpty()) {
            loginLabel.setText("You have to fill the blanks!");
        }
        else if(loginPass.getText().isEmpty()){
            loginLabel.setText("You have to fill the blanks!");
        } else {
            String username = loginUsername.getText().toLowerCase();
            String password = loginPass.getText().toLowerCase();
            boolean inputMatch= authenticate.checkInputsMatch(username,password);
            if (inputMatch) {
                pi = authenticate.getPrimaryInfo();
                showTheStage("UI/mainMenu.fxml");
                System.out.println(pi.getRegisteredUserNames());
            } else {
                    loginLabel.setText("login failed! Try again.");
                    loginUsername.setText("");
                    loginUsername.setPromptText("enter your username");
                    loginPass.setText("");
                    loginPass.setPromptText("enter your password");
            }
        }
    }

    public void loginContinuedWithKey(KeyEvent event) throws IOException, ClassNotFoundException {
        loginLabel.setText("");
        if(loginUsername.getText().isEmpty()) {
            loginLabel.setText("You have to fill the blanks!");
        }
        else if(loginPass.getText().isEmpty()){
            loginLabel.setText("You have to fill the blanks!");
        }
        else {
            if (event.getCode() == KeyCode.ENTER) {
                String username = loginUsername.getText().toLowerCase();
                String password = loginPass.getText().toLowerCase();
                boolean inputMatch = authenticate.checkInputsMatch(username, password);
                if (inputMatch) {
                    pi = authenticate.getPrimaryInfo();
                    showTheStage("UI/mainMenu.fxml");
                } else {
                    loginLabel.setText("login failed! Try again.");
                    loginUsername.setText("");
                    loginUsername.setPromptText("enter your username");
                    loginPass.setText("");
                    loginPass.setPromptText("enter your password");
                }
            }
        }
    }

    public void backToWelcome(MouseEvent event)throws IOException{
        Welcome(event);
    }

    public void showTheStage(String name)throws IOException{
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
