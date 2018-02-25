package com.longyg.backend.ars;

import com.longyg.backend.TemplateRepository;
import com.longyg.backend.adaptation.topology.ObjectModelSheetGenerator;
import com.longyg.backend.ars.tpl.ExcelTemplate;
import com.longyg.backend.ars.tpl.definition.userstory.TemplateDefinition;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileOutputStream;

public class ExcelGenerator {
    private static final Logger LOG = Logger.getLogger(ExcelGenerator.class);
    private HSSFWorkbook wb;

    public ExcelGenerator(HSSFWorkbook wb) {
        this.wb = wb;
    }

    public void generate(String outPath) throws Exception {
        try (FileOutputStream fileOut = new FileOutputStream(outPath)) {

            int usSheet = TemplateRepository.getUsTplDef().getSheet();
            USExcelGenerator usExcelGenerator = new USExcelGenerator(wb.getSheetAt(usSheet));
            usExcelGenerator.generate();

            int objectModelSheet = TemplateRepository.getOmTplDef().getSheet();
            ObjectModelSheetGenerator omsGenerator = new ObjectModelSheetGenerator(wb.getSheetAt(objectModelSheet));
            omsGenerator.generate();

            wb.write(fileOut);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Exception while generating excel: ", e);
            throw new Exception("Exception while generating excel: ", e);
        } finally {
            wb.close();
        }
    }
}
