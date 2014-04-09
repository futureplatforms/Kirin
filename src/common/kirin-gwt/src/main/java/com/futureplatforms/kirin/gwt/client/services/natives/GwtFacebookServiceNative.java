package com.futureplatforms.kirin.gwt.client.services.natives;

import com.futureplatforms.kirin.gwt.client.IKirinNativeService;

public interface GwtFacebookServiceNative extends IKirinNativeService {
	public void getAccessToken();
	public void isLoggedIn();
	public void getAppId();
	public void openSessionWithReadPermissions(String[] permissions, boolean allowUI, int cbId);
	public void requestPublishPermissions(String[] permissions, int cbId);
	public void requestForUploadPhoto(String message, String image, int cbId);
	public void getCurrentPermissions();
	public void signOut();
	public void presentShareDialogWithParams(String caption, String description, String link, String name, String picture, String place, String ref, int cbId);
}
