# Assignment 4: Graph Coloring

###### Abraham Neuwirth
###### MCO 364 - Data Structures II • Spring 2017 • Professor Y. Novick
---

The solution uses one auxiliary class `Course` for the solution. It consists of two fields with getters and setters:

```java
private String title;
private int TimeSlot;
```

All the action happens in `Main`. First, we get the input from the user. The input is collected into two data structures: A `List` of `Course`s which will be used to construct the graph, and a `Map` from student to their set of course. The map is later used to find conflicts between courses.


```java
Scanner reader = new Scanner(System.in);
System.out.println("Enter each course line by line, then enter \"done\" when finished:" +
        "\nThe format should be: COURSE TITLE, STUDENT 1, STUDENT 2, ... , STUDENT N");
List<Course> courses = new LinkedList<>();
HashMap<String, Set<Course>> students = new HashMap<>();

do {
    String str = reader.nextLine();
    if (str.toLowerCase().equals("done")) break;
    String[] split = str.split(",\\s*");
    Course course = new Course(split[0]);
    courses.add(course);
    for (int i = 1; i < split.length; i++) {
        Set<Course> courseSet = students.getOrDefault(split[i], new HashSet<>());
              courseSet.add(course);
              students.putIfAbsent(split[i], courseSet);
    }

} while (true);
```

With the data read in, the conflicts between classes are found. The maps of students is streamed, each student's course set is passed to `getConflicts` which returns a `Set` of `Pairs` of class titles, which is then flattened to a set of the titles of conflicting classes.

```java
Set<Pair<String>> conflicts = students
        .values()
        .stream()
        .flatMap((courseSet) -> getConflicts(courseSet).stream())
        .map(pair -> Pair.of(pair.one().getTitle(), pair.other().getTitle()))
        .collect(toSet());
```

The `getConflicts` method takes a course set and returns the conflicts for that course set , using a polynomial approach: for each class, the rest of the classes are iterated over and are added to the conflicts set.

```java
private static Set<Pair<Course>> getConflicts(Set<Course> courseSet) {
   Set<Pair<Course>> conflictingCourses = new HashSet<>();
   Iterator<Course> iterator = courseSet.iterator();
   while (iterator.hasNext()) {
       Course course = iterator.next();
       iterator.remove();
       iterator.forEachRemaining(course1 -> conflictingCourses.add(new Pair<>(course, course1)));
       iterator = courseSet.iterator();
   }
   return conflictingCourses;
}
```

Finally, we have a set of conflicts, and a list of courses. We now create our graphs, add the conflicts as edges to the graph, sort the nodes by degree, and then "color" the nodes so that there's no conflicts:

```java
Map<Integer, List<Course>> timeSlots = Graph.<String, Course>create()
        .addVertices(courses, Course::getTitle)
        .addEdges(conflicts)
        .getVertices()
        .stream()
        .sorted(comparing(Vertex::getDegree, reverseOrder()))
        .collect(HashMap::new, putVertexInTimeSlotMap(), HashMap::putAll);
```

The imprtant part here is the `collect` at the end: It iterates over the list of vertices returned by `getVertices()` and collects it to a `HashMap<Integer, List<Course>`, the key being the "color" or time slot, and the value being the list of courses in that slot.

The `putVertexInTimeSlotMap` method returns a `BiConsumer` that acts as an accumulator. Basically, it returns a function that takes the hashmap and a vertex, and adds the vertex to its proper place in the HashMap


```java
private static BiConsumer<HashMap<Integer, List<Course>>, Vertex<String, Course>> putVertexInTimeSlotMap() {
  return (accumulator, vertex) -> {
      // first get all the adjacent time slots
      Set<Integer> conflictingTimeSlots =
              vertex.getAdjacentVertices()
                      .stream()
                      .map(adajcent -> adajcent.getData().getTimeSlot())
                      .collect(toSet());

      // find the first time slot not yet taken
      int timeSlot = IntStream.iterate(1, i -> i + 1)
              .filter(i -> !conflictingTimeSlots.contains(i))
              .findFirst()
              .getAsInt();
      // set the course's time slot
      vertex.getData().setTimeSlot(timeSlot);

      // add the course to the hashmap
      accumulator.merge(timeSlot,
              asList(vertex.getData()),
              (currList, newList) -> Stream.of(currList, newList).flatMap(List::stream).collect(toList()));
  };
}
```

After it's collected, the results are printed to the screen:

```java
System.out.println(timeSlots.size() + " time slots" + (timeSlots.size() < 2? " is": " are") + " required");
timeSlots.forEach((i, ls) -> System.out.println("slot " + i + ") " + ls.stream().map(Object::toString).collect(Collectors.joining(", "))));
```

Here's a run, with the example given in the assignment:

```
> Enter each course line by line, then enter "done" when finished:
> The format should be: COURSE TITLE, STUDENT 1, STUDENT 2, ... , STUDENT N
MCO364, Sam, Mike, Henry, Fred, Peter, George
MCO243, Sam, Henry, Nancy, Tiller
MCO368, Wanda, Baker, Cheryl, Stella, William
MCO152, Stella, Tom, Luke
MCO104, William, Wanda, Clarence, Steve
done
> 2 time slots are required
> slot 1) MCO368, MCO364
> slot 2) MCO243, MCO104, MCO152
```
