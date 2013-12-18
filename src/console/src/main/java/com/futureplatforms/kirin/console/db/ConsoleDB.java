package com.futureplatforms.kirin.console.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import com.futureplatforms.kirin.dependencies.db.Database;
import com.futureplatforms.kirin.dependencies.db.DatabaseDelegate;
import com.futureplatforms.kirin.dependencies.db.Transaction.RowSet;
import com.futureplatforms.kirin.dependencies.db.Transaction.Statement;
import com.futureplatforms.kirin.dependencies.db.Transaction.StatementWithRowsReturn;
import com.futureplatforms.kirin.dependencies.db.Transaction.StatementWithTokenReturn;
import com.futureplatforms.kirin.dependencies.db.Transaction.TxElementType;
import com.futureplatforms.kirin.dependencies.internal.TransactionBackend;
import com.futureplatforms.kirin.dependencies.internal.TransactionBundle;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class ConsoleDB implements DatabaseDelegate {

	public static class ConsoleDatabaseImpl extends Database {
		private Connection _Connection;
		
		public ConsoleDatabaseImpl(Connection c) {
			this._Connection = c;
		}
		
		private ResultSet getResultSet(Statement kirinStatement) throws SQLException {
			PreparedStatement pSt = _Connection.prepareStatement(kirinStatement._SQL);
			int count1Based = 1;
			for (String param : kirinStatement._Params) {
				pSt.setString(count1Based, param);
				count1Based++;
			}
			
			pSt.executeQuery();
			return pSt.getResultSet();
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
										ResultSet resultSet = getResultSet(kirinStatement);
										String token = ConsoleDBDropbox.getInstance().putResultSet(resultSet);
										sToken._Callback.onSuccess(token);
									} catch (Exception e) {
										e.printStackTrace();
										sToken._Callback.onError();
									}
								} else {
									StatementWithRowsReturn sRows = (StatementWithRowsReturn) kirinStatement;
									try {
										ResultSet resultSet = getResultSet(kirinStatement);
										ResultSetMetaData metaData = resultSet.getMetaData();
										int colCount = metaData.getColumnCount();
										List<String> colNames = Lists.newArrayList();
										for (int i=0; i<colCount; i++) {
											colNames.add(metaData.getColumnName(i));
										}
										RowSet rowset = new RowSet(ImmutableList.copyOf(colNames));
										while (resultSet.next()) {
											List<String> nextRow = Lists.newArrayList();
											for (int i=0; i<colCount; i++) {
												nextRow.add(resultSet.getString(i));
											}
											rowset.addRow(nextRow);
										}
										sRows._Callback.onSuccess(rowset);
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
									sqlStatement.executeUpdate(sql);
								}
								sqlStatement.close();
							}
						}
						_Connection.commit();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		
	}
	
	@Override
	public void open(String filename, DatabaseOpenedCallback cb) {
		try {
			Class.forName("org.sqlite.JDBC");
			Connection c = DriverManager.getConnection("jdbc:sqlite:" + filename);
			c.setAutoCommit(false);
			cb.onOpened(new ConsoleDatabaseImpl(c));
		} catch (Exception e) {
			e.printStackTrace();
			cb.onError();
		}
	}

}
