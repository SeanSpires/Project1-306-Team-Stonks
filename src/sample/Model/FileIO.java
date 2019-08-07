package sample.Model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileIO {

    private List<String> nodeList = new ArrayList<>();
    private List<Task> taskList = new ArrayList<>();
    private List<String> transitionList = new ArrayList<>();

    public void writeFile(String fileName){
        File outputFile = new File(fileName + ".dot");

        String currentFileName = fileName+".dot";

        if(outputFile.exists()){
            outputFile.delete();
        }
        try {
            FileWriter fw = new FileWriter(currentFileName,true);

            for(String s : nodeList) {
                fw.write(s + "\n");
            }
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFile(String fileName){
        File currentFile = new File(fileName);

        try (BufferedReader br = new BufferedReader((new FileReader(currentFile)))){
            String line;
            while ((line = br.readLine()) != null){
                nodeList.add(line);
                String inputString = line.trim();
                char firstChar = inputString.charAt(0);
                if(Character.isDigit(firstChar)){
                    if(isTransition(line)){
                        transitionList.add(inputString);
                    } else {
                        nodeList.add(inputString);
                    }

                }

            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void processNodes(){
        for(String node : nodeList){
            int weight = getWeightValue(node);
            char firstChar = node.charAt(0);

            createTask(Character.getNumericValue(firstChar), weight);

        }
    }


    public void processTransitions(){

        for(String transition: transitionList) {

            int weight = getWeightValue(transition);

            //Parent node
            int parentNode = Character.getNumericValue(transition.charAt(0));
            //Child node
            int childNode = Character.getNumericValue(transition.charAt(5));

            Task parentTask = null;
            Task childTask = null;

            for(Task task : taskList){
                if(task.getNodeNumber() == parentNode){
                    parentTask = task;
                } else if (task.getNodeNumber() == childNode){
                    childTask = task;
                }
            }

            if(parentTask != null && childTask != null){
                parentTask.setSubTasks(childTask, weight);
                childTask.setParentTasks(parentTask);
            }
        }

    }

    private void createTask(int nodeNumber,int weight){
        Task newTask = new Task(nodeNumber);
        newTask.setWeight(weight);
        taskList.add(newTask);
    }

    private int getWeightValue(String input){
        int equals = input.indexOf('=') + 1;
        int bracket = input.indexOf(']');
        String weightStr = input.substring(equals, bracket);
        return Integer.valueOf(weightStr);
    }

    private boolean isTransition(String s){
        char firstChar = s.charAt(0);
        if(Character.isDigit(firstChar)){
            String sub = s.substring(2,4);
            if(sub.equals("->")) {
                return true;
            } else {
            }
        }
        return false;

    }


}
