package com.longyg.backend.ars.export;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ExportTest {
    private static final String XLS_TPL_PATH = "template.xls";

    public static void main(String[] args) throws Exception {
        HSSFWorkbook wb = null;
        try (FileInputStream fi = new FileInputStream(XLS_TPL_PATH);) {
            wb = new HSSFWorkbook(fi);

            // wb.createSheet("Test");
            cleanSheet(wb.getSheetAt(6));

            wb.removeSheetAt(6);

            String outPath = "ars/test/test.xls";
            FileOutputStream fileOut = new FileOutputStream(outPath);
            wb.write(fileOut);

        } catch (Exception e) {
            throw new Exception("Exception while initializing workbook via template", e);
        } finally {
            if (wb != null) {
                wb.close();
            }
        }

    }

    private static void cleanSheet(HSSFSheet sheet) {
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
