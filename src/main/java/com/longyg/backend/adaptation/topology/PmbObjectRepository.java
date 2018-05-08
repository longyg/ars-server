package com.longyg.backend.adaptation.topology;

import com.longyg.backend.adaptation.main.AdaptationRepository;
import com.longyg.backend.adaptation.pm.*;
import com.longyg.frontend.model.ars.ArsConfig;
import com.longyg.frontend.model.config.ParentObject;
import com.longyg.frontend.model.config.ObjectLoad;
import com.longyg.frontend.model.ne.ParentHierarchy;
import com.longyg.frontend.model.ne.ReleaseConfig;

import java.util.*;
import java.util.logging.Logger;

public class PmbObjectRepository {
    private static final Logger LOG = Logger.getLogger(PmbObjectRepository.class.getName());

    // key: adaptation Id
    // value: all releases' object of specific adaptation id
    private Map<String, List<PmbObject>> allReleaseObjects = new HashMap<>();

    private AdaptationRepository adaptationRepository;

    private ReleaseConfig config;

    private List<ParentObject> globalObjects;

    private List<ObjectLoad> objectLoads;

    public Map<String, List<PmbObject>> getAllReleaseObjects() {
        return allReleaseObjects;
    }

    public void setAllReleaseObjects(Map<String, List<PmbObject>> allReleaseObjects) {
        this.allReleaseObjects = allReleaseObjects;
    }

    public PmbObjectRepository(AdaptationRepository adaptationRepository, ReleaseConfig config, List<ParentObject> globalObjects, List<ObjectLoad> objectLoads) {
        this.adaptationRepository = adaptationRepository;
        this.config = config;
        this.globalObjects = globalObjects;
        this.objectLoads = objectLoads;
    }

    public void init() throws Exception {
        createPmbObjectFromPmb();

        addParentObject();

        setObjectsDN();

        setObjectLoad();
    }

    private void setObjectsDN() {
        Map<String, List<PmbObject>> rootObjectMap = getRootObjects();
        for (List<PmbObject> rootList : rootObjectMap.values()) {
            for (PmbObject root : rootList) {
                setDn(root);
                setChildObjectsDN(root);
            }
        }
    }

    private void setChildObjectsDN(PmbObject obj) {
        for (PmbObject child : obj.getChildObjects()) {
            setDn(child);
            setChildObjectsDN(child);
        }
    }

    private void setDn(PmbObject obj) {
        if (null == obj.getParentObjects() || obj.getParentObjects().size() < 1) {
            obj.setDn(obj.getName());
        } else {
            // multiple parent is not handled
            PmbObject parent = obj.getParentObjects().get(0);
            obj.setDn(parent.getDn() + "/" + obj.getName());
        }
    }

    private void setObjectLoad() throws Exception {
        Map<String, List<PmbObject>> rootObjectMap = getRootObjects();
        for (List<PmbObject> rootList : rootObjectMap.values()) {
            for (PmbObject root : rootList) {
                setObjectNumbers(root);
                setObjectNumbersForChildObjects(root);
            }
        }
    }

    private void setObjectNumbersForChildObjects(PmbObject parent) throws Exception {
        for (PmbObject child : parent.getChildObjects()) {
            setObjectNumbers(child);
            setObjectNumbersForChildObjects(child);
        }
    }

    private PmbObject getParentObject(PmbObject obj) throws Exception {
        if (null == obj.getParentObjects() || obj.getParentObjects().size() < 1) {
            return null;
        }
        PmbObject parentObj = null;
        for (PmbObject parent : obj.getParentObjects()) {
            if (obj.getDn().startsWith(parent.getDn())) {
                parentObj = parent;
                break;
            }
        }
        if (null == parentObj) {
            throw new Exception("No suitable parent object");
        } else {
            return parentObj;
        }
    }

    private void setObjectNumbers(PmbObject obj) throws Exception {
        int avgPerNE = 1;
        int maxPerNE = 1;
        PmbObject parentObj = getParentObject(obj);
        if (null != parentObj) {
            avgPerNE = 1 * parentObj.getAvg();
            maxPerNE = 1 * parentObj.getMax();
        }
        int avgPerNet;
        int maxPerNet;
        int maxPerRoot = 1;
        for (ObjectLoad load : objectLoads) {
            if (load.getObjectClass().equals(obj.getName())) {
                if (load.getRelatedObjectClass() == null || "".equals(load.getRelatedObjectClass()))
                // root
                {
                    maxPerNE = load.getMax();
                    avgPerNE = load.getAvg();
                    maxPerRoot = load.getMax();
                }
                else
                // non-root
                {
                    PmbObject relatedObj = findRelatedObject(obj, load.getRelatedObjectClass());
                    if (null != relatedObj) {
                        maxPerNE = load.getMax() * relatedObj.getMax();
                        avgPerNE = load.getAvg() * relatedObj.getAvg();
                        maxPerRoot = load.getMax();
                    }
                }
            }
        }

        maxPerNet = maxPerNE * config.getNeSize().getMaxNePerNetwork();
        avgPerNet = avgPerNE * config.getNeSize().getAvgNePerNetwork();

        obj.setMin(1);
        obj.setMax(maxPerNE);
        obj.setAvg(avgPerNE);
        obj.setMaxPerNet(maxPerNet);
        obj.setAvgPerNet(avgPerNet);
        obj.setMaxPerNE(maxPerNE);
        obj.setMaxNePerNet(config.getNeSize().getMaxNePerNetwork());
        obj.setAvgNePerNet(config.getNeSize().getAvgNePerNetwork());
        obj.setMaxPerRoot(maxPerRoot);
    }

