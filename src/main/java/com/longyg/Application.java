package com.longyg;

import com.longyg.backend.TemplateRepository;
import com.longyg.backend.ars.tpl.ExcelTemplate;
import com.longyg.backend.ars.tpl.ExcelTemplateParser;
import com.longyg.backend.ars.tpl.definition.objectmodel.ObjectModelTplDef;
import com.longyg.backend.ars.tpl.definition.objectmodel.ObjectModelTplDefParser;
import com.longyg.backend.ars.tpl.definition.userstory.TemplateDefParser;
import com.longyg.backend.ars.tpl.definition.userstory.TemplateDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class Application {
    private static final String xlsTplPath = "template.xls";
    private static final String userStoryTplDefPath = "userstory.template.xml";
    private static final String objectModelTplDefPath = "objectmodel.template.xml";

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);

        init();
    }

    private static void init() throws Exception {
        TemplateDefParser tplDefParser = new TemplateDefParser();
        TemplateDefinition tplDef = tplDefParser.parse(userStoryTplDefPath);

        ObjectModelTplDefParser objectModelTplDefParser = new ObjectModelTplDefParser();
        ObjectModelTplDef objectModelTplDef = objectModelTplDefParser.parse(objectModelTplDefPath);

        ExcelTemplateParser xlsTplParser = new ExcelTemplateParser();
        ExcelTemplate template = xlsTplParser.parse(xlsTplPath, tplDef);

        TemplateRepository.setTplDef(tplDef);
        TemplateRepository.setTemplate(template);
        TemplateRepository.setObjectModelTplDef(objectModelTplDef);
    }
}
