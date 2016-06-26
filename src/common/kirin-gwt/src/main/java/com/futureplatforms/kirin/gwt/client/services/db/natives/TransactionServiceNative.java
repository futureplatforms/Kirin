package com.futureplatforms.kirin.gwt.client.services.db.natives;

import com.futureplatforms.kirin.gwt.client.IKirinNativeService;

public interface TransactionServiceNative extends IKirinNativeService {
	public void appendStatementForRows(String filename, int txId, int statementId, String statement, String[] txParams);
    public void appendStatementForToken(String filename, int txId, int statementId, String statement, String[] txParams);
    public void appendStatementForJSON(String filename, int txId, int statementId, String statement, String[] txParams);
    
    public void appendStatements(String filename, int txId, int[] returnTypes, int[] statementIds, String[] statements, String[] txParams);
    
    public void appendBatch(String filename, int txId, String[] batch);
    public void end(String filename, int txId);
}
