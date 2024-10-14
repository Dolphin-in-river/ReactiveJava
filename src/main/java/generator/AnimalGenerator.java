package generator;

import model.Animal;
import model.Cage;
import model.Person;
import model.enums.AnimalType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AnimalGenerator {
    private final static Integer MAX_COUNT_OF_PERSON = 10;
    private final Random random = new Random();

    public Animal generateAnimal() {
        Animal animal = new Animal();
        animal.setName(generateName());
        animal.setBirthday(generateBirthday());
        animal.setWeight(generateWeight());
        animal.setAnimalType(generateAnimalType());
        animal.setCage(generateCage());
        animal.setPersons(generatePersons());
        return animal;
    }

    private String generateName() {
        return "Animal #" + random.nextInt(1000);
    }

    private LocalDate generateBirthday() {
        return LocalDate.now().minusDays(random.nextInt(365));
    }

    private Double generateWeight() {
        return random.nextDouble() * 100;
    }

    private AnimalType generateAnimalType() {
        return AnimalType.values()[random.nextInt(AnimalType.values().length)];
    }

    private Cage generateCage() {
        return new Cage(random.nextInt(1000));
    }

    private List<Person> generatePersons() {
        int count = random.nextInt(MAX_COUNT_OF_PERSON);
        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            persons.add(new Person("Person" + random.nextInt(MAX_COUNT_OF_PERSON)));
        }
        return persons;
    }
}
