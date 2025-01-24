package com.animalkingdom.utilities;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.animalkingdom.model.Animal;

// Utility class for operations on the tree data set of Animals
@Component
public class Utilities {

    public Utilities() {
    }

    // Searches the tree for an Animal whose map key matches the given animal ID
    public Animal findParentbyAnimalId(List<Map<String, Animal>> animals, String animalId) {
        for (Map<String, Animal> map : animals) {
            for (Map.Entry<String, Animal> entry : map.entrySet()) {
                String id = entry.getKey();
                Animal animal = entry.getValue();

                if (id.equals(animalId)) {
                    return animal; // Found a match
                }

                // Recurse into the children (if present)
                if (animal.getChildren() != null && !animal.getChildren().isEmpty()) {
                    Animal parent = findParentbyAnimalId(animal.getChildren(), animalId);
                    if (parent != null) {
                        return parent;
                    }
                }
            }
        }
        return null; // Not
    }

    // Performs a DFS on the given tree to find the highest ID among all nodes.
    // Returns that ID + 1 as a new ID
    public String createId(List<Map<String, Animal>> animals) {
        int maxId = 0;
        Deque<List<Map<String, Animal>>> deque = new ArrayDeque<>();
        deque.push(animals);

        while (!deque.isEmpty()) {
            List<Map<String, Animal>> listOfAnimals = deque.pop();
            for (Map<String, Animal> map : listOfAnimals) {
                for (Map.Entry<String, Animal> entry : map.entrySet()) {
                    int id = Integer.parseInt(entry.getKey());
                    maxId = Math.max(maxId, id);

                    // Push the children of the current Animal (if any)
                    Animal animal = entry.getValue();
                    if (animal.getChildren() != null && !animal.getChildren().isEmpty()) {
                        deque.push(animal.getChildren());
                    }
                }
            }
        }
        return String.valueOf(maxId + 1); // New Animal ID
    }

    // Recursively counts how many Animal nodes are in the tree data set
    public int countAnimals(List<Map<String, Animal>> animals) {
        int count = 0;
        for (Map<String, Animal> map : animals) {
            for (Animal animal : map.values()) {
                count++; // Count the current animal
                // Recurse into the animal's children (if any)
                if (animal.getChildren() != null && !animal.getChildren().isEmpty()) {
                    count += countAnimals(animal.getChildren());
                }
            }
        }
        return count;
    }

}
