package mvc.model;

import mvc.controller.MenuController;


import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class Schedule {
	
    private LinkedHashMap<Task, Integer> tasks = new LinkedHashMap<Task, Integer>();

    private MenuController controller;

    
    public LinkedHashMap<Task, Integer> getTasks() {
        return tasks;
    }


    Schedule(MenuController controller) {
        this.controller = controller;
    }

    public Task getLastTask() {
    	final Iterator itr = tasks.keySet().iterator();
        Task lastElement = (Task) itr.next();
        while(itr.hasNext()) {
            lastElement = (Task) itr.next();
        }
        return lastElement;
    }

    /**
     * Method used just for tests
     * @param tasks
     */

    public void setTasks(LinkedHashMap<Task, Integer> tasks) {
        this.tasks = tasks;
    }
    
    public void addTask(Task task, int startTime) {
    	tasks.put(task, startTime);

    	controller.updateGraph(tasks);
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