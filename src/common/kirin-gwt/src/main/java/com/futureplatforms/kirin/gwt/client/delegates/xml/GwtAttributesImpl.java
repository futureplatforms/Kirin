package com.futureplatforms.kirin.gwt.client.delegates.xml;

import com.futureplatforms.kirin.dependencies.xml.parser.Attributes;
import com.futureplatforms.kirin.dependencies.xml.parser.Node;
import com.google.gwt.xml.client.NamedNodeMap;

public class GwtAttributesImpl implements Attributes {

	private NamedNodeMap attributes;

	public GwtAttributesImpl(NamedNodeMap attributes) {
		this.attributes = attributes;
	}

	@Override
	public Node getNamedItem(String name) {
		return new GwtNodeImpl(attributes.getNamedItem(name));
	}

}
