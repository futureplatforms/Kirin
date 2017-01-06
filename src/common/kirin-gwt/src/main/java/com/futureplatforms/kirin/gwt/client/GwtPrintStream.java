package com.futureplatforms.kirin.gwt.client;

import com.futureplatforms.kirin.dependencies.StaticDependencies;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;

/**
 * Created by douglashoskins on 14/08/2015.
 */
public class GwtPrintStream extends PrintStream {
    public GwtPrintStream() {
        super((OutputStream) null);
    }

    public void print(boolean x) {
        println("" + x);
    }

    public void print(char x) {
        println("" + x);
    }

    public void print(char[] x) {
        if (x == null) {
            println("<null>");
        } else {
            println(Arrays.toString(x));
        }
    }

    public void print(double x) {
        println("" + x);
    }

    public void print(float x) {
        println("" + x);
    }

    public void print(int x) {
        println("" + x);
    }

    public void print(long x) {
        println("" + x);
    }

    public void print(Object x) {
        println("" + x);
    }

    public void print(String s) {
        println(s);
    }

    public void println() {
        println("");
    }

    public void println(boolean x) {
        println("" + x);
    }

    public void println(char x) {
        println("" + x);
    }

    public void println(char[] x) {
        println(Arrays.toString(x));
    }

    public void println(double x) {
        println("" + x);
    }

    public void println(float x) {
        println("" + x);
    }

    public void println(int x) {
        println("" + x);
    }

    public void println(long x) {
        println("" + x);
    }

    public void println(Object x) {
        println("" + x);
    }

    public void println(String s) {
        StaticDependencies.getInstance().getLogDelegate().log(s);
    }

}
