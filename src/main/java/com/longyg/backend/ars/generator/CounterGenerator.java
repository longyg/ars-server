package com.longyg.backend.ars.generator;

import com.longyg.backend.adaptation.pm.Counter;
import com.longyg.backend.adaptation.pm.PmDataLoadRepository;
import com.longyg.frontend.model.ars.ArsConfig;
import com.longyg.frontend.model.ars.counter.ArsCounter;
import com.longyg.frontend.model.ars.counter.CounterMeas;
import com.longyg.frontend.model.ars.counter.CounterSpec;
import com.longyg.frontend.model.ars.pm.MeasurementInfo;
import com.longyg.frontend.model.ne.ReleaseConfig;
import com.longyg.frontend.service.ArsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class CounterGenerator {
    @Autowired
    private ArsService arsService;

    private ReleaseConfig config;

    private PmDataLoadRepository pmDataLoadRepository;

    public String generateAndSave(ReleaseConfig config, PmDataLoadRepository pmDataLoadRepository) {
        this.config = config;
        this.pmDataLoadRepository = pmDataLoadRepository;

        CounterSpec spec = generateAndSave();
        return spec.getId();
    }

    private CounterSpec generateAndSave() {
        CounterSpec spec = new CounterSpec();
        spec.setNeType(config.getNeType());
        spec.setNeVersion(config.getNeVersion());

        for (Map.Entry<String, List<MeasurementInfo>> entry : pmDataLoadRepository.getAllReleaseMeasurements().entrySet()) {
            String adaptationId = entry.getKey();
            List<MeasurementInfo> miList = entry.getValue();

            addCounterMeas(spec, adaptationId, miList);
        }

        for (List<CounterMeas> measLit : spec.getMeasurementMap().values()) {
            Collections.sort(measLit);

            for (CounterMeas meas : measLit) {
                Collections.sort(meas.getCounters());
            }
        }

        spec = arsService.saveCounter(spec);

        return spec;
    }

    private void addCounterMeas(CounterSpec spec, String adaptationId, List<MeasurementInfo> miList) {
        for (MeasurementInfo mi : miList) {
            CounterMeas meas = createCounterMeas(mi);
            spec.addMeasurement(adaptationId, meas);
        }
    }

    private CounterMeas createCounterMeas(MeasurementInfo mi) {
        CounterMeas meas = new CounterMeas();
        meas.setName(mi.getName());

        createArsCountersForCurrentVersion(meas, mi);

        Map<String, List<Counter>> counterMap = mi.getAllReleaseCounters();
        for (Map.Entry<String, List<Counter>> entry : counterMap.entrySet()) {
            String v = entry.getKey();
            for (Counter counter : entry.getValue()) {
                addArsCounter(meas, counter, v);
            }
        }

        return meas;
    }

    private void createArsCountersForCurrentVersion(CounterMeas meas, MeasurementInfo mi) {
        Map<String, List<Counter>> counterMap = mi.getAllReleaseCounters();
        for (Map.Entry<String, List<Counter>> entry : counterMap.entrySet()) {
            String v = entry.getKey();
            if (config.getNeVersion().equals(v)) {
                for (Counter counter : entry.getValue()) {
                    ArsCounter c = findOrCreateArsCounter(meas, counter);
                    c.setSupported(true);
                    meas.addCounter(c);
                }
            }
        }
    }

    private void addArsCounter(CounterMeas meas, Counter counter, String version) {
        ArsCounter c = findOrCreateArsCounter(meas, counter);
        if (!version.equals(config.getNeVersion())) {
            c.addSupportedPreviousVersion(version);
        }
        meas.addCounter(c);
    }

    private ArsCounter findOrCreateArsCounter(CounterMeas meas, Counter counter) {
        ArsCounter c = findArsCounter(meas, counter.getName());
        if (null == c) {
            c = createArsCounter(counter);
        }
        return c;
    }
    private ArsCounter createArsCounter(Counter counter) {
        ArsCounter c = new ArsCounter();
        c.setName(counter.getName());
        c.setAggRule(counter.getAggRule());
        c.setDescription(counter.getDescription());
        c.setPresentation(counter.getPresentation());
        c.setUnit(counter.getUnit());
        return c;
    }

    private ArsCounter findArsCounter(CounterMeas meas, String name) {
        for (ArsCounter c : meas.getCounters()) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }
}
