package com.futureplatforms.kirin.dependencies.xml.parser;

import com.futureplatforms.kirin.dependencies.xml.XMLException;

public interface XMLParser {
    public Document parse(String contents) throws XMLException;
}
