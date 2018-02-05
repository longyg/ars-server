package com.longyg.backend.adaptation.pm;

import com.longyg.backend.adaptation.config.ConfigRepository;
import com.longyg.backend.adaptation.config.OmesGenerateConfig;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

/**
 * Use to generate OMeS sample file for latest release
 *
 * Created by ylong on 3/24/2017.
 */
public class OmesGenerator {
    private static final Logger LOG = Logger.getLogger(OmesGenerator.class);
    private Properties properties = null;
    private static final String OMES_FILE_NAME_SUFFIX = "omes_example.xml";
    private String outFilePath;
    private static final int NE_NUMBER = 1;
    private String additionalParentDn = null;

    public OmesGenerator() throws Exception {
        this.outFilePath = ConfigRepository.getInstance().getResultDir()
                + File.separator + ConfigRepository.getInstance().getNeVersion()
                + "_" + OMES_FILE_NAME_SUFFIX;
        OmesGenerateConfig omesConfig = ConfigRepository.getInstance().getOmesConfig();

        if (null != omesConfig && null != omesConfig.getAdditionalParentDn()
                && !"".equals(omesConfig.getAdditionalParentDn())) {
            additionalParentDn = omesConfig.getAdditionalParentDn();
        }
        loadProperties();
    }

    public void generateOMeS() throws Exception {
        PmAdaptation pmAdaptation = getLatestPmAdaptation();
        if (null == pmAdaptation) {
            LOG.error("Latest version of PM adaptation is not found.");
            return;
        }

        FileWriter writer = new FileWriter(new File(outFilePath));
        LOG.info("Writing omes file to: " + outFilePath);

        writer.write("<?xml version=\"1.0\"?>\n");
        writer.write("<OMeS version=\"2.3\">\n");

        for (Measurement measurement : pmAdaptation.getMeasurements()) {
            for (int i = 1; i <= NE_NUMBER; i++) {
                int objectInstanceNumber = 1;
                if (null != properties) {
                    Object obj = properties.get(measurement.getName());
                    if (null != obj) {
                        objectInstanceNumber = Integer.valueOf(obj.toString());
                    }
                }

                for (int j = 1; j <= objectInstanceNumber; j++) {
                    writer.write("\t<PMSetup startTime=\"2015-03-05T09:30:00.000+02:00:00\" interval=\"15\">\n");
                    writer.write("\t\t<PMMOResult>\n");

                    for (MeasuredTarget measuredTarget : measurement.getMeasuredTargets()) {
                        writer.write("\t\t\t<MO dimension=\"" + measuredTarget.getDimension() + "\">\n");
                        writer.write("\t\t\t\t<DN>");
                        String hierarchy = format(measuredTarget.getHierarchies().get(0));
                        String[] classes = hierarchy.split("/");
                        String dn = "";
                        if (null != additionalParentDn) {
                            dn = dn + additionalParentDn + "/";
                        }
                        for (int n = 0; n < classes.length; n++) {
                            String className = classes[n];
                            String omesName = getOmesClassName(pmAdaptation, className);
                            if (n < classes.length - 1) {
                                dn = dn + omesName  + "-1/";
                            } else {
                                dn = dn + omesName + "-" + j;
                            }
                        }
                        writer.write(dn + "</DN>\n");
                        writer.write("\t\t\t</MO>\n");
                    }

                    writer.write("\t\t\t<PMTarget measurementType=\"");
                    writer.write(measurement.getNameInOmes() + "\">\n");

                    for(Counter counter : measurement.getCounters()) {
                        String counterName = counter.getOmesName();
                        writer.write("\t\t\t\t<" + counterName + ">");
                        Random random = new Random();
                        writer.write(Integer.toString(random.nextInt(10000)));
                        writer.write("</" + counterName + ">\n");
                    }

                    writer.write("\t\t\t</PMTarget>\n");
                    writer.write("\t\t</PMMOResult>\n");
                    writer.write("\t</PMSetup>\n");
                }

            }
        }
        writer.write("</OMeS>");
        writer.flush();
        writer.close();
    }

    private PmAdaptation getLatestPmAdaptation() {
        PmAdaptation pmAdaptation = null;
        for (Map.Entry<String, PmAdaptation> entry : PmRepository.getPmAdaptations().entrySet()) {
            String version = entry.getKey();
            if (version.equals(ConfigRepository.getInstance().getNeVersion())) {
                pmAdaptation = entry.getValue();
            }
        }
        return pmAdaptation;
    }

    private void loadProperties() throws IOException {
        String path = ConfigRepository.getInstance().getLoadPropPath();
        if (null != path && !"".equals(path)) {
            File file = new File(path);
            if (!file.exists() || !file.isFile()) {
                LOG.error("Path does not exist or is not a file " + path);
                return;
            }
            properties = new Properties();
            properties.load(new FileInputStream(file));
        }
    }

    private String getOmesClassName(PmAdaptation pmAdaptation, String clazz) {
        List<ObjectClass> pmClasses = pmAdaptation.getObjectClasses();
        for (ObjectClass objectClass : pmClasses) {
            if (clazz.equals(objectClass.getName())) {
                return objectClass.getNameInOmes();
            }
        }
        return clazz;
    }

    private static String format(String hierarchy) {
        if (hierarchy.lastIndexOf("/") == hierarchy.length() - 1) {
            hierarchy = hierarchy.substring(0, hierarchy.lastIndexOf("/"));
        }
        if (hierarchy.indexOf("/") == 0) {
            hierarchy = hierarchy.substring(1, hierarchy.length());
        }
        return hierarchy;
    }
}
