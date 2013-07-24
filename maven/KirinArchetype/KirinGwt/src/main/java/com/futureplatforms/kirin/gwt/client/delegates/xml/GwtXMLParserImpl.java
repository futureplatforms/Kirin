package com.futureplatforms.kirin.gwt.client.delegates.xml;

import com.futureplatforms.kirin.dependencies.xml.parser.Node;
import com.futureplatforms.kirin.dependencies.xml.parser.XMLParser;

public class GwtXMLParserImpl implements XMLParser {
    @Override
    public Node parse(String contents) {
        return new GwtNodeImpl(com.google.gwt.xml.client.XMLParser.parse(contents));
    }
}
