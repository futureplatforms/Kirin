package com.futureplatforms.kirin.dependencies.internal;

public interface DatabaseAccessorBackend {
	
	public static interface DatabaseOpenedCallback {
		public void onOpened(DatabaseBackend db);
		public void onError();
	}
	
	public void open(String filename, DatabaseOpenedCallback cb);
}
