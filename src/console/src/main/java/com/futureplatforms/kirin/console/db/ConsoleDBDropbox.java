package com.futureplatforms.kirin.console.db;

import java.sql.ResultSet;
import java.util.Map;

import com.google.common.collect.Maps;

public class ConsoleDBDropbox {

	private ConsoleDBDropbox() {}
	
	private final Map<Integer, ResultSet> _Map = Maps.newHashMap();

	private int _NextToken = Integer.MIN_VALUE;
	
	protected String putResultSet(ResultSet rs) {
		int next = _NextToken;
		_NextToken++;
		_Map.put(next, rs);
		return ""+next;
	}
	
	public ResultSet getResultSet(String token) {
		return _Map.remove(Integer.parseInt(token, 10));
	}
	
	private static ConsoleDBDropbox instance;
	public static ConsoleDBDropbox getInstance() {
		if (instance == null) { return new ConsoleDBDropbox(); }
		return instance;
	}
}
