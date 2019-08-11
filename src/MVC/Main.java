package MVC;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static String filename;


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("View/FXML/menuWindow.fxml"));
        primaryStage.setTitle("Project1-306");
        Scene mainScene = new Scene(root, 1024, 600);
        mainScene.getStylesheets().add(getClass().getResource("View/CSS/menuWindow.css").toExternalForm());
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        filename = args[0];


    }

    private static void initialise(){


    }
}
