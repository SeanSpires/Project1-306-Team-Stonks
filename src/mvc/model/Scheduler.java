package  mvc.model;

import mvc.controller.MenuController;

import java.util.*;

public class Scheduler {

	public Schedule createBasicSchedule(List<Task> tasks, int processor, MenuController controller) {

		Schedule schedule = new Schedule(controller);
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

//	public Schedule createOptimalSchedule (List<Task> tasks, int numberOfProcessors) {
//
//		boolean notFinished = true;
//		List<Task> rootTasks = getRootTasks(tasks);
//		PriorityQueue<Node<Schedule>> openNodesQueue = new PriorityQueue<>(new NodeLowerBoundComparator());
//		Schedule rootSchedule = new Schedule();
//		double bestUpperBound = Double.POSITIVE_INFINITY;
//
//		Node<Schedule> rootNode = new Node<Schedule>();
//		rootNode.setData(rootSchedule);
//        openNodesQueue.add(rootNode);
//
//
//		while (notFinished) {
//            List<Task> unscheduledTasks = new ArrayList<>();
//			unscheduledTasks.addAll(tasks);
//
//			if (!rootNode.getData().getTasks().isEmpty()) {
//				unscheduledTasks.removeAll(rootNode.getData().getTasks().keySet());
//			}
//			else {
//				unscheduledTasks.addAll(tasks);
//			}
//
//			for (Task t : unscheduledTasks) {
//				for (int i = 1; i < numberOfProcessors + 1; i++) {
//					Node<Schedule> childNode = rootNode;
//					Schedule schedule = childNode.getData();
//
//					if(schedule.getTasks().keySet().contains(t.getParentTasks()) || t.getParentTasks().isEmpty()) {
//						t.setProcessor(i);
//						int startTime = calcStartTime(t, childNode.getData(), i);
//						t.setStatus(t.getWeight() + startTime);
//						schedule.addTask(t, startTime);
//		                unscheduledTasks.remove(t);
//
//						int makeSpan = calcMakeSpan(schedule);
//						double lowerBound = calcLowerBound(unscheduledTasks, schedule,makeSpan, numberOfProcessors);
//						double upperBound = calcUpperBound(unscheduledTasks, schedule, numberOfProcessors);
//
//						childNode.setLowerBound(lowerBound);
//						childNode.setUpperBound(upperBound);
//						childNode.setData(schedule);
//
//						if (upperBound < bestUpperBound) {
//							bestUpperBound = upperBound;
//
//							for (Node<Schedule> node : openNodesQueue) {
//								if (node.getLowerBound() > bestUpperBound) {
//									openNodesQueue.remove(node);
//								}
//							}
//						}
//
//
//						if (lowerBound > upperBound) {
//							openNodesQueue.remove(childNode);
//						}
//						else {
//							openNodesQueue.add(childNode);
//						}
//
//					}
//				}
//			}
//
//
//
//            // Get the node with the smallest lower bound
//			// Node<Schedule> node = openNodes.get(0);
//
////          Node<Schedule> node = openNodes.get(0);
////			for (Node<Schedule> n : openNodes) {
////				if (n.getLowerBound() < node.getLowerBound()) {
////					node = n;
////				}
////			}
//
//			if (!openNodesQueue.isEmpty()) {
//				rootNode = openNodesQueue.poll();
//			}
//			else {
//				return rootNode.getData();
//			}
//
//			if (rootNode.getLowerBound() == rootNode.getUpperBound() && rootNode.getData().isComplete(tasks)) {
//				return rootNode.getData();
//			}
//			else {
//				notFinished = false;
//			}
//		}
//
//		return null;
//	}



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

	private int calcComCost(Task currentTask, Task prevTask, int processor) {
		int costOfComs = 0;

		if (prevTask.getProcessor() == processor) {
			return costOfComs;
		}
		else {
			costOfComs = prevTask.getSubTasks().get(currentTask);
			return costOfComs;
		}
	}

	private int calcStartTime(Task currentTask, Schedule schedule, int processor) {
		int maxStartTime = 0;
		Set<Task> predecessors = schedule.getTasks().keySet();
		predecessors.remove(currentTask);

		for (Task t : predecessors) {
			int comCost = calcComCost(currentTask, t, processor);
			int startTime = t.getStatus() + comCost;
			if (maxStartTime <  startTime) {
				maxStartTime =  startTime;
			}
		}

		return maxStartTime;

	}

	private double calcLowerBound(List<Task> unscheduledTasks,Schedule schedule, int makeSpan, int numberOfProcessors) {

		int sum = 0;
		
        //unscheduledTasks.removeAll(schedule.getTasks().keySet());

		for (Task t : unscheduledTasks) {
			sum += t.getWeight();
		}

		return (sum/numberOfProcessors) + makeSpan;

	}

//	private double calcUpperBound(List<Task> unscheduledTasks, Schedule schedule, int numberOfProcessors) {
//		int makeSpan;
//		int startTime;
//		List<Task> tempUnscheduledTasks  = new  ArrayList<Task>(unscheduledTasks);
//
//		while (!tempUnscheduledTasks.isEmpty()) {
//
//			for (int i = 1; i < numberOfProcessors + 1; i++) {
//
//				if(tempUnscheduledTasks.isEmpty()) {
//					return calcMakeSpan(schedule);
//				}
//
//				Task task = pickGreedyTask(tempUnscheduledTasks, schedule);
//				startTime = calcStartTime(task, schedule, i);
//				task.setStatus(startTime + task.getWeight());
//				schedule.addTask(task, startTime);
//				tempUnscheduledTasks.remove(task);
//			}
//
//
//		}
//
//
//		makeSpan = calcMakeSpan(schedule);
//
//		return makeSpan;
//	}

	
	private Task pickGreedyTask(List<Task> tasks, Schedule schedule) {
		Task minTask = null;
		
		for (Task t : tasks) {
			if (schedule.getTasks().keySet().containsAll(t.getParentTasks())) {
				minTask = t;
				break;
			}
		}
				
		for (Task t : tasks) {
			if (schedule.getTasks().keySet().containsAll(t.getParentTasks())) {
				if (t.getWeight() < minTask.getWeight()) {
					minTask = t;
				}
			}
		}
		
		return minTask;
		
		
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
