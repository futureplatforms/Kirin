using KirinWindows.Core;
using Microsoft.Phone.Controls;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Navigation;

namespace KirinWP8
{
    class WP8NavigationEventArgsWrapper : INavigationEventArgsWrapper
    {
        private NavigationEventArgs e;

        public WP8NavigationEventArgsWrapper(NavigationEventArgs e)
        {
            this.e = e;
        }

        public object Content
        {
            get { return e.Content; }
        }

        public Uri Uri
        {
            get { return e.Uri; }
        }
    }

    class WP8NotifyEventArgs : INotifyEventArgsWrapper
    {
        private NotifyEventArgs e;
        public WP8NotifyEventArgs(NotifyEventArgs e)
        {
            this.e = e;
        }

        public string Value
        {
            get { return e.Value; }
        }
    }

    public class WP8WebBrowser : IWebBrowserWrapper
    {
        private WebBrowser _WB;
        public WP8WebBrowser():this(new WebBrowser())
        {
        }

        public WP8WebBrowser(WebBrowser wb)
        {
            _WB = wb;
            _WB.IsScriptEnabled = true;
            _WB.ScriptNotify += wb_ScriptNotify;
            _WB.Navigated += wb_Navigated;
        }

        void wb_Navigated(object sender, NavigationEventArgs e)
        {
            if (Navigated != null)
            {
                Navigated(sender, new WP8NavigationEventArgsWrapper(e));
            }
        }

        void wb_ScriptNotify(object sender, NotifyEventArgs e)
        {
            if (ScriptNotify != null)
            {
                ScriptNotify(sender, new WP8NotifyEventArgs(e));
            }
        }

        public void InvokeScriptAsync(string scriptName, params string[] args)
        {
            _WB.Dispatcher.BeginInvoke(() =>
            {
                try
                {
                    _WB.InvokeScript(scriptName, args);
                }
                catch (Exception e)
                {
                    Debug.WriteLine(e);
                }
            });
        }

        public void Navigate(Uri uri)
        {
            _WB.Navigate(uri);
        }

        public void NavigateKirin()
        {
            Navigate(new Uri("/app/Kirin.html", UriKind.Relative));
        }

        public event EventHandler<INotifyEventArgsWrapper> ScriptNotify;

        public event EventHandler<INavigationEventArgsWrapper> Navigated;


        public System.Windows.Threading.Dispatcher Dispatcher
        {
            get { return _WB.Dispatcher; }
        }
    }
}
