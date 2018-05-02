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
public class ManParser implements Parser {
    private static final Logger LOG = Logger.getLogger(ManParser.class);
    private FmAdaptation fmAdaptation;
    private String filename;

    public ManParser(FmAdaptation fmAdaptation, String filename) {
        this.fmAdaptation = fmAdaptation;
        this.filename = filename;
    }

    public void parse(InputStream is) throws Exception {
        try {
            Document doc = CommonUtils.createDocument(is);

            Element element = (Element) doc.getElementsByTagName("com.nokia.oss.fm.fmbasic:AlarmDescription").item(0);
            Alarm alarm = new Alarm();
            alarm.setAlarmNumber(element.getAttribute("specificProblem"));
            alarm.setAlarmText(element.getAttribute("alarmText"));
            alarm.setAlarmType(element.getAttribute("alarmType"));
            alarm.setCancelling(element.getAttribute("cancelling"));
            alarm.setInstructions(element.getAttribute("instructions"));
            alarm.setMeaning(element.getAttribute("meaning"));
            alarm.setPerceivedSeverityInfo(element.getAttribute("perceivedSeverityInfo"));
            alarm.setProbableCause(element.getAttribute("probableCause"));

            fmAdaptation.addAlarm(alarm);
        } catch (Exception e) {
            LOG.error("Exception while parsing man", e);
            throw new Exception("Exception while parsing man", e);
        }
    }
}
