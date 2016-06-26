package com.futureplatforms.kirin.gwt.client.delegates.db;

import com.futureplatforms.kirin.dependencies.db.Database;
import com.futureplatforms.kirin.gwt.client.services.db.TransactionService;

public class GwtDatabase extends Database {
	public final String _Filename;
	
	public GwtDatabase(String filename) {
		this._Filename = filename;
	}
	
	@Override
	public void performTransaction(TransactionCallback callback) {
		TransactionService ts = TransactionService.BACKDOOR();
		ts._BeginTransaction(_Filename, callback);
	}
}
