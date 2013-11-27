package com.futureplatforms.kirin.gwt.client.services.db.natives;

import com.futureplatforms.kirin.gwt.client.IKirinNativeService;

public interface TransactionServiceNative extends IKirinNativeService {
	public void appendStatementToTxForRows(int dbId, int txId, String statementId, String type, String statement, String[] params);
    public void appendStatementToTxForToken(int dbId, int txId, String statementId, String type, String statement, String[] params);
    public void appendFileToTx(int dbId, int txId, String statementId, String type, String filename);
}
