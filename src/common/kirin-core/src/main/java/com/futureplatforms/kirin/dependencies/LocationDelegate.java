package com.futureplatforms.kirin.dependencies;

import com.futureplatforms.kirin.dependencies.AsyncCallback.AsyncCallback1;

public interface LocationDelegate {
    public static interface LocationCallback {
        public void onFail(String errDesc);
        public void onSuccess(Location location);
    }
    
    public static class Location {
    	public final double _Lat, _Lng, _Acc;
    	public final long _Timestamp;
    	public Location(double lat, double lng, double acc, long timestamp) {
    		this._Lat = lat;
    		this._Lng = lng;
    		this._Acc = acc;
    		this._Timestamp = timestamp;
    	}
    	
    	public String toString() {
    		return "" + _Lat + ", " + _Lng + " (" + _Acc + "m accuracy) <" + _Timestamp + ">";
    	}
    }
    
    public enum Accuracy { Fine, Medium, Coarse, NoPower }
    
    /**
     * Return continuous location readings at a specified interval.  Carries on returning locations
     * until stopListening() is called
     * @param accuracy
     * @param interval
     * @param callback
     */
    public void startUpdatingLocation(Accuracy accuracy, long interval, LocationCallback callback);

    /**
     * Stop listening for the location
     */
    public void stopUpdating();
    
    public void hasPermission(AsyncCallback1<Boolean> cb);
}