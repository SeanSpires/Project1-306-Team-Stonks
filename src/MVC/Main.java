package MVC;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("View/FXML/menuWindow.fxml"));
        primaryStage.setTitle("Project1-306");
        Scene mainScene = new Scene(root, 600, 450);
        mainScene.getStylesheets().add(getClass().getResource("View/CSS/menuWindow.css").toExternalForm());
        primaryStage.setScene(mainScene);
        primaryStage.show();

        System.out.println(javafx.scene.text.Font.getFamilies());
    }


    public static void main(String[] args) {
        launch(args);
    }
}
