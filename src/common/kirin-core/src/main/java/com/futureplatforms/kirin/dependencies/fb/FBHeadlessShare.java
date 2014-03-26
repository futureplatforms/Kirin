package com.futureplatforms.kirin.dependencies.fb;

import java.util.Map;

import com.futureplatforms.kirin.dependencies.AsyncCallback.AsyncCallback1;
import com.futureplatforms.kirin.dependencies.StaticDependencies;
import com.futureplatforms.kirin.dependencies.StaticDependencies.LogDelegate;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate.HttpVerb;
import com.futureplatforms.kirin.dependencies.fb.FacebookDelegate.FBGraphResponse;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.FacebookLoginCallback;
import com.futureplatforms.kirin.dependencies.fb.FacebookDetails.PublishPermission;
import com.futureplatforms.kirin.dependencies.fb.SharingDelegate.Platform;
import com.futureplatforms.kirin.dependencies.fb.SharingDelegate.PostShareable;
import com.futureplatforms.kirin.dependencies.fb.SharingDelegate.ShareResponse;
import com.futureplatforms.kirin.dependencies.json.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;

public class FBHeadlessShare {
	private static LogDelegate ld = StaticDependencies.getInstance().getLogDelegate();
	private static FacebookDelegate fb = StaticDependencies.getInstance().getFacebookDelegate();
	
	private FBHeadlessShare() {}
	
	private static void doFeedPost(final String imageUrl, final String name, final String message, final String link, final AsyncCallback1<JSONObject> cb) {
		Map<String, String> map = Maps.newHashMap();
		map.put("message", message);
		if (!Strings.isNullOrEmpty(imageUrl)) {
			map.put("picture", imageUrl);
		}
		map.put("name", name);
		map.put("link", link);
		fb.graph(HttpVerb.POST, "/me/feed", map, new FBGraphResponse() {
			
			@Override
			public void onSuccess(JSONObject obj) {
				cb.onSuccess(obj);
			}
			
			@Override
			public void onNetError() {
				cb.onFailure();
			}
			
			@Override
			public void onAuthFailed() {
				// looks like we actually don't have publish permissions after all!
				// try again
				requestPublishPermissions(imageUrl, name, message, link, cb);
			}
		});
	}
	
	private static void requestPublishPermissions(final String imageUrl, final String name, final String message, final String link, final AsyncCallback1<JSONObject> cb) {
		fb.requestPublishPermissions(
				new FacebookLoginCallback() {
					
					@Override
					public void onSuccess() {
						ld.log("requestPublishPermissions onSuccess");
						doFeedPost(imageUrl, name, message, link, cb);
					}
					
					@Override
					public void onFailure() {
						ld.log("requestPublishPermissions onFailure");
						cb.onFailure();
					}
					
					@Override
					public void onUserCancel() {
						ld.log("requestPublishPermissions onUserCancel");
						cb.onFailure();
					}
				}, PublishPermission.publish_actions);
	}
	private static void openSessionWithReadPermissions(final String imageUrl, final String name, final String message, final String link, final AsyncCallback1<JSONObject> cb) {
		fb.openSessionWithReadPermissions(new FacebookLoginCallback() {
			
			@Override
			public void onSuccess() {
				ld.log("openSessionWithReadPermissions onSuccess");
				requestPublishPermissions(imageUrl, name, message, link, cb);
			}
			
			@Override
			public void onFailure() {
				ld.log("openSessionWithReadPermissions onFailure");
				
			}
			
			@Override
			public void onUserCancel() {
				ld.log("openSessionWithReadPermissions onUserCancel");
				
			}
		}, true, FacebookDetails.ReadPermission.read_stream);
	}
	
	public static void headlessShare(PostShareable shareable, final ShareResponse cb) {
		openSessionWithReadPermissions(
				shareable.imageUrl(), 
				shareable.name(), 
				shareable.text(), 
				shareable.link(), new AsyncCallback1<JSONObject>() {
					@Override
					public void onSuccess(JSONObject res) {
						cb.onOK(Platform.Facebook);
					}

					@Override
					public void onFailure() {
						cb.onFailure();
					}
				});
		
	}
}
