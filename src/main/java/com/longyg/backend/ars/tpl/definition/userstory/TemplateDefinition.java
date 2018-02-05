package com.longyg.backend.ars.tpl.definition.userstory;

import java.util.ArrayList;
import java.util.List;

public class TemplateDefinition {
    private int sheet;
    private String name;
    private Basic basic;
    private List<US> usList = new ArrayList<US>();

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

    public Basic getBasic() {
        return basic;
    }

    public void setBasic(Basic basic) {
        this.basic = basic;
    }

    public List<US> getUsList() {
        return usList;
    }

    public void setUsList(List<US> usList) {
        this.usList = usList;
    }

    public void addUs(US us) {
        usList.add(us);
    }

    public US getUsByName(String name) {
        for (US us : usList) {
            if (us.getName().equals(name)) {
                return us;
            }
        }
        return null;
    }
}

