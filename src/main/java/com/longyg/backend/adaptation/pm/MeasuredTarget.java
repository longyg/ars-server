package com.longyg.backend.adaptation.pm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ylong on 2/14/2017.
 */
public class MeasuredTarget {
    private String dimension;
    private List<String> hierarchies = new ArrayList<String>();

    public void addHierarchy(String hierarchy) {
        if (!hierarchies.contains(hierarchy)) {
            hierarchies.add(hierarchy);
        }
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public List<String> getHierarchies() {
        return hierarchies;
    }

    public void setHierarchies(List<String> hierarchies) {
        this.hierarchies = hierarchies;
    }
}
