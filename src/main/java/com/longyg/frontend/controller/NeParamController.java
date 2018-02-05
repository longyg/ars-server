package com.longyg.frontend.controller;

import com.longyg.backend.ars.tpl.Variable;
import com.longyg.backend.ars.tpl.VariablesRepository;
import com.longyg.frontend.model.ne.NeRelease;
import com.longyg.frontend.model.ne.NeType;
import com.longyg.frontend.model.ne.NeParam;
import com.longyg.frontend.service.NeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.logging.Logger;

@Controller
public class NeParamController {
    private static final Logger LOG = Logger.getLogger(NeParamController.class.getName());

    @Autowired
    private NeService neService;

    @RequestMapping("/param")
    public ModelAndView list(HttpServletRequest request) {
        String neTypeId = request.getParameter("neTypeId");
        List<NeRelease> neReleaseList = neService.findAllReleasesByTypeId(neTypeId);

        String neRelId = request.getParameter("neRelId");
        List<NeParam> neParamList = neService.findAllParams(neRelId);

        List<NeType> allNeTypeList = neService.findAllNeTypes();

        Map<String, Object> params = new HashMap<>();
        params.put("neTypeId", neTypeId);
        params.put("neRelId", neRelId);
        params.put("neParamList", neParamList);
        params.put("neReleaseList", neReleaseList);
        params.put("allNeTypeList", allNeTypeList);
        return new ModelAndView("param/list", params);
    }

    @RequestMapping(value = "/param/add", method = RequestMethod.GET)
    public ModelAndView add(@RequestParam String neTypeId, @RequestParam String neRelId) {
        NeRelease neRelease = neService.findRelease(neRelId);
        List<Variable> paramList = VariablesRepository.getVariables();

        Map<String, Object> params = new HashMap<>();
        params.put("neTypeId", neTypeId);
        params.put("neRelId", neRelId);
        params.put("neRelease", neRelease);
        params.put("paramList", paramList);

        return new ModelAndView("param/add", params);
    }

    @RequestMapping(value = "/param/save", method = RequestMethod.POST)
    public String save(HttpServletRequest request) {
        String neTypeId = request.getParameter("neTypeId");
        String neRelId = request.getParameter("neRelId");
        String neType = request.getParameter("neType");
        String neVersion = request.getParameter("neVersion");

        List<NeParam> neParamList = neService.findAllParams(neType, neVersion);
        int v = neParamList.size() + 1;

        NeParam neParam = new NeParam();
        neParam.setNeType(neType);
        LOG.info("NE Type: " + neType);
        neParam.setNeVersion(neVersion);
        LOG.info("NE version:" + neVersion);
        neParam.setV(v);

        List<Variable> variables = new ArrayList<>();
        Enumeration parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = (String) parameterNames.nextElement();
            String paramValue = request.getParameter(paramName);
            Variable variable = new Variable(paramName, paramValue);
            variables.add(variable);
        }
        neParam.setVariables(variables);

        neService.saveParam(neParam);

        return "redirect:/param?neTypeId=" + neTypeId + "&neRelId=" + neRelId;
    }

    @RequestMapping("/param/view")
    public ModelAndView view(@RequestParam String id) {
        NeParam neParam = neService.findParam(id);
        Map<String, Object> params = new HashMap<>();
        params.put("neParam", neParam);
        return new ModelAndView("param/view", params);
    }

    @RequestMapping("/param/edit")
    public ModelAndView edit(@RequestParam String id, @RequestParam String neTypeId, @RequestParam String neRelId) {
        NeParam neParam = neService.findParam(id);
        Map<String, Object> params = new HashMap<>();
        params.put("neParam", neParam);
        params.put("neTypeId", neTypeId);
        params.put("neRelId", neRelId);
        params.put("id", id);
        return new ModelAndView("param/edit", params);
    }

    @RequestMapping("/param/update")
    public String update(HttpServletRequest request) {
        String id = request.getParameter("id");
        NeParam neParam = neService.findParam(id);
        if (null != neParam) {
            Enumeration parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String paramName = (String) parameterNames.nextElement();
                String paramValue = request.getParameter(paramName);
                for (Variable variable : neParam.getVariables()) {
                    if (variable.getName().equals(paramName)) {
                        variable.setValue(paramValue);
                    }
                }
            }
            neService.saveParam(neParam);
        }

        String neTypeId = request.getParameter("neTypeId");
        String neRelId = request.getParameter("neRelId");

        return "redirect:/param?neTypeId=" + neTypeId + "&neRelId=" + neRelId;
    }

    @RequestMapping("/param/delete")
    public String delete(@RequestParam String id, @RequestParam String neTypeId, @RequestParam String neRelId) {
        neService.deleteParam(id);
        return "redirect:/param?neTypeId=" + neTypeId + "&neRelId=" + neRelId;
    }


}
