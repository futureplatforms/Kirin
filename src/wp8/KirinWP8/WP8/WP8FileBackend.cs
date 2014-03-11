using KirinWindows.Core;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;
using System.Windows;

namespace KirinWP8.WP8
{
    class WP8FileBackend : IFileBackend
    {
        private string _AssemblyName;
        public WP8FileBackend(string assemblyName)
        {
            this._AssemblyName = assemblyName;
        }

        public string LoadFileFromResource(string resourceName)
        {
            var uriStr = _AssemblyName + ";component" + resourceName;
            Debug.WriteLine("uriStr: " + uriStr);
            var resource = Application.GetResourceStream(new Uri(uriStr, UriKind.Relative));
            var str = ReadFully(resource.Stream);
            return str;
        }

        private static string ReadFully(Stream stream)
        {
            UTF8Encoding temp = new UTF8Encoding(true);
            StringBuilder sb = new StringBuilder();
            byte[] b = new byte[1024];
            int len;
            while ((len = stream.Read(b, 0, b.Length)) > 0)
            {
                sb.Append(temp.GetString(b, 0, len));
            }
            return sb.ToString();
        }
    }
}
