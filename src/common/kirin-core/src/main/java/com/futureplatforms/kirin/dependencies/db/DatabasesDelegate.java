package com.futureplatforms.kirin.dependencies.db;

import com.futureplatforms.kirin.dependencies.internal.DatabaseBackend;
import com.futureplatforms.kirin.dependencies.internal.DatabaseBackend.DatabaseOpenedCallback;
import com.futureplatforms.kirin.dependencies.internal.InternalDependencies;

/**
 * This is the app developer's view of a database
 * @author douglashoskins
 *
 */
public class DatabasesDelegate {
    public static interface DatabaseCB {
        public void onOpened(Database db);
        public void onError();
    }
    
    public static abstract class TxContainer {
        public static interface TxContainerCallback {
            public void onComplete();
            public void onError();
        }
        
        public abstract void execute(Transaction tx);
    }
    
    public static void openDatabase(String name, final DatabaseCB callback) {
    	final DatabaseBackend backend = InternalDependencies.getInstance().getDatabaseBackend();
    	backend.open(name, new DatabaseOpenedCallback() {
			
			@Override
			public void onOpened(String dbId) {
				callback.onOpened(new Database(dbId, backend));
			}
			
			@Override
			public void onError() {
				callback.onError();
			}
		});
    }
}
