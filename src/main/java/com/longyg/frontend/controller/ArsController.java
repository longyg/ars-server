package com.longyg.frontend.controller;

import com.longyg.backend.ars.generator.ArsGenerator;
import com.longyg.frontend.model.ars.*;
import com.longyg.frontend.model.ars.alarm.AlarmSpec;
import com.longyg.frontend.model.ars.counter.CounterSpec;
import com.longyg.frontend.model.ars.om.ObjectModelSpec;
import com.longyg.frontend.model.ars.pm.PmDataLoadSpec;
import com.longyg.frontend.model.ars.us.UsRepository;
import com.longyg.frontend.model.ars.us.UserStorySpec;
import com.longyg.frontend.model.config.*;
import com.longyg.frontend.model.ne.NeRelease;
import com.longyg.frontend.model.ne.NeReleaseRepository;
import com.longyg.frontend.model.ne.NeType;
import com.longyg.frontend.model.ne.NeTypeRepository;
import com.longyg.frontend.model.response.AjaxResponse;
import com.longyg.frontend.service.ArsService;
import com.longyg.frontend.service.ConfigService;
import com.longyg.frontend.service.NeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.logging.Logger;

@RestController
public class ArsController {
    private static final Logger LOG = Logger.getLogger(ArsController.class.getName());

    @Autowired
    private UsRepository usRepository;

    @Autowired
    private NeTypeRepository neTypeRepository;

    @Autowired
    private NeReleaseRepository neReleaseRepository;

    @Autowired
    private ArsRepository arsRepository;

    @Autowired
    private ArsConfigRepository arsConfigRepository;

    @Autowired
    private AdaptationResourceRepository resourceRepository;

    @Autowired
    private NeService neService;

    @Autowired
    private ArsService arsService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private ArsGenerator arsGenerator;

    @RequestMapping("/ars")
    public ModelAndView list(HttpServletRequest request) {
        String neTypeId = request.getParameter("neTypeId");

        List<NeReleaseArs> neArsList = new ArrayList<>();

        List<NeRelease> neReleaseList = findNeReleaseByNeTypeId(neTypeId);

        for (NeRelease neRelease : neReleaseList) {
            NeReleaseArs neArs = new NeReleaseArs();
            neArs.setNeRelease(neRelease);

            ArsConfig arsConfig = arsConfigRepository.findByNeTypeAndRelease(neRelease.getType(), neRelease.getVersion());
            ARS ars = arsRepository.findByNeTypeAndRelease(neRelease.getType(), neRelease.getVersion());

            neArs.setArs(ars);
            neArs.setArsConfig(arsConfig);
            neArsList.add(neArs);
        }

        List<NeType> allNeTypeList = neTypeRepository.findAll();

        Map<String, Object> params = new HashMap<>();
        params.put("neTypeId", neTypeId);
        params.put("allNeTypeList", allNeTypeList);
        params.put("neArsList", neArsList);

        return new ModelAndView("ars/list", params);
    }

    @RequestMapping("/ars/view")
    public ModelAndView ars(@ModelAttribute NeRelease ne) {
        UserStorySpec usSpec = usRepository.findByNe(ne);

        Map<String, Object> params = new HashMap<>();
        params.put("ne", ne);
        params.put("usSpec", usSpec);

        return new ModelAndView("ars/view", params);
    }

    @RequestMapping("/ars/create")
    public String createUs(@RequestParam String neTypeId, @RequestParam String neRelId) {
        ArsConfig arsConfig = findArsConfig(neRelId);
        if (null != arsConfig)
        {
            try
            {
                NeRelease neRelease = neService.findRelease(neRelId);
                // TODO
                arsConfig.setMaxNePerNet(1);
                arsConfig.setAvgNePerNet(1);
                arsGenerator.generateAndSave(arsConfig);
            }
            catch (Exception e)
            {
                LOG.severe("Exception while generating ARS: ");
                e.printStackTrace();
            }
        }
        else
        {
            LOG.severe("ARS config is null, can't generate ARS!");
        }

        return "redirect:/ars?neTypeId=" + neTypeId;
    }

