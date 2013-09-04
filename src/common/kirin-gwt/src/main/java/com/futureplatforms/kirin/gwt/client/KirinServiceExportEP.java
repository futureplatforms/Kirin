package com.futureplatforms.kirin.gwt.client;

import org.timepedia.exporter.client.ExporterUtil;

import com.google.gwt.core.client.EntryPoint;

public class KirinServiceExportEP implements EntryPoint {
    @Override
    public void onModuleLoad() {
        ExporterUtil.exportAll();
    }
}
