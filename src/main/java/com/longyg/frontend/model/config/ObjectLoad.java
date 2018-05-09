package com.longyg.frontend.model.config;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "object_loads")
public class ObjectLoad {
    @Id
    private String id;
    private String objectClass;
    private int max;
    private int avg;
    private String relatedObjectClass;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObjectClass() {
        return objectClass;
    }

    public void setObjectClass(String objectClass) {
        this.objectClass = objectClass;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getAvg() {
        return avg;
    }

    public void setAvg(int avg) {
        this.avg = avg;
    }

    public String getRelatedObjectClass() {
        return relatedObjectClass;
    }

    public void setRelatedObjectClass(String relatedObjectClass) {
        this.relatedObjectClass = relatedObjectClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObjectLoad that = (ObjectLoad) o;

        if (objectClass != null ? !objectClass.equals(that.objectClass) : that.objectClass != null) return false;
        return relatedObjectClass != null ? relatedObjectClass.equals(that.relatedObjectClass) : that.relatedObjectClass == null;
    }

    @Override
    public int hashCode() {
        int result = objectClass != null ? objectClass.hashCode() : 0;
        result = 31 * result + (relatedObjectClass != null ? relatedObjectClass.hashCode() : 0);
        return result;
    }
}
