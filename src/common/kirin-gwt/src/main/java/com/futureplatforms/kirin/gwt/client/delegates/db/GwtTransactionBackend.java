package com.futureplatforms.kirin.gwt.client.delegates.db;

import com.futureplatforms.kirin.dependencies.internal.TransactionBackend;

public class GwtTransactionBackend implements TransactionBackend {

	@Override
	public void appendStatementToTransaction(String sql, String[] params,
			TxRowsCallback dbtc) {
		
	}

	@Override
	public void appendStatementToTransaction(String sql, String[] params,
			TxTokenCallback dbtc) {

	}

	@Override
	public void appendFileToTransaction(String filename) {

	}

	@Override
	public void endTransaction(TxClosedCallback cb) {

	}

}
