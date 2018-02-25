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

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);

        init();
    }

    private static void init() throws Exception {
        TemplateRepository.init();
    }
}
