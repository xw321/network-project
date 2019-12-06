import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.ListenableDirectedWeightedGraph;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.swing.mxGraphComponent;

import java.util.ArrayList;
import java.util.List;


public class GraphViz {

  /*
  Displays the GUI.
  The GUI is embedded in a Java Frame.
   */
  private static void createAndShowGui(List<String> vertices,List<String> edges,String Title) {
    JFrame frame = new JFrame(Title);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    ListenableGraph<String, MyEdge> g = buildGraph(vertices,edges);
    JGraphXAdapter<String, MyEdge> graphAdapter =
      new JGraphXAdapter<String, MyEdge>(g);

    mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
    layout.execute(graphAdapter.getDefaultParent());

    frame.add(new mxGraphComponent(graphAdapter));

    frame.pack();
    frame.setLocationByPlatform(true);
    frame.setVisible(true);
  }

  /*
  Returns the edge weight of the graph.
   */
  public static class MyEdge extends DefaultWeightedEdge {
    @Override
    public String toString() {
      return String.valueOf(getWeight());
    }
  }
/*
Creates a ListenableGraph with the preferred set of vertices and edges.
Returns the Listenable Graph.
 */
  public static ListenableGraph<String, MyEdge> buildGraph(List<String> vertices, List<String> edges) {
    ListenableDirectedWeightedGraph<String, MyEdge> g =
      new ListenableDirectedWeightedGraph<String, MyEdge>(GraphViz.MyEdge.class);
    for(String a:vertices) {
      g.addVertex(new String(a));
    }
    MyEdge e1;
    for(int i = 0 ; i < edges.size() ; i = i + 3) {
      e1 = g.addEdge(new String(edges.get(i)),new String(edges.get(i+1)));
      g.setEdgeWeight(e1,Integer.parseInt(edges.get(i+2)));
    }
    return g;
  }

  /*
  Starts a single thread for graph visualization.
   */
  public static void start(List<String> vertices,List<String> edges,String Title) {

    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGui(vertices,edges,Title);
      }
    });
  }
}
