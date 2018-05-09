package com.longyg.backend.ars.export;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.CellType;

import java.math.BigDecimal;

public class ExportUtils {
    public static void cleanSheet(HSSFSheet sheet) {
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

    public static void setCell(HSSFRow row, int cellNo, Object value, HSSFCellStyle cellStyle, CellType cellType) {
        HSSFCell cell = row.getCell(cellNo);
        if (null == cell) {
            cell = row.createCell(cellNo);
        }
        cell.setCellStyle(cellStyle);
        cell.setCellType(cellType);

        setCellValue(cell, value);
    }

    public static void setFormulaCell(HSSFRow row, int cellNo, String formula, HSSFCellStyle cellStyle) {
        HSSFCell cell = row.getCell(cellNo);
        if (null == cell) {
            cell = row.createCell(cellNo);
        }
        cell.setCellStyle(cellStyle);
        cell.setCellType(CellType.FORMULA);

        cell.setCellFormula(formula);
    }

    public static void setCell(HSSFRow row, int cellNo, Object value, HSSFRow dataTplRow) {
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

    public static void setFormulaCell(HSSFRow row, int cellNo, String formula, HSSFRow dataTplRow) {
        HSSFCell cell = row.getCell(cellNo);
        if (null == cell) {
            cell = row.createCell(cellNo);
        }

        HSSFCellStyle cellStyle = dataTplRow.getCell(cellNo).getCellStyle();
//        CellType cellType = dataTplRow.getCell(cellNo).getCellTypeEnum();

        cell.setCellStyle(cellStyle);
        cell.setCellType(CellType.FORMULA);

        cell.setCellFormula(formula);
    }

    private static void setCellValue(HSSFCell cell, Object value) {
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

    public static int addAdaptationIdRow(int rowNo, HSSFSheet sheet, String adaptationId, HSSFRow adapIdTplRow) {
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
        return rowNo;
    }

    public static int addTitleRow(int rowNo, HSSFSheet sheet, HSSFRow titleTplRow) {
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
        return rowNo;
    }
}
