package  MVC.Model;

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

		List<Node> openNodes = new ArrayList<>();
		Node node = new Node();
		node.setUnscheduledTasks(tasks);
		node.setUpperBound(calcUpperBound(new Node(node), numProc));
		node.setLowerBound(calcLowerBound(node, numProc));

		boolean algoNotFinished = true;
		double bestUpperBound = node.getUpperBound();
		
		int startTime = 0;
		double upperBound;
		double lowerBound;
		
		while (algoNotFinished) {
			System.out.println("=====================");
			
			System.out.println("=====================");
			
			for (Task t : new ArrayList<>(node.getUnscheduledTasks())) {
				
				//System.out.println("task number: " + t.getNodeNumber());
				for(Task parent : t.getParentTasks()) {
					//System.out.println("parent number: " + parent.getNodeNumber());
				}
				
				//System.out.println(node.getScheduledTasks().containsAll(t.getParentTasks()));
				
				for (int i = 1; i < numProc + 1; i++) {
					Node childNode = new Node(node);
					if (containsParents(node, t) || t.getParentTasks().isEmpty()) {
						childNode.removeUnscheduledTask(t);
						t = new Task(t);
						//System.out.println("task number predicate 1: " + t.getNodeNumber());
						t.setProcessor(i);
						if(t.getNodeNumber() == 5)
						{
							//System.out.println("ok");
						}
						startTime = getStartTime(i, t, childNode);
						t.setStatus(startTime + t.getWeight());
						t.setStartTime(startTime);
						childNode.addScheduledTask(t);
						childNode.addTasksToProcessor(t, i);

						upperBound = calcUpperBound(new Node(childNode), numProc);						

						childNode.setUpperBound(upperBound);						
						
						lowerBound = calcLowerBound(childNode, numProc);
						
						childNode.setLowerBound(lowerBound);
						
						for(Task ts : childNode.getUnscheduledTasks()) {
							System.out.println("unscheduled: " + ts.getNodeNumber() + " proc num: " + ts.getProcessor());
						}
						for(Task sch : childNode.getScheduledTasks()) {
							System.out.println("scheduled: " + sch.getNodeNumber() + " proc num: " + sch.getProcessor());
						}
						System.out.println("upper bound = " + upperBound);
						System.out.println("lower bound = " + lowerBound);
						

						if (upperBound < bestUpperBound) {
							System.out.println("task number predicate 2: " + t.getNodeNumber());
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
						//	System.out.println("task number predicate 3: " + t.getNodeNumber());
							continue;
						}
						else {
							
							//System.out.println("task number added to list: " + t.getNodeNumber());
							openNodes.add(childNode);
						}
						
						
					}
				}
			}
			
		//	System.out.println(openNodes.size());
			
			Node minNode = openNodes.get(0);
			double bestLowerBound = minNode.getLowerBound();
			//System.out.println("best bound:" + bestLowerBound);
			for (Node n : openNodes) {
				if (n.getLowerBound() < bestLowerBound) {
					if(n.getScheduledTasks().size() > 0) {
				//		System.out.println("min node selected: " + n.getScheduledTasks().get(0).getNodeNumber());
					}
					minNode = n;
					bestLowerBound = n.getLowerBound();
				}
			}

			openNodes.remove(node);
			node = minNode;
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
		List<Task> unscheduledTasks = node.getUnscheduledTasks();


		while (!unscheduledTasks.isEmpty()) {
			Task task = null;
			double minStartTime = Double.POSITIVE_INFINITY;


			for (Task t : unscheduledTasks) {
				if (containsParents(node, t) || t.getParentTasks().isEmpty()) {
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
		for(Task sch : node.getScheduledTasks()) {
			System.out.println("scheduled upper bound: " + sch.getNodeNumber() + " proc num: " + sch.getProcessor());
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

		//System.out.println("Processor " +  proc + " Node number:" + task.getNodeNumber() + " max "  + Collections.max(allStartTimes));
		return Collections.max(allStartTimes);
	}

}
