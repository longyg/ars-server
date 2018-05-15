package com.longyg.frontend.controller;

import com.longyg.backend.ars.export.ArsExporter;
import com.longyg.backend.ars.generator.ArsCreator;
import com.longyg.frontend.Utils.FileUtils;
import com.longyg.frontend.model.ars.ARS;
import com.longyg.frontend.model.ars.alarm.AlarmSpec;
import com.longyg.frontend.model.ars.counter.CounterSpec;
import com.longyg.frontend.model.ars.om.ObjectModelSpec;
import com.longyg.frontend.model.ars.pm.PmDataLoadSpec;
import com.longyg.frontend.service.ArsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.HTMLDocument;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@RestController
public class ArsRestController {
    private static final Logger LOG = Logger.getLogger(ArsCreator.class.getName());
    @Autowired
    private ArsService arsService;

    @Autowired
    private ArsCreator arsCreator;

    @Autowired
    private ArsExporter arsExporter;

    @GetMapping("/api/ars")
    public List<ARS> getAllArs() {
        return arsService.findAllArses();
    }

    @PostMapping("/api/ars/delete")
    public void removeArses(@RequestBody List<String> ids) {
        arsService.deleteArses(ids);
    }

    @PostMapping("/api/ars/generate")
    public ResponseEntity<ARS> generateArs(@RequestBody ARS ars) {
        ARS existArs = arsService.findArs(ars.getNeType(), ars.getNeVersion());
        if (null != existArs) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        try {
            ARS savedArs = arsCreator.generateAndSave(ars);
            arsExporter.export(ars);
            return new ResponseEntity<>(savedArs, HttpStatus.OK);
        } catch (Exception e) {
            LOG.severe("Exception while generating ARS: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/api/om/{id}")
    public ObjectModelSpec getObjectModelSpec(@PathVariable("id") String id) {
        return arsService.findOm(id);
    }

    @GetMapping("/api/pmdl/{id}")
    public PmDataLoadSpec getPmDataLoadSpec(@PathVariable("id") String id) { return arsService.findPmDL(id); }

    @GetMapping("/api/counter/{id}")
    public CounterSpec getCounterSpec(@PathVariable("id") String id) { return arsService.findCounter(id); }

    @GetMapping("/api/alarm/{id}")
    public AlarmSpec getAlarmSpec(@PathVariable("id") String id) { return arsService.findAlarm(id); }

    @GetMapping("/api/ars/{neType:.+}/{neVersion:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadArs(@PathVariable String neType, @PathVariable String neVersion) {
        String arsFilePath = null;
        try {
            arsFilePath = FileUtils.getArsFilePath(neType, neVersion);
        } catch (Exception e) {
            LOG.severe("Exception while getting ARS file path: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (null == arsFilePath) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Resource resource = null;
        try {
            Path path = Paths.get(arsFilePath);
            resource = new UrlResource(path.toUri());
        } catch (Exception e) {
            LOG.severe("Exception while creating ars resource: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (null == resource) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
