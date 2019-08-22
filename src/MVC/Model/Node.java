package MVC.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Node {




    private List<Task> unscheduledTasks;

    // Map of scheduled tasks
    private HashMap<Integer, List<Task>> scheduledTasks;
    // Computation time (end time) for each scheduled task
    private HashMap<Task, Integer> computationTimes;
    // List of nodes to be expanded
    private List<Node> openNodes;
    // Upper Bound
    private int upperBound;
    // Lower Bound
    private int lowerBound;

    public Node(Node node) {
        this.unscheduledTasks = node.getUnscheduledTasks();
        this.scheduledTasks = node.getScheduledTasks();
        this.computationTimes = node.getComputationTimes();
        this.openNodes = node.getOpenNodes();
        this.upperBound = node.getUpperBound();
        this.lowerBound = node.getLowerBound();
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

    public HashMap<Task, Integer> getComputationTimes() {
        return computationTimes;
    }

    public void setComputationTimes(HashMap<Task, Integer> computationTimes) {
        this.computationTimes = computationTimes;
    }

    public void addComputationTime(Task task, int time) {
        this.computationTimes.put(task,time);
    }

    public HashMap<Integer, List<Task>> getScheduledTasks() {
        return scheduledTasks;
    }

    public void setScheduledTasks(HashMap<Integer, List<Task>> scheduledTasks) {
        this.scheduledTasks = scheduledTasks;
    }

    public List<Node> getOpenNodes() {
        return openNodes;
    }

    public void setOpenNodes(List<Node> openNodes) {
        this.openNodes = openNodes;
    }


    public void addOpenNode(Node node) {
        this.openNodes.add(node);
    }

    public List<Task> getScheduledTasks(int processor) {
        return scheduledTasks.get(processor);
    }


    public int getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(int upperBound) {
        this.upperBound = upperBound;
    }

    public int getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(int lowerBound) {
        this.lowerBound = lowerBound;
    }


    public void addScheduledTask(int proc, Task task) {
        List<Task> tempList = this.scheduledTasks.get(proc);
        this.scheduledTasks.remove(proc);
        tempList.add(task);
        this.scheduledTasks.put(proc, tempList);
    }

}
