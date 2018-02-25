package com.longyg.backend.adaptation.topology;

import com.longyg.backend.TemplateRepository;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

public class ObjectModelSheetGenerator {
    private HSSFSheet objectModelSheet;
    private TopologyRepository topologyRepository = TopologyRepository.getInstance();
    private int startRow = TemplateRepository.getOmTplDef().getStartRow();
    private int row = startRow;

    public ObjectModelSheetGenerator(HSSFSheet objectModelSheet) {
        this.objectModelSheet = objectModelSheet;
    }

    public void generate() throws Exception {
        topologyRepository.buildTopology();

        List<PmbObject> rootObjects = topologyRepository.getRootObjects();

        for (PmbObject parentObj : rootObjects) {
            HSSFRow hssfRow = objectModelSheet.getRow(row);
            if (null == hssfRow) {
                hssfRow = objectModelSheet.createRow(row);
            }
            setCellValue(hssfRow, 0,"|- " + parentObj.getName());
            setObjectAttributes(hssfRow, parentObj);

            List<PmbObject> childObjs = parentObj.getChildObjects();
            Collections.sort(childObjs);
            for (PmbObject childObj : childObjs) {
                int level = 1;
                row++;
                printChildTopology(childObj, level);
            }
        }
    }

    private void printChildTopology(PmbObject rootObj, int level) {
        HSSFRow hssfRow = objectModelSheet.getRow(row);
        if (null == hssfRow) {
            hssfRow = objectModelSheet.createRow(row);
        }
        for (int i = 0; i < level; i++)
        {
            setCellValue(hssfRow, i, "|");
        }

        setCellValue(hssfRow, level,"|- " + rootObj.getName());
        setObjectAttributes(hssfRow, rootObj);

        List<PmbObject> childObjs = rootObj.getChildObjects();
        Collections.sort(childObjs);
        for (PmbObject childObj : childObjs) {
            row++;
            printChildTopology(childObj, level + 1);
        }
    }

    private void setObjectAttributes(HSSFRow hssfRow, PmbObject pmbObject) {
        setCellValue(hssfRow, 9, pmbObject.isMeasuredObject() ? "M" : "");
        setCellValue(hssfRow, 25, listToString(pmbObject.getSupportedVersions()));
        setCellValue(hssfRow, 26, pmbObject.isTransient() ? "Transient" : "MO");
        setCellValue(hssfRow, 27, pmbObject.getPresentation());
        setCellValue(hssfRow, 28, pmbObject.getNameInOmes());
    }


    private void setCellValue(HSSFRow hssfRow, int cell, Object value) {
        HSSFCell hssfCell = hssfRow.getCell(cell);
        if (null == hssfCell) {
            hssfCell = hssfRow.createCell(cell);

            HSSFCellStyle cellStyle = objectModelSheet.getRow(startRow).getCell(cell).getCellStyle();
            CellType cellType = objectModelSheet.getRow(startRow).getCell(cell).getCellTypeEnum();

            hssfCell.setCellStyle(cellStyle);
            hssfCell.setCellType(cellType);

        }
        if (value instanceof String) {
            hssfCell.setCellValue((null == value) ? "" : (String) value);
        } else if (value instanceof Boolean) {
            hssfCell.setCellValue((Boolean) value);
        } else if (value instanceof Integer) {
            hssfCell.setCellValue((Integer) value);
        } else {
            hssfCell.setCellValue((null == value) ? "" : value.toString());
        }
    }

    private String listToString(List<String> list) {
        StringBuffer sb = new StringBuffer();
        for (String l : list) {
            sb.append(l).append(";");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
