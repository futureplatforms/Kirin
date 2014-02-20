package com.futureplatforms.kirin.android.db;

import org.json.JSONException;
import org.json.JSONObject;

import android.database.AbstractWindowedCursor;

public class CursorCoercer5 implements CursorCoercer {
    /* (non-Javadoc)
     * @see com.futureplatforms.android.jscore.fragmentation.CursorCoercer#coerceToJSONObject(java.lang.String[], android.database.AbstractWindowedCursor)
     */
    @SuppressWarnings("deprecation")
	@Override
    public JSONObject coerceToJSONObject(String[] cols, AbstractWindowedCursor c) {
        JSONObject obj = new JSONObject();
        for (int i = 0; i < cols.length; i++) {
            String name = cols[i];
            // do we have to worry about types?
            // if we do, then we need the CursorWindow.

            // TODO we can make this faster for SDK > 5.
            // TODO have a separate class depending on SDK.
            try {
                if (c.isString(i)) {
                    obj.putOpt(name, c.getString(i));
                } else if (c.isLong(i)) {
                    obj.put(name, c.getLong(i));
                } else if (c.isFloat(i)) {
                    obj.put(name, c.getDouble(i));
                } else if (c.isNull(i)) {
                    obj.remove(name);
                }
            } catch (JSONException e) {
            }
        }
        return obj;
    }
}
