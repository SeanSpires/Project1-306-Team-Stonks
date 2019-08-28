package mvc.model;

import mvc.controller.MenuController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;


/**
* This class is used in the parallelization of the branch and bound A* algorithm
* Objects of this class and created and scheduled on mulitiple threads in the
* SchedulerParallel class. Some information of this class is shared between
* different instances of this class's objects. This is made threadsafe by using
* atomic variables.
*/
public class SchedulerParallelTask extends RecursiveTask {
	
	private static final long serialVersionUID = 1L;

	private PriorityBlockingQueue<Node> openNodes;
	
	private AtomicLong bestUpperBound;	
	

	private int numProc;

	private HashSet<Integer> closed;

	private static AtomicBoolean done;
	private MenuController controller;

	/**
	* A standard constructor taking inputs and setting the appropriate fields. 
	*/
	public SchedulerParallelTask(PriorityBlockingQueue<Node> openNodes, int numProc,
			AtomicLong bestLowerBound, AtomicLong bestUpperBound, HashSet<Integer> closed, AtomicBoolean done, MenuController controller) {
		this.done = done;
		this.closed = closed;
		this.numProc = numProc;
		this.openNodes = openNodes;

		this.bestUpperBound = bestUpperBound;
		this.controller = controller;


	}

	/**
	* When a thread with this class's object is invoked. This compute method is called.
	* The compute's algorithmic to find an optimal schedule is nearly identical to the
	* algorithmic logic in the createOptimalSchedule() method in the Scheduler class.
	* The main difference is that the compute() method deals with some atomic variables
	* as certain variables are shared between threads. When the compute method ends when an
	* optimal node is found, the method then returns this node is found.
	*/
	@Override
	protected Object compute() {
		double upperBound;
		int startTime = 0;

		while (true) {

			Node node = openNodes.poll();

			if(controller == null){
				//Do nothing
			} else {
				controller.updateGraph(node.getScheduledTasks());
			}

			if(node == null){
				return null;
			}
			if(done.get()){
				if(node.getUnscheduledTasks().isEmpty()){
					return node;
				}
				return null;
			}
			if (node.getUnscheduledTasks().isEmpty()){
				done.set(true);
				return node;
			}
			closed.add(node.hashCode());
			for (Task t : new ArrayList<>(node.getUnscheduledTasks())) {
				for (int i = 1; i <= numProc; i++) {
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

						upperBound = Math.max(childNode.getLastBL(), t.getStartTime() + getComputationalBottomLevel(new Node(childNode), t));

						childNode.setLastBL(upperBound);

						childNode.setUpperBound(upperBound);

						if (upperBound >= bestUpperBound.get()) {
							continue;
						}
						if (closed.contains(childNode.hashCode())) {
							continue;
						}

						if (upperBound < bestUpperBound.get() && childNode.getUnscheduledTasks().isEmpty()) {
							bestUpperBound.set((long) upperBound);
						}

						openNodes.add(childNode);
					}
				}
			}
		}
	}
	
	
		
	/**
     	*  Computes the computational bottom level of the current schedule and the task to be added.
	*  The computational bottom level is the maximum potentional path through the schedule.
    	*/
	public int getComputationalBottomLevel(Node input, Task added) {
		if (added.getSubTasks().size() > 0) {
			int max = 0;
			for (Integer i : added.getSubTasks().keySet()) {
				Task t = added.getSubTasks().get(i);
				int current = (int) (getComputationalBottomLevel(input, t));
				if (max < current) {
					max = current;
				}
			}
			return max + (int) added.getWeight();
		} else {
			return (int) added.getWeight();
		}
	}

	/**
	* Helper function which checks if the parents of a specific task is scheduled in a schedule
	* If a task has no parents (i.e. is a root node), the function also returns true.
	*/
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

	/**
	* getStartTime takes in a task that wants to be scheduled on a specfic processor and the schedule that
	* it wants to be scheduled on. This function will calculate the start time of this for the schedule
	*/
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
				comCost = t.getSubTaskArcs().get(task.getNodeNumber());
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
