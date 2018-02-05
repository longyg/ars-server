package com.longyg.backend.adaptation.pm;

/**
 * Created by ylong on 2/14/2017.
 */
public class Counter implements Comparable<Counter> {
    private String name;
    private String omesName;
    private String presentation;
    private String unit;
    private String description;
    private String aggRule;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOmesName() {
        return omesName;
    }

    public void setOmesName(String omesName) {
        this.omesName = omesName;
    }

    public String getPresentation() {
        return presentation;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAggRule() {
        return aggRule;
    }

    public void setAggRule(String aggRule) {
        this.aggRule = aggRule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Counter counter = (Counter) o;

        return name.equals(counter.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public int compareTo(Counter o) {
        return this.getName().compareTo(o.getName());
    }
}