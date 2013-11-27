package com.futureplatforms.kirin.gwt.client.services.db;

import java.util.Map;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.NoExport;

import com.futureplatforms.kirin.dependencies.db.Database.TxRunner;
import com.futureplatforms.kirin.dependencies.db.Transaction.RowSet;
import com.futureplatforms.kirin.dependencies.db.Transaction.Statement;
import com.futureplatforms.kirin.dependencies.db.Transaction.StatementWithRowsReturn;
import com.futureplatforms.kirin.dependencies.db.Transaction.StatementWithTokenReturn;
import com.futureplatforms.kirin.dependencies.db.Transaction.TxElementType;
import com.futureplatforms.kirin.dependencies.db.Transaction.TxRowsCB;
import com.futureplatforms.kirin.dependencies.db.Transaction.TxTokenCB;
import com.futureplatforms.kirin.dependencies.internal.TransactionBundle;
import com.futureplatforms.kirin.gwt.client.KirinService;
import com.futureplatforms.kirin.gwt.client.services.db.natives.TransactionServiceNative;
import com.futureplatforms.kirin.gwt.compile.NoBind;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gwt.core.client.GWT;

@Export(value = "TransactionService", all = true)
@ExportPackage("screens")
public class TransactionService extends KirinService<TransactionServiceNative>{
	private static TransactionService _Instance;
    
    @NoBind
    @NoExport
    public static TransactionService BACKDOOR() { return _Instance; }
    
	public TransactionService() {
		super(GWT.<TransactionServiceNative>create(TransactionServiceNative.class));
	}
	
	private Map<Integer, Map<Integer, TransactionBundle>> _Map = Maps.newHashMap();

	private void map(TransactionBundle bundle, int dbId, int txId) {
		if (!_Map.containsKey(dbId)) {
			_Map.put(dbId, Maps.<Integer, TransactionBundle>newHashMap());
		}
		Map<Integer, TransactionBundle> txMap = _Map.get(dbId);
		txMap.put(txId, bundle);
	}
	
	@NoBind
	@NoExport
	// THIS GETS INVOKED FROM THE BACKDOOR!
	public void pullTrigger(
			TransactionBundle bundle, 
			int dbId, int txId) {
		map(bundle, dbId, txId);
		
		int fileCount = 0, statementCount = 0;
    	
    	// run through each transaction element and register it with native
    	for (TxElementType type : bundle._Types) {
    		if (type == TxElementType.File) {
    			String filename = bundle._SqlFiles.get(fileCount);
    			fileCount++;
    			getNativeObject().appendFileToTx(dbId, txId, filename);
    		} else {
    			final Statement statement = bundle._Statements.get(statementCount);
    			if (statement instanceof StatementWithTokenReturn) {
					getNativeObject().appendStatementToTxForToken(dbId, txId, statementCount, statement._SQL, statement._Params);
    			} else {
					getNativeObject().appendStatementToTxForRows(dbId, txId, statementCount, statement._SQL, statement._Params);
    			}
    			statementCount++;
    		}
    	}
    	
    	getNativeObject().end(dbId, txId);
	}
	
	// CALL THIS IF A STATEMENT FAILED
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
		((StatementWithTokenReturn) s)._Callback.onSuccess(token);
	}
	
	
	// BECAUSE WE CAN'T PASS ARRAYS OF ARRAYS, ROW SUCCESSES NEED TO BE PASSED ROW BY ROW
	// SORRY
	private Map<Integer, Map<Integer, Map<Integer, RowSet>>> _RowCallback;
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
		getRowsetMap(dbId, txId).put(statementId, new RowSet(Lists.newArrayList(columnNames)));
	}
	
	public void statementRowSuccess(int dbId, int txId, int statementId, String[] row) {
		getRowsetMap(dbId, txId).get(statementId)._RowValues.add(Lists.newArrayList(row));
	}
	
	// THIS MEANS WE'VE REACHED THE END OF A ROW SUCCESSES CALLBACK
	public void statementRowSuccessEnd(int dbId, int txId, int statementId) {
		RowSet rowset = getRowsetMap(dbId, txId).remove(statementId);
		Statement s = _Map.get(dbId).get(txId)._Statements.get(statementId);
		TxRowsCB c = ((StatementWithRowsReturn) s)._Callback;
		if (c != null) { c.onSuccess(rowset); }
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
