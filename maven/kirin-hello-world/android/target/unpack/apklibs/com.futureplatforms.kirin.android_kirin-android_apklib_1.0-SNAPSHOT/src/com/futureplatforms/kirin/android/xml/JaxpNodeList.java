package com.futureplatforms.kirin.android.xml;

import com.futureplatforms.kirin.dependencies.xml.parser.Node;
import com.futureplatforms.kirin.dependencies.xml.parser.NodeList;

public class JaxpNodeList implements NodeList {

    private org.w3c.dom.NodeList mNodeList;
    
    public JaxpNodeList(org.w3c.dom.NodeList nodeList) {
        mNodeList = nodeList;
    }
    
    @Override
    public Node item(int index) {
        if (mNodeList == null) { return null; }
        org.w3c.dom.Node item = mNodeList.item(index);
        if (item == null) { return null; }
        return new JaxpNode(item);
    }

    @Override
    public int getLength() {
        if (mNodeList == null) { return 0; }
        return mNodeList.getLength();
    }

}
