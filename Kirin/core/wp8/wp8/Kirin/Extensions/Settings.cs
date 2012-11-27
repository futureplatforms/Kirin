using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Diagnostics;
using System.IO.IsolatedStorage;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace KirinWP8.Extensions
{
    class Settings : KirinExtension
    {
        private IsolatedStorageSettings settings;

        public Settings(string s, Kirin k)
            : base(s, k)
        { 
            settings = IsolatedStorageSettings.ApplicationSettings;
            KirinAssistant.jsMethod("mergeOrOverwrite", new object[] { PrepareSettingsObject(settings) });
            KirinAssistant.jsMethod("resetEnvironment");
        }

        private Dictionary<string, object> PrepareSettingsObject(IsolatedStorageSettings settings)
        {
            Dictionary<string, object> toReturn = new Dictionary<string, object>();
            foreach (KeyValuePair<string, object> pair in settings)
            {
                toReturn.Add(pair.Key.Substring("kirin-".Length), pair.Value);
            }

            return toReturn;
        }

        public void updateContents_withDeletes_(JObject adds, JArray deletes)
        {
            foreach (KeyValuePair<string, JToken> pair in adds)
            {
                settings.Add("kirin-" + pair.Key, pair.Value.ToString());
            }

            foreach (JToken val in deletes)
            {
                settings.Remove("kirin-" + val.ToString());
            }
            ScheduleBackgroundSave();
        }

        public void requestPopulateJSWithCallback_(string callback)
        {
            KirinAssistant.executeCallback(callback, new object[] { PrepareSettingsObject(settings) });
        }

        private BackgroundWorker pendingBgWorker = null;
        private void ScheduleBackgroundSave()
        {
            lock (settings)
            {
                if (null == pendingBgWorker)
                {
                    pendingBgWorker = new BackgroundWorker();
                    pendingBgWorker.DoWork += (s, e) => { Thread.Sleep(100); }; // Wait a short moment
                    pendingBgWorker.RunWorkerCompleted += (s, e) =>
                    {
                        lock (settings)
                        {
                            pendingBgWorker = null;
                            ForceImmediateSave();
                        }
                    };
                    pendingBgWorker.RunWorkerAsync();
                }
            }
        }

        private void ForceImmediateSave()
        {
            lock (settings)
            {
                try
                {
                    settings.Save();
                    Debug.WriteLine("SAVED!");
                }
                catch { }
            }
        }
    }
}
