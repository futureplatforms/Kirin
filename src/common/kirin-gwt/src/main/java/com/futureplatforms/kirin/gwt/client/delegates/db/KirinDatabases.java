package com.futureplatforms.kirin.gwt.client.delegates.db;

import com.futureplatforms.kirin.dependencies.db.DatabasesDelegate;

public class KirinDatabases implements DatabasesDelegate {
    @Override
    public Database openDatabase(String filename, int version, DatabaseCB callback) {
        return new PerformOpenDatabase().openDatabase(filename, version, callback);
    }
}
