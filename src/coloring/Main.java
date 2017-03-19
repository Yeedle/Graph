package coloring;

import graph.Graph;
import graph.Pair;
import graph.Vertex;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Arrays.*;
import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * Created by yeedle on 3/13/17.
 */
public class Main {
    public static void main(String[] args) {

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
            IntStream.range(1, split.length).forEach(i -> {
                Set<Course> courseSet = students.getOrDefault(split[i], new HashSet<>());
                courseSet.add(course);
                students.putIfAbsent(split[i], courseSet);
            });


       } while (true);

        Set<Pair<String>> conflicts = students
                .values()
                .stream()
                .flatMap((courseSet) -> getConflicts(courseSet).stream())
                .map(pair -> Pair.of(pair.one().getTitle(), pair.other().getTitle()))
                .collect(toSet());


        Map<Integer, List<Course>> timeSlots = Graph.<String, Course>create()
                .addVertices(courses, Course::getTitle)
                .addEdges(conflicts)
            //    .printGraph()
                .getVertices()
                .stream()
                .sorted(comparing(Vertex::getDegree, reverseOrder()))
              //  .peek(System.out::println)
         //       .collect(toMapOfTimeSlotToCourseList());
                .collect(HashMap::new, putVertexInTimeSlotMap(), HashMap::putAll);
      //   .forEachOrdered(vertex -> setTimeSlot(timeSlots, vertex));

        System.out.println(timeSlots.size() + " time slots" + (timeSlots.size() < 2? " is": " are") + " required");
        timeSlots.forEach((i, ls) -> System.out.println("slot " + i + ") " + ls.stream().map(Object::toString).collect(Collectors.joining(", "))));

    }

    private static BiConsumer<HashMap<Integer, List<Course>>, Vertex<String, Course>> putVertexInTimeSlotMap() {
        return (accumulator, vertex) -> {
            Set<Integer> conflictingTimeSlots =
                    vertex.getAdjacentVertices()
                            .stream()
                            .map(adajcent -> adajcent.getData().getTimeSlot())
                            .collect(toSet());

            int timeSlot = IntStream.iterate(1, i -> i + 1)
                    .filter(i -> !conflictingTimeSlots.contains(i))
                    .findFirst()
                    .getAsInt();

            vertex.getData().setTimeSlot(timeSlot);

            accumulator.merge(timeSlot,
                    asList(vertex.getData()),
                    (currList, newList) -> Stream.of(currList, newList).flatMap(List::stream).collect(toList()));
        };
    }

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

    private static void setTimeSlot(Map<Integer, List<Course>> timeSlots, Vertex<String, Course> vertex) {

        List<Vertex<String, Course>> adjacentVertices = vertex.getAdjacentVertices();
        Set<Integer> conflictingTimeSlots = new HashSet<>();
        for (Vertex<String, Course> neighbor : adjacentVertices) {
            conflictingTimeSlots.add(neighbor.getData().getTimeSlot());
        }

        int first = IntStream.iterate(1, i -> i + 1).filter(i -> !conflictingTimeSlots.contains(i)).findFirst().getAsInt();

        vertex.getData().setTimeSlot(first);

        timeSlots.merge(first,
                Collections.singletonList(vertex.getData()),
                (currList, newList) -> Stream.of(currList, newList).flatMap(Collection::stream).collect(toList()));

    }
}
