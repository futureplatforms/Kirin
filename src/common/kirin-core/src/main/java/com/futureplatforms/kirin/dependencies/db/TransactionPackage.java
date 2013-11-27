package com.futureplatforms.kirin.dependencies.db;

import com.futureplatforms.kirin.dependencies.internal.TransactionBackend.TxClosedCallback;
import com.futureplatforms.kirin.dependencies.internal.TransactionBackend.TxRowsCallback;
import com.futureplatforms.kirin.dependencies.internal.TransactionBackend.TxTokenCallback;

public class TransactionPackage {

	public void appendStatementToTx(String sql, String[] params, TxRowsCallback dbtc) {
		
	}
	public void appendStatementToTx(String sql, String[] params, TxTokenCallback dbtc) {
		
	}
	public void appendFileToTx(String filename) {
		
	}
	public void endTransaction(TxClosedCallback cb) {
		
	}
}
