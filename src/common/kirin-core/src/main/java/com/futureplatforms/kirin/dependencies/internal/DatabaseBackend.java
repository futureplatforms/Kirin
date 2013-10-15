package com.futureplatforms.kirin.dependencies.internal;

public interface DatabaseBackend {
	public static interface BeginTransactionCallback {
		public void onSuccess(TransactionBackend tx);
		public void onError();
	}
	public void beginTransaction(BeginTransactionCallback callback);
}
