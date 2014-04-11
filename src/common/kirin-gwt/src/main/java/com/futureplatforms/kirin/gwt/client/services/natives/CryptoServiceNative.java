package com.futureplatforms.kirin.gwt.client.services.natives;

import com.futureplatforms.kirin.gwt.client.IKirinNativeService;

public interface CryptoServiceNative extends IKirinNativeService {
	void pbkdf2(String cbId, String plaintext, String salt, int iterations, int keyLenBytes);
}
