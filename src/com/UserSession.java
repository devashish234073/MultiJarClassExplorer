package com;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.io.InputStream;

public class UserSession {
	
	private String sessionToken;
	private LinkedHashMap<String,InputStream> hm = new LinkedHashMap<String,InputStream>(); 
	
	public UserSession(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	
	public void addStream(String fName,InputStream is) {
		hm.put(fName, is);
	}
	
	public int getListSize() {
		return hm.size();
	}
	
	public Iterator<String> getFileNameIterator() {
		return hm.keySet().iterator();
	}
	
	public InputStream getInputStream(String fName) {
		return hm.get(fName);
	}
}
