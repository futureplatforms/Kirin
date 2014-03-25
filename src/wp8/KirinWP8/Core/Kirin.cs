using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Net;
using System.Text;
using System.Windows;

namespace KirinWindows.Core
{
    public class Kirin
    {
        private static Kirin instance;
        private KirinWebViewHolder holder;
        private NativeContext context;

        private Kirin(string assemblyName, IKirinPlatform platform)
        {
            holder = new KirinWebViewHolder(context = new NativeContext(), platform.GetWebBrowserWrapper());
            BindScreen(new DebugConsole(), "DebugConsole", isGwt:false);

            platform.GetNetworking("Networking", this);
            new Settings("Settings", this, platform.GetSettingsBackend());
            new KirinWindows.Core.SymbolMaps(this, platform.GetFileBackend(assemblyName));
            DBService db = new KirinWindows.Core.DBService(this);
            new KirinWindows.Core.TXService(db, this);
        }

        private string ReadFully(Stream stream)
        {
            UTF8Encoding temp = new UTF8Encoding(true);
            StringBuilder sb = new StringBuilder();
            byte[] b = new byte[1024];
            while (stream.Read(b, 0, b.Length) > 0)
            {
                sb.Append(temp.GetString(b, 0, b.Length));
            }
            return sb.ToString();
        }

        public static void Initialize(string assemblyName, IKirinPlatform platform)
        {
            instance = new Kirin(assemblyName, platform); 
        }

        public static Kirin GetInstance()
        {
            if (instance == null) { throw new InvalidOperationException("Please call initialize first!"); }
            return instance;
        }

        public KirinAssistant BindScreen(Object o, String moduleName, bool isGwt=true)
        {
            return new KirinAssistant(o, moduleName, holder, context, isGwt);
        }
    }
}
