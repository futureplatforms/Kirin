using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using KirinHelloWP8.Resources;
using KirinWindows.Core;
using KirinWP8;
using System.Diagnostics;

namespace KirinHelloWP8
{
    public partial class MainPage : PhoneApplicationPage, Generated.TestModuleNative
    {
        // Constructor
        public MainPage()
        {
            Debug.WriteLine("here it is");
            InitializeComponent();
            Kirin.Initialize(new WP8KirinPlatform());
            Kirin k = Kirin.GetInstance();
            KirinAssistant ka = k.BindScreen(this, "TestModule");
            ka.onLoad();
            Generated.TestModule testModule = new Generated.TestModule(ka);
            testModule.testyMethod("a hoy hoy hoy", 1333337); 
            // Sample code to localize the ApplicationBar
            //BuildLocalizedApplicationBar();
        }

        // Sample code for building a localized ApplicationBar
        //private void BuildLocalizedApplicationBar()
        //{
        //    // Set the page's ApplicationBar to a new instance of ApplicationBar.
        //    ApplicationBar = new ApplicationBar();

        //    // Create a new button and set the text value to the localized string from AppResources.
        //    ApplicationBarIconButton appBarButton = new ApplicationBarIconButton(new Uri("/Assets/AppBar/appbar.add.rest.png", UriKind.Relative));
        //    appBarButton.Text = AppResources.AppBarButtonText;
        //    ApplicationBar.Buttons.Add(appBarButton);

        //    // Create a new menu item with the localized string from AppResources.
        //    ApplicationBarMenuItem appBarMenuItem = new ApplicationBarMenuItem(AppResources.AppBarMenuItemText);
        //    ApplicationBar.MenuItems.Add(appBarMenuItem);
        //}

        public void testyNativeMethod(string str)
        {
            Debug.WriteLine("testyNativeMethod: " + str);
        }

        public void testyNativeMethod2(string str)
        {
            Debug.WriteLine("testyNativeMethod2: " + str);
        }
    }
}