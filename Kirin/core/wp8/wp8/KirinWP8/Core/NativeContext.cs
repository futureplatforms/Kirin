using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;

namespace KirinWindows.Core
{
    public class NativeContext
    {
        private Dictionary<string, NativeObjectHolder> objects;
        public NativeContext()
        {
            objects = new Dictionary<string, NativeObjectHolder>();
        }

        public void RegisterNativeObject(object obj, string name)
        {
            objects.Add(name, new NativeObjectHolder(obj));
        }

        public void PerformMethod(string className, string methodName, string parameters)
        {
            parameters = Uri.UnescapeDataString(parameters);
            var holder = objects[className];
            
            object[] objs = (object[])JsonConvert.DeserializeObject(parameters, (new object[0]).GetType());

            holder.InvokeMethod(methodName, objs);
        }

        public string[] MethodsForClass(string className)
        {
            return objects[className].GetMethodNames().ToArray();
        }
    }
}
