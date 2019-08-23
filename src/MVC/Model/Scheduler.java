package  MVC.Model;

import java.util.*;

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

	public Node createOptimalSchedule (List<Task> tasks, int numberOfProcessors) {


		int numProc = numberOfProcessors;

		List<Node> openNodes = new ArrayList<>();
		Node node = new Node();
		node.setUnscheduledTasks(tasks);
		node.addOpenNode(node);
		openNodes.add(node);

		boolean algoNotFinished = true;

		while (algoNotFinished) {
			double bestUpperBound = Double.POSITIVE_INFINITY;
			int startTime = 0;
			double upperBound;
			double lowerBound;

			for (Task t : new ArrayList<>(node.getUnscheduledTasks())) {		
				for (int i = 1; i < numProc + 1; i++) {
					Node childNode = new Node();
					childNode = node;
					if (node.getScheduledTasks().containsAll(t.getParentTasks()) || t.getParentTasks().isEmpty()) {
						t.setProcessor(i);
						System.out.println(t.getNodeNumber());
						startTime = getStartTime(i, t, childNode);
						t.setStatus(startTime + t.getWeight());
						t.setStartTime(startTime);
						childNode.addScheduledTask(t);
						childNode.addTasksToProcessor(t, i);
						childNode.removeUnscheduledTask(t);

						upperBound = calcUpperBound(childNode, numProc);
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

						if (childNode.getLowerBound() > childNode.getUpperBound()) {
							break;
						}
						else {
							openNodes.add(childNode);
						}
					}
				}

			}

			Node minNode = new Node();
			minNode = node;
			for (Node n : openNodes) {
				if (n.getLowerBound() < minNode.getLowerBound()) {
					minNode = n;
				}
			}

			node = minNode;

			if (node.getLowerBound() == node.getUpperBound() && node.getUnscheduledTasks().isEmpty()) {
				return node;
			}

		}

		return null;
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
		List<Task> unscheduledTasks = new ArrayList<>(node.getUnscheduledTasks());
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
			Task task = null;
			double minStartTime = Double.POSITIVE_INFINITY;


			for (Task t : unscheduledTasks) {
				if (node.getScheduledTasks().contains(t.getParentTasks()) || t.getParentTasks().isEmpty()) {
					task = t;
					break;
				}
			}

			if (task == null) {
				return calcMakeSpan(node);
			}

			int tempStartTime = 0;
			int bestProc = 1;
			for (int i = 1; i <= numProc; i++) {
				tempStartTime = getStartTime(i, task, node);

				if (minStartTime > tempStartTime) {
					minStartTime = tempStartTime;
					bestProc = i;
				}
			}


			task.setProcessor(bestProc);
			task.setStatus((int) minStartTime + task.getWeight());
			node.addTasksToProcessor(task, bestProc);
			node.addScheduledTask(task);
			node.removeUnscheduledTask(task);
			unscheduledTasks.remove(task);
		}

		makeSpan = calcMakeSpan(node);

		return makeSpan;
	}

	private int getStartTime(int proc, Task task, Node node) {
		int comCost = 0;
		int endTime = 0;
		List<Integer> allStartTimes = new ArrayList<> ();
		allStartTimes.add(0);			

		for (Task t : task.getParentTasks()) {
			if (t.getProcessor() == proc) {
				comCost = 0;
				endTime = t.getStatus();
			}
			else {
				comCost = t.getSubTasks().get(task);
				endTime = t.getStatus();				
			}
			allStartTimes.add(comCost + endTime);
		}

		for (Task t : node.getTasksForProcessor(proc)) {
			comCost = 0;
			endTime = t.getStatus();
			allStartTimes.add(comCost + endTime);
		}

		System.out.println("Processor " +  proc + " Node number:" + task.getNodeNumber() + " max "  + Collections.max(allStartTimes));
		return Collections.max(allStartTimes);
	}

}
