package com.futureplatforms.kirin.gwt.compile;

import java.util.Iterator;
import java.util.SortedMap;

import com.google.gwt.core.ext.LinkerContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.linker.AbstractLinker;
import com.google.gwt.core.ext.linker.Artifact;
import com.google.gwt.core.ext.linker.ArtifactSet;
import com.google.gwt.core.ext.linker.CompilationResult;
import com.google.gwt.core.ext.linker.LinkerOrder;
import com.google.gwt.core.ext.linker.SelectionProperty;

@LinkerOrder(LinkerOrder.Order.PRIMARY)
public class JsOnlyLinker extends AbstractLinker {

	@Override
	public String getDescription() {
		return "JavascriptLinker";
	}

	@Override
	public ArtifactSet link(TreeLogger logger, LinkerContext context,
			ArtifactSet artifacts) throws UnableToCompleteException {
		System.out.println("Yep yep it's " + getDescription());
		artifacts = new ArtifactSet(artifacts);
		
		for (Iterator<Artifact<?>> iterator = artifacts.iterator(); iterator.hasNext();) {
			Artifact<?> artifact = iterator.next();
			if (artifact.toString().endsWith(".gif")) {
				iterator.remove();
			}
		}
		
		

		// Add the compiled JavaScript
		for (CompilationResult c : artifacts.find(CompilationResult.class)) {

			String userAgent = null;
			for (SortedMap<SelectionProperty, String> map : c.getPropertyMap()) {
				for (SelectionProperty p : map.keySet()) {
					if ("user.agent".equals(p.getName())) {
						userAgent = map.get(p);
						break;
					}
				}
			}

			StringBuffer all = new StringBuffer();
			// Add variables required by GWT
			all.append("(function() {");
			all.append("var $wnd=window, $doc=document, $moduleName={}, $moduleBase={}, $stats=null, $sessionId=null;");
			for (String string : c.getJavaScript()) {
				all.append(string);
			}
			
			
			
//				all.append("" +
//						"if ($wnd.defineModule && $wnd.screens) {" +
//							"var gwtModules = $wnd.screens;" +
//							"for (var key in gwtModules) {" +
//									"if (gwtModules.hasOwnProperty(key) {" +
//										"$wnd.defineModule(key, function (require, exports) { return new gwtModules[key]; });" +
//									"}" +
//							"}" +
//						"}");
			all.append("gwtOnLoad(null, \"" + context.getModuleName() + "\", '');");
			
			all.append("})();");
			String filename = (userAgent != null) ? userAgent + ".js" : "safari.js";
			System.out.println("writing " + filename);
			artifacts.add(emitString(logger, all.toString(), filename));
		}

		return artifacts;
	}
}
