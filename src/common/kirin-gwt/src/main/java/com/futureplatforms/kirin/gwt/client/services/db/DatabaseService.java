package com.futureplatforms.kirin.gwt.client.services.db;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportClosure;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

import com.futureplatforms.kirin.dependencies.StaticDependencies;
import com.futureplatforms.kirin.dependencies.db.DatabasesDelegate.TxContainer;
import com.futureplatforms.kirin.dependencies.db.DatabasesDelegate.TxContainer.TxContainerCallback;
import com.futureplatforms.kirin.gwt.client.KirinService;
import com.futureplatforms.kirin.gwt.client.services.natives.DatabaseServiceNative;
import com.futureplatforms.kirin.gwt.compile.NoBind;
import com.google.gwt.core.client.GWT;

@Export(value = "DatabaseService", all = true)
@ExportPackage("screens")
public class DatabaseService extends KirinService<DatabaseServiceNative> {
    
    @Export
    @ExportClosure
    public static interface CreateUpdate extends Exportable {
        @Export
        public void execute(StatementBuilder tx);
    }
    
    @Export
    @ExportClosure
    public static interface DBErr extends Exportable {
        @Export
        public void execute();
    }
    
    public DatabaseService() {
        super(GWT.<DatabaseServiceNative>create(DatabaseServiceNative.class));
    }

    @NoBind
    public DatabasePrototype _openDatabase(String filename, int version, CreateUpdate onCreate, CreateUpdate onUpdate, DBErr onError) {
        //getNativeObject().openOrCreate(filename, version, txId, onOpenedToken, onErrorToken)
        StaticDependencies.getInstance().getLogDelegate().log("DatabaseService._openDatabase(" + filename + ", " + version + ")");
        DatabasePrototype db = new DatabasePrototype(filename, version, getNativeObject());
        TransactionImpl tx = new TransactionImpl(db);
        tx._ReadOnly = false;
        
        return db;
    }
}
