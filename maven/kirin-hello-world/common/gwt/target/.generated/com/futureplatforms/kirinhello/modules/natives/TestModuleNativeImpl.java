package com.futureplatforms.kirinhello.modules.natives;

import com.google.gwt.core.client.JavaScriptObject;
import com.futureplatforms.kirinhello.modules.natives.TestModuleNative;
import com.futureplatforms.kirin.gwt.client.modules.IKirinProxied;

public class TestModuleNativeImpl implements TestModuleNative, IKirinProxied {
  TestModuleNativeImpl() {}
  private JavaScriptObject jso;
  public void $setKirinNativeObject(JavaScriptObject jso) {
    this.jso = jso;
  }
  public void testyNativeMethod(java.lang.String str) {
    _testyNativeMethod(str, jso);
  }
  private static native void _testyNativeMethod(java.lang.String str, JavaScriptObject jso) /*-{
    jso.testyNativeMethod_(str);
  }-*/;
  public void testyNativeMethod2(java.lang.String str) {
    _testyNativeMethod2(str, jso);
  }
  private static native void _testyNativeMethod2(java.lang.String str, JavaScriptObject jso) /*-{
    jso.testyNativeMethod2_(str);
  }-*/;
}
