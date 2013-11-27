package com.futureplatforms.kirin.dependencies.internal;

public interface TransactionAccessBackend {
	public static interface TransactionCallback {
		public void onSuccess(TransactionBackend tx);
		public void onError();
	}
	public void performTransaction(TransactionCallback callback);
}
