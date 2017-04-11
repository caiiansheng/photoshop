package com.huawei.photopro.controller;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.huawei.photopro.utils.Util;

import net.minidev.json.JSONObject;

@Controller
public class RemotePhoto {
	
	private static final Logger LOG=Logger.getLogger(RemotePhoto.class);
	
	@RequestMapping("/photoshop")
	public void remoteProcess(HttpServletRequest req,HttpServletResponse res, @RequestParam("url") String url,@RequestParam("key") String key){
		Map<String, String> map=Util.getEnv();
//		Map<String, String> map=new HashMap<>();
//		map.put("ps.correctionserver", "http://10.130.184.68:8080/api/v1/Correction");
//		map.put("ps.watermarkserver", "http://10.178.67.150:5000/watermark");
//		map.put("ps.roundedCorner", "http://10.130.187.187:8888/showImg");
		//String param="url="+url;
		JSONObject jo=new JSONObject();
		jo.put("url", url);
		try{
    	URL urll = new URL(map.get(key));
        //打开链接  
        HttpURLConnection conn = (HttpURLConnection)urll.openConnection();  
        conn.setDoOutput(true);   //需要输出
        conn.setDoInput(true);   //需要输入
        conn.setUseCaches(false);  //不允许缓存
        //设置请求方式为"GET"  
        conn.setRequestMethod("POST");  
        //超时响应时间为5秒  
        conn.setConnectTimeout(10 * 1000);  
        conn.setRequestProperty("Content-Type",   "application/json");  
        conn.connect();
        OutputStreamWriter dos = new OutputStreamWriter(conn.getOutputStream());
        dos.write(jo.toString());
        dos.flush();
        dos.close();
        int resultCode=conn.getResponseCode();
        if(HttpURLConnection.HTTP_OK==resultCode){
	        //通过输入流获取图片数据  
	        InputStream inStream = conn.getInputStream();  
	        byte[] data = readInputStream(inStream);  
	        OutputStream os = null;
			inStream.close();
			os = res.getOutputStream();
			os.write(data);
			os.flush();
			os.close();
        }else{
        	LOG.warn("request url "+urll+" exception", null);
        }
		}catch(Exception e){
			
			e.printStackTrace();
		}
		
	}
	   private static byte[] readInputStream(InputStream inStream) throws Exception{  
	        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
	        byte[] buffer = new byte[1024];  
	        int len = 0;  
	        while( (len=inStream.read(buffer)) != -1 ){  
	            outStream.write(buffer, 0, len);  
	        }  
	        inStream.close();  
	        return outStream.toByteArray();  
	    }
	   
	
	   
}
