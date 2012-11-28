using KirinWindows.Core;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Windows.ApplicationModel;
using Windows.Foundation;
using Windows.Storage;
using Windows.UI.Core;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Navigation;

namespace KirinW8.W8
{
    class W8NavigationEventArgs : INavigationEventArgsWrapper
    {
        private NavigationEventArgs e;
        public W8NavigationEventArgs(NavigationEventArgs e)
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

    class W8NotifyEventArgs : INotifyEventArgsWrapper
    {
        private NotifyEventArgs e;
        public W8NotifyEventArgs(NotifyEventArgs e)
        {
            this.e = e;
        }

        public string Value
        {
            get { return e.Value; }
        }
    }

    class W8WebBrowser : IWebBrowserWrapper
    {
        private WebView wv;

        public W8WebBrowser()
        {
            wv = new WebView();
            wv.ScriptNotify += wv_ScriptNotify;
        }

        void wv_ScriptNotify(object sender, NotifyEventArgs e)
        {
            if ("OK".Equals(e.Value))
            {
                Navigated(sender, null);
            }
            else
            {
                ScriptNotify(sender, new W8NotifyEventArgs(e));
            }
        } 
         
        public void InvokeScriptAsync(string scriptName, params string[] args)
        {
            IAsyncAction action = wv.Dispatcher.RunAsync(CoreDispatcherPriority.Normal, () =>
            {
                try
                {
                    wv.InvokeScript(scriptName, args);
                }
                catch (Exception e)
                {
                    Debug.WriteLine(e);
                }
            });
        }

        public void NavigateKirin()
        {
            wv.Navigate(new Uri("ms-appx-web:///generated-javascript/index-w8.html"));   
        }

        public event EventHandler<INotifyEventArgsWrapper> ScriptNotify;

        public event EventHandler<INavigationEventArgsWrapper> Navigated;


        public void Navigate(Uri uri)
        {
            wv.Navigate(uri);
        }
    }
}
