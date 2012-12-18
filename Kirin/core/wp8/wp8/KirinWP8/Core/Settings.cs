using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using Windows.Foundation;

namespace KirinWindows.Core
{
    class Settings : KirinExtension
    {
        private ISettingsBackend backend;

        public Settings(string s, Kirin k, ISettingsBackend backend)
            : base(s, k)
        {
            this.backend = backend;
            KirinAssistant.jsMethod("mergeOrOverwrite", new object[] { PrepareSettingsObject(backend.Retrieve()) });
            KirinAssistant.jsMethod("resetEnvironment");
        }

        private Dictionary<string, object> PrepareSettingsObject(IDictionary<string, object> settings)
        {
            Dictionary<string, object> toReturn = new Dictionary<string, object>();
            foreach (KeyValuePair<string, object> pair in settings)
            {
                toReturn.Add(pair.Key.Substring("kirin-".Length), pair.Value);
            }

            return toReturn;
        }

        public void updateContents_withDeletes_(JObject adds, string[] deletes)
        { 
            foreach (KeyValuePair<string, JToken> pair in adds)
            {
                backend.Add("kirin-" + pair.Key, pair.Value.ToString());
            }

            foreach (string val in deletes)
            {
                backend.Remove("kirin-" + val);
            }
            ScheduleBackgroundSave();
        }

        public void requestPopulateJSWithCallback_(string callback)
        {
            KirinAssistant.executeCallback(callback, new object[] { PrepareSettingsObject(backend.Retrieve()) });
        } 

        private void ScheduleBackgroundSave()
        {
            lock (backend)
            {
                IAsyncAction act = Windows.System.Threading.ThreadPool.RunAsync(ForceImmediateSave);
            }
        }

        public void ForceImmediateSave(IAsyncAction op)
        {
            lock (backend)
            {
                try
                {
                    backend.Persist();
                }
                catch { }
            }
        }
    }
}
