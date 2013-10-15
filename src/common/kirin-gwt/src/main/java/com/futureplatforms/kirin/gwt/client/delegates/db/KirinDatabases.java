package com.futureplatforms.kirin.gwt.client.delegates.db;

import com.futureplatforms.kirin.dependencies.internal.DatabaseAccessorBackend;

public class KirinDatabases implements DatabaseAccessorBackend {
	public static class DatabaseRunner {
		private DatabaseOpenedCallback _Callback;
		public DatabaseRunner(DatabaseOpenedCallback callback) {
			this._Callback = callback;
		}
		
        public void onOpened() {
        	
        }
        
        public void onError() {
        	
        }
		
		public void doIt(String filename) {
			_doIt(filename, this);
		}
		
		private static native void _doIt(String filename, DatabaseRunner p) /*-{
        	var networking = $wnd.EXPOSED_TO_NATIVE.native2js.resolveModule("DatabaseService");
        }-*/;
	}
	
	@Override
	public void open(String filename, DatabaseOpenedCallback cb) {
		new DatabaseRunner(cb).doIt(filename);
	}
}