    @RequestMapping("/ars/viewOm")
    public ModelAndView viewObjectModel(@RequestParam String id, @RequestParam String neTypeId) {
        Map<String, Object> params = new HashMap<>();

        NeType neType = neService.findNeType(neTypeId);
        ObjectModelSpec spec = arsService.findOm(id);

        List<String> adaps = new ArrayList<>();
        for (String adaptationId : neType.getAdaptSet()) {
            String adap = adaptationId.replaceAll("\\.", "_");
            adaps.add(adap);
        }
        neType.setAdaptSet(adaps);

        params.put("spec", spec);
        params.put("neTypeId", neTypeId);
        params.put("neType", neType);

        return new ModelAndView("ars/viewOm", params);
    }

    @RequestMapping("/ars/viewPmDL")
    public ModelAndView viewPmDataLoad(@RequestParam String id, @RequestParam String neTypeId) {
        Map<String, Object> params = new HashMap<>();

        NeType neType = neService.findNeType(neTypeId);
        PmDataLoadSpec spec = arsService.findPmDL(id);

        List<String> adaps = new ArrayList<>();
        for (String adaptationId : neType.getAdaptSet()) {
            String adap = adaptationId.replaceAll("\\.", "_");
            adaps.add(adap);
        }
        neType.setAdaptSet(adaps);

        params.put("spec", spec);
        params.put("neTypeId", neTypeId);
        params.put("neType", neType);

        return new ModelAndView("ars/viewPmDL", params);
    }

    @RequestMapping("/ars/viewCounter")
    public ModelAndView viewCounter(@RequestParam String id, @RequestParam String neTypeId) {
        Map<String, Object> params = new HashMap<>();

        NeType neType = neService.findNeType(neTypeId);
        CounterSpec spec = arsService.findCounter(id);

        List<String> adaps = new ArrayList<>();
        for (String adaptationId : neType.getAdaptSet()) {
            String adap = adaptationId.replaceAll("\\.", "_");
            adaps.add(adap);
        }
        neType.setAdaptSet(adaps);

        params.put("spec", spec);
        params.put("neTypeId", neTypeId);
        params.put("neType", neType);

        return new ModelAndView("ars/viewCounter", params);
    }

    @RequestMapping("/ars/viewAlarm")
    public ModelAndView viewAlarm(@RequestParam String id, @RequestParam String neTypeId) {
        Map<String, Object> params = new HashMap<>();

        NeType neType = neService.findNeType(neTypeId);
        AlarmSpec spec = arsService.findAlarm(id);

        List<String> adaps = new ArrayList<>();
        for (String adaptationId : neType.getAdaptSet()) {
            String adap = adaptationId.replaceAll("\\.", "_");
            adaps.add(adap);
        }
        neType.setAdaptSet(adaps);

        params.put("spec", spec);
        params.put("neTypeId", neTypeId);
        params.put("neType", neType);

        return new ModelAndView("ars/viewAlarm", params);
    }

