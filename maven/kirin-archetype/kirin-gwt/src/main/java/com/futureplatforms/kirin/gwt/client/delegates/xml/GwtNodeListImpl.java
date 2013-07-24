package com.futureplatforms.kirin.gwt.client.delegates.xml;

import com.futureplatforms.kirin.dependencies.xml.parser.Node;
import com.futureplatforms.kirin.dependencies.xml.parser.NodeList;

public class GwtNodeListImpl implements NodeList {
    private com.google.gwt.xml.client.NodeList mNodeList; 
    public GwtNodeListImpl(com.google.gwt.xml.client.NodeList nl) {
        this.mNodeList = nl;
    }
    @Override
    public Node item(int index) {
        if (mNodeList == null) { return null; }
        com.google.gwt.xml.client.Node item = mNodeList.item(index);
        if (item == null) { return null; }
        return new GwtNodeImpl(item);
    }
    @Override
    public int getLength() {
        if (mNodeList == null) { return 0; }
        return mNodeList.getLength();
    }
}
