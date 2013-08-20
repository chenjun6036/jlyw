package com.jlyw.util.xmlHandler;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.SAXException;

import com.sun.xml.internal.bind.util.AttributesImpl;

import java.util.HashMap;
import java.util.Properties;

/**
 * XML解析，名称相同的标签只保存最后一个
 * 每个标签都是独立处理的，不处理XML标签上下级的关系
 * @author Administrator
 * 使用DefaultHandler的好处是不必陈列出所有方法
 */
public class ConfigParser extends DefaultHandler {

	// //定义一个Properties 用来存放dbhost dbuser dbpassword的值
	private Properties props;
	private HashMap<Object, Attributes> attrMap;	//props里的每一列对应的属性，attrMap中的Key与props中的Key相同

//	private String currentSet;
//	private String currentName;
	private StringBuffer currentValue = new StringBuffer();

	// 构建器初始化props
	public ConfigParser() {
		this.props = new Properties();
		this.attrMap = new HashMap<Object, Attributes>();
	}

	public Properties getProps() {
		return this.props;
	}
	public HashMap<Object, Attributes> getAttrMap(){
		return this.attrMap;
	}

	// 定义开始解析元素的方法。 这里是将中的名称xxx提取出来。
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		currentValue.delete(0, currentValue.length());
		Attributes attrs = new AttributesImpl(attributes);
		attrMap.put(qName, attrs);
//		this.currentName = qName;
	}

	// 这里是将之间的值加入到currentValue
	public void characters(char[] ch, int start, int length)
			throws SAXException {

		currentValue.append(ch, start, length);

	}

	// 在遇到结束后，将之前的名称和值一一对应保存在props中

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		props.put(qName, currentValue.toString().trim());
	}

}