package com.futureplatforms.kirin.gwt.client.services.db;

import java.util.Map;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportClosure;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

import com.futureplatforms.kirin.gwt.client.KirinService;
import com.futureplatforms.kirin.gwt.client.services.db.natives.DatabaseAccessServiceNative;
import com.futureplatforms.kirin.gwt.compile.NoBind;
import com.google.common.collect.Maps;
import com.google.gwt.core.client.GWT;

@Export(value = "DatabaseService", all = true)
@ExportPackage("screens")
public class DatabaseAccessService extends KirinService<DatabaseAccessServiceNative> {
    
    @Export
    @ExportClosure
    public static interface OpenSuccess extends Exportable {
        @Export
        public void execute(int theId);
    }
    
    @Export
    @ExportClosure
    public static interface CloseSuccess extends Exportable {
    	@Export
    	public void execute();
    }
    
    @Export
    @ExportClosure
    public static interface Failure extends Exportable {
        @Export
        public void execute();
    }
    
    protected static final class OpenCallback {
    	public final OpenSuccess _Success;
    	public final Failure _Failure;
    	public OpenCallback(OpenSuccess success, Failure failure) {
    		this._Success = success;
    		this._Failure = failure;
    	}
    }
    
    protected static final class CloseCallback {
    	public final CloseSuccess _Success;
    	public final Failure _Failure;
    	public CloseCallback(CloseSuccess success, Failure failure) {
    		this._Success = success;
    		this._Failure = failure;
    	}
    }
    
    public DatabaseAccessService() {
        super(GWT.<DatabaseAccessServiceNative>create(DatabaseAccessServiceNative.class));
    }
    
    private int _NextDbId = Integer.MIN_VALUE;
    private Map<Integer, OpenCallback> _OpenCallbacks = Maps.newHashMap();
    
    @NoBind
    public void _openDatabase(String filename, OpenSuccess success, Failure failure) {
        int thisId = _NextDbId;
        _NextDbId++;
        _OpenCallbacks.put(thisId, new OpenCallback(success, failure));
    	getNativeObject().open(filename, thisId);
    }
    
    // BEGIN  Callback functions for database open
    public void databaseOpenedSuccess(int dbId) {
    	OpenCallback cb = _OpenCallbacks.remove(dbId);
    	cb._Success.execute(dbId);
    }
    public void databaseOpenedFailure(int dbId) {
    	OpenCallback cb = _OpenCallbacks.remove(dbId);
    	cb._Failure.execute();
    }
    // END  Callback functions for database open
    
    
    @NoBind
    public void _closeDatabase(int dbId) {
    	getNativeObject().close(dbId);
    }
    
}
