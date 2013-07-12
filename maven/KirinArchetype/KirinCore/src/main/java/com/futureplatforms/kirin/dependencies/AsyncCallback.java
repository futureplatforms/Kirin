package com.futureplatforms.kirin.dependencies;

/**
 * Various methods take an asynchronous callback as a parameter. 
 * This allows the method to perform some potentially long running task without blocking.
 * 
 * Note that it is possible for the callback to be called back immediately, before the called asynchronous 
 * method has even returned.
 * 
 * @param <ResultType> The type of the object passed to the onSuccess method. Could be Void if there is none.
 */
public interface AsyncCallback {
	void onSuccess();
	void onFailure();
	
	public static interface AsyncCallback1<ResultType> {
	    void onSuccess(ResultType res);
	    void onFailure();
	}
	
    public static interface AsyncCallback2<ResultType1, ResultType2> {
        void onSuccess(ResultType1 res1, ResultType2 res2);
        void onFailure();
    }
    
	public static interface AsyncCallback3<ResultType1, ResultType2, ResultType3> {
	    void onSuccess(ResultType1 res1, ResultType2 res2, ResultType3 res3);
	    void onFailure();
	}
}