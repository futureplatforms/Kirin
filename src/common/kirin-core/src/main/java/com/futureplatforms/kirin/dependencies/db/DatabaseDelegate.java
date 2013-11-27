package com.futureplatforms.kirin.dependencies.db;


public interface DatabaseDelegate {
	public static interface DatabaseOpenedCallback {
		public void onOpened(Database db);
		public void onError();
	}
	
	public void open(String filename, DatabaseOpenedCallback cb);
	public void close(Database db);
}
