package MVC.Controller;

import MVC.Main;
import MVC.Model.FileIO;
import MVC.Model.Schedule;
import MVC.Model.Scheduler;
import MVC.Model.Task;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Constructs the GUI components and performs events for displaying and
 * manipulating the binary tree.
 * @author Eric Canull
 * @version 1.0
 */
public final class MenuController implements Initializable {

	// Panels and other GUI components
	@FXML private AnchorPane root_container;
	@FXML private TextArea traversal_textarea;
	@FXML private TextField input_field;
	@FXML private Pane centerPane;

	private GraphicsTree graphicsTree;
    private FileIO fileio;
    private Scheduler scheduler;
    private Schedule schedule;
    private List<Task> taskList;
    private HelloWorld hello;


	/**
	 * Constructs the GUI components and performs events for displaying and
	 * changing the data in the binary tree.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		fileio = new FileIO();
		hello = new HelloWorld();

		// The center panel for drawing the tree
		graphicsTree = new GraphicsTree();
		Group group = new Group((graphicsTree));
		SwingNode swingNode = new SwingNode();
		createSwingContent(swingNode);
		Parent zoomPane = createZoomPane(group);

		centerPane.getChildren().add(swingNode);

		graphicsTree.widthProperty().bind(centerPane.widthProperty());
		graphicsTree.heightProperty().bind(centerPane.heightProperty().subtract(50));
		String style = "-fx-background-color: rgba(255, 255, 255);";
		graphicsTree.setStyle(style);

	}

	/**
	 *  Performs the action when the search button is clicked.
	 */
	@FXML private void searchOnAction(ActionEvent event) {
		try {
			graphicsTree.search(Integer.parseInt(input_field.getText().trim()));
		} catch (NumberFormatException nfe) {
			Alert alert = new Alert(Alert.AlertType.ERROR,
					"Error searching for value. The input field can only accept numbers.", 	ButtonType.OK);

			alert.showAndWait()
					.filter(response -> response == ButtonType.OK)
					.ifPresent(response -> alert.close());

		}
	}

	/**
	 * Performs the action when the delete button is clicked.
	 */
	@FXML private void deleteOnAction(ActionEvent event) {
		try {
			graphicsTree.delete(Integer.parseInt(input_field.getText().trim()));
		} catch (NumberFormatException nfe) {
			Alert alert = new Alert(Alert.AlertType.ERROR, "Error deleting value. The input field can only accept numbers.",
					ButtonType.OK);
			alert.showAndWait()
					.filter(response -> response == ButtonType.OK)
					.ifPresent(response -> alert.close());
		}
	}

	private void clearTree() {
		graphicsTree.makeEmpty();
		traversal_textarea.setText("");
	}

