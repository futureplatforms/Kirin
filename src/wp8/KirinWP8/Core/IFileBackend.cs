using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace KirinWindows.Core
{
    public interface IFileBackend
    {
        string LoadFileFromResource(String resourceName);
    }
}
