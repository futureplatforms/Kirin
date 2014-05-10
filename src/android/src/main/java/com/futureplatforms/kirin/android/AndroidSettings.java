package com.futureplatforms.kirin.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.futureplatforms.kirin.dependencies.StaticDependencies.SettingsDelegate;

public class AndroidSettings implements SettingsDelegate
{
    SharedPreferences mSharedPreferences;
    Context context;
    
    public AndroidSettings(Context context)
	{
    	this.context = context;
    	mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
	}

	
	@Override
	public String get(String key)
	{
		return mSharedPreferences.getString(key, null);
	}
	
	@Override
	public void put(String key, String value)
	{
		Editor e = mSharedPreferences.edit();
		e.putString(key, value);
		e.apply();
	}


	@Override
	public void clear() {
		//mSharedPreferences.edit().clear().commit();
	}
	
}
