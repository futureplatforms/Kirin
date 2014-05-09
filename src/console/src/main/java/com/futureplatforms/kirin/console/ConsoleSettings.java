package com.futureplatforms.kirin.console;

import java.util.Map;

import com.futureplatforms.kirin.dependencies.StaticDependencies.SettingsDelegate;
import com.google.common.collect.Maps;

public class ConsoleSettings implements SettingsDelegate
{
    private Map<String, String> _Map = Maps.newHashMap();
	
	@Override
	public String get(String key)
	{
		return _Map.get(key);
	}
	
	@Override
	public void put(String key, String value)
	{
		System.out.println("SETTINGS: " + key + ": " + value);
		_Map.put(key, value);
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	} 
}
