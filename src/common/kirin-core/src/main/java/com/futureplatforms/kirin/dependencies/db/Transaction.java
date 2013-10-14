package com.futureplatforms.kirin.dependencies.db;

import java.util.List;
import java.util.Map;

import com.futureplatforms.kirin.dependencies.internal.DatabaseBackend;
import com.futureplatforms.kirin.dependencies.internal.DatabaseBackend.DatabaseBackendTxRowsCallback;
import com.futureplatforms.kirin.dependencies.internal.DatabaseBackend.DatabaseBackendTxTokenCallback;
import com.futureplatforms.kirin.dependencies.internal.DatabaseBackend.DatabaseClosedCallback;
import com.google.common.collect.Lists;

public class Transaction {
    public static interface TxCB {
    	public void onError();
    }
    
    public static interface TxReturnCB extends TxCB {
        public void onSuccess(List<Map<String, String>> rowset);
    }
    
    public static interface TxTokenCB extends TxCB {
    	public void onSuccess(String token);
    }
    
    public static abstract class Statement {
        public final String _SQL;
        public final String[] _Params;
        public Statement(String sql, String[] params) {
            this._SQL = sql;
            this._Params = params;
        }
    }
    
    public static class StatementWithTokenReturn extends Statement {
    	public final TxTokenCB _Callback;
    	public StatementWithTokenReturn(String sql, String[]params, TxTokenCB callback) {
    		super(sql, params);
    		this._Callback = callback;
    	}
    }
    
    public static class StatementWithRowsReturn extends Statement {
    	public final TxReturnCB _Callback;
    	public StatementWithRowsReturn(String sql, String[] params, TxReturnCB cb) {
    		super(sql, params);
    		this._Callback = cb;
    	}
    }
    
    private enum TxElementType {
    	Statement, File
    }

    private List<TxElementType> _TxElements = Lists.newArrayList();
    
    private List<Statement> _Statements = Lists.newArrayList();
    private List<String> _Files = Lists.newArrayList();
    
    public enum Mode {
    	ReadOnly, ReadWrite
    }
    private DatabaseBackend _Backend;
    private Mode _Mode;
    private static int _TxCount = 0;
    public final String _TxID, _DbID;
    
    public static interface TransactionCallback {
    	public void onSuccess(Transaction t);
    	public void onError();
    }
    
    protected static void getTransaction(String dbID, DatabaseBackend backend, Mode mode, final TransactionCallback cb) {
    	final Transaction tx = new Transaction(dbID, backend, mode);
    	backend.beginTransaction(dbID, tx._TxID, new DatabaseBackendTxRowsCallback() {

			@Override
			public void onSuccess(List<Map<String, String>> rows) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onError() {
				cb.onError();
			}
			
		});
    }
    
    private Transaction(String dbID, DatabaseBackend backend, Mode mode) {
    	this._Backend = backend;
    	this._Mode = mode;
    	this._DbID = dbID;
    	this._TxID = dbID + _TxCount;
    	_TxCount++;
    }
    
    public void execStatement(String sql, String[] params, TxTokenCB cb) { 
    	_Statements.add(new StatementWithTokenReturn(sql, params, cb));
    	_TxElements.add(TxElementType.Statement); 
    }
    
    public void execStatementWithUniqueReturn(String sql, String[] params, TxReturnCB cb) { 
    	_Statements.add(new StatementWithRowsReturn(sql, params, cb));
    	_TxElements.add(TxElementType.Statement); 
    }
    
    public void execSqlFile(String file) { 
    	_Files.add(file); 
    	_TxElements.add(TxElementType.File); 
    }
    
    protected void done(DatabaseClosedCallback closedCallback) {
    	int fileCount = 0, statementCount = 0;
    	
    	// run through each transaction element and register it with native
    	for (TxElementType type : _TxElements) {
    		if (type == TxElementType.File) {
    			String file = _Files.get(fileCount);
    			fileCount++;
    			_Backend.appendFileToTransaction(_DbID, _TxID, file);
    		} else {
    			final Statement statement = _Statements.get(statementCount);
    			statementCount++;
    			if (statement instanceof StatementWithTokenReturn) {
    				final StatementWithTokenReturn casted = (StatementWithTokenReturn) statement;
    				DatabaseBackendTxTokenCallback cb = new DatabaseBackendTxTokenCallback() {
						
						@Override
						public void onSuccess(String token) {
							casted._Callback.onSuccess(token);
						}
						
						@Override
						public void onError() {
							casted._Callback.onError();
						}
					};
					_Backend.appendStatementToTransaction(_DbID, _TxID, statement._SQL, statement._Params, cb);
    			} else {
    				final StatementWithRowsReturn casted = (StatementWithRowsReturn) statement;
    				DatabaseBackendTxRowsCallback cb = new DatabaseBackendTxRowsCallback() {
						
						@Override
						public void onSuccess(List<Map<String, String>> rows) {
							casted._Callback.onSuccess(rows);
						}
						
						@Override
						public void onError() {
							casted._Callback.onError();
						}
					};
					_Backend.appendStatementToTransaction(_DbID, _TxID, statement._SQL, statement._Params, cb);
    			}
    		}
    	}
    	
    	_Backend.endTransaction(_DbID, closedCallback);
    }
}
