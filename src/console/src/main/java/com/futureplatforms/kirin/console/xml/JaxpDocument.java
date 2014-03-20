package com.futureplatforms.kirin.console.xml;

import com.futureplatforms.kirin.dependencies.xml.parser.Document;
import com.futureplatforms.kirin.dependencies.xml.parser.NodeList;

public class JaxpDocument extends JaxpNode implements Document {
	private org.w3c.dom.Document mDocument;

	public JaxpDocument(org.w3c.dom.Document document) {
		super(document);
		mDocument = document;
	}

	@Override
	public NodeList getElementsByTagName(String tagname) {
		return new JaxpNodeList(mDocument.getElementsByTagName(tagname));
	}
}
