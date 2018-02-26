package com.longyg.backend.ars.export;

import com.longyg.backend.TemplateRepository;
import com.longyg.frontend.model.ars.ARS;
import com.longyg.frontend.model.ars.pm.PmDataLoadSpec;
import com.longyg.frontend.service.ArsService;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PmDataLoadExporter extends Exporter {
    @Autowired
    private ArsService arsService;

    private HSSFRow titleTplRow;

    private HSSFRow dataTplRow;

    public void export(ARS ars, HSSFWorkbook wb) {
        init(ars, wb);

        exportPmDataLoad();
    }

    private void exportPmDataLoad() {
        int sheetNo = TemplateRepository.getPmDlTplDef().getSheet();
        HSSFSheet sheet = this.wb.getSheetAt(sheetNo);

        PmDataLoadSpec spec = arsService.findPmDL(this.ars.getPmDataLoad());

        int titleTplRowNo = TemplateRepository.getPmDlTplDef().getTitleRow();
        this.titleTplRow = sheet.getRow(titleTplRowNo);

        int dataTplRowNo = TemplateRepository.getPmDlTplDef().getDataRow();
        this.dataTplRow = sheet.getRow(dataTplRowNo);


    }
}
