﻿using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace KirinWindows.Core
{
    class DebugConsole
    {
        public void log_atLevel_(string message, string level)
        {
            Debug.WriteLine(level + ": " +message);
        }
    }
}
