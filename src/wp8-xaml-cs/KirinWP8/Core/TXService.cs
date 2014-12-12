using KirinWindows.Core;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

using Newtonsoft.Json;
using System.IO;

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


        public void appendStatements(int dbId, int txId, int[] returnTypes, int[] statementIds, string[] statements, string[] txParams)
        {
            for (int i = 0; i < returnTypes.Length; i++)
            {
                string[] deserParams = JsonConvert.DeserializeObject<string[]>(txParams[i]);
                int returnType = returnTypes[i];
                switch (returnType)
                {
                    case 0: // Rows
                        appendStatementForRows(dbId, txId, statementIds[i], statements[i], deserParams);
                        break;

                    case 1: // Token
                        appendStatementForToken(dbId, txId, statementIds[i], statements[i], deserParams);
                        break;

                    case 2: // JSON
                        appendStatementForJSON(dbId, txId, statementIds[i], statements[i], deserParams);
                        break;

                    case 3: // Batch
                        TXStatement s = new TXStatement()
                        {
                            _HasId = false,
                            _Parameters = null,
                            _Statement = statements[i],
                            _Type = SQLOperationType.Batch
                        };
                        GetStatements(dbId, txId).Add(s);
                        break;
                }
            }
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

        public class KirinQueryReturn
        {
            public List<string> _ColumnNames { get; private set; }
            public List<Dictionary<string, string>> _Rows { get; private set; }

            public KirinQueryReturn(List<object> objects, Dictionary<string, SQLite.TableMapping.Column> cols)
            {
                Debug.WriteLine("done query, returned " + objects.Count + " rows");
                _ColumnNames = new List<string>();
                _Rows = new List<Dictionary<string, string>>();

                // Firstly iterate through the columns to retrieve column names
                foreach (var entry in cols)
                {
                    _ColumnNames.Add(entry.Key);
                }

                // NOW THEN.
                // Each object in "objects" represents a row in the returned table.
                // Each column's dictionary is keyed by this object, and maps to the column's value in that row
                // in that row.
                //
                // EXAMPLE:
                //         _id    |   name   |  size  | colour
                //      =========================================
                // o1       aaa   |    foo   |   S    |   red
                // o2       bbb   |    bar   |   M    |   yellow
                // o3       ccc   |    bat   |   L    |   green
                //
                //  objects:  [o1, o2, o3]
                //  cols:     [ 
                //                 { "_id": { o1: "aaa", o2: "bbb", o3: "ccc" } }, 
                //                 { "name": { o1: "foo", o2: "bar", o3: "bat" } }, 
                //                 { "size": { o1: "S", o2: "M", o3: "L" } }, 
                //                 { "colour", { o1: "red", o2: "yellow", o3: "green"} }
                //            ]
                foreach (var obj in objects)
                {
                    var dict = new Dictionary<string, string>();
                    _Rows.Add(dict);

                    foreach (var entry in cols)
                    {
                        var colName = entry.Key;
                        var col = entry.Value as KirinWindows.Core.KirinMapping.KirinColumn;
                        if (col._TheDict.ContainsKey(obj))
                        {
                            var val = col._TheDict[obj];
                            dict.Add(colName, (string)val);
                        }
                    }
                }
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
                    Debug.WriteLine("about to do query: " + statement._Statement);
                    var parameters = statement._Parameters == null ? new string[0] : statement._Parameters;
                    var mapping = new KirinMapping();
                    var objects = db.Query(mapping, statement._Statement, parameters);
                    var cols = mapping._Columns;
                    var ret = new KirinQueryReturn(objects, cols);
                    if (statement._HasId)
                    {
                        switch (statement._Type)
                        {
                            case SQLOperationType.Batch:
                                {
                                    // Yeah we're not expecting any batch ones here...
                                } break;

                            case SQLOperationType.JSON:
                                {
                                    // todo implement me
                                } break;

                            case SQLOperationType.Rowset:
                                {
                                    _KirinService.statementRowSuccessColumnNames(dbId, txId, statement._StatementId, ret._ColumnNames.ToArray());
                                    foreach (var row in ret._Rows)
                                    {
                                        var vals = row.Values.ToArray();
                                        _KirinService.statementRowSuccess(dbId, txId, statement._StatementId, vals);
                                    }
                                    _KirinService.statementRowSuccessEnd(dbId, txId, statement._StatementId);
                                } break;

                            case SQLOperationType.Token:
                                {
                                    var token = KirinDropbox.DepositObject(ret._Rows);
                                    _KirinService.statementTokenSuccess(dbId, txId, statement._StatementId, token);
                                } break;
                        }
                    }
                    
                }
                Debug.WriteLine("about to commit");
                db.Commit();
                _KirinService.endSuccess(dbId, txId);
            }).Start();
        }
    }
}
