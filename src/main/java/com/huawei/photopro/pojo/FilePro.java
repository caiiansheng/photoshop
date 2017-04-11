package com.huawei.photopro.pojo;

import lombok.Data;

@Data
public class FilePro {
    private String name;
    private int size;
    public FilePro(String name,int size){
    	this.name=name;
    	this.size=size;
    }

    
}
