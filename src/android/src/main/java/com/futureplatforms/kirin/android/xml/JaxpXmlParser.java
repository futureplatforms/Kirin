package com.futureplatforms.kirin.android.xml;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.xml.sax.InputSource;

import com.futureplatforms.kirin.dependencies.xml.XMLException;
import com.futureplatforms.kirin.dependencies.xml.parser.Document;
import com.futureplatforms.kirin.dependencies.xml.parser.XMLParser;

public class JaxpXmlParser implements XMLParser {

	@Override
	public Document parse(String contents) throws XMLException {
		org.w3c.dom.Document document = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();

            contents = contents.replace("\uFEFF", ""); // remove BOM

            document = builder.parse(new InputSource(new StringReader(contents)));
			return new JaxpDocument(document);
		} catch (Exception e) {
			throw new XMLException(e);
		}
	}
}
