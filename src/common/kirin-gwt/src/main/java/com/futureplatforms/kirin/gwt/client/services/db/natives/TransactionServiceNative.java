package com.futureplatforms.kirin.gwt.client.services.db.natives;

import com.futureplatforms.kirin.gwt.client.IKirinNativeService;

public interface TransactionServiceNative extends IKirinNativeService {
	public void beginTransaction(int dbId, int txId);
	public void endTransaction(int dbId, int txId);
}
