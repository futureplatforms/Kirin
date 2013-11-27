package com.futureplatforms.kirin.gwt.client.delegates.db;

import com.futureplatforms.kirin.dependencies.internal.TransactionBackend;
import com.google.gwt.core.client.JavaScriptObject;

public class GwtTransactionBackend implements TransactionBackend {

	private final int _DbId, _TxId;
	
	public GwtTransactionBackend(int dbId, int txId) {
		this._DbId = dbId;
		this._TxId = txId;
	}
	
	@Override
	public void appendStatementToTx(String sql, String[] params,
			TxRowsCallback cb) {
		_appendStatementToTx(getTxService(), sql, params, cb);
	}
	
	private static native void _appendStatementToTx(JavaScriptObject txService, String sql, String[] params, TxRowsCallback cb) /*-{
		
		txService._AppendStatementToTxRows(sql, params);
	}-*/;

	@Override
	public void appendStatementToTx(String sql, String[] params,
			TxTokenCallback cb) {
		_appendStatementToTx(getTxService(), sql, params, cb);
	}

	private static native void _appendStatementToTx(JavaScriptObject txService, String sql, String[] params, TxTokenCallback cb) /*-{
		
	}-*/;
	
	@Override
	public void appendFileToTx(String filename) {

	}

	@Override
	public void endTransaction(TxClosedCallback cb) {

	}

	private static native JavaScriptObject getTxService() /*-{
		return $wnd.EXPOSED_TO_NATIVE.native2js.resolveModule("TransactionService");
	}-*/;
}
