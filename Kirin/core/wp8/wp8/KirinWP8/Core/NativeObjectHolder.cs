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
            // We don't need to traverse our way up the full type hierarchy because all available methods are included in GetRuntimeMethods
            if (!Names.Any(t.FullName.StartsWith)) 
            {
                foreach (var method in RuntimeReflectionExtensions.GetRuntimeMethods(t))
                {
                    if (method.IsPublic && !Names.Any(method.DeclaringType.FullName.StartsWith) && !method.IsAbstract)
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
        }

        public Type GetMethodParamType(string methodName, int paramNum)
        {
            return Methods[methodName].GetParameters()[paramNum].ParameterType;
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
