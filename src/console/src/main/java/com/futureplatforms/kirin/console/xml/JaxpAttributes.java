package com.futureplatforms.kirin.console.xml;

import org.w3c.dom.NamedNodeMap;

import com.futureplatforms.kirin.dependencies.xml.parser.Attributes;
import com.futureplatforms.kirin.dependencies.xml.parser.Node;

public class JaxpAttributes implements Attributes {

	private NamedNodeMap namedNodeMap;

	public JaxpAttributes(NamedNodeMap namedNodeMap) {
		this.namedNodeMap = namedNodeMap;
	}

	@Override
	public Node getNamedItem(String name) {
		return new JaxpNode(namedNodeMap.getNamedItem(name));
	}

}
