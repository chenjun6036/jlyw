package com.jlyw.util.xmlHandler;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

/**
 * XML������������ͬ�ı�ǩֻ��������һ��
 * @author Administrator
 *
 */
public class ParseXML {

	// ����һ��Properties ������� dbhost dbuser dbpassword�ȵ�ֵ
	private Properties props;
	private HashMap<Object, Attributes> attrMap;	//props���ÿһ�ж�Ӧ�����ԣ�attrMap�е�Key��props�е�Key��ͬ
	
	public ParseXML(){
	}
	public ParseXML(String filename) throws Exception{
		parse(filename);
	}
	public ParseXML(InputStream in) throws Exception{
		parse(in);
	}
	public ParseXML(File file) throws Exception{
		parse(file);
	}
	/**
	 * ��ȡ���еı�ǩ����
	 * @return ��ǩ���Ƶĵ�����
	 * @throws NullPointerException
	 */
	public Iterator<Object> getKeyIterator() throws NullPointerException{
		if(this.props == null){
			throw new NullPointerException("δָ��Ҫ������xml�ļ���");
		}
		return this.props.keySet().iterator();
	}
	/**
	 * ��ȡһ��xml��ǩ��ֵ
	 * @param key
	 * @return
	 * @throws NullPointerException
	 */
	public String getProperty(String key) throws NullPointerException{
		if(this.props == null){
			throw new NullPointerException("δָ��Ҫ������xml�ļ���");
		}
		return this.props.getProperty(key);
	}
	/**
	 * ��ȡһ��xml��ǩ��ֵ
	 * @param key
	 * @param defaultValue
	 * @return
	 * @throws NullPointerException
	 */
	public String getProperty(String key, String defaultValue) throws NullPointerException{
		if(this.props == null){
			throw new NullPointerException("δָ��Ҫ������xml�ļ���");
		}
		return this.props.getProperty(key, defaultValue);
	}
	/**
	 * ��ȡһ��xml��ǩ����������
	 * @param key ��xml��ǩ
	 * @return
	 * @throws NullPointerException
	 */
	public Attributes getAttributes(Object key) throws NullPointerException{
		if(this.attrMap == null){
			throw new NullPointerException("δָ��Ҫ������xml�ļ���");
		}
		return this.attrMap.get(key);
	}
	/**
	 * ��ȡһ��xml��ǩ��һ������ֵ
	 * @param key ��xml��ǩ
	 * @param qName ��������
	 * @return
	 * @throws NullPointerException
	 */
	public String getAttribute(Object key, String qName) throws NullPointerException{
		if(this.attrMap == null){
			throw new NullPointerException("δָ��Ҫ������xml�ļ���");
		}
		if(!this.attrMap.containsKey(key)){	//������ָ����key
			return null;
		}
		return this.attrMap.get(key).getValue(qName);
	}

	public void parse(String filename) throws Exception {
		// �����ǵĽ���������
		ConfigParser handler = new ConfigParser();

		// ��ȡSAX��������
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(false);
		factory.setValidating(false);

		// ��ȡSAX����
		SAXParser parser = factory.newSAXParser();

		// �õ������ļ�myenv.xml����Ŀ¼��tomcat������WEB-INF/classes
		URL confURL = ParseXML.class.getClassLoader().getResource(filename);

		try {
			// ���������ͽ�������myenv.xml��ϵ��������ʼ����
			parser.parse(confURL.toString(), handler);
			// ��ȡ�����ɹ�������� �Ժ���������Ӧ�ó���ֻҪ���ñ������props�Ϳ�����ȡ���������ƺ�ֵ��
			this.props = handler.getProps();
			this.attrMap = handler.getAttrMap();
		} finally {
			factory = null;
			parser = null;
			handler = null;
		}
	}
	
	public void parse(InputStream in) throws Exception {
		// �����ǵĽ���������
		ConfigParser handler = new ConfigParser();

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
			this.props = handler.getProps();
			this.attrMap = handler.getAttrMap();
		} finally {
			factory = null;
			parser = null;
			handler = null;
		}
	}
	
	public void parse(File file) throws Exception {
		// �����ǵĽ���������
		ConfigParser handler = new ConfigParser();

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
			this.props = handler.getProps();
			this.attrMap = handler.getAttrMap();
		} finally {
			factory = null;
			parser = null;
			handler = null;
		}
	}
	
	public static void main(String[] args){
		ParseXML p;
		try {
			p = new ParseXML("system.cfg.xml");
			System.out.println(p.getProperty("mongodb-host"));
			System.out.println(p.getProperty("mongodb-port"));
			System.out.println(p.getProperty("mongodb-dbname"));
			System.out.println(p.getProperty("mongodb-collection-name"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	

}