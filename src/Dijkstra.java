import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;

public class Dijkstra {
  /**
   * Get a printed shortest path from source node to destination node in given graph.
   *
   * @param g    given graph.
   * @param src  source node.
   * @param dest destination node.
   */
  public static void dijkstra(Graph g, String src, String dest) {
    Map<String, Graph.Vertex> graph = g.getGraphMap();
    if (!graph.containsKey(src)) {
      System.err.printf("Graph doesn't contain start vertex \"%s\"\n", src);
      return;
    }
    final Graph.Vertex source = graph.get(src);
    NavigableSet<Graph.Vertex> q = new TreeSet<>();

    // set-up vertices
    for (Graph.Vertex v : graph.values()) {
      v.previous = v == source ? source : null;
      v.dist = v == source ? 0 : Integer.MAX_VALUE;
      q.add(v);
    }

    dijkstra(q);
    g.printPath(dest);
  }

  /**
   * Implementation of dijkstra's algorithm using a binary heap.
   */
  private static void dijkstra(final NavigableSet<Graph.Vertex> q) {
    Graph.Vertex u, v;
    while (!q.isEmpty()) {
      // vertex with shortest distance (first iteration will return source)
      u = q.pollFirst();
      if (u.dist == Integer.MAX_VALUE)
        break; // we can ignore u (and any other remaining vertices) since they are unreachable

      // look at distances to each neighbour
      for (Map.Entry<Graph.Vertex, Integer> a : u.neighbours.entrySet()) {
        v = a.getKey(); // the neighbour in this iteration

        final int alternateDist = u.dist + a.getValue();
        if (alternateDist < v.dist) { // shorter path to neighbour found
          q.remove(v);
          v.dist = alternateDist;
          v.previous = u;
          q.add(v);
        }
      }
    }
  }

  /**
   * Get a printed shortest path from source node to destination node in given graph, excluding
   * nodes in the input.
   *
   * @param g        given graph.
   * @param src      source node.
   * @param dest     destination node.
   * @param badNodes nodes that need to avoid in the shortest path.
   */
  public static void dijkstraMustNotPass(Graph g, String src, String dest, String[] badNodes) {
    Map<String, Graph.Vertex> graph = g.getGraphMap();
    // remove bad nodes from map, then perform normal Dijkstra
    for (String badNode : badNodes) {
      if (badNode.equals(dest) || badNode.equals(src)) {
        throw new IllegalArgumentException("Must Pass Source of Destination node.");
      }
      if (graph.containsKey(badNode)) {
        graph.remove(badNode);
      }

      for (String v : graph.keySet()) {
        graph.get(v).neighbours.remove(badNode);
      }
    }


    Graph newGraph = new Graph(graph);

    dijkstra(newGraph, src, dest);
  }

  /**
   * Get a printed shortest path from source node to destination node in given graph, including the
   * nodes in the input.
   *
   * @param g         given graph.
   * @param src       source node.
   * @param dest      destination node.
   * @param passNodes nodes that must need to pass in the shortest path.
   */
  public static void dijkstraMustPass(Graph g, String src, String dest, String[] passNodes) {
    //TODO
  }
}
