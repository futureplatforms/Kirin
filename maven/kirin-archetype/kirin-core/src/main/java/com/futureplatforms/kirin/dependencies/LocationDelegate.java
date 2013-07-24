package com.futureplatforms.kirin.dependencies;

public interface LocationDelegate {
    public static interface LocationCallback {
        public void onFail(String errDesc);
        public void onSuccess(double lat, double lng, float accuracy);
        public void onWarning(String statusDesc);
        public void onEnabledChanged(boolean enabled);
    }
    
    public enum Accuracy { Fine, Coarse }
    
    /**
     * Returns a one-off location reading
     * @param accuracy
     * @param callback
     */
    public void getLocation(Accuracy accuracy, LocationCallback callback);
    
    /**
     * Return continuous location readings at a specified interval.  Carries on returning locations
     * until stopListening() is called
     * @param accuracy
     * @param interval
     * @param callback
     */
    public void getLocationContinuous(Accuracy accuracy, int intervalMs, LocationCallback callback);

    /**
     * Stop listening for the location
     */
    public void stopListening();
    
    public boolean getIsListening();
    public double getLatitude();
    public double getLongitude();
    public float getAccuracy();
}