using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace KirinWindows.Core
{
    class SymbolMaps : KirinExtension, Generated.SymbolMapServiceNative
    {
        private IFileBackend _FileBackend;
        private Generated.SymbolMapService _Service;
        public SymbolMaps(string name, Kirin k, IFileBackend fileBackend) : base(name, k)
        {
            _Service = new Generated.SymbolMapService(KirinAssistant);
            _FileBackend = fileBackend;
        }

        public void setSymbolMapDetails(string moduleName, string strongName)
        {
            Debug.WriteLine("setSymbolMapDetails(" + moduleName + ", " + strongName + ")");
            var symbolMap = _FileBackend.LoadFileFromResource("/app/WEB-INF/" + moduleName + "/symbolMaps/" + strongName + ".symbolMap");
            _Service.setSymbolMap(symbolMap);
        }
    }
}
