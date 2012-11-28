using KirinWindows.Core;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace KirinW8.W8
{
    class W8WebBrowser : IWebBrowserWrapper
    {
        public void InvokeScriptAsync(string scriptName, params string[] args)
        {
            throw new NotImplementedException();
        }

        public void Navigate(Uri uri)
        {
            throw new NotImplementedException();
        }

        public void NavigateKirin()
        {
            throw new NotImplementedException();
        }

        public event EventHandler<INotifyEventArgsWrapper> ScriptNotify;

        public event EventHandler<INavigationEventArgsWrapper> Navigated;
    }
}
