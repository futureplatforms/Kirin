using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;

namespace KirinWindows.Core
{
    class NativeObjectHolder
    {
        private List<string> methodNames;
        private object o;
        public NativeObjectHolder(object o)
        {
            this.o = o;
            methodNames = new List<string>();
            Type t = o.GetType();

            while (t != null && !t.FullName.StartsWith("System"))
            {
                IEnumerable<MethodInfo> methods = RuntimeReflectionExtensions.GetRuntimeMethods(t);
                foreach (MethodInfo method in methods)
                {
                    if (method.IsPublic)
                    {
                        methodNames.Add(method.Name);
                    }
                }
                t = t.GetTypeInfo().BaseType;
            }
        }

        public List<string> GetMethodNames()
        {
            return methodNames;
        }

        public void InvokeMethod(string methodName, object[] args)
        {
            o.GetType().GetTypeInfo().GetDeclaredMethod(methodName).Invoke(o, args);
        }
    }
}
