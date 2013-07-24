package com.futureplatforms.kirin.gwt.client.delegates.xml;

import com.futureplatforms.kirin.dependencies.xml.parser.Node;
import com.futureplatforms.kirin.dependencies.xml.parser.NodeList;

public class GwtNodeImpl implements Node {

    private com.google.gwt.xml.client.Node mNode;
    
    public GwtNodeImpl(com.google.gwt.xml.client.Node node) {
    	this.mNode = node;
    	if ("xml".equals(mNode.getNodeName()) && mNode.getNextSibling() != null) {
    		mNode = mNode.getNextSibling();
    	}
    }
    
    @Override
    public Node getFirstChild() {
        if (mNode == null) { return null; }
        com.google.gwt.xml.client.Node child = mNode.getFirstChild();
        if (child == null) { return null; }
        return new GwtNodeImpl(child);
    }

    @Override
    public String getNodeName() {
        return mNode.getNodeName();
    }

    @Override
    public String getNodeValue() {
        return mNode.getNodeValue();
    }

    @Override
    public short getNodeType() {
        return mNode.getNodeType();
    }

    @Override
    public NodeList getChildNodes() {
        if (mNode == null) { return null; }
        com.google.gwt.xml.client.NodeList list = mNode.getChildNodes();
        if (list == null) { return null; }
        return new GwtNodeListImpl(list);
    }

    @Override
    public Node getNextSibling() {
        if (mNode == null) { return null; }
        com.google.gwt.xml.client.Node sibling = mNode.getNextSibling();
        if (sibling == null) { return null; }
        return new GwtNodeImpl(sibling);
    }

}
