package com.longyg.frontend.service;

import com.longyg.backend.adaptation.pm.PmDataLoadRepository;
import com.longyg.frontend.model.ars.ARS;
import com.longyg.frontend.model.ars.ArsConfig;
import com.longyg.frontend.model.ars.ArsConfigRepository;
import com.longyg.frontend.model.ars.ArsRepository;
import com.longyg.frontend.model.ars.alarm.AlarmRepository;
import com.longyg.frontend.model.ars.alarm.AlarmSpec;
import com.longyg.frontend.model.ars.counter.CounterRepository;
import com.longyg.frontend.model.ars.counter.CounterSpec;
import com.longyg.frontend.model.ars.om.ObjectModelSpec;
import com.longyg.frontend.model.ars.om.OmRepository;
import com.longyg.frontend.model.ars.pm.PmDataLoadSpec;
import com.longyg.frontend.model.ars.pm.PmRepository;
import com.longyg.frontend.model.ars.us.UsRepository;
import com.longyg.frontend.model.ars.us.UserStorySpec;
import com.longyg.frontend.model.ne.NeRelease;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ArsService {
    @Autowired
    private ArsConfigRepository arsConfigRepository;

    @Autowired
    private UsRepository usRepository;

    @Autowired
    private OmRepository omRepository;

    @Autowired
    private ArsRepository arsRepository;

    @Autowired
    private PmRepository pmRepository;

    @Autowired
    private CounterRepository counterRepository;

    @Autowired
    private AlarmRepository alarmRepository;

    // ARS services
    public List<ARS> findAllArses() {
        return arsRepository.findAll();
    }

    public ARS findArs(String neType, String neVersion) {
        return arsRepository.findByNeTypeAndRelease(neType, neVersion);
    }

    public ARS saveArs(ARS ars) {
        return arsRepository.save(ars);
    }

    public void deleteArses(List<String> ids) {
        if (null == ids || ids.size() < 1) {
            return;
        }

        for (String id : ids) {
            Optional<ARS> opt = arsRepository.findById(id);
            if (opt.isPresent()) {
                ARS ars = opt.get();
                if (ars.getObjectModel() != null) {
                    omRepository.deleteById(ars.getObjectModel());
                }
                if (ars.getPmDataLoad() != null) {
                    pmRepository.deleteById(ars.getPmDataLoad());
                }
                if (ars.getCounter() != null) {
                    counterRepository.deleteById(ars.getCounter());
                }
                if (ars.getAlarm() != null) {
                    alarmRepository.deleteById(ars.getAlarm());
                }
            }
        }

        arsRepository.deleteByIdIn(ids);
    }

    // ARS Config Services
    public ArsConfig findArsConfig(NeRelease neRelease) {
        if (null == neRelease) {
            return null;
        }
        return arsConfigRepository.findByNeTypeAndRelease(neRelease.getType(), neRelease.getVersion());
    }

    public ArsConfig saveConfig(ArsConfig arsConfig) {
        return arsConfigRepository.save(arsConfig);
    }

    // User Story Services
    public UserStorySpec saveUs(UserStorySpec usSpec) {
        return usRepository.save(usSpec);
    }

    // Object Model Services
    public ObjectModelSpec saveObjectModel(ObjectModelSpec omSpec) {
        return omRepository.save(omSpec);
    }

    public ObjectModelSpec findOm(String id) {
        if (null == id) {
            return null;
        }
        Optional<ObjectModelSpec> opt = omRepository.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        }
        return null;
    }

    // PM Data Load Services
    public PmDataLoadSpec savePmDataLoad(PmDataLoadSpec spec) {
        return pmRepository.save(spec);
    }

    public PmDataLoadSpec findPmDL(String id) {
        if (null == id) {
            return null;
        }
        Optional<PmDataLoadSpec> opt = pmRepository.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        }
        return null;
    }

    // Counter Services
    public CounterSpec saveCounter(CounterSpec spec) {
        return counterRepository.save(spec);
    }

    public CounterSpec findCounter(String id) {
        if (null == id) {
            return null;
        }
        Optional<CounterSpec> opt = counterRepository.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        }
        return null;
    }

    // Alarm Services
    public AlarmSpec saveAlarm(AlarmSpec spec) {
        return alarmRepository.save(spec);
    }

    public AlarmSpec findAlarm(String id) {
        if (null == id) {
            return null;
        }
        Optional<AlarmSpec> opt = alarmRepository.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        }
        return null;
    }
}
