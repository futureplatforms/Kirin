package com.futureplatforms.kirin.console.db;

import com.futureplatforms.kirin.dependencies.db.Database;
import com.futureplatforms.kirin.dependencies.db.DatabaseDelegate;
import com.futureplatforms.kirin.dependencies.db.Transaction.RowSet;
import com.futureplatforms.kirin.dependencies.db.Transaction.Statement;
import com.futureplatforms.kirin.dependencies.db.Transaction.StatementWithJSONReturn;
import com.futureplatforms.kirin.dependencies.db.Transaction.StatementWithRowsReturn;
import com.futureplatforms.kirin.dependencies.db.Transaction.StatementWithTokenReturn;
import com.futureplatforms.kirin.dependencies.db.Transaction.TxElementType;
import com.futureplatforms.kirin.dependencies.internal.TransactionBackend;
import com.futureplatforms.kirin.dependencies.internal.TransactionBundle;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

public class ConsoleDB implements DatabaseDelegate {

	public interface ResultSetProcessor {
		public void columnNames(List<String> colNames);
		public void nextRow(List<Object> nextRow);
		public void finished();
	}
	
	private static class ConsoleDBResultSetProcessor implements ResultSetProcessor {
		private RowSet _RowSet;
		private final StatementWithRowsReturn _SRows;
		public ConsoleDBResultSetProcessor(StatementWithRowsReturn sRows) {
			_SRows = sRows;
		}
		@Override
		public void columnNames(List<String> colNames) {
			_RowSet = new RowSet(ImmutableList.copyOf(colNames));
		}

		@Override
		public void nextRow(List<Object> nextRow) {
			List<String> strs = Lists.newArrayList();
			for (Object obj : nextRow) {
				if (obj != null) {
					strs.add(obj.toString());
				} else {
					strs.add("<null>");
				}
			}
			_RowSet.addRow(strs);
		}

		@Override
		public void finished() {
			_SRows._Callback.onSuccess(_RowSet);
		}
	}
	
	public static class PrintResultSetProcessor implements ResultSetProcessor {
		private final int _Width;
		public PrintResultSetProcessor(int width) {
			_Width = width;
		}
		
		private String pad(String string) {
			String str = "";
			if (string.length() > _Width) {
				str += string.substring(0, _Width);
			} else {
				str += string;
				for (int i=string.length(); i<_Width; i++) {
					str += " ";
				}
			}
			return str;
		}
		
		@Override
		public void columnNames(List<String> colNames) {
			String str = "";
			for (String string : colNames) {
				str += pad(string);
				str += " | ";
			}
			str += System.getProperty("line.separator");
			for (int i=0; i<colNames.size(); i++) {
				for (int j=0; j<_Width + 3; j++) {
					str += '=';
				}
			}
			System.out.println(str);
		}

		@Override
		public void nextRow(List<Object> nextRow) {
			String str = "";
			for (Object item : nextRow) {
				str += pad(item.toString());
				str += " | ";
			}
			
			System.out.println(str);
		}

		@Override
		public void finished() {
			System.out.println("DONE");
		}
		
	}
	public static ResultSetProcessor _PrintlnResultset = new PrintResultSetProcessor(15);

