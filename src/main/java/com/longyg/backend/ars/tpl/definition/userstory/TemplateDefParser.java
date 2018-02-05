package com.longyg.backend.ars.tpl.definition.userstory;

import org.springframework.stereotype.Component;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;

@Component
public class TemplateDefParser {
    public TemplateDefinition parse(String templatePath) throws FileNotFoundException, XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader eventReader = factory.createXMLEventReader(new FileReader(templatePath));

        TemplateDefinition templateDef = new TemplateDefinition();
        Basic basic = new Basic();
        templateDef.setBasic(basic);

        while (eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();
            switch (event.getEventType()) {
                case XMLStreamConstants.START_ELEMENT:
                    StartElement startElement = event.asStartElement();
                    String qName = startElement.getName().getLocalPart();
                    if (qName.equals("template")) {
                        Iterator<Attribute> attributes = startElement.getAttributes();
                        while (attributes.hasNext()) {
                            Attribute attribute = attributes.next();
                            String name = attribute.getName().getLocalPart();
                            if (name.equals("name")) {
                                templateDef.setName(attribute.getValue());
                            } else if (name.equals("sheet")) {
                                templateDef.setSheet(Integer.valueOf(attribute.getValue()));
                            }
                        }
                    } else if (qName.equals("title")) {
                        Title title = new Title();
                        Iterator<Attribute> attributes = startElement.getAttributes();
                        title.setRow(Integer.valueOf(attributes.next().getValue()));
                        templateDef.getBasic().setTitle(title);
                    } else if (qName.equals("info")) {
                        Info info = new Info();
                        Iterator<Attribute> attributes = startElement.getAttributes();
                        while (attributes.hasNext()) {
                            Attribute attribute = attributes.next();
                            String name = attribute.getName().getLocalPart();
                            if (name.equals("name")) {
                                info.setName(attribute.getValue());
                            } else if (name.equals("row")) {
                                info.setRow(Integer.valueOf(attribute.getValue()));
                            }
                        }
                        templateDef.getBasic().add(info);
                    } else if (qName.equals("us")) {
                        US us = new US();
                        Iterator<Attribute> attributes = startElement.getAttributes();
                        while (attributes.hasNext()) {
                            Attribute attribute = attributes.next();
                            String name = attribute.getName().getLocalPart();
                            if (name.equals("name")) {
                                us.setName(attribute.getValue());
                            } else if (name.equals("row")) {
                                us.setRow(Integer.valueOf(attribute.getValue()));
                            } else if (name.equals("sub")) {
                                us.setSub(attribute.getValue());
                            }
                        }
                        templateDef.addUs(us);
                    }
                break;
            }
        }
        return templateDef;
    }

    public static void main(String[] args) throws Exception {
        TemplateDefParser parser = new TemplateDefParser();
        TemplateDefinition template = parser.parse("template.xml");
        System.out.println(template.getBasic().getTitle().getRow());
        System.out.println(template.getSheet());
        System.out.println(template.getName());
        System.out.println(template.getBasic().getInfoList().size());
        for (Info info : template.getBasic().getInfoList()) {
            System.out.println(info.getName() + ", " + info.getRow());
        }
        for (US us : template.getUsList()) {
            System.out.println(us.getName() + "," + us.getRow() + "," + us.getSub());
        }
    }
}
