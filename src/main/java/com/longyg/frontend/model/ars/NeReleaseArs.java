package com.longyg.frontend.model.ars;

import com.longyg.frontend.model.ne.NeRelease;

public class NeReleaseArs {
    private NeRelease neRelease;
    private ArsConfig arsConfig;
    private ARS ars;

    public NeRelease getNeRelease() {
        return neRelease;
    }

    public void setNeRelease(NeRelease neRelease) {
        this.neRelease = neRelease;
    }

    public ArsConfig getArsConfig() {
        return arsConfig;
    }

    public void setArsConfig(ArsConfig arsConfig) {
        this.arsConfig = arsConfig;
    }

    public ARS getArs() {
        return ars;
    }

    public void setArs(ARS ars) {
        this.ars = ars;
    }
}
