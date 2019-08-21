package  MVC.Model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class Scheduler {

    public Schedule createBasicSchedule(List<Task> tasks, int processor) {

        Schedule schedule = new Schedule();
    	Queue<Task> taskQueue = new LinkedList<>();
        List<Task> rootTasks = getRootTasks(tasks);
        List<Integer> list = new ArrayList<>();

        Task currentTask;
        Set<Task> subTasks;
        
        taskQueue.addAll(rootTasks);
        int startTime = 0;
        
        while (!(taskQueue.isEmpty())) {
        	currentTask = taskQueue.element();
        	subTasks = currentTask.getSubTasks().keySet();
        	taskQueue.removeAll(subTasks);   //remove duplicate subtasks
        	taskQueue.addAll(subTasks);
        	taskQueue.remove(currentTask);
        	
        	currentTask.setProcessor(processor);
        	schedule.addTask(currentTask, startTime);
        	list.add(currentTask.getNodeNumber());

        	startTime = startTime + currentTask.getWeight();
        }
            
        return schedule;

    }

    private List<Task> getRootTasks(List<Task> tasks) {
        List<Task> rootTasks = new ArrayList<>();

        // if a task has no parent it must be a root 
        for (Task t : tasks) {
            if (t.getParentTasks().isEmpty()) {
                rootTasks.add(t);
            }
        }

        return rootTasks;
    }

}