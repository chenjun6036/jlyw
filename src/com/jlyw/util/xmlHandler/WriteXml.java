package com.jlyw.util.xmlHandler;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class WriteXml {
	private static SAXTransformerFactory fac = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
	private TransformerHandler handler = null;
	private OutputStream outStream = null;
	private String fileName;
	private AttributesImpl atts;
	private String rootElement;

	public WriteXml(String fileName, String rootElement) {
		this.fileName = fileName;
		this.rootElement = rootElement;
		init();
	}

	public void init() {
		try {
			handler = fac.newTransformerHandler();
			Transformer transformer = handler.getTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");//����������õı��뷽ʽ
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");// �Ƿ��Զ���Ӷ���Ŀհ�
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");// �Ƿ����xml����

			outStream = new FileOutputStream(fileName);
			Result resultxml = new StreamResult(outStream);
			handler.setResult(resultxml);

			atts = new AttributesImpl();

			startDocument();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * �ĵ���ʼ
	 */
	private void startDocument() {
		try {
			handler.startDocument();
			handler.startElement("", "", rootElement, atts);
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}
	/**
	 * �ĵ�����ʱ����
	 */
	public void endDocument() {
		try {
			handler.endElement("", "", rootElement);
			handler.endDocument();// �ĵ�����,ͬ��������
			outStream.close();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * �²���һ����ǩ����endElement��Ӧ��
	 * @param attrMap����ǩ����
	 * @param objectElement:��ǩ��
	 * @throws SAXException
	 */
	public void startElement(HashMap<String, String> attrMap, String objectElement) throws SAXException{
		atts.clear();
		if(attrMap != null){
			Set<String> attrKeys = attrMap.keySet();
			Iterator<String> attrIt = attrKeys.iterator();
			while(attrIt.hasNext()){
				String key = (String) attrIt.next();
				String value = attrMap.get(key);
				atts.addAttribute("", "", key, "String", value);
			}
		}
		if (objectElement != null) {
			handler.startElement("", "", objectElement, atts);
		}
	}
	/**
	 * ����һ����ǩ����startElement��Ӧ��
	 * @param objectElement
	 * @throws SAXException
	 */
	public void endElement(String objectElement) throws SAXException{
		if (objectElement != null) {
			handler.endElement("", "", objectElement);
		}
	}
	
	/**
	 * ����һ����ǩ���ñ�ǩ��û���ӱ�ǩ
	 * @param attrMap����ǩ����
	 * @param objectElement����ǩ��
	 * @throws SAXException
	 */
	public void writeElement(HashMap<String, String> attrMap, String objectElement) throws SAXException{
		atts.clear();
		if(attrMap != null){
			Set<String> attrKeys = attrMap.keySet();
			Iterator<String> attrIt = attrKeys.iterator();
			while(attrIt.hasNext()){
				String key = (String) attrIt.next();
				String value = attrMap.get(key);
				atts.addAttribute("", "", key, "String", value);
			}
		}
		if (objectElement != null) {
			handler.startElement("", "", objectElement, atts);
			handler.endElement("", "", objectElement);
		}
	}
	

	public static void main(String[] args) {
		WriteXml xml = new WriteXml("c:/original-record.xml", "original-record");
		try {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("description", "ԭʼ��¼���ڹ����������;sheetName���幤��������ƣ�");
			map.put("sheetName", "sheet1");
	
			xml.writeElement(map, "original-record-sheet");

			map.clear();
			xml.startElement(map, "field-definition");
				map.clear();
				map.put("id", "20050506");
				map.put("name", "songdandan");
				map.put("age", "20");
				map.put("classes", "Act052");
				xml.writeElement(map, "Customer");
			xml.endElement("field-definition");
			
			
			
			
			map.clear();
			map.put("sheetName", "֤��Сģ��");
			map.put("region", "A1:H10");
			map.put("description", "�����ԭʼ��¼excel������֤�������;sheetName���幤��������ƣ�region���������磺A1:H10��");
			
			xml.writeElement(map, "certificate-sheet");

			xml.endDocument();
			System.out.println("д��ɹ�");
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}
}
