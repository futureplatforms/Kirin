package com.futureplatforms.kirin.gwt.client.services;

import com.futureplatforms.kirin.dependencies.StaticDependencies;
import com.futureplatforms.kirin.dependencies.json.JSONDelegate;
import com.futureplatforms.kirin.dependencies.json.JSONException;
import com.futureplatforms.kirin.dependencies.json.JSONObject;
import com.futureplatforms.kirin.gwt.client.KirinService;
import com.futureplatforms.kirin.gwt.client.services.natives.GwtSettingsServiceNative;
import com.futureplatforms.kirin.gwt.compile.NoBind;
import com.google.gwt.core.client.GWT;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.NoExport;

import java.util.*;

/**
 * Created by douglashoskins on 27/07/2015.
 */
@Export(value = "GwtSettingsService", all = true)
@ExportPackage("screens")
public class GwtSettingsService extends KirinService<GwtSettingsServiceNative> {
    private static GwtSettingsService _Instance;

    private static final JSONDelegate json = StaticDependencies.getInstance().getJsonDelegate();

    @NoBind
    @NoExport
    public static GwtSettingsService BACKDOOR() { return _Instance; }

    private Map<String, String> _keyValuePairs;
    private List<String> _DeletedKeys;

    public GwtSettingsService() {
        super(GWT.<GwtSettingsServiceNative>create(GwtSettingsServiceNative.class));
        _keyValuePairs = new HashMap<>();
        _DeletedKeys = new ArrayList<>();
        _Instance = this;
    }

    public void mergeOrOverwrite(String settingsJson) {
        StaticDependencies.getInstance().getLogDelegate().log("MergeOrOverwrite " + settingsJson);
        try {
            JSONObject obj = json.getJSONObject(settingsJson);
            Iterator<String> it = obj.keys();
            while (it.hasNext()) {
                String key = it.next();
                _keyValuePairs.put(key, obj.getString(key));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @NoExport
    public void _commit() {
        String[] keys = new String[_keyValuePairs.size()];
        String[] vals = new String[_keyValuePairs.size()];
        int i=0;
        for (Map.Entry<String, String> entry : _keyValuePairs.entrySet()) {
            keys[i] = entry.getKey();
            vals[i] = entry.getValue();
            i++;
        }

        String[] dels = _DeletedKeys.toArray(new String[0]);

        getNativeObject().updateContents(keys, vals, dels);
    }

    private String makeKey(String key) {
        key = key.replace(':', '_');
        return key.replace('/', '_');
    }

    @NoExport
    public String _get(String key) {
        key = makeKey(key);
        if (_keyValuePairs.containsKey(key)) {
            return _keyValuePairs.get(key);
        }
        return null;
    }

    @NoExport
    public void _put(String key, String val) {
        key = makeKey(key);
        if (val == null) {
            _remove(key);
        } else {
            _keyValuePairs.put(key, val);
        }
    }

    @NoExport
    public void _remove(String key) {
        key = makeKey(key);
        _DeletedKeys.add(key);
        _keyValuePairs.remove(key);
    }

    @NoExport
    public void _clear() {
        _keyValuePairs.clear();
        getNativeObject().clear();
    }
}
