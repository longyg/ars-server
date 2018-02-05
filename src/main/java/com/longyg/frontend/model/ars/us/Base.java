package com.longyg.frontend.model.ars.us;

import com.longyg.frontend.Utils.HtmlUtils;

public class Base {
    private String value;
    private int row;
    private int col;
    private String htmlValue;

    public Base() {
    }

    public Base(String value, int row, int col) {
        this.value = value;
        this.row = row;
        this.col = col;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public String getHtmlValue() {
        if (null != value) {
            return HtmlUtils.txtToHtml(value);
        }
        return null;
    }
}
