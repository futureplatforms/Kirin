package com.futureplatforms.kirin.android;

import java.util.List;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.futureplatforms.kirin.dependencies.LocationDelegate;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.common.collect.Lists;

public class AndroidLocation implements LocationDelegate {
	private LocationClient mLocationClient;

	private LocationRequest mLocationRequest;

	private LocationListener mLocationListener;

	public AndroidLocation(Context context) {
		mLocationClient = new LocationClient(context, connectionCallbacks,
				onConnectionFailedListener);
		mLocationClient.connect();
	}

	List<Runnable> onConnecteds = Lists.newArrayList();
	List<Runnable> onConnectFaileds = Lists.newArrayList();

	ConnectionCallbacks connectionCallbacks = new ConnectionCallbacks() {

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
	OnConnectionFailedListener onConnectionFailedListener = new OnConnectionFailedListener() {

		@Override
		public void onConnectionFailed(ConnectionResult connectionResult) {
			for (Runnable r : onConnectFaileds)
				r.run();
			onConnectFaileds.clear();
			onConnecteds.clear();
		}
	};

	@Override
	public boolean getIsListening() {
		return mLocationListener != null;
	}

	@Override
	public void getLocation(final Accuracy accuracy,
			final LocationCallback callback) {
		if (!mLocationClient.isConnected()) {
			onConnecteds.add(new Runnable() {

				@Override
				public void run() {
					getLocation(accuracy, callback);
				}
			});
			onConnectFaileds.add(new Runnable() {

				@Override
				public void run() {
					callback.onFail("Google Play Services Needed");
				}
			});

			if (!mLocationClient.isConnecting())
				mLocationClient.connect();
			return;
		}

		Location loc = mLocationClient.getLastLocation();
		if (loc != null)
			callback.onSuccess(loc.getLatitude(), loc.getLongitude(),
					loc.getAccuracy());
		else
			callback.onFail("Can't get location");
	}

	@Override
	public void getLocationContinuous(final Accuracy accuracy,
			final int intervalMs, final LocationCallback callback) {
		if (!mLocationClient.isConnected()) {
			onConnecteds.add(new Runnable() {

				@Override
				public void run() {
					getLocationContinuous(accuracy, intervalMs, callback);
				}
			});
			onConnectFaileds.add(new Runnable() {

				@Override
				public void run() {
					callback.onFail("Google Play Services Needed");
				}
			});

			if (!mLocationClient.isConnecting())
				mLocationClient.connect();
			return;
		}

		mLocationRequest = LocationRequest.create();
		mLocationRequest.setInterval(intervalMs);
		if (accuracy == Accuracy.Fine)
			mLocationRequest
					.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

		if (mLocationListener != null)
			mLocationClient.removeLocationUpdates(mLocationListener);
		mLocationListener = new LocationListener() {

			@Override
			public void onLocationChanged(Location loc) {
				callback.onSuccess(loc.getLatitude(), loc.getLongitude(),
						loc.getAccuracy());
			}
		};

		mLocationClient.requestLocationUpdates(mLocationRequest,
				mLocationListener);
	}

	@Override
	public void stopListening() {
		if (mLocationListener != null)
			mLocationClient.removeLocationUpdates(mLocationListener);
		mLocationListener = null;
	}

	@Override
	public double getLatitude() {
		return mLocationClient.getLastLocation().getLatitude();
	}

	@Override
	public double getLongitude() {
		return mLocationClient.getLastLocation().getLongitude();
	}

	@Override
	public float getAccuracy() {
		return mLocationClient.getLastLocation().getAccuracy();
	}

}
