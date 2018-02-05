package com.longyg.frontend.model.ars.pm;

import com.longyg.backend.adaptation.pm.Counter;
import com.longyg.backend.adaptation.pm.MeasuredTarget;
import com.longyg.backend.adaptation.pm.Measurement;
import com.longyg.backend.adaptation.topology.PmbObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeasurementInfo {
    private int row;
    private Measurement measurement;
    private String adaptationId;
    private String name;
    private String nameInOmes;
    private List<String> supportedVersions = new ArrayList<>();

    private Map<String, List<Counter>> allReleaseCounters = new HashMap<>();

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public Measurement getMeasurement() {
        return measurement;
    }

    public void setMeasurement(Measurement measurement) {
        this.measurement = measurement;
    }

    public String getAdaptationId() {
        return adaptationId;
    }

    public void setAdaptationId(String adaptationId) {
        this.adaptationId = adaptationId;
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

    public List<String> getSupportedVersions() {
        return supportedVersions;
    }

    public Map<String, List<Counter>> getAllReleaseCounters() {
        return allReleaseCounters;
    }

    public PmbObject getMeasuredPmbObject() {

        return null;
    }

    public String getMeasuredObject() {
        for (MeasuredTarget target : measurement.getMeasuredTargets()) {
            if (target.getDimension().equals("network_element")) {
                // multiple hierarchy is not handled
                return target.getHierarchies().get(0);
            }
        }
        return null;
    }

    public List<String> getDimensions() {
        List<String> dimensions = new ArrayList<>();
        for (MeasuredTarget target : measurement.getMeasuredTargets()) {
            if (!dimensions.contains(target.getDimension())) {
                dimensions.add(target.getDimension());
            }
        }
        return dimensions;
    }

    public void addSupportedVersion(String version) {
        if (!supportedVersions.contains(version)) {
            supportedVersions.add(version);
        }
    }

    public void addCounters(String version, List<Counter> counters) {
        if (!allReleaseCounters.containsKey(version)) {
            allReleaseCounters.put(version, counters);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MeasurementInfo that = (MeasurementInfo) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
