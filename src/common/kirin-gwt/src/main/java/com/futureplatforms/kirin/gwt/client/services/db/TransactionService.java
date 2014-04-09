package com.futureplatforms.kirin.gwt.client.services.db;

import java.util.List;
import java.util.Map;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.NoExport;

import com.futureplatforms.kirin.dependencies.StaticDependencies;
import com.futureplatforms.kirin.dependencies.db.Database.TransactionCallback;
import com.futureplatforms.kirin.dependencies.db.Database.TxRunner;
import com.futureplatforms.kirin.dependencies.db.Transaction.RowSet;
import com.futureplatforms.kirin.dependencies.db.Transaction.Statement;
import com.futureplatforms.kirin.dependencies.db.Transaction.StatementWithJSONReturn;
import com.futureplatforms.kirin.dependencies.db.Transaction.StatementWithRowsReturn;
import com.futureplatforms.kirin.dependencies.db.Transaction.StatementWithTokenReturn;
import com.futureplatforms.kirin.dependencies.db.Transaction.TxElementType;
import com.futureplatforms.kirin.dependencies.db.Transaction.TxJSONCB;
import com.futureplatforms.kirin.dependencies.db.Transaction.TxRowsCB;
import com.futureplatforms.kirin.dependencies.db.Transaction.TxTokenCB;
import com.futureplatforms.kirin.dependencies.internal.TransactionBundle;
import com.futureplatforms.kirin.dependencies.json.JSONArray;
import com.futureplatforms.kirin.dependencies.json.JSONException;
import com.futureplatforms.kirin.gwt.client.KirinService;
import com.futureplatforms.kirin.gwt.client.delegates.db.GwtTransactionBackend;
import com.futureplatforms.kirin.gwt.client.services.db.natives.TransactionServiceNative;
import com.futureplatforms.kirin.gwt.compile.NoBind;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gwt.core.client.GWT;

@Export(value = "TransactionService", all = true)
@ExportPackage("screens")
public class TransactionService extends KirinService<TransactionServiceNative>{
	private static TransactionService _Instance;
    
	private enum StatementReturnType { Rows, Token, JSON, Batch };
	
    @NoBind
    @NoExport
    public static TransactionService BACKDOOR() { return _Instance; }
    
	public TransactionService() {
		super(GWT.<TransactionServiceNative>create(TransactionServiceNative.class));
		_Instance = this;
	}
	
	// Map DB -> latest TX ID for that DB
	private Map<Integer, Integer> _LatestTxIdForDbId = Maps.newHashMap();
	
	// Map DB -> (Map TxID -> TxOpen Callback)
	private Map<Integer, Map<Integer, TransactionCallback>> _OpenCallbacks = Maps.newHashMap();
	
	@NoBind
	@NoExport
	// INVOKED VIA BACKDOOR
	public void _BeginTransaction(int dbId, TransactionCallback callback) {
		int nextId;
		if (_LatestTxIdForDbId.containsKey(dbId)) {
			int latest = _LatestTxIdForDbId.get(dbId);
			nextId = latest + 1;
		} else {
			nextId = Integer.MIN_VALUE;
		}
		if (!_OpenCallbacks.containsKey(dbId)) {
			_OpenCallbacks.put(dbId, Maps.<Integer, TransactionCallback>newHashMap());
		}
		Map<Integer, TransactionCallback> callbackMap = _OpenCallbacks.get(dbId);
		callbackMap.put(nextId, callback);
		_LatestTxIdForDbId.put(dbId, nextId);
		getNativeObject().begin(dbId, nextId);
	}
	
	// NATIVE CALLS THIS BACK WHEN TRANSACTION BEGIN IS SUCCESSFUL
	public void transactionBeginOnSuccess(int dbId, int txId) {
		_OpenCallbacks.get(dbId).remove(txId).onSuccess(new GwtTransactionBackend(dbId, txId));
	}
	// END  Callback function for transaction open
	
	// Map database -> (Map transaction -> txBundle)
	private Map<Integer, Map<Integer, TransactionBundle>> _Map = Maps.newHashMap();
	private void map(TransactionBundle bundle, int dbId, int txId) {
		if (!_Map.containsKey(dbId)) {
			_Map.put(dbId, Maps.<Integer, TransactionBundle>newHashMap());
		}
		Map<Integer, TransactionBundle> txMap = _Map.get(dbId);
		txMap.put(txId, bundle);
	}
	
	private static final String paramsToString(String[] params) throws JSONException {
		JSONArray arr = StaticDependencies.getInstance().getJsonDelegate().getJSONArray();
		if (params != null) {
			for (String param : params) {
				arr.putObject(param);
			}
		}
		return arr.toString();
	}
	
	private static final int[] integerArrToIntArr(Integer[] arr) {
		int[] intArr = new int[arr.length];
		for (int i=0, len=arr.length; i<len; i++) {
			intArr[i] = arr[i];
		}
		return intArr;
	}
	
	private static final int[] srtArrToIntArr(StatementReturnType[] arr) {
		int[] intArr = new int[arr.length];
		for (int i=0, len=arr.length; i<len; i++) {
			intArr[i] = arr[i].ordinal();
		}
		return intArr;
	}
	
