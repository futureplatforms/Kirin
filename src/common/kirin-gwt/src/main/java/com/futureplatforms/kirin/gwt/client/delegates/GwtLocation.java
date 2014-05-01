package com.futureplatforms.kirin.gwt.client.delegates;

import com.futureplatforms.kirin.dependencies.AsyncCallback.AsyncCallback1;
import com.futureplatforms.kirin.dependencies.LocationDelegate;
import com.futureplatforms.kirin.gwt.client.services.GwtLocationService;

public class GwtLocation implements LocationDelegate {

	@Override
	public void startUpdatingLocation(Accuracy accuracy, long interval,
			LocationCallback callback) {
		GwtLocationService.BACKDOOR()._startUpdatingLocation(accuracy, interval, callback);
	}

	@Override
	public void stopUpdating() {
		GwtLocationService.BACKDOOR()._stopUpdating();
	}

	@Override
	public void hasPermission(AsyncCallback1<Boolean> cb) {
		GwtLocationService.BACKDOOR()._hasPermission(cb);
	}

}
