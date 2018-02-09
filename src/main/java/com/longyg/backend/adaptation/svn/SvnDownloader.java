package com.longyg.backend.adaptation.svn;

import com.longyg.backend.adaptation.config.ConfigRepository;
import com.longyg.frontend.model.config.AdaptationResource;
import org.apache.log4j.Logger;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class SvnDownloader {
    private static final Logger LOG = Logger.getLogger(SvnDownloader.class);
    private static final String ROOT_DOWNLOAD_FOLDER = "resources";

    private SVNRepository repository = null;
    private AdaptationResource src;

    public void download(AdaptationResource src) throws Exception {
        this.src = src;

        setupLibrary();

        String rootUrl = src.getSvnRoot();
        String username = src.getSvnUser();
        String password = src.getSvnPassword();

        repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(rootUrl));

        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(username, password);
        repository.setAuthenticationManager(authManager);

        SVNProperties fileProperties = new SVNProperties();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        String filePath = src.getSourcePath();
        File outFile = createDownloadOutputFile(filePath);

        try {
            SVNNodeKind nodeKind = repository.checkPath(filePath, -1);

            if (nodeKind == SVNNodeKind.NONE) {
                LOG.error("There is no entry at '" + filePath + "' in SVN");
                throw new Exception("There is no entry at '" + filePath + "' in SVN");
            } else if (nodeKind == SVNNodeKind.DIR) {
                LOG.error("The entry at '" + filePath
                        + "' is a directory while a file was expected.");
                throw new Exception("The entry at '" + filePath + "' is a directory while a file was expected.");
            } else if (nodeKind == SVNNodeKind.FILE){
                LOG.debug("Valid svn file path: " + filePath);
            }
            /*
             * Gets the contents and properties of the file located at filePath
             * in the repository at the latest revision (which is meant by a
             * negative revision number).
             */
            repository.getFile(filePath, -1, fileProperties, baos);

            baos.writeTo(new FileOutputStream(outFile));

            LOG.info("downloaded file " + outFile.getAbsolutePath());

            src.setLocalPath(outFile.getAbsolutePath());

        } catch (SVNException svne) {
            LOG.error("Error while fetching the file contents and properties: " + svne.getMessage());
            throw new Exception("Error while fetching the file contents and properties: ", svne);
        }
    }

    private void setupLibrary() {
        /*
         * For using over http:// and https://
         */
        DAVRepositoryFactory.setup();
        /*
         * For using over svn:// and svn+xxx://
         */
        SVNRepositoryFactoryImpl.setup();

        /*
         * For using over file:///
         */
        FSRepositoryFactory.setup();
    }

    private File createDownloadOutputFile(String filePath) throws Exception {
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

    public String download(String rootUrl, String username, String password, String filePath, String localPath, String filename) throws Exception {
        setupLibrary();

        repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(rootUrl));

        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(username, password);
        repository.setAuthenticationManager(authManager);

        SVNProperties fileProperties = new SVNProperties();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        File outFile = createDownloadOutputFile(localPath, filename);

        try {
            SVNNodeKind nodeKind = repository.checkPath(filePath, -1);

            if (nodeKind == SVNNodeKind.NONE) {
                LOG.error("There is no entry at '" + filePath + "' in SVN: " + rootUrl);
                throw new Exception("There is no entry at '" + filePath + "' in SVN");
            } else if (nodeKind == SVNNodeKind.DIR) {
                LOG.error("The entry at '" + filePath
                        + "' is a directory while a file was expected.");
                throw new Exception("The entry at '" + filePath + "' is a directory while a file was expected.");
            } else if (nodeKind == SVNNodeKind.FILE){
                LOG.debug("Valid svn file path: " + filePath);
            }
            /*
             * Gets the contents and properties of the file located at filePath
             * in the repository at the latest revision (which is meant by a
             * negative revision number).
             */
            repository.getFile(filePath, -1, fileProperties, baos);

            baos.writeTo(new FileOutputStream(outFile));

            LOG.info("downloaded file " + outFile.getAbsolutePath());

            return outFile.getAbsolutePath();
        } catch (SVNException svne) {
            LOG.error("Error while fetching the file contents and properties: " + svne.getMessage());
            throw new Exception("Error while fetching the file contents and properties: ", svne);
        }
    }

    private File createDownloadOutputFile(String path, String filename) throws Exception {
        File outDir = new File(path);
        if (outDir.exists() && !outDir.isDirectory()) {
            LOG.error(outDir.getAbsolutePath() + " is not a directory");
            throw new Exception(outDir.getAbsolutePath() + " is not a directory");
        } else if (!outDir.exists()) {
            outDir.mkdirs();
        }
        return new File(path + File.separator + filename);
    }
}
