package com.longyg.backend.ars.export;

import com.longyg.frontend.model.ars.ARS;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Component;

@Component
public class PmDataLoadExporter extends Exporter {

    public void export(ARS ars, HSSFWorkbook wb) {
        init(ars, wb);


    }
}
