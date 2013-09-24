package com.futureplatforms.kirin.gwt.client.delegates;

import com.futureplatforms.kirin.dependencies.LocationDelegate;

public class KirinLocation implements LocationDelegate {

    private LocationCallback resp;
    private boolean continuous;
    
    private void locationSuccess(double lat, double lng) {
        resp.onSuccess(lat, lng, 0);
        if (!continuous) {
            unregister();
        }
    }
    
    private void locationError(String errDesc) {
        resp.onFail(errDesc);
        if (!continuous) {
            unregister();
        }
    }
    
    private static native void unregister() /*-{
        ;var location = $wnd.require("Location")
        location.unregisterAllListeners();
    }-*/;
    
    private static native void doIt(boolean isFine, KirinLocation p) /*-{
        var location = $wnd.require("Location")
        location.unregisterAllListeners()
        location.registerLocationListener(isFine, function(newLocation) {
            var lat = newLocation.latitude;
            var lng = newLocation.longitude;
            var timestamp = newLocation.timestamp;
            var horizAcc = newLocation.horizontalAccuracy;
            $entry( p.@com.futureplatforms.kirin.gwt.client.delegates.KirinLocation::locationSuccess(DD)(lat, lng) );
        });
        
        location.registerLocationErrorListener(function(errDesc) {
            $entry( p.@com.futureplatforms.kirin.gwt.client.delegates.KirinLocation::locationError(Ljava/lang/String;)(errDesc) );
        });
        
        location.refreshLocation();
    }-*/;

    @Override
    public void getLocation(Accuracy accuracy, LocationCallback callback) {
        this.resp = callback;
        continuous = false;
        doIt(accuracy == Accuracy.Fine, this);
    }

    @Override
    public void getLocationContinuous(Accuracy accuracy, int intervalMs,
            LocationCallback callback) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void stopListening() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean getIsListening() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public double getLatitude() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getLongitude() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public float getAccuracy() {
        // TODO Auto-generated method stub
        return 0;
    }
}
