package MVC;

import MVC.Model.FileIO;
import MVC.Model.Schedule;
import MVC.Model.Scheduler;
import MVC.Model.Task;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

public class Main extends Application {

    public static String inputFileName;

    public static String outputFileName;
    
    public static boolean isSequential; 
    
    private static List<Task> taskList;
    
    public static int numberOfProcessors;


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
        inputFileName = args[0];

        if(args.length >= 2){
        	numberOfProcessors = Integer.parseInt(args[1]);
        	
        	if(numberOfProcessors < 1) {
        		System.out.println("System cannot run with that number of processors");
        		System.exit(0);
        	}
        	        	
            if(args[2].equals("-v")){
                launch();
            }
            else if(args[2].equals("-o")) {
            	outputFileName = args[3];
            	runAlgorithm();
                System.exit(0);
            }
            else {
                System.out.println("Wrong parameter input");
                System.exit(0);
            }
        } 
        else {
            runAlgorithm();
            System.exit(0);
        }
    }

    private static void runAlgorithm(){
        FileIO fileio = new FileIO();
        fileio.readFile(inputFileName);
        fileio.processNodes();
        fileio.processTransitions();
        taskList = fileio.getTaskList();
        Scheduler scheduler = new Scheduler();
        Schedule schedule = scheduler.createBasicSchedule(taskList, 1);
        fileio.writeFile(schedule.getTasks());
    }

}
