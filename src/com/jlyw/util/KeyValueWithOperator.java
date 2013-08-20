package com.jlyw.util;

public class KeyValueWithOperator extends KeyValue {
	public String m_operator;  //Ëã·û£ºÈç>¡¢<¡¢=µÈµÈ
	
	public KeyValueWithOperator(){
		
	}
	public KeyValueWithOperator(String keyName,Object value,String operator){
		super(keyName,value);
		this.m_operator=operator;
	}
}
