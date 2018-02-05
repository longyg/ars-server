package com.longyg.backend.adaptation.pm;

import com.longyg.backend.adaptation.config.ConfigRepository;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by ylong on 2/16/2017.
 */
public class MeasurementGenerator {
    private static final Logger LOG = Logger.getLogger(MeasurementGenerator.class);
    private static final String OUT_FILE_NAME = "measurement.csv";
    private String outFilePath;
    private StringBuffer context;

    public MeasurementGenerator() {
        this.context = new StringBuffer();
        this.outFilePath = ConfigRepository.getInstance().getResultDir() + File.separator + OUT_FILE_NAME;
    }

    public void generate() throws Exception {
        write();
        writeToFile();
    }

    public void write() {
        writeTitle();

        for (Measurement measurement : getAllMeasurements()) {

            // Column 1: Measurement Type ID
            context.append(measurement.getName()).append(",");

            // Column 2: Measurement Type in OMeS
            context.append("\"" + measurement.getNameInOmes() + "\"").append(",");

            // Column 3: Measured Object
            context.append(getMeasuredObject(measurement)).append(",");

            // Column 4: Is supported by current version
            context.append(checkIfSupportedByCurrentVersion(measurement)).append(",");

            // Column 5: Supported by other versions
            context.append("\"" + getSupportedOtherVersions(measurement) + "\"").append(",");

            // Column 6: Dimonsions
            context.append("\"" + getDimensions(measurement) + "\"").append(",");

            // Column 7: Nbr of counters per measurement
            int currentCount = getCounterNumberOfVersion(measurement, ConfigRepository.getInstance().getNeVersion());
            context.append(currentCount).append(",");

            // Column 8: Nbr-1 of counters per measurement
            int lastCount = getCounterNumberOfVersion(measurement, ConfigRepository.getInstance().getLastVersion());
            context.append(lastCount).append(",");

            // Column 9: Counter delta
            int delta = currentCount - lastCount;
            context.append(delta).append(",");

            // Column 10: Default interval
            context.append(measurement.getInterval()).append("\n");
        }
    }

    private void writeToFile() throws Exception {
        LOG.info("Writing measurement to: " + outFilePath);
        FileWriter writer = new FileWriter(new File(outFilePath));
        writer.write(context.toString());
        writer.flush();
        writer.close();
    }

    private int getCounterNumberOfVersion(Measurement measurement, String version) {
        for (Map.Entry<String, PmAdaptation> entry : PmRepository.getPmAdaptations().entrySet()) {
            String v = entry.getKey();
            if (v.equals(version)) {
                PmAdaptation pmAdaptation = entry.getValue();
                for (Measurement meas : pmAdaptation.getMeasurements()) {
                    if (meas.equals(measurement)) {
                        return meas.getCounters().size();
                    }
                }
            }
        }
        return 0;
    }

    private String checkIfSupportedByCurrentVersion(Measurement measurement) {
        for (Map.Entry<String, PmAdaptation> entry : PmRepository.getPmAdaptations().entrySet()) {
            String version = entry.getKey();
            if (version.equals(ConfigRepository.getInstance().getNeVersion())) {
                PmAdaptation pmAdaptation = entry.getValue();
                for (Measurement meas : pmAdaptation.getMeasurements()) {
                    if (meas.equals(measurement)) {
                        return "Yes";
                    }
                }
            }
        }
        return "No";
    }

    private List<Measurement> getAllMeasurements() {
        List<Measurement> allMeas = new ArrayList<Measurement>();

        // add all measurements of current version
        for (Map.Entry<String, PmAdaptation> entry : PmRepository.getPmAdaptations().entrySet()) {
            String version = entry.getKey();
            PmAdaptation pmAdaptation = entry.getValue();
            if (version.equals(ConfigRepository.getInstance().getNeVersion())) {
                for (Measurement measurement : pmAdaptation.getMeasurements()) {
                    if (!allMeas.contains(measurement)) {
                        allMeas.add(measurement);
                    }
                }
            }
        }

        for (PmAdaptation pmAdaptation : PmRepository.getPmAdaptations().values()) {
            for (Measurement measurement : pmAdaptation.getMeasurements()) {
                if (!allMeas.contains(measurement)) {
                    allMeas.add(measurement);
                }
            }
        }
        Collections.sort(allMeas);
        return  allMeas;
    }

    private String getMeasuredObject(Measurement measurement) {
        for (MeasuredTarget measuredTarget : measurement.getMeasuredTargets()) {
            if (measuredTarget.getDimension().equals("network_element")) {
                if (measuredTarget.getHierarchies().size() > 0) {
                    return measuredTarget.getHierarchies().get(0);
                }
            }
        }
        return "";
    }

    private String getSupportedOtherVersions(Measurement measurement) {
        List<String> supportedOtherVersions = new ArrayList<String>();
        for (Map.Entry<String, PmAdaptation> entry : PmRepository.getPmAdaptations().entrySet()) {
            String version = entry.getKey();
            if (!version.equals(ConfigRepository.getInstance().getNeVersion())) {
                PmAdaptation pmAdaptation = entry.getValue();
                for (Measurement meas : pmAdaptation.getMeasurements()) {
                    if (meas.equals(measurement)) {
                        addToList(supportedOtherVersions, version);
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

    private String getDimensions(Measurement measurement) {
        List<String> dimensions = new ArrayList<String>();
        for (MeasuredTarget measuredTarget : measurement.getMeasuredTargets()) {
            addToList(dimensions, measuredTarget.getDimension());
        }
        return listToString(dimensions);
    }

    private static void addToList(List<String> list, String str) {
        if (!list.contains(str)) {
            list.add(str);
        }
    }

    private void writeTitle() {
        context.append("Measurement Type ID").append(",");
        context.append("Measurement Type in OMeS").append(",");
        context.append("Measured Object").append(",");
        context.append("Is supported by " + ConfigRepository.getInstance().getNeVersion()).append(",");
        context.append("Supported previous versions").append(",");
        context.append("Dimensions").append(",");
        context.append("Nbr of counters per measurement").append(",");
        context.append("Nbr-1 of counters per measurement").append(",");
        context.append("Counter delta").append(",");
        context.append("Default interval").append("\n");
    }
}
