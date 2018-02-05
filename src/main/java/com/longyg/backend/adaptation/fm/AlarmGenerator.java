package com.longyg.backend.adaptation.fm;

import com.longyg.backend.adaptation.config.ConfigRepository;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by ylong on 2/17/2017.
 */
public class AlarmGenerator {
    private static final Logger LOG = Logger.getLogger(AlarmGenerator.class);
    private static final String OUT_FILE_NAME = "alarm.csv";
    private String outFilePath;
    private StringBuffer context;

    public AlarmGenerator() {
        this.context = new StringBuffer();
        this.outFilePath = ConfigRepository.getInstance().getResultDir() + File.separator + OUT_FILE_NAME;
    }

    public void generate() throws Exception {
        write();
        writeToFile();
    }

    private void write() {
        writeTitle();

        List<Alarm> allAlarms = getAllAlarms();

        for (Alarm a : allAlarms) {
            Alarm currentRelAlarm = getAlarmOfCurrentVersion(a);
            Alarm lastRelAlarm = getAlarmOfLastVersion(a);

            // column 1: specificProblem
            context.append(a.getAlarmNumber()).append(",");

            // column 2: Is supported by current version
            context.append((null != currentRelAlarm) ? "Yes" : "No").append(",");

            // column 3: Supported previous versions
            context.append("\"" + getSupportedOtherVersions(a) + "\"").append(",");

            writeAlarmInfo(currentRelAlarm, lastRelAlarm, "alarmText", true);
            writeAlarmInfo(currentRelAlarm, lastRelAlarm, "probableCause", true);
            writeAlarmInfo(currentRelAlarm, lastRelAlarm, "alarmType", true);
            //writeAlarmInfo(currentRelAlarm, lastRelAlarm, "meaning", false);
            //writeAlarmInfo(currentRelAlarm, lastRelAlarm, "instructions", false);
            //writeAlarmInfo(currentRelAlarm, lastRelAlarm, "cancelling", false);
            writeAlarmInfo(currentRelAlarm, lastRelAlarm, "perceivedSeverityInfo", true);

            context.deleteCharAt(context.length() - 1);
            context.append("\n");
        }
    }

    private String getSupportedOtherVersions(Alarm a) {
        List<String> supportedOtherVersions = new ArrayList<String>();
        for (Map.Entry<String, FmAdaptation> entry : FmRepository.getFmAdaptations().entrySet()) {
            String version = entry.getKey();
            if (!version.equals(ConfigRepository.getInstance().getNeVersion())) {
                FmAdaptation fmAdaptation = entry.getValue();
                for (Alarm alarm : fmAdaptation.getAlarms()) {
                    if (alarm.equals(a)) {
                        supportedOtherVersions.add(version);
                    }
                }
            }
        }
        return listToString(supportedOtherVersions);
    }

    private static String listToString(List<String> list) {
        String result = "";
        if (list.size() > 0) {
            result = list.toString();
            result = result.substring(1, result.length() - 1);
        }
        return result;
    }

    private Alarm getAlarmOfCurrentVersion(Alarm a) {
        for (Map.Entry<String, FmAdaptation> entry : FmRepository.getFmAdaptations().entrySet()) {
            String version = entry.getKey();
            FmAdaptation fmAdaptation = entry.getValue();
            if (version.equals(ConfigRepository.getInstance().getNeVersion())) {
                for (Alarm alarm : fmAdaptation.getAlarms()) {
                    if (alarm.equals(a)) {
                        return alarm;
                    }
                }
            }
        }
        return null;
    }

    private Alarm getAlarmOfLastVersion(Alarm a) {
        for (Map.Entry<String, FmAdaptation> entry : FmRepository.getFmAdaptations().entrySet()) {
            String version = entry.getKey();
            FmAdaptation fmAdaptation = entry.getValue();
            if (version.equals(ConfigRepository.getInstance().getLastVersion())) {
                for (Alarm alarm : fmAdaptation.getAlarms()) {
                    if (alarm.equals(a)) {
                        return alarm;
                    }
                }
            }
        }
        return null;
    }

    private List<Alarm> getAllAlarms() {
        List<Alarm> allAlarms = new ArrayList<Alarm>();
        String currentVersion = ConfigRepository.getInstance().getNeVersion();
        // add all alarms of current version
        for (Map.Entry<String, FmAdaptation> entry : FmRepository.getFmAdaptations().entrySet()) {
            String version = entry.getKey();
            FmAdaptation fmAdaptation = entry.getValue();
            LOG.debug("Total alarm of " + version + ": " + fmAdaptation.getAlarms().size());
            if (version.equals(currentVersion)) {
                allAlarms.addAll(fmAdaptation.getAlarms());
            }
        }

        for (FmAdaptation fmAdaptation : FmRepository.getFmAdaptations().values()) {
            for (Alarm alarm : fmAdaptation.getAlarms()) {
                if (!allAlarms.contains(alarm)) {
                    allAlarms.add(alarm);
                }
            }
        }
        Collections.sort(allAlarms);
        return  allAlarms;
    }

