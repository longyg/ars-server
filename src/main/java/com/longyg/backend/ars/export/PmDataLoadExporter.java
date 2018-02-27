package com.longyg.backend.ars.export;

import com.longyg.backend.TemplateRepository;
import com.longyg.frontend.model.ars.ARS;
import com.longyg.frontend.model.ars.pm.ArsMeasurement;
import com.longyg.frontend.model.ars.pm.PmDataLoadSpec;
import com.longyg.frontend.service.ArsService;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class PmDataLoadExporter extends Exporter {
    private static final Logger LOG = Logger.getLogger(PmDataLoadExporter.class);

    @Autowired
    private ArsService arsService;

    private HSSFRow titleTplRow;

    private HSSFRow dataTplRow;

    public void export(ARS ars, HSSFWorkbook wb) {
        init(ars, wb);

        exportPmDataLoad();
    }

    private void exportPmDataLoad() {
        int sheetNo = TemplateRepository.getPmDlTplDef().getSheet();
        HSSFSheet sheet = this.wb.getSheetAt(sheetNo);

        int titleTplRowNo = TemplateRepository.getPmDlTplDef().getTitleRow();
        this.titleTplRow = sheet.getRow(titleTplRowNo);

        int dataTplRowNo = TemplateRepository.getPmDlTplDef().getDataRow();
        this.dataTplRow = sheet.getRow(dataTplRowNo);

        cleanSheet(sheet);

        PmDataLoadSpec spec = arsService.findPmDL(this.ars.getPmDataLoad());

        int rowNo = 0;
        if (spec.getMeasurementMap().keySet().size() > 1) {
            for (String adaptationId : spec.getMeasurementMap().keySet()) {
                addAdaptationIdRow(rowNo, sheet, adaptationId);
                rowNo++;
                addTitleRow(rowNo, sheet);
                rowNo = addDataRows(rowNo, sheet, spec.getMeasurementMap().get(adaptationId));
                rowNo++;
            }
        } else if (spec.getMeasurementMap().keySet().size() == 1) {
            addTitleRow(rowNo, sheet);
            String adaptationId = spec.getMeasurementMap().keySet().iterator().next();
            addDataRows(rowNo, sheet, spec.getMeasurementMap().get(adaptationId));
        } else {
            LOG.error("Empty PM Data Load entries");
        }
    }

    private void addTitleRow(int rowNo, HSSFSheet sheet) {
        HSSFRow newRow = sheet.getRow(rowNo);
        if (null == newRow) {
            newRow = sheet.createRow(rowNo);
        }

        for (int i = 0; i < titleTplRow.getLastCellNum(); i++) {
            // Grab a copy of the old/new cell
            HSSFCell oldCell = titleTplRow.getCell(i);
            HSSFCell newCell = newRow.createCell(i);

            // If the old cell is null jump to next cell
            if (oldCell == null) {
                newCell = null;
                continue;
            }

            // Copy style from old cell and apply to new cell
            newCell.setCellStyle(oldCell.getCellStyle());

            // If there is a cell comment, copy
            if (oldCell.getCellComment() != null) {
                newCell.setCellComment(oldCell.getCellComment());
            }

            // If there is a cell hyperlink, copy
            if (oldCell.getHyperlink() != null) {
                newCell.setHyperlink(oldCell.getHyperlink());
            }

            // Set the cell data type
            newCell.setCellType(oldCell.getCellTypeEnum());

            // Set the cell data value
            switch (oldCell.getCellTypeEnum()) {
                case BLANK:
                    newCell.setCellValue(oldCell.getStringCellValue());
                    break;
                case BOOLEAN:
                    newCell.setCellValue(oldCell.getBooleanCellValue());
                    break;
                case ERROR:
                    newCell.setCellErrorValue(oldCell.getErrorCellValue());
                    break;
                case FORMULA:
                    newCell.setCellFormula(oldCell.getCellFormula());
                    break;
                case NUMERIC:
                    newCell.setCellValue(oldCell.getNumericCellValue());
                    break;
                case STRING:
                    newCell.setCellValue(oldCell.getRichStringCellValue());
                    break;
            }
        }
    }

    private int addDataRows(int initRowNo, HSSFSheet sheet, List<ArsMeasurement> dataList) {
        int rowNo = initRowNo + 1;
        for (ArsMeasurement meas : dataList) {
            HSSFRow newRow = sheet.getRow(rowNo);
            if (null == newRow) {
                newRow = sheet.createRow(rowNo);
            }

            setCellValue(newRow, 0, meas.getName());
            setCellValue(newRow, 1, meas.getNameInOmes());
            setCellValue(newRow, 2, meas.getMeasuredObject());
            setCellValue(newRow, 3, meas.isSupported() ? "Yes" : "No");
            setCellValue(newRow, 4, meas.getSupportedOtherReleases());
            setCellValue(newRow, 5, meas.getDimension());
            setCellValue(newRow, 6, "");
            setCellValue(newRow, 7, "");
            setCellValue(newRow, 8, meas.getAvgPerNet());
            setCellValue(newRow, 9, meas.getMaxPerNet());
            setCellValue(newRow, 10, meas.getMaxPerNe());
            setCellValue(newRow, 11, meas.getCounterNumber());
            setCellValue(newRow, 12, meas.getCounterNumberOfLastVersion());
            setCellValue(newRow, 13, meas.getDelta());
            setCellValue(newRow, 14, meas.getAggObject());
            setCellValue(newRow, 15, meas.getTimeAgg());
            setCellValue(newRow, 16, meas.getBh());
            setCellValue(newRow, 17, meas.getActive());
            setCellValue(newRow, 18, meas.getDefaultInterval());
            setCellValue(newRow, 19, meas.getMinimalInterval());
            setCellValue(newRow, 20, meas.getStorageDays());
            setCellValue(newRow, 21, meas.getBytesPerCounter());
            setCellValue(newRow, 22, meas.getMphPerNE());
            setCellValue(newRow, 23, meas.getCphPerNE());
            setCellValue(newRow, 24, "");
            setCellValue(newRow, 25, meas.getChaPerNE());
            setCellValue(newRow, 26, meas.getCdaPerNe());
            setCellValue(newRow, 27, "");
            setCellValue(newRow, 28, meas.getMaxMph());
            setCellValue(newRow, 29, meas.getMaxCph());
            setCellValue(newRow, 30, meas.getMeasGroup());
            setCellValue(newRow, 31, meas.getDbRrPerNe());
            setCellValue(newRow, 32, meas.getDbRcPerNe());
            setCellValue(newRow, 33, meas.getMsPerNe());
            setCellValue(newRow, 34, meas.getDbMaxRows());
            setCellValue(newRow, 35, meas.getDbMaxCtrs());
            setCellValue(newRow, 36, meas.getMaxMs());
            setCellValue(newRow, 37, meas.getTotalBytesPerInterval());
            setCellValue(newRow, 38, meas.getTotalSizePerHour());
            setCellValue(newRow, 39, meas.getTableSizePerDay());

            rowNo++;
        }
        return rowNo;
    }

    private void setCellValue(HSSFRow row, int cellNo, Object value) {
        HSSFCell cell = row.getCell(cellNo);
        if (null == cell) {
            cell = row.createCell(cellNo);

            HSSFCellStyle cellStyle = dataTplRow.getCell(cellNo).getCellStyle();
            CellType cellType = dataTplRow.getCell(cellNo).getCellTypeEnum();

            cell.setCellStyle(cellStyle);
            cell.setCellType(cellType);

        }
        if (value instanceof String) {
            cell.setCellValue((null == value) ? "" : (String) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Integer || value instanceof Long || value instanceof Double || value instanceof BigDecimal) {
            cell.setCellValue(Double.valueOf(value.toString()));
        } else {
            cell.setCellValue((null == value) ? "" : value.toString());
        }
    }

    private void cleanSheet(HSSFSheet sheet) {
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            HSSFRow row = sheet.getRow(i);
            if (null != row) {
                for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
                    HSSFCell cell = row.getCell(j);
                    if (null != cell) {
                        if (cell.getCellComment() != null) {
                            cell.removeCellComment();
                        }
                    }
                }
                sheet.removeRow(row);
            }
        }
    }

    private void addAdaptationIdRow(int rowNo, HSSFSheet sheet, String adaptationId) {
        HSSFRow newRow = sheet.getRow(rowNo);
        if (null == newRow) {
            newRow = sheet.createRow(rowNo);
        }
        HSSFCell cell = newRow.getCell(0);
        if (null == cell) {
            cell = newRow.createCell(0);
        }

//        HSSFCellStyle cellStyle = this.wb.createCellStyle();
//        HSSFFont font = this.wb.createFont();
//        font.setFontHeightInPoints((short) 18);
//        cellStyle.setFont(font);
//        cell.setCellStyle(cellStyle);

        String adapId = adaptationId.replaceAll("_", ".");
        cell.setCellValue("Adaptation ID: " + adapId);
    }
}
