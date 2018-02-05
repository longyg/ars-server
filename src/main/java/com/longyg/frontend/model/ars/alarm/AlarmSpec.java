package com.longyg.frontend.model.ars.alarm;

import com.longyg.frontend.model.ars.counter.CounterMeas;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document(collection = "alarm")
public class AlarmSpec {
    @Id
    private String id;
    private String neType;
    private String neVersion;

    // key: adaptation id
    // value: alarm list
    private Map<String, List<ArsAlarm>> alarmMap = new HashMap<>();

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

    public Map<String, List<ArsAlarm>> getAlarmMap() {
        return alarmMap;
    }

    public void setAlarmMap(Map<String, List<ArsAlarm>> alarmMap) {
        this.alarmMap = alarmMap;
    }

    public void addAlarm(String adaptationId, ArsAlarm alarm) {
        String adapId = adaptationId.replaceAll("\\.", "_");
        if (alarmMap.containsKey(adapId)) {
            List<ArsAlarm> alarmList = alarmMap.get(adapId);
            if (!alarmList.contains(alarm)) {
                alarmList.add(alarm);
            }
        } else {
            List<ArsAlarm> alarmList = new ArrayList<>();
            alarmList.add(alarm);
            alarmMap.put(adapId, alarmList);
        }
    }
}
