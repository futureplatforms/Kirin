using KirinWindows.Core;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace KirinTest
{
    public class ZomgJS
    {
        private KirinAssistant Assistant;
        public ZomgJS(KirinAssistant ka)
        {
            this.Assistant = ka;
        }

        public void Whateva(int hello)
        {
            Assistant.jsMethod("Whateva", new object[] { hello });
        }
    }
}
