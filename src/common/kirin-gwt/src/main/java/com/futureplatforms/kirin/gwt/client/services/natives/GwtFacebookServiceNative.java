package com.futureplatforms.kirin.gwt.client.services.natives;

import com.futureplatforms.kirin.gwt.client.IKirinNativeService;

public interface GwtFacebookServiceNative extends IKirinNativeService {
	public void getAccessToken(int cbId);
	public void isLoggedIn(int cbId);
	public void getAppId(int cbId);
	public void openSessionWithReadPermissions(String[] permissions, boolean allowUI, int cbId);
	public void requestPublishPermissions(String[] permissions, int cbId);
	public void getCurrentPermissions(int cbId);
	public void signOut();
	public void presentShareDialogWithParams(String caption, String description, String link, String name, String picture, String place, String reference, String[] friends, int cbId);
	public void presentRequestsDialog(int cbId);
	public void logEvent(String eventName, String[] paramKeys, String[] paramVals);
}
