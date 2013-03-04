using KirinWindows.Core;
using Newtonsoft.Json.Linq;
using System;
using System.IO;
using System.Net;
using System.Text;
using System.Net.NetworkInformation;
using System.Threading;

namespace KirinWP8
{
    public class WP8Networking : KirinExtension, INetworking
    {
        private string payload, onError, toPost;
        private bool isGet;
        private JObject _lastRequest;

        public WP8Networking(string s, Kirin k) : base(s, k)
        {
        }

        public void downloadString_(JObject o)
        {
            _lastRequest = o;
            var method = o["method"].ToString().ToUpper();
            isGet = "GET".Equals(method);
            if (!isGet)
            {
                if (!"POST".Equals(method))
                {
                    throw new InvalidOperationException(method + " is not a valid method, only GET or POST supported");
                }
            }
            var url = o["url"].ToString();
            var postData = o["postData"];
            if (postData != null && !isGet)
            {
                toPost = postData.ToString();
            }
            var headers = o["headers"];
            payload = o["payload"].ToString();
            onError = o["onError"].ToString();

            HttpWebRequest req = (HttpWebRequest)WebRequest.Create(url);
            if (headers != null) {
                foreach (JProperty prop in headers)
                {
                    var val = prop.Value.ToString();
                    var key = prop.Name;
                    if ("content-type".Equals(key, StringComparison.InvariantCultureIgnoreCase))
                    {
                        req.ContentType = val;
                    }
                    else
                    {
                        req.Headers[key] = val;
                    }
                }
            }

            req.Method = method;
            PerformRequest(req);
        }

        private void Net_Req(IAsyncResult res)
        {
            try
            {
                var req = res.AsyncState as HttpWebRequest;
                if (!isGet)
                {
                    var bytes = new System.Text.UTF8Encoding().GetBytes(toPost);
                    var stream = req.EndGetRequestStream(res);
                    stream.Write(bytes, 0, bytes.Length);
                    stream.Flush();
                    stream.Close();
                }
                req.BeginGetResponse(new AsyncCallback(Net_Resp), req);
            }
            catch (WebException wex)
            {
                handleWebException(wex, res.AsyncState as HttpWebRequest);
            }
        }

        private void Net_Resp(IAsyncResult res)
        {
            try
            {
                var req = (HttpWebRequest)res.AsyncState;
                var resp = (HttpWebResponse)req.EndGetResponse(res);
                 
                var streamReader = new StreamReader(resp.GetResponseStream());
                string s = streamReader.ReadToEnd();

                // Uri.EscapeDataString only accepts up to 32766 characters.
                int limit = 30000;

                StringBuilder sb = new StringBuilder();
                int loops = s.Length / limit;
                for (int i = 0; i <= loops; i++)
                {
                    if (i < loops)
                    {
                        sb.Append(Uri.EscapeDataString(s.Substring(limit * i, limit)));
                    }
                    else
                    {
                        sb.Append(Uri.EscapeDataString(s.Substring(limit * i)));
                    }
                }

                KirinAssistant.executeCallback(payload, sb.ToString());
            }
            catch (WebException wex)
            {
                handleWebException(wex, res.AsyncState as HttpWebRequest);
            }
        }

        //MS place a RequestCanceled that must be collected before networking can resume
        //http://blogs.msdn.com/b/wpukcoe/archive/2012/01/06/fast-application-switching-and-acquired-os-resources.aspx
        private void handleWebException(WebException wex, HttpWebRequest req)
        {
            if (wex.Status == WebExceptionStatus.RequestCanceled)
            {
                //Fast Application Switching - re-issue request
                downloadString_(_lastRequest);
                return;
            }
            KirinAssistant.executeCallback(onError, "Exception: " + wex.ToString());
        }

        private void PerformRequest(HttpWebRequest request)
        {
            if (request == null) return;

            //If woken up then spin on network state as we need it - max 5 seconds and then operations will fail
            //if the network is still not available
            int waitForNetwork = 10;
            while (!NetworkInterface.GetIsNetworkAvailable() && waitForNetwork-- > 0)
                Thread.Sleep(500);

            if (request.Method.ToUpper() == "GET")
                request.BeginGetResponse(new AsyncCallback(Net_Resp), request);
            else
                request.BeginGetRequestStream(new AsyncCallback(Net_Req), request);
        }
    }
}
