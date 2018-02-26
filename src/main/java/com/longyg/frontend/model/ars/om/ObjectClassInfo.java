package com.longyg.frontend.model.ars.om;

import com.longyg.backend.adaptation.topology.ClassType;

import java.util.ArrayList;
import java.util.List;

public class ObjectClassInfo implements Comparable {
    private int row;
    private int column;
    private String adaptationId;
    private String name;
    private String nameInOmes;
    private boolean isTransient;
    private String presentation;
    private boolean isAlarmingObject;
    private boolean isMeasuredObject;
    private boolean isCmObject;
    private boolean hasIcon;
    private boolean hasGuiLuanch;
    private String tgppObject;
    private String intVersion;
    private String intNasda; // agent or interface
    private ClassType classType;
    private int min;
    private int max;
    private int avg;
    private int avgPerNet;
    private int maxPerNet;
    private int maxPerNE;
    private int maxNePerNet;
    private int avgNePerNet;
    private int maxPerRoot;
    private boolean mocrNeeded;
    private String comment;
    private List<String> supporteredVersions = new ArrayList<>();
    private List<String> dimensions = new ArrayList<>();
    private String supportedReleases;
    private String dn;
    private String originalDn;

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
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

    public boolean isAlarmingObject() {
        return isAlarmingObject;
    }

    public void setAlarmingObject(boolean alarmingObject) {
        isAlarmingObject = alarmingObject;
    }

    public boolean isMeasuredObject() {
        return isMeasuredObject;
    }

    public void setMeasuredObject(boolean measuredObject) {
        isMeasuredObject = measuredObject;
    }

    public boolean isCmObject() {
        return isCmObject;
    }

    public void setCmObject(boolean cmObject) {
        isCmObject = cmObject;
    }

    public boolean isHasIcon() {
        return hasIcon;
    }

    public void setHasIcon(boolean hasIcon) {
        this.hasIcon = hasIcon;
    }

    public boolean isHasGuiLuanch() {
        return hasGuiLuanch;
    }

    public void setHasGuiLuanch(boolean hasGuiLuanch) {
        this.hasGuiLuanch = hasGuiLuanch;
    }

    public String getTgppObject() {
        return tgppObject;
    }

    public void setTgppObject(String tgppObject) {
        this.tgppObject = tgppObject;
    }

    public String getIntVersion() {
        return intVersion;
    }

    public void setIntVersion(String intVersion) {
        this.intVersion = intVersion;
    }

    public String getIntNasda() {
        return intNasda;
    }

    public void setIntNasda(String intNasda) {
        this.intNasda = intNasda;
    }

    public ClassType getClassType() {
        return classType;
    }

    public void setClassType(ClassType classType) {
        this.classType = classType;
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

    public boolean isMocrNeeded() {
        return mocrNeeded;
    }

    public void setMocrNeeded(boolean mocrNeeded) {
        this.mocrNeeded = mocrNeeded;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<String> getSupporteredVersions() {
        return supporteredVersions;
    }

    public String getDn() {
        return dn;
    }

    public void setDn(String dn) {
        this.dn = dn;
    }

    public String getOriginalDn() {
        return originalDn;
    }

    public void setOriginalDn(String originalDn) {
        this.originalDn = originalDn;
    }

    public void setSupporteredVersions(List<String> supporteredVersions) {
        this.supporteredVersions = supporteredVersions;
    }

    public String getSupportedReleases() {
        if (null == supporteredVersions || supporteredVersions.size() < 1) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String version : supporteredVersions) {
            sb.append(version).append(", ");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.deleteCharAt(sb.length() - 1);
        this.supportedReleases = sb.toString();
        return supportedReleases;
    }

    public List<String> getDimensions() {
        return dimensions;
    }

    public void setDimensions(List<String> dimensions) {
        this.dimensions = dimensions;
    }

    @Override
    public int compareTo(Object o) {
        return this.row - ((ObjectClassInfo) o).getRow();
    }
}