	@NoBind
	@NoExport
	// THIS GETS INVOKED FROM THE BACKDOOR
	public void _pullTrigger(
			TransactionBundle bundle, 
			int dbId, int txId) {
		map(bundle, dbId, txId);
		int fileCount = 0, statementCount = 0;
    	
    	// run through each transaction element and register it with native
		List<StatementReturnType> returnTypes = Lists.newArrayList();
		List<Integer> statementIds = Lists.newArrayList();
		List<String> statements = Lists.newArrayList();
		List<String> jsonParams = Lists.newArrayList();
		
		try {
	    	for (int i=0, len=bundle._Types.size(); i<len; i++) {
	    		TxElementType type = bundle._Types.get(i);
	    		if (type == TxElementType.Batch) {
	    			String[] batch = bundle._Batches.get(fileCount);
	    			for (String batchStr : batch) {
	    				statementIds.add(0); // this will be ignored on native side
	    				jsonParams.add(""); // as will this
	    				
	    				returnTypes.add(StatementReturnType.Batch);
	    				statements.add(batchStr);
	    			}
	    			fileCount++;
	    		} else {
	    			final Statement statement = bundle._Statements.get(statementCount);
	    			statements.add(statement._SQL);
	    			statementIds.add(statementCount);
	    			jsonParams.add(paramsToString(statement._Params));
	    			if (statement instanceof StatementWithTokenReturn) {
	    				returnTypes.add(StatementReturnType.Token);
	    			} else if (statement instanceof StatementWithJSONReturn) {
	    				returnTypes.add(StatementReturnType.JSON);
	    			} else {
	    				returnTypes.add(StatementReturnType.Rows);
	    			}
	    			statementCount++;
	    		}
	    	}
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	
    	StatementReturnType[] returnTypesArr = returnTypes.toArray(new StatementReturnType[0]);
    	int[] returnTypesArrPrim = srtArrToIntArr(returnTypesArr);
    	
    	Integer[] statementIdsArr = statementIds.toArray(new Integer[0]);
    	int[] statementIdsArrPrim = integerArrToIntArr(statementIdsArr);
    	
    	String[] statementsArr = statements.toArray(new String[0]);
    	String[] jsonParamsArr = jsonParams.toArray(new String[0]);
    	getNativeObject().appendStatements(dbId, txId, returnTypesArrPrim, statementIdsArrPrim, statementsArr, jsonParamsArr);
    	getNativeObject().end(dbId, txId);
	}
	
	// CALL THIS FROM NATIVE IF A STATEMENT FAILED
	public void statementFailure(int dbId, int txId, int statementId) {
		Statement s = _Map.get(dbId).get(txId)._Statements.get(statementId);
		if (s instanceof StatementWithTokenReturn) {
			TxTokenCB c = ((StatementWithTokenReturn) s)._Callback;
			if (c != null) { c.onError(); }
		} else {
			TxRowsCB c = ((StatementWithRowsReturn) s)._Callback;
			if (c != null) { c.onError(); }
		}
	}
	
	// A TOKEN RETURN STATEMENT WAS SUCCESSFUL
	public void statementTokenSuccess(int dbId, int txId, int statementId, String token) {
		Statement s = _Map.get(dbId).get(txId)._Statements.get(statementId);
		TxTokenCB c = ((StatementWithTokenReturn) s)._Callback;
		if (c != null) { c.onSuccess(token); }
	}
	
	
	// BECAUSE WE CAN'T PASS ARRAYS OF ARRAYS, ROW SUCCESSES NEED TO BE PASSED ROW BY ROW
	// SORRY
	private Map<Integer, Map<Integer, Map<Integer, RowSet>>> _RowCallback = Maps.newHashMap();
	private Map<Integer, RowSet> getRowsetMap(int dbId, int txId) {
		if (!_RowCallback.containsKey(dbId)) {
			_RowCallback.put(dbId, Maps.<Integer, Map<Integer, RowSet>>newHashMap());
		}
		Map<Integer, Map<Integer, RowSet>> txMap = _RowCallback.get(dbId);
		
		if (!txMap.containsKey(txId)) {
			txMap.put(txId, Maps.<Integer, RowSet>newHashMap());
		}
		return txMap.get(txId);
	}
	
	public void statementRowSuccessColumnNames(int dbId, int txId, int statementId, String[] columnNames) {
		getRowsetMap(dbId, txId).put(statementId, new RowSet(ImmutableList.copyOf(columnNames)));
	}
	
	public void statementRowSuccess(int dbId, int txId, int statementId, String[] row) {
		getRowsetMap(dbId, txId).get(statementId).addRow(Lists.newArrayList(row));
	}
	
	// THIS MEANS WE'VE REACHED THE END OF A ROW SUCCESSES CALLBACK
	public void statementRowSuccessEnd(int dbId, int txId, int statementId) {
		RowSet rowset = getRowsetMap(dbId, txId).remove(statementId);
		Statement s = _Map.get(dbId).get(txId)._Statements.get(statementId);
		TxRowsCB c = ((StatementWithRowsReturn) s)._Callback;
		if (c != null) { c.onSuccess(rowset); }
	}
	
	public void statementJSONSuccess(int dbId, int txId, int statementId, String json) throws JSONException {
		Statement s = _Map.get(dbId).get(txId)._Statements.get(statementId);
		TxJSONCB c = ((StatementWithJSONReturn) s)._Callback;
		if (c != null) { c.onSuccess(StaticDependencies.getInstance().getJsonDelegate().getJSONArray(json)); }
	}
	
	public void endSuccess(int dbId, int txId) {
		TxRunner c = _Map.get(dbId).remove(txId)._ClosedCallback;
		if (c != null) { c.onComplete(); }
	}
	
	public void endFailure(int dbId, int txId) {
		TxRunner c = _Map.get(dbId).remove(txId)._ClosedCallback;
		if (c != null) { c.onError(); }
	}
}
