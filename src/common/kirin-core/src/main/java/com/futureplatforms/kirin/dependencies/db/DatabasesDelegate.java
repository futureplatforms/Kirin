package com.futureplatforms.kirin.dependencies.db;

import com.futureplatforms.kirin.dependencies.db.DatabasesDelegate.TxContainer.TxContainerCallback;

/**
 * This is the app developer's view of a database
 * @author douglashoskins
 *
 */
public interface DatabasesDelegate {
    public static interface DatabaseCB {
        public void onCreate(Transaction tx);
        public void onUpdate(Transaction tx);
        public void onError();
    }
    
    public static interface TxContainer {
        public static interface TxContainerCallback {
            public void onComplete();
            public void onError(String err);
        }
        public void execute(Transaction tx);
    }
    
    public interface Database {
        public void transaction(TxContainer txContainer, TxContainerCallback callback);
        public void readTransaction(TxContainer txContainer);
    }
    
    public Database openDatabase(String filename, int version, DatabaseCB callback);
}
