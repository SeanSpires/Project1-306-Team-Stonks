package MVC.Controller;

import javax.swing.*;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class HelloWorld extends JPanel
{

    /**
     *
     */
    private static final long serialVersionUID = -2707712944901661771L;
    private mxGraphComponent graphComponent;

    public HelloWorld()
    {
//        super("Hello World");

        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();

        graph.getModel().beginUpdate();
        try
        {
            Object v1 = graph.insertVertex(parent, null, "Hello", 20, 20, 80,
                    30);
            Object v2 = graph.insertVertex(parent, null, "World!", 240, 150,
                    80, 30);
            graph.insertEdge(parent, null, "Edge", v1, v2);
        }
        finally
        {
            graph.getModel().endUpdate();
        }

       graphComponent = new mxGraphComponent(graph);
        graphComponent.setSize(1024,430);
//        getContentPane().add(graphComponent);
    }
//
//    public static void main(String[] args)
//    {
//        HelloWorld frame = new HelloWorld();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(400, 320);
//        frame.setVisible(true);
//    }

    public mxGraphComponent getGraphComponent(){
        return graphComponent;
    }

}