    @RequestMapping("/ars/setConfig")
    public ModelAndView setConfig(@RequestParam String neTypeId, @RequestParam String neRelId) {
        Map<String, Object> params = new HashMap<>();
        params.put("neTypeId", neTypeId);
        params.put("neRelId", neRelId);

        NeType neType = neService.findNeType(neTypeId);
        params.put("neType", neType);

        NeRelease neRelease = neService.findRelease(neRelId);
        params.put("neRelease", neRelease);

        if (null == neType || null == neRelease) {
            LOG.severe("NE Type or NE release is null");
            return null;
        }

        ArsConfig arsConfig = arsService.findArsConfig(neRelease);
        List<InterfaceObject> interfaces = configService.findInterfaces();
        List<InterfaceObject> supportedInterfaces = new ArrayList<>();
        if (null != arsConfig) {
            for (String ifId : arsConfig.getInterfaces()) {
                InterfaceObject iface = configService.findInterface(ifId);
                if (null != iface) {
                    supportedInterfaces.add(iface);
                }
            }
        }
        params.put("supportedInterfaces", supportedInterfaces);

        List<InterfaceObject> selectableInterfaces = new ArrayList<>();
        for (InterfaceObject ifo : interfaces) {
            boolean supported = false;
            for (InterfaceObject ifObj : supportedInterfaces) {
                if (ifo.getId().equals(ifObj.getId())) {
                    supported = true;
                    break;
                }
            }
            if (!supported) {
                selectableInterfaces.add(ifo);
            }
        }
        params.put("selectableInterfaces", selectableInterfaces);

        List<AdaptationResource> supportedResources = new ArrayList<>();
        if (null != arsConfig) {
            for (String srcId : arsConfig.getResources()) {
                AdaptationResource resource = configService.findResource(srcId);
                if (null != resource) {
                    supportedResources.add(resource);
                }
            }
        }
        params.put("supportedResources", supportedResources);

        Map<String, String> supportedParents = new HashMap<>();
        if (null != arsConfig) {
            supportedParents = arsConfig.getParents();
        }
        params.put("supportedParents", supportedParents);


        List<ObjectLoad> supportedLoads = new ArrayList<>();
        if (null != arsConfig) {
            List<String> loadIds = arsConfig.getLoadIds();
            for (String loadId : loadIds) {
                ObjectLoad objectLoad = configService.findObjectLoad(loadId);
                if (objectLoad != null) {
                    supportedLoads.add(objectLoad);
                }
            }
        }
        params.put("supportedLoads", supportedLoads);

        List<ObjectLoad> selectableLoads = new ArrayList<>();
        List<ObjectLoad> allLoads = configService.findObjectLoads();
        for (ObjectLoad load : allLoads) {
            boolean isSupported = false;
            for (ObjectLoad supportLoad : supportedLoads) {
                if (load.getId().equals(supportLoad.getId())) {
                    isSupported = true;
                    break;
                }
            }
            if (!isSupported) {
                selectableLoads.add(load);
            }
        }

        for (ObjectLoad load : configService.findObjectLoads()) {
            boolean supported = false;
            for (ObjectLoad ld : supportedLoads) {
                if (ld.getId().equals(load.getId())) {
                    supported = true;
                    break;
                }
            }
            if (!supported) {
                selectableLoads.add(load);
            }
        }
        params.put("selectableLoads", selectableLoads);

        return new ModelAndView("ars/addConfig", params);
    }

    @RequestMapping(value = "/getAdapVersions", method = RequestMethod.POST)
    @ResponseBody
    public List<String> getAdaptationReleases(@RequestParam String adaptationId) {
        List<String> releases = new ArrayList<>();
        List<AdaptationResource> resources = resourceRepository.findAll();

        for (AdaptationResource src : resources) {
            if (src.getAdaptation().getId().equals(adaptationId)) {
                releases.add(src.getAdaptation().getRelease());
            }
        }

        return releases;
    }

