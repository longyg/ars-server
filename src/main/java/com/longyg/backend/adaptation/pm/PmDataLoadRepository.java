package com.longyg.backend.adaptation.pm;

import com.longyg.backend.adaptation.main.AdaptationRepository;
import com.longyg.frontend.model.ars.ARS;
import com.longyg.frontend.model.ars.ArsConfig;
import com.longyg.frontend.model.ars.pm.MeasurementInfo;
import com.longyg.frontend.model.ne.ReleaseConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PmDataLoadRepository {
    // key: adaptation id
    // value: measurement list
    private Map<String, List<MeasurementInfo>> allReleaseMeasurements = new HashMap<>();

    private ARS ars;

    private AdaptationRepository adaptationRepository;

    public Map<String, List<MeasurementInfo>> getAllReleaseMeasurements() {
        return allReleaseMeasurements;
    }

    public PmDataLoadRepository(ARS ars, AdaptationRepository adaptationRepository) {
        this.ars = ars;
        this.adaptationRepository = adaptationRepository;
    }

    public void init() {
        createMisForVersion(this.ars.getNeVersion());
        createMisForVersion(this.ars.getLastNeVersion());

        Map<String, List<PmAdaptation>> pmAdaptationsMap = adaptationRepository.getPmAdaptations();
        for (Map.Entry<String, List<PmAdaptation>> entry : pmAdaptationsMap.entrySet())
        {
            String adaptationId = entry.getKey();
            List<PmAdaptation> pmAdaptations = entry.getValue();

            for (PmAdaptation pmAdaptation : pmAdaptations)
            {
                for (Measurement measurement : pmAdaptation.getMeasurements())
                {
                    createMi(adaptationId, measurement, pmAdaptation.getAdapRelease());
                }
            }
        }
    }

    private void createMisForVersion(String version) {
        Map<String, List<PmAdaptation>> pmAdaptationsMap = adaptationRepository.getPmAdaptations();
        for (Map.Entry<String, List<PmAdaptation>> entry : pmAdaptationsMap.entrySet()) {
            String adaptationId = entry.getKey();
            List<PmAdaptation> pmAdaptations = entry.getValue();

            for (PmAdaptation pmAdaptation : pmAdaptations) {
                if (pmAdaptation.getAdapRelease().equals(version)) {
                    for (Measurement measurement : pmAdaptation.getMeasurements()) {
                        createMi(adaptationId, measurement, pmAdaptation.getAdapRelease());
                    }
                }
            }
        }
    }

    private void createMi(String adaptationId, Measurement measurement, String version) {
        MeasurementInfo mi = findOrCreateMi(adaptationId, measurement, version);
        addToRepository(adaptationId, mi);
    }

    private MeasurementInfo findOrCreateMi(String adaptationId, Measurement measurement, String version) {
        MeasurementInfo mi = getMi(adaptationId, measurement.getName());
        if (null == mi) {
            mi = new MeasurementInfo();
            mi.setName(measurement.getName());
            mi.setNameInOmes(measurement.getNameInOmes());
            mi.setAdaptationId(adaptationId);
            mi.setMeasurement(measurement);
            // TODO

        }

        mi.addSupportedVersion(version);
        mi.addCounters(version, measurement.getCounters());

        return mi;
    }

    private void addToRepository(String adaptationId, MeasurementInfo mi) {
        if (allReleaseMeasurements.containsKey(adaptationId)) {
            List<MeasurementInfo> miList = allReleaseMeasurements.get(adaptationId);
            if (!miList.contains(mi)) {
                miList.add(mi);
            }
        } else {
            List<MeasurementInfo> miList = new ArrayList<>();
            miList.add(mi);
            allReleaseMeasurements.put(adaptationId, miList);
        }
    }

    private MeasurementInfo getMi(String adaptationId, String name) {
        if (allReleaseMeasurements.containsKey(adaptationId)) {
            for (MeasurementInfo mi : allReleaseMeasurements.get(adaptationId)) {
                if (mi.getName().equals(name)) {
                    return mi;
                }
            }
        }
        return null;
    }
}
