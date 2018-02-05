package com.longyg.backend.ars.tpl;

import java.util.ArrayList;
import java.util.List;

public class UserStoryTemplate extends VariableTemplate {
    private String name;
    private List<SubTaskTemplate> subTaskTemplateList = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SubTaskTemplate> getSubTaskTemplateList() {
        return subTaskTemplateList;
    }

    public void setSubTaskTemplateList(List<SubTaskTemplate> subTaskTemplateList) {
        this.subTaskTemplateList = subTaskTemplateList;
    }

    public void addSubTask(SubTaskTemplate subTaskTemplate) {
        if (!subTaskTemplateList.contains(subTaskTemplate)) {
            subTaskTemplateList.add(subTaskTemplate);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserStoryTemplate that = (UserStoryTemplate) o;

        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
