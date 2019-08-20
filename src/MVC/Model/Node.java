package MVC.Model;

import java.util.ArrayList;
import java.util.List;

public class Node<Schedule> {
    private Node<Schedule> parentNode;
    private List<Node<Schedule>> childrenNodes = new ArrayList<>();
    private Schedule data;
    private double upperBound;
    private double lowerBound;
    
    
    public void setUpperBound(double upperBound) {
    	this.upperBound = upperBound;
    }
    
    public double getUpperBound() {
    	return this.upperBound;
    }
    
    public void setLowerBound(double lowerBound) {
    	this.lowerBound = lowerBound;
    }
    
    public double getLowerBound() {
    	return this.lowerBound;
    }
    
    
    public void setParentNode(Node<Schedule> schedule) {
 	   this.parentNode = schedule;
    }
    
    public Node<Schedule> getParentNode() {
 	   return this.parentNode;
    }
    
    public void addChildrenNodes(Node<Schedule> childNode) {
 	   this.childrenNodes.add(childNode);
    }
    
    public List<Node<Schedule>> getChildrenNodes() {
 	   return this.childrenNodes;
    }
    
   
    public void setData(Schedule data) {
 	   this.data = data;
    }
    
    public Schedule getData() {
 	   return this.data;
    }
    
 }
