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
    
    public static interface TxRowsCB extends TxCB {
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
    	public final TxRowsCB _Callback;
    	public StatementWithRowsReturn(String sql, String[] params, TxRowsCB cb) {
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
    
    protected Transaction(TransactionBackend backend) {
    	this._Backend = backend;
    }
    
    public void execQueryWithTokenReturn(String sql, TxTokenCB cb) {
    	execQueryWithTokenReturn(sql, null, cb);
    }
    
    public void execQueryWithTokenReturn(String sql, String[] params, TxTokenCB cb) { 
    	_Statements.add(new StatementWithTokenReturn(sql, params, cb));
    	_TxElements.add(TxElementType.Statement); 
    }
    
    public void execUpdate(String sql) {
    	execQueryWithRowsReturn(sql, null);
    }
    
    public void execUpdate(String sql, String[] params) {
    	execQueryWithRowsReturn(sql, params, null);
    }
    
    public void execQueryWithRowsReturn(String sql, TxRowsCB cb) { 
    	execQueryWithRowsReturn(sql, null, cb);
    }
    
    public void execQueryWithRowsReturn(String sql, String[] params, TxRowsCB cb) { 
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
    			_Backend.appendFileToTx(file);
    		} else {
    			final Statement statement = _Statements.get(statementCount);
    			statementCount++;
    			if (statement instanceof StatementWithTokenReturn) {
    				final StatementWithTokenReturn casted = (StatementWithTokenReturn) statement;
    				TxTokenCallback cb = new TxTokenCallback() {
						
						@Override
						public void onSuccess(String token) {
							if (casted._Callback != null) {
								casted._Callback.onSuccess(token);
							}
						}
						
						@Override
						public void onError() {
							if (casted._Callback != null) {
								casted._Callback.onError();
							}
						}
					};
					_Backend.appendStatementToTx(statement._SQL, statement._Params, cb);
    			} else {
    				final StatementWithRowsReturn casted = (StatementWithRowsReturn) statement;
    				TxRowsCallback cb = new TxRowsCallback() {
						
						@Override
						public void onSuccess(List<Map<String, String>> rows) {
							if (casted._Callback != null) {
								casted._Callback.onSuccess(rows);
							}
						}
						
						@Override
						public void onError() {
							if (casted._Callback != null) {
								casted._Callback.onError();
							}
						}
					};
					_Backend.appendStatementToTx(statement._SQL, statement._Params, cb);
    			}
    		}
    	}
    	
    	_Backend.endTransaction(closedCallback);
    }
}
