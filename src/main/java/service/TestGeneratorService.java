package service;

import generator.AnimalCollector;
import generator.AnimalGenerator;
import model.Animal;
import model.Cage;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
@Fork(1)
public class TestGeneratorService {
    private List<Animal> animals = new ArrayList<>();
    private DoGenerateStream doGenerateStream = new DoGenerateStream();
    @Param({"10000000"})
//    @Param({"1000", "10000", "100000"})
    public static int COUNT_ANIMAL;
    public static Map<Cage, Long> doGenerate(Long countOfCollections) {
        AnimalGenerator generator = new AnimalGenerator();
        List<Animal> animals = Stream.generate(generator::generateAnimal).limit(countOfCollections).toList();

        // -------------------------- Базовое итерирование --------------------------------
        long start = System.currentTimeMillis();
        Map<Cage, Long> animalCountByCageBasicIterable = new HashMap<>();
        for (Animal animal : animals) {
            animalCountByCageBasicIterable.merge(animal.getCage(), 1L, Long::sum);
        }
        long end = System.currentTimeMillis();
        System.out.println("Размер массива: " + countOfCollections + " итерационный цикл: " + (end - start) + " ms");

        // -------------------------- StreamApi --------------------------------
        start = System.currentTimeMillis();
        Map<Cage, Long> animalCountByCageStreamApi = animals.stream()
                .collect(Collectors.groupingBy(Animal::getCage, Collectors.counting()));
        end = System.currentTimeMillis();
        System.out.println("Размер массива: " + countOfCollections +
                " при помощи stream API: " + (end - start) + " ms");

        // -------------------------- Свой коллектор --------------------------------
        start = System.currentTimeMillis();
        Map<Cage, Long> animalCountByCageCustomCollector = animals.stream().collect(new AnimalCollector());
        end = System.currentTimeMillis();
        System.out.println("Размер массива: " + countOfCollections +
                " при помощи самописного коллектора: " + (end - start) + " ms");
        return animalCountByCageBasicIterable;
    }

    @Setup(Level.Trial)
    public void setup() {
        AnimalGenerator generator = new AnimalGenerator();
        animals = Stream.generate(generator::generateAnimal).limit(COUNT_ANIMAL).toList();
    }

    @Benchmark
    public void init() {
        // Do nothing
    }

    @Benchmark
    public void testGenerateSingleStreamWithDelay(Blackhole bh) {
        animals = doGenerateStream.doGenerateSingleStreamWithDelay(animals);
        bh.consume(animals);
    }

    @Benchmark
    public void testGenerateSingleStreamWithoutDelay(Blackhole bh) {
        animals = doGenerateStream.doGenerateSingleStreamWithoutDelay(animals);
        bh.consume(animals);
    }

    @Benchmark
    public void testGenerateParallelStreamWithDelay(Blackhole bh) {
        animals = doGenerateStream.doGenerateParallelStreamWithDelay(animals);
        bh.consume(animals);
    }

    @Benchmark
    public void testGenerateParallelStreamWithoutDelay(Blackhole bh) {
        animals = doGenerateStream.doGenerateParallelStreamWithoutDelay(animals);
        bh.consume(animals);
    }

    @Benchmark
    public void testForJoinPool(Blackhole bh) {
        animals = doGenerateStream.doGenerateCommonComparatorWithForkJoinPool(animals);
        bh.consume(animals);
    }
}
