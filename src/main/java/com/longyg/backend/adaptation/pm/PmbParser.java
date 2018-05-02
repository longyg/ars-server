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
public class PmbParser implements Parser{
    private static final Logger LOG = Logger.getLogger(PmbParser.class);
    private PmAdaptation pmAdaptation;

    public PmbParser(PmAdaptation pmAdaptation) {
        this.pmAdaptation = pmAdaptation;
    }

    public void parse(InputStream is) throws Exception {
        try {
            Document doc = CommonUtils.createDocument(is);

            parseObjectClasses(doc);

            parseMeasurements(doc);

        } catch (Exception e) {
            LOG.error("Exception while parsing pmb", e);
            throw new Exception("Exception while parsing pmb", e);
        }
    }

    private void parseObjectClasses(Document doc) {
        NodeList nodeList = doc.getElementsByTagName("ns2:PMClassInfo");
        for (int i = 0 ; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            ObjectClass objectClass = new ObjectClass();
            String name = element.getAttribute("name");
            objectClass.setName(name);
            String nameInOmes = element.getAttribute("nameInOMeS");
            if (null == nameInOmes || "".equals(nameInOmes)) {
                nameInOmes = name;
            }
            objectClass.setNameInOmes(nameInOmes);
            boolean isTransient = true;
            String intransient = element.getAttribute("intransient");
            if (intransient.equals("true")) {
                isTransient = false;
            } else if (intransient.equals("false")) {
                isTransient = true;
            }
            objectClass.setTransient(isTransient);
            objectClass.setPresentation(element.getAttribute("presentation"));
            LOG.debug(objectClass);

            pmAdaptation.addObjectClass(objectClass);
        }
    }

    private void parseMeasurements(Document doc) {
        NodeList measList = doc.getElementsByTagName("ns2:Measurement");
        for (int i = 0 ; i < measList.getLength(); i++) {
            Element element = (Element) measList.item(i);
            Measurement measurement = new Measurement();
            String name = element.getAttribute("measurementType");
            String nameInOmes = element.getAttribute("measurementTypeInOMeS");
            if (null == nameInOmes || "".equals(nameInOmes)) {
                nameInOmes = name;
            }
            measurement.setName(name);
            measurement.setNameInOmes(nameInOmes);
            measurement.setPresentation(element.getAttribute("presentation"));
            measurement.setInterval(element.getAttribute("defaultInterval"));

            NodeList desc = element.getElementsByTagName("ns2:Description");
            if (null != desc && desc.getLength() > 0) {
                String description = desc.item(0).getTextContent();
                measurement.setDescription(description);
            }

            parseMeasuredTargets(element, measurement);
            parseCounters(element, measurement);

            pmAdaptation.addMeasurement(measurement);
        }
    }

    private void parseMeasuredTargets(Element element, Measurement measurement) {
        NodeList nodeList = element.getElementsByTagName("ns2:MeasuredTarget");
        for (int i = 0 ; i < nodeList.getLength(); i++) {
            Element ele = (Element) nodeList.item(i);
            MeasuredTarget measuredTarget = new MeasuredTarget();
            measuredTarget.setDimension(ele.getAttribute("dimension"));

            NodeList hierarchyNodeList = ele.getElementsByTagName("ns2:Hierarchy");
            for (int j = 0; j < hierarchyNodeList.getLength(); j++) {
                Element hie = (Element) hierarchyNodeList.item(j);
                measuredTarget.addHierarchy(hie.getAttribute("classes"));
            }
            measurement.addMeasuredTarget(measuredTarget);
        }
    }

    private void parseCounters(Element element, Measurement measurement) {
        NodeList nodeList = element.getElementsByTagName("ns2:MeasuredIndicator");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element el = (Element) nodeList.item(i);
            String name = el.getAttribute("name");
            String omesName = el.getAttribute("nameInOMeS");
            if (null == omesName || "".equals(omesName)) {
                omesName = name;
            }
            Counter counter = new Counter();
            counter.setName(name);
            counter.setOmesName(omesName);
            counter.setPresentation(el.getAttribute("presentation"));
            counter.setUnit(el.getAttribute("unit"));

            NodeList desc = el.getElementsByTagName("ns2:Description");
            if (null != desc && desc.getLength() > 0) {
                String description = desc.item(0).getTextContent();
                counter.setDescription(description);
            }

            NodeList rule = el.getElementsByTagName("ns2:TimeAndObjectAggregationRule");
            if (null != rule && rule.getLength() > 0) {
                String aggRule = rule.item(0).getTextContent();
                counter.setAggRule(aggRule);
            }

            measurement.addCounter(counter);
        }
    }
}
