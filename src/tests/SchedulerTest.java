package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import mvc.model.Node;

import mvc.model.Scheduler;
import mvc.model.SchedulerParallel;
import mvc.model.Task;

public class SchedulerTest {

    private List<Task> tasks;
    private Scheduler scheduler;
    private SchedulerParallel schedulerParallel;

//	@Test
//	public void testSimpleSchedule() {
//
//    /*
//    * simpleSchedulerTest tests the basic scheduling algorithm
//    * with the simple example input from the project specification.
//    *
//    * Only one root task and one leaf task.
//    *
//    * */
//        tasks = new ArrayList<>();
//        scheduler = new Scheduler();
//
//        Task task1 = new Task(1);
//        Task task2 = new Task(2);
//        Task task3 = new Task(3);
//        Task task4 = new Task(4);
//
//        task1.setWeight(2);
//        task2.setWeight(3);
//        task3.setWeight(3);
//        task4.setWeight(2);
//
//        task1.setSubTasks(task2, 10);
//        task1.setSubTasks(task3, 10);
//
//        task2.setSubTasks(task4, 12);
//
//        task3.setSubTasks(task4, 12);
//
//
//        task2.setParentTasks(task1);
//        task3.setParentTasks(task1);
//
//        task4.setParentTasks(task2);
//        task4.setParentTasks(task3);
//
//        tasks.add(task3);
//        tasks.add(task2);
//        tasks.add(task1);
//        tasks.add(task4);
//
//		//Schedule s = scheduler.createBasicSchedule(tasks, 1);
//        Schedule s = scheduler.createOptimalSchedule(tasks, 1);
//		
//		List<Integer> scheduledTasks = new ArrayList<>();
//
//		
//		for (Task t : s.getTasks().keySet()) {
//			scheduledTasks.add(t.getNodeNumber());
//			System.out.println(t.getNodeNumber());
//		}
//		
//		List<Integer> scheduledTimes =  new ArrayList<>();
//
//		scheduledTimes.addAll(s.getTasks().values());
//
//        List<Integer> expectedTasks = new ArrayList<Integer>() {
//            {
//                add(1);
//                add(3);
//                add(2);
//                add(4);
//            }
//        };
//
//        List<Integer> expectedTimes = new ArrayList<Integer>() {
//            {
//                add(0);
//                add(2);
//                add(5);
//                add(8);
//
//            }
//        };
//
//		assertEquals(expectedTasks, scheduledTasks);
//		assertEquals(expectedTimes, scheduledTimes);
//		
//	}
    

