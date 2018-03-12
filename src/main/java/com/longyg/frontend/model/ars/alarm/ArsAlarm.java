package com.longyg.frontend.model.ars.alarm;

import com.longyg.frontend.Utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;

public class ArsAlarm implements Comparable<ArsAlarm> {
    private String specificProblem;
    private boolean isSupported;
    private List<String> supportedPreviousVersions = new ArrayList<>();
    private String supportedOtherReleases;
    private String alarmText;
    private String lastAlarmText;
    private boolean isAlarmTextChanged;
    private String probableCause;
    private String lastProbableCause;
    private boolean isProbableCauseChanged;
    private String perceivedSeverityInfo;
    private String lastPerceivedSeverityInfo;
    private boolean isSeverityChanged;
    private String alarmType;
    private String lastAlarmType;
    private boolean isAlarmTypeChanged;
    private String meaning;
    private String lastMeaning;
    private boolean isMeaningChanged;
    private String instructions;
    private String lastInstructions;
    private boolean isInstructionsChanged;
    private String cancelling;
    private String lastCancelling;
    private boolean isCancellingChanged;

    public String getSpecificProblem() {
        return specificProblem;
    }

    public void setSpecificProblem(String specificProblem) {
        this.specificProblem = specificProblem;
    }

    public boolean isSupported() {
        return isSupported;
    }

    public void setSupported(boolean supported) {
        isSupported = supported;
    }

    public void addSupportedPreviousVersion(String version) {
        if (!supportedPreviousVersions.contains(version)) {
            supportedPreviousVersions.add(version);
        }
    }

    public List<String> getSupportedPreviousVersions() {
        return supportedPreviousVersions;
    }

    public void setSupportedPreviousVersions(List<String> supportedPreviousVersions) {
        this.supportedPreviousVersions = supportedPreviousVersions;
    }

    public String getSupportedOtherReleases() {
        this.supportedOtherReleases = CommonUtil.listToString(supportedPreviousVersions);
        return supportedOtherReleases;
    }

    public void setSupportedOtherReleases(String supportedOtherReleases) {
        this.supportedOtherReleases = supportedOtherReleases;
    }

    public String getLastAlarmText() {
        return lastAlarmText;
    }

    public void setLastAlarmText(String lastAlarmText) {
        this.lastAlarmText = lastAlarmText;
    }

    public String getLastProbableCause() {
        return lastProbableCause;
    }

    public void setLastProbableCause(String lastProbableCause) {
        this.lastProbableCause = lastProbableCause;
    }

    public String getLastPerceivedSeverityInfo() {
        return lastPerceivedSeverityInfo;
    }

    public void setLastPerceivedSeverityInfo(String lastPerceivedSeverityInfo) {
        this.lastPerceivedSeverityInfo = lastPerceivedSeverityInfo;
    }

    public String getLastAlarmType() {
        return lastAlarmType;
    }

    public void setLastAlarmType(String lastAlarmType) {
        this.lastAlarmType = lastAlarmType;
    }

    public String getLastMeaning() {
        return lastMeaning;
    }

    public void setLastMeaning(String lastMeaning) {
        this.lastMeaning = lastMeaning;
    }

    public String getLastInstructions() {
        return lastInstructions;
    }

    public void setLastInstructions(String lastInstructions) {
        this.lastInstructions = lastInstructions;
    }

    public String getLastCancelling() {
        return lastCancelling;
    }

    public void setLastCancelling(String lastCancelling) {
        this.lastCancelling = lastCancelling;
    }

    public String getAlarmText() {
        return alarmText;
    }

    public void setAlarmText(String alarmText) {
        this.alarmText = alarmText;
    }

    public boolean isAlarmTextChanged() {
        return isAlarmTextChanged;
    }

    public void setAlarmTextChanged(boolean alarmTextChanged) {
        isAlarmTextChanged = alarmTextChanged;
    }

    public String getProbableCause() {
        return probableCause;
    }

    public void setProbableCause(String probableCause) {
        this.probableCause = probableCause;
    }

    public boolean isProbableCauseChanged() {
        return isProbableCauseChanged;
    }

    public void setProbableCauseChanged(boolean probableCauseChanged) {
        isProbableCauseChanged = probableCauseChanged;
    }

    public String getPerceivedSeverityInfo() {
        return perceivedSeverityInfo;
    }

    public void setPerceivedSeverityInfo(String perceivedSeverityInfo) {
        this.perceivedSeverityInfo = perceivedSeverityInfo;
    }

    public boolean isSeverityChanged() {
        return isSeverityChanged;
    }

    public void setSeverityChanged(boolean severityChanged) {
        isSeverityChanged = severityChanged;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public boolean isAlarmTypeChanged() {
        return isAlarmTypeChanged;
    }

    public void setAlarmTypeChanged(boolean alarmTypeChanged) {
        isAlarmTypeChanged = alarmTypeChanged;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public boolean isMeaningChanged() {
        return isMeaningChanged;
    }

    public void setMeaningChanged(boolean meaningChanged) {
        isMeaningChanged = meaningChanged;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public boolean isInstructionsChanged() {
        return isInstructionsChanged;
    }

    public void setInstructionsChanged(boolean instructionsChanged) {
        isInstructionsChanged = instructionsChanged;
    }

    public String getCancelling() {
        return cancelling;
    }

    public void setCancelling(String cancelling) {
        this.cancelling = cancelling;
    }

    public boolean isCancellingChanged() {
        return isCancellingChanged;
    }

    public void setCancellingChanged(boolean cancellingChanged) {
        isCancellingChanged = cancellingChanged;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArsAlarm arsAlarm = (ArsAlarm) o;

        return specificProblem != null ? specificProblem.equals(arsAlarm.specificProblem) : arsAlarm.specificProblem == null;
    }

    @Override
    public int hashCode() {
        return specificProblem != null ? specificProblem.hashCode() : 0;
    }

    @Override
    public int compareTo(ArsAlarm o) {
        return Integer.valueOf(this.getSpecificProblem()).compareTo(Integer.valueOf(o.getSpecificProblem()));
    }
}
