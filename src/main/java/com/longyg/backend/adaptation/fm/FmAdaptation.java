package com.longyg.backend.adaptation.fm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ylong on 2/17/2017.
 */
public class FmAdaptation {
    private String adapId;
    private String adapRelease;
    private String presentation;

    private List<Alarm> alarms = new ArrayList<>();

    public void addAlarm(Alarm alarm) {
        if (!alarms.contains(alarm)) {
            alarms.add(alarm);
        }
    }

    public String getAdapId() {
        return adapId;
    }

    public void setAdapId(String adapId) {
        this.adapId = adapId;
    }

    public String getAdapRelease() {
        return adapRelease;
    }

    public void setAdapRelease(String adapRelease) {
        this.adapRelease = adapRelease;
    }

    public String getPresentation() {
        return presentation;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }

    public List<Alarm> getAlarms() {
        return alarms;
    }

    public void setAlarms(List<Alarm> alarms) {
        this.alarms = alarms;
    }
}
