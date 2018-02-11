package com.longyg.backend.ars.generator;

import com.longyg.backend.adaptation.main.AdaptationRepository;
import com.longyg.backend.adaptation.main.AdaptationResourceParser;
import com.longyg.backend.adaptation.svn.SvnDownloader;
import com.longyg.frontend.model.ars.ARS;
import com.longyg.frontend.model.ars.om.ObjectModelSpec;
import com.longyg.frontend.model.ne.Adaptation;
import com.longyg.frontend.model.ne.ReleaseConfig;
import com.longyg.frontend.service.ArsService;
import com.longyg.frontend.service.ConfigService;
import com.longyg.frontend.service.NeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Component
public class ArsCreator {
    private static final Logger LOG = Logger.getLogger(ArsCreator.class.getName());
    private static final String ROOT_DOWNLOAD_FOLDER = "resources";

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

        ObjectModelSpec om = omGenerator.generateAndSave(config, adaptationRepository);
        this.ars.setObjectModel(om.getId());

        String pmDlId = pmDataLoadGenerator.generateAndSave(ars, adaptationRepository, om);
        this.ars.setPmDataLoad(pmDlId);

        String counterId = counterGenerator.generateAndSave(config, pmDataLoadGenerator.getPmDataLoadRepository());
        this.ars.setCounter(counterId);

        String alarmId = alarmGenerator.generateAndSave(ars, adaptationRepository);
        this.ars.setAlarm(alarmId);

        /**
        String usId = usGenerator.generateAndSave(config);
         **/

        return arsService.saveArs(this.ars);
    }

    private void initAdaptationRepository() throws Exception {
        List<String> resourceList = resolveAdaptationResources();
        adaptationRepository = new AdaptationRepository();
        resourceParser.parse(adaptationRepository, resourceList);
    }

    private List<String> resolveAdaptationResources() throws Exception {
        List<String> resourceList = new ArrayList<>();
        downResource(this.config, resourceList);

        ReleaseConfig lastConfig = neService.findReleaseConfig(this.ars.getNeType(), this.ars.getLastNeVersion());
        downResource(lastConfig, resourceList);

        for (String olderVersion: this.ars.getOlderNeVersions()) {
            ReleaseConfig olderConfig = neService.findReleaseConfig(this.ars.getNeType(), olderVersion);
            downResource(olderConfig, resourceList);
        }
        return resourceList;
    }

    private void downResource(ReleaseConfig config, List<String> resourceList) throws Exception {
        if (null != config) {
            for (String id : config.getAdaptations()) {
                Adaptation adaptation = neService.findAdaptation(id);
                String sourcePath = downloadAdaptation(config, adaptation);
                resourceList.add(sourcePath);
            }
        }
    }

    private String downloadAdaptation(ReleaseConfig config, Adaptation adaptation) throws Exception {
        URL url = new URL(adaptation.getSourcePath());
        String host = url.getProtocol() + "://" + url.getHost();
        String port = (url.getPort() == -1) ? "" : ":" + url.getPort();
        String sourcePath = adaptation.getSourcePath();
        String rootUrl = sourcePath.substring(0, sourcePath.lastIndexOf("/") + 1);
        String path = url.getPath();
        String filename = path.substring(path.lastIndexOf("/") + 1);
        String localPath = ROOT_DOWNLOAD_FOLDER + File.separator +
                config.getNeType() + File.separator +
                config.getNeVersion() + File.separator +
                adaptation.getAdaptationId() + File.separator +
                adaptation.getAdaptationRelease();

        File localFile = new File(localPath + File.separator + filename);
        boolean needDownload = false;
        if (localFile.exists() && localFile.isFile()) {
            if (this.ars.isForceDownload()) {
                needDownload = true;
            }
        } else {
            needDownload = true;
        }
        if (needDownload) {
            SvnDownloader downloader = new SvnDownloader();
            return downloader.download(rootUrl, "ylong", "$Leo2222*", filename, localPath, filename);
        } else {
            return localFile.getAbsolutePath();
        }
    }
}
