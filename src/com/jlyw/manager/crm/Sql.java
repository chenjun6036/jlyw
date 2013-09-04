/**
 * 
 */
package com.jlyw.manager.crm;

import java.io.File;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;

/**
 * @author xx
 *
 */
/**
 * 数据的链接与关闭
 * @author student
 *
 */
public class Sql {
	
	private static String Driver;
	private static String Url;
	private String path = "hibernate.cfg.xml";
	public static String getUrl() {
		return Url;
	}
	public static void setUrl(String url) {
		Url = url;
	}
	private static String UserName;
	private static String Password;

	public static String getDriver() {
		return Driver;
	}
	public static void setDriver(String driver) {
		Driver = driver;
	}
	public static String getUserName() {
		return UserName;
	}
	public static void setUserName(String userName) {
		UserName = userName;
	}
	public static String getPassword() {
		return Password;
	}
	public static void setPassword(String password) {
		Password = password;
	}
	/**
	 * 加在驱动
	 * @throws SAXException 
	 * @throws DocumentException 
	 * @throws ClassNotFoundException 
	 */
	public boolean init () throws SAXException, DocumentException, ClassNotFoundException {
		//File f = new File(System.getProperty("user.dir")+"\\src\\hibernate.cfg.xml");
		//File f = new File(System.getProperty("user.dir")+"\\src\\hibernate.cfg.xml");
		File f= new File(this.getClass().getClassLoader().getResource(path).getPath());
		SAXReader reader = new SAXReader();

		reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		
		Document doc = reader.read(f);

		Element root = doc.getRootElement();
		
		Element foo,fo;
		
		for (Iterator i = root.elementIterator("session-factory"); i.hasNext();) {

			foo = (Element) i.next();
			 
			if(foo.getQualifiedName().equals("session-factory"))
			{
				for (Iterator j = foo.elementIterator(); j.hasNext();)
				{
					fo=(Element)j.next();
					if(fo.getQualifiedName().equals("property"))
					{
						for(Iterator a=fo.attributeIterator();a.hasNext();)
						{
							Attribute attribute=(Attribute) a.next();
							if(attribute.getValue().equals("connection.url"))
								setUrl(fo.getTextTrim());
							//System.out.println("connection.url:" + fo.getTextTrim());
							
							else if(attribute.getValue().equals("connection.driver_class"))
								setDriver(fo.getTextTrim());
								//System.out.println("connection.driver_class:" + fo.getTextTrim());
							
							else if(attribute.getValue().equals("connection.username"))
								setUserName(fo.getTextTrim());
								//System.out.println("connection.username:" + fo.getTextTrim());
							
							else if(attribute.getValue().equals("connection.password"))

							{
								setPassword(fo.getTextTrim());
								//System.out.println("connection.password:" + fo.getTextTrim());
								break;
							}
							
						}
							
						
					}
				}
				
			}
			Class.forName(Driver);
		//Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	}
		return false;
		
	}
	static {

			//
				//Class.forName(Driver);
			try {
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
		
	/**
	 * 得到链接
	 * @return
	 * @throws Exception 
	 * @throws DocumentException 
	 * @throws SAXException 
	 */
	public Connection getMyConn() throws SAXException, DocumentException, Exception{
		
		Connection conn = null;
		
		try {
			init();
			//得到链接
			
			conn =DriverManager.getConnection(Url+";databasename=czjl_new",UserName,Password);
//			conn =DriverManager.getConnection("jdbc:sqlserver://192.168.0.123:1433;databasename=czjl_new","sa","123456");
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		return conn;
		
	}
	/**
	 * 关闭链接对象
	 * @param conn
	 * @param psmt
	 * @param set
	 */
	public void myclosesql(Connection conn ,PreparedStatement psmt ,ResultSet set ){
		//只要不为空全部关闭
        try {
			
           if(conn!=null){
        	   
        	   conn.close();
           }  	  
        	  
		} catch (Exception e) {
			e.printStackTrace();
		}		
		try {
			
	           if(psmt!=null){
	        	   
	        	   psmt.close();
	           }  	  
	        	  
			} catch (Exception e) {
				e.printStackTrace();
		}
	    try {
				 if(set!=null){
		        	   
		        	   set.close();
		          }  	  
		        	  
			 } catch (Exception e) {
					e.printStackTrace();
		}	
	}
	 
}
