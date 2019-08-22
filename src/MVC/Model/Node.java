package MVC.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class Node {


    private List<Task> unscheduledTasks;

    // Map of scheduled tasks
    private LinkedHashMap<Integer, List<Task>> scheduledTasks;
    // Computation time (end time) for each scheduled task
    private LinkedHashMap<Task, Integer> computationTimes;
    // List of nodes to be expanded
    private List<Node> openNodes;
    // Upper Bound
    private double upperBound;
    // Lower Bound
    private double lowerBound;

    public Node(Node node) {
        this.unscheduledTasks = node.getUnscheduledTasks();
        this.scheduledTasks = node.getScheduledTasks();
        this.computationTimes = node.getComputationTimes();
        this.openNodes = node.getOpenNodes();
        this.upperBound = node.getUpperBound();
        this.lowerBound = node.getLowerBound();
    }

    public Node () {
        this.unscheduledTasks = new ArrayList<>();
        this.scheduledTasks = new LinkedHashMap<>();
        this.computationTimes = new LinkedHashMap<>();
        this.openNodes = new ArrayList<>();
        this.upperBound = Double.POSITIVE_INFINITY;
        this.lowerBound = Double.POSITIVE_INFINITY;
    }


    public List<Task> getUnscheduledTasks() {
        return unscheduledTasks;
    }

    public void setUnscheduledTasks(List<Task> unscheduledTasks) {
        this.unscheduledTasks = unscheduledTasks;
    }

    public void removeUnscheduledTask(Task task) {
        this.unscheduledTasks.remove(task);
    }

    public LinkedHashMap<Task, Integer> getComputationTimes() {
        return computationTimes;
    }

    public void setComputationTimes(LinkedHashMap<Task, Integer> computationTimes) {
        this.computationTimes = computationTimes;
    }

    public void addComputationTime(Task task, int time) {
        this.computationTimes.put(task,time);
    }

    public LinkedHashMap<Integer, List<Task>> getScheduledTasks() {
        return scheduledTasks;
    }

    public void setScheduledTasks(LinkedHashMap<Integer, List<Task>> scheduledTasks) {
        this.scheduledTasks = scheduledTasks;
    }

    public List<Node> getOpenNodes() {
        return openNodes;
    }

    public void setOpenNodes(List<Node> openNodes) {
        this.openNodes = openNodes;
    }


    public void addOpenNode(Node node) {
        if (!node.getOpenNodes().contains(node)) {
            this.openNodes.add(node);
        }
    }

    public List<Task> getScheduledTasks(int processor) {
        return scheduledTasks.get(processor);
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

    public void removeOpenNode(Node node) {
        if (this.openNodes.contains(node)) {
            this.openNodes.remove(node);
        }
    }

    public List<Task> getAllScheduledTasks() {
        List<Task> list = new ArrayList<>();
        Set<Integer> keys = this.getScheduledTasks().keySet();
        for (Integer i : keys) {
            list.addAll(this.getScheduledTasks(i));
        }
        return list;
    }


    public void addScheduledTask(int proc, Task task) {
    	List<Task> tempList = new ArrayList<>();
    	
    	if (this.scheduledTasks.get(proc) == null) {
    		tempList.add(task);
            this.scheduledTasks.put(proc, tempList);
    	}
    	else {
            tempList = this.scheduledTasks.get(proc);
            tempList.add(task);
            this.scheduledTasks.put(proc, tempList);
    	}
    
        
    }

}
