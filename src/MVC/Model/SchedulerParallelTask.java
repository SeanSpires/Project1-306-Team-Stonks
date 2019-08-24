package MVC.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicLong;

public class SchedulerParallelTask extends RecursiveTask {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private PriorityBlockingQueue<Node> openNodes;
	
	private AtomicLong bestUpperBound;	
	
	private static Node bestSchedule = null;

	private int numProc;


	public SchedulerParallelTask(PriorityBlockingQueue<Node> openNodes, int numProc,
			AtomicLong bestLowerBound, AtomicLong bestUpperBound) {

		this.numProc = numProc;
		this.openNodes = openNodes;

		this.bestUpperBound = bestUpperBound;

	}

	@Override
	protected Object compute() {
		long upperBound;
		long lowerBound;
		int startTime = 0;

		while (true) {

			Node node = openNodes.poll();
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
							for (Node n : new ArrayList<>(openNodes)) {
								if (n.getLowerBound() > bestUpperBound.get()) {
									openNodes.remove(n);
								}
							}
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
			if (node.getLowerBound() == node.getUpperBound() && node.getUnscheduledTasks().isEmpty()) {
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
	
	private static synchronized void setBest(Node best) {
		if(bestSchedule == null) {
			bestSchedule = best;
		}
	}
	
	public static Node getBest() {
		return bestSchedule;
	}



	private int calcLowerBound(Node node, int numProc) {

		double makeSpan = 0;
		int sum = 0;
		List<Task> unscheduledTasks = node.getUnscheduledTasks();
		for (Task t : unscheduledTasks) {
			sum += t.getWeight();
		}

		makeSpan = calcMakeSpan(node);
		makeSpan += (sum / numProc);

		return (int) makeSpan;

	}


	private long calcUpperBound(Node node, int numProc) {

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

		makeSpan = calcMakeSpan(node);

		return makeSpan;
	}

	private int getStartTime(int proc, Task task, Node node) {
		int comCost = 0;
		int endTime = 0;
		List<Integer> allStartTimes = new ArrayList<> ();
		allStartTimes.add(0);			

		for (Task t : task.getParentTasks()) {
			//System.out.println("task parent get start:" + t.getNodeNumber());
			t = node.getTaskByNumber(t.getNodeNumber());
			if (t.getProcessor() == proc) {
				comCost = 0;
				endTime = t.getStatus();
			}
			else {
				//System.out.println("key:" + t.getSubTasks().containsKey(task.getNodeNumber()));
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

		//System.out.println("Processor " +  proc + " Node number:" + task.getNodeNumber() + " max "  + Collections.max(allStartTimes));
		return Collections.max(allStartTimes);
	}



}
