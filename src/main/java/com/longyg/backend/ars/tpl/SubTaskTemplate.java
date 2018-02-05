package com.longyg.backend.ars.tpl;

public class SubTaskTemplate {
    private String id;
    private int row;
    private boolean selected;
    private String name;
    private VariableTemplate description;
    private VariableTemplate rationale;
    private VariableTemplate issue;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public VariableTemplate getDescription() {
        return description;
    }

    public void setDescription(VariableTemplate description) {
        this.description = description;
    }

    public VariableTemplate getRationale() {
        return rationale;
    }

    public void setRationale(VariableTemplate rationale) {
        this.rationale = rationale;
    }

    public VariableTemplate getIssue() {
        return issue;
    }

    public void setIssue(VariableTemplate issue) {
        this.issue = issue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubTaskTemplate that = (SubTaskTemplate) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
