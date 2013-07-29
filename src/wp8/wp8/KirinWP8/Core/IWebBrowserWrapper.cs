using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace KirinWindows.Core
{
    public interface INavigationEventArgsWrapper
    {
        object Content { get; }
        Uri Uri { get; }
    }

    public interface INotifyEventArgsWrapper
    {
        string Value { get; }
    }

    public interface IWebBrowserWrapper
    {
        void InvokeScriptAsync(string scriptName, params string[] args);
        void Navigate(Uri uri);
        void NavigateKirin();
        event EventHandler<INotifyEventArgsWrapper> ScriptNotify;
        event EventHandler<INavigationEventArgsWrapper> Navigated;
    }
}
