package com.futureplatforms.kirin.gwt.client.delegates.db;

import com.futureplatforms.kirin.dependencies.db.Database;

public class GwtDatabase extends Database {
	public final int _DbId;
	
	public GwtDatabase(int dbId) {
		this._DbId = dbId;
	}
	
	private TransactionCallback _Callback;
	
	@Override
	public void performTransaction(TransactionCallback callback) {
		this._Callback = callback;
		_beginTransaction(_DbId, this);
	}
	
	// These methods call back from the TransactionAccessService
    private void onOpened(int id) {
    	_Callback.onSuccess(new GwtTransactionBackend(_DbId, id));
    }
    private void onError() {
    	_Callback.onError();
    }
	
	private static native void _beginTransaction(int dbId, GwtDatabase p) /*-{
		var txAccess = $wnd.EXPOSED_TO_NATIVE.native2js.resolveModule("TransactionAccessService");
		var success = function(id) {
    		var onOpened = p.@com.futureplatforms.kirin.gwt.client.delegates.db.GwtDatabase::onOpened(I)
    		onOpened(id)
    	}
    	var failure = function() {
    		var onError = p.@com.futureplatforms.kirin.gwt.client.delegates.db.GwtDatabase::onError()
    		onError()
    	}
       	// TransactionAccessService
		//   public void _BeginTransaction(int dbId, OpenSuccess success, Failure failure) 
		txAccess._BeginTransaction(dbId, success, failure);
	}-*/;
	
}
