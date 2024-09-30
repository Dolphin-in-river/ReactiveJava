package model.enums;

public enum AnimalType {
    MAMMAL("herbivore"),
    BIRD("omnivore"),
    REPTILE("carnivore"),
    AMPHIBIAN("omnivore"),
    FISH("carnivore");
    private String eatType;
    AnimalType(String eatType) {
        this.eatType = eatType;
    }
    public String getEatType() {
        return eatType;
    }
}
