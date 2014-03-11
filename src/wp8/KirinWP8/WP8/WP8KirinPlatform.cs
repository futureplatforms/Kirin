using KirinWindows.Core;
using KirinWP8.WP8;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace KirinWP8
{
    public class WP8KirinPlatform : IKirinPlatform
    {
        public IWebBrowserWrapper GetWebBrowserWrapper()
        {
            return new WP8WebBrowser();
        }

        public INetworking GetNetworking(string name, Kirin k)
        {
            return new WP8Networking(name, k);
        }

        public ISettingsBackend GetSettingsBackend()
        {
            return new WP8SettingsBackend();
        }

        public IFileBackend GetFileBackend(string assemblyName)
        {
            return new WP8FileBackend(assemblyName);
        }
    }
}
