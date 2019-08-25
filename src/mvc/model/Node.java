package mvc.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Node class represents a Schedule.
 */
public class Node implements Comparable{


    private List<Task> unscheduledTasks = new ArrayList<>();
    
    private HashMap<Integer, List<Task>> scheduledTasksByProcessor = new HashMap<>();
    // Map of scheduled tasks
    private List<Task>  scheduledTasks = new ArrayList<>();

    private double upperBound;
    private double lowerBound;
    
    private double lastBL = 0;

    /**
     * Constructor for Node. Copies the Node object to solve referencing issues faced.
     * @param node
     */
    public Node(Node node) {
        // Adds a task that has not been scheduled into the unscheduledTasks list.
        for(Task task : node.getUnscheduledTasks()) {
    		this.unscheduledTasks.add(new Task(task));
    	}
        // Adds a task that has been scheduled into the scheduledTasks list.
        for(Task task : node.getScheduledTasks()) {
    		this.scheduledTasks.add(new Task(task));
    	}
        // Sets the Upper and lower found fields.
        this.upperBound = node.getUpperBound();
        this.lowerBound = node.getLowerBound();
        
        Map<Integer, List<Task>> procTasks = node.getScheduledTasksByProcessor();
        // Gets the tasks in each process and adds it into the taskList.
        for(Integer task : procTasks.keySet()) {
        	List<Task> taskList = new ArrayList<>();
        	for(Task t : procTasks.get(task)) {
        		taskList.add(new Task(t));
        	}
        	this.scheduledTasksByProcessor.put(task, taskList);
    	}
    }


    /**
     * Default construct of Node
     */
    public Node() {
        this.unscheduledTasks = new ArrayList<>();
        this.scheduledTasks = new ArrayList<Task> ();
        this.scheduledTasksByProcessor = new HashMap<>();
        this.upperBound = Double.POSITIVE_INFINITY;
        this.lowerBound = Double.POSITIVE_INFINITY;
    }

    /**
     * Gets the task that corresponds to the input number from the scheduledTasks list.
     * @param i - Integer that represents a task
     * @return t - If the tasks does exist, null - If the task doesn't exist.
     */
    public Task getTaskByNumber(Integer i) {
    	for(Task t : scheduledTasks) {
    		if(t.getNodeNumber() == i) {
    			return t;
    		}
    	}
    	return null;
    }

    /**
     * Standard getter for unscheduledTasks
     * @return - unscheduledTasks list.
     */
    public List<Task> getUnscheduledTasks() {
        return unscheduledTasks;
    }

    /**
     * Standard setter for unscheduledTasks
     * @param unscheduledTasks - input list of unscheduled tasks.
     */
    public void setUnscheduledTasks(List<Task> unscheduledTasks) {
    	
        this.unscheduledTasks = unscheduledTasks;
    }

    /**
     * Removes the input task from the unscheduledTasks list.
     * @param task - task to be removed.
     */
    public void removeUnscheduledTask(Task task) {
    	for(Task t : new ArrayList<>(this.unscheduledTasks)) {
    		if(t.getNodeNumber() == task.getNodeNumber()) {
    			unscheduledTasks.remove(t);
    		}
    	}
    }

    /**
     * Standard getter for scheduledTasks.
     * @return - scheduledTasks list.
     */
    public List<Task>  getScheduledTasks() {
        return scheduledTasks;
    }

    /**
     * Method to add the input task, into the input process
     * @param task
     * @param proc
     */
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

    /**
     * Returns all the tasks for the specified process.
     * @param proc - current process.
     * @return new List - if there does not exist a currently existing list for the input process.
     *         scheduledTasksByProcessor - for the specified process.
     */
    public List<Task> getTasksForProcessor(int proc) {
    	if (this.scheduledTasksByProcessor.get(proc) == null) {
    		return new ArrayList<>();
    	}
    	
    	return this.scheduledTasksByProcessor.get(proc);
    }

    /**
     * Standard getter for upperBound
     * @return upperBound field.
     */
    public double getUpperBound() {
        return upperBound;
    }

    /**
     * Standard setter for upperBound.
     * @param upperBound - double for the upper bound of the node.
     */
    public void setUpperBound(double upperBound) {
        this.upperBound = upperBound;
    }

    /**
     * Standard getter for the LowerBound.
     * @return - lowerBound field for the node.
     */
    public double getLowerBound() {
        return lowerBound;
    }

    /**
     * Method to add a task into the scheduledTask list of the node.
     * @param task - input task user wants to add into the list.
     */
    public void addScheduledTask(Task task) {
    	this.scheduledTasks.add(task);
    }

    /**
     * Standard getter for scheduledTasksByProcessor.
     * @return - HashMap scheduledTasksByProcessor.
     */
	public HashMap<Integer, List<Task>> getScheduledTasksByProcessor() {
		return scheduledTasksByProcessor;
	}


    /**
     * Essential for the priority queue. This allows the priority queue know which node to place.
     * @param node
     * @return Numbers to see if the in the previous queue is Smaller, Larger, the Same as the input node.
     */
	@Override
	public int compareTo(Object node) {
		if(node instanceof Node) {
			if(((Node) node).getUpperBound() < this.getUpperBound()) {
				return 1;
			}else {
				return -1;
			}
		}
		return 0;
		
	}
	
    @Override
    public int hashCode(){
        int out = 1;
        Node new1 = new Node(this);
        for(Integer proc : new1.scheduledTasksByProcessor.keySet()){
            for(Task t : new1.scheduledTasksByProcessor.get(proc)) {
                out = out + t.hashCode();
            }
        }
        return out;
    }


    public void setLastBL(double bl){
        this.lastBL = bl;
    }

    public double getLastBL(){
        return this.lastBL;
    }



}

