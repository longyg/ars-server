package com.longyg.frontend.model.ars.counter;

import com.longyg.frontend.Utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;

public class ArsCounter implements Comparable<ArsCounter> {
    private String name;
    private boolean isSupported;
    private List<String> supportedPreviousVersions = new ArrayList<>();
    private String supportedOtherReleases;
    private String aggRule;
    private String presentation;
    private String unit;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSupported() {
        return isSupported;
    }

    public void setSupported(boolean supported) {
        isSupported = supported;
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

    public String getAggRule() {
        return aggRule;
    }

    public void setAggRule(String aggRule) {
        this.aggRule = aggRule;
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

    public void addSupportedPreviousVersion(String version) {
        if (!supportedPreviousVersions.contains(version)) {
            supportedPreviousVersions.add(version);
        }
    }

    @Override
    public int compareTo(ArsCounter o) {
        return this.name.compareTo(o.getName());
    }
}
