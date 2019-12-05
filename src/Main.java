public class Main {
  // A sample directed graph.
  private static final Graph.Edge[] GRAPH = {
          // Distance from node "a" to node "b" is 7.
          // In the current Graph there is no way to move the other way (e,g, from "b" to "a"),
          // a new edge would be needed for that
          new Graph.Edge("a", "b", 7),
          new Graph.Edge("a", "c", 9),
          new Graph.Edge("a", "f", 14),
          new Graph.Edge("b", "c", 10),
          new Graph.Edge("b", "d", 15),
          new Graph.Edge("c", "d", 11),
          new Graph.Edge("c", "f", 2),
          new Graph.Edge("d", "e", 6),
          new Graph.Edge("e", "f", 9),
  };
  private static final String START = "a";
  private static final String END = "e";

  /**
   * Entry point of the program. First define a Graph. Then run one of the dijkstra algorithms
   * defined in Dijkstra class. The possible shortest path will be printed on the console.
   *
   * @param args none.
   */
  public static void main(String[] args) {
    Graph g = new Graph(GRAPH);
    Dijkstra.dijkstra(g, START, END);
    Dijkstra.dijkstraMustNotPass(g, START, END, new String[]{"c"});
  }
}
