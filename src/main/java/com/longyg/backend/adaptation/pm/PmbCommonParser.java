package com.longyg.backend.adaptation.pm;

import com.longyg.backend.adaptation.common.CommonUtils;
import com.longyg.backend.adaptation.common.Parser;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.InputStream;

/**
 * Created by ylong on 2/14/2017.
 */
public class PmbCommonParser implements Parser {
    private static final Logger LOG = Logger.getLogger(PmbCommonParser.class);
    private PmAdaptation pmAdaptation;

    public PmbCommonParser(PmAdaptation pmAdaptation) {
        this.pmAdaptation = pmAdaptation;
    }
    public void parse(InputStream is) {
        try {
            Document doc = CommonUtils.createDocument(is);

            Element element = (Element) doc.getElementsByTagName("com.nokia.oss.common:Adaptation").item(0);
            pmAdaptation.setAdapId(element.getAttribute("id"));
            pmAdaptation.setAdapRelease(element.getAttribute("release"));
            pmAdaptation.setPresentation(element.getAttribute("presentation"));
            LOG.debug(pmAdaptation);
        } catch (Exception e) {
            LOG.error("Exception while parsing pmb common", e);
        }
    }
}
