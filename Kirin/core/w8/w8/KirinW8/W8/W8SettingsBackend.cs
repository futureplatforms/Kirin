using KirinWindows.Core;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Windows.Storage;

namespace KirinW8.W8
{
    class W8SettingsBackend : ISettingsBackend
    {
        private ApplicationDataContainer localSettings;
        private ApplicationDataContainer kirinContainer;

        public W8SettingsBackend()
        {
            localSettings = ApplicationData.Current.LocalSettings;
            if (!localSettings.Containers.ContainsKey("KirinSettings"))
            {
                kirinContainer = localSettings.CreateContainer("KirinSettings", Windows.Storage.ApplicationDataCreateDisposition.Always);
                Debug.WriteLine("New Container created");
            }
            else
            {
                Debug.WriteLine("Container already existed");
            } 
        }

        public IDictionary<string, object> Retrieve()
        {
            return kirinContainer.Values;
        }

        public void Persist()
        {
            // don't have to do nothing
        }

        public void Add(string key, object val)
        {
            kirinContainer.Values[key] = val;
        }

        public bool Remove(string key)
        {
            return kirinContainer.Values.Remove(key);
        }
    }
}
