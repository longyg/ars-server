package com.longyg.backend.ars.export;

import com.longyg.backend.TemplateRepository;
import com.longyg.frontend.model.ars.ARS;
import com.longyg.frontend.model.ars.counter.ArsCounter;
import com.longyg.frontend.model.ars.counter.CounterMeas;
import com.longyg.frontend.model.ars.counter.CounterSpec;
import com.longyg.frontend.model.ars.pm.ArsMeasurement;
import com.longyg.frontend.service.ArsService;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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
                setCell(newRow, 2, meas.getMeasuredObject());
                setCell(newRow, 3, meas.isSupported() ? "Yes" : "No");
                setCell(newRow, 4, meas.getSupportedOtherReleases());
                setCell(newRow, 5, meas.getDimension());
                setCell(newRow, 6, "");
                setCell(newRow, 7, "");
                setCell(newRow, 8, meas.getAvgPerNet());
                setCell(newRow, 9, meas.getMaxPerNet());
                setCell(newRow, 10, meas.getMaxPerNe());
                setCell(newRow, 11, meas.getCounterNumber());
                setCell(newRow, 12, meas.getCounterNumberOfLastVersion());
                setCell(newRow, 13, meas.getDelta());
                setCell(newRow, 14, meas.getAggObject());
                setCell(newRow, 15, meas.getTimeAgg());
                setCell(newRow, 16, meas.getBh());
                setCell(newRow, 17, meas.getActive());
                setCell(newRow, 18, meas.getDefaultInterval());
                setCell(newRow, 19, meas.getMinimalInterval());
                setCell(newRow, 20, meas.getStorageDays());
                setCell(newRow, 21, meas.getBytesPerCounter());
                setCell(newRow, 22, meas.getMphPerNE());
                setCell(newRow, 23, meas.getCphPerNE());
                setCell(newRow, 24, "");
                setCell(newRow, 25, meas.getChaPerNE());
                setCell(newRow, 26, meas.getCdaPerNe());
                setCell(newRow, 27, "");
                setCell(newRow, 28, meas.getMaxMph());
                setCell(newRow, 29, meas.getMaxCph());
                setCell(newRow, 30, meas.getMeasGroup());
                setCell(newRow, 31, meas.getDbRrPerNe());
                setCell(newRow, 32, meas.getDbRcPerNe());
                setCell(newRow, 33, meas.getMsPerNe());
                setCell(newRow, 34, meas.getDbMaxRows());
                setCell(newRow, 35, meas.getDbMaxCtrs());
                setCell(newRow, 36, meas.getMaxMs());
                setCell(newRow, 37, meas.getTotalBytesPerInterval());

                rowNo++;
            }
        }
        return rowNo;
    }

    private void setCell(HSSFRow row, int cellNo, Object value, HSSFCellStyle cellStyle, CellType cellType) {
        HSSFCell cell = row.getCell(cellNo);
        if (null == cell) {
            cell = row.createCell(cellNo);
        }
        cell.setCellStyle(cellStyle);
        cell.setCellType(cellType);

        setCellValue(cell, value);
    }

    private void setCell(HSSFRow row, int cellNo, Object value) {
        HSSFCell cell = row.getCell(cellNo);
        if (null == cell) {
            cell = row.createCell(cellNo);
        }

        HSSFCellStyle cellStyle = dataTplRow.getCell(cellNo).getCellStyle();
        CellType cellType = dataTplRow.getCell(cellNo).getCellTypeEnum();

        cell.setCellStyle(cellStyle);
        cell.setCellType(cellType);

        setCellValue(cell, value);
    }

    private void setCellValue(HSSFCell cell, Object value) {
        if (value instanceof String) {
            cell.setCellValue((null == value) ? "" : (String) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Integer
                || value instanceof Long
                || value instanceof Double
                || value instanceof BigDecimal) {
            cell.setCellValue(Double.valueOf(value.toString()));
        } else {
            cell.setCellValue((null == value) ? "" : value.toString());
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

    private void addAdaptationIdRow(int rowNo, HSSFSheet sheet, String adaptationId) {
        HSSFRow newRow = sheet.getRow(rowNo);
        if (null == newRow) {
            newRow = sheet.createRow(rowNo);
        }
        HSSFCell cell = newRow.getCell(0);
        if (null == cell) {
            cell = newRow.createCell(0);
        }

        HSSFCellStyle cellStyle = adapIdTplRow.getCell(0).getCellStyle();
        CellType cellType = adapIdTplRow.getCell(0).getCellTypeEnum();

        cell.setCellStyle(cellStyle);
        cell.setCellType(cellType);

        String adapId = adaptationId.replaceAll("_", ".");
        cell.setCellValue("Adaptation ID: " + adapId);
    }
}
