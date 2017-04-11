package com.huawei.photopro.utils;

import java.util.HashMap;
import java.util.Map;

public class Util {
	
	public static  Map<String, String> getEnv(){
		Map<String, String> map=System.getenv();
		Map<String, String> ret=new HashMap<String, String>();
		  for (String key : map.keySet()) {
			  if(key.startsWith("ps"))
				  ret.put(key, map.get(key));
			  }
		  return ret;
	}
	
	public static  int getEnvCount(){
		return getEnv().size();
	}

}
