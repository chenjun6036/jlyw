package com.jlyw.util.xmlHandler;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;

/**
 * XML������������ͬ�ı�ǩ������
 * @author Administrator
 * ��ǩ��ֵ��Ϊһ����������ȡ
 */
public class ParseXMLAll {
	private static final String QNameValueInAttrName = CfgParserAll.QNameValueInAttrName;
	private Map<String, List<Attributes>> attrMap;	//�洢XML�б�ǩ�µ����ԣ�KeyΪ��ǩ��
	
	public ParseXMLAll(){
	}
	public ParseXMLAll(String filename) throws Exception{
		parse(filename);
	}
	public ParseXMLAll(InputStream in) throws Exception{
		parse(in);
	}
	public ParseXMLAll(File file) throws Exception{
		parse(file);
	}
	/**
	 * ��ȡ���еı�ǩ����
	 * @return ��ǩ���Ƶĵ�����
	 * @throws NullPointerException
	 */
	public Iterator<String> getKeyIterator() throws NullPointerException{
		if(this.attrMap == null){
			throw new NullPointerException("δָ��Ҫ������xml�ļ���");
		}
		return this.attrMap.keySet().iterator();
	}
	/**
	 * ��ȡ��index������Ϊkey�ı�ǩ��ֵ
	 * @param key
	 * @param index �ڼ�����ǩ����0��ʼ��
	 * @return
	 * @throws NullPointerException
	 */
	public String getProperty(String key, int index) throws Exception{
		return getProperty(key, index, null);
	}
	/**
	 * ��ȡ��index������Ϊkey�ı�ǩ��ֵ
	 * @param key
	 * @param index �ڼ�����ǩ����0��ʼ��
	 * @param defaultValue Ĭ��ֵ
	 * @return
	 * @throws Exception
	 */
	public String getProperty(String key, int index, String defaultValue) throws Exception{
		if(this.attrMap == null){
			throw new Exception("δָ��Ҫ������xml�ļ���");
		}
		if(this.attrMap.containsKey(key)){
			return attrMap.get(key).get(index).getValue(QNameValueInAttrName);
		}else{
			return defaultValue;
		}
	}
	/**
	 * ��ȡһ��XML��ǩ���ֵĴ���
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public int getQNameCount(Object key) throws Exception{
		if(this.attrMap == null){
			throw new Exception("δָ��Ҫ������xml�ļ���");
		}
		if(this.attrMap.containsKey(key)){
			return this.attrMap.get(key).size();
		}
		return 0;
	}
	/**
	 * ��ȡ��index������Ϊkey�ı�ǩ����������
	 * @param key ��xml��ǩ
	 * @param index �ڼ�����ǩ����0��ʼ��
	 * @return
	 * @throws Exception
	 */
	public Attributes getAttributes(Object key, int index) throws Exception{
		if(this.attrMap == null){
			throw new Exception("δָ��Ҫ������xml�ļ���");
		}
		return this.attrMap.get(key).get(index);
	}
	/**
	 * ��ȡ��index������Ϊkey�ı�ǩ��һ������ֵ
	 * @param key ��xml��ǩ
	 * @param qName ��������
	 * @param index �ڼ�����ǩ����0��ʼ��
	 * @return
	 * @throws Exception
	 */
	public String getAttribute(Object key, String qName, int index) throws Exception{
		if(this.attrMap == null){
			throw new Exception("δָ��Ҫ������xml�ļ���");
		}
		if(!this.attrMap.containsKey(key)){	//������ָ����key
			return null;
		}
		return this.attrMap.get(key).get(index).getValue(qName);
	}
	
