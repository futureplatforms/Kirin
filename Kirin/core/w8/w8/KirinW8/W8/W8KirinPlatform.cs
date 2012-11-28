using KirinWindows.Core;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace KirinW8.W8
{
    class W8KirinPlatform : IKirinPlatform
    {
        public IWebBrowserWrapper GetWebBrowserWrapper()
        {
            throw new NotImplementedException();
        }

        public INetworking GetNetworking(string name, Kirin k)
        {
            throw new NotImplementedException();
        }

        public ISettingsBackend GetSettingsBackend()
        {
            throw new NotImplementedException();
        }
    }
}
