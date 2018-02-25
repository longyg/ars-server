package com.longyg.backend.ars.export;

import com.longyg.frontend.model.ars.ARS;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class Exporter {
    protected HSSFWorkbook wb;
    protected ARS ars;

    protected void init(ARS ars, HSSFWorkbook wb) {
        this.ars = ars;
        this.wb = wb;
    }

    public void export(ARS ars, HSSFWorkbook wb) {
        // do nothing
    }
}
