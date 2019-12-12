import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
  // mapping of vertex names to Vertex objects, built from a set of Edges
  private final Map<String, Vertex> graph;
  static int count = 0;


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
    public  static  List<String> vertices = new ArrayList<>();
    public  static  List<String> edges = new ArrayList<>();
    public final String name;
    public int dist = Integer.MAX_VALUE;
    public Vertex previous = null;
    public final Map<Vertex, Integer> neighbours = new HashMap<>();

    public Vertex(String name) {
      this.name = name;
    }

    public void printPath(int flag) {
      if(flag == 1 && vertices.size()!=0) {
        viz(vertices,edges,flag,"Simple Dijkstra");
        vertices.clear();
        edges.clear();
        flag = 0;
      }
      if(flag == 0 && vertices.size()!=0) {
        System.out.println();
        viz(vertices,edges,1,"Must not pass Dijkstra");
        vertices.clear();
        edges.clear();
        flag = 0;
      }
      if (this == this.previous) {
        if(count != 2) {
          System.out.printf("%s", this.name);
        }
        vertices.add(this.name);
      } else if (this.previous == null) {
        System.out.printf("%s(unreached)", this.name);
      } else {
        this.previous.printPath(flag);
        if(count != 2) {
          System.out.printf(" -> %s(%d)", this.name, this.dist);
        }
        vertices.add(this.name);
        edges.add(String.valueOf(this.dist));
      }
    }
    /*
    Creates a list of vertices and edges for visualizing the graph.
     */
    public void viz(List<String> vertices,List<String> edges,int flag,String Title) {
      if(flag == 1) {
        List<String> ver = new ArrayList<>();
        List<String> edges1 = new ArrayList<>();
        int j = 0;
        for(int i = 0 ; i < vertices.size()-1;i++) {
          edges1.add(vertices.get(i));
          edges1.add(vertices.get(i+1));
          edges1.add(edges.get(j));
          j++;
        }
        for(String a:vertices) {
          ver.add(a);
        }
        GraphViz.start(ver,edges1,Title);
      }
    }

    private int getPathCost() {
      int cost = 0;
      if (this == this.previous) {
      } else if (this.previous == null) {
        cost = Integer.MAX_VALUE;
        return cost;
      } else {
        cost += this.dist;
      }

      return cost;
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

  public int getPathCost(String endName) {
    if (!graph.containsKey(endName)) {
      System.err.printf("This graph doesn't contain end vertex \"%s\"\n", endName);
      return Integer.MAX_VALUE;
    }

    return graph.get(endName).getPathCost();
    //System.out.println();
  }

  /**
   * Prints a path from the source to the specified vertex
   */
  public void printPath(String endName) {
    if (!graph.containsKey(endName)) {
      System.err.printf("Graph doesn't contain end vertex \"%s\"\n", endName);
      return;
    }
    if(count == 0) {
      graph.get(endName).printPath(0);
      count++;
    }
    else {
      graph.get(endName).printPath(1);
      count++;
      graph.get(endName).printPath(0);
    }
    System.out.println();

  }

  /**
   * Prints the path from the source to every vertex (output order is not guaranteed)
   */
  public void printAllPaths() {
    int flag = 1;
    for (Vertex v : graph.values()) {
      v.printPath(flag);
      System.out.println();
    }
  }
}
