package com.animalkingdom.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.animalkingdom.model.Animal;
import com.animalkingdom.service.AnimalKingdomService;
import com.animalkingdom.utilities.EndpointConstants;
import com.animalkingdom.utilities.ErrorMessages;

@WebMvcTest(AnimalKingdomController.class)
public class AnimalKingdomControllerTest {

        private static final String URL = EndpointConstants.API + EndpointConstants.TREE;

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private AnimalKingdomService animalKingdomService;

        // GET /api/tree
        @Test
        public void getAnimals_ShouldReturnAnimals_WhenDataExists() throws Exception {
                Animal root = new Animal("root");
                Animal dog = new Animal("dog");
                root.getChildren().add(Map.of("2", dog));

                final List<Map<String, Animal>> animals = List.of(Map.of("1", root));
                when(animalKingdomService.getAnimals()).thenReturn(animals);

                ResultActions response = mockMvc.perform(get(URL)
                                .accept(MediaType.APPLICATION_JSON_VALUE));

                response.andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                                .andExpect(jsonPath("$.animals[0]['1'].label").value("root"))
                                .andExpect(jsonPath("$.animals[0]['1'].children[0]['2'].label").value("dog"));
        }

        @Test
        public void getAnimals_ShouldReturnNotFound_WhenNoAnimalsExist() throws Exception {
                when(animalKingdomService.getAnimals()).thenReturn(Collections.emptyList());

                mockMvc.perform(get(URL)
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                                .andExpect(status().isNotFound());
        }

        @Test
        public void getAnimals_ShouldReturnNotFound_WhenServiceReturnsNull() throws Exception {
                when(animalKingdomService.getAnimals()).thenReturn(null);

                mockMvc.perform(get(URL)
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                                .andExpect(status().isNotFound());
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

                List<Map<String, Animal>> animals = List.of(Map.of("1", root));
                Mockito.when(animalKingdomService.getAnimals()).thenReturn(animals);

                ResultActions response = mockMvc.perform(get(URL)
                                .accept(MediaType.APPLICATION_JSON_VALUE));

                response.andExpect(status().isOk())
                                .andExpect(jsonPath("$.animals[0]['1'].label").value("root"))
                                .andExpect(jsonPath("$.animals[0]['1'].children[0]['2'].label").value("lion"))
                                .andExpect(jsonPath("$.animals[0]['1'].children[1]['4'].label").value("tiger"))
                                .andExpect(jsonPath("$.animals[0]['1'].children[0]['2'].children[0]['3'].label")
                                                .value("cat"));
        }

        @Test
        public void getAnimals_ShouldHandleLargeDataSet() throws Exception {
                Animal root = new Animal("root");
                for (int i = 1; i <= 1000; i++) {
                        root.getChildren().add(Map.of(String.valueOf(i), new Animal("cat" + i)));
                }

                List<Map<String, Animal>> animals = List.of(Map.of("1", root));
                Mockito.when(animalKingdomService.getAnimals()).thenReturn(animals);

                ResultActions response = mockMvc.perform(get(URL)
                                .accept(MediaType.APPLICATION_JSON_VALUE));

                response.andExpect(status().isOk())
                                .andExpect(jsonPath("$.animals[0]['1'].label").value("root"))
                                .andExpect(jsonPath("$.animals[0]['1'].children").isArray())
                                .andExpect(jsonPath("$.animals[0]['1'].children.length()").value(1000));
        }

        // POST /api/tree
        @Test
        public void addAnimal_ShouldReturnCreated_WhenAnimalIsAddedSuccessfully() throws Exception {
                String newAnimalId = "3";
                final String requestBody = """
                                {
                                    "parent": "1",
                                    "label": "cat"
                                }
                                """;
                Mockito.when(animalKingdomService.addAnimal(eq("1"), eq("cat"))).thenReturn(newAnimalId);

                ResultActions response = mockMvc.perform(post(URL)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(requestBody));

                response.andExpect(status().isCreated())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                                .andExpect(jsonPath("$.animalId").value("3"))
                                .andExpect(jsonPath("$.label").value("cat"))
                                .andExpect(jsonPath("$.parent").value("1"));
        }

        @Test
        public void addAnimal_ShouldReturnBadRequest_WhenParentIdIsMissing() throws Exception {
                final String requestBody = """
                                {
                                    "parent": "",
                                    "label": "tiger"
                                }
                                """;
                mockMvc.perform(post(URL)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(requestBody))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().string(ErrorMessages.ANIMAL_PARENT_NOT_FOUND));
        }

        @Test
        public void addAnimal_ShouldReturnBadRequest_WhenLabelIsMissing() throws Exception {
                final String requestBody = """
                                {
                                    "parent": "1",
                                    "label": ""
                                }
                                """;
                mockMvc.perform(post(URL)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(requestBody))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().string(ErrorMessages.ANIMAL_LABEL_REQUIRED));
        }

        @Test
        public void addAnimal_ShouldReturnBadRequest_WhenServiceFailsToAddAnimal() throws Exception {
                Mockito.when(animalKingdomService.addAnimal(eq("1"), eq("lion"))).thenReturn(null);

                final String requestBody = """
                                {
                                    "parent": "1",
                                    "label": "lion"
                                }
                                """;
                String errorMessage = String.format("%s %s", ErrorMessages.ANIMAL_NOT_CREATED_WITH_ID, "1");
                mockMvc.perform(post(URL)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(requestBody))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().string(errorMessage));
        }

        @Test
        public void addAnimal_ShouldReturnBadRequest_ForInvalidJson() throws Exception {
                mockMvc.perform(post(URL)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content("{''}"))
                                .andExpect(status().isBadRequest());
        }

        @Test
        public void addAnimal_ShouldReturnBadRequest_WhenParentOrLabelFieldIsMissing() throws Exception {
                mockMvc.perform(post(URL)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content("""
                                                {
                                                    "label": "tiger"
                                                }
                                                """))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().string(ErrorMessages.ANIMAL_PARENT_NOT_FOUND));

                mockMvc.perform(post(URL)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content("""
                                                {
                                                    "parent": "1"
                                                }
                                                """))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().string(ErrorMessages.ANIMAL_LABEL_REQUIRED));
        }

}