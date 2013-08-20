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
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");//设置输出采用的编码方式
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");// 是否自动添加额外的空白
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");// 是否忽略xml声明

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
	 * 文档开始
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
	 * 文档结束时调用
	 */
	public void endDocument() {
		try {
			handler.endElement("", "", rootElement);
			handler.endDocument();// 文档结束,同步到磁盘
			outStream.close();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 新插入一个标签（与endElement对应）
	 * @param attrMap：标签属性
	 * @param objectElement:标签名
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
	 * 结束一个标签（与startElement对应）
	 * @param objectElement
	 * @throws SAXException
	 */
	public void endElement(String objectElement) throws SAXException{
		if (objectElement != null) {
			handler.endElement("", "", objectElement);
		}
	}
	
	/**
	 * 新增一个标签，该标签下没有子标签
	 * @param attrMap：标签属性
	 * @param objectElement：标签名
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
			map.put("description", "原始记录所在工作表的名称;sheetName定义工作表的名称；");
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
			map.put("sheetName", "证书小模板");
			map.put("region", "A1:H10");
			map.put("description", "定义该原始记录excel中用于证书的区域;sheetName定义工作表的名称；region定义区域，如：A1:H10；");
			
			xml.writeElement(map, "certificate-sheet");

			xml.endDocument();
			System.out.println("写入成功");
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}
}
