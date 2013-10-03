package com.futureplatforms.kirin.gwt.client.services;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportClosure;
import org.timepedia.exporter.client.Exportable;

@Export
@ExportClosure
public interface NetworkingResult extends Exportable {
    public void result(int code, String headers, String payload);
}
