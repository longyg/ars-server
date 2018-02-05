package com.longyg.backend;

import com.longyg.backend.ars.tpl.ExcelTemplate;
import com.longyg.backend.ars.tpl.definition.objectmodel.ObjectModelTplDef;
import com.longyg.backend.ars.tpl.definition.userstory.TemplateDefinition;

public class TemplateRepository {
    private static TemplateDefinition tplDef;
    private static ObjectModelTplDef objectModelTplDef;
    private static ExcelTemplate template;

    public static ExcelTemplate getTemplate() {
        return template;
    }

    public static void setTemplate(ExcelTemplate template) {
        TemplateRepository.template = template;
    }

    public static TemplateDefinition getTplDef() {
        return tplDef;
    }

    public static void setTplDef(TemplateDefinition tplDef) {
        TemplateRepository.tplDef = tplDef;
    }

    public static ObjectModelTplDef getObjectModelTplDef() {
        return objectModelTplDef;
    }

    public static void setObjectModelTplDef(ObjectModelTplDef objectModelTplDef) {
        TemplateRepository.objectModelTplDef = objectModelTplDef;
    }
}
