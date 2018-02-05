package com.longyg.backend.adaptation.fm;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ylong on 2/14/2017.
 */
public class FmRepository {

    private static Map<String, FmAdaptation> fmAdaptations = new HashMap<String, FmAdaptation>();

    public static Map<String, FmAdaptation> getFmAdaptations() {
        return fmAdaptations;
    }

    public static void addFmAdaptation(String version, FmAdaptation fmAdaptation) {
        if (!fmAdaptations.containsKey(version)) {
            fmAdaptations.put(version, fmAdaptation);
        }
    }
}
