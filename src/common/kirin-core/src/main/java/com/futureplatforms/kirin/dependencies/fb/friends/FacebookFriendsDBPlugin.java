package com.futureplatforms.kirin.dependencies.fb.friends;

import java.util.List;

import com.futureplatforms.kirin.dependencies.AsyncCallback;
import com.futureplatforms.kirin.dependencies.AsyncCallback.AsyncCallback1;
import com.futureplatforms.kirin.dependencies.fb.friends.FacebookFriends.Friend;

public interface FacebookFriendsDBPlugin {
	public void resetDB(AsyncCallback cb);
	public void saveFriends(List<Friend> friends, AsyncCallback cb);
	public void getFriends(AsyncCallback1<List<Friend>> cb);
	public void friendForUid(String uid, AsyncCallback1<Friend> cb);
}
