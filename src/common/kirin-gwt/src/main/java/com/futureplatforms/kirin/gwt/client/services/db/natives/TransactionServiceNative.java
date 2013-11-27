package com.futureplatforms.kirin.gwt.client.services.db.natives;

import com.futureplatforms.kirin.gwt.client.IKirinNativeService;

public interface TransactionServiceNative extends IKirinNativeService {
	public void appendStatementToTxForRows(int dbId, int txId, int statementId, String statement, String[] params);
    public void appendStatementToTxForToken(int dbId, int txId, int statementId, String statement, String[] params);
    public void appendFileToTx(int dbId, int txId, String filename);
    public void end(int dbId, int txId);
}
