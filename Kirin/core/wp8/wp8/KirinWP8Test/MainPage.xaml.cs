using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using KirinTest.Resources;
using KirinWP8;
using System.Diagnostics;
using KirinWindows.Core;

namespace KirinTest
{
    public partial class MainPage : PhoneApplicationPage
    { 
        // Constructor
        public MainPage()
        {
            this.InitializeComponent();
            Kirin.Initialize(new WP8KirinPlatform());
            Kirin k = Kirin.GetInstance();
            KirinAssistant ka = k.BindScreen(new Zomg(), "Zomg");
            ka.onLoad(); 
        }
    }
}