package mvc.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Task {

    private int nodeNumber;
    private int weight;
    private int processor;
    private int status;
    private int startTime;
    private List<Task> parentTasks = new ArrayList<>();
    private HashMap<Integer, Integer> subTasks = new HashMap<>();


    public Task(int nodeNumber){
        this.nodeNumber = nodeNumber;
    }
    
    public Task(Task t) {
    	this.nodeNumber = t.nodeNumber;
    	this.weight = t.weight;
    	this.processor = t.processor;
    	this.status = t.status;
    	this.startTime = t.startTime;
    	
    	for(Task task : t.parentTasks) {
    		this.parentTasks.add(new Task(task));
    	}
    	this.subTasks = new HashMap<>(t.subTasks);
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

    public HashMap<Integer, Integer> getSubTasks() {

        return subTasks;
    }

    public void setSubTasks(Task task, Integer weight) {
        subTasks.put(task.getNodeNumber(), weight);
    }

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}
    
    




}
