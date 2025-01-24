package com.animalkingdom.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.animalkingdom.model.Animal;

// In-memory data store for the list (tree) of animals
@Component
public class AnimalTreeDataLoader {

    private static final Log log = LogFactory.getLog(AnimalTreeDataLoader.class);

    private final Utilities utilities;

    public AnimalTreeDataLoader(final Utilities utilities) {
        this.utilities = utilities;
    }

    public List<Map<String, Animal>> createAnimals() {

        List<Map<String, Animal>> animals = new ArrayList<>();

        // Initialize the data set
        Animal root = new Animal("root");
        Animal ant = new Animal("ant");
        Animal bear = new Animal("bear");
        Animal cat = new Animal("cat");
        Animal dog = new Animal("dog");
        Animal elephant = new Animal("elephant");
        Animal frog = new Animal("frog");

        // Build the subtrees (with children):
        // 2: ant -> children: []
        // 3: bear -> children: 4, 5
        // 4: cat -> []
        // 5: dog -> children: 6
        // 6: elephant -> []
        // 7: frog -> []
        Map<String, Animal> antMap = Map.of("2", ant);
        Map<String, Animal> catMap = Map.of("4", cat);
        Map<String, Animal> elephantMap = Map.of("6", elephant);

        Map<String, Animal> dogMap = new HashMap<>();
        dogMap.put("5", dog);
        dog.getChildren().add(elephantMap); // dog's children are "6"

        Map<String, Animal> bearMap = new HashMap<>();
        bearMap.put("3", bear);
        bear.getChildren().add(catMap); // bear's children are "4" and "5"
        bear.getChildren().add(dogMap);

        // root's children: ["2", "3", "7"]
        root.getChildren().add(antMap);
        root.getChildren().add(bearMap);

        Map<String, Animal> frogMap = Map.of("7", frog);
        root.getChildren().add(frogMap);

        Map<String, Animal> rootMap = new HashMap<>();
        rootMap.put("1", root);
        animals.add(rootMap);

        log.info(String.format("Created data set with '%d' animals", utilities.countAnimals(animals)));
        return animals;
    }

}
