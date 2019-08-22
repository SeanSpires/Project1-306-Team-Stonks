package mvc.controller;

import javafx.application.Platform;
import mvc.Main;
import mvc.model.*;
import mvc.view.fxml.ZoomableScrollPane;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;

/**
 * Constructs the GUI components and performs events for displaying and
 * manipulating the binary tree.
 *
 * @author Eric Canull
 * @version 1.0
 */
public final class MenuController {


	@FXML private AnchorPane centerPane;
	@FXML private ZoomableScrollPane ganttScrollPane;
	@FXML private Label _timerOutput;
	@FXML private Timer timer;
	@FXML private Label _processors;
	@FXML private Label _cores;

    private FileIO fileio;
    private Scheduler scheduler;
    private Schedule schedule;
	private List<Task> taskList;

	private List<String> processorList = new ArrayList<String>();

	private Timeline timeline;


	private double timeTaken;

	// Init Gantt Chart.
	private NumberAxis xAxis;
	private CategoryAxis yAxis;
	private GanttChart<Number, String> ganttChart;
	/**
	 * Constructs the GUI components and performs events for displaying and
	 * changing the data in the binary tree.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		getInputs();



		fileio = new FileIO();

		xAxis = new NumberAxis();
		yAxis = new CategoryAxis();
		ganttChart = new GanttChart<Number, String>(xAxis, yAxis);

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
		xAxis.setLabel("Time");
		xAxis.setTickLabelFill(Color.BLACK);

		yAxis.setLabel("Processor No.");
		yAxis.setTickLabelFill(Color.BLACK);
		initGanttChartYAxis();
		yAxis.setCategories(FXCollections.<String>observableArrayList(processorList));

		ganttChart.setTitle("");
		ganttChart.setLegendVisible(true);
		ganttChart.setBlockHeight(50);
		//ganttChart.getStylesheets().add(getClass().getResource("ganttchart.css").toExternalForm());
	}

	private void initGanttChartYAxis(){
		for (int i = 0; i <= Main.numberOfProcessors; i++) {
			XYChart.Series series = new XYChart.Series();
			ganttChart.getData().add(series);
			String processor = "" + 1;
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

	/**
	 *  Performs the action when the search button is clicked.
	 */
	@FXML private void searchOnAction(ActionEvent event) {

	}

	/**
	 * Performs the action when the delete button is clicked.
	 */
	@FXML private void deleteOnAction(ActionEvent event) {
	}


	/**
	 * Performs the action when the clear button is clicked.
	 */
	@FXML private void clearOnAction(ActionEvent event) {


	}

	/**
	 *  Performs the action when the insert button is clicked.
	 */
	@FXML private void insertOnAction(ActionEvent event) {

	}

	@FXML
	public void handleRunButton(javafx.event.ActionEvent actionEvent) {
		// Starts running the timer for the app.
		runTimer();
		fileio.readFile(Main.inputFileName);
		fileio.processNodes();
		fileio.processTransitions();
		taskList = fileio.getTaskList();
		scheduler = new Scheduler();
		schedule = scheduler.createBasicSchedule(taskList, 1);
		fileio.writeFile(schedule);
	}

	@FXML
	public void handleStopButton(javafx.event.ActionEvent actionEvent) {
		// Use this to stop the timer
//		System.out.println("asjdsklajd l");
		timeline.stop();

//		Platform.exit();
    }

    // Replace partial schedule graph with own data structure.
//    public void updateGraph(Current Best Schedule) {
//
//        // Clear gantt chart
//        // Add nodes to gantt chart
//        for (all Tasks in Schedule){
//            // Iterate through the schedule and sort it out in the appropriate data structure.
//        }
////
//    }

}
