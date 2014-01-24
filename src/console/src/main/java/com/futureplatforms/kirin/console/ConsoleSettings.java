package com.futureplatforms.kirin.console;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.futureplatforms.kirin.dependencies.StaticDependencies.SettingsDelegate;
import com.google.common.collect.Maps;

public class ConsoleSettings implements SettingsDelegate
{
    private Map<String, String> _Map = Maps.newHashMap();
    
    public ConsoleSettings() {
    	try {
			FileInputStream fis = new FileInputStream(new File("console.settings"));
			DataInputStream dis = new DataInputStream(fis);
			while (dis.available() > 0) {
				_Map.put(dis.readUTF(), dis.readUTF());
			}
			dis.close();
			fis.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
    	
    }
    
	@Override
	public String get(String key) {
		return _Map.get(key);
	}
	
	@Override
	public void put(String key, String value) {
		_Map.put(key, value);
		
		try {
			FileOutputStream fos = new FileOutputStream(new File("console.settings"));
			DataOutputStream dos = new DataOutputStream(fos);
			Set<Entry<String, String>> entries = _Map.entrySet();
			for (Entry<String, String> entry : entries) {
				dos.writeUTF(entry.getKey());
				dos.writeUTF(entry.getValue());
			}
			dos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
