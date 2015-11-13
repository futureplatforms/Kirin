using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace KirinWindows.Core
{
    public interface ISettingsBackend
    {
        IDictionary<string, object> Retrieve();
        void Persist();
        void Add(string key, object val);
        bool Remove(string key);
    }
}
