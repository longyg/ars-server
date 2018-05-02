package com.longyg.backend.ars.export;

import com.longyg.backend.TemplateRepository;
import com.longyg.backend.ars.tpl.definition.pmdataload.PmDataLoadTplDef;
import com.longyg.frontend.model.ars.ARS;
import com.longyg.frontend.model.ars.pm.ArsMeasurement;
import com.longyg.frontend.model.ars.pm.PmDataLoadSpec;
import com.longyg.frontend.service.ArsService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

@Component
public class PmDataLoadExporter extends Exporter {
    private static final Logger LOG = Logger.getLogger(PmDataLoadExporter.class.getName());

    @Autowired
    private ArsService arsService;

    private HSSFRow adapIdTplRow;
    private HSSFRow titleTplRow;
    private HSSFRow dataTplRow;
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
        int startRowNo = 0;

        int measStartRowNo = startRowNo + 1;
        int measEndRowNo = generateMeasurements(sheet, spec, measStartRowNo);
        int statisticStartRowNo = measEndRowNo + 2;
        int statisticEndRowNo = generateStatistics(sheet, statisticStartRowNo, measStartRowNo, measEndRowNo);
        int pmFileStartRowNo = statisticEndRowNo + 2;
        int pmFileEndRowNo = generatePmFilesInfo(sheet, spec, pmFileStartRowNo);

        int endRwoNo = pmFileEndRowNo;

