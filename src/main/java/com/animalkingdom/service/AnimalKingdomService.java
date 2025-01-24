package com.animalkingdom.service;

import com.animalkingdom.model.Animal;
import com.animalkingdom.utilities.Utilities;
import com.animalkingdom.utilities.AnimalTreeDataLoader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnimalKingdomService {

    private static final Log log = LogFactory.getLog(AnimalKingdomService.class);

    private final AnimalTreeDataLoader animalTreeDataLoader;

    private final Utilities utilities;

    // In-memory data store for the list (tree) of animals
    private final List<Map<String, Animal>> animals;

    public AnimalKingdomService(final AnimalTreeDataLoader animalTreeDataLoader, final Utilities utilities) {
        this.animalTreeDataLoader = animalTreeDataLoader;
        this.utilities = utilities;
        this.animals = this.animalTreeDataLoader.createAnimals();
    }

    // Returns the entire tree data set of all animals (including the children)
    public List<Map<String, Animal>> getAnimals() {
        if (animals == null || animals.isEmpty()) {
            log.warn("No animals found.");
            return Collections.emptyList();
        }

        int count = utilities.countAnimals(animals);
        log.info(String.format("Found '%d' animals.", count));
        return animals;
    }

    // Adds a new animal (with the given label) under the parent
    // (by parent ID), and returns the newly created animal's ID
    public String addAnimal(String parentId, String label) {
        // Find the parent animal in the tree data set
        final Animal parent = utilities.findParentbyAnimalId(animals, parentId);
        if (parent == null) {
            return null; // Parent not found
        }

        // Generate a new ID
        final String newAnimalId = utilities.createId(animals);

        // Create the child Animal and wrap it in a map
        Animal child = new Animal(label);
        Map<String, Animal> childMap = new HashMap<>();
        childMap.put(newAnimalId, child);

        // Append this new child to the parent's list of children
        parent.getChildren().add(childMap);

        log.info(String.format("Added a new animal with ID: %s", newAnimalId));
        return newAnimalId;
    }

}
