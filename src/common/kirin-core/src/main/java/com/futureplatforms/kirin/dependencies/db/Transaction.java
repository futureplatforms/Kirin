package com.futureplatforms.kirin.dependencies.db;

import java.util.List;
import java.util.Map;

import com.futureplatforms.kirin.dependencies.internal.TransactionBackend;
import com.futureplatforms.kirin.dependencies.internal.TransactionBackend.TxClosedCallback;
import com.futureplatforms.kirin.dependencies.internal.TransactionBackend.TxRowsCallback;
import com.futureplatforms.kirin.dependencies.internal.TransactionBackend.TxTokenCallback;
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
    private TransactionBackend _Backend;
    
    public static interface TransactionCallback {
    	public void onSuccess(Transaction t);
    	public void onError();
    }
    
    /*
    protected static void getTransaction(String dbID, DatabaseAccessorBackend backend, Mode mode, final TransactionCallback cb) {
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
    }*/
    
    protected Transaction(TransactionBackend backend) {
    	this._Backend = backend;
    }
    
    public void execStatementWithTokenReturn(String sql, String[] params, TxTokenCB cb) { 
    	_Statements.add(new StatementWithTokenReturn(sql, params, cb));
    	_TxElements.add(TxElementType.Statement); 
    }
    
    public void execStatementWithRowsReturn(String sql, String[] params, TxReturnCB cb) { 
    	_Statements.add(new StatementWithRowsReturn(sql, params, cb));
    	_TxElements.add(TxElementType.Statement); 
    }
    
    public void execSqlFile(String file) { 
    	_Files.add(file); 
    	_TxElements.add(TxElementType.File); 
    }
    
    protected void pullTrigger(TxClosedCallback closedCallback) {
    	int fileCount = 0, statementCount = 0;
    	
    	// run through each transaction element and register it with native
    	for (TxElementType type : _TxElements) {
    		if (type == TxElementType.File) {
    			String file = _Files.get(fileCount);
    			fileCount++;
    			_Backend.appendFileToTransaction(file);
    		} else {
    			final Statement statement = _Statements.get(statementCount);
    			statementCount++;
    			if (statement instanceof StatementWithTokenReturn) {
    				final StatementWithTokenReturn casted = (StatementWithTokenReturn) statement;
    				TxTokenCallback cb = new TxTokenCallback() {
						
						@Override
						public void onSuccess(String token) {
							casted._Callback.onSuccess(token);
						}
						
						@Override
						public void onError() {
							casted._Callback.onError();
						}
					};
					_Backend.appendStatementToTransaction(statement._SQL, statement._Params, cb);
    			} else {
    				final StatementWithRowsReturn casted = (StatementWithRowsReturn) statement;
    				TxRowsCallback cb = new TxRowsCallback() {
						
						@Override
						public void onSuccess(List<Map<String, String>> rows) {
							casted._Callback.onSuccess(rows);
						}
						
						@Override
						public void onError() {
							casted._Callback.onError();
						}
					};
					_Backend.appendStatementToTransaction(statement._SQL, statement._Params, cb);
    			}
    		}
    	}
    	
    	_Backend.endTransaction(closedCallback);
    }
}
