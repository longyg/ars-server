package com.longyg.backend.ars.export;

import com.longyg.backend.TemplateRepository;
import com.longyg.frontend.model.ars.ARS;
import com.longyg.frontend.model.ars.om.ObjectClassInfo;
import com.longyg.frontend.model.ars.om.ObjectModelSpec;
import com.longyg.frontend.service.ArsService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;
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
        for (int i = 0; i < colNo - 1; i++) {
            setCellValue(row, i, "|");
        }
        setCellValue(row, colNo,"|- " + oci.getName());
    }

    private void setObjectAttributes(HSSFRow row, ObjectClassInfo oci) {
        setCellValue(row, 9, oci.isMeasuredObject() ? "M" : "");
        setCellValue(row, 25, oci.getSupportedReleases());
        setCellValue(row, 26, oci.isTransient() ? "Transient" : "MO");
        setCellValue(row, 27, oci.getPresentation());
        setCellValue(row, 28, oci.getNameInOmes());
    }

    private void setCellValue(HSSFRow row, int cellNo, Object value) {
        HSSFCell cell = row.getCell(cellNo);
        if (null == cell) {
            cell = row.createCell(cellNo);

            HSSFCellStyle cellStyle = this.startRow.getCell(cellNo).getCellStyle();
            CellType cellType = this.startRow.getCell(cellNo).getCellTypeEnum();

            cell.setCellStyle(cellStyle);
            cell.setCellType(cellType);

        }
        if (value instanceof String) {
            cell.setCellValue((null == value) ? "" : (String) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else {
            cell.setCellValue((null == value) ? "" : value.toString());
        }
    }
}
