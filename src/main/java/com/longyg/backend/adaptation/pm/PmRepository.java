package com.longyg.backend.adaptation.pm;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ylong on 2/14/2017.
 */
public class PmRepository {
    private static Map<String, PmAdaptation> pmAdaptations = new HashMap<>();

    public static Map<String, PmAdaptation> getPmAdaptations() {
        return pmAdaptations;
    }

    public static void addPmAdaptation(String version, PmAdaptation pmAdaptation) {
        if (!pmAdaptations.containsKey(version)) {
            pmAdaptations.put(version, pmAdaptation);
        }
    }
}
