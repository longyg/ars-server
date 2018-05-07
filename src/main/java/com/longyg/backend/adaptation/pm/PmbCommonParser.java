package com.longyg.backend.adaptation.pm;

import com.longyg.backend.adaptation.common.CommonUtils;
import com.longyg.backend.adaptation.common.Parser;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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
    public void parse(InputStream is) throws Exception {
        try {
            Document doc = CommonUtils.createDocument(is);
            NodeList list = doc.getElementsByTagName("com.nokia.oss.common:Adaptation");
            if (null == list) {
                list = doc.getElementsByTagName("base:Adaptation");
            }
            Element element = null;
            if (null != list) {
                element = (Element) list.item(0);
            }
            if (null == element) {
                throw new Exception("Invalid pmb.common adaptation");
            }
            pmAdaptation.setAdapId(element.getAttribute("id"));
            pmAdaptation.setAdapRelease(element.getAttribute("release"));
            pmAdaptation.setPresentation(element.getAttribute("presentation"));
            LOG.debug(pmAdaptation);
        } catch (Exception e) {
            LOG.error("Exception while parsing pmb common", e);
            throw new Exception("Exception while parsing pmb common", e);
        }
    }
}
