package com.futureplatforms.kirin.dependencies.db;

import com.futureplatforms.kirin.dependencies.internal.TransactionBackend2;

public abstract class Database {
    public static interface TxRunner {
        
        public void run(Transaction tx);
        
        public void onComplete();
        public void onError();
    }
    
	public static interface TransactionCallback {
		public void onSuccess(TransactionBackend2 tx);
		public void onError();
	}
	
	// This is the main method developers use to perform some database stuffs
    public void transaction(final TxRunner txRunner) {
    	performTransaction(new TransactionCallback() {
			
			@Override
			public void onSuccess(TransactionBackend2 txBackend) {
				Transaction tx = new Transaction(txBackend);
				txRunner.run(tx);
				tx.pullTrigger(txRunner);
			}
			
			@Override
			public void onError() {
				txRunner.onError();
			}
		});
    }
    
    protected abstract void performTransaction(TransactionCallback cb);
}
