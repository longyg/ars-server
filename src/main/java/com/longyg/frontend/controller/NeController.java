package com.longyg.frontend.controller;

import com.longyg.frontend.model.config.InterfaceObject;
import com.longyg.frontend.model.config.InterfaceRepository;
import com.longyg.frontend.model.ne.*;
import com.longyg.frontend.service.NeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.logging.Logger;

@Controller
public class NeController {
    @Autowired
    private NeService neService;

    @Autowired
    private InterfaceRepository interfaceRepository;

    @RequestMapping("/netype")
    public ModelAndView listNeType() {
        List<NeType> neTypeList = neService.findAllNeTypes();
        Map<String, Object> params = new HashMap<>();
        params.put("neTypeList", neTypeList);
        return new ModelAndView("ne/netype", params);
    }

    @RequestMapping(value = "/netype/add", method = RequestMethod.POST)
    public String addNeType(@RequestParam("adaptations") String[] adaptations, @ModelAttribute NeType neType) {
        List<String> adaptSet = new ArrayList<>();
        for (String adaptation : adaptations) {
            adaptSet.add(adaptation);
        }
        neType.setAdaptSet(adaptSet);
        neService.saveNeType(neType);
        return "redirect:/netype";
    }

    @RequestMapping(value = "/netype/delete")
    public String deleteNeType(@RequestParam String id) {
        neService.deleteNeType(id);
        return "redirect:/netype";
    }

    //////////////////////////////////////////////////////////////
    // NE Release
    @RequestMapping("/nerelease")
    public ModelAndView listNeRelease() {
        List<NeRelease> neReleaseList = neService.findAllReleases();
        List<NeType> neTypeList = neService.findAllNeTypes();
        Map<String, Object> params = new HashMap<>();
        params.put("neReleaseList", neReleaseList);
        params.put("neTypeList", neTypeList);
        return new ModelAndView("ne/nerelease", params);
    }

    @RequestMapping(value = "/nerelease/add", method = RequestMethod.POST)
    public String addNeRelease(@ModelAttribute NeRelease neRelease) {
        neService.saveRelease(neRelease);
        return "redirect:/nerelease";
    }

    @RequestMapping("/nerelease/delete")
    public String deleteNeRelease(@RequestParam String id) {
        neService.deleteRelease(id);
        return "redirect:/nerelease";
    }

    /////////////////////////////////////////////////////////
    // NE Interface
    @RequestMapping("/neif")
    public ModelAndView listNeInterface(HttpServletRequest request) {
        String neTypeId = request.getParameter("neTypeId");

        List<NeRelease> neReleaseList = neService.findAllReleasesByTypeId(neTypeId);

        String neRelId = request.getParameter("neRelId");

        NeInterface neInterface = neService.findNeInterface(neRelId);

        List<InterfaceObject> neIntObjs = new ArrayList<>();
        if (null != neInterface) {
            for (String ifId : neInterface.getInterfaces()) {
                Optional<InterfaceObject> opt = interfaceRepository.findById(ifId);
                if (opt.isPresent()) {
                    neIntObjs.add(opt.get());
                }
            }
        }

        List<InterfaceObject> allInterfaces = interfaceRepository.findAll();
        List<InterfaceObject> selectableIntObjs = new ArrayList<>();
        for (InterfaceObject io : allInterfaces) {
            boolean isAdd = false;
            for (InterfaceObject neIntObj : neIntObjs) {
                if (io.getId().equals(neIntObj.getId())) {
                    isAdd = true;
                    break;
                }
            }
            if (!isAdd) {
                selectableIntObjs.add(io);
            }
        }


        List<NeType> allNeTypeList = neService.findAllNeTypes();

        Map<String, Object> params = new HashMap<>();

        params.put("neTypeId", neTypeId);
        params.put("neRelId", neRelId);
        params.put("neReleaseList", neReleaseList);
        params.put("allNeTypeList", allNeTypeList);
        params.put("neInterface", neInterface);
        params.put("neIntObjs", neIntObjs);
        params.put("selectableIntObjs", selectableIntObjs);

        return new ModelAndView("ne/interface", params);
    }

    @RequestMapping("/neif/add")
    public String addNeInterface(HttpServletRequest request) {
        String neTypeId = request.getParameter("neTypeId");
        String neRelId = request.getParameter("neRelId");

        NeRelease neRelease = neService.findRelease(neRelId);

        String ifId = request.getParameter("interface");

        NeInterface neInterface = neService.findNeInterface(neRelId);

        if (null != neInterface)
        {
            neInterface.addInterface(ifId);
        }
        else
        {
            neInterface = new NeInterface();
            neInterface.setNeType(neRelease.getType());
            neInterface.setNeVersion(neRelease.getVersion());
            neInterface.addInterface(ifId);
        }

        neService.saveNeInterface(neInterface);

        return "redirect:/neif?neTypeId=" + neTypeId + "&neRelId=" + neRelId;
    }

    @RequestMapping("/neif/delete")
    public String deleteNeInterface(HttpServletRequest request) {
        String neTypeId = request.getParameter("neTypeId");
        String neRelId = request.getParameter("neRelId");

        String ifId = request.getParameter("id");

        NeInterface neInterface = neService.findNeInterface(neRelId);

        if (null != neInterface)
        {
            neInterface.removeInterface(ifId);
        }

        neService.saveNeInterface(neInterface);

        return "redirect:/neif?neTypeId=" + neTypeId + "&neRelId=" + neRelId;
    }
}
