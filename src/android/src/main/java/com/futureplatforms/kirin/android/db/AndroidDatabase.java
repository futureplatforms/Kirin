package com.futureplatforms.kirin.android.db;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.futureplatforms.kirin.dependencies.StaticDependencies;
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
import com.google.common.collect.Maps;

public class AndroidDatabase implements DatabaseDelegate {

	private class Helper extends SQLiteOpenHelper {
		public Helper(String name) {
			super(_Context, name, null, 1);
		}

		@Override
		public void onCreate(SQLiteDatabase db) { }

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
	}
	
	private final StaticDependencies deps = StaticDependencies.getInstance();
	private final Map<String, SQLiteDatabase> _DbMap = Maps.newHashMap();
	private final Context _Context;
	
	public AndroidDatabase(Context context) {
		this._Context = context;
	}
	
	@Override
	public void open(String filename, DatabaseOpenedCallback cb) {
		SQLiteDatabase _db = _DbMap.get(_DbMap);
        if (_db == null || !_db.isOpen()) {
            SQLiteOpenHelper helper = new Helper(filename);
            _db = helper.getWritableDatabase();
            _DbMap.put(filename, _db);
        }
        final SQLiteDatabase db = _db;
        
        cb.onOpened(new Database() {
			
			@Override
			protected void performTransaction(TransactionCallback cb) {
				cb.onSuccess(new TransactionBackend() {
					
					@Override
					public void pullTrigger(TransactionBundle bundle) {
						db.beginTransaction();
						
						List<TxElementType> types = bundle._Types;
						int statementCount=0, fileCount=0;
						for (TxElementType type : types) {
							if (type == TxElementType.Statement) {
								Statement st = bundle._Statements.get(statementCount);
								statementCount++;
								
								// Execute the statement
								Cursor cursor = db.rawQuery(st._SQL, st._Params);
								if (st instanceof StatementWithRowsReturn) {
									// rows return
									// construct RowSet from cursor
									ImmutableList<String> columnNames = ImmutableList.copyOf(cursor.getColumnNames());
									RowSet rowset = new RowSet(columnNames);
									int colCount = cursor.getColumnCount();
									while (cursor.moveToNext()) {
										for (int i=0; i<colCount; i++) {
											List<String> values = Lists.newArrayList();
											// Everything has to be a string...
											int entryType = cursor.getType(i);
											switch (entryType) {
												case Cursor.FIELD_TYPE_BLOB: {
													values.add(new String(cursor.getBlob(i)));
												} break;
												
												case Cursor.FIELD_TYPE_FLOAT: {
													values.add(String.valueOf(cursor.getDouble(i)));
												} break;
												
												case Cursor.FIELD_TYPE_INTEGER: {
													values.add(String.valueOf(cursor.getInt(i)));
												} break;
												
												case Cursor.FIELD_TYPE_NULL: {
													values.add(null);
												} break;
												
												case Cursor.FIELD_TYPE_STRING: {
													values.add(cursor.getString(i));
												} break;
											}
											rowset.addRow(values);
										}
									}
									((StatementWithRowsReturn) st)._Callback.onSuccess(rowset);
								} else {
									// token return -- stick it on the dropbox!!
									String token = AndroidDbDropbox.getInstance().putCursor(cursor);
									((StatementWithTokenReturn) st)._Callback.onSuccess(token);
								}
							} else {
								String file = bundle._SqlFiles.get(fileCount);
								fileCount++;
								deps.getLogDelegate().log("SQL FILES not implemented");
							}
						}
						bundle._ClosedCallback.onComplete();
					}
				});
			}
		});
	}

	@Override
	public void close(Database db) {
		// TODO Auto-generated method stub
		
	}

}
