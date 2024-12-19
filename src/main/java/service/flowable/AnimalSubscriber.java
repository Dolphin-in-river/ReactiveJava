package service.flowable;

import io.reactivex.rxjava3.core.FlowableSubscriber;
import model.Animal;
import model.Cage;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnimalSubscriber implements FlowableSubscriber<Animal> {
    private Subscription subscription;
    private int count = 0;
    private static final long BATCH_SIZE = 100001;
    private static long start;
    private List<Animal> animals = new ArrayList<>();

    @Override
    public void onSubscribe(Subscription subscription) {
        start = System.currentTimeMillis();
        this.subscription = subscription;
        subscription.request(BATCH_SIZE);
    }

    @Override
    public void onNext(Animal animal) {
        count++;
        animals.add(animal);
        if (count % BATCH_SIZE == 0) {
            animals.parallelStream()
                    .collect(Collectors.groupingBy(entity -> entity.getCage(), Collectors.counting()));
            animals = new ArrayList<>();
            var current = System.currentTimeMillis();
            System.out.println("Обработка текущей пачки заняла: " + (current - start) + "мс");
            start = current;
            subscription.request(BATCH_SIZE);
        }
    }

    @Override
    public void onError(Throwable t) {
        System.err.println("Error: " + t.getMessage());
    }

    @Override
    public void onComplete() {
        System.out.println("Completed. Total count: " + count);
    }
}
