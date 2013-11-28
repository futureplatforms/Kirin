package com.futureplatforms.kirin.dependencies.internal;

import java.util.List;

import com.futureplatforms.kirin.dependencies.db.Database.TxRunner;
import com.futureplatforms.kirin.dependencies.db.Transaction.Statement;
import com.futureplatforms.kirin.dependencies.db.Transaction.TxElementType;

public class TransactionBundle {

	public final List<TxElementType> _Types;
	public final List<Statement> _Statements;
	public final List<String[]> _Batches;
	public final TxRunner _ClosedCallback;
	
	public TransactionBundle(
			List<TxElementType> types,
			List<Statement> statements, 
			List<String[]> batches, 
			TxRunner closedCallback) {
		this._Types = types;
		this._Statements = statements;
		this._Batches = batches;
		this._ClosedCallback = closedCallback;
	}

}
