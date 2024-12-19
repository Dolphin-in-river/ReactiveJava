package service;

import generator.AnimalTaskPoolService;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;
import model.Animal;
import model.Cage;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DoGenerateStream {
    public static long DELAY = 1L;
    public static long WITHOUT_DELAY = 0L;

    public List<Animal> doGenerateSingleStreamWithDelay(List<Animal> animals) {
        Map<Cage, Long> animalCountByCageStreamApi = animals.stream()
                .collect(Collectors.groupingBy(animal -> animal.getCageDelay(WITHOUT_DELAY), Collectors.counting()));
        return animals;
    }

    public List<Animal> doGenerateSingleStreamWithoutDelay(List<Animal> animals) {
        Map<Cage, Long> animalCountByCageStreamApi = animals.stream()
                .collect(Collectors.groupingBy(animal -> animal.getCage(), Collectors.counting()));
        return animals;
    }

    public List<Animal> doGenerateParallelStreamWithDelay(List<Animal> animals) {
        Map<Cage, Long> animalCountByCageStreamApi = animals.parallelStream()
                .collect(Collectors.groupingBy(animal -> animal.getCageDelay(DELAY), Collectors.counting()));
        return animals;
    }

    public List<Animal> doGenerateParallelStreamWithoutDelay(List<Animal> animals) {
        Map<Cage, Long> animalCountByCageStreamApi = animals.parallelStream()
                .collect(Collectors.groupingBy(animal -> animal.getCage(), Collectors.counting()));
        return animals;
    }

    public List<Animal> doGenerateCommonComparatorWithForkJoinPool(List<Animal> animals) {
        ForkJoinPool customPool = new ForkJoinPool(8);
        Map<Cage, Long> animalCountByCage = customPool.invoke(new AnimalTaskPoolService(animals));
        return animals;
    }

    public List<Animal> doGenerateCommonComparatorWithRxJavaWithDelay(List<Animal> animals) {
        Map<Cage, Long> animalCountByCageStreamApi = Observable.fromIterable(animals)
                .flatMap(animal -> Observable.just(animal)
                        .delay(DELAY, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.computation()))
                .toList()
                .map(list -> list.stream()
                        .collect(Collectors.groupingBy(Animal::getCage, Collectors.counting())))
                .blockingGet();
        return animals;
    }
}
