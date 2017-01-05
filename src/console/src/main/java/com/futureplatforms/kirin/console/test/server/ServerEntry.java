package com.futureplatforms.kirin.console.test.server;

import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate.NetworkResponse;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ServerEntry {

	public enum EntryType { Resource, Text }

	public interface Condition {
		boolean matches(String postData);
	}

	private static final Condition TRUE_CONDITION = new Condition() {
		@Override
		public boolean matches(String postData) {
			return true;
		}
	};
	
	private final String mUrlContains, mToReturn;
	private final Condition mCondition;
	private final Map<String, String> mHeadersToReturn;
	private final EntryType mEntryType;

	public ServerEntry(String urlContains, String toReturn) {
		this(urlContains, TRUE_CONDITION, toReturn);
	}

	public ServerEntry(String urlContains, String textToReturn, EntryType et) {
		this(urlContains, TRUE_CONDITION, textToReturn, new HashMap<String, String>(), et);
	}

	public ServerEntry(String urlContains, String toReturn, Map<String, String> headersToReturn) {
		this(urlContains, TRUE_CONDITION, toReturn, headersToReturn);
	}

	public ServerEntry(String urlContains, String toReturn, Map<String, String> headersToReturn, EntryType et) {
		this(urlContains, TRUE_CONDITION, toReturn, headersToReturn, et);
	}

	public ServerEntry(String urlContains, Condition condition, String toReturn) {
		this(urlContains, condition, toReturn, new HashMap<String, String>());
	}

	public ServerEntry(String urlContains, Condition condition, String toReturn, EntryType et) {
		this(urlContains, condition, toReturn, new HashMap<String, String>(), et);
	}

	public ServerEntry(String urlContains, Condition condition, String toReturn, Map<String, String> headersToReturn) {
		this(urlContains, condition, toReturn, headersToReturn, EntryType.Resource);
	}

	public ServerEntry(String urlContains, Condition condition, String toReturn, Map<String, String> headersToReturn, EntryType et) {
		this.mUrlContains = urlContains;
		this.mCondition = condition;
		this.mToReturn = toReturn;
		this.mHeadersToReturn = headersToReturn;
		this.mEntryType = et;
	}

	public boolean matches(String url, String postData) {
		return url.contains(this.mUrlContains) && mCondition.matches(postData);
	}
	
	public void result(NetworkResponse callback) {
		if (mEntryType == EntryType.Resource) {
			try {
				URL res = Server.class.getResource("/" + mToReturn);
				String toReturn = Resources.toString(res, Charsets.UTF_8);
				toReturn = toReturn.replaceAll("&\\s+", "&amp;");
				callback.callOnSuccess(200, toReturn, mHeadersToReturn);
			} catch (IOException e) {
				callback.callOnFail("Test server could not load resource " + mToReturn);
			}
		} else {
			callback.callOnSuccess(200, mToReturn, mHeadersToReturn);
		}
	}
}
