package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import MVC.Model.Schedule;
import MVC.Model.Scheduler;
import MVC.Model.Task;

public class SchedulerTest {

    private List<Task> tasks;
    private Scheduler scheduler;


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

        Task task1 = new Task(1);
        Task task2 = new Task(2);

        task1.setWeight(2);
        task2.setWeight(3);

        task1.setSubTasks(task2, 10);



        task2.setParentTasks(task1);


        tasks.add(task2);
        tasks.add(task1);

		//Schedule s = scheduler.createBasicSchedule(tasks, 1);
        Schedule s = scheduler.createOptimalSchedule(tasks, 1);
		
		List<Integer> scheduledTasks = new ArrayList<>();

		
		for (Task t : s.getTasks().keySet()) {
			scheduledTasks.add(t.getNodeNumber());
			System.out.println(t.getNodeNumber());
		}
		
		List<Integer> scheduledTimes =  new ArrayList<>();

		scheduledTimes.addAll(s.getTasks().values());

        List<Integer> expectedTasks = new ArrayList<Integer>() {
            {
                add(1);
                add(3);
                add(2);
                add(4);
            }
        };

        List<Integer> expectedTimes = new ArrayList<Integer>() {
            {
                add(0);
                add(2);
                add(5);
                add(8);

            }
        };

		assertEquals(expectedTasks, scheduledTasks);
		assertEquals(expectedTimes, scheduledTimes);
		
	}

//    @Test
//    public void testMultiRootSchedule() {
//        /*
//         * Tests the basic scheduling algorithm
//         *
//         * More than one root tasks and one leaf task.
//         *
//         * */
//        tasks = new ArrayList<>();
//        scheduler = new Scheduler();
//
//        Task task1 = new Task(1);
//        Task task2 = new Task(2);
//        Task task3 = new Task(3);
//        Task task4 = new Task(4);
//
//
//        task1.setWeight(2);
//        task2.setWeight(3);
//        task3.setWeight(7);
//        task4.setWeight(2);
//
//        task1.setSubTasks(task4, 10);
//        task2.setSubTasks(task4, 12);
//        task3.setSubTasks(task4, 13);
//
//        task4.setParentTasks(task2);
//        task4.setParentTasks(task3);
//        task4.setParentTasks(task1);
//
//        tasks.add(task3);
//        tasks.add(task2);
//        tasks.add(task1);
//        tasks.add(task4);
//
//        Schedule s = scheduler.createBasicSchedule(tasks, 1);
//
//        List<Integer> scheduledTasks = new ArrayList<>();
//
////		s.getTasks().keySet().forEach(task -> {
////		    scheduledTasks.add(task.getNodeNumber());
////		});
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
//                add(3);
//                add(2);
//                add(1);
//                add(4);
//            }
//        };
//
//        List<Integer> expectedTimes = new ArrayList<Integer>() {
//            {
//                add(0);
//                add(7);
//                add(10);
//                add(12);
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


