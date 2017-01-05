package com.futureplatforms.kirin.gwt.client.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.NoExport;

import com.futureplatforms.kirin.dependencies.StaticDependencies;
import com.futureplatforms.kirin.dependencies.StaticDependencies.Configuration;
import com.futureplatforms.kirin.gwt.client.KirinService;
import com.futureplatforms.kirin.gwt.client.services.natives.SymbolMapServiceNative;
import com.futureplatforms.kirin.gwt.compile.NoBind;
import com.google.common.collect.Maps;
import com.google.gwt.core.client.GWT;

@Export(value = "SymbolMapService", all = true)
@ExportPackage("screens")
public class SymbolMapService extends KirinService<SymbolMapServiceNative> {
	
	public static class MappedJavaMethod {
		public final String _ClassName, _MemberName, _SourceUri, _SourceLine;
		public MappedJavaMethod(String className, String memberName, String sourceUri, String sourceLine) {
			this._ClassName = className;
			this._MemberName = memberName;
			this._SourceUri = sourceUri;
			this._SourceLine = sourceLine;
		}
	}
	
	public Map<String, MappedJavaMethod> _SymbolMap = Maps.newHashMap();
	
	private static SymbolMapService _Instance;
    
    @NoBind
    @NoExport
    public static SymbolMapService BACKDOOR() { return _Instance; }
    
	public SymbolMapService() {
		super(GWT.<SymbolMapServiceNative>create(SymbolMapServiceNative.class));
		_Instance = this;
	}
	
	protected void _onLoad() {
		if (StaticDependencies.getInstance().getProfile() == Configuration.Debug) {
			getNativeObject().setSymbolMapDetails(GWT.getModuleName(), GWT.getPermutationStrongName());
		}
	}

	public void setSymbolMap(String symbolMap) {
		// Symbol maps are in the form:
		// # jsName, jsniIdent, className, memberName, sourceUri, sourceLine, fragmentNumber

		// Strange bug in String.split caused infinite loop here on iOS 9.  So replacing
		// with our own implementation.
		List<String> lines = new ArrayList<>();
		int from = 0, index;
		while ((index = symbolMap.indexOf("\n", from)) != -1) {
			lines.add(symbolMap.substring(from, index));
			from = index + 1;
		}
		lines.add(symbolMap.substring(from));

		for (String line : lines) {
			// Ignore comments
			try {
				if (!line.startsWith("#")) {
					String[] components = line.split(",");
					_SymbolMap.put(components[0], new MappedJavaMethod(components[2], components[3], components[4], components[5]));
				}
			} catch (Throwable t) {
			}
		}
		StaticDependencies.getInstance().getLogDelegate().log("SymbolMap: " + _SymbolMap.size());
	}
}
