package  MVC.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class Scheduler {

    public Schedule createBasicSchedule(List<Task> tasks, int processor) {

        Schedule schedule = new Schedule();
    	Queue<Task> taskQueue = new LinkedList<>();
        List<Task> rootTasks = getRootTasks(tasks);

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
    
    public Schedule createOptimalSchedule (List<Task> tasks, int numberOfProcessors) {
    	
    	List<Task> rootTasks = getRootTasks(tasks);
    	List<Node<Schedule>> openNodes = new ArrayList<>();
    	Schedule rootSchedule = new Schedule();
    	double bestUpperBound = Double.POSITIVE_INFINITY;
    	
    	Node<Schedule> rootNode = new Node<Schedule>();
    	rootNode.setData(rootSchedule);
    	openNodes.add(rootNode);
    	
    	HashMap<Task, Integer> endTimeOfTasks = new HashMap<>();
    	
    	for (Task t : tasks) {
    		endTimeOfTasks.put(t, -1);
    	}	
        	
    	for (Task t : tasks) {
    		for (int i = 0; i < numberOfProcessors; i++) {
    	    	Node<Schedule> childNode = rootNode;
    	    	Schedule  schedule = childNode.getData();
    	    	
    	    	if(schedule.getTasks().keySet().contains(t.getParentTasks())) {
    	    		t.setProcessor(i);
    	    		int startTime = calcStartTime(t, childNode.getData(), i);
    	    		t.setStatus(t.getWeight() + startTime);
    	    		schedule.addTask(t, startTime);
    	    		
    	    		int makeSpan = calcMakeSpan(schedule);
    	    		double lowerBound = calcLowerBound(tasks, schedule,makeSpan, numberOfProcessors);
    	    		double upperBound = calcUpperBound(tasks, schedule, numberOfProcessors);
    	    		
    	    		childNode.setLowerBound(lowerBound);
    	    		childNode.setUpperBound(upperBound);
    	    		
    	    		openNodes.add(childNode);
    	    		
    	    		if (upperBound < bestUpperBound) {
    	    			bestUpperBound = upperBound;
    	    			
    	    			for (Node<Schedule> node : openNodes) {
    	    				if (node.getLowerBound() > bestUpperBound) {
    	    					openNodes.remove(node);
    	    				}
    	    			}
    	    		}
    	    		
    	    		if (lowerBound > upperBound) {
    	    			openNodes.remove(childNode);
    	    		}
    	    	}
    		}
    	}
    	
    	
    	Node<Schedule> node = openNodes.get(0);
    	
    	for (Node<Schedule> n : openNodes) {
    		if (n.getLowerBound() < node.getLowerBound()) {
    			node = n;
    		}
    	}
    	
    	rootNode = node;
    	
    	if (rootNode.getLowerBound() == rootNode.getUpperBound() && rootNode.getData().isComplete(tasks)) {
    		return rootNode.getData();
    	}
    	else {
    		
    	}
    	
    	
    	
    	return null;
    }
    


	private int calcMakeSpan(Schedule schedule) {

		int makeSpan = 0;
		Set<Task> tasks = schedule.getTasks().keySet();
		for (Task t : tasks) {
			if (makeSpan < t.getStatus()) {
				makeSpan = t.getStatus();
			}
		}
		
		return makeSpan;
	}

	private int calcComCost(Task task, Task prevTask, int processor) {
    	int costOfComs = 0;
    	
    	if (prevTask.getProcessor() == processor) {
    		return costOfComs;
    	}
    	else {
    		costOfComs = prevTask.getSubTasks().get(task);
    		return costOfComs;
    	}
    }
    
    private int calcStartTime(Task currentTask, Schedule schedule, int processor) {
    	int maxStartTime = 0;
    	Set<Task> predecessors = schedule.getTasks().keySet();
    	
    	for (Task t : predecessors) {
    		int comCost = calcComCost(currentTask, t, processor);
    		int startTime = schedule.getTasks().get(t) + comCost;
    		if (maxStartTime <  startTime) {
    			maxStartTime =  startTime;
    		}
    	}
    	
    	return maxStartTime;
    	
    }
    
    private double calcLowerBound(List<Task> tasks,Schedule schedule, int makeSpan, int numberOfProcessors) {
    	
    	int sum = 0;
    	
    	tasks.removeAll(schedule.getTasks().keySet());
    	
    	for (Task t : tasks) {
    		sum += t.getWeight();
    	}
    	
    	return (sum/numberOfProcessors) + makeSpan;

    }
    
    private double calcUpperBound(List<Task> tasks, Schedule schedule, int numberOfProcessors) {
    	int makeSpan;
    	int startTime;
    	Task task;
    	
    	tasks.removeAll(schedule.getTasks().keySet());

    	
    	while (!tasks.isEmpty()) {
    		for (int i = 0; i < numberOfProcessors; i++) {
    			task = getMinTask(tasks, schedule);
    			startTime = calcStartTime(task, schedule, i);
    			schedule.addTask(task, startTime);
    			task.setStatus(startTime + task.getWeight()); 
    			tasks.remove(task);
    		}
			
    	}
    	
    	makeSpan = calcMakeSpan(schedule);
    
		return makeSpan;
	}

	private Task getMinTask(List<Task> unscheduledTasks, Schedule schedule) {
		Task task = unscheduledTasks.get(0);		
		
		for (Task t : unscheduledTasks) {
			if (schedule.getTasks().keySet().containsAll(t.getParentTasks())) {
				if (t.getWeight() < task.getWeight()) {
					task = t;
				}
			}
		}
		
		return task;
	}
    
    
    

}
