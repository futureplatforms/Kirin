using Generated;
using KirinWindows.Core;
using KirinWP8;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;

namespace KirinWP8
{
    public class ReqVals
    {
        public readonly int _ConnId;
        public readonly string _Method, _Url, _PostData;
        public readonly string[] _HeaderKeys, _HeaderVals;
        public ReqVals(int connId, string method, string url, string postData, string[] headerKeys, string[] headerVals)
        {
            this._ConnId = connId;
            this._Method = method;
            this._Url = url;
            this._PostData = postData;
            this._HeaderKeys = headerKeys;
            this._HeaderVals = headerVals;
        }
    }

    public class WP8GwtNetworkingRunner
    {
        private string payload, onError, toPost;
        private bool isGet;
        private ReqVals _LastReq;
        private Generated.NetworkingService _Service;

        public WP8GwtNetworkingRunner(KirinAssistant assistant)
        {
            _Service = new Generated.NetworkingService(assistant);
        }

        private void _retrieve(ReqVals req) {
            retrieve(req._ConnId, req._Method, req._Url, req._PostData, req._HeaderKeys, req._HeaderVals);
        }

        public void retrieveB64(int connId, string method, string url, string postData, string[] headerKeys, string[] headerVals)
        {
            // TODO: implement me
        }

        public void retrieve(int connId, string method, string url, string postData, string[] headerKeys, string[] headerVals)
        {
            try
            {
                _LastReq = new ReqVals(connId, method, url, postData, headerKeys, headerVals);
                isGet = "GET".Equals(method);
                if (!isGet)
                {
                    if (!"POST".Equals(method) && !"PUT".Equals(method))
                    {
                        throw new InvalidOperationException(method + " is not a valid method, only GET, POST or PUT supported");
                    }
                }
                if (postData != null && !isGet)
                {
                    toPost = postData.ToString();
                }

                HttpWebRequest req = (HttpWebRequest)WebRequest.Create(url);
                for (var i=0; i<headerKeys.Length; i++) 
                {
                    var key = headerKeys[i];
                    var val = headerVals[i];

                    if ("content-type".Equals(key, StringComparison.InvariantCultureIgnoreCase))
                    {
                        req.ContentType = val;
                    }
                    else
                    {
                        req.Headers[key] = val;
                    }
                }

                req.Method = method;
                PerformRequest(req);
            }
            catch (Exception e)
            {
                Debug.WriteLine(e.ToString());
            }
        }


        private void Net_Req(IAsyncResult res)
        {
            try
            {
                var req = res.AsyncState as HttpWebRequest;
                if (!isGet && !string.IsNullOrEmpty(toPost))
                {
                    var bytes = new System.Text.UTF8Encoding().GetBytes(toPost);
                    using (var stream = req.EndGetRequestStream(res))
                    {
                        stream.Write(bytes, 0, bytes.Length);
                        stream.Flush();
                        stream.Close();
                    }
                }
                req.BeginGetResponse(Net_Resp, req);
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
                        sb.Append(s.Substring(limit * i, limit));
                    }
                    else
                    {
                        sb.Append(s.Substring(limit * i));
                    }
                }

                int code = (int)resp.StatusCode;
                string[] keys = resp.Headers.AllKeys;
                string[] vals = new string[keys.Length];
                for (var i = 0; i < keys.Length; i++)
                {
                    vals[i] = resp.Headers[keys[i]];
                }
                _Service.payload(_LastReq._ConnId, code, sb.ToString(), keys, vals);
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
                _retrieve(_LastReq);
                return;
            }
            _Service.onError(_LastReq._ConnId);
            Debug.WriteLine("Exception: " + wex.ToString());
        }

        private void PerformRequest(HttpWebRequest request)
        {
            if (request == null) return;

            if (request.Method.ToUpper() == "GET")
                request.BeginGetResponse(new AsyncCallback(Net_Resp), request);
            else
                request.BeginGetRequestStream(new AsyncCallback(Net_Req), request);
        }   
    }

    public class WP8GwtNetworking : KirinExtension, NetworkingServiceNative
    {
        public WP8GwtNetworking(string s, Kirin k)
            : base(s, k)
        {
        }

        public void retrieve(int connId, string method, string url, string postData, string[] headerKeys, string[] headerVals)
        {
            (new WP8GwtNetworkingRunner(this.KirinAssistant)).retrieve(connId, method, url, postData, headerKeys, headerVals);
        }

        public void retrieveB64(int connId, string method, string url, string postData, string[] headerKeys, string[] headerVals)
        {
            // TODO: implement me
        }
    }
}