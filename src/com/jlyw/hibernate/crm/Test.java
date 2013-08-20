/**
 * 
 */
package com.jlyw.hibernate.crm;
import java.io.*;

import java.util.*;

import org.dom4j.*;

import org.dom4j.io.*;
/**
 * @author xx
 *
 */
public class Test {


	/**
	 * @param arge
	 */
	public static void main(String arge[]) {

	long lasting = System.currentTimeMillis();

	try {
		
		/*SAXReader saxReader = new SAXReader();
        saxReader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        saxReader.setEncoding("UTF-8");
        document = saxReader.read(file);*/

		//System.out.println(System.getProperty("user.dir"));
	//File f = new File(System.getProperty("user.dir")+"\\src\\hibernate.cfg.xml");
	File f = new File("D:\\CRM\\jlyw\\src\\META-INF\\Preference.xml");

	SAXReader reader = new SAXReader();

	reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
	
	Document doc = reader.read(f);

	Element root = doc.getRootElement();

	Element foo,fo,o;

	/*for (Iterator i = root.elementIterator("VALUE"); i.hasNext();) {

	foo = (Element) i.next();

	System.out.print("车牌号码:" + foo.elementText("NO"));

	System.out.println("车主地址:" + foo.elementText("ADDR"));

	System.out.println("运行时间：" + (System.currentTimeMillis() - lasting)+ "毫秒");*/
	
	/*for (Iterator i = root.elementIterator("Theta"); i.hasNext();) {

		foo = (Element) i.next();
		 
		if(foo.getQualifiedName().equals("Theta"))
		{
			for (Iterator j = foo.elementIterator(); j.hasNext();)
			{
				fo=(Element)j.next();
				if(fo.getQualifiedName().equals("property"))
				{
					for(Iterator a=fo.attributeIterator();a.hasNext();)
					{
						Attribute attribute=(Attribute) a.next();
						if(attribute.getValue().equals("theta1"))
						System.out.println("connection.url:" + fo.getTextTrim());
						
						else if(attribute.getValue().equals("theta2"))
							System.out.println("connection.driver_class:" + fo.getTextTrim());
						
						else if(attribute.getValue().equals("theta3"))
						{
							System.out.println("connection.username:" + fo.getTextTrim());
							break;
						}
					}
				}
			}
		}
	}*/
	for (Iterator i = root.elementIterator(); i.hasNext();) {

		foo = (Element) i.next();
		 
		if(foo.getQualifiedName().equals("Theta"))
		{
			for (Iterator j = foo.elementIterator(); j.hasNext();)
			{
				fo=(Element)j.next();
				if(fo.getQualifiedName().equals("property"))
				{
					for(Iterator a=fo.attributeIterator();a.hasNext();)
					{
						Attribute attribute=(Attribute) a.next();
						if(attribute.getValue().equals("theta1"))
						{
							fo.setText("~~");
						System.out.println("set theta1:");
						}
						
						else if(attribute.getValue().equals("theta2"))
							System.out.println("connection.driver_class:" + fo.getTextTrim());
						
						else if(attribute.getValue().equals("theta3"))
						{
							System.out.println("connection.username:" + fo.getTextTrim());
							break;
						}
					}
				}
			}
		}
		else if(foo.getQualifiedName().equals("LevelPer"))
		{
			for (Iterator j = foo.elementIterator(); j.hasNext();)
			{
				fo=(Element)j.next();
				if(fo.getQualifiedName().equals("property"))
				{
					for(Iterator a=fo.attributeIterator();a.hasNext();)
					{
						Attribute attribute=(Attribute) a.next();
						if(attribute.getValue().equals("level1"))
							System.out.println("connection.url:" + fo.getTextTrim());
						
						else if(attribute.getValue().equals("level2"))
							System.out.println("connection.driver_class:" + fo.getTextTrim());
						else if(attribute.getValue().equals("level3"))
							System.out.println("connection.username:" + fo.getTextTrim());
						else if(attribute.getValue().equals("level4"))
							System.out.println("connection.driver_class:" + fo.getTextTrim());
						else if(attribute.getValue().equals("level5"))
							System.out.println("connection.driver_class:" + fo.getTextTrim());
						else if(attribute.getValue().equals("level6"))
							System.out.println("connection.driver_class:" + fo.getTextTrim());
						else if(attribute.getValue().equals("level7"))
							System.out.println("connection.driver_class:" + fo.getTextTrim());
					}
				}
			}
		}
	}
	OutputFormat format = OutputFormat.createPrettyPrint();
	format.setEncoding("GBK");
	XMLWriter output = new XMLWriter(new FileWriter( new File("D:\\CRM\\jlyw\\src\\META-INF\\Preference.xml") ),format);
	output.write( doc );
	output.close();
	}catch(Exception e)
	{
		e.printStackTrace();
	}

	}
}
