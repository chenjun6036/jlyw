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
 * XML解析：名称相同的标签只保存最后的一次
 * @author Administrator
 *
 */
public class ParseXML {

	// 定义一个Properties 用来存放 dbhost dbuser dbpassword等的值
	private Properties props;
	private HashMap<Object, Attributes> attrMap;	//props里的每一列对应的属性，attrMap中的Key与props中的Key相同
	
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
	 * 获取所有的标签名称
	 * @return 标签名称的迭代器
	 * @throws NullPointerException
	 */
	public Iterator<Object> getKeyIterator() throws NullPointerException{
		if(this.props == null){
			throw new NullPointerException("未指定要解析的xml文件！");
		}
		return this.props.keySet().iterator();
	}
	/**
	 * 获取一个xml标签的值
	 * @param key
	 * @return
	 * @throws NullPointerException
	 */
	public String getProperty(String key) throws NullPointerException{
		if(this.props == null){
			throw new NullPointerException("未指定要解析的xml文件！");
		}
		return this.props.getProperty(key);
	}
	/**
	 * 获取一个xml标签的值
	 * @param key
	 * @param defaultValue
	 * @return
	 * @throws NullPointerException
	 */
	public String getProperty(String key, String defaultValue) throws NullPointerException{
		if(this.props == null){
			throw new NullPointerException("未指定要解析的xml文件！");
		}
		return this.props.getProperty(key, defaultValue);
	}
	/**
	 * 获取一个xml标签的所有属性
	 * @param key ：xml标签
	 * @return
	 * @throws NullPointerException
	 */
	public Attributes getAttributes(Object key) throws NullPointerException{
		if(this.attrMap == null){
			throw new NullPointerException("未指定要解析的xml文件！");
		}
		return this.attrMap.get(key);
	}
	/**
	 * 获取一个xml标签的一个属性值
	 * @param key ：xml标签
	 * @param qName ：属性名
	 * @return
	 * @throws NullPointerException
	 */
	public String getAttribute(Object key, String qName) throws NullPointerException{
		if(this.attrMap == null){
			throw new NullPointerException("未指定要解析的xml文件！");
		}
		if(!this.attrMap.containsKey(key)){	//不包含指定的key
			return null;
		}
		return this.attrMap.get(key).getValue(qName);
	}

	public void parse(String filename) throws Exception {
		// 将我们的解析器对象化
		ConfigParser handler = new ConfigParser();

		// 获取SAX工厂对象
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(false);
		factory.setValidating(false);

		// 获取SAX解析
		SAXParser parser = factory.newSAXParser();

		// 得到配置文件myenv.xml所在目录。tomcat中是在WEB-INF/classes
		URL confURL = ParseXML.class.getClassLoader().getResource(filename);

		try {
			// 将解析器和解析对象myenv.xml联系起来，开始解析
			parser.parse(confURL.toString(), handler);
			// 获取解析成功后的属性 以后我们其他应用程序只要调用本程序的props就可以提取出属性名称和值了
			this.props = handler.getProps();
			this.attrMap = handler.getAttrMap();
		} finally {
			factory = null;
			parser = null;
			handler = null;
		}
	}
	
	public void parse(InputStream in) throws Exception {
		// 将我们的解析器对象化
		ConfigParser handler = new ConfigParser();

		// 获取SAX工厂对象
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(false);
		factory.setValidating(false);

		// 获取SAX解析
		SAXParser parser = factory.newSAXParser();

		try {
			// 将解析器和解析对象myenv.xml联系起来，开始解析
			parser.parse(in, handler);
			// 获取解析成功后的属性 以后我们其他应用程序只要调用本程序的props就可以提取出属性名称和值了
			this.props = handler.getProps();
			this.attrMap = handler.getAttrMap();
		} finally {
			factory = null;
			parser = null;
			handler = null;
		}
	}
	
	public void parse(File file) throws Exception {
		// 将我们的解析器对象化
		ConfigParser handler = new ConfigParser();

		// 获取SAX工厂对象
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(false);
		factory.setValidating(false);

		// 获取SAX解析
		SAXParser parser = factory.newSAXParser();

		try {
			// 将解析器和解析对象myenv.xml联系起来，开始解析
			parser.parse(file, handler);
			// 获取解析成功后的属性 以后我们其他应用程序只要调用本程序的props就可以提取出属性名称和值了
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