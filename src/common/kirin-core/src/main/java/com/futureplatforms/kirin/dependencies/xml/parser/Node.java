package com.futureplatforms.kirin.dependencies.xml.parser;

public interface Node {
    public static final short ELEMENT_NODE = 1;
    public static final short TEXT_NODE = 3;
    public Node getFirstChild();
    public Node getNextSibling();
    public String getNodeName();
    public String getNodeValue();
    public NodeList getChildNodes();
    public short getNodeType();
}
