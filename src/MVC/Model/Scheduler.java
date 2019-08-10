package  MVC.Model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class Scheduler {

    private Queue<Schedule> schedules = new LinkedList<Schedule>();
    private List<Schedule> scheduleTree = new ArrayList<>();
        

    public Schedule createBasicSchedule(List<Task> tasks, int processor) {

        Schedule schedule = new Schedule();
    	Queue<Task> taskQueue = new LinkedList<>();
        List<Task> rootTasks = getRootTasks(tasks);
        List<Integer> list = new ArrayList<>();

        Task currentTask;
        Set<Task> subTasks;
        
        taskQueue.addAll(rootTasks);
        int cumulativeWeight = 0;

        while (!(taskQueue.isEmpty())) {
        	currentTask = taskQueue.element();
        	subTasks = currentTask.getSubTasks().keySet();
        	taskQueue.removeAll(subTasks);   //remove duplicate subtasks
        	taskQueue.addAll(subTasks);
        	taskQueue.remove(currentTask);
        	
        	cumulativeWeight = cumulativeWeight + currentTask.getWeight();
        	
        	currentTask.setProcessor(processor);
        	schedule.addTask(currentTask, cumulativeWeight);
        	list.add(currentTask.getNodeNumber());
        }
        
        System.out.println(list);
        
        return schedule;

    }

    private List<Task> getRootTasks(List<Task> tasks) {
        List<Task> rootTasks = new ArrayList<>();

        for (Task t : tasks) {
            if (t.getParentTasks().isEmpty()) {
                rootTasks.add(t);
            }
        }

        return rootTasks;
    }


    
//    public void CreateScheduleTree (Task rootTask, int numberOfProcessors) {
//    	HashMap<Task, Integer> reachableTasks = rootTask.getSubTasks();
//    	CreateChildSchedules(schedules.element(), rootTask, reachableTasks, numberOfProcessors);   //queue up branches off root task 
//    	schedules.remove();  //remove empty schedule from head of queue
//    	
//    	while (!(schedules.isEmpty())) {   //  end when queue is empty
//    		CreateChildSchedules(schedules.element(), )
//    	}
//    }
//    
//  
//    private void CreateChildSchedules(Schedule s, Task parentTask, HashMap<Task, Integer> reachableTasks, int numberOfProcessors) {
//    	
//        for (Task t : reachableTasks.keySet()) {
//        	
//            for (int i = 0; i < numberOfProcessors; i++){
//            	
//            	Schedule newSchedule = new Schedule();
//            	int cumlativeWeight = 0;
//            	newSchedule.setTasks(s.getTasks());
//            	
//                if (i == parentTask.getProcessor()) {
//                	cumlativeWeight = s.getTasks().get(parentTask) + t.getWeight();
//                    newSchedule.addTask(t, cumlativeWeight);
//                }
//                else {
//                	cumlativeWeight = s.getTasks().get(parentTask) + reachableTasks.get(t) + t.getWeight();
//                	newSchedule.addTask(t, cumlativeWeight);
//                }
//                schedules.add(newSchedule);
//            }
//        }
//    }


}