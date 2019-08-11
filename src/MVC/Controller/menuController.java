package MVC.Controller;

import MVC.Model.FileIO;
import MVC.Model.Schedule;
import MVC.Model.Scheduler;
import MVC.Model.Task;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

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

        fileio.readFile("Nodes_7_OutTree.dot");
        fileio.processNodes();
        fileio.processTransitions();
        taskList = fileio.getTaskList();
        scheduler = new Scheduler();
        schedule = scheduler.createBasicSchedule(taskList, 1);
        fileio.writeFile(schedule.getTasks());
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
        Platform.exit();
    }


}
