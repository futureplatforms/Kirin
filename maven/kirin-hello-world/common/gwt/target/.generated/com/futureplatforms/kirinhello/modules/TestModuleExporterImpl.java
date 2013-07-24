package com.futureplatforms.kirinhello.modules;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import org.timepedia.exporter.client.Exporter;
import org.timepedia.exporter.client.ExporterUtil;

public class TestModuleExporterImpl implements Exporter {
    public TestModuleExporterImpl() { export(); }
    public native void export0() /*-{
      var pkg = @org.timepedia.exporter.client.ExporterUtil::declarePackage(Ljava/lang/String;)('screens.TestModule');
      var _;
      $wnd.screens.TestModule = $entry(function() {
        var g, j = this;
        if (@org.timepedia.exporter.client.ExporterUtil::isAssignableToInstance(Ljava/lang/Class;Lcom/google/gwt/core/client/JavaScriptObject;)(@com.futureplatforms.kirinhello.modules.TestModule::class, arguments))
          g = arguments[0];
        else if (arguments.length == 0)
          g = @com.futureplatforms.kirinhello.modules.TestModuleExporterImpl::___create()();
        j.g = g;
        @org.timepedia.exporter.client.ExporterUtil::setWrapper(Ljava/lang/Object;Lcom/google/gwt/core/client/JavaScriptObject;)(g, j);
        return j;
      });
      _ = $wnd.screens.TestModule.prototype = new Object();
      _.onLoad = $entry(function(a0) { 
        this.g.@com.futureplatforms.kirinhello.modules.TestModule::onLoad(Lcom/google/gwt/core/client/JavaScriptObject;)(a0);
      });
      _.onUnload = $entry(function() { 
        this.g.@com.futureplatforms.kirinhello.modules.TestModule::onUnload()();
      });
      _.testyMethod = $entry(function(a0,a1) { 
        this.g.@com.futureplatforms.kirinhello.modules.TestModule::testyMethod(Ljava/lang/String;I)(a0,a1);
      });
      _.toString = $entry(function() { 
        return this.g.@com.futureplatforms.kirinhello.modules.TestModule::toString()();
      });
      
      @org.timepedia.exporter.client.ExporterUtil::addTypeMap(Ljava/lang/Class;Lcom/google/gwt/core/client/JavaScriptObject;)
       (@com.futureplatforms.kirinhello.modules.TestModule::class, $wnd.screens.TestModule);
      
      if(pkg) for (p in pkg) if ($wnd.screens.TestModule[p] === undefined) $wnd.screens.TestModule[p] = pkg[p];
    }-*/;
    public static com.futureplatforms.kirinhello.modules.TestModule ___create() {
      return new com.futureplatforms.kirinhello.modules.TestModule();
    }
    private static boolean exported;
    public void export() { 
      if(!exported) {
        exported=true;
        export0();
      }
    }
}
