package com.longyg.backend.ars.export;

import com.longyg.frontend.Utils.FileUtils;
import com.longyg.frontend.model.ars.ARS;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class ArsExporter {
    private static final Logger LOG = Logger.getLogger(ArsExporter.class);
    private static final String XLS_TPL_PATH = "template.xls";
    private static final String ARS_XLS_ROOT_PATH = "ars";
    private static final String ARS_XLS_FILENAME_PREFIX = "Adaptation Requirements Specification";

    private HSSFWorkbook wb;
    private ARS ars;

    @Autowired
    private ObjectModelExporter omExporter;

    @Autowired
    private PmDataLoadExporter pmDlExporter;

    @Autowired
    private CountersExporter countersExporter;

    @Autowired
    private AlarmsExporter alarmsExporter;

    private List<Exporter> exporters = new ArrayList<>();

    public void export(ARS ars) throws Exception {
        this.ars = ars;

        init();

        for (Exporter exporter : this.exporters) {
            exporter.export(this.ars, this.wb);
        }

        String outPath = FileUtils.getArsFilePath(this.ars.getNeType(), this.ars.getNeVersion());
        try (FileOutputStream fileOut = new FileOutputStream(outPath)) {
            this.wb.write(fileOut);
        } catch (Exception e) {
            LOG.error("Exception while exporting ARS excel: ", e);
            throw new Exception("Exception while exporting ARS excel: ", e);
        } finally {
            if (null != this.wb) {
                this.wb.close();
            }
        }
    }

    private void init() throws Exception {
        initWorkbook();
        initExporters();
    }
    private void initExporters() {
        this.exporters.clear();
        this.exporters.add(omExporter);
        this.exporters.add(pmDlExporter);
        this.exporters.add(countersExporter);
        this.exporters.add(alarmsExporter);
    }

    private void initWorkbook() throws Exception {
        try (FileInputStream fi = new FileInputStream(XLS_TPL_PATH)) {
            this.wb = new HSSFWorkbook(fi);
        } catch (Exception e) {
            LOG.error("Exception while initializing workbook via template", e);
            throw new Exception("Exception while initializing workbook via template", e);
        }
    }
}
