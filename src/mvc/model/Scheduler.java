package  mvc.model;


import java.util.*;

public class Scheduler {
/*
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

*/

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

	public Node createOptimalSchedule (List<Task> tasks, int numProc) {

		PriorityQueue<Node> openNodes = new PriorityQueue<>();
		Node node = new Node();
		node.setUnscheduledTasks(tasks);
		//node.setUpperBound(calcUpperBound(new Node(node), numProc));
		//node.setLowerBound(calcLowerBound(node, numProc));

		boolean algoNotFinished = true;
		double bestUpperBound = node.getUpperBound();
		
		int startTime = 0;
		double upperBound;
		double lowerBound;
		
		while (algoNotFinished) {
			for (Task t : new ArrayList<>(node.getUnscheduledTasks())) {			
				for (int i = 1; i < numProc + 1; i++) {
					Node childNode = new Node(node);
					if (containsParents(node, t) || t.getParentTasks().isEmpty()) {
						childNode.removeUnscheduledTask(t);
						t = new Task(t);
						t.setProcessor(i);
						startTime = getStartTime(i, t, childNode);
						t.setStatus(startTime + t.getWeight());
						t.setStartTime(startTime);
						childNode.addScheduledTask(t);
						childNode.addTasksToProcessor(t, i);

						upperBound = calcUpperBound(new Node(childNode), numProc);						

						childNode.setUpperBound(upperBound);						
						
						lowerBound = calcLowerBound(childNode, numProc);
						
						childNode.setLowerBound(lowerBound);                                                                   				

						if (upperBound < bestUpperBound) {
							bestUpperBound = upperBound;
							for (Node n : new ArrayList<>(openNodes)) {
								if (n.getLowerBound() > bestUpperBound) {
									openNodes.remove(n);
								}
							}			
						}
						//System.out.println("node lower: " + childNode.getLowerBound());
						///System.out.println("node upper: " + childNode.getUpperBound());
						if (childNode.getLowerBound() > childNode.getUpperBound()) {
							continue;
						}
						else {			
							openNodes.add(childNode);
						}			
					}
				}
			}
			
			
			Node minNode = openNodes.poll();

			node = new Node(minNode);
			if (node.getLowerBound() == node.getUpperBound() && node.getUnscheduledTasks().isEmpty()) {
				return node;
			}


		}
		return null;
	}

	
	private boolean containsParents(Node node, Task t) {
		
		List<Task> scheduled = node.getScheduledTasks();		
		
		for(Task parent : t.getParentTasks()) {
			boolean found = false;
			for(Task tScheduled : scheduled) {
				if(tScheduled.getNodeNumber() == parent.getNodeNumber()) {
					found = true;
					break;
				}
			}
			
			if(!found) {
				return false;
			}
		}		
		return true;
	}


	private int calcMakeSpan(Node node) {
		int makeSpan = 0;
		int temptCompTime = 0;
		List<Task> tasks = node.getScheduledTasks();

		for (Task t : tasks) {
			temptCompTime = t.getStatus();
			if (temptCompTime > makeSpan) {
				makeSpan = temptCompTime;

			}
		}

		return makeSpan;
	}




	private double calcLowerBound(Node node, int numProc) {

		double makeSpan = 0;
		int sum = 0;
		List<Task> unscheduledTasks = node.getUnscheduledTasks();
		for (Task t : unscheduledTasks) {
			sum += t.getWeight();
		}

		makeSpan = calcMakeSpan(node);
		makeSpan += (sum / numProc);

		return makeSpan;

	}


	private double calcUpperBound(Node node, int numProc) {
		
		int makeSpan = 0;
		List<Task> unscheduledTasks = new ArrayList<>(node.getUnscheduledTasks());


		while (!unscheduledTasks.isEmpty()) {
			Task t = null;
			for (int i = 1; i < numProc + 1; i++) {
				t = PickGreedyTask(node);
				
				if (t == null) {
					return calcMakeSpan(node);
				}
				
				int startTime = getStartTime(i, t, node);
				t.setStatus(startTime + t.getWeight());
				node.addScheduledTask(t);
				node.addTasksToProcessor(t, i);
				node.removeUnscheduledTask(t);
				unscheduledTasks.remove(t);
			}
		}
		
		makeSpan = calcMakeSpan(node);
		return makeSpan;

	}

	private Task PickGreedyTask(Node node) {
		List<Task> tasks = new ArrayList<>();
		
		for (Task t : node.getUnscheduledTasks()) {
			if (containsParents(node, t) || t.getParentTasks().isEmpty()) {
				tasks.add(t);
			}
		}
		if (tasks.isEmpty()) {
			return null;
		}
		Task minTask = tasks.get(0);
 
		for (Task t: tasks) {
			if (t.getWeight() < minTask.getWeight()) {
				minTask = t;

			}
		}
		
		return minTask;
	}

	private int getStartTime(int proc, Task task, Node node) {
		int comCost = 0;
		int endTime = 0;
		
		List<Integer> allStartTimes = new ArrayList<> ();
		
		allStartTimes.add(0);
		for (Task t : task.getParentTasks()) {
			t = node.getTaskByNumber(t.getNodeNumber());
			
			if (t.getProcessor() != proc) {
				comCost = t.getSubTasks().get(task.getNodeNumber());
				endTime = t.getStatus();	
			}
			
			allStartTimes.add(comCost + endTime);
		}
		
		for (Task t : node.getTasksForProcessor(proc)) {
			endTime = t.getStatus();
			allStartTimes.add(endTime);
		}

		return Collections.max(allStartTimes);
	}

}
