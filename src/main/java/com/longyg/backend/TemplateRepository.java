package com.longyg.backend;

import com.longyg.backend.ars.tpl.ExcelTemplate;
import com.longyg.backend.ars.tpl.ExcelTemplateParser;
import com.longyg.backend.ars.tpl.definition.objectmodel.ObjectModelTplDef;
import com.longyg.backend.ars.tpl.definition.objectmodel.ObjectModelTplDefParser;
import com.longyg.backend.ars.tpl.definition.pmdataload.PmDataLoadTplDef;
import com.longyg.backend.ars.tpl.definition.pmdataload.PmDlTplDefParser;
import com.longyg.backend.ars.tpl.definition.userstory.TemplateDefParser;
import com.longyg.backend.ars.tpl.definition.userstory.TemplateDefinition;

public class TemplateRepository {
    public static final String XLS_TPL_PATH = "template.xls";
    private static final String US_TPL_DEF_PATH = "userstory.template.xml";
    private static final String OM_TPL_DEF_PATH = "objectmodel.template.xml";
    private static final String PMDL_TPL_DEF_PATH = "pmdataload.template.xml";

    private static TemplateDefinition usTplDef;
    private static ObjectModelTplDef omTplDef;
    private static PmDataLoadTplDef pmDlTplDef;
    private static ExcelTemplate template;

    private static boolean initialized = false;

    public static void init() throws Exception {
        if (initialized) {
            return;
        }
        TemplateDefParser tplDefParser = new TemplateDefParser();
        TemplateDefinition usTplDef = tplDefParser.parse(US_TPL_DEF_PATH);

        ObjectModelTplDefParser objectModelTplDefParser = new ObjectModelTplDefParser();
        ObjectModelTplDef omTplDef = objectModelTplDefParser.parse(OM_TPL_DEF_PATH);

        PmDlTplDefParser pmDlTplDefParser = new PmDlTplDefParser();
        PmDataLoadTplDef pmDlTplDef = pmDlTplDefParser.parse(PMDL_TPL_DEF_PATH);

        ExcelTemplateParser xlsTplParser = new ExcelTemplateParser();
        ExcelTemplate template = xlsTplParser.parse(XLS_TPL_PATH, usTplDef);

        TemplateRepository.template = template;
        TemplateRepository.usTplDef = usTplDef;
        TemplateRepository.omTplDef = omTplDef;
        TemplateRepository.pmDlTplDef = pmDlTplDef;

        initialized = true;
    }

    public static ExcelTemplate getTemplate() {
        return template;
    }

    public static void setTemplate(ExcelTemplate template) {
        TemplateRepository.template = template;
    }

    public static TemplateDefinition getUsTplDef() {
        return usTplDef;
    }

    public static void setUsTplDef(TemplateDefinition usTplDef) {
        TemplateRepository.usTplDef = usTplDef;
    }

    public static ObjectModelTplDef getOmTplDef() {
        return omTplDef;
    }

    public static void setOmTplDef(ObjectModelTplDef omTplDef) {
        TemplateRepository.omTplDef = omTplDef;
    }

    public static PmDataLoadTplDef getPmDlTplDef() { return pmDlTplDef; }

    public static void setPmDlTplDef(PmDataLoadTplDef pmDlTplDef) { TemplateRepository.pmDlTplDef = pmDlTplDef; }
}
