package com.futureplatforms.kirin.dependencies.fb;

import java.util.List;

import com.futureplatforms.kirin.dependencies.AsyncCallback;
import com.futureplatforms.kirin.dependencies.AsyncCallback.AsyncCallback1;
import com.google.common.collect.ImmutableList;

public interface FacebookDetails {
	public static interface FacebookLoginCallback extends AsyncCallback {
		public void onUserCancel();
		public void onFailWithUserMessage(String msg);
	}
	
	public static interface FacebookRequestsCallback extends AsyncCallback1<String[]> {
		public void onUserCancel();
	}
	
	public static interface FacebookShareCallback extends AsyncCallback1<String> {
		public void onUserCancel();
	}
	
	public static interface FacebookJSONCallback extends AsyncCallback1<String> {}
	
	public static interface Permission {}
	
	public enum ReadPermission implements Permission {
		email, user_birthday, user_location, // Basic profile read permissions
		
		// Below are read permissions
		user_about_me, friends_about_me, user_activities,
		friends_activities, friends_birthday,
		user_checkins, friends_checkins, user_education_history,
		friends_education_history, user_events, friends_events,
		user_groups, friends_groups, user_hometown,
		friends_hometown, user_interests, friends_interests,
		user_likes, friends_likes, user_notes,
		friends_notes, user_online_presence, friends_online_presence,
		user_religion_politics, friends_religion_politics, 
		user_status, friends_status,
		user_subscriptions, friends_subscriptions, user_videos,
		friends_videos, user_website, friends_website,
		user_work_history, friends_work_history, read_friendlists,
		read_mailbox, read_requests, read_stream,
		read_insights, xmpp_login
	}
	
	public enum PublishPermission implements Permission {
		ads_management, create_event, rsvp_event,
		manage_friendlists, manage_notifications, manage_pages,
		publish_actions
	}
	
	public static class ShareDialogParams {
		public final String _Caption, _Description, _Link, _Name, _Picture, _Place, _Ref;
		public final ImmutableList<String> _Friends;
		public ShareDialogParams(String caption, String description, String link, String name, String picture, String place, String ref, List<String> friends) {
			this._Caption = caption;
			this._Description = description;
			this._Link = link;
			this._Name = name;
			this._Picture = picture;
			this._Place = place;
			this._Ref = ref;
			if (friends != null) {
				this._Friends = ImmutableList.copyOf(friends);
			} else {
				this._Friends = ImmutableList.of();
			}
		}
		
		@Override
		public String toString() {
			return "ShareDialogParams:\n" +
					"Caption: " + _Caption + "\n" +
					"Description: " + _Description + "\n" +
					"Link: " + _Link + "\n" +
					"Name: " + _Name + "\n" +
					"Picture: " + _Picture + "\n" +
					"Place: " + _Place + "\n" +
					"Ref: " + _Ref;
		}
	}
}
