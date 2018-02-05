package com.longyg.backend.ars.tpl.definition.userstory;

import java.util.ArrayList;
import java.util.List;

public class Basic {
    private Title title;
    private List<Info> infoList = new ArrayList<Info>();

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public List<Info> getInfoList() {
        return infoList;
    }

    public void setInfoList(List<Info> infoList) {
        this.infoList = infoList;
    }

    public void add(Info info) {
        infoList.add(info);
    }

    public Info getInfoByName(String name) {
        for (Info info : infoList) {
            if (info.getName().equals(name)) {
                return info;
            }
        }
        return null;
    }
}
