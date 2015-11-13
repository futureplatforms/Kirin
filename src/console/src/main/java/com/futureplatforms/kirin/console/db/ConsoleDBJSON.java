package com.futureplatforms.kirin.console.db;

import java.util.List;

import com.futureplatforms.kirin.console.db.ConsoleDB.ResultSetProcessor;
import com.futureplatforms.kirin.dependencies.StaticDependencies;
import com.futureplatforms.kirin.dependencies.db.Transaction.StatementWithJSONReturn;
import com.futureplatforms.kirin.dependencies.json.JSONArray;
import com.futureplatforms.kirin.dependencies.json.JSONDelegate;
import com.futureplatforms.kirin.dependencies.json.JSONException;
import com.futureplatforms.kirin.dependencies.json.JSONObject;

public class ConsoleDBJSON implements ResultSetProcessor {

	private StatementWithJSONReturn _Statement;
	private List<String> _ColNames;
	private static JSONDelegate _Json = StaticDependencies.getInstance().getJsonDelegate();
	private JSONArray _Arr = _Json.getJSONArray();
	
	public ConsoleDBJSON(StatementWithJSONReturn statement) {
		this._Statement = statement;
	}
	
	@Override
	public void columnNames(List<String> colNames) {
		_ColNames = colNames;
	}

	@Override
	public void nextRow(List<Object> nextRow) {
		try {
			JSONObject obj = _Json.getJSONObject();
			for (int i=0,len=nextRow.size(); i<len; i++) {
				Object col = nextRow.get(i);
				String colName = _ColNames.get(i);
				
				if (col == null) {
					obj.put(colName, null);
				} else if (col instanceof Integer) {
					obj.put(colName, (Integer) col);
				} else if (col instanceof Double) {
					obj.put(colName, (Double) col);
				} else if (col instanceof String) {
					obj.put(colName, (String) col);
				} else if (col instanceof Boolean) {
					obj.put(colName, (Boolean) col);
				} else {
					obj.put(colName, col.toString());
				}
			}
			
			_Arr.putObject(obj);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void finished() {
		_Statement._Callback.onSuccess(_Arr);
	}

}
