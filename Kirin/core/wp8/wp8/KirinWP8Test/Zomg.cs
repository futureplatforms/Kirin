using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text; 
using System.Threading.Tasks;
 
namespace KirinTest
{ 
    class Zomg 
    { 
        public void Arrrgh()
        { 
            Debug.WriteLine("it's arrrgh!"); 
        } 
         
        public void HeresAStringArray(string[] strs)
        {
            foreach (var str in strs)
            {
                Debug.WriteLine(str);
            }
        }
         
        public void HeresAnIntArray(int[] ints)
        {  
            foreach (var i in ints)
            { 
                Debug.WriteLine(i);
            }
        }

        public void HeresABoolArray(bool[] bools)
        {
            foreach (var b in bools)
            {
                Debug.WriteLine(b);
            }
        }

        public void HeresSomeArrays(string[] strs, int[] ints, bool[] bools)
        {
            HeresAStringArray(strs);
            HeresAnIntArray(ints);
            HeresABoolArray(bools);
        }
    } 
} 
