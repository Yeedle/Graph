# Assignment 3: Graphs and Isomorphism

###### Abraham Neuwirth
###### MCO 364 - Data Structures II • Spring 2017 • Professor Y. Novick
---
The Graph's API is defined in the [JavaDocs](http://yeedle.github.io/graph-docs/).

## Graph

The graph consists of three classes: Graph, Vertex, and Pair.

`Pair<K>` is a generic class to store a set of two values where order doesn't matter. It defines a convenient factory method `of` which returns a new `Pair`, and its two main methods are `one` and `other` which each return one of the two elements in the pair.

The `Vertex` class represent a vertex in the graph. It doesn't have any public methods, only private or package-protected methods that serve the graph class. It consists of a key field, a data field, and a list of adjacent vertices.

The Graph class describes several methods for adding a vertex, adding multiple vertices, adding an edge, adding multiple edges, removing a vertex, and removing and edge. It also has a method that returns all the edges as a set of Pairs, and a method that checks if the graph is isomorphic using a user provided mapping.

## Isomrphism

The `isIsomorphic` method in the graph is simple. It uses the Java streams `Map` function to map the edge set into a set of edges with the key of the other graph using the user provided map, and then just checks if it is the same as the edge set of the other graph

```java
public <K2, V2> boolean isIsomorphic(Graph<K2, V2> otherGraph, Map<K, K2> map) {

    return this.edgesAsPairSet()
            .stream()
            .map(edge -> Pair.of(map.get(edge.one()), map.get(edge.other())))
            .collect(toSet())
            .equals(otherGraph.edgesAsPairSet());
}
```

In the main method, the Isomrphism functionality is tested:

```java
List<Person> people = Arrays.asList(
        new Person("abe", 70),
        new Person("bob", 45),
        new Person("con", 55),
        new Person("dan", 54),
        new Person("gil", 69),
        new Person("ida", 68),
        new Person("jon", 71),
        new Person("hil", 69)
);

List<Pair<String>> friends = Arrays.asList(
        new Pair<>("abe", "gil"),
        new Pair<>("abe", "hil"),
        new Pair<>("abe", "ida"),
        new Pair<>("bob", "gil"),
        new Pair<>("bob", "hil"),
        new Pair<>("bob", "jon"),
        new Pair<>("con", "gil"),
        new Pair<>("con", "ida"),
        new Pair<>("con", "jon"),
        new Pair<>("dan", "hil"),
        new Pair<>("dan", "ida"),
        new Pair<>("dan", "jon"),
        new Pair<>("dan", "gil")
);

List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);

List<Pair<Integer>> intEdges = Arrays.asList(
        new Pair<>(1, 2),
        new Pair<>(1, 5),
        new Pair<>(1, 4),
        new Pair<>(6, 2),
        new Pair<>(6, 5),
        new Pair<>(6, 7),
        new Pair<>(8, 5),
        new Pair<>(8, 7),
        new Pair<>(8, 4),
        new Pair<>(3, 7),
        new Pair<>(3, 4),
        new Pair<>(3, 2),
        new Pair<>(3, 5)
);


Graph<String, Person> friendsGraph = Graph.<String, Person>create()
        .addVertices(people, Person::getName)
        .addEdges(friends);

Graph<Integer, Integer> ints = Graph.<Integer, Integer>create()
        .addVertices(numbers, Integer::intValue)
        .addEdges(intEdges);


Map<String, Integer> map = new HashMap<>();
map.put("abe", 1);
map.put("bob", 6);
map.put("con", 8);
map.put("dan", 3);
map.put("gil", 5);
map.put("hil", 2);
map.put("ida", 4);
map.put("jon", 7);


System.out.println(friendsGraph.isIsomorphic(ints, map));
```

The output of the above is `true`. if the mapping is changed, for example, "Abe" is mapped to 2, and "hil" to 6, the result is "false"
