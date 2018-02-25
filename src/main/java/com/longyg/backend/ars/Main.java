package com.longyg.backend.ars;

import com.longyg.backend.TemplateRepository;
import com.longyg.backend.adaptation.main.AdaptationCollector;
import com.longyg.backend.ars.tpl.ExcelTemplate;
import com.longyg.backend.ars.tpl.ExcelTemplateParser;
import com.longyg.backend.ars.tpl.Variable;
import com.longyg.backend.ars.tpl.VariablesRepository;
import com.longyg.backend.ars.tpl.definition.objectmodel.ObjectModelTplDef;
import com.longyg.backend.ars.tpl.definition.objectmodel.ObjectModelTplDefParser;
import com.longyg.backend.ars.tpl.definition.userstory.TemplateDefParser;
import com.longyg.backend.ars.tpl.definition.userstory.TemplateDefinition;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
    private static final String xlsTplPath = "template.xls";
    private static final String tplDefPath = "userstory.template.xml";
    private static final String objectModelTplDefPath = "objectmodel.template.xml";

    public static void main(String[] args) throws Exception {
        TemplateDefParser tplDefParser = new TemplateDefParser();
        TemplateDefinition tplDef = tplDefParser.parse(tplDefPath);

        ObjectModelTplDefParser objectModelTplDefParser = new ObjectModelTplDefParser();
        ObjectModelTplDef objectModelTplDef = objectModelTplDefParser.parse(objectModelTplDefPath);

        ExcelTemplateParser xlsTplParser = new ExcelTemplateParser();
        ExcelTemplate template = xlsTplParser.parse(xlsTplPath, tplDef);

        TemplateRepository.setUsTplDef(tplDef);
        TemplateRepository.setTemplate(template);
        TemplateRepository.setOmTplDef(objectModelTplDef);

        prepareAdaptation();
        readVariableInput();

        ExcelGenerator generator = new ExcelGenerator(xlsTplParser.getWb());
        String outFileName = template.getUsExcelTemplate().getBasicTemplate().getTitleTemplate().getReal();
        generator.generate(outFileName + ".xls");
    }

    private static void prepareAdaptation() throws Exception {
        AdaptationCollector adaptationCollector = new AdaptationCollector("D:\\workspace\\IntelliJProjects\\ars\\myconfig\\cscf_18.0VNF_config.json");
        adaptationCollector.initialize();
    }

    private static void readVariableInput() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input = null;
        for (Variable variable : VariablesRepository.getVariables()) {
            System.out.println(variable.getName() + ":");
            input = reader.readLine();
            VariablesRepository.setValue(variable.getName(), input);
        }
    }
}
