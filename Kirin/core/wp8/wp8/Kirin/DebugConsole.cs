using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace KirinWP8
{
    class DebugConsole
    {
        public void log(string message, string level)
        {
            Debug.WriteLine(level + ": " +message);
        }
    }
}
