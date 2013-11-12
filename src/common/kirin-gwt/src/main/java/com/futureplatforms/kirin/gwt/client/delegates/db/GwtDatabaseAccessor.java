package com.futureplatforms.kirin.gwt.client.delegates.db;

import com.futureplatforms.kirin.dependencies.internal.DatabaseAccessorBackend;
import com.futureplatforms.kirin.dependencies.internal.DatabaseBackend;
import com.google.gwt.core.client.JavaScriptObject;

public class GwtDatabaseAccessor implements DatabaseAccessorBackend {
	public static class DatabaseRunner {
		private DatabaseOpenedCallback _Callback;
		public DatabaseRunner(DatabaseOpenedCallback callback) {
			this._Callback = callback;
		}
		
        public void onOpened(int id) {
        	_Callback.onOpened(new DatabaseBackend() {
				
				@Override
				public void beginTransaction(BeginTransactionCallback callback) {
					
				}
			});
        }
        
        public void onError() {
        	_Callback.onError();
        }
		
		public void doIt(String filename) {
			_doIt(filename, getDatabaseService(), this);
		}
		
		private static native void _doIt(String filename, JavaScriptObject db, DatabaseRunner p) /*-{
        	var success = function(id) {
        		var onOpened = p.@com.futureplatforms.kirin.gwt.client.delegates.db.GwtDatabaseAccessor$DatabaseRunner::onOpened(I);
        		onOpened(id);
        	};
        	var failure = function() {
        		var onError = p.@com.futureplatforms.kirin.gwt.client.delegates.db.GwtDatabaseAccessor$DatabaseRunner::onError();
        		onError();
        	};
        	db._openDatabase(filename, success, failure);
        }-*/;
	}
	
	protected static native JavaScriptObject getDatabaseService() /*-{
		return $wnd.EXPOSED_TO_NATIVE.native2js.resolveModule("DatabaseService");
	}-*/;
	
	@Override
	public void open(String filename, DatabaseOpenedCallback cb) {
		new DatabaseRunner(cb).doIt(filename);
	}
}
