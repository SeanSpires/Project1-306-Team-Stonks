package MVC.Model;

import java.util.Comparator;

public class NodeLowerBoundComparator implements Comparator<Node<Schedule>> {

    @Override
    public int compare(Node<Schedule> n1, Node<Schedule> n2) {
        if (n1.getLowerBound() < n2.getLowerBound()) {
            return -1;
        }

        if (n2.getLowerBound() < n1.getLowerBound()) {
            return 1;
        }
        return 0;
    }
}