    @Ignore
    @Test
	public void testSimpleSchedule() {

    /*
    * simpleSchedulerTest tests the basic scheduling algorithm
    * with the simple example input from the project specification.
    *
    * Only one root task and one leaf task.
    *
    * */
        tasks = new ArrayList<>();
        scheduler = new Scheduler();
        schedulerParallel = new SchedulerParallel();

        Task task1 = new Task(1);
        Task task2 = new Task(2);
        Task task3 = new Task(3);
        Task task4 = new Task(4);
        Task task5 = new Task(5);

        task1.setWeight(2);
        task2.setWeight(2);
        task3.setWeight(2);
        task4.setWeight(5);
        task5.setWeight(1);

        task1.setSubTasks(task2, 2);
        task1.setSubTasks(task3, 2);
        
        task2.setParentTasks(task1);
        task3.setParentTasks(task1);
        
        task2.setSubTasks(task4, 2);
        
        task3.setSubTasks(task5, 2);
        
        task4.setParentTasks(task2);
        
        task4.setSubTasks(task5, 2);
        task5.setParentTasks(task4);
        task5.setParentTasks(task3);


        tasks.add(task2);
        tasks.add(task1);
        tasks.add(task3);
        tasks.add(task4);
        tasks.add(task5);

		//Schedule s = scheduler.createBasicSchedule(tasks, 1);
      //  Node s = scheduler.createOptimalSchedule(tasks, 2);
        Node sp = schedulerParallel.createOptimalSchedule(tasks, 4, 4);
        
        System.out.println(sp.getScheduledTasks().size());
        for (Task t : sp.getScheduledTasks()) {
        	
        	System.out.println("Node number: " + t.getNodeNumber() + "Comp time: " + t.getStatus()
        			+ " processor: " +  t.getProcessor());
        }
    }
    
   
    @Test
	public void testSevenSchedule() {

    /*
    * simpleSchedulerTest tests the basic scheduling algorithm
    * with the simple example input from the project specification.
    *
    * Only one root task and one leaf task.
    *
    * */
        tasks = new ArrayList<>();
        scheduler = new Scheduler();
        schedulerParallel = new SchedulerParallel();

        Task task1 = new Task(0);
        Task task2 = new Task(1);
        Task task3 = new Task(2);
        Task task4 = new Task(3);
        Task task5 = new Task(4);
        Task task6 = new Task(5);
        Task task7 = new Task(6);

        task1.setWeight(5);
        task2.setWeight(6);
        task3.setWeight(5);
        task4.setWeight(6);
        task5.setWeight(4);
        task6.setWeight(7);
        task7.setWeight(7);

        task1.setSubTasks(task2, 15);
        task1.setSubTasks(task3, 11);
        task1.setSubTasks(task4, 11);
        
        task2.setParentTasks(task1);
        task3.setParentTasks(task1);
        task4.setParentTasks(task1);
        
        task2.setSubTasks(task5, 19);
        task2.setSubTasks(task6, 4);
        task2.setSubTasks(task7, 21);
        
        task5.setParentTasks(task2);
        task6.setParentTasks(task2);
        task7.setParentTasks(task2);       


        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        tasks.add(task4);
        tasks.add(task5);
        tasks.add(task6);
        tasks.add(task7);

		//Schedule s = scheduler.createBasicSchedule(tasks, 1);
        Node s = schedulerParallel.createOptimalSchedule(tasks, 4, 4);
        
        System.out.println(s.getScheduledTasks().size());
        for (Task t : s.getScheduledTasks()) {
        	
        	System.out.println("Node number: " + t.getNodeNumber() + "Comp time: " + t.getStatus()
        			+ " processor: " +  t.getProcessor());
        }
    }
        
        
        
      
    //    assertEquals(s.getScheduledTasks().get(0).getStatus(), 3);
        



//   @Test
//   public void testMultiRootSchedule() {
//	    tasks = new ArrayList<>();
//        scheduler = new Scheduler();
//
//        Task task1 = new Task(1);
//        Task task2 = new Task(2);
//        Task task3 = new Task(3);
//        Task task4 = new Task(4);
//        Task task5 = new Task(5);
//        Task task6 = new Task(6);
//        Task task6 = new Task(6);
//        Task task6 = new Task(6);
//        Task task6 = new Task(6);
//        Task task6 = new Task(6);
//        Task task6 = new Task(6);
//
//        task1.setWeight(5);
//        task2.setWeight(6);
//        task3.setWeight(5);
//        task4.setWeight(6);
//        task5.setWeight(4);
//        task6.setWeight(7);
//        task7.setWeight(7);
//
//        task1.setSubTasks(task2, 15);
//        task1.setSubTasks(task3, 11);
//        task1.setSubTasks(task4, 11);
//        
//        task2.setParentTasks(task1);
//        task3.setParentTasks(task1);
//        task4.setParentTasks(task1);
//        
//        task2.setSubTasks(task5, 15);
//        task2.setSubTasks(task6, 11);
//        task2.setSubTasks(task7, 11);
//        
//        task5.setParentTasks(task2);
//        task6.setParentTasks(task2);
//        task7.setParentTasks(task2);       
//
//
//        tasks.add(task1);
//        tasks.add(task2);
//        tasks.add(task3);
//        tasks.add(task4);
//        tasks.add(task5);
//        tasks.add(task6);
//        tasks.add(task7);
//
//		//Schedule s = scheduler.createBasicSchedule(tasks, 1);
//        Node s = new Scheduler().createOptimalSchedule(tasks, 2);
//        
//        System.out.println(s.getScheduledTasks().size());
//        for (Task t : s.getScheduledTasks()) {
//        	
//        	System.out.println("Node number: " + t.getNodeNumber() + "Comp time: " + t.getStatus()
//        			+ " processor: " +  t.getProcessor());
//        }
//        
//    }
////
//    @Test
//    public void testMultiLeafSchedule() {
//        /*
//         * Tests the basic scheduling algorithm
//         *
//         * More than one root tasks and more than one leaf tasks.
//         *
//         * */
//        tasks = new ArrayList<>();
//        scheduler = new Scheduler();
//
//        Task task1 = new Task(1);
//        Task task2 = new Task(2);
//        Task task3 = new Task(3);
//        Task task4 = new Task(4);
//        Task task5 = new Task(5);
//
//
//        task1.setWeight(2);
//        task2.setWeight(3);
//        task3.setWeight(3);
//        task4.setWeight(2);
//        task5.setWeight(6);
//
//        task1.setSubTasks(task2, 10);
//        task1.setSubTasks(task3, 12);
//        task1.setSubTasks(task4, 31);
//        task1.setSubTasks(task5, 1);
//
//        tasks.add(task1);
//        tasks.add(task2);
//        tasks.add(task3);
//        tasks.add(task4);
//        tasks.add(task5);
//
//        Schedule s = scheduler.createBasicSchedule(tasks, 1);
//
//        List<Integer> scheduledTasks = new ArrayList<>();
//
//
//        for (Task t : s.getTasks().keySet()) {
//            scheduledTasks.add(t.getNodeNumber());
//            System.out.println(t.getNodeNumber());
//        }
//
//        List<Integer> scheduledTimes =  new ArrayList<>();
//
//        scheduledTimes.addAll(s.getTasks().values());
//
//        List<Integer> expectedTasks = new ArrayList<Integer>() {
//            {
//                add(1);
//                add(2);
//                add(3);
//                add(5);
//                add(4);
//            }
//        };
//
//        List<Integer> expectedTimes = new ArrayList<Integer>() {
//            {
//                add(0);
//                add(2);
//                add(5);
//                add(8);
//                add(14);
//
//            }
//        };
//
//        assertEquals(expectedTasks, scheduledTasks);
//        assertEquals(expectedTimes, scheduledTimes);
//
//    }
//
//    @Test
//    public void testIsolatedTaskSchedule() {
//        /*
//         * Tests the basic scheduling algorithm
//         *
//         * More than one root tasks and more than one leaf tasks.
//         *
//         * */
//        tasks = new ArrayList<>();
//        scheduler = new Scheduler();
//
//        Task task1 = new Task(1);
//        Task task2 = new Task(2);
//        Task task3 = new Task(3);
//        Task task4 = new Task(4);
//        Task task5 = new Task(5);
//
//
//        task1.setWeight(2);
//        task2.setWeight(3);
//        task3.setWeight(3);
//        task4.setWeight(2);
//        task5.setWeight(6);
//
//        task1.setSubTasks(task2, 10);
//        task1.setSubTasks(task3, 10);
//        task2.setSubTasks(task4, 12);
//
//
//
//        task2.setParentTasks(task1);
//        task3.setParentTasks(task1);
//        task4.setParentTasks(task2);
//
//        tasks.add(task3);
//        tasks.add(task2);
//        tasks.add(task1);
//        tasks.add(task4);
//        tasks.add(task5);
//
//        Schedule s = scheduler.createBasicSchedule(tasks, 1);
//
//        List<Integer> scheduledTasks = new ArrayList<>();
//
//        for (Task t : s.getTasks().keySet()) {
//            scheduledTasks.add(t.getNodeNumber());
//            System.out.println(t.getNodeNumber());
//        }
//
//        List<Integer> scheduledTimes =  new ArrayList<>();
//
//        scheduledTimes.addAll(s.getTasks().values());
//
//        List<Integer> expectedTasks = new ArrayList<Integer>() {
//            {
//                add(1);
//                add(5);
//                add(2);
//                add(3);
//                add(4);
//            }
//        };
//
//        List<Integer> expectedTimes = new ArrayList<Integer>() {
//            {
//                add(0);
//                add(2);
//                add(8);
//                add(11);
//                add(14);
//
//            }
//        };
//
//        assertEquals(expectedTasks, scheduledTasks);
//        assertEquals(expectedTimes, scheduledTimes);
//
//    }
//
//    @Test
//    public void testInternalMultiRootTaskSchedule() {
//        /*
//         * Tests the basic scheduling algorithm
//         *
//         * More than one root tasks and more than one leaf tasks.
//         *
//         * */
//        tasks = new ArrayList<>();
//        scheduler = new Scheduler();
//
//        Task task1 = new Task(1);
//        Task task2 = new Task(2);
//        Task task3 = new Task(3);
//        Task task4 = new Task(4);
//        Task task5 = new Task(5);
//        Task task6 = new Task(6);
//
//
//        task1.setWeight(2);
//        task2.setWeight(3);
//        task3.setWeight(3);
//        task4.setWeight(2);
//        task5.setWeight(6);
//        task6.setWeight(9);
//
//        task1.setSubTasks(task2, 4);
//        task1.setSubTasks(task3, 6);
//        task2.setSubTasks(task4, 156);
//        task3.setSubTasks(task5, 1);
//        task4.setSubTasks(task5, 2);
//        task4.setSubTasks(task6, 232);
//
//
//
//        task2.setParentTasks(task1);
//        task3.setParentTasks(task1);
//        task4.setParentTasks(task2);
//        task5.setParentTasks(task3);
//        task5.setParentTasks(task4);
//        task6.setParentTasks(task4);
//
//        tasks.add(task3);
//        tasks.add(task2);
//        tasks.add(task1);
//        tasks.add(task4);
//        tasks.add(task5);
//        tasks.add(task6);
//
//        Schedule s = scheduler.createBasicSchedule(tasks, 1);
//
//        List<Integer> scheduledTasks = new ArrayList<>();
//
//        for (Task t : s.getTasks().keySet()) {
//            scheduledTasks.add(t.getNodeNumber());
//            System.out.println(t.getNodeNumber());
//        }
//
//        List<Integer> scheduledTimes =  new ArrayList<>();
//
//        scheduledTimes.addAll(s.getTasks().values());
//
//        List<Integer> expectedTasks = new ArrayList<Integer>() {
//            {
//                add(1);
//                add(2);
//                add(3);
//                add(4);
//                add(5);
//                add(6);
//            }
//        };
//
//        List<Integer> expectedTimes = new ArrayList<Integer>() {
//            {
//                add(0);
//                add(2);
//                add(5);
//                add(8);
//                add(10);
//                add(16);
//
//            }
//        };
//
//        assertEquals(expectedTasks, scheduledTasks);
//        assertEquals(expectedTimes, scheduledTimes);
//
//    }
}


