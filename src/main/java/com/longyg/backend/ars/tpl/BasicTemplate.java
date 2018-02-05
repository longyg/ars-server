package com.longyg.backend.ars.tpl;

import java.util.ArrayList;
import java.util.List;

public class BasicTemplate {
    private TitleTemplate titleTemplate;
    private List<AdapInfoTemplate> adapInfoTemplateList = new ArrayList<>();

    public TitleTemplate getTitleTemplate() {
        return titleTemplate;
    }

    public void setTitleTemplate(TitleTemplate titleTemplate) {
        this.titleTemplate = titleTemplate;
    }

    public List<AdapInfoTemplate> getAdapInfoTemplateList() {
        return adapInfoTemplateList;
    }

    public void setAdapInfoTemplateList(List<AdapInfoTemplate> adapInfoTemplateList) {
        this.adapInfoTemplateList = adapInfoTemplateList;
    }

    public void addAdapInfoTemplate(AdapInfoTemplate tpl) {
        if (!adapInfoTemplateList.contains(tpl)) {
            adapInfoTemplateList.add(tpl);
        }
    }
}
