package com.longyg.backend.adaptation.main;

import com.longyg.backend.adaptation.config.ConfigRepository;
import com.longyg.backend.adaptation.svn.SvnUtil;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ylong on 2/9/2017.
 */
public class ResourceRepository {
    private static final Logger LOG = Logger.getLogger(ResourceRepository.class);
    private static final String ROOT_DOWNLOAD_FOLDER = "resources";

    private static List<Resource> resources = new ArrayList<>();
    private static ConfigRepository configRepository = ConfigRepository.getInstance();

    public static List<Resource> getResources() {
        return resources;
    }

    public static void initialize() throws Exception {
        if (configRepository.getType().equals(ConfigRepository.TYPE_LOCAL)) {
            initializeFromLocal();
        } else if (configRepository.getType().equals(ConfigRepository.TYPE_SVN)) {
            initializeFromSVN();
        }
    }

    private static void initializeFromSVN() throws Exception {
        try {
            SvnUtil svnUtil = new SvnUtil();
            svnUtil.initSVN();

            List<String> list = configRepository.getResoures();
            for (String filePath : list) {
                LOG.debug(filePath);
                File outFile = createDownloadOutputFile(filePath);
                if (outFile.isFile() && outFile.exists() && !ConfigRepository.getInstance().isForceDownload()) {
                    LOG.debug(outFile.getAbsolutePath() + " is already downloaded.");
                } else {
                    svnUtil.downloadFile(filePath, outFile);
                    if (!outFile.exists()) {
                        LOG.error("Download failed for: " + filePath);
                    }
                }

                Resource resource = new Resource();
                resource.setPath(filePath);
                resource.setInputStream(new FileInputStream(outFile));

                resources.add(resource);
            }
        } catch (Exception e) {
            LOG.error("Exception while initialize SVN resources", e);
            throw new Exception("Exception while initialize SVN resources", e);
        }
    }

    private static void initializeFromLocal() throws Exception {
        String root = configRepository.getRoot();
        List<String> list = configRepository.getResoures();
        try {
            for (String r : list) {
                String path = root + File.separator + r;
                File file = new File(path);
                if (file.exists() && file.isFile()) {
                    Resource resource = new Resource();
                    resource.setPath(path);
                    resource.setInputStream(new FileInputStream(file));
                    resources.add(resource);
                } else {
                    LOG.error("File is invalid: " + path);
                }
            }
        } catch (Exception e) {
            LOG.error("Exception while initialize local resources", e);
            throw new Exception("Exception while initialize local resources", e);
        }
    }

    private static File createDownloadOutputFile(String filePath) throws Exception {
        String path = filePath.substring(0, filePath.lastIndexOf("/"));
        String filename = filePath.substring(filePath.lastIndexOf("/"));
        String outDirPath = ROOT_DOWNLOAD_FOLDER + File.separator + path;
        File outDir = new File(outDirPath);

        if (outDir.exists() && !outDir.isDirectory()) {
            LOG.error(outDir.getAbsolutePath() + " is not a directory");
            throw new Exception(outDir.getAbsolutePath() + " is not a directory");
        } else if (!outDir.exists()) {
            outDir.mkdirs();
        }
        return new File(outDirPath + File.separator + filename);
    }
}
