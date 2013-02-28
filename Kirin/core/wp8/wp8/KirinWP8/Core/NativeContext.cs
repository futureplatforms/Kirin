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
            if (objects.ContainsKey(name))
                objects[name] = new NativeObjectHolder(obj, isGwt);
            else
                objects.Add(name, new NativeObjectHolder(obj, isGwt));
        }

        private object ConvertParameter(object param, Type expectedType)
        {
            // ints all come through as Int64s.  These need converting to ints.
            if (param is Int64)
            {
                if (expectedType != new Int32().GetType())
                {
                    throw new InvalidOperationException(param + " is an int, but expected " + expectedType);
                }
                return Convert.ToInt32(param);
            }

            // ok this parameter is an array
            if (param is JArray)
            {
                if (!expectedType.IsArray)
                {
                    throw new InvalidOperationException(param + " is an array, but expected " + expectedType);
                }

                var arrayOf = expectedType.GetElementType();
                
                var objAsJarray = param as JArray;
                // If this array is empty we need to create an empty array of the appropriate type
                if (objAsJarray.Count == 0)
                {
                    if (new Int32().GetType().Equals(arrayOf))
                    {
                        return new int[0];
                    }
                    if (true.GetType().Equals(arrayOf))
                    {
                        return new bool[0];
                    }
                    if (string.Empty.GetType().Equals(arrayOf))
                    {
                        return new string[0];
                    }

                    // this will probably fail, but should never get here anyway
                    return new object[0];
                }

                // OK if we haven't returned by now the array is not empty
                var objArr = new object[objAsJarray.Count];

                for (var j = 0; j < objArr.Length; j++)
                {
                    var current = objAsJarray[j].ToObject((new object()).GetType());
                    objArr[j] = current;
                }

                // Get the first element of the array
                JToken anObj = objAsJarray[0];
                
                // it's either gonna be int, string or bool
                if (anObj.Type == JTokenType.String)
                {
                    var strArr = new string[objAsJarray.Count];
                    for (var j = 0; j < objArr.Length; j++)
                    {
                        strArr[j] = (string)objArr[j];
                    }
                    return strArr;
                }
                else if (anObj.Type == JTokenType.Integer)
                {
                    var intArr = new int[objAsJarray.Count];
                    for (var j = 0; j < intArr.Length; j++)
                    {
                        intArr[j] = Convert.ToInt32(objArr[j]);
                    }
                    return intArr;
                }
                else
                {
                    var boolArr = new bool[objAsJarray.Count];
                    for (var j = 0; j < boolArr.Length; j++)
                    {
                        boolArr[j] = (bool)objArr[j];
                    }
                    return boolArr;
                }
            }

            return param;
        }

        public void PerformMethod(string className, string methodName, string parameters)
        {
            parameters = Uri.UnescapeDataString(parameters);
            var holder = objects[className];

            // so the Parameters string is a JSON array of the parameters to pass in to the method.
            object[] objs = (object[])JsonConvert.DeserializeObject(parameters, (new object[0]).GetType());
            
            // Expecting parameters to be ints, strings, bools or ARRAYS of all those.  JObjects can go through as they are.

            for (var i = 0; i < objs.Length; i++)
            {
                objs[i] = ConvertParameter(objs[i], holder.GetMethodParamType(methodName, i));
            }

#if DEBUG
            System.Diagnostics.Debug.WriteLine("{0}", methodName);
#endif
            holder.InvokeMethod(methodName, objs);
        }

        public string[] MethodsForClass(string className)
        {
            return objects[className].GetMethodNames().ToArray();
        }
    }
}
