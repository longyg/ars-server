package com.longyg.backend.adaptation.pm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ylong on 2/14/2017.
 */
public class Measurement implements Comparable<Measurement> {
    private String name;
    private String nameInOmes;
    private String presentation;
    private String description;
    private String interval;

    private List<MeasuredTarget> measuredTargets = new ArrayList<>();
    private List<Counter> counters = new ArrayList<>();

    public void addCounter(Counter counter) {
        if (!counters.contains(counter)) {
            counters.add(counter);
        }
    }

    public void addMeasuredTarget(MeasuredTarget measuredTarget) {
        measuredTargets.add(measuredTarget);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Counter> getCounters() {
        return counters;
    }

    public void setCounters(List<Counter> counters) {
        this.counters = counters;
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

    public String getPresentation() {
        return presentation;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }

    public List<MeasuredTarget> getMeasuredTargets() {
        return measuredTargets;
    }

    public void setMeasuredTargets(List<MeasuredTarget> measuredTargets) {
        this.measuredTargets = measuredTargets;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Measurement that = (Measurement) o;

        if (!name.equals(that.name)) return false;
        return nameInOmes.equals(that.nameInOmes);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + nameInOmes.hashCode();
        return result;
    }

    public int compareTo(Measurement o) {
        return this.getName().compareTo(o.getName());
    }
}
