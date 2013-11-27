package com.futureplatforms.kirin.gwt.client.services.db.natives;

import com.futureplatforms.kirin.gwt.client.IKirinNativeService;

public interface TransactionAccessServiceNative extends IKirinNativeService {
	public void beginTx(int dbId, int txId);
	public void endTx(int dbId, int txId);
}
