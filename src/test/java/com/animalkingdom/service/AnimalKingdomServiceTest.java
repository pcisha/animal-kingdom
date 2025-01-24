package com.animalkingdom.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.animalkingdom.model.Animal;
import com.animalkingdom.utilities.AnimalTreeDataLoader;
import com.animalkingdom.utilities.Utilities;

@SpringBootTest
public class AnimalKingdomServiceTest {

    private AnimalKingdomService animalKingdomService;
    private AnimalTreeDataLoader animalTreeDataLoader;
    private Utilities utilities;

    private List<Map<String, Animal>> animals;

    @BeforeEach
    public void setUp() {
        animalTreeDataLoader = mock(AnimalTreeDataLoader.class);
        utilities = mock(Utilities.class);

        Animal root = new Animal("root");
        Animal dog = new Animal("dog");
        root.getChildren().add(Map.of("2", dog));
        animals = List.of(Map.of("1", root));
    }

    // GET api/tree
    @Test
    public void getAnimals_ShouldReturnAnimals_WhenDataExists() {
        when(animalTreeDataLoader.createAnimals()).thenReturn(animals);
        when(utilities.countAnimals(animals)).thenReturn(2);
        animalKingdomService = new AnimalKingdomService(animalTreeDataLoader, utilities);

        List<Map<String, Animal>> animalList = animalKingdomService.getAnimals();

        assertNotNull(animalList);
        assertEquals(1, animalList.size());
        assertEquals("root", animalList.get(0).get("1").getLabel());
    }

    @Test
    public void getAnimals_ShouldReturnEmptyList_WhenNoAnimalsExists() {
        when(animalTreeDataLoader.createAnimals()).thenReturn(Collections.emptyList());
        when(utilities.countAnimals(Collections.emptyList())).thenReturn(0);
        animalKingdomService = new AnimalKingdomService(animalTreeDataLoader, utilities);

        List<Map<String, Animal>> animalList = animalKingdomService.getAnimals();

        assertNotNull(animalList);
        assertTrue(animalList.isEmpty());
    }

    @Test
    public void getAnimals_ShouldReturnEmptyList_WhenDataLoaderReturnsNull() {
        when(animalTreeDataLoader.createAnimals()).thenReturn(null);
        animalKingdomService = new AnimalKingdomService(animalTreeDataLoader, utilities);

        List<Map<String, Animal>> animalList = animalKingdomService.getAnimals();

        assertNotNull(animalList);
        assertTrue(animalList.isEmpty());
    }

    @Test
    public void getAnimals_ShouldReturnNestedChildren() throws Exception {
        Animal root = new Animal("root");
        Animal lion = new Animal("lion");
        Animal tiger = new Animal("tiger");
        Animal cat = new Animal("cat"); // Grandchild

        lion.getChildren().add(Map.of("3", cat));
        root.getChildren().add(Map.of("2", lion));
        root.getChildren().add(Map.of("4", tiger));

        animals = List.of(Map.of("1", root));
        when(animalTreeDataLoader.createAnimals()).thenReturn(animals);
        when(utilities.countAnimals(animals)).thenReturn(4);
        animalKingdomService = new AnimalKingdomService(animalTreeDataLoader, utilities);

        List<Map<String, Animal>> animalList = animalKingdomService.getAnimals();

        assertNotNull(animalList);
        assertEquals(1, animalList.size());
        assertEquals("root", animalList.get(0).get("1").getLabel());
        assertEquals(2, animalList.get(0).get("1").getChildren().size()); // 2 children
        assertEquals("lion", animalList.get(0).get("1").getChildren().get(0).get("2").getLabel());
        assertEquals(1, animalList.get(0).get("1").getChildren().get(0).get("2").getChildren().size()); // 1 child
        assertEquals("cat",
                animalList.get(0).get("1").getChildren().get(0).get("2").getChildren().get(0).get("3").getLabel());
        assertEquals("tiger", animalList.get(0).get("1").getChildren().get(1).get("4").getLabel());
    }

    // POST api/tree
    @Test
    public void addAnimal_ShouldAddAnimalSuccessfully() {
        Animal root = new Animal("root");
        animals = List.of(Map.of("1", root));

        when(animalTreeDataLoader.createAnimals()).thenReturn(animals);
        when(utilities.findParentbyAnimalId(animals, "1")).thenReturn(root);
        when(utilities.createId(animals)).thenReturn("2");
        animalKingdomService = new AnimalKingdomService(animalTreeDataLoader, utilities);

        String newAnimalId = animalKingdomService.addAnimal("1", "spider");

        assertEquals(1, root.getChildren().size());
        assertNotNull(newAnimalId);
        assertEquals("2", newAnimalId);
        Animal spider = root.getChildren().get(0).get("2");
        assertNotNull(spider);
        assertEquals("spider", spider.getLabel());
    }

    @Test
    public void addAnimal_ShouldReturnNull_WhenParentNotFound() {
        when(utilities.findParentbyAnimalId(animals, "999")).thenReturn(null);
        animalKingdomService = new AnimalKingdomService(animalTreeDataLoader, utilities);

        String newAnimalId = animalKingdomService.addAnimal("999", "lion");
        assertNull(newAnimalId);
    }

    @Test
    public void addAnimal_ShouldReturnNull_WhenLabelIsEmpty() {
        Animal root = new Animal("root");
        animals = List.of(Map.of("1", root));

        when(animalTreeDataLoader.createAnimals()).thenReturn(animals);
        animalKingdomService = new AnimalKingdomService(animalTreeDataLoader, utilities);

        String newAnimalId = animalKingdomService.addAnimal("1", "");
        assertNull(newAnimalId);
    }

    @Test
    public void addAnimal_ShouldAddMultipleChildrenSuccessfully() {
        Animal root = new Animal("root");
        animals = List.of(Map.of("1", root));

        when(animalTreeDataLoader.createAnimals()).thenReturn(animals);
        when(utilities.findParentbyAnimalId(animals, "1")).thenReturn(root);
        when(utilities.createId(animals)).thenReturn("2").thenReturn("3");
        animalKingdomService = new AnimalKingdomService(animalTreeDataLoader, utilities);

        assertEquals(0, root.getChildren().size());

        String lionId = animalKingdomService.addAnimal("1", "lion");
        String tigerId = animalKingdomService.addAnimal("1", "tiger");

        assertNotNull(lionId);
        assertNotNull(tigerId);
        assertEquals("2", lionId);
        assertEquals("3", tigerId);

        assertEquals(2, root.getChildren().size()); // 2 children;

        Animal lion = root.getChildren().get(0).get("2");
        Animal tiger = root.getChildren().get(1).get("3");

        assertNotNull(lion);
        assertNotNull(tiger);
        assertEquals("lion", lion.getLabel());
        assertEquals("tiger", tiger.getLabel());
    }

}
