package coloring;

import graph.Vertex;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

/**
 * Created by yeedle on 3/14/17.
 */
public class TimeSlotsCollector implements Collector<Vertex<String, Course>, Map<Integer, List<Course>>, Map<Integer, List<Course>>> {

    public static TimeSlotsCollector toMapOfTimeSlotToCourseList(){
        return new TimeSlotsCollector();
    }


    @Override
    public Supplier<Map<Integer, List<Course>>> supplier() {
        return HashMap<Integer, List<Course>>::new;
    }

    @Override
    public BiConsumer<Map<Integer, List<Course>>, Vertex<String, Course>> accumulator() {
        return (accumulator, vertex) ->  {
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

            List<Course> courseList = accumulator.getOrDefault(timeSlot, new LinkedList<>());
            courseList.add(vertex.getData());
            accumulator.putIfAbsent(timeSlot, courseList);
        };
    }


    @Override
    public BinaryOperator combiner() {
        return null;
    }

    @Override
    public Function<Map<Integer, List<Course>>, Map<Integer, List<Course>>> finisher() {
        return Function.identity();
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Stream.of(Characteristics.IDENTITY_FINISH).collect(Collectors.toSet());
    }
}
