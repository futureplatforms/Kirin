using KirinWindows.Core;
using System;
using System.Collections.Generic;
using System.IO.IsolatedStorage;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace KirinWP8.WP8
{
    class WP8SettingsBackend : ISettingsBackend
    {
        private IsolatedStorageSettings settings;

        public WP8SettingsBackend()
        {
            settings = IsolatedStorageSettings.ApplicationSettings;
        }

        public void Persist()
        {
            settings.Save();
        }

        public void Add(string key, object val)
        {
            settings[key] = val;
        }

        public bool Remove(string key)
        {
            return settings.Remove(key);
        }

        IDictionary<string, object> ISettingsBackend.Retrieve()
        {
            return settings;
        }
    }
}
