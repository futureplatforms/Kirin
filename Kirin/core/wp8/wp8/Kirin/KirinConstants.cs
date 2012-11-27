using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace KirinWP8
{
    class KirinConstants
    {
        public static string REGISTER_MODULE_WITH_METHODS = "EXPOSED_TO_NATIVE.native2js.loadProxyForModule('{0}', {1})";
        public static string EXECUTE_METHOD_JS = "EXPOSED_TO_NATIVE.native2js.execMethod('{0}', '{1}')";
        public static string EXECUTE_METHOD_WITH_ARGS_JS = "EXPOSED_TO_NATIVE.native2js.execMethod('{0}', '{1}', {2})";
        public static string EXECUTE_CALLBACK_JS = "EXPOSED_TO_NATIVE.native2js.execCallback('{0}')";
        public static string EXECUTE_CALLBACK_WITH_ARGS_JS = "EXPOSED_TO_NATIVE.native2js.execCallback('{0}', {1})";
    }
}
