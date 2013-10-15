package com.futureplatforms.kirin.gwt.client.delegates.db;

import com.futureplatforms.kirin.controllers.DatabasesDelegate.DatabaseCB;
import com.futureplatforms.kirin.gwt.client.services.db.DatabasePrototype;

public class PerformOpenDatabase {
    private DatabaseCB _Callback;
    
    public DatabasePrototype openDatabase(String filename, int version, DatabaseCB callback) {
        return _openDatabase(filename, version, this);
    }
    
    private static native DatabasePrototype _openDatabase(String filename, int version, PerformOpenDatabase p) /*-{
        var databases = $wnd.EXPOSED_TO_NATIVE.native2js.resolveModule("DatabaseService");
        var onCreate = function(tx) {
            var fn = p.@com.futureplatforms.kirin.gwt.client.delegates.db.PerformOpenDatabase::openDatabaseCallbackCreate();
            $entry(
                fn()
            );
        };
        var onUpdate = function(tx) {
            var fn = p.@com.futureplatforms.kirin.gwt.client.delegates.db.PerformOpenDatabase::openDatabaseCallbackUpdate();
            $entry(
                fn()
            );
        };
        var onErr = function() {
            var fn = p.@com.futureplatforms.kirin.gwt.client.delegates.db.PerformOpenDatabase::openDatabaseCallbackOnError();
            $entry(
                fn()
            );
        };
        return databases._openDatabase(filename, version, onCreate, onUpdate, onErr);
    }-*/;
}
