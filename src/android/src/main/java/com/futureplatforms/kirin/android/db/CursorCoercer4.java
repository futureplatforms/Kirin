package com.futureplatforms.kirin.android.db;

import org.json.JSONException;
import org.json.JSONObject;

import android.database.AbstractWindowedCursor;

public class CursorCoercer4 implements CursorCoercer {
    public JSONObject coerceToJSONObject(String[] cols, AbstractWindowedCursor c) {
        JSONObject obj = new JSONObject();
        for (int i = 0; i < cols.length; i++) {
            String name = cols[i];

            if (c.isNull(i)) {
                continue;
            }
            String str = null;
            try {
                str = c.getString(i);
            } catch (Exception e) {
                // not a string
            }
            Object res = null;
            try {
                if (res == null) {
                    res = Long.parseLong(str);
                }
            } catch (Exception e) {
                // wasn't a long!
            }
            try {
                if (res == null) {
                    res = Double.parseDouble(str);
                }
            } catch (Exception e) {
                // wasn't a double!
            }
            if (res == null) {
                res = str;
            }

            if (res != null) {
                try {
                    obj.putOpt(name, res);
                } catch (JSONException e) {
                }
            }
        }
        return obj;
    }
}
