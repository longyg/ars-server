package com.longyg.backend.adaptation.topology;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ylong on 2/15/2017.
 */
public class PmbObject implements Comparable<PmbObject> {
    private String id;
    private String name;
    private String nameInOmes;
    private boolean isTransient;
    private String presentation;
    private boolean isMeasuredObject;
    private String dn;
    private String originalDn;

    private List<String> supporteredVersions = new ArrayList<>();
    private List<String> dimensions = new ArrayList<>();
    private List<PmbObject> childObjects = new ArrayList<>();
    private List<PmbObject> parentObjects = new ArrayList<>();

    // object load
    private int min;
    private int max;
    private int avg;
    private int avgPerNet;
    private int maxPerNet;
    private int maxPerNE;
    private int maxNePerNet;
    private int avgNePerNet;
    private int maxPerRoot;

    private boolean isAdditional;

    public void addSupportedVersion(String version) {
        if (!supporteredVersions.contains(version)) {
            supporteredVersions.add(version);
        }
    }

    public void addDimension(String dimension) {
        if (!dimensions.contains(dimension)) {
            dimensions.add(dimension);
        }
    }

    public void addParentObject(PmbObject parent) {
        if (!parentObjects.contains(parent)) {
            parentObjects.add(parent);
        }
    }

    public void addChildObject(PmbObject child) {
        if (!childObjects.contains(child)) {
            childObjects.add(child);
        }
    }

    @Override
    public String toString() {
        return "PmbObject{" +
                "name='" + name + '\'' +
                ", supporteredVersions=" + supporteredVersions +
                ", childObjects=" + childObjects +
                '}';
    }

    public String getOriginalDn() {
        return originalDn;
    }

    public void setOriginalDn(String originalDn) {
        this.originalDn = originalDn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isMeasuredObject() {
        return isMeasuredObject;
    }

    public void setMeasuredObject(boolean measuredObject) {
        isMeasuredObject = measuredObject;
    }

    public List<String> getDimensions() {
        return dimensions;
    }

    public void setDimensions(List<String> dimensions) {
        this.dimensions = dimensions;
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

    public List<String> getSupporteredVersions() {
        return supporteredVersions;
    }

    public List<String> getSupportedVersions() {
        return supporteredVersions;
    }

    public String getDn() {
        return dn;
    }

    public void setDn(String dn) {
        this.dn = dn;
    }

    public void setSupporteredVersions(List<String> supporteredVersions) {
        this.supporteredVersions = supporteredVersions;
    }

    public List<PmbObject> getChildObjects() {
        return childObjects;
    }

    public void setChildObjects(List<PmbObject> childObjects) {
        this.childObjects = childObjects;
    }

    public List<PmbObject> getParentObjects() {
        return parentObjects;
    }

    public void setParentObjects(List<PmbObject> parentObjects) {
        this.parentObjects = parentObjects;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getAvg() {
        return avg;
    }

    public void setAvg(int avg) {
        this.avg = avg;
    }

    public int getAvgPerNet() {
        return avgPerNet;
    }

    public void setAvgPerNet(int avgPerNet) {
        this.avgPerNet = avgPerNet;
    }

    public int getMaxPerNet() {
        return maxPerNet;
    }

    public void setMaxPerNet(int maxPerNet) {
        this.maxPerNet = maxPerNet;
    }

    public int getMaxPerNE() {
        return maxPerNE;
    }

    public void setMaxPerNE(int maxPerNE) {
        this.maxPerNE = maxPerNE;
    }

    public int getMaxNePerNet() {
        return maxNePerNet;
    }

    public void setMaxNePerNet(int maxNePerNet) {
        this.maxNePerNet = maxNePerNet;
    }

    public int getAvgNePerNet() {
        return avgNePerNet;
    }

    public void setAvgNePerNet(int avgNePerNet) {
        this.avgNePerNet = avgNePerNet;
    }


    public int getMaxPerRoot() {
        return maxPerRoot;
    }

    public void setMaxPerRoot(int maxPerRoot) {
        this.maxPerRoot = maxPerRoot;
    }

    public boolean isAdditional() {
        return isAdditional;
    }

    public void setAdditional(boolean additional) {
        isAdditional = additional;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PmbObject pmbObject = (PmbObject) o;

        return name.equals(pmbObject.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public int compareTo(PmbObject o) {
        return this.getNameInOmes().compareTo(o.getName());
    }
}
