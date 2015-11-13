using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;

namespace KirinWindows.Core
{
    public class Hybrid
    {
        private IWebBrowserWrapper _Browser;
        private object _Module;
        private MethodInfo _WebViewSaid;
        public Hybrid(IWebBrowserWrapper browser, object module)
        {
            this._Browser = browser;
            _Browser.ScriptNotify += browser_ScriptNotify;

            this._Module = module;

            foreach (var method in RuntimeReflectionExtensions.GetRuntimeMethods(module.GetType()))
            {
                if ("webViewSaid" == method.Name)
                {
                    var args = method.GetParameters();
                    
                    if (args.Length == 2)
                    {
                        _WebViewSaid = method;
                        break;
                    }
                }
            }

            if (_WebViewSaid == null)
            {
                throw new InvalidOperationException("module must have a webViewSaid(string, string) method");
            }
        }

        public void tellWebview(string javascript)
        {
            _Browser.InvokeScriptAsync("eval", javascript);
        }

        void browser_ScriptNotify(object sender, INotifyEventArgsWrapper e)
        {
            if (e.Value.StartsWith("native"))
            {
                _Browser.InvokeScriptAsync("eval", "window.api.setReady(true);");

                string schemelessUri = e.Value.Substring("native://".Length);

                int slash = schemelessUri.IndexOf('/');
                string method = schemelessUri.Substring(0, slash);
                string parameters = schemelessUri.Substring(slash + 1);

                Debug.WriteLine("about to invoke webViewSaid: " + method + ", " + parameters);
                _WebViewSaid.Invoke(_Module, new object[] { method, parameters });
            }
        }
    }


}
