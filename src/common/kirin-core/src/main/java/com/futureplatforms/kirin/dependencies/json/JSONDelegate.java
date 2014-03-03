package com.futureplatforms.kirin.dependencies.json;

public interface JSONDelegate {
	public JSONObject getJSONObject();

	public JSONObject getJSONObject(String jsonText) throws KirinJsonException;

	public JSONArray getJSONArray(String jsonText) throws KirinJsonException;

	public JSONArray getJSONArray();

	public String quoteAndEscape(String javaStringToEscapeAsJSONString);

	public class KirinJsonException extends Exception {

		public KirinJsonException(String string) {
			super(string);
		}

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
	}
}
