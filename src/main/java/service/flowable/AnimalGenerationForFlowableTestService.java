package service.flowable;

public class AnimalGenerationForFlowableTestService {
    private final AnimalGeneratorForFlowable animalGeneratorForFlowable;
    private final AnimalSubscriber animalSubscriber;

    public AnimalGenerationForFlowableTestService(AnimalGeneratorForFlowable animalGeneratorForFlowable,
                                                  AnimalSubscriber animalSubscriber) {
        this.animalGeneratorForFlowable = animalGeneratorForFlowable;
        this.animalSubscriber = animalSubscriber;
    }

    public void doTest(Integer size) {
        animalGeneratorForFlowable.generateAnimals(size).blockingSubscribe(animalSubscriber);
    }
}
