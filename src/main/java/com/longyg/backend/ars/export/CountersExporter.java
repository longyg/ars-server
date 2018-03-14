package com.longyg.backend.ars.export;

import com.longyg.backend.TemplateRepository;
import com.longyg.frontend.model.ars.ARS;
import com.longyg.frontend.model.ars.counter.ArsCounter;
import com.longyg.frontend.model.ars.counter.CounterMeas;
import com.longyg.frontend.model.ars.counter.CounterSpec;
import com.longyg.frontend.service.ArsService;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CountersExporter extends Exporter {
    private static final Logger LOG = Logger.getLogger(CountersExporter.class);

    @Autowired
    private ArsService arsService;

    private HSSFRow adapIdTplRow;
    private HSSFRow titleTplRow;
    private HSSFRow dataTplRow;

    public void export(ARS ars, HSSFWorkbook wb) {
        init(ars, wb);

        exportCounters();
    }

    private void exportCounters() {
        int sheetNo = TemplateRepository.getCtrTplDef().getSheet();
        HSSFSheet sheet = this.wb.getSheetAt(sheetNo);

        int adapIdRowNo = TemplateRepository.getCtrTplDef().getAdapIdRow();
        this.adapIdTplRow = sheet.getRow(adapIdRowNo);
        int titleRowNo = TemplateRepository.getCtrTplDef().getTitleRow();
        this.titleTplRow = sheet.getRow(titleRowNo);
        int dataRowNo = TemplateRepository.getCtrTplDef().getDataRow();
        this.dataTplRow = sheet.getRow(dataRowNo);

        ExportUtils.cleanSheet(sheet);

        CounterSpec spec = arsService.findCounter(this.ars.getCounter());

        generateXlsContent(sheet, spec);
    }

    private void generateXlsContent(HSSFSheet sheet, CounterSpec spec) {
        int rowNo = 0;
        if (spec.getMeasurementMap().keySet().size() > 1) {
            for (String adaptationId : spec.getMeasurementMap().keySet()) {
                ExportUtils.addAdaptationIdRow(rowNo, sheet, adaptationId, adapIdTplRow);
                rowNo++;
                ExportUtils.addTitleRow(rowNo, sheet, titleTplRow);
                rowNo = addDataRows(rowNo, sheet, spec.getMeasurementMap().get(adaptationId));
                rowNo++;
            }
        } else if (spec.getMeasurementMap().keySet().size() == 1) {
            ExportUtils.addTitleRow(rowNo, sheet, titleTplRow);
            String adaptationId = spec.getMeasurementMap().keySet().iterator().next();
            addDataRows(rowNo, sheet, spec.getMeasurementMap().get(adaptationId));
        } else {
            LOG.error("Empty counter entries");
        }
    }

    private int addDataRows(int initRowNo, HSSFSheet sheet, List<CounterMeas> dataList) {
        int rowNo = initRowNo + 1;
        for (CounterMeas meas : dataList) {
            for (ArsCounter counter : meas.getCounters()) {
                HSSFRow newRow = sheet.getRow(rowNo);
                if (null == newRow) {
                    newRow = sheet.createRow(rowNo);
                }

                setCell(newRow, 0, meas.getName());
                setCell(newRow, 1, counter.getName());
                setCell(newRow, 2, counter.isSupported() ? "Yes" : "No");
                setCell(newRow, 3, counter.getSupportedOtherReleases());
                setCell(newRow, 4, counter.getAggRule());
                setCell(newRow, 5, counter.getPresentation());
                setCell(newRow, 6, counter.getUnit());
                setCell(newRow, 7, counter.getDescription());

                rowNo++;
            }
        }
        return rowNo;
    }

    private void setCell(HSSFRow row, int cellNo, Object value) {
        ExportUtils.setCell(row, cellNo, value, dataTplRow);
    }
}
