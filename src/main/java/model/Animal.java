package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.enums.AnimalType;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Animal {
    private String name;
    private LocalDate birthday;
    private Double weight;
    private AnimalType animalType;
    private Cage cage;
    private List<Person> persons;
}
