package ch3_stack_queue.cats_and_dogs;

import java.time.Instant;
import java.util.LinkedList;

public class AnimalShelter {

    enum AnimalSpecie { CAT, DOG }

    private LinkedList<Animal> catQueue;
    private LinkedList<Animal> dogQueue;

    static class Animal {

        AnimalSpecie animalSpecie;
        int animalId;
        Instant timeStamp;

        public Animal(AnimalSpecie animalSpecie, int animalId) {
            this.animalSpecie = animalSpecie;
            this.animalId = animalId;
            this.timeStamp = Instant.now();
        }
    }

    public AnimalShelter() {
        catQueue = new LinkedList<>();
        dogQueue = new LinkedList<>();
    }

    public void enqueue (Animal animal) {
        if (animal.animalSpecie == AnimalSpecie.CAT) {
            catQueue.addLast(animal);
        } else {
            dogQueue.addLast(animal);
        }
    }

    // most interesting method (see readme file)
    public Animal dequeueAny() {
        if      (isCatsEmpty()) { return dequeueDog(); }
        else if (isDogsEmpty()) { return dequeueCat(); }
        else                    { return dequeueBasedOnTimestamp(); }
    }

    private Animal dequeueBasedOnTimestamp() {
        Animal cat = catQueue.getFirst();
        Animal dog = catQueue.getFirst();
        if (cat.timeStamp.isBefore(dog.timeStamp)) {
            return dequeueCat();
        } else {
            return dequeueDog();
        }
    }

    public Animal dequeueCat() {
        return catQueue.removeFirst();
    }

    public Animal dequeueDog() {
        return dogQueue.removeFirst();
    }

    public boolean isEmpty() {
        return catQueue.isEmpty() && dogQueue.isEmpty();
    }

    public boolean isCatsEmpty() {
        return catQueue.isEmpty();
    }

    public boolean isDogsEmpty() {
        return dogQueue.isEmpty();
    }

}
























