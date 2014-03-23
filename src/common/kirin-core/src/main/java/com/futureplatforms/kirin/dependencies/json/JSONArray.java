package com.futureplatforms.kirin.dependencies.json;

/**
 * Replicates the 20080701 version of org.json API
 * 
 */
public abstract class JSONArray {
	/**
	 * Determine if the value is null.
	 * 
	 * @param index
	 *            The index must be between 0 and length() - 1.
	 * @return true if the value at the index is null, or if there is no value.
	 */
	public abstract boolean isNull(int index);

	/**
	 * Get the boolean value associated with an index. The string values "true"
	 * and "false" are converted to boolean.
	 * 
	 * @param index
	 *            The index must be between 0 and length() - 1.
	 * @return The truth.
	 * @throws JSONException
	 *             If there is no value for the index or if the value is not
	 *             convertable to boolean.
	 */
	public abstract boolean getBoolean(int index) throws JSONException;

	/**
	 * Get the double value associated with an index.
	 * 
	 * @param index
	 *            The index must be between 0 and length() - 1.
	 * @return The value.
	 * @throws JSONException
	 *             If the key is not found or if the value cannot be converted
	 *             to a number.
	 */
	public abstract double getDouble(int index) throws JSONException;

	/**
	 * Get the int value associated with an index.
	 * 
	 * @param index
	 *            The index must be between 0 and length() - 1.
	 * @return The value.
	 * @throws JSONException
	 *             If the key is not found or if the value cannot be converted
	 *             to a number. if the value cannot be converted to a number.
	 */
	public abstract int getInt(int index) throws JSONException;

	/**
	 * Get the JSONArray associated with an index.
	 * 
	 * @param index
	 *            The index must be between 0 and length() - 1.
	 * @return A JSONArray value.
	 * @throws JSONException
	 *             If there is no value for the index. or if the value is not a
	 *             JSONArray
	 */
	public abstract JSONArray getJSONArray(int index) throws JSONException;

	/**
	 * Get the JSONObject associated with an index.
	 * 
	 * @param index
	 *            subscript
	 * @return A JSONObject value.
	 * @throws JSONException
	 *             If there is no value for the index or if the value is not a
	 *             JSONObject
	 */
	public abstract JSONObject getJSONObject(int index) throws JSONException;

	/**
	 * Get the string associated with an index.
	 * 
	 * @param index
	 *            The index must be between 0 and length() - 1.
	 * @return A string value.
	 * @throws JSONException
	 *             If there is no value for the index.
	 */
	public abstract String getString(int index) throws JSONException;

	/**
	 * Get the optional boolean value associated with an index. It returns false
	 * if there is no value at that index, or if the value is not Boolean.TRUE
	 * or the String "true".
	 * 
	 * @param index
	 *            The index must be between 0 and length() - 1.
	 * @return The truth.
	 */
	public abstract boolean optBoolean(int index);

	/**
	 * Get the optional boolean value associated with an index. It returns the
	 * defaultValue if there is no value at that index or if it is not a Boolean
	 * or the String "true" or "false" (case insensitive).
	 * 
	 * @param index
	 *            The index must be between 0 and length() - 1.
	 * @param defaultValue
	 *            A boolean default.
	 * @return The truth.
	 */
	public abstract boolean optBoolean(int index, boolean defVal);

	/**
	 * Get the optional double value associated with an index. NaN is returned
	 * if there is no value for the index, or if the value is not a number and
	 * cannot be converted to a number.
	 * 
	 * @param index
	 *            The index must be between 0 and length() - 1.
	 * @return The value.
	 */
	public abstract double optDouble(int index);

	/**
	 * Get the optional double value associated with an index. The defaultValue
	 * is returned if there is no value for the index, or if the value is not a
	 * number and cannot be converted to a number.
	 * 
	 * @param index
	 *            subscript
	 * @param defaultValue
	 *            The default value.
	 * @return The value.
	 */
	public abstract double optDouble(int index, double defVal);

	/**
	 * Get the optional int value associated with an index. Zero is returned if
	 * there is no value for the index, or if the value is not a number and
	 * cannot be converted to a number.
	 * 
	 * @param index
	 *            The index must be between 0 and length() - 1.
	 * @return The value.
	 */
	public abstract int optInt(int index);

	/**
	 * Get the optional int value associated with an index. The defaultValue is
	 * returned if there is no value for the index, or if the value is not a
	 * number and cannot be converted to a number.
	 * 
	 * @param index
	 *            The index must be between 0 and length() - 1.
	 * @param defaultValue
	 *            The default value.
	 * @return The value.
	 */
	public abstract int optInt(int index, int defVal);

	/**
	 * Get the optional string value associated with an index. It returns an
	 * empty string if there is no value at that index. If the value is not a
	 * string and is not null, then it is coverted to a string.
	 * 
	 * @param index
	 *            The index must be between 0 and length() - 1.
	 * @return A String value.
	 */
	public abstract String optString(int index);

	/**
	 * Get the optional string associated with an index. The defaultValue is
	 * returned if the key is not found.
	 * 
	 * @param index
	 *            The index must be between 0 and length() - 1.
	 * @param defaultValue
	 *            The default value.
	 * @return A String value.
	 */
	public abstract String optString(int index, String defVal);

	/**
	 * Get the optional JSONArray associated with an index.
	 * 
	 * @param index
	 *            subscript
	 * @return A JSONArray value, or null if the index has no value, or if the
	 *         value is not a JSONArray.
	 */
	public abstract JSONArray optJSONArray(int index);

	/**
	 * Get the optional JSONObject associated with an index. Null is returned if
	 * the key is not found, or null if the index has no value, or if the value
	 * is not a JSONObject.
	 * 
	 * @param index
	 *            The index must be between 0 and length() - 1.
	 * @return A JSONObject value.
	 */
	public abstract JSONObject optJSONObject(int index);

	public abstract int length();

	public abstract JSONArray putBoolean(boolean b) throws JSONException;

	public abstract JSONArray putDouble(double d) throws JSONException;

	public abstract JSONArray putObject(Object o) throws JSONException;

	public abstract JSONArray putBoolean(int index, boolean b)
			throws JSONException;

	public abstract JSONArray putDouble(int index, double d)
			throws JSONException;

	public abstract JSONArray putObject(int index, Object o)
			throws JSONException;

	public abstract String toString();

	public interface ForEach {
		public void process(JSONObject obj);
	}

	public void forEach(ForEach each) throws JSONException {
		int len = length();
		for (int i = 0; i < len; i++) {
			each.process(getJSONObject(i));
		}
	}
}
