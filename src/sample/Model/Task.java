package sample.Model;

import java.util.HashMap;
import java.util.List;

public class Task {

    private int weight;
    private Processor processor;
    List<Task> parentTasks;
    HashMap<Task, Integer> subTasks;

    public Task(int weight){
        this.weight = weight;

    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Processor getProcessor() {
        return processor;
    }

    public void setProcessor(Processor processor) {
        this.processor = processor;
    }

    public List<Task> getParentTasks() {
        return parentTasks;
    }

    public void setParentTasks(Task task) {
        this.parentTasks.add(task);
    }

    public HashMap<Task, Integer> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks() {
    }



}
