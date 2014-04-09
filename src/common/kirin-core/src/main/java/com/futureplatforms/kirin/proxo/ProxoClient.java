package com.futureplatforms.kirin.proxo;

import com.futureplatforms.kirin.dependencies.db.Transaction;
import com.futureplatforms.kirin.dependencies.json.JSONObject;

public abstract class ProxoClient {
	// Called when the given object is to be inserted or updated to the database
	public abstract void onInsertOrUpdate(Transaction tx, JSONObject obj);
	
	// Called when the given object is to be deleted
	public abstract void onDelete(Transaction tx, JSONObject obj);
	
	// Called when all insertions and deletions have been performed, but the
	// transaction has not yet been committed
	public void onSyncCompleting(Transaction tx) {}
	
	// Called when the transaction has been committed 
	public abstract void onSyncComplete(boolean updated, boolean baked);
	public abstract void onError();
}
