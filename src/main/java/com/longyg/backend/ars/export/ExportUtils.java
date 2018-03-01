package com.longyg.backend.ars.export;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

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
}
