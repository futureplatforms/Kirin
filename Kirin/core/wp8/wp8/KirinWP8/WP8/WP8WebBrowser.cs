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

    class WP8WebBrowser : IWebBrowserWrapper
    {
        private WebBrowser wb;
        public WP8WebBrowser()
        {
            wb = new WebBrowser();
            wb.IsScriptEnabled = true;
            wb.ScriptNotify += wb_ScriptNotify;
            wb.Navigated += wb_Navigated;
        }

        void wb_Navigated(object sender, NavigationEventArgs e)
        { 
            Navigated(sender, new WP8NavigationEventArgsWrapper(e));
        }

        void wb_ScriptNotify(object sender, NotifyEventArgs e)
        {
            ScriptNotify(sender, new WP8NotifyEventArgs(e));
        }

        public void InvokeScriptAsync(string scriptName, params string[] args)
        {
            wb.Dispatcher.BeginInvoke(() =>
            {
                try
                {
                    wb.InvokeScript(scriptName, args);
                }
                catch (Exception e)
                {
                    Debug.WriteLine(e);
                }
            });
        }

        public void Navigate(Uri uri)
        {
            wb.Navigate(uri);
        }

        public void NavigateKirin()
        {
            Navigate(new Uri("/generated-javascript/index-wp8.html", UriKind.Relative));
        }

        public event EventHandler<INotifyEventArgsWrapper> ScriptNotify;

        public event EventHandler<INavigationEventArgsWrapper> Navigated;


        public System.Windows.Threading.Dispatcher Dispatcher
        {
            get { return wb.Dispatcher; }
        }
    }
}
