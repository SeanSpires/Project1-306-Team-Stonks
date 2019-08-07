package sample.Model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileIO {

    private List<String> inputStringList = new ArrayList<>();
    public List<Task> taskList = new ArrayList<>();

    public void writeFile(String fileName){
        File outputFile = new File(fileName + ".dot");

        String currentFileName = fileName+".dot";

        if(outputFile.exists()){
            outputFile.delete();
        }
        try {
            FileWriter fw = new FileWriter(currentFileName,true);

            for(String s : inputStringList) {
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
                inputStringList.add(line);
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void processFile(){

        for(String s: inputStringList){
            String currentLine = s.trim();
            char firstChar = currentLine.charAt(0);
            if(Character.isDigit(firstChar)){

                String sub = currentLine.substring(2,4);
                if(sub.equals("->")){

                    //Checks if first node has been created;
                    int weight = getWeightValue(currentLine);
                    createTask(Character.getNumericValue(firstChar), weight);

                    //Check if second node has been created;
                    char charOfSub = currentLine.charAt(5);





                    //this is a transition
                    //add to parentTasks if applicable
                    //add to hash map if applicable
                    //
                } else {
                    //this is just a task
                    //Check if task has been created
                    int weight = getWeightValue(currentLine);

                    boolean inList = false;
                    for(Task t : taskList){
                        if((t.getNodeNumber()) == (Character.getNumericValue(firstChar))){
                            t.setWeight(weight);
                            inList = true;
                            break;
                        }
                    }
                    if(!inList){
                        createTask(Character.getNumericValue(firstChar), weight);
                    }

                }
            } else {
                //ignore
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


}
