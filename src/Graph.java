import java.util.HashMap;
import java.util.Map;

public class Graph {
  // mapping of vertex names to Vertex objects, built from a set of Edges
  private final Map<String, Vertex> graph;

  /**
   * One edge of the graph (only used by Graph constructor)
   */
  public static class Edge {
    public final String v1, v2;
    public final int dist;

    public Edge(String v1, String v2, int dist) {
      this.v1 = v1;
      this.v2 = v2;
      this.dist = dist;
    }
  }

  /**
   * One vertex of the graph, complete with mappings to neighbouring vertices
   */
  public static class Vertex implements Comparable<Vertex> {
    public final String name;
    public int dist = Integer.MAX_VALUE;
    public Vertex previous = null;
    public final Map<Vertex, Integer> neighbours = new HashMap<>();

    public Vertex(String name) {
      this.name = name;
    }

    private void printPath() {
      if (this == this.previous) {
        System.out.printf("%s", this.name);
      } else if (this.previous == null) {
        System.out.printf("%s(unreached)", this.name);
      } else {
        this.previous.printPath();
        System.out.printf(" -> %s(%d)", this.name, this.dist);
      }
    }

    public int compareTo(Vertex other) {
      if (dist == other.dist)
        return name.compareTo(other.name);

      return Integer.compare(dist, other.dist);
    }

    @Override
    public String toString() {
      return "(" + name + ", " + dist + ")";
    }
  }

  /**
   * Builds a graph from a set of edges
   */
  public Graph(Edge[] edges) {
    graph = new HashMap<>(edges.length);

    // one pass to find all vertices
    for (Edge e : edges) {
      if (!graph.containsKey(e.v1)) graph.put(e.v1, new Vertex(e.v1));
      if (!graph.containsKey(e.v2)) graph.put(e.v2, new Vertex(e.v2));
    }

    // another pass to set neighbouring vertices
    for (Edge e : edges) {
      graph.get(e.v1).neighbours.put(graph.get(e.v2), e.dist);
      // graph.get(e.v2).neighbours.put(graph.get(e.v1), e.dist); // also do this for an undirected graph
    }
  }

  /**
   * Constructs a graph from a vertex mapping map.
   */
  public Graph(Map<String, Graph.Vertex> graph) {
    this.graph = graph;
  }

  /**
   * Get the vertex mapping map.
   *
   * @return the vertex mapping of current graph.
   */
  public Map<String, Graph.Vertex> getGraphMap() {
    return graph;
  }


  /**
   * Prints a path from the source to the specified vertex
   */
  public void printPath(String endName) {
    if (!graph.containsKey(endName)) {
      System.err.printf("Graph doesn't contain end vertex \"%s\"\n", endName);
      return;
    }

    graph.get(endName).printPath();
    System.out.println();
  }

  /**
   * Prints the path from the source to every vertex (output order is not guaranteed)
   */
  public void printAllPaths() {
    for (Vertex v : graph.values()) {
      v.printPath();
      System.out.println();
    }
  }
}
