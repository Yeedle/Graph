package graph;

import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.toSet;

/**
 * An undirected, unweighted graph data structure, represented as a map of keys to vertices,
 * with each vertex containing a list of adjacent vertices.
 * @param <K> Type of the keys
 * @param <V> Type of the vertices
 */
public class Graph<K, V> {

    private Map<K, Vertex<K, V>> vertices = new HashMap<>();
    Set<Pair<Vertex<K, V>>> edges = new HashSet<>();

    private Graph() {}

    /**
     * Factory method to create graphs. Returns a new graph.
     * @param <K> The key type
     * @param <V> The value type
     * @return a new graph.Graph instance
     */
    public static <K, V> Graph<K, V> create(){
        Graph<K, V> graph = new Graph<>();
        return new Graph<>();
    }

    /**
     * returns a list of {@link Vertex} objects
     * @return Collection\<Vertex\<K, V>>
     */
    public Collection<Vertex<K, V>> getVertices() {
        return vertices.values();
    }

    /**
     * Adds a vertex to the graph.
     * @param key the key of the vertex
     * @param data the data
     * @return the graph
     */
    public Graph<K, V> addVertex(K key, V data) {
        Vertex<K, V> vertex = new Vertex<>(key, data);
        vertices.put(key, vertex);
        return this;
    }

    /**
     * Adds a vertex to the graph. Any edge in the added vertex's adjacency list will be added
     * to the graph as well.
     * @param vertex the new vertex to add to the graph
     * @return the graph
     */
    public Graph<K, V> addVertex(Vertex<K, V> vertex){
        vertices.put(vertex.getId(), vertex);

        for(Vertex<K, V> neighbor : vertex.getAdjacentVertices()) {
            if (!vertices.containsKey(neighbor.getId())) {
                this.addVertex(neighbor.getId(), neighbor.getData());
            }
        }
        return this;
    }


    /**
     * Add a list of objects as vertices to the graph
     * @param vertices list of objects of type V
     * @param function a function that maps the object of type V to a key of type K
     * @return the graph
     */
    public Graph<K, V> addVertices(List<V> vertices, Function<V, K> function) {

        for (V vertex : vertices) {
            this.vertices.put(function.apply(vertex), new Vertex<>(function.apply(vertex), vertex));
        }
        return this;
    }

    /**
     * Add a new edge to the graph. If one or both of the vertices are not in the graph
     * this will throw an exception
     * @param v1 vertex on one side of the edge
     * @param v2 vertex on other side of the edge
     * @return the graph
     */
    public Graph<K, V> addEdge(Vertex<K, V> v1, Vertex<K, V> v2) {
        checkIfKeysAreInGraph(v1.getId(), v2.getId());

        vertices.get(v2.getId()).addEdge(v1);
        vertices.get(v1.getId()).addEdge(v2);
        edges.add(Pair.of(v1, v2));
        return this;
    }

    /**
     * Adds a new edge to the graph using the keys of the vertices
     * @param key1 key for the vertex on one side of the edge
     * @param key2 key for the vertex on the other side of the edge
     * @return the graph
     */
    public Graph<K, V> addEdge(K key1, K key2) {
        checkIfKeysAreInGraph(key1, key2);
        Vertex<K, V> v1 = vertices.get(key1);
        Vertex<K, V> v2 = vertices.get(key2);
        addEdge(v1, v2);
        return this;
    }

    /**
     * Removes a node from the graph using the provided key.
     * @param key
     * @return the graph with the node removed
     */
    public Graph<K, V> removeVertex(K key) {
        Vertex<K, V> removed = vertices.remove(key);
        if (removed == null) throw new VertexNotInGraphException("Vertex{" + key + "} is not a member of the graph");

        for (Vertex<K, V> neighbor : removed.getAdjacentVertices()) {
            neighbor.removeEdge(removed);
        }
        edges.removeIf(edge -> edge.one().equals(removed) || edge.other().equals(removed));

        return this;
    }

    /**
     * Removes a node from the graph
     * @param vertex to remove
     * @return the graph
     */
    public Graph<K, V> removeVertex(Vertex<K, V> vertex) {
        removeVertex(vertex.getId());
        return this;
    }

    /**
     * Removes an edge between two vertices, if it exists. Otherwise does nothing.
     * @param v1 first vertex of the edge
     * @param v2 second vertex of the edge
     * @return the graph with the edge removed
     */
    public Graph<K, V> removeEdge(Vertex<K, V> v1, Vertex<K, V> v2) {
        vertices.computeIfPresent(v1.getId(), (id, vertex) -> vertex.removeEdge(v2));
        vertices.computeIfPresent(v2.getId(), (id, vertex) -> vertex.removeEdge(v1));
        edges.remove(Pair.of(v1, v2));
        return this;
    }

