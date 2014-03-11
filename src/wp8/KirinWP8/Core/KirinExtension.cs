using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace KirinWindows.Core
{
    public abstract class KirinExtension
    {
        private string moduleName;

        protected KirinAssistant KirinAssistant { get; private set; }

        public KirinExtension(string moduleName, Kirin k, bool isGwt=true)
        {
            this.moduleName = moduleName;
            KirinAssistant = k.BindScreen(this, moduleName, isGwt);
            KirinAssistant.onLoad();
        }
    }
}
