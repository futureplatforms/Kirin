using KirinWP8.Extensions;
using Microsoft.Phone.Controls;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Documents;
using System.Windows.Ink;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Shapes;

namespace KirinWP8
{
    public class Kirin
    {
        private static Kirin instance;
        private KirinWebViewHolder holder;
        private NativeContext context;

        private Kirin()
        {
            holder = new KirinWebViewHolder(context = new NativeContext());
            BindScreen(new DebugConsole(), "DebugConsole");
            new WP8Networking("Networking", this);
            new Settings("Settings", this); 
        }


        public static Kirin getInstance()
        {
            if (instance == null) { instance = new Kirin(); }
            return instance;
        }

        public KirinAssistant BindScreen(Object o, String moduleName)
        {
            return new KirinAssistant(o, moduleName, holder, context);
        }
    }
}
