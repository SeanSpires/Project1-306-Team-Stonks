package MVC.Controller;

import MVC.Main;
import MVC.Model.FileIO;
import MVC.Model.Schedule;
import MVC.Model.Scheduler;
import MVC.Model.Task;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class menuController implements Initializable {

    private FileIO fileio;
    private Scheduler scheduler;
    private Schedule schedule;
    private List<Task> taskList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileio = new FileIO();

    }

    @FXML
    public void handleMenuButton(javafx.event.ActionEvent actionEvent) {
        Platform.exit();
    }

    @FXML
    public void handleStopButton(javafx.event.ActionEvent actionEvent) {
        Platform.exit();
    }

    @FXML
    public void handleRunButton(javafx.event.ActionEvent actionEvent) {

        fileio.readFile(Main.filename);
        fileio.processNodes();
        fileio.processTransitions();
        taskList = fileio.getTaskList();
        scheduler = new Scheduler();
        schedule = scheduler.createBasicSchedule(taskList, 1);
        fileio.writeFile(schedule.getTasks());




//        //first ask the user to select the txt file
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Dot File Picker");
//        fileChooser.getExtensionFilters().addAll( new FileChooser.ExtensionFilter("Dot Files", "*.dot"));
//        Node node = (Node) actionEvent.getSource();
//        File selectedFile = fileChooser.showOpenDialog(node.getScene().getWindow());

//        //check if the file is usable
//        if (selectedFile != null) {
//
//            //create new FileIO -- taken from initialize
//
//
//        }else {
//            //if the file is empty show a warning to the user
//            Alert alert = new Alert(Alert.AlertType.WARNING);
//            alert.setTitle("Warning Dialog");
//            alert.setHeaderText("Looks like that file has no valid input.");
//            alert.setContentText("Try again with another file.");
//            alert.showAndWait();
//        }
    }


}
