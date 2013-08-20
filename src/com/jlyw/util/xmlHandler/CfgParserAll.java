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
 * XML������������ͬ�ı�ǩ������
 * ÿ����ǩ���Ƕ�������ģ�������XML��ǩ���¼��Ĺ�ϵ
 * @author Administrator
 * ʹ��DefaultHandler�ĺô��ǲ��س��г����з���
 * ��ǩ��ֵ��Ϊһ����������ȡ
 */
public class CfgParserAll extends DefaultHandler {

	public final static String QNameValueInAttrName = "_QNameValue";	//�洢��ǩֵ��������
	
	private Map<String, List<Attributes>> attrMap;	//�洢XML�б�ǩ�µ����ԣ�KeyΪ��ǩ��

	private StringBuffer currentValue = new StringBuffer();

	// ��������ʼ��props
	public CfgParserAll() {
		this.attrMap = new HashMap<String, List<Attributes>>();
	}

	public Map<String, List<Attributes>> getAttrMap(){
		return this.attrMap;
	}

	// ���忪ʼ����Ԫ�صķ����� �����ǽ��е�����xxx��ȡ������
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

	// �����ǽ�֮���ֵ���뵽currentValue
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		currentValue.append(ch, start, length);
	}

	// �����������󣬽�֮ǰ�����ƺ�ֵһһ��Ӧ������attrs��
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if(attrMap.containsKey(qName)){
			List<Attributes> list = attrMap.get(qName);
			for(int i = list.size() - 1; i >= 0; i--){	//������ң����һ��û��ֵ�ļ�Ϊ��ǰ��ǩ��ֵ
				AttributesImpl attrs = (AttributesImpl)list.get(i);
				if(attrs.getValue(QNameValueInAttrName) == null){
					attrs.addAttribute(uri, localName, QNameValueInAttrName, "", currentValue.toString().trim());	//���ĸ�����ΪType
					break;
				}
			}
		}
		
		
		
	}

}