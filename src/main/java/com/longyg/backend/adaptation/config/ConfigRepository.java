package com.longyg.backend.adaptation.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ylong on 2/9/2017.
 */
public class ConfigRepository {
    public static final String TYPE_SVN = "svn";
    public static final String TYPE_LOCAL = "local";

    private String type;

    private SvnInfo svnInfo;

    private String root;

    private String outputRoot;

    private String neVersion;

    private String lastVersion;

    private boolean isForceDownload = true;

    private List<String> resoures = new ArrayList<String>();

    private String loadPropPath;

    private OmesGenerateConfig omesConfig;

    public static ConfigRepository INSTANCE;

    public static ConfigRepository getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new ConfigRepository();
        }
        return INSTANCE;
    }

    public String getResultDir() {
        String outRootPath = ConfigRepository.getInstance().getOutputRoot();
        String outDirPath = outRootPath + File.separator + ConfigRepository.getInstance().getNeVersion();
        File outDir = new File(outDirPath);
        if (!outDir.exists()) {
            outDir.mkdirs();
        }
        return outDir.getAbsolutePath();
    }

    public String getLastVersion() {
        return lastVersion;
    }

    public void setLastVersion(String lastVersion) {
        this.lastVersion = lastVersion;
    }

    public boolean isForceDownload() {
        return isForceDownload;
    }

    public void setForceDownload(boolean forceDownload) {
        isForceDownload = forceDownload;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SvnInfo getSvnInfo() {
        return svnInfo;
    }

    public void setSvnInfo(SvnInfo svnInfo) {
        this.svnInfo = svnInfo;
    }

    public List<String> getResoures() {
        return resoures;
    }

    public void setResoures(List<String> resoures) {
        this.resoures = resoures;
    }

    public String getOutputRoot() {
        return outputRoot;
    }

    public void setOutputRoot(String outputRoot) {
        this.outputRoot = outputRoot;
    }

    public String getNeVersion() {
        return neVersion;
    }

    public void setNeVersion(String neVersion) {
        this.neVersion = neVersion;
    }

    public String getLoadPropPath() {
        return loadPropPath;
    }

    public void setLoadPropPath(String loadPropPath) {
        this.loadPropPath = loadPropPath;
    }

    public OmesGenerateConfig getOmesConfig() {
        return omesConfig;
    }

    public void setOmesConfig(OmesGenerateConfig omesConfig) {
        this.omesConfig = omesConfig;
    }
}
