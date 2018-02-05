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
 * Created by ylong on 2/17/2017.
 */
public class CounterGenerator {
    private static final Logger LOG = Logger.getLogger(CounterGenerator.class);
    private static final String OUT_FILE_NAME = "counter.csv";
    private String outFilePath;
    private StringBuffer context;

    public CounterGenerator() {
        this.context = new StringBuffer();
        this.outFilePath = ConfigRepository.getInstance().getResultDir() + File.separator + OUT_FILE_NAME;
    }

    public void generate() throws Exception {
        write();
        writeToFile();
    }

    public void write() {
        writeTitle();

        for (Measurement meas : getAllMeasurements())
        {
            List<Counter> allCounters = getAllCounters(meas);
            Collections.sort(allCounters);
            for (int i = 0; i < allCounters.size(); i++) {
                Counter counter = allCounters.get(i);

                // column 1: Measurement Type ID
                context.append(meas.getName()).append(",");

                // column 2: Counters
                context.append(counter.getName()).append(",");

                // column 3: Is supported by current version
                context.append(checkIfSupportedByCurrentVersion(meas, counter)).append(",");

                // column 4: Supported previous versions
                context.append("\"" + getSupportedOtherVersions(meas, counter) + "\"").append(",");

                // column 5: Aggregation rule
                context.append(counter.getAggRule()).append(",");

                // column 6: Presentation
                context.append("\"" + counter.getPresentation() + "\"").append(",");

                // column 7: Unit
                context.append("\"" + counter.getUnit() + "\"").append(",");

                // column 8: Description
                context.append("\"" + counter.getDescription() + "\"").append("\n");
            }
        }
    }

    private void writeToFile() throws Exception {
        LOG.info("Writing counter to: " + outFilePath);
        FileWriter writer = new FileWriter(new File(outFilePath));
        writer.write(context.toString());
        writer.flush();
        writer.close();
    }

    private String checkIfSupportedByCurrentVersion(Measurement measurement, Counter counter) {
        for (Map.Entry<String, PmAdaptation> entry : PmRepository.getPmAdaptations().entrySet()) {
            String version = entry.getKey();
            if (version.equals(ConfigRepository.getInstance().getNeVersion())) {
                PmAdaptation pmAdaptation = entry.getValue();
                for (Measurement meas : pmAdaptation.getMeasurements()) {
                    if (meas.equals(measurement)) {
                        for (Counter c : meas.getCounters()) {
                            if (c.equals(counter)) {
                                return "Yes";
                            }
                        }
                    }
                }
            }
        }
        return "No";
    }

    private String getSupportedOtherVersions(Measurement measurement, Counter counter) {
        List<String> supportedOtherVersions = new ArrayList<String>();
        for (Map.Entry<String, PmAdaptation> entry : PmRepository.getPmAdaptations().entrySet()) {
            String version = entry.getKey();
            if (!version.equals(ConfigRepository.getInstance().getNeVersion())) {
                PmAdaptation pmAdaptation = entry.getValue();
                for (Measurement meas : pmAdaptation.getMeasurements()) {
                    if (meas.equals(measurement)) {
                        for (Counter c : meas.getCounters()) {
                            if (c.equals(counter)) {
                                supportedOtherVersions.add(version);
                            }
                        }
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

    private List<Counter> getAllCounters(Measurement measurement) {
        List<Counter> allCounters = new ArrayList<Counter>();
        allCounters.addAll(measurement.getCounters());

        for (PmAdaptation pmAdaptation : PmRepository.getPmAdaptations().values()) {
            for (Measurement meas : pmAdaptation.getMeasurements()) {
                if (meas.equals(measurement)) {
                    for (Counter c : meas.getCounters()) {
                        if (!allCounters.contains(c)) {
                            allCounters.add(c);
                        }
                    }
                }
            }
        }
        return allCounters;
    }

    private void writeTitle() {
        String currentVersion = ConfigRepository.getInstance().getNeVersion();
        context.append("Measurement Type ID").append(",");
        context.append("Counters").append(",");
        context.append("Is supported by - ").append(currentVersion).append(",");
        context.append("Supported previous versions").append(",");
        context.append("Aggregation rule").append(",");
        context.append("Presentation").append(",");
        context.append("Unit").append(",");
        context.append("Description").append("\n");
    }
}
