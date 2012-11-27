using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace KirinWP8
{
    public class KirinAssistant
    {
        private object nativeObject;
        private string moduleName;
        private KirinWebViewHolder holder;
        private NativeContext context;

        public KirinAssistant(object nativeObject, string moduleName, KirinWebViewHolder holder, NativeContext context)
        {
            this.nativeObject = nativeObject;
            this.moduleName = moduleName;
            this.holder = holder;
            this.context = context;
            context.RegisterNativeObject(nativeObject, moduleName);
        } 

        public void onLoad()
        {
            string[] args = new string[2];
            args[0] = moduleName;
            string[] methods = context.MethodsForClass(moduleName);
            args[1] = JsonConvert.SerializeObject(methods);
            holder.InvokeScriptoids(string.Format(KirinConstants.REGISTER_MODULE_WITH_METHODS, args));
        }

        public void jsMethod(string methodName)
        {
            jsMethod(methodName, null);
        }

        public void jsMethod(string methodName, object[] args)
        {   
            if (args == null || args.Length == 0)
            {
                holder.InvokeScriptoids(string.Format(KirinConstants.EXECUTE_METHOD_JS, moduleName, methodName));
            }
            else
            {
                holder.InvokeScriptoids(string.Format(KirinConstants.EXECUTE_METHOD_WITH_ARGS_JS, moduleName, methodName, JsonConvert.SerializeObject(args)));
            }
        }

        public void executeCallback(string methodName, object arg)
        {
            this.executeCallback(methodName, new object[] { arg });
        }

        public void executeCallback(string methodName, object[] args)
        {
            if (args == null || args.Length == 0)
            {
                holder.InvokeScriptoids(string.Format(KirinConstants.EXECUTE_CALLBACK_JS, methodName));
            }
            else
            {
                holder.InvokeScriptoids(string.Format(KirinConstants.EXECUTE_CALLBACK_WITH_ARGS_JS, methodName, JsonConvert.SerializeObject(args)));
            }
        }
    }  
}
