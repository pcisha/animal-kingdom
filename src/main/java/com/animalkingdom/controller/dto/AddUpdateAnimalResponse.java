package com.animalkingdom.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AddUpdateAnimalResponse {

    @JsonProperty("animalId")
    private String animalId;

    @JsonProperty("label")
    private String label;

    @JsonProperty("parent")
    private String parent;

    public AddUpdateAnimalResponse() {
    }

    public AddUpdateAnimalResponse(String animalId, String label, String parent) {
        this.animalId = animalId;
        this.label = label;
        this.parent = parent;
    }

    public String getAnimalId() {
        return animalId;
    }

    public void setAnimalId(String animalId) {
        this.animalId = animalId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

}