    private PmbObject findRelatedObject(PmbObject object, String name) {
        for (List<PmbObject> list : allReleaseObjects.values()) {
            for (PmbObject obj : list) {
                if (obj.getName().equals(name) && isDescendant(object, name) && object.getDn().startsWith(obj.getDn())) {
                    return obj;
                }
            }
        }
        return null;
    }

    // is object is descendant of object with name
    private boolean isDescendant(PmbObject object, String name) {
        boolean descendant = false;
        String dn = object.getDn();
        String[] clazz = dn.split("/");
        for (String clz : clazz) {
            if (clz.equals(name)) {
                descendant = true;
                break;
            }
        }
        return descendant;
    }

    private String getParent(String adaptationId) {
        List<ParentHierarchy> parentHierarchies = config.getParentHierarchies();
        for (ParentHierarchy parentHierarchy : parentHierarchies) {
            if (parentHierarchy.getAdaptationId().equals(adaptationId)) {
                return parentHierarchy.getHierarchy();
            }
        }
        return null;
    }

    private void addParentObject() throws Exception {
        Map<String, List<PmbObject>> rootObjectMap = getRootObjects();
        for (Map.Entry<String, List<PmbObject>> entry : allReleaseObjects.entrySet()) {
            String adaptationId = entry.getKey();
            String parent = getParent(adaptationId);
            if (null != parent) {
                List<PmbObject> rootObjects = rootObjectMap.get(adaptationId);

                String lastClass = getLastClass(parent);
                ParentObject globalObject = findGlobalObjectByName(lastClass);
                if (null == globalObject) {
                    throw new Exception("Parent Object class '" + lastClass + "' is not defined.");
                }
                PmbObject pmbObject = new PmbObject();
                pmbObject.setAdditional(true);
                pmbObject.setName(lastClass);
                pmbObject.setNameInOmes(globalObject.getNameInOMeS());
                pmbObject.setPresentation(globalObject.getPresentation());
                pmbObject.setTransient(globalObject.isTransient());

                addPmbObjectToList(adaptationId, pmbObject);

                String parentHierarchy = getParentHierarchy(parent);

                for (PmbObject rootObject : rootObjects) {
                    rootObject.addParentObject(pmbObject);
                    pmbObject.addChildObject(rootObject);

                    createPmbObjectsFromParentHierarchy(adaptationId, pmbObject, parentHierarchy);
                }
            }
        }
    }

    private ParentObject findGlobalObjectByName(String name) {
        for (ParentObject globalObject : globalObjects) {
            if (globalObject.getName().equals(name)) {
                return globalObject;
            }
        }
        return null;
    }

    private void addPmbObjectToList(String adaptationId, PmbObject pmbObject) {
        if (allReleaseObjects.containsKey(adaptationId)) {
            List<PmbObject> pmbObjects = allReleaseObjects.get(adaptationId);
            if (null != pmbObjects) {
                if (!pmbObjects.contains(pmbObject)) {
                    pmbObjects.add(pmbObject);
                }
            }
        }
    }

    private void createPmbObjectsFromParentHierarchy(String adaptationId,
                                                     PmbObject childObject,
                                                     String hierarchy) throws Exception{
        if (null == hierarchy || "".equals(hierarchy)) {
            return;
        }
        String lastClass = getLastClass(hierarchy);

        ParentObject globalObject = findGlobalObjectByName(lastClass);
        if (null == globalObject) {
            throw new Exception("Parent Object class '" + lastClass + "' is not defined.");
        }

        PmbObject pmbObject = new PmbObject();
        pmbObject.setAdditional(true);
        pmbObject.setName(lastClass);
        pmbObject.setNameInOmes(globalObject.getNameInOMeS());
        pmbObject.setPresentation(globalObject.getPresentation());
        pmbObject.setTransient(globalObject.isTransient());

        if (null != childObject) {
            pmbObject.addChildObject(childObject);
            childObject.addParentObject(pmbObject);
        }
        addPmbObjectToList(adaptationId, pmbObject);

        String parentHierarchy = getParentHierarchy(hierarchy);
        createPmbObjectsFromParentHierarchy(adaptationId, pmbObject, parentHierarchy);
    }

