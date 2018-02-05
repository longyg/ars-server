package com.longyg.backend.ars;

import com.longyg.backend.TemplateRepository;
import com.longyg.backend.ars.tpl.*;
import com.longyg.backend.ars.tpl.definition.userstory.Info;
import com.longyg.backend.ars.tpl.definition.userstory.TemplateDefinition;
import com.longyg.backend.ars.tpl.definition.userstory.US;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;

public class USExcelGenerator {
    private HSSFSheet usSheet;
    private TemplateDefinition tplDef = TemplateRepository.getTplDef();
    private ExcelTemplate template = TemplateRepository.getTemplate();

    public USExcelGenerator(HSSFSheet usSheet) {
        this.usSheet = usSheet;
    }

    public void generate() {
        HSSFRow titleRow = usSheet.getRow(tplDef.getBasic().getTitle().getRow());
        HSSFCell titleCell = titleRow.getCell(Constants.TITLE_COL);
        titleCell.setCellType(CellType.STRING);
        String value = template.getUsExcelTemplate().getBasicTemplate().getTitleTemplate().getReal();
        System.out.println(value);
        titleCell.setCellValue(value);

        for (Info info : tplDef.getBasic().getInfoList()) {
            HSSFRow row = usSheet.getRow(info.getRow());
            HSSFCell cell = row.getCell(Constants.ADAP_INFO_VALUE_COL);
            cell.setCellType(CellType.STRING);
            cell.setCellValue(getAdapInfo(info.getName()));
        }

        for (US us : tplDef.getUsList()) {
            HSSFRow usRow = usSheet.getRow(us.getRow());
            HSSFCell usCell = usRow.getCell(Constants.US_TITLE_COL);
            usCell.setCellType(CellType.STRING);

            UserStoryTemplate usTemplate = getUs(us.getName());
            if (usTemplate == null) {
                continue;
            }
            // set US title
            usCell.setCellValue(usTemplate.getReal());

            // Sub Tasks
            String sub = us.getSub();
            if (sub.contains(",")) {
                String[] ss = sub.split(",");
                int startRow = Integer.valueOf(ss[0].trim());
                int endRow = Integer.valueOf(ss[1].trim());

                for (int i = startRow; i <= endRow; i++) {
                    HSSFRow row = usSheet.getRow(i);
                    String subTaskId = us.getName() + "_" + i;
                    setSubTaskCells(us, usTemplate, subTaskId, row);
                }
            } else {
                int rowIndex = Integer.valueOf(sub.trim());
                HSSFRow row = usSheet.getRow(rowIndex);
                String subTaskId = us.getName() + "_" + rowIndex;
                setSubTaskCells(us, usTemplate, subTaskId, row);
            }
        }
    }

    private String getAdapInfo(String name) {
        for (AdapInfoTemplate adapInfoTemplate : template.getUsExcelTemplate().getBasicTemplate().getAdapInfoTemplateList()) {
            if (adapInfoTemplate.getName().equals(name)) {
                return adapInfoTemplate.getReal();
            }
        }
        return "";
    }

    private UserStoryTemplate getUs(String name) {
        for (UserStoryTemplate us : template.getUsExcelTemplate().getUserStoryTemplateList()) {
            if (us.getName().equals(name)) {
                return us;
            }
        }
        return null;
    }

    private String getSubTaskDescription(UserStoryTemplate usTemplate, String id) {
        for (SubTaskTemplate subTaskTemplate : usTemplate.getSubTaskTemplateList()) {
            if (subTaskTemplate.getId().equals(id)) {
                VariableTemplate description = subTaskTemplate.getDescription();
                if (null != description) {
                    return description.getReal();
                }
            }
        }
        return "";
    }

    private String getSubTaskRationale(UserStoryTemplate usTemplate, String id) {
        for (SubTaskTemplate subTaskTemplate : usTemplate.getSubTaskTemplateList()) {
            if (subTaskTemplate.getId().equals(id)) {
                VariableTemplate rationale = subTaskTemplate.getRationale();
                if (null != rationale) {
                    return rationale.getReal();
                }
            }
        }
        return "";
    }

    private String getSubTaskIssue(UserStoryTemplate usTemplate, String id) {
        for (SubTaskTemplate subTaskTemplate : usTemplate.getSubTaskTemplateList()) {
            if (subTaskTemplate.getId().equals(id)) {
                VariableTemplate issue = subTaskTemplate.getIssue();
                if (null != issue) {
                    return issue.getReal();
                }
            }
        }
        return "";
    }

    private void setSubTaskCells(US us, UserStoryTemplate usTemplate, String subTaskId, HSSFRow row) {
        // Description
        HSSFCell descCell = row.getCell(Constants.SUB_DESCRIPTION_COL);
        if (descCell == null) {
            descCell = row.createCell(Constants.SUB_DESCRIPTION_COL);
        }
        descCell.setCellType(CellType.STRING);
        descCell.setCellValue(getSubTaskDescription(usTemplate, subTaskId));

        // Rationale
        HSSFCell rationaleCell = row.getCell(Constants.SUB_RATIONALE_COL);
        if (rationaleCell == null) {
            rationaleCell = row.createCell(Constants.SUB_RATIONALE_COL);
        }
        rationaleCell.setCellType(CellType.STRING);
        rationaleCell.setCellValue(getSubTaskRationale(usTemplate, subTaskId));

        // Open issue
        HSSFCell issueCell = row.getCell(Constants.SUB_ISSUE_COL);
        if (issueCell == null) {
            issueCell = row.createCell(Constants.SUB_ISSUE_COL);
        }
        issueCell.setCellType(CellType.STRING);
        issueCell.setCellValue(getSubTaskIssue(usTemplate, subTaskId));
    }
}
