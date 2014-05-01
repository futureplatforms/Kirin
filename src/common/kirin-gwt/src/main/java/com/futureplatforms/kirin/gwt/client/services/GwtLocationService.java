package com.futureplatforms.kirin.gwt.client.services;

import java.util.Map;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.NoExport;

import com.futureplatforms.kirin.dependencies.StaticDependencies;
import com.futureplatforms.kirin.dependencies.AsyncCallback.AsyncCallback1;
import com.futureplatforms.kirin.dependencies.LocationDelegate.Accuracy;
import com.futureplatforms.kirin.dependencies.LocationDelegate.Location;
import com.futureplatforms.kirin.dependencies.LocationDelegate.LocationCallback;
import com.futureplatforms.kirin.dependencies.StaticDependencies.SettingsDelegate;
import com.futureplatforms.kirin.gwt.client.KirinService;
import com.futureplatforms.kirin.gwt.client.services.natives.GwtLocationServiceNative;
import com.futureplatforms.kirin.gwt.compile.NoBind;
import com.google.common.collect.Maps;
import com.google.gwt.core.client.GWT;

@Export(value = "GwtLocationService", all = true)
@ExportPackage("screens")
public class GwtLocationService extends KirinService<GwtLocationServiceNative> {

	private static GwtLocationService _Instance;
    
    @NoBind
    @NoExport
    public static GwtLocationService BACKDOOR() { return _Instance; }
    
	public GwtLocationService() {
		super(GWT.<GwtLocationServiceNative>create(GwtLocationServiceNative.class));
		_Instance = this;
	}

	private LocationCallback _Callback;
	
	@NoExport
	public void _startUpdatingLocation(Accuracy accuracy, long interval,
			LocationCallback callback) {
		this._Callback = callback;
		getNativeObject().startUpdating(accuracy.ordinal());
	}
	
	@NoExport
	public void _stopUpdating() {
		_Callback = null;
		getNativeObject().stopUpdating();
	}
	
	private int _PermissionCbId = Integer.MIN_VALUE;
	private Map<Integer, AsyncCallback1<Boolean>> _PermissionCallbacks = Maps.newHashMap();
	
	@NoExport
	public void _hasPermission(AsyncCallback1<Boolean> cb) {
		int id = _PermissionCbId;
		_PermissionCbId++;
		_PermissionCallbacks.put(id, cb);
		getNativeObject().hasPermission(id);
	}
	
	public void updatingLocationCallback(String lat, String lng, String acc, String timestamp) {
		double secs = Double.parseDouble(timestamp);
		long ms = (long) (secs * 1000);
		if (_Callback != null) {
			Location loc = new Location(
					Double.parseDouble(lat), 
					Double.parseDouble(lng), 
					Double.parseDouble(acc), 
					ms);
			_Callback.onSuccess(loc);
		}
	}
	
	public void hasPermissionCallback(int cbId, boolean hasPermission) {
		_PermissionCallbacks.remove(cbId).onSuccess(hasPermission);
	}
	
	public void locationError(String err) {
		if (_Callback != null) {
			_Callback.onFail(err);
		}
	}
}