	/**
	 * �����������ƺͶ�Ӧֵ���������б�
	 * @param key
	 * @param qName
	 * @param qValue
	 * @return
	 */
	public List<Attributes> getAttributesByPropertyValue(Object key, String qName, String qValue) throws Exception{
		if(this.attrMap == null){
			throw new Exception("δָ��Ҫ������xml�ļ���");
		}
		List<Attributes> retList = new ArrayList<Attributes>();
		if(this.attrMap.containsKey(key)){	//������ָ����key
			List<Attributes> attrList = this.attrMap.get(key);
			for(Attributes a : attrList){
				if(a.getValue(qName) != null && a.getValue(qName).equals(qValue)){
					retList.add(a);
				}
			}
		}
		return retList;
	}
	/**
	 * �����������ƺͶ�Ӧֵ���������б�
	 * @param key
	 * @param qName
	 * @param qValue
	 * @param qName2
	 * @param qValue2
	 * @return
	 */
	public List<Attributes> getAttributesByPropertyValues(Object key, String qName, String qValue, String qName2, String qValue2) throws Exception{
		if(this.attrMap == null){
			throw new Exception("δָ��Ҫ������xml�ļ���");
		}
		List<Attributes> retList = new ArrayList<Attributes>();
		if(this.attrMap.containsKey(key)){	//������ָ����key
			List<Attributes> attrList = this.attrMap.get(key);
			for(Attributes a : attrList){
				if(a.getValue(qName) != null && a.getValue(qName).equals(qValue) && 
						a.getValue(qName2) != null && a.getValue(qName2).equals(qValue2)){
					retList.add(a);
				}
			}
		}
		return retList;
	}
	/**
	 * ��ȡ��1������Ϊkey�ı�ǩ��һ������ֵ
	 * @param key ��xml��ǩ
	 * @param qName ��������
	 * @return
	 * @throws Exception
	 */
	public String getFirstAttribute(Object key, String qName) throws Exception{
		return getAttribute(key, qName, 0);
	}

	public void parse(String filename) throws Exception {
		// �����ǵĽ���������
		CfgParserAll handler = new CfgParserAll();

		// ��ȡSAX��������
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(false);
		factory.setValidating(false);

		// ��ȡSAX����
		SAXParser parser = factory.newSAXParser();

		// �õ������ļ�myenv.xml����Ŀ¼��tomcat������WEB-INF/classes
		URL confURL = ParseXMLAll.class.getClassLoader().getResource(filename);

		try {
			// ���������ͽ�������myenv.xml��ϵ��������ʼ����
			parser.parse(confURL.toString(), handler);
			// ��ȡ�����ɹ�������� �Ժ���������Ӧ�ó���ֻҪ���ñ������props�Ϳ�����ȡ���������ƺ�ֵ��
			this.attrMap = handler.getAttrMap();
		} finally {
			factory = null;
			parser = null;
			handler = null;
		}
	}
	
	public void parse(InputStream in) throws Exception {
		// �����ǵĽ���������
		CfgParserAll handler = new CfgParserAll();

		// ��ȡSAX��������
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(false);
		factory.setValidating(false);

		// ��ȡSAX����
		SAXParser parser = factory.newSAXParser();

		try {
			// ���������ͽ�������myenv.xml��ϵ��������ʼ����
			parser.parse(in, handler);
			// ��ȡ�����ɹ�������� �Ժ���������Ӧ�ó���ֻҪ���ñ������props�Ϳ�����ȡ���������ƺ�ֵ��
			this.attrMap = handler.getAttrMap();
		} finally {
			factory = null;
			parser = null;
			handler = null;
		}
	}
	
	public void parse(File file) throws Exception {
		// �����ǵĽ���������
		CfgParserAll handler = new CfgParserAll();

		// ��ȡSAX��������
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(false);
		factory.setValidating(false);

		// ��ȡSAX����
		SAXParser parser = factory.newSAXParser();

		try {
			// ���������ͽ�������myenv.xml��ϵ��������ʼ����
			parser.parse(file, handler);
			// ��ȡ�����ɹ�������� �Ժ���������Ӧ�ó���ֻҪ���ñ������props�Ϳ�����ȡ���������ƺ�ֵ��
			this.attrMap = handler.getAttrMap();
		} finally {
			factory = null;
			parser = null;
			handler = null;
		}
	}
	
	public static void main(String[] args){
		ParseXMLAll p;
		try {
			p = new ParseXMLAll("META-INF/system.cfg.xml");
			System.out.println(p.getProperty("mongodb-host", 0));
			System.out.println(p.getProperty("mongodb-port", 0));
			System.out.println(p.getProperty("mongodb-dbname", 0));
			System.out.println(p.getProperty("mongodb-collection-name", 0));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}