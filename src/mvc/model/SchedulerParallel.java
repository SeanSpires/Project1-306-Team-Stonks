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

public class SchedulerParallel {
	
	private PriorityBlockingQueue<Node> openNodes;
	private ForkJoinPool forkJoinPool;
	
	private AtomicLong bestLowerBound;
	private AtomicLong bestUpperBound;
	private HashSet<Integer> closed;
	
	public Node createOptimalSchedule(List<Task> tasks, int numProc, int numCores, MenuController controller) {

		this.closed = new HashSet<>();
		
		this.openNodes = new PriorityBlockingQueue<>();
		this.forkJoinPool = new ForkJoinPool(numCores);
		this.bestLowerBound = new AtomicLong();
		this.bestUpperBound = new AtomicLong();

		bestUpperBound.set(Long.MAX_VALUE);
		
		Node n = new Node();
		n.setUnscheduledTasks(tasks);
		openNodes.add(n);
		ArrayList<SchedulerParallelTask> schedulerTasks = new ArrayList<SchedulerParallelTask>();
		AtomicBoolean done = new AtomicBoolean();
		done.set(false);
		
		for(int i = 0 ; i < numCores; i++) {
			SchedulerParallelTask t = new SchedulerParallelTask(openNodes, numProc, bestLowerBound, bestUpperBound, closed, done, controller);
			schedulerTasks.add(t);
			forkJoinPool.invoke(t);
		}
		
		Node out = null;
		for (SchedulerParallelTask t : schedulerTasks) {
			Node joined = (Node) t.join();
			if(joined == null){
				continue;
			}
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