	/**
	 * Performs the action when the clear button is clicked.
	 */
	@FXML private void clearOnAction(ActionEvent event) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to empty the tree?", ButtonType.OK);
		alert.showAndWait()
				.filter(response -> response == ButtonType.OK)
				.ifPresent(response -> clearTree());

	}

	/**
	 *  Performs the action when the insert button is clicked.
	 */
	@FXML private void insertOnAction(ActionEvent event) {
		try {
			graphicsTree.insert(Integer.parseInt(input_field.getText().trim()));
		} catch (NumberFormatException nfe) {
			Alert alert = new Alert(Alert.AlertType.ERROR, "Error inserting value. The input field can only accept numbers.",
					ButtonType.OK);
			alert.showAndWait()
					.filter(response -> response == ButtonType.OK)
					.ifPresent(response -> alert.close());
		}
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

	@FXML
	public void handleStopButton(javafx.event.ActionEvent actionEvent) {
		graphicsTree.createTree();
//		Platform.exit();
	}


	private Parent createZoomPane(final Group group) {
		final double SCALE_DELTA = 1.1;
		final StackPane zoomPane = new StackPane();



		zoomPane.getChildren().add(group);

		final ScrollPane scroller = new ScrollPane();
		final Group scrollContent = new Group(zoomPane);
		scroller.setContent(scrollContent);
		String style = "-fx-background-color: rgba(255, 255, 0);";
		zoomPane.setStyle(style);

		scroller.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
			@Override
			public void changed(ObservableValue<? extends Bounds> observable,
								Bounds oldValue, Bounds newValue) {
				zoomPane.setMinSize(newValue.getWidth(), newValue.getHeight());
			}
		});


		scroller.setPrefViewportWidth(1024);
		scroller.setPrefViewportHeight(600);
		scroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scroller.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

		zoomPane.setOnScroll(new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				event.consume();

				if (event.getDeltaY() == 0) {
					return;
				}

				double scaleFactor = (event.getDeltaY() > 0) ? SCALE_DELTA
						: 1 / SCALE_DELTA;

				// amount of scrolling in each direction in scrollContent coordinate
				// units
				Point2D scrollOffset = figureScrollOffset(scrollContent, scroller);

				group.setScaleX(group.getScaleX() * scaleFactor);
				group.setScaleY(group.getScaleY() * scaleFactor);

				// move viewport so that old center remains in the center after the
				// scaling
				repositionScroller(scrollContent, scroller, scaleFactor, scrollOffset);

			}
		});

		// Panning via drag....
		final ObjectProperty<Point2D> lastMouseCoordinates = new SimpleObjectProperty<Point2D>();
		scrollContent.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				lastMouseCoordinates.set(new Point2D(event.getX(), event.getY()));
			}
		});

		scrollContent.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				double deltaX = event.getX() - lastMouseCoordinates.get().getX();
				double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
				double deltaH = deltaX * (scroller.getHmax() - scroller.getHmin()) / extraWidth;
				double desiredH = scroller.getHvalue() - deltaH;
				scroller.setHvalue(Math.max(0, Math.min(scroller.getHmax(), desiredH)));

				double deltaY = event.getY() - lastMouseCoordinates.get().getY();
				double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
				double deltaV = deltaY * (scroller.getHmax() - scroller.getHmin()) / extraHeight;
				double desiredV = scroller.getVvalue() - deltaV;
				scroller.setVvalue(Math.max(0, Math.min(scroller.getVmax(), desiredV)));
			}
		});

		return scroller;
	}

	private Point2D figureScrollOffset(Node scrollContent, ScrollPane scroller) {
		double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
		double hScrollProportion = (scroller.getHvalue() - scroller.getHmin()) / (scroller.getHmax() - scroller.getHmin());
		double scrollXOffset = hScrollProportion * Math.max(0, extraWidth);
		double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
		double vScrollProportion = (scroller.getVvalue() - scroller.getVmin()) / (scroller.getVmax() - scroller.getVmin());
		double scrollYOffset = vScrollProportion * Math.max(0, extraHeight);
		return new Point2D(scrollXOffset, scrollYOffset);
	}

	private void repositionScroller(Node scrollContent, ScrollPane scroller, double scaleFactor, Point2D scrollOffset) {
		double scrollXOffset = scrollOffset.getX();
		double scrollYOffset = scrollOffset.getY();
		double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
		if (extraWidth > 0) {
			double halfWidth = scroller.getViewportBounds().getWidth() / 2 ;
			double newScrollXOffset = (scaleFactor - 1) *  halfWidth + scaleFactor * scrollXOffset;
			scroller.setHvalue(scroller.getHmin() + newScrollXOffset * (scroller.getHmax() - scroller.getHmin()) / extraWidth);
		} else {
			scroller.setHvalue(scroller.getHmin());
		}
		double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
		if (extraHeight > 0) {
			double halfHeight = scroller.getViewportBounds().getHeight() / 2 ;
			double newScrollYOffset = (scaleFactor - 1) * halfHeight + scaleFactor * scrollYOffset;
			scroller.setVvalue(scroller.getVmin() + newScrollYOffset * (scroller.getVmax() - scroller.getVmin()) / extraHeight);
		} else {
			scroller.setHvalue(scroller.getHmin());
		}
	}

	private void createSwingContent(final SwingNode swingNode) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JPanel panel = new JPanel();
				panel.setLayout(null);
				panel.setSize(1024, 430);
				panel.add(hello.getGraphComponent());


				swingNode.setContent(panel);

			}
		});
	}
}
