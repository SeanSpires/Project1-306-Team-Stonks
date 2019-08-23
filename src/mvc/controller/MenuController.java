package mvc.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import mvc.Main;
import mvc.model.*;
import mvc.view.fxml.ZoomableScrollPane;

import java.net.URL;
import java.util.*;

/**
 * Constructs the GUI components and performs events for displaying and
 * manipulating the binary tree.
 *
 * @author Eric Canull
 * @version 1.0
 */
public final class MenuController implements Initializable{


	@FXML private AnchorPane centerPane;
	@FXML private ZoomableScrollPane ganttScrollPane;
	@FXML private Label _timerOutput;
	@FXML private Timer timer;
	@FXML private Label _processors;
	@FXML private Label _cores;
	@FXML private Button _stopBtn;
	@FXML private Button _runBtn;

    private FileIO fileio;
    private Scheduler scheduler;
    private Schedule schedule;
	private List<Task> taskList;

	private List<String> processorList = new ArrayList<String>();

	private Timeline timeline;


	private double timeTaken;
	private MenuController thisController = this;

	// Init Gantt Chart.
	private NumberAxis xAxis;
	private CategoryAxis yAxis;
	private GanttChart<Number, String> ganttChart;
	private LinkedHashMap<Task, Integer> finishedScheduleTasks;
	/**
	 * Constructs the GUI components and performs events for displaying and
	 * changing the data in the binary tree.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		fileio = new FileIO();

		xAxis = new NumberAxis();
		yAxis = new CategoryAxis();
		ganttChart = new GanttChart<Number, String>(xAxis, yAxis);
		getInputs();
		initScrollPane();
		initGanttChart();

	}

	//displays cores/processors being used
	private void getInputs(){
		_cores.setText(Integer.toString(Main.numberOfCores));
		_processors.setText(Integer.toString(Main.numberOfProcessors));
	}


	private void initScrollPane() {
		ganttScrollPane.initialise();
		ganttScrollPane.setVvalue(0.75);
	}

	private void initGanttChart() {
		ganttChart.setMinWidth(1034);
		ganttChart.setMinHeight(600);
		// TODO: Change the centerPane name.
		centerPane.getChildren().add(ganttChart);
		xAxis.setLabel("T I M E");
		xAxis.setTickLabelFill(Color.BLACK);

		yAxis.setLabel("P R O C E S S O R S");
		yAxis.setTickLabelFill(Color.BLACK);
		initGanttChartYAxis();
		yAxis.setCategories(FXCollections.<String>observableArrayList(processorList));
		ganttChart.setTitle("");
		ganttChart.setLegendVisible(true);
		ganttChart.setBlockHeight(50);
		ganttChart.getStylesheets().add(getClass().getResource("ganttChart.css").toExternalForm());
	}

	private void initGanttChartYAxis(){
		for (int i = 1; i <= Main.numberOfProcessors; i++) {
			XYChart.Series series = new XYChart.Series();
			ganttChart.getData().add(series);
			String processor = "" + i;
			processorList.add(processor);
		}
	}

	// Should be called when the start button is pressed.
	private void runTimer() {
		timeTaken = 0;
		timer = new Timer();

		// In x seconds xx miliseconds.
		timeline = new Timeline(new KeyFrame(Duration.millis( 10 ),
				new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event) {
						timeTaken = timeTaken + 0.01;
						_timerOutput.setText(String.format("%.2f", timeTaken));
					}
				})
		);
		timeline.setCycleCount( Animation.INDEFINITE );
		timeline.play();

	}

	// Creates a new thread to run the algorithm.
	@FXML
	public void handleRunButton(javafx.event.ActionEvent actionEvent) {
		// Starts running the timer for the app.
		runTimer();
		_stopBtn.setDisable(false);
		_runBtn.setDisable(true);

        Service algorithmService = new Service() {

            @Override
            protected javafx.concurrent.Task createTask() {

                return new javafx.concurrent.Task(){

                    @Override
                    protected Object call() {
                        fileio.readFile(Main.inputFileName);
                        fileio.processNodes();
                        fileio.processTransitions();
                        taskList = fileio.getTaskList();
                        scheduler = new Scheduler();
                        schedule = scheduler.createBasicSchedule(taskList, 1, thisController);
                        finishedScheduleTasks = schedule.getTasks();
                        fileio.writeFile(schedule);
                        return null;
                    }

                };
            }

            @Override
            protected void succeeded() {
                _runBtn.setDisable(false);
                _stopBtn.setDisable(true);
                timeline.stop();
                System.out.println("Finished");
            }
        };
        algorithmService.start();
	}

	@FXML
	public void handleStopButton(javafx.event.ActionEvent actionEvent) {
		// Use this to stop the timer
		timeline.stop();
		_stopBtn.setDisable(true);
		_runBtn.setDisable(false);
    }

    // Replace partial schedule graph with own data structure.
    public void updateGraph(LinkedHashMap<Task, Integer> scheduledTasks) {

		Platform.runLater(new Runnable() {

			@SuppressWarnings({ "rawtypes", "unchecked" })
			public void run() {
				ganttChart.getData().clear();
				initGanttChartYAxis();

				for(Task t : scheduledTasks.keySet()){
					XYChart.Series series = ganttChart.getData().get(t.getProcessor()-1);
					int startTime = scheduledTasks.get(t);
					long weight = t.getWeight();
					int processor = t.getProcessor();
					String nodeNumber = Integer.toString(t.getNodeNumber());
					String style = "status-red";

					XYChart.Data newData = new XYChart.Data(startTime, Integer.toString(processor), new GanttChart.ExtraData(weight, style, nodeNumber));
					series.getData().add(newData);

				}

			}
		});

//
    }

}
