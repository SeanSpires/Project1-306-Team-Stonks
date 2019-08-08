package  sample.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Scheduler {

    private Queue<Schedule> schedules = new LinkedList<Schedule>();
    private List<Schedule> scheduleTree = new ArrayList<>();
    
    
    public Scheduler(Task rootTask, int numberOfProcessors) {
    	for (int i = 0; i < numberOfProcessors; i++) {
    		Schedule newSchedule = new Schedule();
    		newSchedule.addTask(rootTask, weight);
    	}
    }
    
    
    public void CreateScheduleTree (Task rootTask, int numberOfProcessors) {
    	HashMap<Task, Integer> reachableTasks = rootTask.getSubTasks();
    	CreateChildSchedules(schedules.element(), rootTask, reachableTasks, numberOfProcessors);   //queue up branches off root task 
    	schedules.remove();  //remove empty schedule from head of queue
    	
    	while (!(schedules.isEmpty())) {   //  end when queue is empty
    		CreateChildSchedules(schedules.element(), )
    	}
    }
    
  
    private void CreateChildSchedules(Schedule s, Task parentTask, HashMap<Task, Integer> reachableTasks, int numberOfProcessors) {
    	
        for (Task t : reachableTasks.keySet()) {
        	
            for (int i = 0; i < numberOfProcessors; i++){
            	
            	Schedule newSchedule = new Schedule();
            	int cumlativeWeight = 0;
            	newSchedule.setTasks(s.getTasks());
            	
                if (i == parentTask.getProcessor()) {
                	cumlativeWeight = s.getTasks().get(parentTask) + t.getWeight();
                    newSchedule.addTask(t, cumlativeWeight);
                }
                else {
                	cumlativeWeight = s.getTasks().get(parentTask) + reachableTasks.get(t) + t.getWeight();
                	newSchedule.addTask(t, cumlativeWeight);
                }
                schedules.add(newSchedule);
            }
        }
    }


}