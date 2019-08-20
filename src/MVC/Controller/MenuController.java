package MVC.Controller;

import MVC.Main;
import MVC.Model.FileIO;
import MVC.Model.Schedule;
import MVC.Model.Scheduler;
import MVC.Model.Task;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML private BorderPane map;

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

        fileio.readFile(Main.inputFileName);
        fileio.processNodes();
        fileio.processTransitions();
        taskList = fileio.getTaskList();
        scheduler = new Scheduler();
        schedule = scheduler.createBasicSchedule(taskList, 1);
        fileio.writeFile(schedule);


    }


}
