package com.longyg.backend.adaptation.pm;

import com.longyg.backend.adaptation.common.ManualCloseZipInputStream;
import com.longyg.backend.adaptation.common.Parser;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.zip.ZipEntry;

/**
 * Created by ylong on 2/17/2017.
 */
public class PmbPackageParser {
    private static final Logger LOG = Logger.getLogger(PmbPackageParser.class);

    public PmAdaptation parse(InputStream is) throws Exception {
        ManualCloseZipInputStream zin = new ManualCloseZipInputStream(is);
        ZipEntry entry = null;
        PmAdaptation pmAdaptation = new PmAdaptation();
        Parser parser;
        try {
            while ((entry = zin.getNextEntry()) != null)
            {
                String name = entry.getName();
                if (name.endsWith(".pmb"))
                {
                    LOG.debug("Parsing pmb: " + name);
                    parser = new PmbParser(pmAdaptation);
                    parser.parse(zin);
                }
                else if (name.endsWith(".pmb.common"))
                {
                    LOG.debug("Parsing pmb common: " + name);
                    parser = new PmbCommonParser(pmAdaptation);
                    parser.parse(zin);
                }
            }
        } catch (Exception e) {
            LOG.error("Exception while parsing PMB zip: " + entry.getName(), e);
            throw new Exception("Exception while parsing PMB zip: " + entry.getName(), e);
        }
        LOG.debug("Adding pmAdaptation: " + pmAdaptation);
        return pmAdaptation;
    }

}
