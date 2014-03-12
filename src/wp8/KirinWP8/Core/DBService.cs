using KirinWindows.Core;
using SQLite;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows.Threading;

namespace KirinWP8.Core
{
    class DBService : KirinExtension, Generated.DatabaseAccessServiceNative
    {
        public Dictionary<int, SQLiteConnection> _Connections = new Dictionary<int, SQLiteConnection>();
        private Generated.DatabaseAccessService _Service;

        public DBService(Kirin k) : base("DatabaseAccessService", k)
        {
            _Service = new Generated.DatabaseAccessService(KirinAssistant);
        }

        public void open(string filename, int dbId)
        {
            new Thread(() =>
            {
                string dbPath = Path.Combine(Windows.Storage.ApplicationData.Current.LocalFolder.Path, filename);
                if (!FileExists(filename).Result)
                {
                    using (var db = new SQLiteConnection(dbPath))
                    {
                        _Connections.Add(dbId, db);
                        _Service.databaseOpenedSuccess(dbId);
                    }
                }
            }).Start();
        }

        private async Task<bool> FileExists(string fileName)
        {
            var result = false;
            try
            {
                var store = await Windows.Storage.ApplicationData.Current.LocalFolder.GetFileAsync(fileName);
                result = true;
            }
            catch
            {
            }

            return result;

        }
    }
}
