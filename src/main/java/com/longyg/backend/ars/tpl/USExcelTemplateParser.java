package com.longyg.backend.ars.tpl;

import com.longyg.backend.ars.tpl.definition.userstory.Info;
import com.longyg.backend.ars.tpl.definition.userstory.TemplateDefinition;
import com.longyg.backend.ars.tpl.definition.userstory.US;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class USExcelTemplateParser {
    private USExcelTemplate template;
    private HSSFWorkbook wb;
    private TemplateDefinition tplDef;

    public USExcelTemplate parse(HSSFWorkbook wb, TemplateDefinition tplDef) {
        this.wb = wb;
        this.tplDef = tplDef;

        template = new USExcelTemplate();
        template.setName(tplDef.getName());

        HSSFSheet usSheet = wb.getSheetAt(tplDef.getSheet());

        BasicTemplate basicTemplate = new BasicTemplate();
        TitleTemplate titleTemplate = new TitleTemplate();

        HSSFRow titleRow = usSheet.getRow(tplDef.getBasic().getTitle().getRow());
        String titleTpl = titleRow.getCell(Constants.TITLE_COL).getStringCellValue();
        titleTemplate.setTpl(titleTpl);

        List<AdapInfoTemplate> adapInfoTemplateList = new ArrayList<>();
        for (Info info : tplDef.getBasic().getInfoList()) {
            AdapInfoTemplate adapTpl = new AdapInfoTemplate();
            adapTpl.setName(info.getName());
            String presentation = usSheet.getRow(info.getRow()).getCell(Constants.ADAP_INFO_PRESENTATION_COL).getStringCellValue();
            adapTpl.setPresentation(presentation);

            HSSFRow row = usSheet.getRow(info.getRow());
            HSSFCell cell = row.getCell(Constants.ADAP_INFO_VALUE_COL);
            String tpl = cell.getStringCellValue();
            adapTpl.setTpl(tpl);
            if (!adapInfoTemplateList.contains(adapTpl)) {
                adapInfoTemplateList.add(adapTpl);
            }
        }
        basicTemplate.setAdapInfoTemplateList(adapInfoTemplateList);
        basicTemplate.setTitleTemplate(titleTemplate);

        List<UserStoryTemplate> userStoryTemplates = new ArrayList<>();
        for (US us : tplDef.getUsList()) {
            UserStoryTemplate userStoryTemplate = new UserStoryTemplate();
            userStoryTemplate.setName(us.getName());
            HSSFRow row = usSheet.getRow(us.getRow());
            HSSFCell cell = row.getCell(Constants.US_TITLE_COL);
            String tpl = cell.getStringCellValue();
            userStoryTemplate.setTpl(tpl);

            parseSubTasks(userStoryTemplate, us, usSheet);

            if (!userStoryTemplates.contains(userStoryTemplate)) {
                userStoryTemplates.add(userStoryTemplate);
            }
        }

        template.setBasicTemplate(basicTemplate);
        template.setUserStoryTemplateList(userStoryTemplates);
        return template;
    }

    public USExcelTemplate getTemplate() {
        return template;
    }

    public void setTemplate(USExcelTemplate template) {
        this.template = template;
    }

    private void parseSubTasks(UserStoryTemplate template, US us, HSSFSheet usSheet) {
        String sub = us.getSub();
        if (sub.contains(",")) {
            String[] ss = sub.split(",");
            int startRow = Integer.valueOf(ss[0].trim());
            int endRow = Integer.valueOf(ss[1].trim());

            for (int i = startRow; i <= endRow; i++) {
                HSSFRow row = usSheet.getRow(i);
                SubTaskTemplate subTaskTemplate = createSubTaskTemplate(row);
                subTaskTemplate.setId(template.getName() + "_" + i);
                subTaskTemplate.setRow(i);
                template.addSubTask(subTaskTemplate);
            }
        } else {
            int rowIndex = Integer.valueOf(sub.trim());
            HSSFRow row = usSheet.getRow(rowIndex);
            SubTaskTemplate subTaskTemplate = createSubTaskTemplate(row);
            subTaskTemplate.setId(template.getName() + "_" + rowIndex);
            subTaskTemplate.setRow(rowIndex);
            template.addSubTask(subTaskTemplate);
        }
    }

    private SubTaskTemplate createSubTaskTemplate(HSSFRow row) {
        SubTaskTemplate subTaskTemplate = new SubTaskTemplate();
        HSSFCell cell = row.getCell(Constants.SUB_SELECTED_COL);
        if (null != cell && cell.getStringCellValue().trim().equalsIgnoreCase("X")) {
            subTaskTemplate.setSelected(true);
        }

        cell = row.getCell(Constants.SUB_NAME_COL);
        if (null != cell) {
            subTaskTemplate.setName(cell.getStringCellValue());
        }

        cell = row.getCell(Constants.SUB_DESCRIPTION_COL);
        if (null != cell) {
            subTaskTemplate.setDescription(new VariableTemplate(cell.getStringCellValue()));
        }

        cell = row.getCell(Constants.SUB_RATIONALE_COL);
        if (null != cell) {
            subTaskTemplate.setRationale(new VariableTemplate(cell.getStringCellValue()));
        }

        cell = row.getCell(Constants.SUB_ISSUE_COL);
        if (null != cell) {
            subTaskTemplate.setIssue(new VariableTemplate(cell.getStringCellValue()));
        }

        return subTaskTemplate;
    }
}