    @RequestMapping(value = "/ars/addResource", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse addArsResource(@RequestParam String neTypeId, @RequestParam String neRelId,
                                       @RequestParam String adaptationId, @RequestParam String adaptationRelease) {
        ArsConfig arsConfig = findOrCreateArsConfig(neRelId);

        AdaptationResource addResource = null;
        List<AdaptationResource> resources = configService.findResources();
        for (AdaptationResource src : resources) {
            if (src.getAdaptation().getId().equals(adaptationId) && src.getAdaptation().getRelease().equals(adaptationRelease)) {
                boolean success = arsConfig.addResource(src.getId());
                if (success) {
                    addResource = src;
                }
                break;
            }
        }
        arsService.saveConfig(arsConfig);

        AjaxResponse response = new AjaxResponse();
        if (null != addResource) {
            response.setStatus("ok");
            response.setData(addResource.getAdaptation());
        } else {
            response.setStatus("nok");
        }
        return response;
    }

    @RequestMapping("/ars/addInterface")
    @ResponseBody
    public AjaxResponse addInterface(@RequestParam String neTypeId,
                                     @RequestParam String neRelId, @RequestParam String interfaceId) {
        ArsConfig arsConfig = findOrCreateArsConfig(neRelId);

        InterfaceObject addInterface = null;
        List<InterfaceObject> interfaces = configService.findInterfaces();
        for (InterfaceObject ifo : interfaces) {
            if (ifo.getId().equals(interfaceId)) {
                boolean success = arsConfig.addInterface(interfaceId);
                if (success) {
                    addInterface = ifo;
                }
                break;
            }
        }
        arsService.saveConfig(arsConfig);

        AjaxResponse response = new AjaxResponse();
        if (null != addInterface) {
            response.setStatus("ok");
            response.setData(addInterface);
            LOG.info(response.getData().toString());
        } else {
            response.setStatus("nok");
        }
        return response;
    }

    @RequestMapping("/ars/addParent")
    @ResponseBody
    public AjaxResponse addParent(@RequestParam String neTypeId, @RequestParam String neRelId,
                                  @RequestParam String adaptationId, @RequestParam String parent) {
        ArsConfig arsConfig = findOrCreateArsConfig(neRelId);

        boolean flag = arsConfig.addParent(adaptationId, parent);

        AjaxResponse response = new AjaxResponse();
        if (flag) {
            arsService.saveConfig(arsConfig);
            response.setStatus("ok");
        } else {
            response.setStatus("nok");
        }
        return response;
    }

    @RequestMapping("/ars/addLoad")
    @ResponseBody
    public AjaxResponse addLoad(@RequestParam String neTypeId,
                                     @RequestParam String neRelId, @RequestParam String loadId) {
        ArsConfig arsConfig = findOrCreateArsConfig(neRelId);

        ObjectLoad objectLoad = null;
        List<ObjectLoad> loads = configService.findObjectLoads();
        for (ObjectLoad load : loads) {
            if (load.getId().equals(loadId)) {
                boolean success = arsConfig.addObjectLoad(loadId);
                if (success) {
                    objectLoad = load;
                }
                break;
            }
        }
        arsService.saveConfig(arsConfig);

        AjaxResponse response = new AjaxResponse();
        if (null != objectLoad) {
            response.setStatus("ok");
            response.setData(objectLoad);
        } else {
            response.setStatus("nok");
        }
        return response;
    }

    private ArsConfig findOrCreateArsConfig(String neRelId) {
        NeRelease neRelease = neService.findRelease(neRelId);
        ArsConfig arsConfig = arsService.findArsConfig(neRelease);
        if (arsConfig == null) {
            arsConfig = new ArsConfig();
            if (neRelease != null) {
                arsConfig.setNeType(neRelease.getType());
                arsConfig.setNeVersion(neRelease.getVersion());
            }
        }
        return arsConfig;
    }

    private List<NeRelease> findNeReleaseByNeTypeId(String neTypeId) {
        List<NeRelease> neReleaseList = new ArrayList<>();
        if (null != neTypeId && !"".equals(neTypeId)) {
            Optional<NeType> neTypeOpt = neTypeRepository.findById(neTypeId);
            if (neTypeOpt.isPresent()) {
                NeType neType = neTypeOpt.get();
                neReleaseList = neReleaseRepository.findByType(neType.getName());
            }
        }
        return neReleaseList;
    }

    private ArsConfig findArsConfig(String neRelId) {
        if (null != neRelId && !"".equals(neRelId)) {
            Optional<NeRelease> neRelOpt = neReleaseRepository.findById(neRelId);
            if (neRelOpt.isPresent()) {
                NeRelease neRelease = neRelOpt.get();
                return arsConfigRepository.findByNeTypeAndRelease(neRelease.getType(), neRelease.getVersion());
            }
        }
        return null;
    }


}
