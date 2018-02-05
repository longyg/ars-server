package com.longyg.backend.adaptation.fm;

/**
 * Created by ylong on 2/17/2017.
 */
public class Alarm implements Comparable<Alarm> {
    private String alarmNumber;
    private String alarmText;
    private String probableCause;
    private String perceivedSeverityInfo;
    private String alarmType;
    private String meaning;
    private String instructions;
    private String cancelling;

    public String getAlarmNumber() {
        return alarmNumber;
    }

    public void setAlarmNumber(String alarmNumber) {
        this.alarmNumber = alarmNumber;
    }

    public String getAlarmText() {
        return alarmText;
    }

    public void setAlarmText(String alarmText) {
        this.alarmText = alarmText;
    }

    public String getProbableCause() {
        return probableCause;
    }

    public void setProbableCause(String probableCause) {
        this.probableCause = probableCause;
    }

    public String getPerceivedSeverityInfo() {
        return perceivedSeverityInfo;
    }

    public void setPerceivedSeverityInfo(String perceivedSeverityInfo) {
        this.perceivedSeverityInfo = perceivedSeverityInfo;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getCancelling() {
        return cancelling;
    }

    public void setCancelling(String cancelling) {
        this.cancelling = cancelling;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Alarm alarm = (Alarm) o;

        return alarmNumber.equals(alarm.alarmNumber);

    }

    @Override
    public int hashCode() {
        return alarmNumber.hashCode();
    }

    public int compareTo(Alarm o) {
        Integer io = Integer.valueOf(o.getAlarmNumber());
        Integer to = Integer.valueOf(this.getAlarmNumber());
        return to.compareTo(io);
    }
}
