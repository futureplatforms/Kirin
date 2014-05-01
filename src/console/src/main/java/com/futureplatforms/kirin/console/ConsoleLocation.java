package com.futureplatforms.kirin.console;

import com.futureplatforms.kirin.dependencies.LocationDelegate;
 
public class ConsoleLocation implements LocationDelegate
{
	
	public ConsoleLocation()
	{
	}

    public void startUpdatingLocation(Accuracy accuracy, LocationCallback callback) {
        // TODO Auto-generated method stub
        
    }

    public void getLocationContinuous(Accuracy accuracy, int intervalMs,
            LocationCallback callback) {
        // TODO Auto-generated method stub
        
    }

    public void stopListening() {
        // TODO Auto-generated method stub
        
    }

    public boolean getIsListening() {
        // TODO Auto-generated method stub
        return false;
    }

    public double getLatitude() {
        // TODO Auto-generated method stub
        return 0;
    }

    public double getLongitude() {
        // TODO Auto-generated method stub
        return 0;
    }

    public float getAccuracy() {
        // TODO Auto-generated method stub
        return 0;
    }
	
}
