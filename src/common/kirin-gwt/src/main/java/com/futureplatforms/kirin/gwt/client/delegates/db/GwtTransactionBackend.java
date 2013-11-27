package com.futureplatforms.kirin.gwt.client.delegates.db;

import com.futureplatforms.kirin.dependencies.internal.TransactionBackend;
import com.futureplatforms.kirin.dependencies.internal.TransactionBundle;
import com.futureplatforms.kirin.gwt.client.services.db.TransactionService;

public class GwtTransactionBackend implements TransactionBackend {

	private final int _DbId, _TxId;
	
	public GwtTransactionBackend(int dbId, int txId) {
		this._DbId = dbId;
		this._TxId = txId;
	}
	
	@Override
	public void pullTrigger(TransactionBundle bundle) {
		TransactionService.BACKDOOR().pullTrigger(bundle, _DbId, _TxId);
	}

}
