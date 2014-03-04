package com.futureplatforms.kirin.proxo;

import com.futureplatforms.kirin.dependencies.db.Transaction;
import com.futureplatforms.kirin.dependencies.json.JSONObject;

public interface ProxoClient {
	void onDelete(Transaction tx, JSONObject obj);
	void onInsertOrUpdate(Transaction tx, JSONObject obj);
	void onSyncComplete();
	void onError();
}
