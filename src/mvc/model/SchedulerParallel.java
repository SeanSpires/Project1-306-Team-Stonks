package mvc.model;

import mvc.controller.MenuController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;


/**
 * The SchedulerParallel class is responsable for creating an optimal scheudle (Node). However, it unlike the scheduler class
 * it creates an optimal schedule using multiple threads. 
 */
public class SchedulerParallel {
	
	private PriorityBlockingQueue<Node> openNodes; 
	private ForkJoinPool forkJoinPool;
	
	// use atomic variables to make it threadsafe
	private AtomicLong bestLowerBound;
	private AtomicLong bestUpperBound;
	private HashSet<Integer> closed;
	
	
	/**
     	* Create an optimal schedule using branch & bound A* algorithm.
	* Takes in a list of unschedulded tasks and the number of processors to create an optimal
	* schedule based off these two inputs. A menucontroller is passed in to update the frontend
	* during runtime.
	* This method also uses multiple threads to compute the an optimal schedule
    	*/
	public Node createOptimalSchedule(List<Task> tasks, int numProc, int numCores, MenuController controller) {

		this.closed = new HashSet<>();
		
		this.openNodes = new PriorityBlockingQueue<>();
		this.forkJoinPool = new ForkJoinPool(numCores); //create as many threads as numcores
		this.bestLowerBound = new AtomicLong();
		this.bestUpperBound = new AtomicLong();

		bestUpperBound.set(Long.MAX_VALUE);
		
		Node n = new Node(); //create root node
		n.setUnscheduledTasks(tasks);
		openNodes.add(n);
		ArrayList<SchedulerParallelTask> schedulerTasks = new ArrayList<SchedulerParallelTask>();
		AtomicBoolean done = new AtomicBoolean();
		done.set(false);
		
		// Add a SchedulerParallelTask object to each thread and then invoke that thread
		for(int i = 0 ; i < numCores; i++) {
			SchedulerParallelTask t = new SchedulerParallelTask(openNodes, numProc, bestLowerBound, bestUpperBound, closed, done, controller);
			schedulerTasks.add(t);
			forkJoinPool.invoke(t);
		}
		
		Node out = null;
		for (SchedulerParallelTask t : schedulerTasks) {
			Node joined = (Node) t.join(); //wait for each thread to finish
			if(joined == null){
				continue;
			}
			// find thread which returned node with the lowest upper bound
			if(out == null || joined.getUpperBound() < out.getUpperBound()) {
				out = joined;

				if(controller == null){
					//Do nothing
				} else {
					controller.updateGraph(out.getScheduledTasks());
				}
			}
		}
				
		return out;
		
	}

}
