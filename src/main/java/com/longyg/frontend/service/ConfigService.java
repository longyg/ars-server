package com.longyg.frontend.service;

import com.longyg.frontend.model.config.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ConfigService {
    @Autowired
    private AdaptationResourceRepository resourceRepository;

    @Autowired
    private InterfaceRepository interfaceRepository;

    @Autowired
    private ObjectRepository objectRepository;

    @Autowired
    private AlarmObjectRepository alarmObjectRepository;

    @Autowired
    private ObjectLoadRepository objectLoadRepository;


    //////////////////////////////////////////////////////////
    // Adaptation Resource Service
    public AdaptationResource findResource(String id) {
        if (null == id) {
            return null;
        }
        Optional<AdaptationResource> opt = resourceRepository.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        }
        return null;
    }

    public List<AdaptationResource> findResources() {
        return resourceRepository.findAll();
    }

    public AdaptationResource saveResource(AdaptationResource resource) {
        return resourceRepository.save(resource);
    }


    //////////////////////////////////////////////////////////
    // Interface Object Service
    public List<InterfaceObject> findInterfaces() {
        return interfaceRepository.findAll();
    }

    public Iterable<InterfaceObject> findInterfaces(Iterable<String> ids) {
        return interfaceRepository.findAllById(ids);
    }

    public InterfaceObject findInterface(String id) {
        if (null == id) {
            return null;
        }
        Optional<InterfaceObject> opt = interfaceRepository.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        }
        return null;
    }

    public InterfaceObject saveInterface(InterfaceObject ifo) {
        return interfaceRepository.save(ifo);
    }

    public void deleteInterface(String id) {
        if (null == id) {
            return;
        }
        interfaceRepository.deleteById(id);
    }

    public void deleteInterfaces(List<String> ids) {
        if (null != ids && ids.size() > 0) {
            interfaceRepository.deleteByIdIn(ids);
        }
    }


    //////////////////////////////////////////////////////////
    // Parent Object Service
    public List<ParentObject> findParentObjects() {
        return objectRepository.findAll();
    }

    public ParentObject saveParentObject(ParentObject pto) {
        return objectRepository.save(pto);
    }

    public ParentObject findParentObject(String id) {
        if (null == id) {
            return null;
        }
        Optional<ParentObject> opt = objectRepository.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        }
        return null;
    }

    public void deleteParentObject(String id) {
        if (null == id) {
            return;
        }
        objectRepository.deleteById(id);
    }

    public void deleteParentObjects(List<String> ids) {
        if (null != ids && ids.size() > 0) {
            objectRepository.deleteByIdIn(ids);
        }
    }


    //////////////////////////////////////////////////////////
    // Alarm Object Service
    public List<AlarmObject> findAlarmObjects() {
        return alarmObjectRepository.findAll();
    }

    public AlarmObject saveAlarmObject(AlarmObject entity) {
        return alarmObjectRepository.save(entity);
    }

    public AlarmObject findAlarmObject(String id) {
        if (null == id) {
            return null;
        }
        Optional<AlarmObject> opt = alarmObjectRepository.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        }
        return null;
    }

    public void deleteAlarmObject(String id) {
        if (null == id) {
            return;
        }
        alarmObjectRepository.deleteById(id);
    }

    public void deleteAlarmObjects(List<String> ids) {
        if (null != ids && ids.size() > 0) {
            alarmObjectRepository.deleteByIdIn(ids);
        }
    }


    //////////////////////////////////////////////////////////
    // Object Load Service
    public List<ObjectLoad> findObjectLoads() {
        return objectLoadRepository.findAll();
    }

    public ObjectLoad saveObjectLoad(ObjectLoad objectLoad) {
        return objectLoadRepository.save(objectLoad);
    }

    public ObjectLoad findObjectLoad(String id) {
        if (null == id)
            return null;
        Optional<ObjectLoad> opt = objectLoadRepository.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        }
        return null;
    }

    public List<ObjectLoad> findObjectLoads(List<String> ids) {
        List<ObjectLoad> loads = new ArrayList<>();
        for (String id : ids) {
            ObjectLoad load = findObjectLoad(id);
            if (null != load && !loads.contains(load)) {
                loads.add(load);
            }
        }
        return loads;
    }

    public void deleteObjectLoad(String id) {
        if (null == id) {
            return;
        }
        objectLoadRepository.deleteById(id);
    }

    public void deleteObjectLoads(List<String> ids) {
        if (null != ids && ids.size() > 0) {
            objectLoadRepository.deleteByIdIn(ids);
        }
    }
}
