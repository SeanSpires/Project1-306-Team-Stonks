package mvc.model;


import java.util.Comparator;

public class MinTaskComparator implements Comparator<Task> {
    @Override
    public int compare(Task t1,Task t2) {

        // If Task one costs less than task two then return -1
        if (t1.getWeight() < t2.getWeight()) {
            return -1;
        }
        // If Task two costs less than task one then return 1
        if (t2.getWeight() < t1.getWeight()) {
            return 1;
        }
        // Else they have similar costs
        return 0;
    }
}
