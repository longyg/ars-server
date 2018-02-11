package com.longyg.backend.ars.generator;

import com.longyg.backend.adaptation.main.AdaptationRepository;
import com.longyg.backend.adaptation.pm.PmDataLoadRepository;
import com.longyg.frontend.model.ars.ARS;
import com.longyg.frontend.model.ars.ArsConfig;
import com.longyg.frontend.model.ars.om.ObjectClassInfo;
import com.longyg.frontend.model.ars.om.ObjectModelSpec;
import com.longyg.frontend.model.ars.pm.ArsMeasurement;
import com.longyg.frontend.model.ars.pm.MeasurementInfo;
import com.longyg.frontend.model.ars.pm.PmDataLoadSpec;
import com.longyg.frontend.model.ne.ReleaseConfig;
import com.longyg.frontend.service.ArsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class PmDataLoadGenerator {
    private static final Logger LOG = Logger.getLogger(PmDataLoadGenerator.class.getName());
    @Autowired
    private ArsService arsService;

//    private ReleaseConfig config;

    private ARS ars;

    private AdaptationRepository adaptationRepository;

    private PmDataLoadRepository pmDataLoadRepository;

    private ObjectModelSpec om;

    public String generateAndSave(ARS ars, AdaptationRepository adaptationRepository, ObjectModelSpec om) throws Exception {
//        this.config = config;
        this.ars = ars;
        this.adaptationRepository = adaptationRepository;
        this.om = om;

        initRepository();

        PmDataLoadSpec spec = generateAndSave();
        return spec.getId();
    }

    private PmDataLoadSpec generateAndSave() throws Exception {
        PmDataLoadSpec spec = new PmDataLoadSpec();
        spec.setNeType(this.ars.getNeType());
        spec.setNeVersion(this.ars.getNeVersion());

        for (Map.Entry<String, List<MeasurementInfo>> entry : pmDataLoadRepository.getAllReleaseMeasurements().entrySet()) {
            String adaptationId = entry.getKey();
            List<MeasurementInfo> miList = entry.getValue();

            addArsMeasurements(spec, adaptationId, miList);
        }

        for (List<ArsMeasurement> measLit : spec.getMeasurementMap().values()) {
            Collections.sort(measLit);
        }

        spec = arsService.savePmDataLoad(spec);

        return spec;
    }

    private void addArsMeasurements(PmDataLoadSpec spec, String adaptationId, List<MeasurementInfo> miList) throws Exception{
        for (MeasurementInfo mi : miList) {
            ArsMeasurement meas = createArsMeasurement(mi);
            spec.addMeasurement(adaptationId, meas);
        }
    }

    private ArsMeasurement createArsMeasurement(MeasurementInfo mi) throws Exception {
        ArsMeasurement meas = new ArsMeasurement();
        meas.setName(mi.getName());
        meas.setNameInOmes(mi.getNameInOmes());
        meas.setMeasuredObject(mi.getMeasuredObject());
        meas.setSupported(mi.getSupportedVersions().contains(this.ars.getNeVersion()));
        meas.setSupportedPreviousVersions(getOtherVersions(mi));
        meas.setDimensions(mi.getDimensions());

        ObjectClassInfo oci = getMeasuredOci(mi);
        if (null == oci) {
            throw new Exception("Can't find ObjectClassInfo for " + mi.getName());
        }
        meas.setAvgPerNet(oci.getAvgPerNet());
        meas.setMaxPerNet(oci.getMaxPerNet());
        meas.setMaxPerNe(oci.getMaxPerNE());
        meas.setCounterNumber(getCnOfVersion(mi, this.ars.getNeVersion()));
        meas.setCounterNumberOfLastVersion(getCnOfVersion(mi, this.ars.getLastNeVersion()));
        meas.setDelta(meas.getCounterNumber() - meas.getCounterNumberOfLastVersion());
        meas.setAggObject(meas.getMeasuredObject());
        meas.setMphPerNE(meas.getMaxPerNe() * meas.getActive() * (60 / meas.getMinimalInterval()));
        meas.setCphPerNE(meas.getMphPerNE() * meas.getCounterNumber());
        meas.setChaPerNE(meas.getCphPerNE() / (60 / meas.getMinimalInterval()));
        meas.setCdaPerNe(meas.getChaPerNE());
        meas.setMaxMph(meas.getMaxPerNet() * meas.getActive() * (60 / meas.getMinimalInterval()));
        meas.setMaxCph(meas.getMaxMph() * meas.getCounterNumber());
        meas.setDbRrPerNe(24 * meas.getMphPerNE() * meas.getStorageDays());
        meas.setDbRcPerNe(24 * meas.getCphPerNE() * meas.getStorageDays());
        meas.setMsPerNe(meas.getDbRcPerNe() * meas.getBytesPerCounter());
        meas.setDbMaxRows(24 * meas.getMaxMph() * meas.getStorageDays());
        meas.setDbMaxCtrs(24 * meas.getMaxCph() * meas.getStorageDays());
        meas.setMaxMs(meas.getDbMaxCtrs() * meas.getBytesPerCounter());
        meas.setTotalBytesPerInterval(meas.getMaxPerNet() * meas.getCounterNumber() * meas.getBytesPerCounter() * meas.getActive());

        BigDecimal d = new BigDecimal(1024 * 1024 * 1024);
        BigDecimal d1 = new BigDecimal(meas.getTotalBytesPerInterval() * (60 / meas.getMinimalInterval()));
        meas.setTotalSizePerHour(d1.divide(d));
        meas.setTableSizePerDay(meas.getTotalSizePerHour().multiply(new BigDecimal(24)));

        return meas;
    }

    private int getCnOfVersion(MeasurementInfo mi, String version) {
        if (null != version) {
            return mi.getAllReleaseCounters().get(version).size();
        }
        return 0;
    }

    private ObjectClassInfo getMeasuredOci(MeasurementInfo mi) {
        List<ObjectClassInfo> ociList = om.getOciList(mi.getAdaptationId());
        String hierarchy = mi.getMeasuredObject();
        for (ObjectClassInfo oci : ociList) {
            if (oci.getOriginalDn() == null) {
                continue;
            }
            if (oci.getOriginalDn().equals(hierarchy)) {
                return oci;
            }
        }
        return null;
    }

    private List<String> getOtherVersions(MeasurementInfo mi) {
        List<String> otherVerions = new ArrayList<>();
        for (String version : mi.getSupportedVersions()) {
            if (!version.equals(this.ars.getNeVersion())) {
                otherVerions.add(version);
            }
        }
        return otherVerions;
    }

    private void initRepository() {
        pmDataLoadRepository = new PmDataLoadRepository(ars, adaptationRepository);
        pmDataLoadRepository.init();
    }

    public PmDataLoadRepository getPmDataLoadRepository() {
        return pmDataLoadRepository;
    }
}
