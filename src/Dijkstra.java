import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
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

  private static void dijkstraSetup(Map<String, Graph.Vertex> graph, String src) {
    final Graph.Vertex source = graph.get(src);
    NavigableSet<Graph.Vertex> q = new TreeSet<>();

    // set-up vertices
    for (Graph.Vertex v : graph.values()) {
      v.previous = v == source ? source : null;
      v.dist = v == source ? 0 : Integer.MAX_VALUE;
      q.add(v);
    }

    dijkstra(q);
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
    /*
      Find all permutations of the must-pass-node;
      One of the permutations is the shortest-path order;
      When we found such path, return it.
     */
    ArrayList<String> inputList = new ArrayList<>();
    for (String node : passNodes) {
      if (node.equals(src) || node.equals(dest)) {
        throw new IllegalArgumentException("The nodes to pass cannot contain source or destination node.");
      }
      inputList.add(node);
    }
    ArrayList<ArrayList<String>> nodesPermutation = getPermutationLists(inputList);

    int minCost = Integer.MAX_VALUE;
    int minCostIndex = -1;

    // compare the cost for each permutation
    for (int index = 0; index < nodesPermutation.size(); index++) {
      ArrayList<String> path = nodesPermutation.get(index);
      int cost = calculatePathCost(g, src, dest, path);

      if (cost < minCost) {
        minCost = cost;
        minCostIndex = index;
      }
    }

    // return the shortest one
    ArrayList<String> shortestPath = nodesPermutation.get(minCostIndex);
    System.out.println("\nMin cost path : " + shortestPath.toString());
    System.out.println("Min cost is : " + minCost);
    printMinCostPath(g, src, dest, shortestPath);
  }

  /**
   * Get a total cost by given source and destination node in the graph, and follow the nodes in the
   * path list between source and destination node.
   *
   * @param g    graph.
   * @param src  source node.
   * @param dest destination node.
   * @param path list of the nodes to pass.
   * @return the total cost of given path in this graph.
   */
  private static int calculatePathCost(Graph g, String src, String dest, ArrayList<String> path) {
    int cost = 0;
    path.add(dest);

    String pre = src;
    for (int i = 0; i < path.size(); i++) {
      String end = path.get(i);
      int currCost = pathCost(g, pre, end);
      if (currCost == Integer.MAX_VALUE) {
        return currCost;
      }
      cost += currCost;
      pre = end;
    }
    return cost;
  }

  private static void printMinCostPath(Graph g, String src, String dest, ArrayList<String> path) {
    path.add(dest);
    Map<String, Graph.Vertex> graph = g.getGraphMap();
    Set<String> fullPath = new HashSet<>();


    String pre = src;
    for (int i = 0; i < path.size(); i++) {
      String end = path.get(i);
      int currCost = pathCost(g, pre, end);

      if (currCost == Integer.MAX_VALUE) {
        return;
      }
      Graph.Vertex endNode = graph.get(end);

      while (endNode != null && !fullPath.contains(endNode.name)) {
        fullPath.add(endNode.name);
        endNode = endNode.previous;
      }
      pre = end;
    }

    List<String> vertices = new ArrayList<>();
    List<String> edges = new ArrayList<>();
    for (String node : fullPath) {
      vertices.add(node);
    }

    int cnt = 0;
    String preVertex = "";

    for (String node : fullPath) {
      if (cnt == 0) {
        preVertex = node;
      }

      if (cnt != 0) {
        edges.add(preVertex);
        edges.add(node);
        //add dist
        Graph.Vertex preNode = graph.get(preVertex);
        Graph.Vertex currNode = graph.get(node);
        int dist = preNode.neighbours.get(currNode);
        edges.add(Integer.toString(dist));
        preVertex = node;
      }
      cnt++;
    }
    System.out.println("full path : " + fullPath.toString());
    GraphViz.start(vertices, edges, "Must Pass");
  }

  /**
   * Get the path cost between given source and destination node.
   *
   * @param g    graph.
   * @param src  source node.
   * @param dest destination node.
   * @return the path cost between given source and destination node.
   */
  private static int pathCost(Graph g, String src, String dest) {
    Map<String, Graph.Vertex> graph = g.getGraphMap();
    if (!graph.containsKey(src)) {
      System.err.printf("Graph does not contain start vertex \"%s\"\n", src);
      return -1;
    }
    dijkstraSetup(graph, src);
    return g.getPathCost(dest);
  }


  /**
   * Generate a list of path lists, which are permutations of the given nodes.
   *
   * @param inputList list of must-pass nodes.
   * @return list of node lists.
   */
  private static ArrayList<ArrayList<String>> getPermutationLists(ArrayList<String> inputList) {
    if (inputList.isEmpty()) {
      ArrayList<ArrayList<String>> res = new ArrayList<>();
      res.add(new ArrayList<>());
      return res;
    }
    String firstNode = inputList.get(0);
    inputList.remove(0);

    ArrayList<ArrayList<String>> returnVal = new ArrayList<>();
    ArrayList<ArrayList<String>> permutations = getPermutationLists(inputList);

    for (ArrayList<String> subPerm : permutations) {
      for (int index = 0; index <= subPerm.size(); index++) {
        ArrayList<String> temp = new ArrayList<>(subPerm);
        temp.add(index, firstNode);
        returnVal.add(temp);
      }
    }

    return returnVal;
  }
}