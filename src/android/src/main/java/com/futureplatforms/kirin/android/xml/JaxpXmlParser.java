package com.futureplatforms.kirin.android.xml;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.futureplatforms.kirin.dependencies.xml.parser.Document;
import com.futureplatforms.kirin.dependencies.xml.parser.XMLParser;

public class JaxpXmlParser implements XMLParser {

    @Override
    public Document parse(String contents) {
    	org.w3c.dom.Document document = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(new InputSource(new StringReader(contents)));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JaxpDocument(document);
    }
}
