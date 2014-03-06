package com.futureplatforms.kirin.gwt.client.services;

import java.util.Map;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.NoExport;

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

	@NoBind
	@NoExport
	public void _setStrongName(String strongName) {
		getNativeObject().setStrongName(strongName);
	}

	public void setSymbolMap(String symbolMap) {
		// Symbol maps are in the form:
		// # jsName, jsniIdent, className, memberName, sourceUri, sourceLine, fragmentNumber
		String[] lines = symbolMap.split("\n");
		for (String line : lines) {
			// Ignore comments
			if (!line.startsWith("#")) {
				String[] components = line.split(",");
				_SymbolMap.put(components[0], new MappedJavaMethod(components[2], components[3], components[4], components[5]));
			}
		}
	}
}
