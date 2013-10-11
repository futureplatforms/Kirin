package com.futureplatforms.kirin.gwt.client.delegates.db;

import com.futureplatforms.kirin.dependencies.db.DatabasesDelegate.TxContainer;
import com.futureplatforms.kirin.dependencies.db.DatabasesDelegate.TxContainer.TxContainerCallback;

public class PerformTransaction {
    private TxContainerCallback _TxContainerCallback;
    
    public PerformTransaction(TxContainer txContainer,
            TxContainerCallback callback) {
        _transaction();
    }
    
    private void transactionComplete() {
        this._TxContainerCallback.onComplete();
    }
    
    private void transactionError(String err) {
        this._TxContainerCallback.onError(err);
    }
    
    private static native void _transaction() /*-{
        var databases = $wnd.EXPOSED_TO_NATIVE.native2js.resolveModule("DatabaseService");
        databases._transaction
    }-*/;
}
