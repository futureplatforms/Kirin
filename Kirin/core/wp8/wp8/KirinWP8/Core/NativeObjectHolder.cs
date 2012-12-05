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
        private readonly string[] Names = { "System", "Microsoft" };
        private Dictionary<string, MethodInfo> Methods;
        private object Obj;

        public NativeObjectHolder(object o, bool isGwt)
        {
            this.Obj = o;
            Methods = new Dictionary<string, MethodInfo>();

            var t = Obj.GetType();

            while (t != null)
            {
                if (!Names.Any(t.FullName.StartsWith)) 
                {
                    foreach (var method in RuntimeReflectionExtensions.GetRuntimeMethods(t))
                    {
                        if (method.IsPublic && !Names.Any(method.DeclaringType.FullName.StartsWith))
                        {
                            var methodNameForJS = method.Name;
                            var numParams = method.GetParameters().Length;
                            if (isGwt)
                            {
                                for (var i = 0; i < numParams; i++)
                                {
                                    methodNameForJS += "_";
                                }
                            }
                            Methods.Add(methodNameForJS, method);
                        }
                    }
                }
                t = t.GetTypeInfo().BaseType;
            }
        }

        public void InvokeMethod(string methodName, object[] args)
        {
            Methods[methodName].Invoke(Obj, args);
        }

        public IEnumerable<string> GetMethodNames()
        {
            return Methods.Keys;
        }
    }
}
