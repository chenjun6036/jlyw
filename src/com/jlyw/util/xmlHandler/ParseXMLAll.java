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
 * XML解析：名称相同的标签保存多次
 * @author Administrator
 * 标签的值作为一个属性来存取
 */
public class ParseXMLAll {
	private static final String QNameValueInAttrName = CfgParserAll.QNameValueInAttrName;
	private Map<String, List<Attributes>> attrMap;	//存储XML中标签下的属性，Key为标签名
	
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
	 * 获取所有的标签名称
	 * @return 标签名称的迭代器
	 * @throws NullPointerException
	 */
	public Iterator<String> getKeyIterator() throws NullPointerException{
		if(this.attrMap == null){
			throw new NullPointerException("未指定要解析的xml文件！");
		}
		return this.attrMap.keySet().iterator();
	}
	/**
	 * 获取第index个名称为key的标签的值
	 * @param key
	 * @param index 第几个标签（从0开始）
	 * @return
	 * @throws NullPointerException
	 */
	public String getProperty(String key, int index) throws Exception{
		return getProperty(key, index, null);
	}
	/**
	 * 获取第index个名称为key的标签的值
	 * @param key
	 * @param index 第几个标签（从0开始）
	 * @param defaultValue 默认值
	 * @return
	 * @throws Exception
	 */
	public String getProperty(String key, int index, String defaultValue) throws Exception{
		if(this.attrMap == null){
			throw new Exception("未指定要解析的xml文件！");
		}
		if(this.attrMap.containsKey(key)){
			return attrMap.get(key).get(index).getValue(QNameValueInAttrName);
		}else{
			return defaultValue;
		}
	}
	/**
	 * 获取一个XML标签出现的次数
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public int getQNameCount(Object key) throws Exception{
		if(this.attrMap == null){
			throw new Exception("未指定要解析的xml文件！");
		}
		if(this.attrMap.containsKey(key)){
			return this.attrMap.get(key).size();
		}
		return 0;
	}
	/**
	 * 获取第index个名称为key的标签的所有属性
	 * @param key ：xml标签
	 * @param index 第几个标签（从0开始）
	 * @return
	 * @throws Exception
	 */
	public Attributes getAttributes(Object key, int index) throws Exception{
		if(this.attrMap == null){
			throw new Exception("未指定要解析的xml文件！");
		}
		return this.attrMap.get(key).get(index);
	}
	/**
	 * 获取第index个名称为key的标签的一个属性值
	 * @param key ：xml标签
	 * @param qName ：属性名
	 * @param index 第几个标签（从0开始）
	 * @return
	 * @throws Exception
	 */
	public String getAttribute(Object key, String qName, int index) throws Exception{
		if(this.attrMap == null){
			throw new Exception("未指定要解析的xml文件！");
		}
		if(!this.attrMap.containsKey(key)){	//不包含指定的key
			return null;
		}
		return this.attrMap.get(key).get(index).getValue(qName);
	}
	
	/**
	 * 根据属性名称和对应值返回属性列表
	 * @param key
	 * @param qName
	 * @param qValue
	 * @return
	 */
	public List<Attributes> getAttributesByPropertyValue(Object key, String qName, String qValue) throws Exception{
		if(this.attrMap == null){
			throw new Exception("未指定要解析的xml文件！");
		}
		List<Attributes> retList = new ArrayList<Attributes>();
		if(this.attrMap.containsKey(key)){	//不包含指定的key
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
	 * 根据属性名称和对应值返回属性列表
	 * @param key
	 * @param qName
	 * @param qValue
	 * @param qName2
	 * @param qValue2
	 * @return
	 */
	public List<Attributes> getAttributesByPropertyValues(Object key, String qName, String qValue, String qName2, String qValue2) throws Exception{
		if(this.attrMap == null){
			throw new Exception("未指定要解析的xml文件！");
		}
		List<Attributes> retList = new ArrayList<Attributes>();
		if(this.attrMap.containsKey(key)){	//不包含指定的key
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
	 * 获取第1个名称为key的标签的一个属性值
	 * @param key ：xml标签
	 * @param qName ：属性名
	 * @return
	 * @throws Exception
	 */
	public String getFirstAttribute(Object key, String qName) throws Exception{
		return getAttribute(key, qName, 0);
	}

	public void parse(String filename) throws Exception {
		// 将我们的解析器对象化
		CfgParserAll handler = new CfgParserAll();

		// 获取SAX工厂对象
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(false);
		factory.setValidating(false);

		// 获取SAX解析
		SAXParser parser = factory.newSAXParser();

		// 得到配置文件myenv.xml所在目录。tomcat中是在WEB-INF/classes
		URL confURL = ParseXMLAll.class.getClassLoader().getResource(filename);

		try {
			// 将解析器和解析对象myenv.xml联系起来，开始解析
			parser.parse(confURL.toString(), handler);
			// 获取解析成功后的属性 以后我们其他应用程序只要调用本程序的props就可以提取出属性名称和值了
			this.attrMap = handler.getAttrMap();
		} finally {
			factory = null;
			parser = null;
			handler = null;
		}
	}
	
	public void parse(InputStream in) throws Exception {
		// 将我们的解析器对象化
		CfgParserAll handler = new CfgParserAll();

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
			this.attrMap = handler.getAttrMap();
		} finally {
			factory = null;
			parser = null;
			handler = null;
		}
	}
	
	public void parse(File file) throws Exception {
		// 将我们的解析器对象化
		CfgParserAll handler = new CfgParserAll();

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