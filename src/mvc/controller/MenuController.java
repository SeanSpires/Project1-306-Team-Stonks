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
import mvc.view.components.GanttChart;
import mvc.view.components.ZoomableScrollPane;

import java.net.URL;
import java.util.*;

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
	private List<String> colour;

	/**
	 * Initialise of the Menu controller when the FXML boots up.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		colour = new ArrayList<String>();
		xAxis = new NumberAxis();
		yAxis = new CategoryAxis();
		ganttChart = new GanttChart<Number, String>(xAxis, yAxis);
		getInputs();
		initScrollPane();
		initGanttChart();
		initColours();

	}

	/**
	 * Adds the Strings to the Array list = Colour.
	 */
	public void initColours() {
		this.colour.add("status-red");
		this.colour.add("status-green");
	}

	/**
	 * Displays cores/processors being used
	 */
	private void getInputs(){
		_cores.setText(Integer.toString(Main.numberOfCores));
		_processors.setText(Integer.toString(Main.numberOfProcessors));
	}

	/**
	 * initialise the scroll pane.
	 */
	private void initScrollPane() {
		ganttScrollPane.initialise();
		ganttScrollPane.setVvalue(0.75);
	}

	/**
	 * Initialises the Gantt chart with the appropriate labels & height + width.
	 */
	private void initGanttChart() {
		ganttChart.setMinWidth(1034);
		ganttChart.setMinHeight(600);
		// TODO: Change the centerPane name.
		centerPane.getChildren().add(ganttChart);
		xAxis.setLabel("T I M E");
		xAxis.setTickLabelFill(Color.BLACK);

		yAxis.setLabel("P R O C E S S E S");
		yAxis.setTickLabelFill(Color.BLACK);
		initGanttChartYAxis();
		yAxis.setCategories(FXCollections.<String>observableArrayList(processorList));
		ganttChart.setTitle("");
		ganttChart.setLegendVisible(true);
		ganttChart.setBlockHeight(50);
		ganttChart.getStylesheets().add(getClass().getResource("ganttChart.css").toExternalForm());
	}

	/**
	 * Sets up the Gantt Chart Y axis.
	 */
	private void initGanttChartYAxis(){
		for (int i = 1; i <= Main.numberOfProcessors; i++) {
			XYChart.Series series = new XYChart.Series();
			ganttChart.getData().add(series);
			String processor = "" + i;
			processorList.add(processor);
		}
	}

	/**
	 * To be called when the start button is pressed.
	 */
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
	 * Creates a new thread to run the algorithm.
	 * @param actionEvent
	 */
	@FXML
	public void handleRunButton(javafx.event.ActionEvent actionEvent) {

		// Starts running the timer for the app.
		fileio = new FileIO();
		runTimer();
		_stopBtn.setDisable(false);
		_runBtn.setDisable(true);

        Service algorithmService = new Service() {

        	// Create a new worker thread to run in the background, when visualisation is happening.
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
                        schedule = scheduler.createOptimalSchedule(taskList, 1, thisController);
                        finishedScheduleTasks = schedule.getTasks();
                        fileio.writeFile(schedule);
                        return null;
                    }

                };
            }

            // When the thread has finished
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

	/**
	 * Run when the stop button is pressed.
	 * @param actionEvent
	 */
	@FXML
	public void handleStopButton(javafx.event.ActionEvent actionEvent) {
		// Use this to stop the timer
		timeline.stop();
		_stopBtn.setDisable(true);
		_runBtn.setDisable(false);
    }

	/**
	 * Update the graph iteratively
	 * @param scheduledTasks
	 */
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
					String style = getColor(t.getNodeNumber());

					// Plots the task on the Schedule
					XYChart.Data newData = new XYChart.Data(startTime, Integer.toString(processor), new GanttChart.ExtraData(weight, style, nodeNumber));
					series.getData().add(newData);

				}

			}
		});
    }


	/**
	 * Gets the String for the odd and even tasks
	 * @param x - Node number
	 * @return - String for CSS colour.
	 */
    public String getColor(int x) {
		if(x % 2 == 0) {
			return this.colour.get(0);
		} else {
			return this.colour.get(1);
		}
	}

}
