using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace KirinWP8
{
    class WP8Networking : KirinExtension
    {
        public WP8Networking(string s, Kirin k) : base(s, k)
        {
        }

        public void downloadString_(JObject o)
        {
            var method = o["method"];
            var url = o["url"];
            var postData = o["postData"];
            var headers = o["headers"];
            var payload = o["payload"];
            var onError = o["onError"];

            KirinAssistant.executeCallback(payload.ToString(), "arse");
        }
    }
}
