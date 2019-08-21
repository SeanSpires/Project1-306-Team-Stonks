package MVC.Model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Task {

    private int nodeNumber;
    private int weight;
    private int processor;
    private int status;
    private List<Task> parentTasks = new ArrayList<>();
    private HashMap<Task, Integer> subTasks = new HashMap<>();

    public Task(int nodeNumber){
        this.nodeNumber = nodeNumber;
    }
    
    public int getStatus() {
    	return this.status;
    }
    
    public void setStatus(int status) {
    	this.status = status;
    }

    public int getNodeNumber(){
        return nodeNumber;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getProcessor() {
        return processor;
    }

    public void setProcessor(int processor) {
        this.processor = processor;
    }

    public List<Task> getParentTasks() {
        return parentTasks;
    }

    public void setParentTasks(Task task) {
        if(!parentTasks.contains(task))
        this.parentTasks.add(task);
    }

    public HashMap<Task, Integer> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(Task task, Integer weight) {
        subTasks.put(task, weight);
    }



}
