package com.futureplatforms.kirin.gwt.client.delegates.db;

import com.futureplatforms.kirin.dependencies.internal.DatabaseBackend;
import com.google.gwt.core.client.JavaScriptObject;

public class GwtDatabaseBackend implements DatabaseBackend {

	@Override
	public void beginTransaction(BeginTransactionCallback callback) {
		_beginTransaction(GwtDatabaseAccessor.getDatabaseService(), this);
	}
	
	private static native void _beginTransaction(JavaScriptObject db, GwtDatabaseBackend p) /*-{
		db
	}-*/;

}
