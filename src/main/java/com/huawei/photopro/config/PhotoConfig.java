package com.huawei.photopro.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

		@Data
		@Component  
		@ConfigurationProperties(prefix="myProps") //接收application.yml中的myProps下面的属性  
		public class PhotoConfig {  
		    private String filepath;  
		    
		    private String cdnPath; 
		    
		    private String[] arrayProps;  
		    
		    private List<Map<String, String>> listProp1 = new ArrayList(); //接收prop1里面的属性值  
		    
		    private List<String> listProp2 = new ArrayList(); //接收prop2里面的属性值  
		    
		    private Map<String, String> mapProps = new HashMap(); //接收prop1里面的属性值  
		    
		}  

