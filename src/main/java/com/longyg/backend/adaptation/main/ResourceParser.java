package com.longyg.backend.adaptation.main;

import com.longyg.backend.adaptation.common.ManualCloseZipInputStream;
import com.longyg.backend.adaptation.common.Parser;
import com.longyg.backend.adaptation.fm.ManZipParser;
import com.longyg.backend.adaptation.pm.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;

/**
 * Created by ylong on 2/14/2017.
 */
public class ResourceParser {
    private static final Logger LOG = Logger.getLogger(ResourceParser.class);

    public void parse() throws Exception {
        List<Resource> resourceList = ResourceRepository.getResources();

        for (Resource resource : resourceList) {
            parseOneResource(resource);
        }
    }

    private void parseOneResource(Resource resource) throws Exception {
        LOG.debug("Parsing resource: " + resource.getPath());
        ManualCloseZipInputStream zin = new ManualCloseZipInputStream(resource.getInputStream());
        ZipEntry entry = null;
        Parser parser;
        try {
            while ((entry = zin.getNextEntry()) != null)
            {
                String name = entry.getName();

                LOG.debug("Parsing zip entry: " + name);
                if (name.contains(".pmb"))
                {
                    LOG.debug("Parsing pmb zip: " + name);
                    parser = new PmbZipParser();
                    parser.parse(zin);
                }
                else if (name.contains(".man"))
                {
                    LOG.debug("Parsing man zip: " + name);
                    parser = new ManZipParser();
                    parser.parse(zin);
                }
            }
        } catch (IOException e) {
            LOG.error("Exception while parsing resource: " + resource.getPath(), e);
            throw new Exception("Exception while parsing resource: " + resource.getPath(), e);
        } finally {
            try {
                zin.doClose();
            } catch (IOException e) {
                LOG.error("Exception while closing resource stream", e);
            }
        }
    }
}
