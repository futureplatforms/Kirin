package com.futureplatforms.kirin.android;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

import com.futureplatforms.kirin.dependencies.LocationDelegate;
 
public class AndroidLocation implements LocationDelegate
{
	private LocationManager locationManager;
	private LocationListener locationListener;
	private String activeProvider;
	private boolean _currentStatusAvailable;
	
	public AndroidLocation(Context context)
	{
		locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		_currentStatusAvailable = false;
	}
	
	@Override
	public boolean getIsListening() {
		return (locationListener != null);
	}
	
	@Override
	public void getLocation(Accuracy accuracy, LocationCallback callback)
	{
		configureListener(accuracy, callback, 0, 0);
	}
	
	@Override
	public void getLocationContinuous(Accuracy accuracy, int intervalMs,
			LocationCallback callback)
	{
		configureListener(accuracy, callback, intervalMs, 1);
	}
	
	private void configureProvider(Accuracy accuracy) {
		Criteria criteria = new Criteria();
		switch (accuracy)
		{
		case Coarse:
			criteria.setAccuracy(Criteria.ACCURACY_COARSE);
			break;
		case Fine:
			criteria.setAccuracy(Criteria.ACCURACY_FINE);
			break;
		default:
			criteria.setAccuracy(Criteria.NO_REQUIREMENT);
			break;
		}
		
//		criteria.setAccuracy(Criteria.NO_REQUIREMENT);
		activeProvider = locationManager.getBestProvider(criteria, true);
	}
	
	private void configureListener(Accuracy accuracy, LocationCallback callback, int intervalMs, int minDistance) {
		stopListening();
		
		configureProvider(accuracy);
		if (activeProvider == null)
		{
			callback.onFail("No Location Provider Available");
			return;
		}
		
		//Ensure our provider is enabled
		if (!locationManager.isProviderEnabled(activeProvider))
		{
			callback.onFail("No Location Provider Available");
			return;
		}
		
		locationListener = new KirirnLocationListener(callback, (intervalMs > 0)); 
		locationManager.requestLocationUpdates(activeProvider, intervalMs, minDistance, locationListener);
		setInitialLocation();
	}
	
	@Override
	public void stopListening()
	{
		if (locationListener != null)
			locationManager.removeUpdates(locationListener);
		locationListener = null;
	}
	
	private double _latitude;
	public double getLatitude() {
		return _latitude;
	}
	
	private double _longitude;
	public double getLongitude() {
		return _longitude;
	}
	
	private float _accuracy;
	public float getAccuracy() {
		return _accuracy;
	}
	
	private void setInitialLocation() {
		Location startPoint = locationManager.getLastKnownLocation(activeProvider);
		if (startPoint == null) return;
		_latitude = startPoint.getLatitude();
		_longitude = startPoint.getLongitude();
		_accuracy = startPoint.getAccuracy();
	}
	
	class KirirnLocationListener implements LocationListener {
		LocationCallback callback;
		boolean continuous;
		
		public KirirnLocationListener(LocationCallback callback,
				boolean continuous)
		{
			this.callback = callback;
			this.continuous = continuous;
		}
		
		@Override
		public void onLocationChanged(Location location) {
			callback.onSuccess(location.getLatitude(), location.getLongitude(), location.getAccuracy());
			if (!continuous) locationManager.removeUpdates(this);
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			stopListening();
			_currentStatusAvailable = false;
			callback.onEnabledChanged(false);
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			_currentStatusAvailable = true;
			callback.onEnabledChanged(true);
		}
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			
			boolean newStatus = false;
			if (status == LocationProvider.AVAILABLE || status == LocationProvider.TEMPORARILY_UNAVAILABLE)
				newStatus = true;
			
			if (newStatus == _currentStatusAvailable) return;
			callback.onEnabledChanged(newStatus);
			_currentStatusAvailable = newStatus;
		}
	}
}
