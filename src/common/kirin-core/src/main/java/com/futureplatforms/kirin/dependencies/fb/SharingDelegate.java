package com.futureplatforms.kirin.dependencies.fb;

import java.util.List;
import java.util.Map;

import com.futureplatforms.kirin.dependencies.AsyncCallback;
import com.futureplatforms.kirin.dependencies.AsyncCallback.AsyncCallback1;
import com.futureplatforms.kirin.dependencies.StaticDependencies;
import com.futureplatforms.kirin.dependencies.StaticDependencies.LogDelegate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public abstract class SharingDelegate {

	private static final LogDelegate _LD = StaticDependencies.getInstance().getLogDelegate();
	
	private Map<Platform, ShareHandler> _ShareHandlers;
	public enum Platform { Facebook, Twitter, Email, SMS };
	
	public static interface Shareable {
		public String text();
	}
	
	public static interface PostShareable extends Shareable {
		public String link();
		public String imageUrl();
		public String name();
		public String shortText();
	}


	public static interface ImageShareable extends PostShareable {
		public String imageToken();
		public String fileName();
	}
	public static interface ShareSheetResponse {
		public void onOK(Platform platform);
		public void onCancel();
	}
	
	public static interface ShareResponse extends ShareSheetResponse {
		public void onOK(Platform platform);
		public void onFailure();
	}
	
	public static interface ShareHandler {
		public void share(Shareable sh, ShareResponse cb);
	}
	
	private void sharePlatform(Platform platform, Shareable sh, Map<Platform, ShareHandler> customHandlers, final ShareResponse cb) {
		if (customHandlers != null && customHandlers.containsKey(platform)) {
			customHandlers.get(platform).share(sh, cb);
		} else if (_ShareHandlers.containsKey(platform)) {
			_ShareHandlers.get(platform).share(sh, cb);
		} else {
			cb.onFailure();
		}
	}
	
	private void getPlatforms(final AsyncCallback cb) {
		getSupportedPlatforms(new AsyncCallback1<SharingDelegate.Platform[]>() {

			@Override
			public void onSuccess(Platform[] supported) {
				_ShareHandlers = Maps.newHashMap();
				_LD.log("Hello from the sharing delegate!  This platform supports:");
				for (Platform platform : supported) {
					_LD.log(platform.name());
					switch (platform) {
						case Twitter: {
							_ShareHandlers.put(Platform.Twitter, new ShareHandler() {
								
								@Override
								public void share(Shareable sh, ShareResponse cb) {
									if (sh instanceof ImageShareable) {
										ImageShareable imSh = (ImageShareable)sh;
										String text = imSh.shortText();
										if(text == null) text = imSh.text();
										nativeShareTwitter(text, imSh.imageToken(), imSh.link(), cb);
									} else if (sh instanceof PostShareable) {
										PostShareable postSh = (PostShareable)sh;
										String text = postSh.shortText();
										if(text == null) text = postSh.text();
										nativeShareTwitter(text, null, postSh.link(), cb);
									}
								}
							});
						} break;
						
						case Email: {
							_ShareHandlers.put(Platform.Email, new ShareHandler() {
								
								@Override
								public void share(Shareable sh, ShareResponse cb) {
									if (sh instanceof ImageShareable) {
										ImageShareable imageSh = (ImageShareable)sh;
										nativeShareEmail(imageSh.name(), imageSh.text()+"\n"+imageSh.link(),imageSh.imageToken(), cb);
									} else if (sh instanceof PostShareable) {
										PostShareable postSh = (PostShareable)sh;
										nativeShareEmail(postSh.name(), postSh.text()+"\n"+postSh.link(),null, cb);
									}
								}
							});
						} break;
						
						case SMS: {
							_ShareHandlers.put(Platform.SMS, new ShareHandler() {
								
								@Override
								public void share(Shareable sh, ShareResponse cb) {
									if (sh instanceof ImageShareable) {
										cb.onFailure();
									} else if (sh instanceof PostShareable) {
										PostShareable postSh = (PostShareable)sh;
										nativeShareSms(postSh.text()+"\n"+postSh.link(), cb);
									}
								}
							});
						} break;
						
						default: {
							throw new IllegalStateException("something went wrong");
						} 
					}
 				}
				
				cb.onSuccess();
			}

			@Override
			public void onFailure() { cb.onFailure(); }
		});
	}
	
	private List<Platform> getSupported(Map<Platform, ShareHandler> custom, Platform ... desired) {
		List<Platform> supported = Lists.newArrayList();
		for (Platform platform : desired) {
			if (_ShareHandlers.containsKey(platform) || custom.containsKey(platform)) {
				supported.add(platform);
			}
		}
		return supported;
	}
	
	private void doShare(final Shareable sh, final Map<Platform, ShareHandler> customHandlers, final ShareResponse cb, Platform ... platforms) {
		if (platforms == null || platforms.length == 0) { platforms = Platform.values(); }
		List<Platform> supported = getSupported(customHandlers, platforms);
		if (supported.isEmpty()) {
			cb.onFailure();
		} else if (supported.size() == 1) {
			Platform platform = supported.get(0);
			sharePlatform(platform, sh, customHandlers, cb);
		} else {
			showShareSheet(new ShareSheetResponse() {
				
				@Override
				public void onOK(Platform platform) {
					sharePlatform(platform, sh, customHandlers, cb);
				}
				
				@Override
				public void onCancel() {
					cb.onCancel();
				}
				
			}, supported);
		}
	}
	
	public void share(final Shareable sh, final Map<Platform, ShareHandler> customHandlers, final ShareResponse cb, final Platform ... platforms) {
		if (_ShareHandlers == null) {
			getPlatforms(new AsyncCallback() {
				
				@Override
				public void onSuccess() {
					doShare(sh, customHandlers, cb, platforms);
				}
				
				@Override
				public void onFailure() {}
			});
		} else {
			doShare(sh, customHandlers, cb, platforms);
		}
	}
	
	public abstract void showShareSheet(ShareSheetResponse cb, List<Platform> platforms);
	
	public abstract void nativeShareSms(String text, ShareResponse cb);
	public abstract void nativeShareEmail(String subject, String body, String imageToken, ShareResponse cb);
	public abstract void nativeShareTwitter(String text, String imageToken, String url, ShareResponse cb);
	
	public abstract void getSupportedPlatforms(AsyncCallback1<Platform[]> cb);
}
