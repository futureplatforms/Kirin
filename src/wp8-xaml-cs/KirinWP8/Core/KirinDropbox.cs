using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace KirinWindows.Core
{
    public class KirinDropbox
    {
        private static int _NextKey = int.MinValue;
        private static Dictionary<int, List<Dictionary<string, string>>> _Queries = new Dictionary<int, List<Dictionary<string, string>>>();
        public static string DepositObject(List<Dictionary<string, string>> query)
        {
            int thisKey = _NextKey;
            _NextKey++;
            _Queries[thisKey] = query;
            return ""+thisKey;
        }

        public List<Dictionary<string, string>> ConsumeObject(string token)
        {
            var intToken = int.Parse(token);
            var obj = _Queries[intToken];
            _Queries.Remove(intToken);
            return obj;
        }
    }
}
