package com.futureplatforms.kirin.gwt.client.services.db;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportClosure;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

import com.futureplatforms.kirin.dependencies.StaticDependencies;
import com.futureplatforms.kirin.gwt.client.KirinService;
import com.futureplatforms.kirin.gwt.client.services.natives.DatabaseServiceNative;
import com.futureplatforms.kirin.gwt.compile.NoBind;
import com.google.gwt.core.client.GWT;

@Export(value = "DatabaseService", all = true)
@ExportPackage("screens")
public class DatabaseService extends KirinService<DatabaseServiceNative> {
    
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
    public DatabasePrototype _openDatabase(String filename) {
        //getNativeObject().openOrCreate(filename, version, txId, onOpenedToken, onErrorToken)
        StaticDependencies.getInstance().getLogDelegate().log("DatabaseService._openDatabase(" + filename + ", " + version + ")");
        DatabasePrototype db = new DatabasePrototype(filename, getNativeObject());
        TransactionImpl tx = new TransactionImpl(db);
        tx._ReadOnly = false;
        
        return db;
    }
}
