package com.animalkingdom.controller.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.animalkingdom.model.Animal;

public class GetAnimalsResponse {

    @JsonProperty("animals")
    private List<Map<String, Animal>> animals;

    public GetAnimalsResponse() {
    }

    public GetAnimalsResponse(List<Map<String, Animal>> animals) {
        this.animals = animals;
    }

    public List<Map<String, Animal>> getAnimals() {
        return animals;
    }

    public void setAnimals(List<Map<String, Animal>> animals) {
        this.animals = animals;
    }

}
