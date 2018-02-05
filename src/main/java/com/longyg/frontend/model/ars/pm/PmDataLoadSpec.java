package com.longyg.frontend.model.ars.pm;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document(collection = "pm")
public class PmDataLoadSpec {
    @Id
    private String id;
    private String neType;
    private String neVersion;

    private Map<String, List<ArsMeasurement>> measurementMap = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNeType() {
        return neType;
    }

    public void setNeType(String neType) {
        this.neType = neType;
    }

    public String getNeVersion() {
        return neVersion;
    }

    public void setNeVersion(String neVersion) {
        this.neVersion = neVersion;
    }

    public void addMeasurement(String adaptationId, ArsMeasurement measurement) {
        String adapId = adaptationId.replaceAll("\\.", "_");
        if (measurementMap.containsKey(adapId)) {
            List<ArsMeasurement> measList = measurementMap.get(adapId);
            if (!measList.contains(measurement)) {
                measList.add(measurement);
            }
        } else {
            List<ArsMeasurement> measList = new ArrayList<>();
            measList.add(measurement);
            measurementMap.put(adapId, measList);
        }
    }

    public Map<String, List<ArsMeasurement>> getMeasurementMap() {
        return measurementMap;
    }

    public void setMeasurementMap(Map<String, List<ArsMeasurement>> measurementMap) {
        this.measurementMap = measurementMap;
    }
}
