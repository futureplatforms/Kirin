package com.futureplatforms.kirin.dependencies.internal;

import java.util.List;
import java.util.Map;

public interface DatabaseBackend {
	
	public static interface DatabaseOpenedCallback {
		public void onOpened(String dbId);
		public void onError();
	}
	
	public static interface DatabaseBackendTxRowsCallback {
		public void onSuccess(List<Map<String, String>> rows);
		public void onError();
	}
	
	public static interface DatabaseBackendTxTokenCallback {
		public void onSuccess(String token);
		public void onError();
	}
	
	public static interface DatabaseClosedCallback {
		public void onClosed();
		public void onError();
	}
	
	public void open(String filename, DatabaseOpenedCallback cb);
	public void beginTransaction(String dbId, String txId, DatabaseBackendTxRowsCallback dbtc);
	public void appendStatementToTransaction(String dbId, String txId, String sql, String[] params, DatabaseBackendTxRowsCallback dbtc);
	public void appendStatementToTransaction(String dbId, String txId, String sql, String[] params, DatabaseBackendTxTokenCallback dbtc);
	public void appendFileToTransaction(String dbId, String txId, String filename);
	public void endTransaction(String id, DatabaseClosedCallback cb);
}