    /**
     * removes an edge -- if it exists -- between two vertices.
     * @param key1
     * @param key2
     * @return
     */
    public Graph<K, V> removeEdge(K key1, K key2) {

        vertices.computeIfPresent(key1, (id, vertex) -> vertex.removeEdge(vertices.get(key2)));
        vertices.computeIfPresent(key2, (id, vertex) -> vertex.removeEdge(vertices.get(key1)));
        edges.remove(Pair.of(vertices.get(key1), vertices.get(key2)));
        return this;
    }


    /**
     * searches for the given data in the graph
     * @param data the data to search for
     * @return an Optional\<graph.Vertex\>, empty if not found.
     */
    public Optional<Vertex> search(V data) {
        throw new VertexNotInGraphException("not yet implemented");
    }


    private void checkIfKeysAreInGraph(K k1, K k2) {
        Vertex<K, V> vertex1 = vertices.get(k1);
        Vertex<K, V> vertex2 = vertices.get(k2);
        if (vertex1 == null|| vertex2 == null) {
            StringBuilder str = new StringBuilder();
            if (vertex1 == null) str.append("Vertex{").append(k1).append("}");
            if (vertex1 == null && vertex2 == null) str.append(" and ");
            if (vertex2 == null) str.append("Vertex{").append(k2).append("}");
            if (vertex1 == null && vertex2 == null) str.append(" are not members");
            else str.append(" is not a member");
            str.append(" of the graph");

            throw new UnsupportedOperationException(str.toString());
        }
    }




    /**
     * Get the set of all edges in the graph
     * @return a Set containing all edges in the graph
     */
    public Set<Pair<Vertex<K, V>>> getEdgeSet() {

        return edges;
    }
    /**
     * Get the set of all edges in the graph
     * @return a Set containing all edges in the graph
     */
    public Set<Pair<K>> edgesAsPairSet() {

        Set<Pair<Vertex<K, V>>> pairSet = new HashSet<>();
        for (Vertex<K,V> vertex : vertices.values()) {
            for (Vertex<K, V> vertex1 :vertex.getAdjacentVertices()){
                pairSet.add(Pair.of(vertex, vertex1));
            }
        }

        return vertices
                .values()
                .stream()
                .flatMap(vertex -> vertex
                        .getAdjacentVertices()
                        .stream()
                        .map(vertex1 -> Pair.of(vertex1.getId(), vertex.getId())))
                .collect(toSet());

      /*   vertices
                .values()
                .stream()
                .flatMap(vertx -> vertx.getEdges().stream())
                .map(edge -> Pair.<K>of((K)edge.verticesAsArray()[0].getId(), (K)edge.verticesAsArray()[1].getId()))
                .collect(toSet());*/
    }


    /**
     * Add a list of {@link Pair}s as edges to the graph
     * @param pairs of vertex keys of type K
     * @return the graph
     */
    public Graph<K,V> addEdges(List<Pair<K>> pairs) {
        for (Pair<K> pair : pairs) {
            //todo add existence check, throw vertex not in edge exception
            addEdge(vertices.get(pair.one()), vertices.get(pair.other()));
        }

        return this;
    }


    /**
     * Add a set of {@link Pair}s as edges to the graph
     * @param pairs of vertex keys of type K
     * @return the graph
     */
    public Graph<K,V> addEdges(Set<Pair<K>> pairs) {
        for (Pair<K> pair : pairs) {
            //todo add existence check, throw vertex not in edge exception
            addEdge(vertices.get(pair.one()), vertices.get(pair.other()));
        }

        return this;
    }


    /**
     * Compares this graph to another graph to check if they're isomorphic. Mapping
     * for the keys in this graph to the keys in the other graph has to be provided.
     * @param otherGraph to compare to
     * @param map a mapping of keys in this graph to keys in the other graphe
     * @param <K2> the type of keys for the other graph
     * @param <V2> the type of values in the other graph
     * @return true if the graphs are isomorphic, false otherwise
     */
    public <K2, V2> boolean isIsomorphic(Graph<K2, V2> otherGraph, Map<K, K2> map) {

        return this.edgesAsPairSet()
                .stream()
             //   .peek(pair -> System.out.println(pair.one() + ", " +pair.other()))
                .map(edge -> Pair.of(map.get(edge.one()), map.get(edge.other())))
             //   .peek(pair -> System.out.println(pair.one() + ", " + pair.other()))
                .collect(toSet())
                .equals(otherGraph.edgesAsPairSet());
    }

    /**
     * Prints graph so that it can easily be visualized using <a href="https://knsv.github.io/mermaid/">Mermand</a>
     * @return the graph
     */
    public Graph<K, V> printGraph(){
        System.out.println("```@mermaid");
        System.out.println("graph TD");
        getEdgeSet().forEach(
                edge -> System.out.println(edge.one().getId() + " --> " + edge.other().getId())
        );
        System.out.println("```");
        return this;
    }
}
