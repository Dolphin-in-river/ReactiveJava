package service;

import generator.AnimalTaskPoolService;
import lombok.RequiredArgsConstructor;
import model.Animal;
import model.Cage;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DoGenerateStream {
    public static long DELAY = 1L;
    public static long WITHOUT_DELAY = 0L;

    public List<Animal> doGenerateSingleStreamWithDelay(List<Animal> animals ) {
        Map<Cage, Long> animalCountByCageStreamApi = animals.stream()
                .collect(Collectors.groupingBy(animal -> animal.getCageDelay(WITHOUT_DELAY), Collectors.counting()));
        return animals;
    }

    public List<Animal> doGenerateSingleStreamWithoutDelay(List<Animal> animals ) {
        Map<Cage, Long> animalCountByCageStreamApi = animals.stream()
                .collect(Collectors.groupingBy(animal -> animal.getCage(), Collectors.counting()));
        return animals;
    }

    public List<Animal> doGenerateParallelStreamWithDelay(List<Animal> animals ) {
        Map<Cage, Long> animalCountByCageStreamApi = animals.parallelStream()
                .collect(Collectors.groupingBy(animal -> animal.getCageDelay(WITHOUT_DELAY), Collectors.counting()));
        return animals;
    }

    public List<Animal> doGenerateParallelStreamWithoutDelay(List<Animal> animals ) {
        Map<Cage, Long> animalCountByCageStreamApi = animals.parallelStream()
                .collect(Collectors.groupingBy(animal -> animal.getCage(), Collectors.counting()));
        return animals;
    }

    public List<Animal> doGenerateCommonComparatorWithForkJoinPool(List<Animal> animals ) {
        ForkJoinPool customPool = new ForkJoinPool(6);
        Map<Cage, Long> animalCountByCage = customPool.invoke(new AnimalTaskPoolService(animals));
        return animals;
    }
}
