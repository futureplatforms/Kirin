package com.futureplatforms.kirin.dependencies.xml;

import com.futureplatforms.kirin.dependencies.xml.parser.Node;

public interface XML {
	public void parse(Node d) throws XMLException;
	public void clone(XML xml);
	public String serialize() throws XMLException;
}
