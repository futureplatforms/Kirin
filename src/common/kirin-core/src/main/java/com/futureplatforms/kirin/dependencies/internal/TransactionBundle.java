package com.futureplatforms.kirin.dependencies.internal;

import java.util.List;

import com.futureplatforms.kirin.dependencies.db.Database.TxRunner;
import com.futureplatforms.kirin.dependencies.db.Transaction.Statement;
import com.futureplatforms.kirin.dependencies.db.Transaction.TxElementType;

public class TransactionBundle {

	public final List<TxElementType> _Types;
	public final List<Statement> _Statements;
	public final List<String> _SqlFiles;
	public final TxRunner _ClosedCallback;
	
	public TransactionBundle(
			List<TxElementType> types,
			List<Statement> statements, 
			List<String> sqlFiles, 
			TxRunner closedCallback) {
		this._Types = types;
		this._Statements = statements;
		this._SqlFiles = sqlFiles;
		this._ClosedCallback = closedCallback;
	}

}
