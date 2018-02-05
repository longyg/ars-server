package com.longyg.frontend.model.ars.pm;

import com.longyg.frontend.Utils.CommonUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ArsMeasurement implements Comparable<ArsMeasurement> {
    private String name;
    private String nameInOmes;
    private String measuredObject;
    private boolean isSupported;
    private List<String> supportedPreviousVersions = new ArrayList<>();
    private String supportedOtherReleases;
    private List<String> dimensions = new ArrayList<>();
    private String dimension;
    private long avgPerNet;
    private long maxPerNet;
    private long maxPerNe;
    private long counterNumber;
    private long counterNumberOfLastVersion;
    private long delta;
    private String aggObject;
    private String timeAgg = "15min, 30min, Hour,Day,Week,Month";
    private String bh = "";
    private long active = 1;
    private long defaultInterval = 15;
    private long minimalInterval = 1;
    private long storageDays = 14;
    private long bytesPerCounter = 4;
    private long mphPerNE;
    private long cphPerNE;
    private long chaPerNE;
    private long cdaPerNe;
    private long maxMph;
    private long maxCph;
    private String measGroup;
    private long dbRrPerNe;
    private long dbRcPerNe;
    private long msPerNe;
    private long dbMaxRows;
    private long dbMaxCtrs;
    private long maxMs;
    private long totalBytesPerInterval;
    private BigDecimal totalSizePerHour;
    private BigDecimal tableSizePerDay;

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

    public String getMeasuredObject() {
        return measuredObject;
    }

    public void setMeasuredObject(String measuredObject) {
        this.measuredObject = measuredObject;
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

    public List<String> getDimensions() {
        return dimensions;
    }

    public void setDimensions(List<String> dimensions) {
        this.dimensions = dimensions;
    }

    public String getDimension() {
        if (null == dimensions || dimensions.size() < 1) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String version : dimensions) {
            sb.append(version).append(", ");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.deleteCharAt(sb.length() - 1);
        this.dimension = sb.toString();
        return dimension;
    }

    public void setSupportedOtherReleases(String supportedOtherReleases) {
        this.supportedOtherReleases = supportedOtherReleases;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public long getAvgPerNet() {
        return avgPerNet;
    }

    public void setAvgPerNet(long avgPerNet) {
        this.avgPerNet = avgPerNet;
    }

    public long getMaxPerNet() {
        return maxPerNet;
    }

    public void setMaxPerNet(long maxPerNet) {
        this.maxPerNet = maxPerNet;
    }

    public long getMaxPerNe() {
        return maxPerNe;
    }

    public void setMaxPerNe(long maxPerNe) {
        this.maxPerNe = maxPerNe;
    }

    public long getCounterNumber() {
        return counterNumber;
    }

    public void setCounterNumber(long counterNumber) {
        this.counterNumber = counterNumber;
    }

    public long getCounterNumberOfLastVersion() {
        return counterNumberOfLastVersion;
    }

    public void setCounterNumberOfLastVersion(long counterNumberOfLastVersion) {
        this.counterNumberOfLastVersion = counterNumberOfLastVersion;
    }

    public long getDelta() {
        return delta;
    }

    public void setDelta(long delta) {
        this.delta = delta;
    }

    public String getAggObject() {
        return aggObject;
    }

    public void setAggObject(String aggObject) {
        this.aggObject = aggObject;
    }

    public String getTimeAgg() {
        return timeAgg;
    }

    public void setTimeAgg(String timeAgg) {
        this.timeAgg = timeAgg;
    }

    public String getBh() {
        return bh;
    }

    public void setBh(String bh) {
        this.bh = bh;
    }

    public long getActive() {
        return active;
    }

    public void setActive(long active) {
        this.active = active;
    }

    public long getDefaultInterval() {
        return defaultInterval;
    }

    public void setDefaultInterval(long defaultInterval) {
        this.defaultInterval = defaultInterval;
    }

    public long getMinimalInterval() {
        return minimalInterval;
    }

    public void setMinimalInterval(long minimalInterval) {
        this.minimalInterval = minimalInterval;
    }

    public long getStorageDays() {
        return storageDays;
    }

    public void setStorageDays(long storageDays) {
        this.storageDays = storageDays;
    }

    public long getBytesPerCounter() {
        return bytesPerCounter;
    }

    public void setBytesPerCounter(long bytesPerCounter) {
        this.bytesPerCounter = bytesPerCounter;
    }

    public long getMphPerNE() {
        return mphPerNE;
    }

    public void setMphPerNE(long mphPerNE) {
        this.mphPerNE = mphPerNE;
    }

    public long getCphPerNE() {
        return cphPerNE;
    }

    public void setCphPerNE(long cphPerNE) {
        this.cphPerNE = cphPerNE;
    }

    public long getChaPerNE() {
        return chaPerNE;
    }

    public void setChaPerNE(long chaPerNE) {
        this.chaPerNE = chaPerNE;
    }

    public long getCdaPerNe() {
        return cdaPerNe;
    }

    public void setCdaPerNe(long cdaPerNe) {
        this.cdaPerNe = cdaPerNe;
    }

    public long getMaxMph() {
        return maxMph;
    }

    public void setMaxMph(long maxMph) {
        this.maxMph = maxMph;
    }

    public long getMaxCph() {
        return maxCph;
    }

    public void setMaxCph(long maxCph) {
        this.maxCph = maxCph;
    }

    public String getMeasGroup() {
        return measGroup;
    }

    public void setMeasGroup(String measGroup) {
        this.measGroup = measGroup;
    }

    public long getDbRrPerNe() {
        return dbRrPerNe;
    }

    public void setDbRrPerNe(long dbRrPerNe) {
        this.dbRrPerNe = dbRrPerNe;
    }

    public long getDbRcPerNe() {
        return dbRcPerNe;
    }

    public void setDbRcPerNe(long dbRcPerNe) {
        this.dbRcPerNe = dbRcPerNe;
    }

    public long getMsPerNe() {
        return msPerNe;
    }

    public void setMsPerNe(long msPerNe) {
        this.msPerNe = msPerNe;
    }

    public long getDbMaxRows() {
        return dbMaxRows;
    }

    public void setDbMaxRows(long dbMaxRows) {
        this.dbMaxRows = dbMaxRows;
    }

    public long getDbMaxCtrs() {
        return dbMaxCtrs;
    }

    public void setDbMaxCtrs(long dbMaxCtrs) {
        this.dbMaxCtrs = dbMaxCtrs;
    }

    public long getMaxMs() {
        return maxMs;
    }

    public void setMaxMs(long maxMs) {
        this.maxMs = maxMs;
    }

    public long getTotalBytesPerInterval() {
        return totalBytesPerInterval;
    }

    public void setTotalBytesPerInterval(long totalBytesPerInterval) {
        this.totalBytesPerInterval = totalBytesPerInterval;
    }

    public BigDecimal getTotalSizePerHour() {
        return totalSizePerHour;
    }

    public void setTotalSizePerHour(BigDecimal totalSizePerHour) {
        this.totalSizePerHour = totalSizePerHour;
    }

    public BigDecimal getTableSizePerDay() {
        return tableSizePerDay;
    }

    public void setTableSizePerDay(BigDecimal tableSizePerDay) {
        this.tableSizePerDay = tableSizePerDay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArsMeasurement that = (ArsMeasurement) o;

        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public int compareTo(ArsMeasurement o) {
        return this.getName().compareTo(o.getName());
    }
}
