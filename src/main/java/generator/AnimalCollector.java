package generator;

import model.Animal;
import model.Cage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class AnimalCollector implements Collector<Animal, Map<Cage, Long>, Map<Cage, Long>> {
    @Override
    public Supplier<Map<Cage, Long>> supplier() {
        return HashMap::new;
    }

    @Override
    public BiConsumer<Map<Cage, Long>, Animal> accumulator() {
        return (map, animal) -> map.merge(animal.getCage(), 1L, Long::sum);
    }

    @Override
    public BinaryOperator<Map<Cage, Long>> combiner() {
        return (map1, map2) -> {
            map1.putAll(map2);
            return map1;
        };
    }

    @Override
    public Function<Map<Cage, Long>, Map<Cage, Long>> finisher() {
        return Function.identity();
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.emptySet();
    }
}
