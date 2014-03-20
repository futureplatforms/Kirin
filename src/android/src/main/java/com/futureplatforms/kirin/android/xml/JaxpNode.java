package com.futureplatforms.kirin.android.xml;

import com.futureplatforms.kirin.dependencies.xml.parser.Attributes;
import com.futureplatforms.kirin.dependencies.xml.parser.CharacterData;
import com.futureplatforms.kirin.dependencies.xml.parser.Node;
import com.futureplatforms.kirin.dependencies.xml.parser.NodeList;

public class JaxpNode implements Node, CharacterData {
	private org.w3c.dom.Node mNode;

	public JaxpNode(org.w3c.dom.Node node) {
		mNode = node;
		if (node != null)
			mNode.normalize();
	}

	@Override
	public Node getFirstChild() {
		if (mNode == null) {
			return null;
		}
		org.w3c.dom.Node child = mNode.getFirstChild();
		if (child == null) {
			return null;
		}
		return new JaxpNode(child);
	}

	@Override
	public Node getNextSibling() {
		if (mNode == null) {
			return null;
		}
		org.w3c.dom.Node sibling = mNode.getNextSibling();
		if (sibling == null) {
			return null;
		}
		return new JaxpNode(sibling);
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
	public NodeList getChildNodes() {
		if (mNode == null) {
			return null;
		}
		org.w3c.dom.NodeList list = mNode.getChildNodes();
		if (list == null) {
			return null;
		}
		return new JaxpNodeList(list);
	}

	@Override
	public short getNodeType() {
		return mNode.getNodeType();
	}

	@Override
	public Attributes getAttributes() {
		return new JaxpAttributes(mNode.getAttributes());
	}

}
