package com.futureplatforms.kirin.gwt.client.services.db;

import java.util.Map;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.NoExport;

import com.futureplatforms.kirin.dependencies.db.Database.TransactionCallback;
import com.futureplatforms.kirin.gwt.client.KirinService;
import com.futureplatforms.kirin.gwt.client.delegates.db.GwtTransactionBackend2;
import com.futureplatforms.kirin.gwt.client.services.db.natives.TransactionAccessServiceNative;
import com.futureplatforms.kirin.gwt.compile.NoBind;
import com.google.common.collect.Maps;
import com.google.gwt.core.client.GWT;

@Export(value = "TransactionService", all = true)
@ExportPackage("screens")
public class TransactionAccessService extends KirinService<TransactionAccessServiceNative> {
    private static TransactionAccessService _Instance;
    
    @NoBind
    @NoExport
    public static TransactionAccessService BACKDOOR() { return _Instance; }
    
	protected TransactionAccessService() {
		super(GWT.<TransactionAccessServiceNative>create(TransactionAccessServiceNative.class));
	}

	private Map<Integer, Integer> _LatestTxIdForDbId = Maps.newHashMap();
	private Map<Integer, Map<Integer, TransactionCallback>> _OpenCallbacks = Maps.newHashMap();
	
	@NoBind
	@NoExport
	// this method gets called via the backdoor
	public void _BeginTransaction(int dbId, TransactionCallback callback) {
		int nextId;
		if (_LatestTxIdForDbId.containsKey(dbId)) {
			int latest = _LatestTxIdForDbId.get(dbId);
			nextId = latest + 1;
		} else {
			nextId = Integer.MIN_VALUE;
		}
		
		if (!_OpenCallbacks.containsKey(dbId)) {
			_OpenCallbacks.put(dbId, Maps.<Integer, TransactionCallback>newHashMap());
		}
		Map<Integer, TransactionCallback> callbackMap = _OpenCallbacks.get(dbId);
		callbackMap.put(nextId, callback);
		_LatestTxIdForDbId.put(dbId, nextId);
		
		getNativeObject().beginTx(dbId, nextId);
	}
	
	// BEGIN  Callback functions for transaction open
	public void transactionBeginOnSuccess(int dbId, int txId) {
		_OpenCallbacks.get(dbId).remove(txId).onSuccess(new GwtTransactionBackend2(dbId, txId));
		if (_OpenCallbacks.get(dbId).isEmpty()) { _OpenCallbacks.remove(dbId); }
	}
	public void transactionBeginOnError(int dbId, int txId) {
		_OpenCallbacks.get(dbId).remove(txId).onError();
		if (_OpenCallbacks.get(dbId).isEmpty()) { _OpenCallbacks.remove(dbId); }
	}
	// END  Callback functions for transaction open
}
