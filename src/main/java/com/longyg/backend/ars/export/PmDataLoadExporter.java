package com.longyg.backend.ars.export;

import com.longyg.backend.TemplateRepository;
import com.longyg.backend.ars.tpl.definition.pmdataload.PmDataLoadTplDef;
import com.longyg.frontend.model.ars.ARS;
import com.longyg.frontend.model.ars.pm.ArsMeasurement;
import com.longyg.frontend.model.ars.pm.PmDataLoadSpec;
import com.longyg.frontend.service.ArsService;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PmDataLoadExporter extends Exporter {
    private static final Logger LOG = Logger.getLogger(PmDataLoadExporter.class);

    @Autowired
    private ArsService arsService;

    private HSSFRow adapIdTplRow;
    private HSSFRow titleTplRow;
    private HSSFRow dataTplRow;
    private HSSFRow colorTplRow;
    private HSSFRow statisticTotalTplRow;
    private HSSFRow statisticTitleTplRow;
    private HSSFRow statisticDataTplRow;
    private HSSFRow pmfileTitleTplRow;
    private HSSFRow pmfileDataTplRow;

    public void export(ARS ars, HSSFWorkbook wb) {
        init(ars, wb);

        exportPmDataLoad();
    }

    private void exportPmDataLoad() {
        int sheetNo = TemplateRepository.getPmDlTplDef().getSheet();
        HSSFSheet sheet = this.wb.getSheetAt(sheetNo);

        PmDataLoadTplDef tplDef = TemplateRepository.getPmDlTplDef();
        this.adapIdTplRow = sheet.getRow(tplDef.getAdapIdRow());
        this.titleTplRow = sheet.getRow(tplDef.getTitleRow());
        this.dataTplRow = sheet.getRow(tplDef.getDataRow());
        this.colorTplRow = sheet.getRow(tplDef.getCellColorRow());
        this.statisticTotalTplRow = sheet.getRow(tplDef.getStatisticRow());
        this.statisticTitleTplRow = sheet.getRow(tplDef.getStatisticRow() + 1);
        this.statisticDataTplRow = sheet.getRow(tplDef.getStatisticRow() + 2);
        this.pmfileTitleTplRow = sheet.getRow(tplDef.getPmfileRow());
        this.pmfileDataTplRow = sheet.getRow(tplDef.getPmfileRow() + 1);

        ExportUtils.cleanSheet(sheet);

        PmDataLoadSpec spec = arsService.findPmDL(this.ars.getPmDataLoad());

        generateXlsContent(sheet, spec);
    }

    private int generateXlsContent(HSSFSheet sheet, PmDataLoadSpec spec) {
        int rowNo = 0;
        rowNo = generateMeasurements(sheet, spec, rowNo);
        rowNo = rowNo + 1;
        rowNo = generateStatistics(sheet, spec, rowNo);
        rowNo = rowNo + 2;
        rowNo = generatePmFilesInfo(sheet, spec, rowNo);
        return rowNo;
    }

    private int generateMeasurements(HSSFSheet sheet, PmDataLoadSpec spec, int startRowNo) {
        int rowNo = startRowNo;
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
            rowNo = addDataRows(rowNo, sheet, spec.getMeasurementMap().get(adaptationId));
        } else {
            LOG.error("Empty PM Data Load entries");
        }
        return rowNo;
    }

    private int addDataRows(int startRowNo, HSSFSheet sheet, List<ArsMeasurement> dataList) {
        int rowNo = startRowNo + 1;
        for (ArsMeasurement meas : dataList) {
            HSSFRow newRow = sheet.getRow(rowNo);
            if (null == newRow) {
                newRow = sheet.createRow(rowNo);
            }

            if (meas.getTotalSizePerHour().doubleValue() > 2) {
                HSSFCellStyle cellStyle = colorTplRow.getCell(1).getCellStyle();
                CellType cellType = colorTplRow.getCell(1).getCellTypeEnum();
                ExportUtils.setCell(newRow, 0, meas.getName(), cellStyle, cellType);
            } else if (meas.getTableSizePerDay().doubleValue() > 2) {
                HSSFCellStyle cellStyle = colorTplRow.getCell(0).getCellStyle();
                CellType cellType = colorTplRow.getCell(0).getCellTypeEnum();
                ExportUtils.setCell(newRow, 0, meas.getName(), cellStyle, cellType);
            } else {
                setCell(newRow, 0, meas.getName());
            }

            setCell(newRow, 1, meas.getNameInOmes());
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

            HSSFCellStyle greenStyle = colorTplRow.getCell(2).getCellStyle();
            CellType greenType = colorTplRow.getCell(2).getCellTypeEnum();
            HSSFCellStyle redStyle = colorTplRow.getCell(3).getCellStyle();
            CellType redType = colorTplRow.getCell(3).getCellTypeEnum();

            if (meas.getTotalSizePerHour().doubleValue() > 2) {
                ExportUtils.setCell(newRow, 38, meas.getTotalSizePerHour(), redStyle, redType);
            } else {
                ExportUtils.setCell(newRow, 38, meas.getTotalSizePerHour(), greenStyle, greenType);
            }

            if (meas.getTableSizePerDay().doubleValue() > 2) {
                ExportUtils.setCell(newRow, 39, meas.getTableSizePerDay(), redStyle, redType);
            } else {
                ExportUtils.setCell(newRow, 39, meas.getTableSizePerDay(), greenStyle, greenType);
            }
            rowNo++;
        }
        return rowNo;
    }

    private int generateStatistics(HSSFSheet sheet, PmDataLoadSpec spec, int startRowNo) {
        int rowNo = startRowNo;

        rowNo = generateStatisticsTotal(sheet, spec, rowNo);
        rowNo++;
        rowNo = generateStatisticsTitle(sheet, rowNo);
        rowNo++;
        rowNo = generateStatisticsData(sheet, spec, rowNo);

        return rowNo;
    }

    private int generateStatisticsTotal(HSSFSheet sheet, PmDataLoadSpec spec, int startRow) {
        int rowNo = startRow;
        HSSFRow row = sheet.getRow(rowNo);
        if (null == row) {
            row = sheet.createRow(rowNo);
        }

        HSSFCellStyle cellStyle = statisticTotalTplRow.getCell(20).getCellStyle();
        CellType cellType = statisticTotalTplRow.getCell(20).getCellTypeEnum();
        ExportUtils.setCell(row, 20, "Total", cellStyle, cellType);

        HSSFCellStyle dataCellStyle = statisticTotalTplRow.getCell(31).getCellStyle();
        CellType dataCellType = statisticTotalTplRow.getCell(31).getCellTypeEnum();

        long totalDbRrPerNe = 0;
        long totalDbRcPerNe = 0;
        long totalDbMaxRows = 0;
        long totalDbMaxCtrs = 0;
        for (List<ArsMeasurement> dataList : spec.getMeasurementMap().values()) {
            for (ArsMeasurement meas : dataList) {
                totalDbRrPerNe += meas.getDbRrPerNe();
                totalDbRcPerNe += meas.getDbRcPerNe();
                totalDbMaxRows += meas.getDbMaxRows();
                totalDbMaxCtrs += meas.getDbMaxCtrs();
            }
        }
        ExportUtils.setCell(row, 31, totalDbRrPerNe, dataCellStyle, dataCellType);
        ExportUtils.setCell(row, 32, totalDbRcPerNe, dataCellStyle, dataCellType);
        ExportUtils.setCell(row, 34, totalDbMaxRows, dataCellStyle, dataCellType);
        ExportUtils.setCell(row, 35, totalDbMaxCtrs, dataCellStyle, dataCellType);

        return rowNo;
    }

    private int generateStatisticsTitle(HSSFSheet sheet, int startRow) {
        int rowNo = startRow;
        HSSFRow row = sheet.getRow(rowNo);
        if (null == row) {
            row = sheet.createRow(rowNo);
        }

        HSSFCellStyle oneNeCellStyle = statisticTitleTplRow.getCell(22).getCellStyle();
        CellType oneNeCellType = statisticTitleTplRow.getCell(22).getCellTypeEnum();
        ExportUtils.setCell(row, 22, "1 NE:", oneNeCellStyle, oneNeCellType);

        HSSFCellStyle maxCellStype = statisticTitleTplRow.getCell(28).getCellStyle();
        CellType maxCellType = statisticTitleTplRow.getCell(28).getCellTypeEnum();
        ExportUtils.setCell(row, 28, "Max:", maxCellStype, maxCellType);

        return rowNo;
    }

    private int generateStatisticsData(HSSFSheet sheet, PmDataLoadSpec spec, int startRow) {
        int rowNo = startRow;

        HSSFCellStyle leftTitleCellStyle = statisticDataTplRow.getCell(20).getCellStyle();
        CellType leftTitleCellType = statisticDataTplRow.getCell(20).getCellTypeEnum();

        HSSFCellStyle dataCellStype = statisticDataTplRow.getCell(22).getCellStyle();
        CellType dataCellType = statisticDataTplRow.getCell(22).getCellTypeEnum();

        long totalMphPerNe = 0;
        long totalCphPerNe = 0;
        long totalChaPerNe = 0;
        long totalCdaPerNe = 0;
        long totalMaxMph = 0;
        long totalMaxCph = 0;
        for (List<ArsMeasurement> dataList : spec.getMeasurementMap().values()) {
            for (ArsMeasurement meas : dataList) {
                totalMphPerNe += meas.getMphPerNE();
                totalCphPerNe += meas.getCphPerNE();
                totalChaPerNe += meas.getChaPerNE();
                totalCdaPerNe += meas.getCdaPerNe();
                totalMaxMph += meas.getMaxMph();
                totalMaxCph += meas.getMaxCph();
            }
        }

        HSSFRow dayRow = sheet.getRow(rowNo);
        if (null == dayRow) {
            dayRow = sheet.createRow(rowNo);
        }
        ExportUtils.setCell(dayRow, 20, "Day", leftTitleCellStyle, leftTitleCellType);
        ExportUtils.setCell(dayRow, 22, totalMphPerNe * 24, dataCellStype, dataCellType);
        ExportUtils.setCell(dayRow, 23, totalCphPerNe * 24, dataCellStype, dataCellType);
        ExportUtils.setCell(dayRow, 25, totalChaPerNe * 24, dataCellStype, dataCellType);
        ExportUtils.setCell(dayRow, 26, totalCdaPerNe * 24, dataCellStype, dataCellType);
        ExportUtils.setCell(dayRow, 28, totalMaxMph * 24, dataCellStype, dataCellType);
        ExportUtils.setCell(dayRow, 29, totalMaxCph * 24, dataCellStype, dataCellType);

        rowNo++;
        HSSFRow hourRow = sheet.getRow(rowNo);
        if (null == hourRow) {
            hourRow = sheet.createRow(rowNo);
        }
        ExportUtils.setCell(hourRow, 20, "Hour", leftTitleCellStyle, leftTitleCellType);
        ExportUtils.setCell(hourRow, 22, totalMphPerNe, dataCellStype, dataCellType);
        ExportUtils.setCell(hourRow, 23, totalCphPerNe, dataCellStype, dataCellType);
        ExportUtils.setCell(hourRow, 25, totalChaPerNe, dataCellStype, dataCellType);
        ExportUtils.setCell(hourRow, 26, totalCdaPerNe, dataCellStype, dataCellType);
        ExportUtils.setCell(hourRow, 28, totalMaxMph, dataCellStype, dataCellType);
        ExportUtils.setCell(hourRow, 29, totalMaxCph, dataCellStype, dataCellType);

        rowNo++;
        HSSFRow minuteRow = sheet.getRow(rowNo);
        if (null == minuteRow) {
            minuteRow = sheet.createRow(rowNo);
        }
        ExportUtils.setCell(minuteRow, 20, "Minute", leftTitleCellStyle, leftTitleCellType);
        ExportUtils.setCell(minuteRow, 22, totalMphPerNe / 60, dataCellStype, dataCellType);
        ExportUtils.setCell(minuteRow, 23, totalCphPerNe / 60, dataCellStype, dataCellType);
        ExportUtils.setCell(minuteRow, 25, totalChaPerNe / 60, dataCellStype, dataCellType);
        ExportUtils.setCell(minuteRow, 26, totalCdaPerNe / 60, dataCellStype, dataCellType);
        ExportUtils.setCell(minuteRow, 28, totalMaxMph / 60, dataCellStype, dataCellType);
        ExportUtils.setCell(minuteRow, 29, totalMaxCph / 60, dataCellStype, dataCellType);

        rowNo++;
        HSSFRow secondRow = sheet.getRow(rowNo);
        if (null == secondRow) {
            secondRow = sheet.createRow(rowNo);
        }
        ExportUtils.setCell(secondRow, 20, "Second", leftTitleCellStyle, leftTitleCellType);
        ExportUtils.setCell(secondRow, 22, totalMphPerNe / 3600, dataCellStype, dataCellType);
        ExportUtils.setCell(secondRow, 23, totalCphPerNe / 3600, dataCellStype, dataCellType);
        ExportUtils.setCell(secondRow, 25, totalChaPerNe / 3600, dataCellStype, dataCellType);
        ExportUtils.setCell(secondRow, 26, totalCdaPerNe / 3600, dataCellStype, dataCellType);
        ExportUtils.setCell(secondRow, 28, totalMaxMph / 3600, dataCellStype, dataCellType);
        ExportUtils.setCell(secondRow, 29, totalMaxCph / 3600, dataCellStype, dataCellType);

        return rowNo;
    }

    private int generatePmFilesInfo(HSSFSheet sheet, PmDataLoadSpec spec, int startRow) {
        int rowNo = startRow;

        HSSFCellStyle groupTitleCellStyle = pmfileTitleTplRow.getCell(0).getCellStyle();
        CellType groupTitleCellType = pmfileTitleTplRow.getCell(0).getCellTypeEnum();

        HSSFCellStyle pmfileTitleCellStyle = pmfileTitleTplRow.getCell(2).getCellStyle();
        CellType pmfileTitleCellType = pmfileTitleTplRow.getCell(2).getCellTypeEnum();

        HSSFCellStyle groupDataCellStyle = pmfileDataTplRow.getCell(0).getCellStyle();
        CellType groupDataCellType = pmfileDataTplRow.getCell(0).getCellTypeEnum();

        HSSFCellStyle pmfileTotalCellStyle = pmfileDataTplRow.getCell(1).getCellStyle();
        CellType pmfileTotalCellType = pmfileDataTplRow.getCell(1).getCellTypeEnum();

        HSSFCellStyle pmfileDataCellStyle = pmfileDataTplRow.getCell(2).getCellStyle();
        CellType pmfileDataCellType = pmfileDataTplRow.getCell(2).getCellTypeEnum();

        HSSFRow titleRow = sheet.getRow(rowNo);
        if (null == titleRow) {
            titleRow = sheet.createRow(rowNo);
        }
        ExportUtils.setCell(titleRow, 0, "DN Pattern/Group Name", groupTitleCellStyle, groupTitleCellType);
        ExportUtils.setCell(titleRow, 2, "Number of files per interval", pmfileTitleCellStyle, pmfileTitleCellType);

        rowNo++;
        HSSFRow dataRow = sheet.getRow(rowNo);
        if (null == dataRow) {
            dataRow = sheet.createRow(rowNo);
        }
        ExportUtils.setCell(dataRow, 0, "CSCF", groupDataCellStyle, groupDataCellType);
        ExportUtils.setCell(dataRow, 1, "Total", pmfileTotalCellStyle, pmfileTotalCellType);
        ExportUtils.setCell(dataRow, 2, "1 to 3", pmfileDataCellStyle, pmfileDataCellType);

        return rowNo;
    }

    private void setCell(HSSFRow row, int cellNo, Object value) {
        ExportUtils.setCell(row, cellNo, value, dataTplRow);
    }
}
