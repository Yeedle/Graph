package graph;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * Created by yeedle on 3/14/17.
 */
class PairTest {

    @Test
    @DisplayName("Pairs of two objects are equal, order doesn't matter")
    void testForEquality(){

        // primitives
        Assertions.assertEquals(Pair.of(5, 10), Pair.of(5, 10));
        Assertions.assertEquals(Pair.of(5, 10), Pair.of(10, 5));

        // strings
        Assertions.assertEquals(Pair.of("this", "is"), Pair.of("is", "this"));
        Assertions.assertEquals(Pair.of("is", "this"), Pair.of("is", "this"));

        // objects
        Assertions.assertEquals(
                Pair.of(Arrays.asList("a", "list"), Arrays.asList("and", "another")),
                Pair.of(Arrays.asList("a", "list"), Arrays.asList("and", "another")));
        Assertions.assertEquals(
                Pair.of(Arrays.asList("a", "list"), Arrays.asList("and", "another")),
                Pair.of(Arrays.asList("and", "another"), Arrays.asList("a", "list")));

    }


    @Test
    @DisplayName("Hash codes of pairs of two identical objects are equal, order doesn't matter")
    void testForHashCodeEquality(){

        // primitives
        Assertions.assertEquals(Pair.of(5, 10).hashCode(), Pair.of(5, 10).hashCode());
        Assertions.assertEquals(Pair.of(5, 10).hashCode(), Pair.of(10, 5).hashCode());

        // strings
        Assertions.assertEquals(Pair.of("this", "is").hashCode(), Pair.of("is", "this").hashCode());
        Assertions.assertEquals(Pair.of("is", "this").hashCode(), Pair.of("is", "this").hashCode());

        // objects
        Assertions.assertEquals(
                Pair.of(Arrays.asList("a", "list").hashCode(), Arrays.asList("and", "another").hashCode()),
                Pair.of(Arrays.asList("a", "list").hashCode(), Arrays.asList("and", "another").hashCode()));
        Assertions.assertEquals(
                Pair.of(Arrays.asList("a", "list").hashCode(), Arrays.asList("and", "another").hashCode()),
                Pair.of(Arrays.asList("and", "another").hashCode(), Arrays.asList("a", "list").hashCode()));

    }

}