package com.futureplatforms.kirin.dependencies.db;

import com.futureplatforms.kirin.dependencies.db.Database.TxRunner.TxRunnerCallback;
import com.futureplatforms.kirin.dependencies.internal.DatabaseBackend;
import com.futureplatforms.kirin.dependencies.internal.TransactionBackend;
import com.futureplatforms.kirin.dependencies.internal.TransactionBackend.TxClosedCallback;

public class Database {
    public static abstract class TxRunner {
        public static interface TxRunnerCallback {
            public void onComplete();
            public void onError();
        }
        
        public abstract void run(Transaction tx);
    }
	
	private DatabaseBackend _Backend;
	public Database(DatabaseBackend backend) {
		this._Backend = backend;
	}
	
    public void transaction(final TxRunner txRunner, final TxRunnerCallback callback) {
    	_Backend.beginTransaction(new DatabaseBackend.BeginTransactionCallback() {
			
			@Override
			public void onSuccess(TransactionBackend txBackend) {
				Transaction tx = new Transaction(txBackend);
				txRunner.run(tx);
				tx.pullTrigger(new TxClosedCallback() {
					
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
