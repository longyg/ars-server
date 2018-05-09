package com.longyg.backend.ars.export;

import com.longyg.backend.TemplateRepository;
import com.longyg.frontend.model.ars.ARS;
import com.longyg.frontend.model.ars.om.ObjectClassInfo;
import com.longyg.frontend.model.ars.om.ObjectModelSpec;
import com.longyg.frontend.service.ArsService;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObjectModelExporter extends Exporter {
    @Autowired
    private ArsService arsService;

    private HSSFRow startRow;

    public void export(ARS ars, HSSFWorkbook wb) {
        init(ars, wb);

        exportObjectModel();
    }

    private void exportObjectModel() {
        int sheetNo = TemplateRepository.getOmTplDef().getSheet();
        HSSFSheet sheet = this.wb.getSheetAt(sheetNo);

        ObjectModelSpec spec = arsService.findOm(this.ars.getObjectModel());

        int startRowNo = TemplateRepository.getOmTplDef().getStartRow();
        this.startRow = sheet.getRow(startRowNo);

        spec.getOciMap().values().forEach(ociList -> {
            ociList.forEach(oci -> {
                HSSFRow row = sheet.getRow(oci.getRow());
                if (null == row) {
                    row = sheet.createRow(oci.getRow());
                }

                setObjectName(row, oci);
                setObjectAttributes(row, oci);
            });
        });
    }

    private void setObjectName(HSSFRow row, ObjectClassInfo oci) {
        int colNo = oci.getColumn();
        /**
        for (int i = 0; i < colNo; i++) {
            setCellValue(row, i, "|");
        }
         **/
        setCell(row, colNo,"|- " + oci.getName());
    }

    private void setObjectAttributes(HSSFRow row, ObjectClassInfo oci) {
        setCell(row, 8, oci.isAlarmingObject() ? "A" : "");
        setCell(row, 9, oci.isMeasuredObject() ? "M" : "");
        setCell(row, 10, oci.isCmObject() ? "x" : "");
        setCell(row, 11, oci.isHasIcon() ? "x" : "");
        setCell(row, 12, oci.isHasGuiLuanch() ? "x" : "");
        setCell(row, 13, oci.getTgppObject());
        setCell(row, 14, oci.getIntVersion());
        setCell(row, 15, oci.getIntNasda());
        setCell(row, 16, oci.getMin());
        setCell(row, 17, oci.getMax());
        setCell(row, 18, oci.getAvg());
        setCell(row, 19, oci.getAvgPerNet());
        setCell(row, 20, oci.getMaxPerNet());
        setCell(row, 21, oci.getMaxPerNE());
        setCell(row, 22, oci.getMaxNePerNet());
        setCell(row, 23, oci.getAvgNePerNet());
        setCell(row, 24, oci.isMocrNeeded() ? "x" : "");
        setCell(row, 25, oci.getSupportedReleases());
        setCell(row, 26, oci.isTransient() ? "Transient" : "MO");
        setCell(row, 27, oci.getPresentation());
        setCell(row, 28, oci.getNameInOmes());
        setCell(row, 29, oci.getAdaptationId());
        setCell(row, 30, oci.getComment());
    }

    private void setCell(HSSFRow row, int cellNo, Object value) {
        ExportUtils.setCell(row, cellNo, value, startRow);
    }
}
