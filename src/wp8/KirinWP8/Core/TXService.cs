using KirinWindows.Core;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace KirinWP8.Core
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

    class Mapping : SQLite.TableMapping
    {
        public override Column FindColumn(string columnName)
        {
            Debug.WriteLine("FindColumn(" + columnName + ")");
            return null;
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
                db.BeginTransaction();

                var statements = GetStatements(dbId, txId);
                foreach (var statement in statements)
                {
                    //db.Query(new SQLite.TableMapping(

                    List<object> result = db.Query(new Mapping(), statement._Statement, statement._Parameters);
                }

                db.Commit();
            }).Start();
        }
    }
}
