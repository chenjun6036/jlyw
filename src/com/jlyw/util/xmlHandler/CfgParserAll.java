package com.jlyw.util.xmlHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.sun.xml.internal.bind.util.AttributesImpl;

/**
 * XML解析，名称相同的标签都保存
 * 每个标签都是独立处理的，不处理XML标签上下级的关系
 * @author Administrator
 * 使用DefaultHandler的好处是不必陈列出所有方法
 * 标签的值作为一个属性来存取
 */
public class CfgParserAll extends DefaultHandler {

	public final static String QNameValueInAttrName = "_QNameValue";	//存储标签值的属性名
	
	private Map<String, List<Attributes>> attrMap;	//存储XML中标签下的属性，Key为标签名

	private StringBuffer currentValue = new StringBuffer();

	// 构建器初始化props
	public CfgParserAll() {
		this.attrMap = new HashMap<String, List<Attributes>>();
	}

	public Map<String, List<Attributes>> getAttrMap(){
		return this.attrMap;
	}

	// 定义开始解析元素的方法。 这里是将中的名称xxx提取出来。
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		currentValue.delete(0, currentValue.length());
		AttributesImpl attrs = new AttributesImpl(attributes);
		
		if(attrMap.containsKey(qName)){
			List<Attributes> list = attrMap.get(qName);
			list.add(attrs);
		}else{
			List<Attributes> list = new ArrayList<Attributes>();
			list.add(attrs);
			attrMap.put(qName, list);
		}
	}

	// 这里是将之间的值加入到currentValue
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		currentValue.append(ch, start, length);
	}

	// 在遇到结束后，将之前的名称和值一一对应保存在attrs中
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if(attrMap.containsKey(qName)){
			List<Attributes> list = attrMap.get(qName);
			for(int i = list.size() - 1; i >= 0; i--){	//倒序查找，最后一个没有值的即为当前标签的值
				AttributesImpl attrs = (AttributesImpl)list.get(i);
				if(attrs.getValue(QNameValueInAttrName) == null){
					attrs.addAttribute(uri, localName, QNameValueInAttrName, "", currentValue.toString().trim());	//第四个参数为Type
					break;
				}
			}
		}
		
		
		
	}

}