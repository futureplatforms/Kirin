using KirinWindows.Core;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace KirinW8.W8
{
    public class W8KirinPlatform : IKirinPlatform
    {
        public IWebBrowserWrapper GetWebBrowserWrapper()
        {
            return new W8WebBrowser();
        }

        public INetworking GetNetworking(string name, Kirin k)
        {
            return new W8Networking(name, k);
        }

        public ISettingsBackend GetSettingsBackend()
        {
            return new W8SettingsBackend();
        }
    }
}
