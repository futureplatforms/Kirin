package com.futureplatforms.kirin.gwt.client.services.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.futureplatforms.kirin.dependencies.StaticDependencies;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.NoExport;

import com.futureplatforms.kirin.dependencies.db.DatabaseDelegate.DatabaseOpenedCallback;
import com.futureplatforms.kirin.gwt.client.KirinService;
import com.futureplatforms.kirin.gwt.client.delegates.db.GwtDatabase;
import com.futureplatforms.kirin.gwt.client.services.db.natives.DatabaseAccessServiceNative;
import com.futureplatforms.kirin.gwt.compile.NoBind;
import com.google.common.collect.Maps;
import com.google.gwt.core.client.GWT;

@Export(value = "DatabaseAccessService", all = true)
@ExportPackage("screens")
public class DatabaseAccessService extends KirinService<DatabaseAccessServiceNative> {
    private static DatabaseAccessService _Instance;
    
    @NoBind
    @NoExport
    public static DatabaseAccessService BACKDOOR() { return _Instance; }
    
    public DatabaseAccessService() {
        super(GWT.<DatabaseAccessServiceNative>create(DatabaseAccessServiceNative.class));
        // There will only ever be one instance of this class, it will be created in javascript
        // by the GWT platform when the app starts.
        // We need to do this because the use of this module is initiated from Kirin code
        // rather than native code (as with most regular screen modules).
        _Instance = this;
    }

    private static class DatabaseCallback {
        public final DatabaseOpenedCallback cb;
        public final String filename;
        public DatabaseCallback(DatabaseOpenedCallback cb, String filename) {
            this.cb = cb;
            this.filename = filename;
        }
    }


    private Map<String, List<DatabaseOpenedCallback>> callbacks = new HashMap<>();
    private Map<String, GwtDatabase> databasesByName = new HashMap<>();

    @NoBind
    @NoExport
    public void _openDatabase(String filename, DatabaseOpenedCallback cb) {
        if (databasesByName.containsKey(filename)) {
            cb.onOpened(databasesByName.get(filename));
            return;
        }

        if (!callbacks.containsKey(filename)) {
            callbacks.put(filename, new ArrayList<DatabaseOpenedCallback>());
        }

        List<DatabaseOpenedCallback> l = callbacks.get(filename);
        l.add(cb);

        if (l.size() == 1) {
            // don't want to kick off more than one native call per db
            getNativeObject().open(filename);
        }
    }
    
    // BEGIN  Callback functions for database open
    public void databaseOpenedSuccess(String filename) {
        GwtDatabase db = new GwtDatabase(filename);

        for (DatabaseOpenedCallback cb : callbacks.get(filename)) {
            cb.onOpened(db);
        }
        callbacks.clear();
    }

    public void databaseOpenedFailure(String filename) {
        for (DatabaseOpenedCallback cb : callbacks.get(filename)) {
            cb.onError();
        }
        callbacks.clear();
    }
    // END  Callback functions for database open
    
}
