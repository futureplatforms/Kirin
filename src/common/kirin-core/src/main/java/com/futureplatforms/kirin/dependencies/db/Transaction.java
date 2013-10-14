package com.futureplatforms.kirin.dependencies.db;

import java.util.List;
import com.futureplatforms.kirin.dependencies.internal.DatabaseBackend;
import com.futureplatforms.kirin.dependencies.internal.DatabaseBackend.DatabaseBackendTxCallback;
import com.google.common.collect.Lists;

public class Transaction {
    public static interface Row {
        public enum Type { TypeString, TypeInt }
        
        public Type columnType(int i);
        public int columnInt(int i);
        public String columnString(int i);
    }
    
    public static interface TxCB {}
    
    public static interface TxUniqueReturnCB extends TxCB {
        public void result(Row row);
    }
    
    public static interface TxTokenCB extends TxCB {
    	public void result(String token);
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
    
    public static class StatementWithRowReturn extends Statement {
    	public final TxUniqueReturnCB _Callback;
    	public StatementWithRowReturn(String sql, String[] params, TxUniqueReturnCB cb) {
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
    
    public static void getTransaction(String dbID, DatabaseBackend backend, Mode mode, final TransactionCallback cb) {
    	final Transaction tx = new Transaction(dbID, backend, mode);
    	backend.beginTransaction(dbID, tx._TxID, new DatabaseBackendTxCallback() {
			
			@Override
			public void onSuccess() {
				cb.onSuccess(tx);
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
    
    public void execStatementWithUniqueReturn(String sql, String[] params, TxUniqueReturnCB cb) { 
    	_Statements.add(new StatementWithRowReturn(sql, params, cb));
    	_TxElements.add(TxElementType.Statement); 
    }
    
    public void execSqlFile(String file) { 
    	_Files.add(file); 
    	_TxElements.add(TxElementType.File); 
    }
}
