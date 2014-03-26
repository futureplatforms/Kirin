package com.futureplatforms.kirin.gwt.client.services.natives.fb;

import com.futureplatforms.kirin.gwt.client.IKirinNativeService;

public interface GwtShareServiceNative extends IKirinNativeService {
	void showShareSheet(String[] platforms);
	void shareSms(String text, int cbId);
	void shareTwitter(String text, String img, String link, int cbId);
	void shareEmail(String subject, String text, int cbId);
	void getSupportedPlatforms();
	
}
