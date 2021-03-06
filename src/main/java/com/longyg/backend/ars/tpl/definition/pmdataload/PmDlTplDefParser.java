package com.longyg.backend.ars.tpl.definition.pmdataload;

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

public class PmDlTplDefParser {
    public PmDataLoadTplDef parse(String templatePath) throws FileNotFoundException, XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader eventReader = factory.createXMLEventReader(new FileReader(templatePath));

        PmDataLoadTplDef tplDef = new PmDataLoadTplDef();
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
                                tplDef.setName(attribute.getValue());
                            } else if (name.equals("sheet")) {
                                tplDef.setSheet(Integer.valueOf(attribute.getValue()));
                            }
                        }
                    } else if (qName.equals("adaptationId")) {
                        Iterator<Attribute> attributes = startElement.getAttributes();
                        tplDef.setAdapIdRow(Integer.valueOf(attributes.next().getValue()));
                    } else if (qName.equals("title")) {
                        Iterator<Attribute> attributes = startElement.getAttributes();
                        tplDef.setTitleRow(Integer.valueOf(attributes.next().getValue()));
                    } else if (qName.equals("data")) {
                        Iterator<Attribute> attributes = startElement.getAttributes();
                        tplDef.setDataRow(Integer.valueOf(attributes.next().getValue()));
                    } else if (qName.equals("cellColor")) {
                        Iterator<Attribute> attributes = startElement.getAttributes();
                        tplDef.setCellColorRow(Integer.valueOf(attributes.next().getValue()));
                    } else if (qName.equals("statistic")) {
                        Iterator<Attribute> attributes = startElement.getAttributes();
                        tplDef.setStatisticRow(Integer.valueOf(attributes.next().getValue()));
                    } else if (qName.equals("pmfile")) {
                        Iterator<Attribute> attributes = startElement.getAttributes();
                        tplDef.setPmfileRow(Integer.valueOf(attributes.next().getValue()));
                    }
                    break;
            }
        }
        return tplDef;
    }
}
