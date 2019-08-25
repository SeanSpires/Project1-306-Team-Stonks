package mvc.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileIO {

    private List<String> nodeList = new ArrayList<>();
    private List<Task> taskList = new ArrayList<>();
    private List<String> transitionList = new ArrayList<>();
    private String fileName;

    /**
     * Writes the right inputs into the file.
     * @param data - A completed Schedule.
     */
    public void writeFile(Node data) {
    	
    	List<Task> schedule = data.getScheduledTasks();
    	String outputFileName;
    	
    	if (!(mvc.Main.outputFileName == null)) {
    		outputFileName = mvc.Main.outputFileName;
    	}
    	else {
    		outputFileName = fileName + "-output.dot";
    	}
    	
        File outputFile = new File(outputFileName);
        
        if (outputFile.exists()) {
            outputFile.delete();
        }
        try {
            FileWriter fw = new FileWriter(outputFileName, true);

            fw.write("digraph \"" + outputFileName +"\" {\n");

            for (Task t : schedule) {
                int node = t.getNodeNumber();
                int weight = t.getWeight();
                int processor = t.getProcessor();
                int startTime = t.getStartTime();
                int endTime = t.getStatus();
                fw.write("\t"+ node + "\t[Weight=" + weight + ",Start=" + startTime + ",End=" + endTime + ",Processor=" + processor + "];\n");
            }

            for(String s : transitionList){
                fw.write("\t" + s + "\n");
            }

            fw.write('}');
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads the file and places the inputs in the right data structure.
     * @param fileName - Input file name of the .dot file
     */
    public void readFile(String fileName) {
        File currentFile = new File(fileName);
        int dot = fileName.indexOf('.');
        
        this.fileName = fileName.substring(0,dot);


        try (BufferedReader br = new BufferedReader((new FileReader(currentFile)))) {
            String line;
            while ((line = br.readLine()) != null) {
                String inputString = line.trim();
                char firstChar = inputString.charAt(0);
                if (Character.isDigit(firstChar)) {
                    if (isTransition(inputString)) {
                        transitionList.add(inputString);
                    } else {
                        nodeList.add(inputString);
                    }

                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * For each node from the file input, create a new Task object for it.
     */
    public void processNodes() {
        for (String node : nodeList) {
            int weight = getWeightValue(node);
            String s = node.substring(0, node.indexOf('\t'));
            int nodeNumber = Integer.valueOf(s);

            createTask(nodeNumber, weight);

        }
    }

    /**
     * This processes all the transitions of the input from the -> , this sets the parent and child task of each.
     */
    public void processTransitions() {

        for (String transition : transitionList) {

            int weight = getWeightValue(transition);

            String parentString = transition.substring(0, transition.indexOf(' '));
            int parentNode = Integer.valueOf(parentString);
            //Child node
            int arrow = transition.indexOf('>');
            int tab = transition.indexOf('\t');
            String childString = transition.substring(arrow + 2, tab);
            int childNode = Integer.valueOf(childString);

            Task parentTask = null;
            Task childTask = null;

            for (Task task : taskList) {
                if (task.getNodeNumber() == parentNode) {
                    parentTask = task;
                } else if (task.getNodeNumber() == childNode) {
                    childTask = task;
                }
            }

            if (parentTask != null && childTask != null) {
                parentTask.setSubTasks(childTask, weight);
                childTask.setParentTasks(parentTask);
            }
        }

    }

    /**
     * This method creates the task
     * @param nodeNumber - Nodes ID
     * @param weight - Time taken for the node to finish
     */
    private void createTask(int nodeNumber, int weight) {
        Task newTask = new Task(nodeNumber);
        newTask.setWeight(weight);
        taskList.add(newTask);
    }

    /**
     * Parses through a line and gets the weight of each task.
     * @param input - A line from the input file
     * @return - The weight of the node.
     */
    private int getWeightValue(String input) {
        int equals = input.indexOf('=') + 1;
        int bracket = input.indexOf(']');
        String weightStr = input.substring(equals, bracket);
        return Integer.valueOf(weightStr);
    }

    /**
     * Checks if the input line is a Transition
     * @param s - A line from the input file.
     * @return - True if line reflects a transition, false if line is not a transition.
     */
    private boolean isTransition(String s) {
        String sub = s.substring(2, 4);
        if (sub.equals("->")) {
            return true;
        }
        return false;
    }

    /**
     * Gets the list of tasks
     * @return - taskList
     */
    public List<Task> getTaskList() {
        return taskList;
    }

    public void processInput(){
        processNodes();
        processTransitions();
    }

    /**
     *             String parentString = transition.substring(0, transition.indexOf(' '));
     *             int parentNode = Integer.valueOf(parentString);
     *             //Child node
     *             int arrow = transition.indexOf('>');
     *             int tab = transition.indexOf('\t');
     *             String childString = transition.substring(arrow + 2, tab);
     *             int childNode = Integer.valueOf(childString);
     */
}
