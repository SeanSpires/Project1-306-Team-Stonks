package sample.Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;

public class menuController{

    @FXML
    public void handleStopButton(javafx.event.ActionEvent actionEvent) {
        Platform.exit();
    }

    @FXML
    public void handleRunButton(javafx.event.ActionEvent actionEvent) {
        Platform.exit();
    }

}
