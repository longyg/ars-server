package com.longyg.frontend.model.config;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "parent_objects")
public class ParentObject {
    @Id
    private String id;
    @Indexed (unique = true)
    private String name;
    private String presentation;
    private String nameInOMeS;
    private String isTransient;

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

    public String getNameInOMeS() {
        return nameInOMeS;
    }

    public void setNameInOMeS(String nameInOMeS) {
        this.nameInOMeS = nameInOMeS;
    }

    public String getIsTransient() {
        return isTransient;
    }

    public void setIsTransient(String isTransient) {
        this.isTransient = isTransient;
    }

    public boolean isTransient() {
        return isTransient.equalsIgnoreCase("yes") ? true : false;
    }
}
