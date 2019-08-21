package  MVC.Model;

import java.util.*;

public class Scheduler {

    // Temporary Array
    private Integer[] taskArray;

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

        // Temporary
        Integer[] taskNodeNumber = new Integer[list.size()];
        taskNodeNumber = scheduleToIntArray(list);

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

    // Temporary Method
    private Integer[] scheduleToIntArray(List<Integer> list) {
        taskArray = new Integer[list.size()];
        for (int i = 0; i < list.size(); i++) {
            taskArray[i] = list.get(i);
        }
        System.out.println(Arrays.toString(taskArray));
        return taskArray;
    }

}