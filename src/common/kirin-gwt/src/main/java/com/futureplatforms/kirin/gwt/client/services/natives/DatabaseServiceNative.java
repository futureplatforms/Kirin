package com.futureplatforms.kirin.gwt.client.services.natives;

import com.futureplatforms.kirin.gwt.client.IKirinNativeService;

public interface DatabaseServiceNative extends IKirinNativeService {

	public void open(String filename, String dbId);
    public void beginTx(String dbId, String txId);
    public void endTx(String dbId, String txId);
    public void appendStatementToTxForRows(String dbId, String txId, String statementId, String type, String statement, String[] params);
    public void appendStatementToTxForToken(String dbId, String txId, String statementId, String type, String statement, String[] params);
    public void appendFileToTx(String dbId, String txId, String statementId, String type, String filename);

}