    private void writeAlarmInfo(Alarm alarm, Alarm lastRelAlarm, String attribute, boolean display) {
        if (null != alarm) {
            String attrValue = getAlarmFieldValue(alarm, attribute);
            attrValue = formatValue(attrValue);
            // column n: attribute - <release>
            if (display) {
                context.append("\"" + attrValue + "\"").append(",");
            } else {
                context.append(",");
            }

            if (null != lastRelAlarm) {
                String lastRelAttrValue = getAlarmFieldValue(lastRelAlarm, attribute);
                lastRelAttrValue = formatValue(lastRelAttrValue);
                if (!attrValue.equals(lastRelAttrValue)) {
                    // column n + 1: attribute - <last release>
                    if (display) {
                        context.append("\"" + lastRelAttrValue + "\"").append(",");
                    } else {
                        context.append(",");
                    }

                    // column n + 2: Is attribute changed
                    context.append("Yes").append(",");
                } else {
                    // column n + 1: attribute - <last release>
                    context.append(",");
                    // column n + 2: Is attribute changed
                    context.append(",");
                }
            } else {
                // column n + 1: attribute - <last release>
                context.append(",");
                // column n + 2: Is attribute changed
                context.append(",");
            }
        } else {
            // column n: attribute - <release>
            context.append(",");
            if (null != lastRelAlarm) {
                String lastRelAttrValue = getAlarmFieldValue(lastRelAlarm, attribute);
                lastRelAttrValue = formatValue(lastRelAttrValue);
                // column n + 1: attribute - <last release>
                if (display) {
                    context.append("\"" + lastRelAttrValue + "\"").append(",");
                } else {
                    context.append(",");
                }
                // column n + 2: Is attribute changed
                context.append(",");
            } else {
                // column n + 1: attribute - <last release>
                context.append(",");
                // column n + 2: Is attribute changed
                context.append(",");
            }
        }
    }

    private String formatValue(String value) {
        String result = value.replaceAll("\n", " ");
        return result;
    }

    private String getAlarmFieldValue(Alarm alarm, String attribute) {
        try {
            Field field = alarm.getClass().getDeclaredField(attribute);
            field.setAccessible(true);
            if (null != field) {
                Object obj = field.get(alarm);
                if (null != obj) {
                    return obj.toString();
                }
            }
        } catch (Exception e) {
            LOG.error("Get Alarm attribute exception, alarm = " + alarm.getAlarmNumber() + ", attribute = " + attribute + e);
        }
        return "";
    }

    private void writeToFile() throws Exception {
        LOG.info("Writing alarm to: " + outFilePath);
        FileWriter writer = new FileWriter(new File(outFilePath));
        writer.write(context.toString());
        writer.flush();
        writer.close();
    }

    private void writeTitle() {
        String release = ConfigRepository.getInstance().getNeVersion();
        String lastRelease = ConfigRepository.getInstance().getLastVersion();
        context.append("SpecificProblem").append(",");

        context.append("Is supported by ").append(release).append(",");
        context.append("Supported previous versions").append(",");

        context.append("Alarm Text - ").append(release).append(",");
        context.append("Alarm Text - ").append(lastRelease).append(",");
        context.append("Is alarmText Changed").append(",");

        context.append("probableCause - ").append(release).append(",");
        context.append("probableCause - ").append(lastRelease).append(",");
        context.append("Is probableCause Changed").append(",");

        context.append("alarmType - ").append(release).append(",");
        context.append("alarmType - ").append(lastRelease).append(",");
        context.append("Is alarmType Changed").append(",");

        /**
        context.append("meaning - ").append(release).append(",");
        context.append("meaning - ").append(lastRelease).append(",");
        context.append("Is meaning Changed").append(",");

        context.append("instructions - ").append(release).append(",");
        context.append("instructions - ").append(lastRelease).append(",");
        context.append("Is instructions Changed").append(",");

        context.append("cancelling - ").append(release).append(",");
        context.append("cancelling - ").append(lastRelease).append(",");
        context.append("Is cancelling Changed").append(",");
         **/

        context.append("Severity - ").append(release).append(",");
        context.append("Severity - ").append(lastRelease).append(",");
        context.append("Is Severity Changed");
        context.append("\n");
    }
}
