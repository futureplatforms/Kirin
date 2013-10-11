package com.futureplatforms.kirin.gwt.client.services.db;

import org.timepedia.exporter.client.Exportable;

import com.futureplatforms.kirin.dependencies.StaticDependencies;
import com.futureplatforms.kirin.dependencies.db.DatabasesDelegate.Database;
import com.futureplatforms.kirin.dependencies.db.DatabasesDelegate.TxContainer;
import com.futureplatforms.kirin.dependencies.db.DatabasesDelegate.TxContainer.TxContainerCallback;
import com.futureplatforms.kirin.gwt.client.services.natives.DatabaseServiceNative;

public class DatabasePrototype implements Database, Exportable {
    public final String _Filename;
    public final int _Version;
    private DatabaseServiceNative _NativeService;
    public DatabasePrototype(String filename, int version, DatabaseServiceNative nativeService) {
        this._Filename = filename;
        this._Version = version;
        this._NativeService = nativeService;
    }
    
    @Override
    public void transaction(TxContainer txContainer,
            TxContainerCallback callback) {
        TransactionImpl ti = new TransactionImpl(this);
        ti._ReadOnly = false;
        _NativeService.beginTransaction(_Filename, _Version, errToken, successToken)
        StaticDependencies.getInstance().getLogDelegate().log("DatabasePrototype.transaction()");
        txContainer.execute(ti);
        _NativeService.endTransaction(id);
    }

    @Override
    public void readTransaction(TxContainer txContainer) {
        
    }
}
