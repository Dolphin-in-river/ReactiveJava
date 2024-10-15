package generator;

import model.Animal;
import model.Cage;

import java.util.List;
import java.util.Map;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

import static service.DoGenerateStream.WITHOUT_DELAY;

public class AnimalTaskPoolService extends RecursiveTask<Map<Cage, Long>> {
    private final List<Animal> animals;
    private static final int THRESHOLD = 10000;

    public AnimalTaskPoolService(List<Animal> animals) {
        this.animals = animals;
    }

    @Override
    protected Map<Cage, Long> compute() {
        if (animals.size() <= THRESHOLD) {
            return animals.stream()
                    .collect(Collectors.groupingBy(animal -> animal.getCageDelay(WITHOUT_DELAY), Collectors.counting()));
        } else {
            int mid = animals.size() / 2;
            AnimalTaskPoolService task1 = new AnimalTaskPoolService(animals.subList(0, mid));
            AnimalTaskPoolService task2 = new AnimalTaskPoolService(animals.subList(mid, animals.size()));

            task1.fork();
            Map<Cage, Long> result2 = task2.compute();
            Map<Cage, Long> result1 = task1.join();
            return mergeResults(result1, result2);
        }
    }

    private Map<Cage, Long> mergeResults(Map<Cage, Long> result1, Map<Cage, Long> result2) {
        result2.forEach((cage, count) -> result1.merge(cage, count, Long::sum));
        return result1;
    }
}
