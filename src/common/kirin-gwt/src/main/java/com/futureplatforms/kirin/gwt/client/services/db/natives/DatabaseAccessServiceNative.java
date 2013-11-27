package com.futureplatforms.kirin.gwt.client.services.db.natives;

import com.futureplatforms.kirin.gwt.client.IKirinNativeService;

public interface DatabaseAccessServiceNative extends IKirinNativeService {

	public void open(String filename, int dbId);
	public void close(int dbId);

}
