package com.longyg.frontend.model.ne;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "NeInterface")
public class NeInterface {
    @Id
    private String id;
    private String neType;
    private String neVersion;
    private List<String> interfaces = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNeType() {
        return neType;
    }

    public void setNeType(String neType) {
        this.neType = neType;
    }

    public String getNeVersion() {
        return neVersion;
    }

    public void setNeVersion(String neVersion) {
        this.neVersion = neVersion;
    }

    public List<String> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(List<String> interfaces) {
        this.interfaces = interfaces;
    }

    public void addInterface(String ifId) {
        if (!interfaces.contains(ifId)) {
            interfaces.add(ifId);
        }
    }

    public void removeInterface(String ifId) {
        if (interfaces.contains(ifId)) {
            interfaces.remove(ifId);
        }
    }
}
