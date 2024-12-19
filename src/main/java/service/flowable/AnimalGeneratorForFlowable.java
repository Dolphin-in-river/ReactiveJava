package service.flowable;

import generator.AnimalGenerator;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import model.Animal;

public class AnimalGeneratorForFlowable {
    private final AnimalGenerator animalGenerator;

    public AnimalGeneratorForFlowable(AnimalGenerator animalGenerator) {
        this.animalGenerator = animalGenerator;
    }

    public Flowable<Animal> generateAnimals(int count) {
        return Flowable.range(1, count)
                .map(i -> animalGenerator.generateAnimal())
                .observeOn(Schedulers.computation());
    }
}
