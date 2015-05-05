package com.futureplatforms.kirin.dependencies.db;

import com.futureplatforms.kirin.dependencies.StaticDependencies;
import com.futureplatforms.kirin.dependencies.internal.TransactionBackend;

public abstract class Database {
	public static boolean DEBUG = false;
	
    public static interface TxRunner {
        
        public void run(Transaction tx);
        
        public void onComplete();
        public void onError();
    }
    
	public static interface TransactionCallback {
		public void onSuccess(TransactionBackend tx);
	}
	
	// Users call database.transaction to do their db stuff
    public void transaction(final TxRunner txRunner) {
    	// Set up a native transaction
    	performTransaction(new TransactionCallback() {
			
			@Override
			public void onSuccess(TransactionBackend txBackend) {
				try {
					// OK, we've set up a native transaction
					Transaction tx = new Transaction(txBackend);
					
					// Run the user-implemented TxRunner.run() method --
					// this will fill the Transaction class with all the 
					// SQL queries the user wants to run
					txRunner.run(tx);
					
					// Actually execute it, and pass the TxRunner as callback
					tx.pullTrigger(txRunner);
				} catch (Throwable t) {
					StaticDependencies.getInstance().getLogDelegate().log("Kirin","DB Transaction Exception",t);
					txRunner.onError();
				}
			}
		});
    }
    
    protected abstract void performTransaction(TransactionCallback cb);
}
