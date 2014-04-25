using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace KirinWindows.Core
{
    public interface IKirinPlatform
    {
        IWebBrowserWrapper GetWebBrowserWrapper();
        Generated.NetworkingServiceNative GetNetworking(string name, Kirin k);
        ISettingsBackend GetSettingsBackend();
        IFileBackend GetFileBackend(string assemblyName);
    }
}
