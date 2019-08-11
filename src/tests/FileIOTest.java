package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import MVC.Model.FileIO;
import MVC.Model.Task;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
public class FileIOTest {

    private FileIO fileIO;
    private File testFile;

    @Before
    public void initialise(){
        fileIO = new FileIO();
        testFile = new File("testFile.dot");
        try {
            testFile.createNewFile();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Test
    public void testFileRead(){
        try {

            FileWriter fw = new FileWriter(testFile, true);
            fw.write("\t0\t[Weight=4];\n");
            fw.write("\t1\t[Weight=2];\n");
            fw.write("\t0 -> 1\t[Weight=5];\n");
            fw.close();

            fileIO.readFile("testFile.dot");
            fileIO.processNodes();
            fileIO.processTransitions();
            List<Task> taskList = fileIO.getTaskList();

            Task parentTask = taskList.get(0);
            Task childTask = taskList.get(1);
            assertEquals(parentTask.getNodeNumber(), 0);
            assertEquals(parentTask.getWeight(), 4);
            assertEquals(parentTask.getSubTasks().get(childTask), Integer.valueOf(5));
            assertEquals(childTask.getNodeNumber(), 1);
            assertEquals(childTask.getWeight(), 2);
            assertTrue(childTask.getParentTasks().contains(parentTask));

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Test
    public void testFileWrite(){
        fileIO.readFile("testFile.dot");
        LinkedHashMap<Task, Integer> exampleHashmap = new LinkedHashMap<>();

        Task task1 = new Task(1);
        Task task2 = new Task(2);
        task1.setWeight(2);
        task2.setWeight(3);

        exampleHashmap.put(task1, 0);
        exampleHashmap.put(task2, 5);
        fileIO.writeFile(exampleHashmap);
        assertTrue(new File("testFile-output.dot").exists());
    }

    @After
    public void deleteFile(){
        if(testFile.exists()) {
            testFile.delete();
        }
    }
}
