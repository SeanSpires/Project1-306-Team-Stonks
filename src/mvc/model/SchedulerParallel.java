package mvc.model;

import mvc.controller.MenuController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

public class SchedulerParallel {
	
	private PriorityBlockingQueue<Node> openNodes;
	private ForkJoinPool forkJoinPool;
	
	private AtomicLong bestLowerBound;
	private AtomicLong bestUpperBound;
	
	
	public Node createOptimalSchedule(List<Task> tasks, int numProc, int numCores, MenuController controller) {
		
		this.openNodes = new PriorityBlockingQueue<>();
		this.forkJoinPool = new ForkJoinPool(numCores);
		this.bestLowerBound = new AtomicLong();
		this.bestUpperBound = new AtomicLong();
		
		Node n = new Node();
		n.setUnscheduledTasks(tasks);
		n.setUpperBound(calcUpperBound(new Node(n), numProc));
		n.setLowerBound(calcLowerBound(n, numProc));
		openNodes.add(n);
		bestUpperBound.set((long) n.getUpperBound());
		
		ArrayList<SchedulerParallelTask> schedulerTasks = new ArrayList<SchedulerParallelTask>();
		
		for(int i = 0 ; i < numCores; i++) {
			SchedulerParallelTask t = new SchedulerParallelTask(openNodes, numProc, bestLowerBound, bestUpperBound, controller);
			schedulerTasks.add(t);
			forkJoinPool.invoke(t);
		}
		
		Node out = null;
		
		boolean notFinished = true;
		
		while (notFinished) {
				for (SchedulerParallelTask t : schedulerTasks) {
					if (t.isDone()) {
						try {
							out = (Node) t.get();
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (ExecutionException e) {
							e.printStackTrace();
						}
						finally {
							if(controller==null){
								//Do nothing
							} else {
								controller.updateGraph(out.getScheduledTasks());
							}
							notFinished = false;
							forkJoinPool.shutdownNow();
						}
					}
				}
		}
		return out;
	}
	
	

	private boolean containsParents(Node node, Task t) {

		List<Task> scheduled = node.getScheduledTasks();

		for (Task parent : t.getParentTasks()) {
			boolean found = false;
			for (Task tScheduled : scheduled) {
				if (tScheduled.getNodeNumber() == parent.getNodeNumber()) {
					found = true;
					break;
				}
			}

			if (!found) {
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
		long sum = 0;

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

		List<Integer> allStartTimes = new ArrayList<>();

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
