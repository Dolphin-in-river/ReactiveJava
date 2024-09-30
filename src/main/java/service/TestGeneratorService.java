package service;

import generator.AnimalCollector;
import generator.AnimalGenerator;
import model.Animal;
import model.Cage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestGeneratorService {
    public static void doGenerate(Long countOfCollections) {
        AnimalGenerator generator = new AnimalGenerator();
        List<Animal> animals = Stream.generate(generator::generateAnimal).limit(countOfCollections).toList();

        // -------------------------- Базовое итерирование --------------------------------
        long start = System.currentTimeMillis();
        Map<Cage, Long> animalCountByCage__Basic_iterable = new HashMap<>();
        for (Animal animal : animals) {
            animalCountByCage__Basic_iterable.merge(animal.getCage(), 1L, Long::sum);
        }
        long end = System.currentTimeMillis();
        System.out.println("Размер массива: " + countOfCollections + " итерационный цикл: " + (end - start) + " ms");

        // -------------------------- StreamApi --------------------------------
        start = System.currentTimeMillis();
        Map<Cage, Long> animalCountByCage__StreamApi = animals.stream()
                .collect(Collectors.groupingBy(Animal::getCage, Collectors.counting()));
        end = System.currentTimeMillis();
        System.out.println("Размер массива: " + countOfCollections +
                " конвейер при помощи stream API: " + (end - start) + " ms");

        // -------------------------- Свой коллектор --------------------------------
        start = System.currentTimeMillis();
        Map<Cage, Long> animalCountByCage = animals.stream().collect(new AnimalCollector());
        end = System.currentTimeMillis();
        System.out.println("Размер массива: " + countOfCollections +
                " конвейер при помощи самописного коллектора: " + (end - start) + " ms");
    }
}
