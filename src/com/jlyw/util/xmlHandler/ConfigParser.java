package com.jlyw.util.xmlHandler;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.SAXException;

import com.sun.xml.internal.bind.util.AttributesImpl;

import java.util.HashMap;
import java.util.Properties;

/**
 * XML������������ͬ�ı�ǩֻ�������һ��
 * ÿ����ǩ���Ƕ�������ģ�������XML��ǩ���¼��Ĺ�ϵ
 * @author Administrator
 * ʹ��DefaultHandler�ĺô��ǲ��س��г����з���
 */
public class ConfigParser extends DefaultHandler {

	// //����һ��Properties �������dbhost dbuser dbpassword��ֵ
	private Properties props;
	private HashMap<Object, Attributes> attrMap;	//props���ÿһ�ж�Ӧ�����ԣ�attrMap�е�Key��props�е�Key��ͬ

//	private String currentSet;
//	private String currentName;
	private StringBuffer currentValue = new StringBuffer();

	// ��������ʼ��props
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

	// ���忪ʼ����Ԫ�صķ����� �����ǽ��е�����xxx��ȡ������
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		currentValue.delete(0, currentValue.length());
		Attributes attrs = new AttributesImpl(attributes);
		attrMap.put(qName, attrs);
//		this.currentName = qName;
	}

	// �����ǽ�֮���ֵ���뵽currentValue
	public void characters(char[] ch, int start, int length)
			throws SAXException {

		currentValue.append(ch, start, length);

	}

	// �����������󣬽�֮ǰ�����ƺ�ֵһһ��Ӧ������props��

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		props.put(qName, currentValue.toString().trim());
	}

}