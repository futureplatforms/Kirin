package com.futureplatforms.kirin.gwt.client.services;

import java.util.Map;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.NoExport;

import com.futureplatforms.kirin.dependencies.AsyncCallback.AsyncCallback1;
import com.futureplatforms.kirin.gwt.client.KirinService;
import com.futureplatforms.kirin.gwt.client.services.natives.CryptoServiceNative;
import com.futureplatforms.kirin.gwt.compile.NoBind;
import com.google.common.collect.Maps;
import com.google.gwt.core.client.GWT;

@Export(value = "CryptoService", all = true)
@ExportPackage("screens")
public class CryptoService extends KirinService<CryptoServiceNative> {
	private static CryptoService _Instance;
    
    @NoBind
    @NoExport
    public static CryptoService BACKDOOR() { return _Instance; }
    
	public CryptoService() {
		super(GWT.<CryptoServiceNative>create(CryptoServiceNative.class));
		_Instance = this;
	}
	
	private Map<String, AsyncCallback1<byte[]>> _PBKDF2Callbacks = Maps.newHashMap();
	
	private int _NextID = Integer.MIN_VALUE;
	
	@NoExport
    public void _pbkdf2(String plaintext, String salt, int iterations, int keyLenBytes, AsyncCallback1<byte[]> cb) {
		int next = _NextID;
		_NextID++;
		_PBKDF2Callbacks.put("" + next, cb);
		getNativeObject().pbkdf2("" + next, plaintext, salt, iterations, keyLenBytes);
	}
	
    private static byte[] hexStrToBytes(String hex) {
		byte[] bytes = new byte[hex.length() / 2];
		for (int i = 0; i < hex.length(); i += 2) {
	      String str = hex.substring(i, i + 2);
	      int byteVal = Integer.parseInt(str, 16);
	      bytes[i/2] = (byte)byteVal;
	    }
		return bytes;
	}
	
	public void result(String cbId, String res) {
		_PBKDF2Callbacks.remove(cbId).onSuccess(hexStrToBytes(res));
	}
}
