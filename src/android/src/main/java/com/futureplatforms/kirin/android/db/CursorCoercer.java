package com.futureplatforms.kirin.android.db;

import org.json.JSONObject;

import android.database.AbstractWindowedCursor;

public interface CursorCoercer {

    public abstract JSONObject coerceToJSONObject(String[] cols, AbstractWindowedCursor c);

}