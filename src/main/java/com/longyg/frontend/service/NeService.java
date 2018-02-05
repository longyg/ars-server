package com.longyg.frontend.service;

import com.longyg.frontend.model.ne.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class NeService {
    @Autowired
    private NeTypeRepository neTypeRepository;

    @Autowired
    private NeReleaseRepository neReleaseRepository;

    @Autowired
    private NeParamRepository neParamRepository;

    @Autowired
    private NeInterfaceRepository neInterfaceRepository;

    @Autowired
    private AdaptationRepository adaptationRepository;

    @Autowired
    private ReleaseConfigRepository releaseConfigRepository;

    ///////////////////////////////////////
    // NE Type Service
    public List<NeType> findAllNeTypes() {
        return neTypeRepository.findAll();
    }

    public NeType findNeType(String id) {
        if (null == id) {
            return null;
        }
        NeType neType = null;

        Optional<NeType> neTypeOpt = neTypeRepository.findById(id);
        if (neTypeOpt.isPresent())
        {
            neType = neTypeOpt.get();
        }

        return neType;
    }

    public NeType findNeTypeByName(String name) {
        return neTypeRepository.findByName(name);
    }

    public NeType saveNeType(NeType neType) {
        if (null == neType) {
            return null;
        }
        return neTypeRepository.save(neType);
    }

    public void deleteNeType(String id) {
        if (null == id) {
            return;
        }
        neTypeRepository.deleteById(id);
    }

    public void deleteNeTypes(List<String> ids) {
        if (null != ids && ids.size() > 0) {
            neTypeRepository.deleteByIdIn(ids);
        }
    }

    ///////////////////////////////////////////////////////
    // NE Release Service
    public List<NeRelease> findAllReleases() {
        return neReleaseRepository.findAll();
    }

    public NeRelease saveRelease(NeRelease neRelease) {
        if (null == neRelease) {
            return null;
        }
        return neReleaseRepository.save(neRelease);
    }

    public void deleteRelease(String id) {
        if (null == id) {
            return;
        }
        neReleaseRepository.deleteById(id);
    }

    public void deleteReleases(List<String> ids) {
        if (null != ids && ids.size() > 0) {
            neReleaseRepository.deleteByIdIn(ids);
        }
    }

    public List<NeRelease> findAllReleasesByType(String neType) {
        if (null == neType) {
            return new ArrayList<>();
        }
        return neReleaseRepository.findByType(neType);
    }

    public List<NeRelease> findAllReleasesByTypeId(String neTypeId) {
        List<NeRelease> neReleaseList = new ArrayList<>();
        if (null == neTypeId) {
            return neReleaseList;
        }

        NeType neType = findNeType(neTypeId);
        if (null != neType) {
            return findAllReleasesByType(neType.getName());
        }
        return neReleaseList;
    }

    public NeRelease findRelease(String id) {
        if (null == id || "".equals(id)) {
            return null;
        }
        NeRelease neRelease = null;
        Optional<NeRelease> neRelOpt = neReleaseRepository.findById(id);
        if (neRelOpt.isPresent())
        {
            neRelease = neRelOpt.get();
        }

        return neRelease;
    }

    ///////////////////////////////////////////////////////
    // NE Adaptation Service
    public List<Adaptation> findAllAdaptations() {
        return adaptationRepository.findAll();
    }

    public Adaptation saveAdaptation(Adaptation adaptation) {
        if (null == adaptation) {
            return null;
        }
        return adaptationRepository.save(adaptation);
    }

    public void deleteAdaptation(String id) {
        if (null == id) {
            return;
        }
        adaptationRepository.deleteById(id);
    }

    public void deleteAdaptations(List<String> ids) {
        if (null != ids && ids.size() > 0) {
            adaptationRepository.deleteByIdIn(ids);
        }
    }

    public Adaptation findAdaptation(String id) {
        if (null == id) {
            return null;
        }
        Optional<Adaptation> opt = adaptationRepository.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        }
        return null;
    }


    ///////////////////////////////////////////////////////
    // NE Release Config Service
    public List<ReleaseConfig> findAllReleaseConfigs() {
        return releaseConfigRepository.findAll();
    }

    public ReleaseConfig saveReleaseConfig(ReleaseConfig config) {
        if (null == config) {
            return null;
        }
        return releaseConfigRepository.save(config);
    }

    public void deleteReleaseConfig(String id) {
        if (null == id) {
            return;
        }
        releaseConfigRepository.deleteById(id);
    }

    public void deleteReleaseConfigs(List<String> ids) {
        if (null != ids && ids.size() > 0) {
            releaseConfigRepository.deleteByIdIn(ids);
        }
    }

    public ReleaseConfig findReleaseConfig(String id) {
        if (null == id) {
            return null;
        }
        Optional<ReleaseConfig> opt = releaseConfigRepository.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        }
        return null;
    }


    ///////////////////////////////////////////////////////////////////////
    // NE Param
    public List<NeParam> findAllParams(String neType, String neRelease) {
        if (null == neType || null == neRelease) {
            return new ArrayList<>();
        }
        return neParamRepository.findByNeRelease(neType, neRelease);
    }

    public List<NeParam> findAllParams(String neRelId) {
        List<NeParam> neParamList = new ArrayList<>();
        if (null == neRelId || "".equals(neRelId))
        {
            return neParamList;
        }

        NeRelease neRelease = findRelease(neRelId);

        if (null != neRelease)
        {
            return findAllParams(neRelease.getType(), neRelease.getVersion());
        }

        return neParamList;
    }

    public NeParam saveParam(NeParam neParam) {
        if (null == neParam) {
            return null;
        }
        return neParamRepository.save(neParam);
    }

    public NeParam findParam(String id) {
        if (null == id || "".equals(id)) {
            return null;
        }
        NeParam neParam = null;
        Optional<NeParam> opt = neParamRepository.findById(id);
        if (opt.isPresent()) {
            neParam = opt.get();
        }
        return neParam;
    }

    public void deleteParam(String id) {
        if (null == id) {
            return;
        }
        neParamRepository.deleteById(id);
    }

    ///////////////////////////////////////////////////
    // NE Interface
    public NeInterface saveNeInterface(NeInterface neInterface) {
        if (null == neInterface) {
            return null;
        }
        return neInterfaceRepository.save(neInterface);
    }

    public NeInterface findNeInterface(String neType, String neVersion) {
        if (null == neType || null == neVersion) {
            return null;
        }
        return neInterfaceRepository.findByNeRelease(neType, neVersion);
    }

    public NeInterface findNeInterface(NeRelease neRelease) {
        if (null == neRelease) {
            return null;
        }
        return findNeInterface(neRelease.getType(), neRelease.getVersion());
    }

    public NeInterface findNeInterface(String neRelId) {
        if (null == neRelId || "".equals(neRelId))
        {
            return null;
        }

        NeRelease neRelease = findRelease(neRelId);

        if (null != neRelease)
        {
            return findNeInterface(neRelease);
        }

        return null;
    }
}
