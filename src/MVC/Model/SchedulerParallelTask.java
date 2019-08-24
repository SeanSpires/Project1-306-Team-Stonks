package MVC.Model;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicLong;

public class SchedulerParallelTask extends RecursiveAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private PriorityBlockingQueue<Node> openNodes;
	
	private ForkJoinPool forkJoinPool;
	
	private AtomicLong bestLowerBound;
	private AtomicLong bestUpperBound;	
	
	
	public SchedulerParallelTask(PriorityBlockingQueue<Node> openNodes, ForkJoinPool forkJoinPool, 
			AtomicLong bestLowerBound, AtomicLong bestUpperBound) {
		
		this.openNodes = openNodes;
		this.forkJoinPool = forkJoinPool;
		this.bestLowerBound = bestLowerBound;
		this.bestUpperBound = bestUpperBound;
		
	}

	@Override
	protected void compute() {
		// TODO Auto-generated method stub
		
	}
	
	
}
