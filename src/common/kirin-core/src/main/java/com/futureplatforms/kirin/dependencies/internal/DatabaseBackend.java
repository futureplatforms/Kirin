package com.futureplatforms.kirin.dependencies.internal;

public interface DatabaseBackend {
	
	public static interface DatabaseBackendCallback {
		public void onOpened(String dbId);
		public void onError();
	}
	
	public static interface DatabaseBackendTxCallback {
		public void onSuccess();
		public void onError();
	}
	
	public void openOrCreate(String filename, DatabaseBackendCallback cb);
	public void beginTransaction(String dbId, String txId, DatabaseBackendTxCallback dbtc);
	public void appendStatementToTransaction(String dbId, String txId, String sql, String[] params, DatabaseBackendTxCallback dbtc);
	public void appendFileToTransaction(String dbId, String txId, String filename);
	public void endTransaction(String id, DatabaseBackendTxCallback dbtc);
}
