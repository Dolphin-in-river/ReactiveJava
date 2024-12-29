package service;

import generator.AnimalGenerator;
import model.Animal;
import reactor.core.publisher.Flux;
import java.time.Duration;
import java.util.Random;

public class AnimalFxService {
    private final AnimalGenerator animalGenerator;
    public AnimalFxService(AnimalGenerator animalGenerator) {
        this.animalGenerator = animalGenerator;
    }

    private Random random = new Random();

    public Flux<Animal> getAnimalData() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(tick -> animalGenerator.generateAnimal());
    }
}
