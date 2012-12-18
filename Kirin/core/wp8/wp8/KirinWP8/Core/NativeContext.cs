using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
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

        public void RegisterNativeObject(object obj, string name, bool isGwt)
        {
            objects.Add(name, new NativeObjectHolder(obj, isGwt));
        }

        public void PerformMethod(string className, string methodName, string parameters)
        {
            parameters = Uri.UnescapeDataString(parameters);
            var holder = objects[className];
            
            object[] objs = (object[])JsonConvert.DeserializeObject(parameters, (new object[0]).GetType());

            for (var i = 0; i < objs.Length; i++)
            {
                var obj = objs[i];
                if (obj is Int64)
                {
                    objs[i] = Convert.ToInt32(obj);
                }

                if (obj is JArray)
                {
                    var objAsJarray = obj as JArray;
                    if (objAsJarray.Count == 0)
                    {
                        objs[i] = new object[0];
                    }

                    var objArr = new object[objAsJarray.Count];

                    // what if it's empty................?
                    JToken anObj = objAsJarray[0];
                    for (var j = 0; j < objArr.Length; j++)
                    {
                        var current = objAsJarray[j].ToObject((new object()).GetType());
                        objArr[j] = current;
                    }

                    // it's either gonna be int, string or bool
                    if (anObj.Type == JTokenType.String)
                    {
                        var strArr = new string[objAsJarray.Count];
                        for (var j = 0; j < objArr.Length; j++)
                        {
                            strArr[j] = (string)objArr[j];
                        }
                        obj = strArr;
                    }
                    else if (anObj.Type == JTokenType.Integer)
                    {
                        var intArr = new int[objAsJarray.Count];
                        for (var j = 0; j < intArr.Length; j++)
                        { 
                            intArr[j] = Convert.ToInt32(objArr[j]);
                        }
                        obj = intArr;
                    }
                    else
                    {
                        var boolArr = new bool[objAsJarray.Count];
                        for (var j = 0; j < boolArr.Length; j++)
                        {
                            boolArr[j] = (bool)objArr[j];
                        }
                        obj = boolArr;
                    }
                    objs[i] = obj;
                }
            }

            holder.InvokeMethod(methodName, objs);
        }

        public string[] MethodsForClass(string className)
        {
            return objects[className].GetMethodNames().ToArray();
        }
    }
}
