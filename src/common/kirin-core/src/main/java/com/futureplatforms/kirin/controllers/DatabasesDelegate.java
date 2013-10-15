package com.futureplatforms.kirin.controllers;

import com.futureplatforms.kirin.dependencies.db.Database;
import com.futureplatforms.kirin.dependencies.internal.DatabaseAccessorBackend;
import com.futureplatforms.kirin.dependencies.internal.DatabaseAccessorBackend.DatabaseOpenedCallback;
import com.futureplatforms.kirin.dependencies.internal.DatabaseBackend;
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
    
    public static void openDatabase(String name, final DatabaseCB callback) {
    	final DatabaseAccessorBackend backend = InternalDependencies.getInstance().getDatabaseAccessor();
    	backend.open(name, new DatabaseOpenedCallback() {
			
			@Override
			public void onOpened(DatabaseBackend db) {
				callback.onOpened(new Database(db));
			}
			
			@Override
			public void onError() {
				callback.onError();
			}
		});
    }
}
