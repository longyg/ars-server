package com.longyg.frontend.controller;

import com.longyg.backend.ars.generator.ArsCreator;
import com.longyg.frontend.model.ars.ARS;
import com.longyg.frontend.service.ArsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ArsRestController {
    @Autowired
    private ArsService arsService;

    @Autowired
    private ArsCreator arsCreator;

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
            return new ResponseEntity<>(savedArs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}