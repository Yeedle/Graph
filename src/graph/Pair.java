package graph;

import java.util.HashSet;
import java.util.Set;

/**
 * A Pair is a set of two items. Two Pairs are equivalent if they have the same
 * two items regardless of order. This is useful for representing an undirected edge
 * between two {@link Vertex} instances.
 * @param <K> The type of the items
 */
public class Pair<K> {
    Set<K> pairs = new HashSet<K>();

    /**
     * Constructor for the Pair class
     * @param k1 One item
     * @param k2 The other item
     */
    public Pair(K k1, K k2) {
        pairs.add(k1);
        pairs.add(k2);
    }

    /**
     * Convenience factory method to create pairs. Returns the new pair to allow
     * method chaining.
     * @param k1 One item
     * @param k2 The other item
     * @param <K> the type of items in the pair
     * @return the pair
     */
    public static <K> Pair<K> of(K k1, K k2) {
        return new Pair<>(k1, k2);
    }

    /**
     * Gets one of the two items in the pair
     * @return one of the two items in the pair
     */
    public K one() {
        return (K)pairs.toArray()[0];
    }

    /**
     * Gets the counterpart for the item returned by the {@link #one()} method
     * @return one of the two items in the pair
     */
    public K other() {
        return (K)pairs.toArray()[1];
    }

    /**
     * Gets the underlying set of two items in the pair
     * @return the Set used by the Pair instance
     */
    public Set<K> getPairs() {
        return pairs;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null ||!(o instanceof Pair)) return false;

        return this.pairs.equals(((Pair) o).getPairs());
    }

    @Override
    public int hashCode() {
        return this.pairs.hashCode();
    }
}
