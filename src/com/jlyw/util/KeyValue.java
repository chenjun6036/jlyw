package com.jlyw.util;

/**
 * 一个结构体：键-值对
 */
public class KeyValue {
	public String m_keyName;
	public Object m_value;
	
	public KeyValue(String keyName,Object value){
		this.m_keyName=keyName;
		this.m_value=value;
	}
	public KeyValue(){
		
	}

}
