package com.longyg.frontend.model.ars.counter;

import com.longyg.frontend.model.ars.pm.ArsMeasurement;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document(collection = "counter")
public class CounterSpec {
    @Id
    private String id;
    private String neType;
    private String neVersion;

    // key: adaptation id
    // value: measurement list
    private Map<String, List<CounterMeas>> measurementMap = new HashMap<>();

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

    public Map<String, List<CounterMeas>> getMeasurementMap() {
        return measurementMap;
    }

    public void setMeasurementMap(Map<String, List<CounterMeas>> measurementMap) {
        this.measurementMap = measurementMap;
    }

    public void addMeasurement(String adaptationId, CounterMeas measurement) {
        String adapId = adaptationId.replaceAll("\\.", "_");
        if (measurementMap.containsKey(adapId)) {
            List<CounterMeas> measList = measurementMap.get(adapId);
            if (!measList.contains(measurement)) {
                measList.add(measurement);
            }
        } else {
            List<CounterMeas> measList = new ArrayList<>();
            measList.add(measurement);
            measurementMap.put(adapId, measList);
        }
    }
}
