package com.futureplatforms.kirin.dependencies.db;

import com.futureplatforms.kirin.dependencies.db.DatabasesDelegate.TxContainer;
import com.futureplatforms.kirin.dependencies.db.DatabasesDelegate.TxContainer.TxContainerCallback;
import com.futureplatforms.kirin.dependencies.db.Transaction.Mode;
import com.futureplatforms.kirin.dependencies.db.Transaction.TransactionCallback;
import com.futureplatforms.kirin.dependencies.internal.DatabaseBackend;
import com.futureplatforms.kirin.dependencies.internal.DatabaseBackend.DatabaseClosedCallback;;

public class Database {
	private DatabaseBackend _Backend;
	private String _DbID;
	public Database(String dbId, DatabaseBackend backend) {
		this._DbID = dbId;
		this._Backend = backend;
	}
	
    public void transaction(final TxContainer txContainer, final TxContainerCallback callback) {
    	Transaction.getTransaction(_DbID, _Backend, Mode.ReadWrite, new TransactionCallback() {
			
			@Override
			public void onSuccess(Transaction t) {
				txContainer.execute(t);
				t.done(new DatabaseClosedCallback() {
					
					@Override
					public void onError() {
						callback.onError();
					}
					
					@Override
					public void onClosed() {
						callback.onComplete();
					}
				});
			}
			
			@Override
			public void onError() {
				callback.onError();
			}
		});
    }
}
