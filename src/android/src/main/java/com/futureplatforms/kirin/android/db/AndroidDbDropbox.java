package com.futureplatforms.kirin.android.db;

import java.util.Map;

import com.google.common.collect.Maps;

import android.database.Cursor;

public class AndroidDbDropbox {

	private AndroidDbDropbox() {}
	
	private final Map<Integer, Cursor> _Map = Maps.newHashMap();

	private int _NextToken = Integer.MIN_VALUE;
	
	protected String putCursor(Cursor c) {
		int next = _NextToken;
		_NextToken++;
		_Map.put(next, c);
		return ""+next;
	}
	
	public Cursor getCursor(String token) {
		return _Map.remove(Integer.parseInt(token, 10));
	}
	
	private static AndroidDbDropbox instance;
	public static AndroidDbDropbox getInstance() {
		if (instance == null) { return new AndroidDbDropbox(); }
		return instance;
	}
}
