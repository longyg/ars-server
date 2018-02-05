package com.longyg.backend.ars.tpl;

import java.util.ArrayList;
import java.util.List;

public class USExcelTemplate {
    private String name;
    private BasicTemplate basicTemplate;
    private List<UserStoryTemplate> userStoryTemplateList = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BasicTemplate getBasicTemplate() {
        return basicTemplate;
    }

    public void setBasicTemplate(BasicTemplate basicTemplate) {
        this.basicTemplate = basicTemplate;
    }

    public List<UserStoryTemplate> getUserStoryTemplateList() {
        return userStoryTemplateList;
    }

    public void setUserStoryTemplateList(List<UserStoryTemplate> userStoryTemplateList) {
        this.userStoryTemplateList = userStoryTemplateList;
    }
}
