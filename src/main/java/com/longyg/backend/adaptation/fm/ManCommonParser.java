package com.longyg.backend.adaptation.fm;

import com.longyg.backend.adaptation.common.CommonUtils;
import com.longyg.backend.adaptation.common.Parser;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.InputStream;

/**
 * Created by ylong on 2/17/2017.
 */
public class ManCommonParser implements Parser {
    private static final Logger LOG = Logger.getLogger(ManCommonParser.class);
    private FmAdaptation fmAdaptation;

    public ManCommonParser(FmAdaptation fmAdaptation) {
        this.fmAdaptation = fmAdaptation;
    }

    public void parse(InputStream is) throws Exception {
        try {
            Document doc = CommonUtils.createDocument(is);
            Element element = (Element) doc.getElementsByTagName("com.nokia.oss.common:Adaptation").item(0);
            fmAdaptation.setAdapId(element.getAttribute("id"));
            fmAdaptation.setAdapRelease(element.getAttribute("release"));
            fmAdaptation.setPresentation(element.getAttribute("presentation"));
        } catch (Exception e) {
            LOG.error("Exception while parsing man.common", e);
            throw new Exception("Exception while parsing man.common", e);
        }
    }
}
