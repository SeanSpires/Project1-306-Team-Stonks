package mvc.model;

import mvc.controller.MenuController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicLong;

public class SchedulerParallelTask extends RecursiveTask<Object> {

	private static final long serialVersionUID = 1L;

	private PriorityBlockingQueue<Node> openNodes;

	private AtomicLong bestUpperBound;	

	private int numProc;

	private MenuController controller;

	public SchedulerParallelTask(PriorityBlockingQueue<Node> openNodes, int numProc,
			AtomicLong bestLowerBound, AtomicLong bestUpperBound, MenuController controller) {

		this.numProc = numProc;
		this.openNodes = openNodes;
		this.bestUpperBound = bestUpperBound;
		this.controller = controller;

	}

	@Override
	protected Object compute() {
		long upperBound;
		long lowerBound;
		int startTime = 0;
		Node node = new Node(openNodes.poll());


		while (true) {		
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

						if (upperBound < bestUpperBound.get()) {
							bestUpperBound.set(upperBound);	
						}

						if (childNode.getLowerBound() > childNode.getUpperBound()) {
							continue;
						}
						else {
							openNodes.add(childNode);
						}
					}
				}
			}

			node = new Node(openNodes.poll());
			
			controller.updateGraph(node.getScheduledTasks());
			if (node.getLowerBound() == bestUpperBound.get() && node.getUnscheduledTasks().isEmpty()) {
				return node;
			}
		}
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


	private long calcLowerBound(Node node, int numProc) {

		double makeSpan = 0;
		int sum = 0;
		List<Task> unscheduledTasks = node.getUnscheduledTasks();
		for (Task t : unscheduledTasks) {
			sum += t.getWeight();
		}

		makeSpan = calcMakeSpan(node);
		makeSpan += (sum / numProc);

		return (long) makeSpan;
	}



	private long calcUpperBound(Node node, int numProc) {

		List<Task> unscheduledTasks = new ArrayList<>(node.getUnscheduledTasks());
		int makeSpan = 0;

		while (!unscheduledTasks.isEmpty()) {
			Task t = null;
			for (int i = 1; i < numProc + 1; i++) {
				t = PickGreedyTask(node);

				if (t == null) {
					return calcMakeSpan(node);
				}

				int startTime = getStartTime(i, t, node);
				t.setStatus(startTime + t.getWeight());
				t.setProcessor(i);
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

		for (Task t : tasks) {
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
			if (t.getProcessor() == proc) {
				comCost = 0;
				endTime = t.getStatus();
			}
			else {
				comCost = t.getSubTasks().get(task.getNodeNumber());
				endTime = t.getStatus();				
			}
			allStartTimes.add(comCost + endTime);
		}

		for (Task t : node.getTasksForProcessor(proc)) {
			comCost = 0;
			endTime = t.getStatus();
			allStartTimes.add(comCost + endTime);
		}

		return Collections.max(allStartTimes);
	}



}
