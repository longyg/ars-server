package com.longyg.backend.ars.generator;

import com.longyg.backend.adaptation.main.AdaptationRepository;
import com.longyg.backend.adaptation.main.AdaptationResourceParser;
import com.longyg.backend.adaptation.svn.SvnDownloader;
import com.longyg.frontend.model.ars.ARS;
import com.longyg.frontend.model.ars.om.ObjectModelSpec;
import com.longyg.frontend.model.config.AdaptationResource;
import com.longyg.frontend.model.config.InterfaceObject;
import com.longyg.frontend.model.ne.Adaptation;
import com.longyg.frontend.model.ne.ReleaseConfig;
import com.longyg.frontend.service.ArsService;
import com.longyg.frontend.service.ConfigService;
import com.longyg.frontend.service.NeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Component
public class ArsCreator {
    private static final Logger LOG = Logger.getLogger(ArsCreator.class.getName());

    @Autowired
    private ArsService arsService;

    @Autowired
    private NeService neService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private UsGenerator usGenerator;

    @Autowired
    private ObjectModelGenerator omGenerator;

    @Autowired
    private PmDataLoadGenerator pmDataLoadGenerator;

    @Autowired
    private CounterGenerator counterGenerator;

    @Autowired
    private AlarmGenerator alarmGenerator;

    @Autowired
    private AdaptationResourceParser resourceParser;

    private AdaptationRepository adaptationRepository;
    private ARS ars;
    private ReleaseConfig config;

    public ARS generateAndSave(ARS ars) throws Exception {
        this.ars = ars;
        this.config = neService.findReleaseConfig(this.ars.getNeType(), this.ars.getNeVersion());
        if (null == config) {
            throw new Exception("Release configuration is not defined for: " + this.ars.getNeType() + "-" + this.ars.getNeVersion());
        }

        initAdaptationRepository();

        String usId = usGenerator.generateAndSave(config);
        ObjectModelSpec om = omGenerator.generateAndSave(config, adaptationRepository);
        String pmDlId = pmDataLoadGenerator.generateAndSave(config, adaptationRepository, om);
        String counterId = counterGenerator.generateAndSave(config, pmDataLoadGenerator.getPmDataLoadRepository());
        String alarmId = alarmGenerator.generateAndSave(config, adaptationRepository);

        this.ars.setUserStory(usId);
        this.ars.setObjectModel(om.getId());
        this.ars.setPmDataLoad(pmDlId);
        this.ars.setCounter(counterId);
        this.ars.setAlarm(alarmId);

        return arsService.saveArs(this.ars);
    }

    private void initAdaptationRepository() throws Exception {
        List<AdaptationResource> resourceList = new ArrayList<>();
        for (String srcId : config.getResources()) {
            AdaptationResource src = configService.findResource(srcId);
            downloadAndSave(src);

            resourceList.add(src);
        }
        adaptationRepository = new AdaptationRepository();
        resourceParser.parse(adaptationRepository, resourceList);
    }

    private List<Adaptation> resolveAdaptations() {
        List<Adaptation> adaptationList = new ArrayList<>();
        List<AdaptationResource> resources = new ArrayList<>();
        for (String id: this.config.getAdaptations()) {
            Adaptation adaptation = neService.findAdaptation(id);
            AdaptationResource src = new AdaptationResource();
            com.longyg.frontend.model.config.Adaptation adap = new com.longyg.frontend.model.config.Adaptation();
            adap.setId(adaptation.getAdaptationId());
            adap.setRelease(adaptation.getAdaptationRelease());
            src.setAdaptation(adap);
            src.setLocalPath(this.ars.getNeType() + File.separator + this.ars.getNeVersion() + File.separator + adaptation.getAdaptationId() + File.separator + adaptation.getAdaptationRelease());
            adaptationList.add(adaptation);
        }
        ReleaseConfig lastConfig = neService.findReleaseConfig(this.ars.getNeType(), this.ars.getLastNeVersion());
        if (null != lastConfig) {
            for (String id: lastConfig.getAdaptations()) {
                Adaptation adaptation = neService.findAdaptation(id);
                adaptationList.add(adaptation);
            }
        }
        for (String olderVersion: this.ars.getOlderNeVersions()) {
            ReleaseConfig olderConfig = neService.findReleaseConfig(this.ars.getNeType(), olderVersion);
            if (null != olderConfig) {
                for (String id: olderConfig.getAdaptations()) {
                    Adaptation adaptation = neService.findAdaptation(id);
                    adaptationList.add(adaptation);
                }
            }
        }
        return adaptationList;
    }

    private void downloadAdaptations(List<Adaptation> adaptationList) {

    }

    private List<InterfaceObject> getInterfaces() {
        List<InterfaceObject> list = new ArrayList<>();
        for (String ifId : config.getInterfaces()) {
            InterfaceObject ifo = configService.findInterface(ifId);
            if (null != ifo) {
                list.add(ifo);
            } else {
                LOG.severe("There is no interface object with id: " + ifId);
            }
        }
        return list;
    }

    private List<AdaptationResource> getResources() {
        List<AdaptationResource> resources = new ArrayList<>();

        for (String srcId : config.getResources()) {
            AdaptationResource resource = configService.findResource(srcId);
            if (null != resource) {
                resources.add(resource);
            }
        }
        return resources;
    }

    private void downloadAndSave(AdaptationResource src) throws Exception {
        // If the file already exists, no need to download
        if (src.getLocalPath() != null) {
            File localFile = new File(src.getLocalPath());
            if (localFile.exists())
                return;
        }

        SvnDownloader downloader = new SvnDownloader();
        downloader.download(src);

        // Save to DB for the local file path
        configService.saveResource(src);
    }
}
