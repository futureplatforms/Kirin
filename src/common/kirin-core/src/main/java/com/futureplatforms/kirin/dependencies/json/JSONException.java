package com.futureplatforms.kirin.dependencies.json;

/**
 * Replicates the 20080701 version of org.json API
 *
 */
public class JSONException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Throwable cause;

    /**
     * Constructs a JSONException with an explanatory message.
     * @param message Detail about the reason for the exception.
     */
    public JSONException(String message) {
        super(message);
    }

    public JSONException(Throwable t) {
        super(t.getMessage());
        this.cause = t;
    }

    public Throwable getCause() {
        return this.cause;
    }
}