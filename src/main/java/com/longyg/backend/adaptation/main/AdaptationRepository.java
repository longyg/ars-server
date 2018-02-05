package com.longyg.backend.adaptation.main;

import com.longyg.backend.adaptation.fm.FmAdaptation;
import com.longyg.backend.adaptation.pm.PmAdaptation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdaptationRepository {
    // key: adaptation id
    // value: adaptation list of same adaptation id, with different release
    private Map<String, List<PmAdaptation>> pmAdaptations = new HashMap<>();
    private Map<String, List<FmAdaptation>> fmAdaptations = new HashMap<>();

    public Map<String, List<PmAdaptation>> getPmAdaptations() {
        return pmAdaptations;
    }

    public void addPmAdaptation(String adaptationId, PmAdaptation pmAdaptation) {
        if (pmAdaptations.containsKey(adaptationId)) {
            List<PmAdaptation> adapList = pmAdaptations.get(adaptationId);
            if (!adapList.contains(pmAdaptation)) {
                adapList.add(pmAdaptation);
            }
        } else {
            List<PmAdaptation> adapList = new ArrayList<>();
            adapList.add(pmAdaptation);
            pmAdaptations.put(adaptationId, adapList);
        }
    }

    public Map<String, List<FmAdaptation>> getFmAdaptations() {
        return fmAdaptations;
    }

    public void addFmAdaptation(String adaptationId, FmAdaptation fmAdaptation) {
        if (fmAdaptations.containsKey(adaptationId)) {
            List<FmAdaptation> adapList = fmAdaptations.get(adaptationId);
            if (!adapList.contains(fmAdaptation)) {
                adapList.add(fmAdaptation);
            }
        } else {
            List<FmAdaptation> adapList = new ArrayList<>();
            adapList.add(fmAdaptation);
            fmAdaptations.put(adaptationId, adapList);
        }
    }
}
