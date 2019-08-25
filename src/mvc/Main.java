package mvc;

import mvc.model.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

public class Main extends Application {

    public static String inputFileName;

    public static String outputFileName;
    
    public static int numberOfCores;

    public static int numberOfProcessors;
    
    private static List<Task> taskList;
    


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader. load(getClass().getResource("view/FXML/MenuWindow.fxml"));
        primaryStage.setTitle("Project1-306");
        Scene mainScene = new Scene(root, 1024, 700);
        mainScene.getStylesheets().add(getClass().getResource("view/CSS/menuWindow.css").toExternalForm());
        primaryStage.setResizable(false);
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        /**
         * Check if all of the input arguments are valid.
         * */
     //        java −jar scheduler.jar INPUT.dot P [OPTION]
     //        INPUT. dot         a task graph with integer weights in dot format
     //        P                  number of  processors  to  schedule  the INPUT graph on
     //        Optional :
     //        −p N               use N cores for execution in parallel (default  is  sequential)
     //        −v                 visualise the search
     //        −o OUPUT           output file  is named OUTPUT (default  isINPUT−output . dot)

        if((args.length < 1) || (args.length > 7)){
            runFailed();
        }

        inputFileName = args[0];

        if(args.length == 1){
            System.out.println("No input received for the number of processors.\n Default value set to 1.");
            numberOfProcessors = 1;
            runAlgorithm();
            System.exit(0);
        }

        //check if second argument is a valid int
        if(args.length == 2) {
            boolean isValid = true;

            try {
                Integer testValue = Integer.parseInt(args[1]);
            } catch (NumberFormatException | NullPointerException nfe) {
                isValid = false;
            }

            if(isValid){
                numberOfProcessors = Integer.parseInt(args[1]);
                runAlgorithm();
                System.exit(0);
            }else{
                System.out.println("Invalid input type for for the number of processors.");
                runFailed();
            }
        }


        if(args.length >= 3) {
            List<String> argsList = Arrays.asList(args);
            numberOfCores = 1;
            numberOfProcessors = Integer.parseInt(args[1]);

            for (String s :argsList){ //Checks for invalid param
                if(s.equals("-o") || s.equals("-v") || s.equals("-p") ){ //Checks if it either -o, -v or -p
                    continue;
                } else {
                    int index = argsList.indexOf(s);
                    int indexOfP = argsList.indexOf("-p");
                    int indexOfO = argsList.indexOf("-o");
                    try {
                        int input = Integer.parseInt(s);
                        if((index != indexOfP + 1)) {
                            if(index != indexOfO + 1) {
                                if (index != 1) {
                                    runFailed();
                                }
                            }
                        }
                    } catch (NumberFormatException | NullPointerException | ArrayIndexOutOfBoundsException e) {
                        if(index != indexOfO + 1){
                            if(index != 0){
                                runFailed();
                            }
                        }
                    }
                }
            }

            if (argsList.contains("-o")) {
                try {
                    outputFileName = args[argsList.indexOf("-o")+1];
                } catch (NumberFormatException | NullPointerException | ArrayIndexOutOfBoundsException e) {
                    System.out.println("No valid input for file output name (after -o). test");
                    runFailed();
                }
                outputFileName = args[argsList.indexOf("-o")+1];
            }
            if(argsList.contains("-p")){
                try{
                     numberOfCores = Integer.parseInt(args[argsList.indexOf("-p")+1]);
                } catch (NumberFormatException | NullPointerException | ArrayIndexOutOfBoundsException e) {
                    System.out.println("No valid input for number of cores (after -p).");
                    runFailed();
                }
            }
            if (argsList.contains("-v")) {
                launch();
            } else{
                runAlgorithm();
                System.exit(1);
            }
        }
    }

    private static void runAlgorithm(){
        FileIO fileio = new FileIO();
        fileio.readFile(inputFileName);
        fileio.processInput();
        taskList = fileio.getTaskList();
        Node schedule;
        if(numberOfCores > 1){ //Multiple Cores
            SchedulerParallel scheduler = new SchedulerParallel();
            schedule = scheduler.createOptimalSchedule(taskList, numberOfProcessors, numberOfCores, null);

        } else { //Single Core
            Scheduler scheduler = new Scheduler();
            schedule = scheduler.createOptimalSchedule(taskList, numberOfProcessors, null);
        }
        fileio.writeFile(schedule);
    }

    private  static void runFailed() {
        System.out.println("ERROR: Invalid input parameters.\n" +
                "The correct input pattern is shown below:\n" +
                "\t\tjava −jar scheduler.jar INPUT.dot P [OPTION]");
        System.exit(1);
    }

}
