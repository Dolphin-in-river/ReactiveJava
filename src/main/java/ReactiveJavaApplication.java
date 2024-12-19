import generator.AnimalGenerator;
import service.flowable.AnimalGenerationForFlowableTestService;
import service.flowable.AnimalGeneratorForFlowable;
import service.flowable.AnimalSubscriber;

//import static service.TestGeneratorService.doGenerate;

public class ReactiveJavaApplication {
    private final static Long COUNT_OF_COLLECTION_TEST_1 = 5000L;
    private final static Long COUNT_OF_COLLECTION_TEST_2 = 50000L;
    private final static Long COUNT_OF_COLLECTION_TEST_3 = 250000L;

    public static void main(String[] args) {
//        doGenerate(COUNT_OF_COLLECTION_TEST_3);
//        System.out.println("-------------------------------------------------------------------------------");
//        doGenerate(COUNT_OF_COLLECTION_TEST_2);
//        System.out.println("-------------------------------------------------------------------------------");
//        doGenerate(COUNT_OF_COLLECTION_TEST_1);
        AnimalGenerationForFlowableTestService animalGenerationForFlowableTestService =
                new AnimalGenerationForFlowableTestService(
                        new AnimalGeneratorForFlowable(new AnimalGenerator()),
                        new AnimalSubscriber());
        animalGenerationForFlowableTestService.doTest(10000000);
    }
}
