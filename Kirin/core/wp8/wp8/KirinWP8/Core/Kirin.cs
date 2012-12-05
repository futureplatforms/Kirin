using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Net;
using System.Windows;

namespace KirinWindows.Core
{
    public class Kirin
    {
        private static Kirin instance;
        private KirinWebViewHolder holder;
        private NativeContext context;

        private Kirin(IKirinPlatform platform)
        {
            holder = new KirinWebViewHolder(context = new NativeContext(), platform.GetWebBrowserWrapper());
            BindScreen(new DebugConsole(), "DebugConsole", isGwt:false);
            platform.GetNetworking("Networking", this);
            new Settings("Settings", this, platform.GetSettingsBackend()); 
        }


        public static void Initialize(IKirinPlatform platform)
        {
            instance = new Kirin(platform); 
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
