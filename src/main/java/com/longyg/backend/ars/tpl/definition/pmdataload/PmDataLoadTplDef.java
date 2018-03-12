package com.longyg.backend.ars.tpl.definition.pmdataload;

public class PmDataLoadTplDef {
    private int sheet;
    private String name;
    private int adapIdRow;
    private int titleRow;
    private int dataRow;
    private int cellColorRow;
    private int statisticRow;
    private int pmfileRow;

    public int getSheet() {
        return sheet;
    }

    public void setSheet(int sheet) {
        this.sheet = sheet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTitleRow() {
        return titleRow;
    }

    public int getAdapIdRow() {
        return adapIdRow;
    }

    public void setAdapIdRow(int adapIdRow) {
        this.adapIdRow = adapIdRow;
    }

    public void setTitleRow(int titleRow) {
        this.titleRow = titleRow;
    }

    public int getDataRow() {
        return dataRow;
    }

    public void setDataRow(int dataRow) {
        this.dataRow = dataRow;
    }

    public int getCellColorRow() {
        return cellColorRow;
    }

    public void setCellColorRow(int cellColorRow) {
        this.cellColorRow = cellColorRow;
    }

    public int getStatisticRow() {
        return statisticRow;
    }

    public void setStatisticRow(int statisticRow) {
        this.statisticRow = statisticRow;
    }

    public int getPmfileRow() {
        return pmfileRow;
    }

    public void setPmfileRow(int pmfileRow) {
        this.pmfileRow = pmfileRow;
    }
}
