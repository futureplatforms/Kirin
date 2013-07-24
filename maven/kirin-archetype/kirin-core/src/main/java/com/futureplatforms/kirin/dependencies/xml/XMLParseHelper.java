package com.futureplatforms.kirin.dependencies.xml;

import java.math.BigDecimal;

import com.futureplatforms.kirin.dependencies.xml.parser.Node;
import com.futureplatforms.kirin.dependencies.xml.parser.NodeList;

public class XMLParseHelper {
    private static final String[][] escapees = {
        {"&", "&amp;"},
        {"<", "&lt;"},
        {">", "&gt;"},
        {"\"", "&quot;"}
    };
    
    public static String xmlEscape(String str) {
        for (String[] escapee : escapees) {
            str = str.replaceAll(escapee[0], escapee[1]);
        }
        return str;
    }
    
  public static String getFirstChildString(Node child) {
    Node n = child.getFirstChild();
    if (n != null) {
        String val = n.getNodeValue();
      return val;
    }
    return null;
  }

  public static boolean getFirstChildBool(Node child) {
    NodeList nl = child.getChildNodes();
    if (nl != null && nl.getLength() > 0) {
      return "true".equalsIgnoreCase(nl.item(0).getNodeValue());
    }
    return false;
  }

  public static Boolean getFirstChildBoolean(Node child) {
    Node n = child.getFirstChild();
    if (n != null) {
      String val = n.getNodeValue();
      if ("true".equalsIgnoreCase(val)) {
        return true;
      }
      if ("false".equalsIgnoreCase(val)) {
        return false;
      }
    }
    return null;
  }

    public static BigDecimal getFirstChildBigDecimal(Node child) {
        Node n = child.getFirstChild();
        if (n != null) {
            String val = n.getNodeValue();
            try {
                return new BigDecimal(val);
            } catch (NumberFormatException e) {}
        }
        return null;
    }
  
    public static Integer getFirstChildInteger(Node child) {
        Node n = child.getFirstChild();
        if (n != null) {
            String val = n.getNodeValue();
            try {
                return Integer.parseInt(val);
            } catch (NumberFormatException e) {
            }
        }
        return null;
    }
}
