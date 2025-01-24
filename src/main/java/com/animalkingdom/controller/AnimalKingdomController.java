package com.animalkingdom.controller;

import com.animalkingdom.controller.dto.AddUpdateAnimalRequest;
import com.animalkingdom.controller.dto.AddUpdateAnimalResponse;
import com.animalkingdom.controller.dto.GetAnimalsResponse;
import com.animalkingdom.model.Animal;
import com.animalkingdom.service.AnimalKingdomService;
import com.animalkingdom.utilities.EndpointConstants;
import com.animalkingdom.utilities.ErrorMessages;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(EndpointConstants.API)
public class AnimalKingdomController {

    private static final Log log = LogFactory.getLog(AnimalKingdomController.class);

    @Autowired
    private AnimalKingdomService animalKingdomService;

    public AnimalKingdomController(final AnimalKingdomService animalKingdomService) {
        this.animalKingdomService = animalKingdomService;
    }

    /**
     * GET /api/tree
     * Returns the entire tree of animals (including their children).
     */
    @GetMapping(value = EndpointConstants.TREE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetAnimalsResponse> getAnimals() {

        log.info("Fetching the entire animal tree...");
        List<Map<String, Animal>> animals = animalKingdomService.getAnimals();

        if (animals == null || animals.isEmpty()) {
            log.error(ErrorMessages.NO_ANIMALS_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        log.info("Animals successfully retrieved.");
        GetAnimalsResponse getAnimalsResponse = new GetAnimalsResponse(animals);
        return ResponseEntity.ok(getAnimalsResponse);
    }

    /**
     * Adds a new animal under a given parent.
     * 
     * @param addUpdateAnimalRequest the request containing parent ID and the label
     * @return a response entity containing either an error status
     *         or the newly created addUpdateAnimalResponse
     */
    @PostMapping(value = EndpointConstants.TREE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addAnimal(@NonNull @Validated @RequestBody AddUpdateAnimalRequest addUpdateAnimalRequest) {

        log.info("Adding a new animal...");

        if (addUpdateAnimalRequest.getParent() == null || addUpdateAnimalRequest.getParent().isBlank()) {
            log.error(ErrorMessages.ANIMAL_CANNOT_BE_ADDED_WITHOUT_PARENT);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessages.ANIMAL_PARENT_NOT_FOUND);
        }
        if (addUpdateAnimalRequest.getLabel() == null || addUpdateAnimalRequest.getLabel().isBlank()) {
            log.error(ErrorMessages.ANIMAL_CANNOT_BE_ADDED_WITHOUT_LABEL);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessages.ANIMAL_LABEL_REQUIRED);
        }

        String newAnimalId = animalKingdomService.addAnimal(
                addUpdateAnimalRequest.getParent(), addUpdateAnimalRequest.getLabel());

        if (newAnimalId == null || newAnimalId.isBlank()) {
            log.error(ErrorMessages.ANIMAL_NOT_CREATED);
            String message = String.format("%s %s", ErrorMessages.ANIMAL_NOT_CREATED_WITH_ID,
                    addUpdateAnimalRequest.getParent());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(message);
        }

        log.info(String.format("Animal created successfully with ID: '%s'", newAnimalId));
        AddUpdateAnimalResponse addUpdateAnimalResponse = new AddUpdateAnimalResponse(
                newAnimalId, addUpdateAnimalRequest.getLabel(), addUpdateAnimalRequest.getParent());
        return ResponseEntity.status(HttpStatus.CREATED).body(addUpdateAnimalResponse);
    }

}
