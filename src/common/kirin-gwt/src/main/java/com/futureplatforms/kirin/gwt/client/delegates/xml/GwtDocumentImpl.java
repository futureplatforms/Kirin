package com.futureplatforms.kirin.gwt.client.delegates.xml;

import com.futureplatforms.kirin.dependencies.xml.parser.Document;
import com.futureplatforms.kirin.dependencies.xml.parser.NodeList;

public class GwtDocumentImpl extends GwtNodeImpl implements Document {

	private com.google.gwt.xml.client.Document mDocument;

	public GwtDocumentImpl(com.google.gwt.xml.client.Document document) {
		super(document);
		this.mDocument = document;
	}

	@Override
	public NodeList getElementsByTagName(String tagname) {
		return new GwtNodeListImpl(mDocument.getElementsByTagName(tagname));
	}

}
