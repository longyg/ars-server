package com.longyg.backend.adaptation.svn;

import com.longyg.backend.adaptation.config.ConfigRepository;
import org.apache.log4j.Logger;
import org.tmatesoft.svn.core.*;
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

/**
 * Created by ylong on 2/9/2017.
 */
public class SvnUtil {
    private static final Logger LOG = Logger.getLogger(SvnUtil.class);
    private SVNRepository repository = null;

    public void initSVN() throws Exception {
        setupLibrary();

        String rootUrl = ConfigRepository.getInstance().getSvnInfo().getSvnRoot();
        String username = ConfigRepository.getInstance().getSvnInfo().getUsername();
        String password = ConfigRepository.getInstance().getSvnInfo().getPassword();

        repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(rootUrl));

        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(username, password);
        repository.setAuthenticationManager(authManager);
    }

    public void downloadFile(String filePath, File outFile) throws Exception {
        SVNProperties fileProperties = new SVNProperties();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

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

        } catch (SVNException svne) {
            LOG.error("Error while fetching the file contents and properties: " + svne.getMessage());
            throw new Exception("Error while fetching the file contents and properties: ", svne);
        }
    }

    /**
    public void downloadFiles(String filePath, File outDir) throws Exception {
        SVNProperties fileProperties = new SVNProperties();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            SVNNodeKind nodeKind = repository.checkPath(filePath, -1);

            if (nodeKind == SVNNodeKind.NONE) {
                LOG.error("There is no entry at '" + filePath + "' in SVN");
                throw new Exception("There is no entry at '" + filePath + "' in SVN");
            } else if (nodeKind == SVNNodeKind.DIR) {
                LOG.debug("Valid svn file path: " + filePath);
            } else if (nodeKind == SVNNodeKind.FILE){
                LOG.error("The entry at '" + filePath
                        + "' is a file while a directory was expected.");
                throw new Exception("The entry at '" + filePath + "' is a file while a directory was expected.");
            }

            Collection dirEntries = null;
            Collection<SVNDirEntry> entries = repository.getDir(filePath, -1, fileProperties, dirEntries);
            for (SVNDirEntry entry : entries) {
                entry.getURL()
            }

            baos.writeTo(new FileOutputStream(outFile));

            LOG.info("downloaded file " + outFile.getAbsolutePath());

        } catch (SVNException svne) {
            LOG.error("Error while fetching the file contents and properties: " + svne.getMessage());
            throw new Exception("Error while fetching the file contents and properties: ", svne);
        }
    }
    **/

    /*
     * Initializes the library to work with a repository via
     * different protocols.
     */
    private static void setupLibrary() {
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
}
