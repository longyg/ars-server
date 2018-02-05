package com.longyg.backend.adaptation.pm;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ylong on 2/14/2017.
 */
public class ObjectClass {
    private static final Logger LOG = Logger.getLogger(ObjectClass.class);
    private String name;
    private String nameInOmes;
    private boolean isTransient;
    private String presentation;

    private ObjectClass parentObject;
    private List<ObjectClass> childObjects = new ArrayList<ObjectClass>();

    public void addChildObject(ObjectClass objectClass) {
        if (!childObjects.contains(objectClass)) {
            childObjects.add(objectClass);
        }
    }

    public ObjectClass getParentObject() {
        return parentObject;
    }

    public void setParentObject(ObjectClass parentObject) throws Exception {
        if (null != this.parentObject && !this.parentObject.equals(parentObject)) {
            LOG.error("Multiple parent objects are defined for class: " + name);
            throw new Exception("Multiple parent objects are defined for class: " + name);
        }
        this.parentObject = parentObject;
    }

    public List<ObjectClass> getChildObjects() {
        return childObjects;
    }

    public void setChildObjects(List<ObjectClass> childObjects) {
        this.childObjects = childObjects;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameInOmes() {
        return nameInOmes;
    }

    public void setNameInOmes(String nameInOmes) {
        this.nameInOmes = nameInOmes;
    }

    public boolean isTransient() {
        return isTransient;
    }

    public void setTransient(boolean aTransient) {
        isTransient = aTransient;
    }

    public String getPresentation() {
        return presentation;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObjectClass that = (ObjectClass) o;

        if (!name.equals(that.name)) return false;
        return nameInOmes.equals(that.nameInOmes);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + nameInOmes.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ObjectClass{" +
                "name='" + name + '\'' +
                ", nameInOmes='" + nameInOmes + '\'' +
                ", isTransient=" + isTransient +
                ", presentation='" + presentation + '\'' +
                '}';
    }
}
