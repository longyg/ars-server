package com.longyg.frontend.model.ars.om;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Document(collection = "om")
public class ObjectModelSpec {
    @Id
    private String id;

    private String neType;

    private String neVersion;

    private Map<String, List<ObjectClassInfo>> ociMap = new HashMap<>();

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

    public Map<String, List<ObjectClassInfo>> getOciMap() {
        return ociMap;
    }

    public void setOciMap(Map<String, List<ObjectClassInfo>> ociMap) {
        this.ociMap = ociMap;
    }

    public void addObjectClassInfo(String adaptationId, ObjectClassInfo oci) {
        String adapId = adaptationId.replaceAll("\\.", "_");
        if (ociMap.containsKey(adapId)) {
            List<ObjectClassInfo> ociSet = ociMap.get(adapId);
            if (!ociSet.contains(oci)) {
                ociSet.add(oci);
            }
        } else {
            List<ObjectClassInfo> ociSet = new ArrayList<>();
            ociSet.add(oci);
            ociMap.put(adapId, ociSet);
        }
    }

    public List<ObjectClassInfo> getOciList(String adaptationId) {
        String adapId = adaptationId.replaceAll("\\.", "_");
        if (ociMap.containsKey(adapId)) {
            return ociMap.get(adapId);
        } else {
            return new ArrayList<>();
        }
    }
}
