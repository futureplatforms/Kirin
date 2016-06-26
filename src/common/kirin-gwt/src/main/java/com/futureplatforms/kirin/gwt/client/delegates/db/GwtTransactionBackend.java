package com.futureplatforms.kirin.gwt.client.delegates.db;

import com.futureplatforms.kirin.dependencies.internal.TransactionBackend;
import com.futureplatforms.kirin.dependencies.internal.TransactionBundle;
import com.futureplatforms.kirin.gwt.client.services.db.TransactionService;

public class GwtTransactionBackend implements TransactionBackend {

	private final String _Filename;
	private final int _TxId;
	
	public GwtTransactionBackend(String filename, int txId) {
		this._Filename = filename;
		this._TxId = txId;
	}
	
	@Override
	public void pullTrigger(TransactionBundle bundle) {
		TransactionService.BACKDOOR()._pullTrigger(bundle, _Filename, _TxId);
	}

}
