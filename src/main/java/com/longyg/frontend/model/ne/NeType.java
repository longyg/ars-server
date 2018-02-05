package com.longyg.frontend.model.ne;

import org.springframework.context.annotation.Primary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

@Document(collection = "ne_types")
public class NeType {
    @Id
    private String id;
    @Indexed(unique = true)
    private String name;
    private String presentation;
    private String description;
    private String agentClass;
    private List<String> adaptSet = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPresentation() {
        return presentation;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAgentClass() {
        return agentClass;
    }

    public void setAgentClass(String agentClass) {
        this.agentClass = agentClass;
    }

    public List<String> getAdaptSet() {
        return adaptSet;
    }

    public void setAdaptSet(List<String> adaptSet) {
        this.adaptSet = adaptSet;
    }
}
