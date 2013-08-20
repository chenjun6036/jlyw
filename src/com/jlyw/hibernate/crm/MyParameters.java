/**
 * 
 */
package com.jlyw.hibernate.crm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.SAXException;

/**
 * @author xx
 *
 */
public class MyParameters {
	private Double theta1;
	private Double theta2;
	private Double theta3;
	
	private Double levelPercentage1;
	private Double levelPercentage2;
	private Double levelPercentage3;
	private Double levelPercentage4;
	private Double levelPercentage5;
	private Double levelPercentage6;
	private Double levelPercentage7;
	private String path = "META-INF/Preference.xml";
	public Double getTheta1() {
		return theta1;
	}
	public void setTheta1(Double theta1) {
		this.theta1 = theta1;
	}
	public Double getTheta2() {
		return theta2;
	}
	public void setTheta2(Double theta2) {
		this.theta2 = theta2;
	}
	public Double getTheta3() {
		return theta3;
	}
	public void setTheta3(Double theta3) {
		this.theta3 = theta3;
	}
	public Double getLevelPercentage1() {
		return levelPercentage1;
	}
	public void setLevelPercentage1(Double levelPercentage1) {
		this.levelPercentage1 = levelPercentage1;
	}
	public Double getLevelPercentage2() {
		return levelPercentage2;
	}
	public void setLevelPercentage2(Double levelPercentage2) {
		this.levelPercentage2 = levelPercentage2;
	}
	public Double getLevelPercentage3() {
		return levelPercentage3;
	}
	public void setLevelPercentage3(Double levelPercentage3) {
		this.levelPercentage3 = levelPercentage3;
	}
	public Double getLevelPercentage4() {
		return levelPercentage4;
	}
	public void setLevelPercentage4(Double levelPercentage4) {
		this.levelPercentage4 = levelPercentage4;
	}
	public Double getLevelPercentage5() {
		return levelPercentage5;
	}
	public void setLevelPercentage5(Double levelPercentage5) {
		this.levelPercentage5 = levelPercentage5;
	}
	public Double getLevelPercentage6() {
		return levelPercentage6;
	}
	public void setLevelPercentage6(Double levelPercentage6) {
		this.levelPercentage6 = levelPercentage6;
	}
	public Double getLevelPercentage7() {
		return levelPercentage7;
	}
	public void setLevelPercentage7(Double levelPercentage7) {
		this.levelPercentage7 = levelPercentage7;
	}

	public void init() throws SAXException, DocumentException {
		File f= new File(this.getClass().getClassLoader().getResource(path).getPath());
//		File f = new File("D:\\CRM\\jlyw\\src\\META-INF\\Preference.xml");

		SAXReader reader = new SAXReader();

		reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		
		Document doc = reader.read(f);

		Element root = doc.getRootElement();

		Element foo,fo;
		
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
								//System.out.println("connection.url:" + fo.getTextTrim());
								setTheta1(Double.parseDouble(fo.getTextTrim()));
							else if(attribute.getValue().equals("theta2"))
								//System.out.println("connection.driver_class:" + fo.getTextTrim());
								setTheta2(Double.parseDouble(fo.getTextTrim()));
							else if(attribute.getValue().equals("theta3"))
							{
								//System.out.println("connection.username:" + fo.getTextTrim());
								setTheta3(Double.parseDouble(fo.getTextTrim()));
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
								//System.out.println("connection.url:" + fo.getTextTrim());
								setLevelPercentage1(Double.parseDouble(fo.getTextTrim()));
							else if(attribute.getValue().equals("level2"))
								//System.out.println("connection.driver_class:" + fo.getTextTrim());
								setLevelPercentage2(Double.parseDouble(fo.getTextTrim()));
							else if(attribute.getValue().equals("level3"))
								setLevelPercentage3(Double.parseDouble(fo.getTextTrim()));
								//System.out.println("connection.username:" + fo.getTextTrim());
							else if(attribute.getValue().equals("level4"))
								setLevelPercentage4(Double.parseDouble(fo.getTextTrim()));
								//System.out.println("connection.driver_class:" + fo.getTextTrim());
							else if(attribute.getValue().equals("level5"))
								setLevelPercentage5(Double.parseDouble(fo.getTextTrim()));
								//System.out.println("connection.driver_class:" + fo.getTextTrim());
							else if(attribute.getValue().equals("level6"))
								setLevelPercentage6(Double.parseDouble(fo.getTextTrim()));
								//System.out.println("connection.driver_class:" + fo.getTextTrim());
							else if(attribute.getValue().equals("level7"))
								setLevelPercentage7(Double.parseDouble(fo.getTextTrim()));
								//System.out.println("connection.driver_class:" + fo.getTextTrim());
						}
					}
				}
			}
		}
		
	}
	
	
	
	
	public boolean save() throws SAXException, DocumentException {
//		File f = new File("D:\\CRM\\jlyw\\src\\META-INF\\Preference.xml");
		File f= new File(this.getClass().getClassLoader().getResource(path).getPath());

		SAXReader reader = new SAXReader();

		reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		
		Document doc = reader.read(f);

		Element root = doc.getRootElement();

		Element foo,fo;
		
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
								fo.setText(getTheta1().toString());
								//setTheta1(Double.parseDouble(fo.getTextTrim()));
							else if(attribute.getValue().equals("theta2"))
								fo.setText(getTheta2().toString());
								//setTheta2(Double.parseDouble(fo.getTextTrim()));
							else if(attribute.getValue().equals("theta3"))
							{
								fo.setText(getTheta3().toString());
								
								//setTheta3(Double.parseDouble(fo.getTextTrim()));
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
								fo.setText(getLevelPercentage1().toString());
							else if(attribute.getValue().equals("level2"))
								fo.setText(getLevelPercentage2().toString());
							else if(attribute.getValue().equals("level3"))
								fo.setText(getLevelPercentage3().toString());
							else if(attribute.getValue().equals("level4"))
								fo.setText(getLevelPercentage4().toString());
							else if(attribute.getValue().equals("level5"))
								fo.setText(getLevelPercentage5().toString());
							else if(attribute.getValue().equals("level6"))
								fo.setText(getLevelPercentage6().toString());
							else if(attribute.getValue().equals("level7"))
								fo.setText(getLevelPercentage7().toString());
						}
					}
				}
			}
		}
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("GBK");
			XMLWriter output = new XMLWriter(new FileWriter(f/* new File("D:\\CRM\\jlyw\\src\\META-INF\\Preference.xml")*/ ),format);
			output.write( doc );
			output.close();
			return true;
			
		} catch (IOException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();return false;
		}
		
	}
}
