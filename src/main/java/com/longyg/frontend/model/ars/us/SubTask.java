package com.longyg.frontend.model.ars.us;

public class SubTask {
    private int row;
    private Base select;
    private Base name;
    private Base description;
    private Base rationale;
    private Base issue;

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public Base getSelect() {
        return select;
    }

    public void setSelect(Base select) {
        this.select = select;
    }

    public Base getName() {
        return name;
    }

    public void setName(Base name) {
        this.name = name;
    }

    public Base getDescription() {
        return description;
    }

    public void setDescription(Base description) {
        this.description = description;
    }

    public Base getRationale() {
        return rationale;
    }

    public void setRationale(Base rationale) {
        this.rationale = rationale;
    }

    public Base getIssue() {
        return issue;
    }

    public void setIssue(Base issue) {
        this.issue = issue;
    }
}
