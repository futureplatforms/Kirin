package com.futureplatforms.kirin.dependencies.db;

import java.util.Map;

import com.google.common.collect.Maps;

public interface Transaction {
    public static interface Row {
        public enum Type { TypeString, TypeInt }
        
        public Type columnType(int i);
        public int columnInt(int i);
        public String columnString(int i);
    }
    public static interface TxCB {
        public void result(Row row);
    }
    
    public static class Statement {
        public enum Types {
            Rowset, Row, Array, File
        }
        public final Types _Type;
        public final String _SQL;
        public Statement(Types type, String sql) {
            this._Type = type;
            this._SQL = sql;
        }
    }
    
    public static class StatementBuilder {
        public final Statement _Statement;
        private final int numQueries;
        private final Map<Integer, Integer> intParams = Maps.newHashMap();
        private final Map<Integer, String> stringParams = Maps.newHashMap();
        public StatementBuilder(Statement statement) {
            this._Statement = statement;
            int index = 0;
            // Count number of ?s in the statement's SQL
            int queries = 0;
            while ((index = _Statement._SQL.indexOf('?', index)) != -1) {
                queries++;
            }
            numQueries = queries;
        }
        
        public StatementBuilder bind(int index, int value) {
            intParams.put(index, value);
            return this;
        }
        public StatementBuilder bind(int index, String value) {
            stringParams.put(index, value);
            return this;
        }
    }
    public StatementBuilder execStatement(Statement statement);
    public StatementBuilder execStatement(Statement statement, TxCB callback);
    public StatementBuilder execStatementWithUniqueReturn(Statement statement, TxCB callback);
    public void execSqlFile(String file);
}
