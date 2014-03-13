using KirinWindows.Core;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace KirinWindows.Core
{
    enum SQLOperationType
    {
        Rowset, Token, JSON, Batch
    }

    class TXStatement
    {
        public SQLOperationType _Type { get; set; }
        public int _StatementId { get; set; }
        public string _Statement { get; set; }
        public string[] _Parameters { get; set; }
        public bool _HasId { get; set; }
    }

    class KirinMapping : SQLite.TableMapping
    {
        public Dictionary<string, Column> _Columns = new Dictionary<string, Column>();
        public class KirinColumn : Column
        {
            // the first object is a particular row, the second object is that row's value in this column
            public Dictionary<object, object> _TheDict = new Dictionary<object, object>();
            public KirinColumn(string name)
                : base(name)
            {
            }

            public override void SetValue(object obj, object val)
            {
                _TheDict.Add(obj, val);
            }

            public override object GetValue(object obj)
            {
                return base.GetValue(obj);
            }
        }

        public KirinMapping()
            : base(typeof(object))
        {
        }

        public override Column FindColumn(string columnName)
        {
            if (!_Columns.ContainsKey(columnName))
            {
                _Columns.Add(columnName, new KirinColumn(columnName));
            }
            return _Columns[columnName];
        }
    }

    class TXService : KirinExtension, Generated.TransactionServiceNative
    {
        private Generated.TransactionService _KirinService;
        private DBService _DBService;

        private Dictionary<int, Dictionary<int, List<TXStatement>>> _DBtoTX = new Dictionary<int, Dictionary<int, List<TXStatement>>>();

        public TXService(DBService dbService, Kirin k) : base("TransactionService", k)
        {
            this._KirinService = new Generated.TransactionService(KirinAssistant);
            this._DBService = dbService;
        }

        private List<TXStatement> GetStatements(int dbId, int txId)
        {
            if (!_DBtoTX.ContainsKey(dbId))
            {
                _DBtoTX[dbId] = new Dictionary<int, List<TXStatement>>();
            }
            if (!_DBtoTX[dbId].ContainsKey(txId))
            {
                _DBtoTX[dbId][txId] = new List<TXStatement>();
            }
            return _DBtoTX[dbId][txId];
        }

        public void begin(int dbId, int txId)
        {
            this._KirinService.transactionBeginOnSuccess(dbId, txId);
        }

        public void appendStatementForRows(int dbId, int txId, int statementId, string statement, string[] txParams)
        {
            TXStatement s = new TXStatement()
            {
                _HasId = true,
                _Parameters = txParams,
                _Statement = statement,
                _StatementId = statementId,
                _Type = SQLOperationType.Rowset
            };
            GetStatements(dbId, txId).Add(s);
        }

        public void appendStatementForToken(int dbId, int txId, int statementId, string statement, string[] txParams)
        {
            TXStatement s = new TXStatement()
            {
                _HasId = true,
                _Parameters = txParams,
                _Statement = statement,
                _StatementId = statementId,
                _Type = SQLOperationType.Token
            };
            GetStatements(dbId, txId).Add(s);
        }

        public void appendStatementForJSON(int dbId, int txId, int statementId, string statement, string[] txParams)
        {
            TXStatement s = new TXStatement()
            {
                _HasId = true,
                _Parameters = txParams,
                _Statement = statement,
                _StatementId = statementId,
                _Type = SQLOperationType.JSON
            };
            GetStatements(dbId, txId).Add(s);
        }

        public void appendBatch(int dbId, int txId, string[] batch)
        {
            foreach (string b in batch)
            {
                TXStatement s = new TXStatement()
                {
                    _HasId = false,
                    _Parameters = null,
                    _Statement = b,
                    _Type = SQLOperationType.Batch
                };
                GetStatements(dbId, txId).Add(s);
            }
        }

        public void end(int dbId, int txId)
        {
            new Thread(() =>
            {
                var db = _DBService._Connections[dbId];
                Debug.WriteLine("about to begin transaction");
                db.BeginTransaction();
                Debug.WriteLine("begun transaction");
                var statements = GetStatements(dbId, txId);
                foreach (var statement in statements)
                {
                    //db.Query(new SQLite.TableMapping(
                    Debug.WriteLine("about to do query: " + statement._Statement);
                    var parameters = statement._Parameters == null ? new string[0] : statement._Parameters;
                    var mapping = new KirinMapping();
                    var objects = db.Query(mapping, statement._Statement, parameters);
                    var cols = mapping._Columns;
                    if (statement._HasId)
                    {
                        switch (statement._Type)
                        {
                            case SQLOperationType.Batch:
                                break;

                            case SQLOperationType.JSON:
                                break;

                            case SQLOperationType.Rowset:
                                break;

                        }
                    }
                    Debug.WriteLine("done query, returned " + objects.Count + " rows");
                    foreach (var obj in objects)
                    {
                        foreach (var entry in cols)
                        {
                            var colName = entry.Key;
                            var col = entry.Value as KirinWindows.Core.KirinMapping.KirinColumn;
                            if (col._TheDict.ContainsKey(obj))
                            {
                                var val = col._TheDict[obj];
                                Debug.WriteLine(colName + ": " + val);
                            }
                        }
                    }
                }
                Debug.WriteLine("about to commit");
                db.Commit();
            }).Start();
        }
    }
}
