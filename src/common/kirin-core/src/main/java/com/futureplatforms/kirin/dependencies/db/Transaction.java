package com.futureplatforms.kirin.dependencies.db;

import java.util.List;

import com.futureplatforms.kirin.dependencies.db.Database.TxRunner;
import com.futureplatforms.kirin.dependencies.internal.TransactionBackend2;
import com.futureplatforms.kirin.dependencies.internal.TransactionBundle;
import com.google.common.collect.Lists;

public class Transaction {
	public static class RowSet {
		public final List<String> _ColumnNames;
		public final List<List<String>> _RowValues = Lists.newArrayList();
		public RowSet(List<String> columnNames) {
			_ColumnNames = columnNames;
		}
	}
	
    public static interface TxCB {
    	public void onError();
    }
    
    public static interface TxRowsCB extends TxCB {
        public void onSuccess(RowSet rowset);
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
    
    public enum TxElementType {
    	Statement, File
    }

    private List<TxElementType> _TxElements = Lists.newArrayList();
    private List<Statement> _Statements = Lists.newArrayList();
    private List<String> _SqlFiles = Lists.newArrayList();
    
    public enum Mode {
    	ReadOnly, ReadWrite
    }
    private TransactionBackend2 _Backend;
    
    public static interface TransactionCallback {
    	public void onSuccess(Transaction t);
    	public void onError();
    }
    
    protected Transaction(TransactionBackend2 backend) {
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
    	_SqlFiles.add(file); 
    	_TxElements.add(TxElementType.File); 
    }
    
    protected void pullTrigger(TxRunner closedCallback) {
    	_Backend.pullTrigger(new TransactionBundle(_TxElements, _Statements, _SqlFiles, closedCallback));
    }
}
