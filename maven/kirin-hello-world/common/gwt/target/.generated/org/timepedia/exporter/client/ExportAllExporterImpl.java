package org.timepedia.exporter.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import org.timepedia.exporter.client.Exporter;
import org.timepedia.exporter.client.ExporterUtil;

public class ExportAllExporterImpl implements Exporter {
  public ExportAllExporterImpl() { export(); } 
  public void export() { 
    GWT.create(com.futureplatforms.kirinhello.modules.TestModule.class);
  }
}
