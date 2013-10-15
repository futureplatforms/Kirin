package com.futureplatforms.kirin.dependencies.internal;

import java.util.List;
import java.util.Map;

public interface TransactionBackend {

	public static interface TxRowsCallback {
		public void onSuccess(List<Map<String, String>> rows);
		public void onError();
	}
	
	public static interface TxTokenCallback {
		public void onSuccess(String token);
		public void onError();
	}
	
	public static interface TxClosedCallback {
		public void onClosed();
		public void onError();
	}
	
	public void appendStatementToTransaction(String sql, String[] params, TxRowsCallback dbtc);
	public void appendStatementToTransaction(String sql, String[] params, TxTokenCallback dbtc);
	public void appendFileToTransaction(String filename);
	public void endTransaction(TxClosedCallback cb);
}
