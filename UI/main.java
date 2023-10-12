package UI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;

public class main extends Application {


    //TODO mainPageController

    public static Stage primaryStage2;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        File file = new File("src", "UI/firstPage.fxml");
        Parent root = FXMLLoader.load(file.toURI().toURL());
        Scene scene1 = new Scene(root);
        scene1.getStylesheets().add("UI/style.css");
        primaryStage2 = primaryStage;
        primaryStage.setScene(scene1);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
