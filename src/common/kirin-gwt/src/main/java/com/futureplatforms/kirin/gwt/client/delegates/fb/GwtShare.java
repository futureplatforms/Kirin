package com.futureplatforms.kirin.gwt.client.delegates.fb;

import java.util.List;

import com.futureplatforms.kirin.dependencies.AsyncCallback.AsyncCallback1;
import com.futureplatforms.kirin.dependencies.fb.SharingDelegate;
import com.futureplatforms.kirin.gwt.client.services.fb.GwtShareService;

public class GwtShare extends SharingDelegate {

	@Override
	public void showShareSheet(ShareSheetResponse cb, List<Platform> platforms) {
		GwtShareService.BACKDOOR()._showShareSheet(cb, platforms);
	}

	@Override
	public void nativeShareSms(String text, ShareResponse cb) {
		GwtShareService.BACKDOOR()._shareSms(text, cb);
	}

	@Override
	public void nativeShareEmail(String subject, String body, String imageToken, ShareResponse cb) {
		GwtShareService.BACKDOOR()._shareEmail(subject, body, cb);
	}

	@Override
	public void getSupportedPlatforms(AsyncCallback1<Platform[]> cb) {
		GwtShareService.BACKDOOR()._getSupportedPlatforms(cb);
	}

	@Override
	public void nativeShareTwitter(String text, String imageToken, String url,
			ShareResponse cb) {
		GwtShareService.BACKDOOR()._shareTwitter(text, imageToken, url, cb);
	}
}
