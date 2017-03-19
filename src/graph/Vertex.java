package graph;

import java.util.ArrayList;
import java.util.List;

/**
 * A vertex (node) in a graph.
 * @param <V> Type of data contained in the vertex
 * @param <K> Type of the vertex id
 */
public class Vertex<K, V> {

    private K id;

    private V data;

    private List<Vertex<K, V>> adjacentVertices = new ArrayList<>();

    /**
     * Constructor
     * @param key the vertex key
     * @param data the vertex data
     */
    Vertex(K key, V data) {
        this.id = key;
        this.data = data;

    }

    /**
     * Get the vertex key
     * @return the vertex key
     */
    public K getId() {
        return id;
    }

    /**
     * Get the object stored in the vertex
     * @return the object stored in the vertex
     */
    public V getData() {
        return data;
    }

    /**
     * Get the degree (number of connected edges) of the vertex
     * @return an integer, degree of the vertex
     */
    public int getDegree(){
        return adjacentVertices.size();
    }

    public void setData(V data) {
        this.data = data;
    }

    /**
     * Adds an edge between this vertex and the passed vertex
     * @param vertex
     * @return this vertex with the edge removed
     */
    Vertex<K,V> addEdge(Vertex<K, V> vertex) {

        adjacentVertices.add(vertex);

        return this;
    }

    Vertex<K, V> addEdges(List<Vertex<K, V>> vertices) {
        this.adjacentVertices.addAll(vertices);
        return this;
    }




    public List<Vertex<K, V>> getAdjacentVertices() {
        return adjacentVertices;
    }


    Vertex<K, V> removeEdge(Vertex<K, V> vertex) {
        adjacentVertices.remove(vertex);
        return this;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vertex<?, ?> vertex = (Vertex<?, ?>) o;

        return getId() != null ? getId().equals(vertex.getId()) : vertex.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
