package com.animalkingdom.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Animal {

    private String label;
    private List<Map<String, Animal>> children = new ArrayList<>();

    public Animal() {
    }

    public Animal(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Map<String, Animal>> getChildren() {
        return children;
    }

    public void setChildren(List<Map<String, Animal>> children) {
        this.children = children;
    }

}
