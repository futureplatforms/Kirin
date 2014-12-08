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

        public void HeresAString(string str)
        {
            Debug.WriteLine(str);
        }

        public void HeresAnInt(int i)
        {
            Debug.WriteLine(i);
        }

        public void HeresABool(bool b)
        {
            Debug.WriteLine(b);
        }

        public void HeresAStringArray(string[] strs)
        {
            Array.ForEach(strs, HeresAString);
        }
         
        public void HeresAnIntArray(int[] ints)
        {
            Array.ForEach(ints, HeresAnInt);
        }

        public void HeresABoolArray(bool[] bools)
        {
            Array.ForEach(bools, HeresABool);
        }

        public void HeresSomeArrays(string[] strs, int[] ints, bool[] bools)
        {
            HeresAStringArray(strs);
            HeresAnIntArray(ints);
            HeresABoolArray(bools);
        }

        /*public void HeresSomeArraysOfArrays(string[][] strs, int[][][] ints)
        {
            Array.ForEach(strs, HeresAStringArray);
            Array.ForEach(ints, (x) => Array.ForEach(x, HeresAnIntArray));
        }*/
    } 
} 
