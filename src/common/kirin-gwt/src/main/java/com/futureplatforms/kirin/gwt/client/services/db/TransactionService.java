package com.futureplatforms.kirin.gwt.client.services.db;

import java.util.Map;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;

import com.futureplatforms.kirin.gwt.client.KirinService;
import com.futureplatforms.kirin.gwt.client.services.db.DatabaseAccessService.OpenCallback;
import com.futureplatforms.kirin.gwt.client.services.db.natives.TransactionServiceNative;
import com.futureplatforms.kirin.gwt.compile.NoBind;
import com.google.common.collect.Maps;
import com.google.gwt.core.client.GWT;

@Export(value = "TransactionService", all = true)
@ExportPackage("screens")
public class TransactionService extends KirinService<TransactionServiceNative>{

	public TransactionService() {
		super(GWT.<TransactionServiceNative>create(TransactionServiceNative.class));
	}
	
	private int _NextTxCall = Integer.MIN_VALUE;
    private Map<Integer, OpenCallback> _OpenCallbacks = Maps.newHashMap();
    
    @NoBind
    public void _AppendStatementToTxRows() {
    	
    }
    
    @NoBind
    public void _AppendStatementToTxToken() {
    	
    }
    
    @NoBind
    public void _AppendFileToTx() {
    	
    }
    
}
