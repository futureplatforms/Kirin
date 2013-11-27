package com.futureplatforms.kirin.gwt.client.services.db;

import java.util.Map;

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

@Export(value = "DatabaseService", all = true)
@ExportPackage("screens")
public class DatabaseAccessService extends KirinService<DatabaseAccessServiceNative> {
    private static DatabaseAccessService _Instance;
    
    @NoBind
    @NoExport
    public static DatabaseAccessService BACKDOOR() { return _Instance; }
    
    public DatabaseAccessService() {
        super(GWT.<DatabaseAccessServiceNative>create(DatabaseAccessServiceNative.class));
        _Instance = this;
    }
    
    private int _NextDbId = Integer.MIN_VALUE;
    private Map<Integer, DatabaseOpenedCallback> _OpenCallbacks = Maps.newHashMap();
    
    @NoBind
    @NoExport
    public void _openDatabase(String filename, DatabaseOpenedCallback cb) {
        int thisId = _NextDbId;
        _NextDbId++;
        _OpenCallbacks.put(thisId, cb);
    	getNativeObject().open(filename, thisId);
    }
    
    // BEGIN  Callback functions for database open
    public void databaseOpenedSuccess(int dbId) {
    	DatabaseOpenedCallback cb = _OpenCallbacks.remove(dbId);
    	cb.onOpened(new GwtDatabase(dbId));
    }
    public void databaseOpenedFailure(int dbId) {
    	DatabaseOpenedCallback cb = _OpenCallbacks.remove(dbId);
    	cb.onError();
    }
    // END  Callback functions for database open
    
    
    
    @NoBind
    @NoExport
    public void _closeDatabase(int dbId) {
    	getNativeObject().close(dbId);
    }
    
}
