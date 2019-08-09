package sample.Model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class Schedule {
	
    private LinkedHashMap<Task, Integer> tasks = new LinkedHashMap<Task, Integer>();

    public LinkedHashMap<Task, Integer> getTasks() {
        return tasks;
    }

    public void setTasks(LinkedHashMap<Task, Integer> tasks) {
        this.tasks = tasks;
    }
    
    public void addTask(Task task, int weight) {
    	tasks.put(task, weight);
    }

    public boolean isComplete(List<Task> allTasks) {
        for (Task t : allTasks) {
            if (!(tasks.containsKey(t))) {
                return false;
            }
        }
        return true;
    }



}