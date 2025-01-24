package com.animalkingdom.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AddUpdateAnimalRequest {

    @JsonProperty("parent")
    private String parent;

    @JsonProperty("label")
    private String label;

    public AddUpdateAnimalRequest() {
    }

    public AddUpdateAnimalRequest(String parent, String label) {
        this.parent = parent;
        this.label = label;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}
