package com.longyg.backend.ars.tpl.definition.alarms;

public class AlarmsTplDef {
    private int sheet;
    private String name;
    private int titleRow;
    private int dataRow;

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

    public void setTitleRow(int titleRow) {
        this.titleRow = titleRow;
    }

    public int getDataRow() {
        return dataRow;
    }

    public void setDataRow(int dataRow) {
        this.dataRow = dataRow;
    }
}
