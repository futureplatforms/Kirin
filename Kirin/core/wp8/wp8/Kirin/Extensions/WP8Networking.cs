using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;

namespace KirinWP8.Extensions
{
    class WP8Networking : KirinExtension
    {
        private string payload, onError, toPost;
        private bool isGet;
        public WP8Networking(string s, Kirin k) : base(s, k)
        {
        }

        public void downloadString_(JObject o)
        {
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
            if (isGet)
            {
                req.BeginGetResponse(new AsyncCallback(Net_Resp), req);
            }
            else
            {
                req.BeginGetRequestStream(new AsyncCallback(Net_Req), req);
            }
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
            catch (Exception e)
            {
                KirinAssistant.executeCallback(onError, "WebPost.Post_ReqStream() Just caught exception: " + e.GetType() + ", " + e.Message);
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
                KirinAssistant.executeCallback(payload, Uri.EscapeDataString(s));
            }
            catch (WebException e)
            {
                KirinAssistant.executeCallback(onError, "WebException: " + e.ToString());
            }
            catch (Exception e)
            {
                KirinAssistant.executeCallback(onError, "Exception: " + e.ToString());
            }
        }
    }
}
