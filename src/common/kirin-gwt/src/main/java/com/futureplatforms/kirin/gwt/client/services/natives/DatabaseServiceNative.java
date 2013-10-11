package com.futureplatforms.kirin.gwt.client.services.natives;

import com.futureplatforms.kirin.gwt.client.IKirinNativeService;

public interface DatabaseServiceNative extends IKirinNativeService {
    public void openOrCreate(String filename, int version, String txId, String onOpenedToken, String onErrorToken);
    public void beginTransaction(String filename, String txId, int errToken, int successToken);
    public void endTransaction(String txId);
    public void appendToTransaction(String txId, String type, String statement, );
}
