package mvc.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Node implements Comparable{


    private List<Task> unscheduledTasks = new ArrayList<>();
    
    private HashMap<Integer, List<Task>> scheduledTasksByProcessor = new HashMap<>();
    // Map of scheduled tasks
    private List<Task>  scheduledTasks = new ArrayList<>();

    // Upper Bound
    private double upperBound;
    // Lower Bound
    private double lowerBound;

    public Node(Node node) {
        for(Task task : node.getUnscheduledTasks()) {
    		this.unscheduledTasks.add(new Task(task));
    	}
        for(Task task : node.getScheduledTasks()) {
    		this.scheduledTasks.add(new Task(task));
    	}
        this.upperBound = node.getUpperBound();
        this.lowerBound = node.getLowerBound();
        
        Map<Integer, List<Task>> procTasks = node.getScheduledTasksByProcessor();
        for(Integer task : procTasks.keySet()) {
        	List<Task> taskList = new ArrayList<>();
        	for(Task t : procTasks.get(task)) {
        		taskList.add(new Task(t));
        	}
        	this.scheduledTasksByProcessor.put(task, taskList);
    	}
    }

    
    
    public Node() {
        this.unscheduledTasks = new ArrayList<>();
        this.scheduledTasks = new ArrayList<Task> ();
        this.scheduledTasksByProcessor = new HashMap<>();
        this.upperBound = Double.POSITIVE_INFINITY;
        this.lowerBound = Double.POSITIVE_INFINITY;
    }

    public Task getTaskByNumber(Integer i) {
    	for(Task t : scheduledTasks) {
    		if(t.getNodeNumber() == i) {
    			return t;
    		}
    	}
    	return null;
    	
    }
    
    public List<Task> getUnscheduledTasks() {
        return unscheduledTasks;
    }

    public void setUnscheduledTasks(List<Task> unscheduledTasks) {
    	
        this.unscheduledTasks = unscheduledTasks;
    }

    public void removeUnscheduledTask(Task task) {
    	for(Task t : new ArrayList<>(this.unscheduledTasks)) {
    		if(t.getNodeNumber() == task.getNodeNumber()) {
    			unscheduledTasks.remove(t);
    		}
    	}
    }


    public List<Task>  getScheduledTasks() {
        return scheduledTasks;
    }

    public void setScheduledTasks(List<Task> scheduledTasks) {
        this.scheduledTasks = scheduledTasks;
    }


    public void addTasksToProcessor(Task task,int proc) {
    	List<Task> tempList = this.scheduledTasksByProcessor.get(proc);
    	
    	if (this.scheduledTasksByProcessor.get(proc) == null) {
    		tempList = new ArrayList<>();
    		tempList.add(task);
            this.scheduledTasksByProcessor.put(proc, tempList);
    	}
    	else {
            tempList = this.scheduledTasksByProcessor.get(proc);
            tempList.add(task);
            this.scheduledTasksByProcessor.put(proc, tempList);
    	}
    }
    
    public List<Task> getTasksForProcessor(int proc) {
    	if (this.scheduledTasksByProcessor.get(proc) == null) {
    		return new ArrayList<>();
    	}
    	
    	return this.scheduledTasksByProcessor.get(proc);
    }


    public double getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(double upperBound) {
        this.upperBound = upperBound;
    }

    public double getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(double lowerBound) {
        this.lowerBound = lowerBound;
    }



    public void addScheduledTask(Task task) {
    	this.scheduledTasks.add(task);
    }



	public HashMap<Integer, List<Task>> getScheduledTasksByProcessor() {
		return scheduledTasksByProcessor;
	}



	public void setScheduledTasksByProcessor(HashMap<Integer, List<Task>> scheduledTasksByProcessor) {
		this.scheduledTasksByProcessor = scheduledTasksByProcessor;
	}



	@Override
	public int compareTo(Object node) {
		if(node instanceof Node) {
			if(((Node) node).getLowerBound() < this.getLowerBound()) {
				return 1;
			}else {
				return -1;
			}
		}
		return 0;
		
	}

}

