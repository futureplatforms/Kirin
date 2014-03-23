package com.futureplatforms.kirin.dependencies.db;

import java.util.List;

import com.futureplatforms.kirin.dependencies.StaticDependencies.LogDelegate;
import com.futureplatforms.kirin.dependencies.db.Database.TxRunner;
import com.futureplatforms.kirin.dependencies.internal.TransactionBackend;
import com.futureplatforms.kirin.dependencies.internal.TransactionBundle;
import com.futureplatforms.kirin.dependencies.json.JSONArray;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class Transaction {
	public static class RowSet {
		public class Row {
			public final List<String> _Values;
			public Row(List<String> values) {
				this._Values = values;
			}
			public String valueForColumn(String column) {
				if (!_ColumnNames.contains(column)) {
					throw new IllegalArgumentException("No such column: " + column);
				}
				return this._Values.get(_ColumnNames.indexOf(column));
			}
		}
		public final ImmutableList<String> _ColumnNames;
		public final List<Row> _Rows = Lists.newArrayList();
		
		public RowSet(ImmutableList<String> columnNames) {
			_ColumnNames = columnNames;
		}
		public void addRow(List<String> values) {
			_Rows.add(new Row(values));
		}
		
		private static String str(String str, int colWidth, char pad) {
			str = (str == null) ? "<null>" : str;
			String s = str;
			if (str.length() > colWidth) {
				s = s.substring(0, colWidth);
			} else {
				for (int i=str.length(); i<colWidth; i++) {
					s += pad;
				}
			}
			return s;
		}
		
		public void log(LogDelegate log) {
			String header = "";
			for (String colName : _ColumnNames) {
				header += str(colName, 15, ' ') + " | ";
			}
			log.log(header);
			log.log(str("", 15 * _ColumnNames.size(), '='));
			
			for (Row row : _Rows) {
				String rowStr = "";
				for (String value : row._Values) {
					rowStr += str(value, 15, ' ') + " | ";
				}
				log.log(rowStr);
			}
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

    public static interface TxJSONCB extends TxCB {
    	public void onSuccess(JSONArray json);
    }
    
    public static abstract class Statement {
        public final String _SQL;
        public final String[] _Params;
        public Statement(String sql, String[] params) {
            this._SQL = sql;
            this._Params = params;
        }
    }
    /**
     * Insert Statement, that allows nulls in the params
     * @author kassimmaguire
     *
     */
    public static class InsertStatement extends StatementWithRowsReturn{
		public InsertStatement(String sql, String[] params) {
			super(sql, params, null);
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

    public static class StatementWithJSONReturn extends Statement {
    	public final TxJSONCB _Callback;
    	public StatementWithJSONReturn(String sql, String[] params, TxJSONCB cb) {
    		super(sql, params);
    		this._Callback = cb;
    	}
    }
    
    public enum TxElementType {
    	Statement, Batch
    }

    private List<TxElementType> _TxElements = Lists.newArrayList();
    private List<Statement> _Statements = Lists.newArrayList();
    private List<String[]> _BatchQueries = Lists.newArrayList();
    
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
    
    private void validateParams(String[] params) {
    	if (params != null) {
	    	for (int i=0; i<params.length; i++) {
				if (params[i] == null) { 
					throw new NullPointerException("param " + i + " must not be null!"); 
				} 
			}
    	}
    }
    
    public void execQueryWithTokenReturn(String sql, String[] params, TxTokenCB cb) { 
    	validateParams(params);
    	_Statements.add(new StatementWithTokenReturn(sql, params, cb));
    	_TxElements.add(TxElementType.Statement); 
    }

    public void execInsert(String sql) {
    	execInsert(sql, null);
    }
    public void execInsert(String sql,String[] params) {
    	_Statements.add(new InsertStatement(sql, params));
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
    	validateParams(params);
    	_Statements.add(new StatementWithRowsReturn(sql, params, cb));
    	_TxElements.add(TxElementType.Statement); 
    }
 
    public void execQueryWithJSONReturn(String sql, TxJSONCB cb) {
    	execQueryWithJSONReturn(sql, null, cb);
    }
    
    public void execQueryWithJSONReturn(String sql, String[] params, TxJSONCB cb) {
    	validateParams(params);
    	_Statements.add(new StatementWithJSONReturn(sql, params, cb));
    	_TxElements.add(TxElementType.Statement);
    }
    /**
     * IF you're executing a SQL file then use this function.  Statements must be separated
     * with semicolon followed by newline!
     * @param batch
     */
    public void execBatchUpdate(String batch) { 
    	// Stick a newline on the end in case we forgot
    	batch += "\n";
    	String[] lines = batch.split(";(\\s)*[\n\r]");
    	_BatchQueries.add(lines); 
    	_TxElements.add(TxElementType.Batch); 
    }
    
    protected void pullTrigger(TxRunner closedCallback) {
    	// Package all the queries the user wants to do in this statement
    	// into a bundle
    	TransactionBundle bundle = new TransactionBundle(_TxElements, _Statements, _BatchQueries, closedCallback);
    	
    	// Now pass it to the native platform's flavour of pulling trigger
    	_Backend.pullTrigger(bundle);
    }
}