	public ConsoleDB() {
		try {
			close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void processResultSet(ResultSet resultSet, ResultSetProcessor processor) throws SQLException {
		ResultSetMetaData metaData = resultSet.getMetaData();
		int colCount = metaData.getColumnCount();
		List<String> colNames = Lists.newArrayList();
		for (int i=1; i<colCount+1; i++) {
			colNames.add(metaData.getColumnName(i));
		}
		
		processor.columnNames(colNames);
		while (resultSet.next()) {
			List<Object> nextRow = Lists.newArrayList();
			for (int i=1; i<colCount+1; i++) {
				int type = metaData.getColumnType(i);
				if (type == Types.INTEGER) {
					nextRow.add(resultSet.getInt(i));
				} else if (type == Types.BOOLEAN) {
					nextRow.add(resultSet.getBoolean(i));
				} else if (type == Types.REAL) {
					nextRow.add(resultSet.getDouble(i));
				} else {
					if (type != Types.VARCHAR) {
						System.out.println("Unknown type: " + metaData.getColumnTypeName(i));
					}
					nextRow.add(resultSet.getString(i));
				}
			}
			processor.nextRow(nextRow);
		}
		processor.finished();
	}
	
	public static class ConsoleDatabaseImpl extends Database {
		private Connection _Connection;
		
		public ConsoleDatabaseImpl(Connection c) {
			this._Connection = c;
		}

		public void close() throws SQLException {
			this._Connection.close();
		}

		private PreparedStatement getPreparedStatement(Statement kirinStatement) throws SQLException {
			PreparedStatement pSt = _Connection.prepareStatement(kirinStatement._SQL);
			int count1Based = 1;
			if (kirinStatement._Params != null) {
				for (String param : kirinStatement._Params) {
					pSt.setString(count1Based, param);
					count1Based++;
				}
			}
			return pSt;
		}
		
		private void execUpdate(Statement kirinStatement) throws SQLException {
			PreparedStatement pSt = getPreparedStatement(kirinStatement);
			pSt.executeUpdate();
		}
		
		private ResultSet execQuery(Statement kirinStatement) throws SQLException {
			PreparedStatement pSt = getPreparedStatement(kirinStatement);
			return pSt.executeQuery();
		}
		
		@Override
		protected void performTransaction(final TransactionCallback cb) {
			cb.onSuccess(new TransactionBackend() {
				
				@Override
				public void pullTrigger(TransactionBundle bundle) {
					try {
						int statementCount=0, batchCount=0;
						for (TxElementType type : bundle._Types) {
							if (type == TxElementType.Statement) {
								Statement kirinStatement = bundle._Statements.get(statementCount);
								statementCount++;
								if (kirinStatement instanceof StatementWithTokenReturn) {
									StatementWithTokenReturn sToken = (StatementWithTokenReturn) kirinStatement;
									try {
										if (sToken._Callback != null) {
											ResultSet resultSet = execQuery(kirinStatement);
											String token = ConsoleDBDropbox.getInstance().putResultSet(resultSet);
											sToken._Callback.onSuccess(token);
										} else {
											execUpdate(kirinStatement);
										}
									} catch (Exception e) {
										e.printStackTrace();
										sToken._Callback.onError();
									}
								} else if (kirinStatement instanceof StatementWithJSONReturn) {
									StatementWithJSONReturn sJson = (StatementWithJSONReturn) kirinStatement;
									if (sJson._Callback != null) {
										ResultSet resultSet = execQuery(kirinStatement);
										processResultSet(resultSet, new ConsoleDBJSON(sJson));
									} else {
										execUpdate(kirinStatement);
									}
								} else {
									StatementWithRowsReturn sRows = (StatementWithRowsReturn) kirinStatement;
									try {
										if (sRows._Callback != null) {
											ResultSet resultSet = execQuery(kirinStatement);
											processResultSet(resultSet, new ConsoleDBResultSetProcessor(sRows));
										} else {
											execUpdate(kirinStatement);
										}
									} catch (Exception e) {
										e.printStackTrace();
										sRows._Callback.onError();
									}
								}
							} else {
								String[] batch = bundle._Batches.get(batchCount);
								batchCount++;
								java.sql.Statement sqlStatement = _Connection.createStatement();
								for (String sql : batch) {
									try {
										sqlStatement.executeUpdate(sql);
									} catch (SQLException e) {
										System.out.println(sql);
										e.printStackTrace();
									}
								}
								sqlStatement.close();
							}
						}
						_Connection.commit();
						bundle._ClosedCallback.onComplete();
					} catch (Exception e) {
						e.printStackTrace();
						bundle._ClosedCallback.onError();
					}
				}
			});
		}
		
	}

    private static ConsoleDatabaseImpl _DbImpl;

	@Override
	public void open(String filename, DatabaseOpenedCallback cb) {
        if (_DbImpl == null) {
            try {
                System.out.println("ConsoleDB Opening Connection");
                Class.forName("org.sqlite.JDBC");
                Connection c = DriverManager.getConnection("jdbc:sqlite:" + filename);
                c.setAutoCommit(false);
                _DbImpl = new ConsoleDatabaseImpl(c);
            } catch (Exception e) {
                e.printStackTrace();
                cb.onError();
            }
        }
        cb.onOpened(_DbImpl);
	}

	public static void close() throws SQLException {
		if (_DbImpl != null) {
			_DbImpl.close();
			_DbImpl = null;
		}
	}
}
