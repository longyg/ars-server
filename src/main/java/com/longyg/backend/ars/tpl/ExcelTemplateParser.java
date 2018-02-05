package com.longyg.backend.ars.tpl;

import com.longyg.backend.ars.tpl.definition.userstory.TemplateDefinition;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;

@Component
public class ExcelTemplateParser  {
    private static final Logger LOG = Logger.getLogger(ExcelTemplateParser.class);
    private TemplateDefinition tplDef;
    private HSSFWorkbook wb;

    public ExcelTemplate parse(String templatePath, TemplateDefinition tplDef) throws Exception {
        this.tplDef = tplDef;
        try (FileInputStream fi = new FileInputStream(templatePath);)
        {
            wb = new HSSFWorkbook(fi);
        }
        catch (Exception e)
        {
            LOG.error("Exception while parsing excel template", e);
            throw new Exception("Exception while parsing excel template", e);
        }

        // User Story sheet
        USExcelTemplateParser usExcelTemplateParser = new USExcelTemplateParser();
        USExcelTemplate usExcelTemplate = usExcelTemplateParser.parse(wb, this.tplDef);

        ExcelTemplate excelTemplate = new ExcelTemplate();
        excelTemplate.setUsExcelTemplate(usExcelTemplate);
        return excelTemplate;
    }

    public HSSFWorkbook getWb() {
        return wb;
    }

    public void setWb(HSSFWorkbook wb) {
        this.wb = wb;
    }
}
