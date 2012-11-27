using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;

namespace KirinWP8
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
                Debug.WriteLine("type: " + t);
                MethodInfo[] methods = t.GetMethods();
                foreach (MethodInfo method in methods)
                {
                    if (method.IsPublic)
                    {
                        methodNames.Add(method.Name);
                    }
                }
                t = t.BaseType;
            }
        }

        public List<string> GetMethodNames()
        {
            return methodNames;
        }

        public void InvokeMethod(string methodName, object[] args)
        {
            o.GetType().GetMethod(methodName).Invoke(o, args);
        }
    }
}
