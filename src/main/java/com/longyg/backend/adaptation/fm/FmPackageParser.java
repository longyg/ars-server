package com.longyg.backend.adaptation.fm;

import com.longyg.backend.adaptation.common.ManualCloseZipInputStream;
import com.longyg.backend.adaptation.common.Parser;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.zip.ZipEntry;

/**
 * Created by ylong on 2/17/2017.
 */
public class FmPackageParser {
    private static final Logger LOG = Logger.getLogger(FmPackageParser.class);

    public FmAdaptation parse(InputStream is) throws Exception {
        ManualCloseZipInputStream zin = new ManualCloseZipInputStream(is);
        ZipEntry entry = null;
        FmAdaptation fmAdaptation = new FmAdaptation();
        Parser parser;
        try {
            while ((entry = zin.getNextEntry()) != null)
            {
                String name = entry.getName();
                if (name.endsWith(".man"))
                {
                    LOG.debug("Parsing man page: " + name);
                    parser = new ManParser(fmAdaptation, name);
                    parser.parse(zin);
                }
                else if (name.endsWith(".man.common"))
                {
                    LOG.debug("Parsing man common: " + name);
                    parser = new ManCommonParser(fmAdaptation);
                    parser.parse(zin);
                }
            }
        } catch (Exception e) {
            LOG.error("Exception while parsing FMB zip: " + entry.getName(), e);
            throw new Exception("Exception while parsing FMB zip: " + entry.getName(), e);
        }
        LOG.debug("Adding FmAdaptation: " + fmAdaptation);
        return fmAdaptation;
    }
}
