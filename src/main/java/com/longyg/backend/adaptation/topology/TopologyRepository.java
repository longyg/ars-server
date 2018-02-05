package com.longyg.backend.adaptation.topology;

import com.longyg.backend.adaptation.pm.*;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by ylong on 2/15/2017.
 */
public class TopologyRepository {
    private static final Logger LOG = Logger.getLogger(TopologyRepository.class);
    private static TopologyRepository INSTANCE;

    private List<PmbObject> allReleaseObjects = new ArrayList<>();

    private int maxLevel = 0;
    private List<PmbObject> rootObjects = new ArrayList<>();

    public static TopologyRepository getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new TopologyRepository();
        }
        return INSTANCE;
    }

    public void buildTopology() throws Exception {
        buildTopologyFromPmb();

        maxLevel = extractTotalLevel();
        addRootObjects();
        Collections.sort(rootObjects);
    }

    private int extractTotalLevel() {
        int currentLevel = 0;
        int maxLevel = currentLevel;
        for (PmbObject pmbObject : TopologyRepository.getInstance().getAllReleaseObjects()) {
            currentLevel = 1;
            if (pmbObject.getChildObjects().size() > 0) {
                currentLevel++;
            }
            for (PmbObject childObj : pmbObject.getChildObjects()) {
                int tmplevel = getLevel(childObj, currentLevel);
                if (tmplevel > maxLevel) {
                    maxLevel = tmplevel;
                }
            }
        }
        return maxLevel;
    }

    private void addRootObjects() {
        for (PmbObject pmbObject : TopologyRepository.getInstance().getAllReleaseObjects()) {
            if (pmbObject.getParentObjects().size() < 1) {
                rootObjects.add(pmbObject);
            }
        }
    }

    private int getLevel(PmbObject pmbObject, int currentLevel) {
        int curLevel = 0;
        int maxLevel = currentLevel;
        if (pmbObject.getChildObjects().size() > 0) {
            curLevel = currentLevel + 1;
        }
        for (PmbObject childObj : pmbObject.getChildObjects())
        {
            int childLevel = getLevel(childObj, curLevel);
            if (childLevel > maxLevel) {
                maxLevel = childLevel;
            }
        }

        return maxLevel;
    }

    private void buildTopologyFromPmb() throws Exception {
        Map<String, PmAdaptation> pmAdaptations = PmRepository.getPmAdaptations();

        for (Map.Entry<String, PmAdaptation> entry : pmAdaptations.entrySet()) {
            String version = entry.getKey();
            PmAdaptation pmAdaptation = entry.getValue();
            LOG.debug("version:" + version + ", pmAdaptation: " + pmAdaptation);

            List<ObjectClass> pmClasses = pmAdaptation.getObjectClasses();
            for (Measurement measurement : pmAdaptation.getMeasurements()) {
                for (MeasuredTarget measuredTarget : measurement.getMeasuredTargets()) {
                    String dimension = measuredTarget.getDimension();
                    for (String hierarchy : measuredTarget.getHierarchies()) {
                        hierarchy = format(hierarchy);
                        createPmObjectsFromHierarchy(hierarchy, null, version, dimension, pmClasses);
                    }
                }
            }
        }
    }

    private void addToList(PmbObject pmbObject) {
        if (!allReleaseObjects.contains(pmbObject)) {
            allReleaseObjects.add(pmbObject);
        }
    }

    private String getLastClass(String hierarchy) {
        String lastClass;
        if (hierarchy.lastIndexOf("/") == -1) {
            lastClass = hierarchy;
        } else {
            lastClass = hierarchy.substring(hierarchy.lastIndexOf("/") + 1, hierarchy.length());
        }
        return lastClass;
    }

    private void createPmObjectsFromHierarchy(String hierarchy, PmbObject parentObj, String version, String dimension, List<ObjectClass> pmClasses) throws Exception {
        if (null == hierarchy || "".equals(hierarchy)) {
            return;
        }
        String firstClass = getFirstClass(hierarchy);
        String childHierarchy = getChildHierarchy(hierarchy);

        PmbObject pmbObject = findOrCreatePmObject(firstClass, pmClasses, version, dimension);
        addToList(pmbObject);
        if (null != parentObj) {
            parentObj.addChildObject(pmbObject);
            pmbObject.addParentObject(parentObj);
        }
        if (null == childHierarchy || "".equals(childHierarchy)) {
            pmbObject.setMeasuredObject(true);
        }
        createPmObjectsFromHierarchy(childHierarchy, pmbObject, version, dimension, pmClasses);
    }

    private static String getFirstClass(String hierarchy) {
        hierarchy = format(hierarchy);
        String firstClass;
        if (hierarchy.indexOf("/") == -1) {
            firstClass = hierarchy;
        } else {
            firstClass = hierarchy.substring(0, hierarchy.indexOf("/"));
        }
        return firstClass;
    }

    private static String getChildHierarchy(String hierarchy) {
        hierarchy = format(hierarchy);
        String childHierarchy = null;
        if (hierarchy.indexOf("/") > -1) {
            childHierarchy = hierarchy.substring(hierarchy.indexOf("/"), hierarchy.length());
        }
        return childHierarchy;
    }

    private PmbObject findOrCreatePmObject(String clazz, List<ObjectClass> objectClasses, String version, String dimension) throws Exception {
        PmbObject pmbObject = findPmObjectByName(clazz);
        if (pmbObject == null) {
            pmbObject = new PmbObject();
            ObjectClass classDef = findPmClassDefinition(clazz, objectClasses);
            if (null == classDef) {
                LOG.error("Can't find PM class: " + clazz);
                throw new Exception("Can't find PM class: " + clazz + ". Please check your adaptation");
            }
            pmbObject.setName(classDef.getName());
            pmbObject.setNameInOmes(classDef.getNameInOmes());
            pmbObject.setPresentation(classDef.getPresentation());
            pmbObject.setTransient(classDef.isTransient());
        }
        pmbObject.addSupportedVersion(version);
        pmbObject.addDimension(dimension);
        return pmbObject;
    }



    private ObjectClass findPmClassDefinition(String clazz, List<ObjectClass> pmClasses) {
        for (ObjectClass objectClass : pmClasses) {
            if (clazz.equals(objectClass.getName())) {
                return objectClass;
            }
        }
        return null;
    }

    private PmbObject findPmObjectByName(String className) {
        for (PmbObject pmbObject : allReleaseObjects) {
            if (pmbObject.getName().equals(className)) {
                return pmbObject;
            }
        }
        return null;
    }

    private static String format(String hierarchy) {
        if (hierarchy.lastIndexOf("/") == hierarchy.length() - 1) {
            hierarchy = hierarchy.substring(0, hierarchy.lastIndexOf("/"));
        }
        if (hierarchy.indexOf("/") == 0) {
            hierarchy = hierarchy.substring(1, hierarchy.length());
        }
        return hierarchy;
    }


    public List<PmbObject> getAllReleaseObjects() {
        return allReleaseObjects;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public void setRootObjects(List<PmbObject> rootObjects) {
        this.rootObjects = rootObjects;
    }

    public List<PmbObject> getRootObjects() {
        return rootObjects;
    }

    public static void main(String[] args) throws Exception {
        String hierarchy = "s";
        System.out.println(getFirstClass(hierarchy));
        System.out.println(getChildHierarchy(hierarchy));
    }
}
