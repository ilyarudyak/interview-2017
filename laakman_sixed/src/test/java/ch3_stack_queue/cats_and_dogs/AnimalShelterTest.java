package ch3_stack_queue.cats_and_dogs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static ch3_stack_queue.cats_and_dogs.AnimalShelter.AnimalSpecie.CAT;
import static ch3_stack_queue.cats_and_dogs.AnimalShelter.AnimalSpecie.DOG;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by ilyarudyak on 3/23/17.
 */
class AnimalShelterTest {

    private AnimalShelter animalShelter;

    @BeforeEach
    void setUp() {
        animalShelter = new AnimalShelter();
        buildSimpleShelter();
    }

    private void buildSimpleShelter() {
        List<AnimalShelter.Animal> animals = Arrays.asList(
                new AnimalShelter.Animal(DOG, 1),
                new AnimalShelter.Animal(DOG, 2),
                new AnimalShelter.Animal(CAT, 3),
                new AnimalShelter.Animal(DOG, 4),
                new AnimalShelter.Animal(DOG, 5)
        );
        for (AnimalShelter.Animal animal: animals) {
            animalShelter.enqueue(animal);
        }
    }

    @Test
    void dequeueAnySimple() {

        assertEquals(1, animalShelter.dequeueDog().animalId);
        assertEquals(3, animalShelter.dequeueCat().animalId);
        assertEquals(2, animalShelter.dequeueAny().animalId);
        assertEquals(4, animalShelter.dequeueAny().animalId);
        assertEquals(5, animalShelter.dequeueAny().animalId);
    }


}