        LOG.info(String.format("measStartRowNo: %d, measEndRowNo: %d, statisticStartRowNo: %d, " +
                "statisticEndRowNo: %d, pmFileStartRowNo: %d, pmFileEndRowNo: %d",
                measStartRowNo, measEndRowNo, statisticStartRowNo,
                statisticEndRowNo, pmFileStartRowNo, pmFileEndRowNo));
        return endRwoNo;
    }

    private int generateMeasurements(HSSFSheet sheet, PmDataLoadSpec spec, int startRowNo) {
        int rowNo = startRowNo - 1;
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
            LOG.severe("Empty PM Data Load entries");
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

            setCell(newRow, 0, meas.getName());
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

            int rowRealNo = rowNo + 1;

            // K2*R2*(60/T2)
            String formula = String.format("K%d*R%d*(60/T%d)", rowRealNo, rowRealNo, rowRealNo);
            setFormulaCell(newRow, 22, formula);

            // W2*L2
            formula = String.format("W%d*L%d", rowRealNo, rowRealNo);
            setFormulaCell(newRow, 23, formula);

            setCell(newRow, 24, "");

            // X2/(60/S2)
            formula = String.format("X%d/(60/S%d)", rowRealNo, rowRealNo);
            setFormulaCell(newRow, 25, formula);

            // 24*Z2/24
            formula = String.format("24*Z%d/24", rowRealNo);
            setFormulaCell(newRow, 26, formula);

            setCell(newRow, 27, "");

            // J2*R2*(60/T2)
            formula = String.format("J%d*R%d*(60/T%d)", rowRealNo, rowRealNo, rowRealNo);
            setFormulaCell(newRow, 28, formula);

            // AC2*L2
            formula = String.format("AC%d*L%d", rowRealNo, rowRealNo);
            setFormulaCell(newRow, 29, formula);

            setCell(newRow, 30, meas.getMeasGroup());

            // 24*W2*U2
            formula = String.format("24*W%d*U%d", rowRealNo, rowRealNo);
            setFormulaCell(newRow, 31, formula);

            // 24*X2*U2
            formula = String.format("24*X%d*U%d", rowRealNo, rowRealNo);
            setFormulaCell(newRow, 32, formula);

            // AG2*V2
            formula = String.format("AG%d*V%d", rowRealNo, rowRealNo);
            setFormulaCell(newRow, 33, formula);

            // 24*AC2*U2
            formula = String.format("24*AC%d*U%d", rowRealNo, rowRealNo);
            setFormulaCell(newRow, 34, formula);

            // 24*AD2*U2
            formula = String.format("24*AD%d*U%d", rowRealNo, rowRealNo);
            setFormulaCell(newRow, 35, formula);

            // AJ2*V2
            formula = String.format("AJ%d*V%d", rowRealNo, rowRealNo);
            setFormulaCell(newRow, 36, formula);

            // J2*L2*V2
            formula = String.format("J%d*L%d*V%d", rowRealNo, rowRealNo, rowRealNo);
            setFormulaCell(newRow, 37, formula);

            // (AL2*(60/T2))/(1024*1024*1024)
            formula = String.format("(AL%d*(60/T%d))/(1024*1024*1024)", rowRealNo, rowRealNo);
            setFormulaCell(newRow, 38, formula);

            // AM2*24
            formula = String.format("AM%d*24", rowRealNo);
            setFormulaCell(newRow, 39, formula);

            rowNo++;
        }

        setConditionFormatting(sheet, startRowNo + 2, rowNo);

        return rowNo;
    }

    private void setConditionFormatting(HSSFSheet sheet, int startRowNo, int endRowNo) {
        HSSFSheetConditionalFormatting scf = sheet.getSheetConditionalFormatting();

        String formula02 = String.format("AM%d > 2", startRowNo - 1);
        HSSFConditionalFormattingRule rule02 = scf.createConditionalFormattingRule(formula02);
        HSSFPatternFormatting pp02 = rule02.createPatternFormatting();
        pp02.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());
        HSSFFontFormatting fp02 = rule02.createFontFormatting();
        fp02.setFontColorIndex(IndexedColors.RED.getIndex());
        String rangeStr02 = String.format("A%d:A%d", startRowNo, endRowNo);
        CellRangeAddress[] range02 = {CellRangeAddress.valueOf(rangeStr02)};
        ConditionalFormattingRule[] rules02 = {rule02};
        scf.addConditionalFormatting(range02, rules02);

        String formula01 = String.format("AN%d > 2", startRowNo - 1);
        HSSFConditionalFormattingRule rule01 = scf.createConditionalFormattingRule(formula01);
        HSSFPatternFormatting pp01 = rule01.createPatternFormatting();
        pp01.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());
        String rangeStr01 = String.format("A%d:A%d", startRowNo, endRowNo);
        CellRangeAddress[] range01 = {CellRangeAddress.valueOf(rangeStr01)};
        ConditionalFormattingRule[] rules01 = {rule01};
        scf.addConditionalFormatting(range01, rules01);

        //
        HSSFConditionalFormattingRule rule1 = scf.createConditionalFormattingRule(ComparisonOperator.LT, "2");
        HSSFFontFormatting fp1 = rule1.createFontFormatting();
        fp1.setFontColorIndex(IndexedColors.GREEN.getIndex());
        HSSFPatternFormatting pp1 = rule1.createPatternFormatting();
        pp1.setFillBackgroundColor(IndexedColors.LIGHT_GREEN.getIndex());

        //
        HSSFConditionalFormattingRule rule2 = scf.createConditionalFormattingRule(ComparisonOperator.GT, "2");
        HSSFFontFormatting fp2 = rule2.createFontFormatting();
        fp2.setFontColorIndex(IndexedColors.DARK_RED.getIndex());
        HSSFPatternFormatting pp2 = rule2.createPatternFormatting();
        pp2.setFillBackgroundColor(IndexedColors.RED.getIndex());

        String rangeStr1 = String.format("AM%d:AM%d", startRowNo, endRowNo);
        CellRangeAddress[] range1 = {CellRangeAddress.valueOf(rangeStr1)};
        ConditionalFormattingRule[] rules = {rule1, rule2};
        scf.addConditionalFormatting(range1, rules);

        String rangeStr2 = String.format("AN%d:AN%d", startRowNo, endRowNo);
        CellRangeAddress[] range2 = {CellRangeAddress.valueOf(rangeStr2)};
        scf.addConditionalFormatting(range2, rules);
    }

    private int generateStatistics(HSSFSheet sheet, int startRowNo, int measStartRowNo, int measEndRowNo) {
        int totalStartRowNo = startRowNo;

        int totalEndRowNo = generateStatisticsTotal(sheet, totalStartRowNo, measStartRowNo, measEndRowNo);
        int titleStartRowNo = totalEndRowNo + 1;
        int titleEndRowNo = generateStatisticsTitle(sheet, titleStartRowNo);
        int dataStartRowNo = titleEndRowNo + 1;
        int dataEndRowNo = generateStatisticsData(sheet, dataStartRowNo, measStartRowNo, measEndRowNo);

        return dataEndRowNo;
    }

    private int generateStatisticsTotal(HSSFSheet sheet,
                                        int startRowNo, int measStartRowNo, int measEndRowNo) {
        int rowNo = startRowNo - 1;
        HSSFRow row = sheet.getRow(rowNo);
        if (null == row) {
            row = sheet.createRow(rowNo);
        }

        HSSFCellStyle cellStyle = statisticTotalTplRow.getCell(20).getCellStyle();
        CellType cellType = statisticTotalTplRow.getCell(20).getCellTypeEnum();
        ExportUtils.setCell(row, 20, "Total", cellStyle, cellType);

        HSSFCellStyle dataCellStyle = statisticTotalTplRow.getCell(31).getCellStyle();

        String formula = String.format("SUM(AF%d:AF%d)", measStartRowNo, measEndRowNo);
        ExportUtils.setFormulaCell(row, 31, formula, dataCellStyle);

        formula = String.format("SUM(AG%d:AG%d)", measStartRowNo, measEndRowNo);
        ExportUtils.setFormulaCell(row, 32, formula, dataCellStyle);

        formula = String.format("SUM(AI%d:AI%d)", measStartRowNo, measEndRowNo);
        ExportUtils.setFormulaCell(row, 34, formula, dataCellStyle);

        formula = String.format("SUM(AJ%d:AJ%d)", measStartRowNo, measEndRowNo);
        ExportUtils.setFormulaCell(row, 35, formula, dataCellStyle);

        return rowNo + 1;
    }

    private int generateStatisticsTitle(HSSFSheet sheet, int startRowNo) {
        int rowNo = startRowNo - 1;
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

        return rowNo + 1;
    }

    private int generateStatisticsData(HSSFSheet sheet, int startRowNo, int measStartRowNo, int measEndRowNo) {
        int rowNo = startRowNo - 1;

        HSSFCellStyle leftTitleCellStyle = statisticDataTplRow.getCell(20).getCellStyle();
        CellType leftTitleCellType = statisticDataTplRow.getCell(20).getCellTypeEnum();

        HSSFCellStyle dataCellStype = statisticDataTplRow.getCell(22).getCellStyle();

        HSSFRow dayRow = sheet.getRow(rowNo);
        if (null == dayRow) {
            dayRow = sheet.createRow(rowNo);
        }
        ExportUtils.setCell(dayRow, 20, "Day", leftTitleCellStyle, leftTitleCellType);

        String formula = String.format("24*SUM(W%d:W%d)", measStartRowNo, measEndRowNo);
        ExportUtils.setFormulaCell(dayRow, 22, formula, dataCellStype);
        formula = String.format("24*SUM(X%d:X%d)", measStartRowNo, measEndRowNo);
        ExportUtils.setFormulaCell(dayRow, 23, formula, dataCellStype);
        formula = String.format("24*SUM(Z%d:Z%d)", measStartRowNo, measEndRowNo);
        ExportUtils.setFormulaCell(dayRow, 25, formula, dataCellStype);
        formula = String.format("24*SUM(AA%d:AA%d)", measStartRowNo, measEndRowNo);
        ExportUtils.setFormulaCell(dayRow, 26, formula, dataCellStype);
        formula = String.format("24*SUM(AC%d:AC%d)", measStartRowNo, measEndRowNo);
        ExportUtils.setFormulaCell(dayRow, 28, formula, dataCellStype);
        formula = String.format("24*SUM(AD%d:AD%d)", measStartRowNo, measEndRowNo);
        ExportUtils.setFormulaCell(dayRow, 29, formula, dataCellStype);

        rowNo++;
        HSSFRow hourRow = sheet.getRow(rowNo);
        if (null == hourRow) {
            hourRow = sheet.createRow(rowNo);
        }
        ExportUtils.setCell(hourRow, 20, "Hour", leftTitleCellStyle, leftTitleCellType);

        formula = String.format("SUM(W%d:W%d)", measStartRowNo, measEndRowNo);
        ExportUtils.setFormulaCell(hourRow, 22, formula, dataCellStype);
        formula = String.format("SUM(X%d:X%d)", measStartRowNo, measEndRowNo);
        ExportUtils.setFormulaCell(hourRow, 23, formula, dataCellStype);
        formula = String.format("SUM(Z%d:Z%d)", measStartRowNo, measEndRowNo);
        ExportUtils.setFormulaCell(hourRow, 25, formula, dataCellStype);
        formula = String.format("SUM(AA%d:AA%d)", measStartRowNo, measEndRowNo);
        ExportUtils.setFormulaCell(hourRow, 26, formula, dataCellStype);
        formula = String.format("SUM(AC%d:AC%d)", measStartRowNo, measEndRowNo);
        ExportUtils.setFormulaCell(hourRow, 28, formula, dataCellStype);
        formula = String.format("SUM(AD%d:AD%d)", measStartRowNo, measEndRowNo);
        ExportUtils.setFormulaCell(hourRow, 29, formula, dataCellStype);

        rowNo++;
        HSSFRow minuteRow = sheet.getRow(rowNo);
        if (null == minuteRow) {
            minuteRow = sheet.createRow(rowNo);
        }
        ExportUtils.setCell(minuteRow, 20, "Minute", leftTitleCellStyle, leftTitleCellType);

        formula = String.format("SUM(W%d:W%d)/60", measStartRowNo, measEndRowNo);
        ExportUtils.setFormulaCell(minuteRow, 22, formula, dataCellStype);
        formula = String.format("SUM(X%d:X%d)/60", measStartRowNo, measEndRowNo);
        ExportUtils.setFormulaCell(minuteRow, 23, formula, dataCellStype);
        formula = String.format("SUM(Z%d:Z%d)/60", measStartRowNo, measEndRowNo);
        ExportUtils.setFormulaCell(minuteRow, 25, formula, dataCellStype);
        formula = String.format("SUM(AA%d:AA%d)/60", measStartRowNo, measEndRowNo);
        ExportUtils.setFormulaCell(minuteRow, 26, formula, dataCellStype);
        formula = String.format("SUM(AC%d:AC%d)/60", measStartRowNo, measEndRowNo);
        ExportUtils.setFormulaCell(minuteRow, 28, formula, dataCellStype);
        formula = String.format("SUM(AD%d:AD%d)/60", measStartRowNo, measEndRowNo);
        ExportUtils.setFormulaCell(minuteRow, 29, formula, dataCellStype);

        rowNo++;
        HSSFRow secondRow = sheet.getRow(rowNo);
        if (null == secondRow) {
            secondRow = sheet.createRow(rowNo);
        }
        ExportUtils.setCell(secondRow, 20, "Second", leftTitleCellStyle, leftTitleCellType);

        formula = String.format("SUM(W%d:W%d)/(60*60)", measStartRowNo, measEndRowNo);
        ExportUtils.setFormulaCell(secondRow, 22, formula, dataCellStype);
        formula = String.format("SUM(X%d:X%d)/(60*60)", measStartRowNo, measEndRowNo);
        ExportUtils.setFormulaCell(secondRow, 23, formula, dataCellStype);
        formula = String.format("SUM(Z%d:Z%d)/(60*60)", measStartRowNo, measEndRowNo);
        ExportUtils.setFormulaCell(secondRow, 25, formula, dataCellStype);
        formula = String.format("SUM(AA%d:AA%d)/(60*60)", measStartRowNo, measEndRowNo);
        ExportUtils.setFormulaCell(secondRow, 26, formula, dataCellStype);
        formula = String.format("SUM(AC%d:AC%d)/(60*60)", measStartRowNo, measEndRowNo);
        ExportUtils.setFormulaCell(secondRow, 28, formula, dataCellStype);
        formula = String.format("SUM(AD%d:AD%d)/(60*60)", measStartRowNo, measEndRowNo);
        ExportUtils.setFormulaCell(secondRow, 29, formula, dataCellStype);

        return rowNo + 1;
    }

    private int generatePmFilesInfo(HSSFSheet sheet, PmDataLoadSpec spec, int startRowNo) {
        int rowNo = startRowNo - 1;

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

        return rowNo + 1;
    }

    private void setCell(HSSFRow row, int cellNo, Object value) {
        ExportUtils.setCell(row, cellNo, value, dataTplRow);
    }

    private void setFormulaCell(HSSFRow row, int cellNo, String formula) {
        ExportUtils.setFormulaCell(row, cellNo, formula, dataTplRow);
    }
}
