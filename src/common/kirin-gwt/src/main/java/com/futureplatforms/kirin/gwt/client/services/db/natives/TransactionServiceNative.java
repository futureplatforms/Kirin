package com.futureplatforms.kirin.gwt.client.services.db.natives;

import com.futureplatforms.kirin.gwt.client.IKirinNativeService;

public interface TransactionServiceNative extends IKirinNativeService {
	public void begin(int dbId, int txId);
	public void appendStatementForRows(int dbId, int txId, int statementId, String statement, String[] txParams);
    public void appendStatementForToken(int dbId, int txId, int statementId, String statement, String[] txParams);
    public void appendStatementForJSON(int dbId, int txId, int statementId, String statement, String[] txParams);
    public void appendBatch(int dbId, int txId, String[] batch);
    public void end(int dbId, int txId);
}
