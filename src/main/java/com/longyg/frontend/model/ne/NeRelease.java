package com.longyg.frontend.model.ne;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ne_releases")
public class NeRelease {
    @Id
    private String id;
    private String type;
    private String version;
    private String remarks;

    public NeRelease() {
    }

    public NeRelease(String type, String version) {
        this.type = type;
        this.version = version;
    }

    public NeRelease(String type, String version, String remarks) {
        this.type = type;
        this.version = version;
        this.remarks = remarks;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NeRelease neRelease = (NeRelease) o;

        if (!type.equals(neRelease.type)) return false;
        return version.equals(neRelease.version);
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + version.hashCode();
        return result;
    }
}
