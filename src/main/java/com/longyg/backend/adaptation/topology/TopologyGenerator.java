package com.longyg.backend.adaptation.topology;

import com.longyg.backend.adaptation.config.ConfigRepository;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ylong on 2/15/2017.
 */
public class TopologyGenerator {
    private static final Logger LOG = Logger.getLogger(TopologyGenerator.class);
    private static final String OUT_FILE_NAME = "topology.csv";
    private String outFilePath;
    private StringBuffer topology;
    private TopologyRepository topologyRepository = TopologyRepository.getInstance();

    public TopologyGenerator() {
        this.topology = new StringBuffer();
        this.outFilePath = ConfigRepository.getInstance().getResultDir() + File.separator + OUT_FILE_NAME;
    }

    public void generate() throws Exception {
        topologyRepository.buildTopology();
        writeTopology();
        writeToFile();
    }

    private void writeTopology() {
        int maxLevel = topologyRepository.getMaxLevel();
        List<PmbObject> rootObjects = topologyRepository.getRootObjects();

        for (PmbObject parentObj : rootObjects) {
            topology.append(parentObj.getName());

            for (int i = maxLevel + 1; i > 0; i--) {
                topology.append(",");
            }
            topology.append(parentObj.isMeasuredObject() ? "M" : "").append(",");

            for (String version : parentObj.getSupportedVersions()) {
                topology.append(version).append(";");
            }
            topology.deleteCharAt(topology.length() - 1);
            topology.append(",");

            topology.append(parentObj.isTransient() ? "Transient" : "MO").append(",");
            topology.append("\"" + parentObj.getPresentation() + "\"").append(",");
            topology.append("\"" + parentObj.getNameInOmes() + "\"").append("\n");

            List<PmbObject> childObjs = parentObj.getChildObjects();
            Collections.sort(childObjs);
            for (PmbObject childObj : childObjs) {
                int level = 1;
                printChildTopology(childObj, level);
            }
        }
    }

    private void printChildTopology(PmbObject rootObj, int level) {
        for (int i = 0; i < level; i++)
        {
            topology.append("|,");
        }
        topology.append("|- ").append(rootObj.getName()).append(",");

        for (int i = topologyRepository.getMaxLevel(); i > level; i--)
        {
            topology.append(",");
        }
        topology.append(rootObj.isMeasuredObject() ? "M" : "").append(",");

        for (String version : rootObj.getSupportedVersions()) {
            topology.append(version).append(";");
        }
        topology.deleteCharAt(topology.length() - 1);
        topology.append(",");

        topology.append(rootObj.isTransient() ? "Transient" : "MO").append(",");
        topology.append("\"" + rootObj.getPresentation() + "\"").append(",");
        topology.append("\"" + rootObj.getNameInOmes() + "\"").append("\n");


        List<PmbObject> childObjs = rootObj.getChildObjects();
        Collections.sort(childObjs);
        for (PmbObject childObj : childObjs) {
            printChildTopology(childObj, level + 1);
        }
    }

    private void writeToFile() throws Exception {
        LOG.info("Writing topology to: " + outFilePath);
        FileWriter writer = new FileWriter(new File(outFilePath));
        writer.write(topology.toString());
        writer.flush();
        writer.close();
    }
}
