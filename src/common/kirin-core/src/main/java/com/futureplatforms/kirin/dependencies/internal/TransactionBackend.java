package com.futureplatforms.kirin.dependencies.internal;


public interface TransactionBackend {
	public void pullTrigger(TransactionBundle bundle);
}
