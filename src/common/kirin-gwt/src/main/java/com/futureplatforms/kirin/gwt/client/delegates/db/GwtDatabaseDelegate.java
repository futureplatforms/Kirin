package com.futureplatforms.kirin.gwt.client.delegates.db;

import com.futureplatforms.kirin.dependencies.db.Database;
import com.futureplatforms.kirin.dependencies.db.DatabaseDelegate;

public class GwtDatabaseDelegate implements DatabaseDelegate {
	// factory to make it easy to create GwtDatabase in jsni code
	private GwtDatabase createDB(int dbId) {
		return new GwtDatabase(dbId);
	}
	
	@Override
	public native void open(String filename, DatabaseOpenedCallback cb) /*-{
		var that = this;
		var dbAccess = $wnd.EXPOSED_TO_NATIVE.native2js.resolveModule("DatabaseAccessService");
		var success = function(id) {
			var onOpened = cb.@com.futureplatforms.kirin.dependencies.db.DatabaseDelegate$DatabaseOpenedCallback::onOpened(Lcom/futureplatforms/kirin/dependencies/db/Database;);
			var createDB = that.@com.futureplatforms.kirin.gwt.client.delegates.db.GwtDatabaseDelegate::createDB(I);
			onOpened(createDB(id));
		}
		var failure = function() {
			var onError = cb.@com.futureplatforms.kirin.dependencies.db.DatabaseDelegate$DatabaseOpenedCallback::onError();
			onError();
		}
		dbAccess._openDatabase(filename, success, failure);
	}-*/;

	@Override
	public void close(Database db) {
		_close(((GwtDatabase)db)._DbId);
	}
	
	private native static void _close(int id) /*-{
		var dbAccess = $wnd.EXPOSED_TO_NATIVE.native2js.resolveModule("DatabaseAccessService");
		dbAccess._closeDatabase(id);
	}-*/;
}
