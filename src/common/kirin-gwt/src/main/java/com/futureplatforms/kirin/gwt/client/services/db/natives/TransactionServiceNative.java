package com.futureplatforms.kirin.gwt.client.services.db.natives;

import com.futureplatforms.kirin.gwt.client.IKirinNativeService;

public interface TransactionServiceNative extends IKirinNativeService {
	public void begin(int dbId, int txId);
	public void appendStatementForRows(int dbId, int txId, int statementId, String statement, String[] params);
    public void appendStatementForToken(int dbId, int txId, int statementId, String statement, String[] params);
    public void appendFile(int dbId, int txId, String filename);
    public void end(int dbId, int txId);
}