    public Map<String, List<PmbObject>> getRootObjects() {
        Map<String, List<PmbObject>> rootObjectMap = new HashMap<>();
        for (Map.Entry<String, List<PmbObject>> entry : allReleaseObjects.entrySet()) {
            String adaptationId = entry.getKey();
            List<PmbObject> rootObjects = new ArrayList<>();
            for (PmbObject pmbObject : entry.getValue()) {
                if (pmbObject.getParentObjects().size() < 1) {
                    rootObjects.add(pmbObject);
                }
            }
            rootObjectMap.put(adaptationId, rootObjects);
        }
        return rootObjectMap;
    }

    private void createPmbObjectFromPmb() throws Exception {
        Map<String, List<PmAdaptation>> pmAdaptationsMap = adaptationRepository.getPmAdaptations();

        for (Map.Entry<String, List<PmAdaptation>> entry : pmAdaptationsMap.entrySet()) {
            String adaptationId = entry.getKey();
            List<PmAdaptation> pmAdaptations = entry.getValue();

            for (PmAdaptation pmAdaptation : pmAdaptations) {
                List<ObjectClass> pmClasses = pmAdaptation.getObjectClasses();
                for (Measurement measurement : pmAdaptation.getMeasurements()) {
                    for (MeasuredTarget measuredTarget : measurement.getMeasuredTargets()) {
                        String dimension = measuredTarget.getDimension();
                        for (String hierarchy : measuredTarget.getHierarchies()) {
                            hierarchy = format(hierarchy);
                            createPmObjectsFromHierarchy(adaptationId, hierarchy, null, pmAdaptation.getAdapRelease(), dimension, pmClasses);
                        }
                    }
                }
            }
        }
    }

    private void addToList(String adaptationId, PmbObject pmbObject) {
        if (allReleaseObjects.containsKey(adaptationId)) {
            List<PmbObject> pmbObjects = allReleaseObjects.get(adaptationId);
            if (!pmbObjects.contains(pmbObject)) {
                pmbObjects.add(pmbObject);
            }
        } else {
            List<PmbObject> pmbObjects = new ArrayList<>();
            pmbObjects.add(pmbObject);
            allReleaseObjects.put(adaptationId, pmbObjects);
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

    private void createPmObjectsFromHierarchy(String adaptationId,
                                              String hierarchy,
                                              PmbObject parentObj,
                                              String version,
                                              String dimension,
                                              List<ObjectClass> pmClasses) throws Exception {
        if (null == hierarchy || "".equals(hierarchy)) {
            return;
        }
        String firstClass = getFirstClass(hierarchy);
        String childHierarchy = getChildHierarchy(hierarchy);

        PmbObject pmbObject = findOrCreatePmObject(adaptationId, firstClass, pmClasses, version, dimension);
        addToList(adaptationId, pmbObject);
        if (null != parentObj) {
            parentObj.addChildObject(pmbObject);
            pmbObject.addParentObject(parentObj);

            pmbObject.setOriginalDn(parentObj.getOriginalDn() + "/" + pmbObject.getName());
        } else {
            pmbObject.setOriginalDn(pmbObject.getName());
        }
        if (null == childHierarchy || "".equals(childHierarchy)) {
            pmbObject.setMeasuredObject(true);
        }
        createPmObjectsFromHierarchy(adaptationId, childHierarchy, pmbObject, version, dimension, pmClasses);
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

    private static String getParentHierarchy(String hierarchy) {
        hierarchy = format(hierarchy);
        String parentHierarchy = null;
        if (hierarchy.lastIndexOf("/") > -1) {
            parentHierarchy = hierarchy.substring(0, hierarchy.lastIndexOf("/"));
        }
        return parentHierarchy;
    }

    private PmbObject findOrCreatePmObject(String adaptationId,
                                           String clazz,
                                           List<ObjectClass> objectClasses,
                                           String version,
                                           String dimension) throws Exception {
        PmbObject pmbObject = findPmObjectByName(adaptationId, clazz);
        if (pmbObject == null) {
            pmbObject = new PmbObject();
            ObjectClass classDef = findPmClassDefinition(clazz, objectClasses);
            if (null == classDef) {
                LOG.severe("Can't find PM class: " + clazz);
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

    private PmbObject findPmObjectByName(String adaptationId, String className) {
        if (allReleaseObjects.containsKey(adaptationId)) {
            List<PmbObject> pmbObjects = allReleaseObjects.get(adaptationId);
            for (PmbObject pmbObject : pmbObjects) {
                if (pmbObject.getName().equals(className)) {
                    return pmbObject;
                }
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
}
