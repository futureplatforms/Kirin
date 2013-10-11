package com.futureplatforms.kirin.gwt.client.services.db;

import java.util.List;
import java.util.Map;

import com.futureplatforms.kirin.dependencies.db.Transaction;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class TransactionImpl implements Transaction {
    
    public static interface TransactionCBSuccess {
        public void execute(String s);
    }
    public static interface TransactionCBFail {
        public void execute();
    }
    
    private static class TransactionCB {
        public final TransactionCBSuccess _Success;
        public final TransactionCBFail _Fail;
        public TransactionCB(TransactionCBSuccess success, TransactionCBFail fail) {
            this._Success = success; this._Fail = fail;
        }
    }
    
    private static Map<String, TransactionCB> _Callbacks = Maps.newHashMap();
    
    private List<Statement> _TxLog = Lists.newArrayList();
    
    private static int TX_COUNT = 0;
    
    public final String _Id;
    private boolean _IsStale;

    public boolean _ReadOnly;
    
    public TransactionImpl(DatabasePrototype db) {
        this._Id = db._Filename + TX_COUNT;
        TX_COUNT++;
        _IsStale = false;
    }
    
    private void addToTxLog(Statement st) {
        
    }

    @Override
    public StatementBuilder execStatement(Statement statement) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public StatementBuilder execStatement(Statement statement, TxCB callback) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public StatementBuilder execStatementWithUniqueReturn(Statement statement,
            TxCB callback) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void execSqlFile(String file) {
        // TODO Auto-generated method stub
        
    }
}
