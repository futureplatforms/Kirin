package com.futureplatforms.kirin.android;

import java.util.List;

import android.Manifest.permission;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.futureplatforms.kirin.dependencies.AsyncCallback.AsyncCallback1;
import com.futureplatforms.kirin.dependencies.LocationDelegate;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.common.collect.Lists;

public class AndroidLocation implements LocationDelegate {
	private LocationClient _LocationClient;

	private LocationListener mLocationListener;

	private Context _Context;
	
	public AndroidLocation(Context context) {
		this._Context = context;
		_LocationClient = new LocationClient(context, connectionCallbacks,
				onConnectionFailedListener);
		_LocationClient.connect();
	}

	private List<Runnable> onConnecteds = Lists.newArrayList();
	private List<Runnable> onConnectFaileds = Lists.newArrayList();

	private ConnectionCallbacks connectionCallbacks = new ConnectionCallbacks() {

		@Override
		public void onDisconnected() {
		}

		@Override
		public void onConnected(Bundle arg0) {
			for (Runnable r : onConnecteds)
				r.run();
			onConnecteds.clear();
			onConnectFaileds.clear();

		}
	};
	private OnConnectionFailedListener onConnectionFailedListener = new OnConnectionFailedListener() {

		@Override
		public void onConnectionFailed(ConnectionResult connectionResult) {
			for (Runnable r : onConnectFaileds)
				r.run();
			onConnectFaileds.clear();
			onConnecteds.clear();
		}
	};

	private static int priorityForAccuracy(Accuracy acc) {
		switch (acc) {
			case Coarse:
				return LocationRequest.PRIORITY_LOW_POWER;
			case Fine:
				return LocationRequest.PRIORITY_HIGH_ACCURACY;
			case Medium:
				return LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;
			case NoPower:
				return LocationRequest.PRIORITY_NO_POWER;
		}
		throw new IllegalArgumentException();
	}
	
	@Override
	public void startUpdatingLocation(final Accuracy accuracy, final long ms,
			final LocationCallback callback) {
		if (!_LocationClient.isConnected()) {
			onConnecteds.add(new Runnable() {

				@Override
				public void run() {
					startUpdatingLocation(accuracy, ms, callback);
				}
			});
			onConnectFaileds.add(new Runnable() {

				@Override
				public void run() {
					callback.onFail("Google Play Services Needed");
				}
			});

			if (!_LocationClient.isConnecting()) {
				_LocationClient.connect();
			}
			return;
		}

		LocationRequest req = LocationRequest.create();
		req.setPriority(priorityForAccuracy(accuracy));
		req.setInterval(ms);
		_LocationClient.requestLocationUpdates(req, mLocationListener = new LocationListener() {
			
			@Override
			public void onLocationChanged(android.location.Location loc) {
				callback.onSuccess(
						new Location(
								loc.getLatitude(), 
								loc.getLongitude(), 
								loc.getAccuracy(), 
								loc.getTime()));
			}
		});
	}


	@Override
	public void stopUpdating() {
		if (mLocationListener != null) {
			_LocationClient.removeLocationUpdates(mLocationListener);
		}
		mLocationListener = null;
	}

	@Override
	public void hasPermission(AsyncCallback1<Boolean> cb) {
		PackageManager pm = _Context.getPackageManager();
	    if (pm.checkPermission(permission.ACCESS_FINE_LOCATION, _Context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
	        cb.onSuccess(true);
	    } else {
	        cb.onSuccess(false);
	    }
	}

}
