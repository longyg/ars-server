package com.longyg.backend.ars.export;

import com.longyg.backend.TemplateRepository;
import com.longyg.frontend.model.ars.ARS;
import com.longyg.frontend.model.ars.alarm.AlarmSpec;
import com.longyg.frontend.model.ars.alarm.ArsAlarm;
import com.longyg.frontend.service.ArsService;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AlarmsExporter extends Exporter {
    private static final Logger LOG = Logger.getLogger(AlarmsExporter.class);

    @Autowired
    private ArsService arsService;

    private HSSFRow adapIdTplRow;
    private HSSFRow titleTplRow;
    private HSSFRow dataTplRow;

    public void export(ARS ars, HSSFWorkbook wb) {
        init(ars, wb);

        exportAlarms();
    }

    private void exportAlarms() {
        int sheetNo = TemplateRepository.getAlarmsTplDef().getSheet();
        HSSFSheet sheet = this.wb.getSheetAt(sheetNo);

        int adapIdRowNo = TemplateRepository.getAlarmsTplDef().getAdapIdRow();
        this.adapIdTplRow = sheet.getRow(adapIdRowNo);
        int titleRowNo = TemplateRepository.getAlarmsTplDef().getTitleRow();
        this.titleTplRow = sheet.getRow(titleRowNo);
        int dataRowNo = TemplateRepository.getAlarmsTplDef().getDataRow();
        this.dataTplRow = sheet.getRow(dataRowNo);

        ExportUtils.cleanSheet(sheet);

        AlarmSpec spec = arsService.findAlarm(this.ars.getAlarm());

        generateXlsContent(sheet, spec);
    }

    private void generateXlsContent(HSSFSheet sheet, AlarmSpec spec) {
        int rowNo = 0;
        if (spec.getAlarmMap().keySet().size() > 1) {
            for (String adaptationId : spec.getAlarmMap().keySet()) {
                ExportUtils.addAdaptationIdRow(rowNo, sheet, adaptationId, adapIdTplRow);
                rowNo++;
                ExportUtils.addTitleRow(rowNo, sheet, titleTplRow);
                rowNo = addDataRows(rowNo, sheet, spec.getAlarmMap().get(adaptationId));
                rowNo++;
            }
        } else if (spec.getAlarmMap().keySet().size() == 1) {
            ExportUtils.addTitleRow(rowNo, sheet, titleTplRow);
            String adaptationId = spec.getAlarmMap().keySet().iterator().next();
            addDataRows(rowNo, sheet, spec.getAlarmMap().get(adaptationId));
        } else {
            LOG.error("Empty alarm entries");
        }
    }

    private int addDataRows(int initRowNo, HSSFSheet sheet, List<ArsAlarm> dataList) {
        int rowNo = initRowNo + 1;
        for (ArsAlarm alarm : dataList) {
            HSSFRow newRow = sheet.getRow(rowNo);
            if (null == newRow) {
                newRow = sheet.createRow(rowNo);
            }

            setCell(newRow, 0, Double.valueOf(alarm.getSpecificProblem()));
            setCell(newRow, 1, alarm.isSupported() ? "Yes" : "No");
            setCell(newRow, 2, alarm.getSupportedOtherReleases());

            setCell(newRow, 3, alarm.getAlarmText());
            setCell(newRow, 4, alarm.isAlarmTextChanged() ? alarm.getLastAlarmText() : "");
            setCell(newRow, 5, alarm.isAlarmTextChanged() ? "Yes" : "No");

            setCell(newRow, 6, Double.valueOf(alarm.getProbableCause()));
            setCell(newRow, 7, alarm.isProbableCauseChanged() ? Double.valueOf(alarm.getLastProbableCause()) : "");
            setCell(newRow, 8, alarm.isProbableCauseChanged() ? "Yes" : "No");

            setCell(newRow, 9, alarm.getAlarmType());
            setCell(newRow, 10, alarm.isAlarmTypeChanged() ? alarm.getLastAlarmType() : "");
            setCell(newRow, 11, alarm.isAlarmTypeChanged() ? "Yes" : "No");


            setCell(newRow, 12, alarm.getPerceivedSeverityInfo());
            setCell(newRow, 13, alarm.isSeverityChanged() ? alarm.getLastPerceivedSeverityInfo() : "");
            setCell(newRow, 14, alarm.isSeverityChanged() ? "Yes" : "No");

            rowNo++;
        }
        return rowNo;
    }

    private void setCell(HSSFRow row, int cellNo, Object value) {
        ExportUtils.setCell(row, cellNo, value, dataTplRow);
    }
}
