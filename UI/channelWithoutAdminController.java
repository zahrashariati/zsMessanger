package UI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import Client.*;

public class channelWithoutAdminController implements Initializable {

    @FXML public Label channelName;
    @FXML public TextFlow channelTextFlow;

    threadHandler threadHandler;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        channelName.setText(mainPageController.previousChName);
        for (Channel channel : controller.pi.getChannels()){
            if(channel.getChannelName().equals(mainPageController.previousChName)){
                displayOldMessage(channel.getMessages(),channelTextFlow);
            }
        }
        threadHandler = new threadHandler(Client.getSocket(),channelTextFlow);
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
    }

    public void displayOldMessage (ArrayList<Message> messages , TextFlow textFlow){
        for (Message message : messages) {
            if (!message.getFilePath().equals("")) {
                File file1 = new File(message.getFilePath());
                String fileFormat = file1.getName().toLowerCase();
                if (photoOfChannel(fileFormat)){
                    displayPicFile(file1, channelName.getText(), textFlow);
                } else {
                    Text text = new Text(channelName.getText() + " : Send a file!\n");
                    textFlow.getChildren().add(text);
                }
            } else {
                Text text = new Text(channelName.getText() +" : " + message.getText() + "\n");
                textFlow.getChildren().add(text);
            }
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

    public Boolean photoOfChannel(String fileFormat){
        if (fileFormat.contains(".png") || fileFormat.contains(".jpeg") || fileFormat.contains(".jpg") || fileFormat.contains(".gif") || fileFormat.contains(".raw") || fileFormat.contains(".tiff") || fileFormat.contains(".psd")){
            return true;
        }
        return false;
    }

}
