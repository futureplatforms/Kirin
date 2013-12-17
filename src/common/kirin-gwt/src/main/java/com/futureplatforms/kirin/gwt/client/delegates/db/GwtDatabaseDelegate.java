package com.futureplatforms.kirin.gwt.client.delegates.db;

import com.futureplatforms.kirin.dependencies.db.DatabaseDelegate;
import com.futureplatforms.kirin.gwt.client.services.db.DatabaseAccessService;

public class GwtDatabaseDelegate implements DatabaseDelegate {
	@Override
	public void open(String filename, DatabaseOpenedCallback cb) {
		DatabaseAccessService das = DatabaseAccessService.BACKDOOR();
		das._openDatabase(filename, cb);
	}
}
