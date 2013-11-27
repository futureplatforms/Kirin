package com.futureplatforms.kirin.gwt.client.services.db;

import java.util.Map;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;

import com.futureplatforms.kirin.gwt.client.KirinService;
import com.futureplatforms.kirin.gwt.client.services.db.DatabaseAccessService.CloseCallback;
import com.futureplatforms.kirin.gwt.client.services.db.DatabaseAccessService.CloseSuccess;
import com.futureplatforms.kirin.gwt.client.services.db.DatabaseAccessService.Failure;
import com.futureplatforms.kirin.gwt.client.services.db.DatabaseAccessService.OpenCallback;
import com.futureplatforms.kirin.gwt.client.services.db.DatabaseAccessService.OpenSuccess;
import com.futureplatforms.kirin.gwt.client.services.db.natives.TransactionAccessServiceNative;
import com.futureplatforms.kirin.gwt.compile.NoBind;
import com.google.common.collect.Maps;
import com.google.gwt.core.client.GWT;

@Export(value = "TransactionService", all = true)
@ExportPackage("screens")
public class TransactionAccessService extends KirinService<TransactionAccessServiceNative> {

	protected TransactionAccessService() {
		super(GWT.<TransactionAccessServiceNative>create(TransactionAccessServiceNative.class));
	}

	private Map<Integer, Integer> _LatestTxIdForDbId = Maps.newHashMap();
	private Map<Integer, Map<Integer, OpenCallback>> _OpenCallbacks = Maps.newHashMap();
	private Map<Integer, Map<Integer, CloseCallback>> _CloseCallbacks = Maps.newHashMap();
	
	@NoBind
	public void _BeginTransaction(int dbId, OpenSuccess success, Failure failure) {
		int nextId;
		if (_LatestTxIdForDbId.containsKey(dbId)) {
			int latest = _LatestTxIdForDbId.get(dbId);
			nextId = latest + 1;
		} else {
			nextId = Integer.MIN_VALUE;
		}
		
		if (!_OpenCallbacks.containsKey(dbId)) {
			_OpenCallbacks.put(dbId, Maps.<Integer, OpenCallback>newHashMap());
		}
		Map<Integer, OpenCallback> callbackMap = _OpenCallbacks.get(dbId);
		callbackMap.put(nextId, new OpenCallback(success, failure));
		_LatestTxIdForDbId.put(dbId, nextId);
		
		getNativeObject().beginTx(dbId, nextId);
	}
	
	// BEGIN  Callback functions for transaction open
	public void transactionBeginOnSuccess(int dbId, int txId) {
		_OpenCallbacks.get(dbId).remove(txId)._Success.execute(txId);
		if (_OpenCallbacks.get(dbId).isEmpty()) { _OpenCallbacks.remove(dbId); }
	}
	public void transactionBeginOnError(int dbId, int txId) {
		_OpenCallbacks.get(dbId).remove(txId)._Failure.execute();
		if (_OpenCallbacks.get(dbId).isEmpty()) { _OpenCallbacks.remove(dbId); }
	}
	// END  Callback functions for transaction open
	
	
	@NoBind
	public void _EndTransaction(int dbId, int txId, CloseSuccess success, Failure failure) {
		if (!_CloseCallbacks.containsKey(dbId)) {
			_CloseCallbacks.put(dbId, Maps.<Integer, CloseCallback>newHashMap());
		} 
		Map<Integer, CloseCallback> callbackMap = Maps.newHashMap();
		callbackMap.put(txId, new CloseCallback(success, failure));
		
		getNativeObject().endTx(dbId, txId);
	}
	
	// BEGIN  Callback functions for transaction closed
	public void transactionEndOnSuccess(int dbId, int txId) {
		_CloseCallbacks.get(dbId).remove(txId)._Success.execute();
		if (_CloseCallbacks.get(dbId).isEmpty()) { _CloseCallbacks.remove(dbId); }
	}
	public void transactionEndOnError(int dbId, int txId) {
		_CloseCallbacks.get(dbId).remove(txId)._Failure.execute();
		if (_CloseCallbacks.get(dbId).isEmpty()) { _CloseCallbacks.remove(dbId); }
	}
	// END  Callback functions for transaction closed	
}
