package com.jlyw.util;

import java.io.File;
import java.net.URL;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jlyw.util.xmlHandler.ParseXML;
import com.jlyw.util.xmlHandler.WriteXml;

/**
 * 系统配置文件助手类:单例模式
 * @author Administrator
 *
 */
public class SystemCfgUtil {
	private static final Log log = LogFactory.getLog(SystemCfgUtil.class);
	
	public static final String ProjectName = "/jlyw/";	//项目名称：用于URL区分资源部分
	public static final String DBPrexName = "[czjl_new].[dbo].";	//数据库前缀，用于SQL语句中
	public static final String CertificateTemplateFilesetName = "CertificateTemplate";	//证书模板文件集名称
	public static final String ContextAttrNameUserPrivilegesMap = "USER_PRIVILEGES_MAP";	//ServletContext中存放已登录用户权限的Map的属性名
	public static final String SessionAttrNameLoginUser = "LOGIN_USER";	//Session中存放已登录用户的属性名
	public static final String SystemManagerUserName = "000000";	//系统管理员的用户名
	
	private static final String SysConfigFilePath = "META-INF/system.cfg.xml";	//系统配置文件的路径
	private static final String SysDynamicConfigFilePath = "META-INF/system-dynamic.cfg.xml";	//系统可动态配置的文件的路径
	public static final String MenuXmlFilePath = "META-INF/menu.xml";	//菜单树的XML文件
	public static final String Log4jConfigFilePath = "META-INF/log4j.xml";	//Log4j的配置文件
	public static final String UnprotectedUrlsConfigFilePath = "META-INF/unprotectedurls.xml";	//访问不受限制的XML配置文件路径
	public static final String UnvalidateSessionUrlsConfigFilePath = "META-INF/unvalidatesessionurls.xml"; //不需要验证Session（是否登录）的URL的XML配置文件路径	
	public static final String LoginInfoFilePath = "META-INF/logininfo.xml";	//存放系统用户登录信息的文件
	
	
	public static Integer OverdueThreSholdShort;	//超期预警阈值（短）：默认15天
	public static Integer OverdueThresholdLong;	//超期预警阈值（常）：默认30天
	
	public static Integer PdfConvertServerPort;	//pdf转换服务端口：默认8100
	
	/***********      文件服务器各项参数                *****************/
	public static String MongoDBHost;
	public static int MongoDBPort;
	public static String FileDBName;
	public static int MongoDBPoolSize;			//连接池大小
	public static String BucketOriginalRecord;	//原始记录的文档集合名称
	public static String BucketCertificate;		//证书的文档集合名称
	public static String BucketTemplate;		//模板文件的集合名称
	public static String BucketAttachment;		//业务系统附件（如计量标准等的附件、转包业务的附件等等）的集合名称
	public static String BucketSharing;			//共享文件的集合名称
	public static String BucketOthers;			//其他文件的集合名称
	
	
	/***********      可动态配置的系统参数                *****************/
	private static int TaskAllotRule = 0;	//任务分配规则，0为按业务量排序（从小到大），1为按产值排序（从小到大）
	private static int SecondLoginPeriod = 10;	//用户不安全退出后第二次允许登录系统的时间间隔（单位：分钟）
	private static String SpecialLetters = "";   //常用特殊字符
	private static String DonglePID = "";
	private static String DonglePIN = "";
	
	
	private static ParseXML parser = new ParseXML();	//xml解析
	static{
		rebuildCfgParams();
	}
	/**
	 * 私有默认构造函数：防止外部实例化
	 */
	private SystemCfgUtil(){}
	
	public static int getTaskAllotRule(){
		return TaskAllotRule;
	}
	public static void setTaskAllotRule(int rule){
		if(TaskAllotRule != rule){
			TaskAllotRule = rule;
		}
	}
	public static int getSecondLoginPeriod(){
		return SecondLoginPeriod;
	}
	public static void setSecondLoginPeriod(int secondLoginPeriod){
		if(secondLoginPeriod >= 0){
			SecondLoginPeriod = secondLoginPeriod;
		}
	}
	public static String getSpecialLetters(){
		return SpecialLetters;
	}
	public static void setSpecialLetters(String specialLetters){
		if(specialLetters != null&&!specialLetters.equals("")){
			SpecialLetters = specialLetters;
		}
	}

	public static String getDonglePID() {
		return DonglePID;
	}

	public static void setDonglePID(String donglePID) {
		if(donglePID != null&&!donglePID.equals("")){
			DonglePID = donglePID;
		}
	}

	public static String getDonglePIN() {
		return DonglePIN;
	}

	public static void setDonglePIN(String donglePIN) {
		if(donglePIN != null&&donglePIN.equals("")){
			DonglePIN = donglePIN;
		}
	}

	private static void rebuildCfgParams(){
		try {
			parser.parse(SysConfigFilePath);
			
			OverdueThreSholdShort = Integer.parseInt(parser.getProperty("OverdueThreSholdShort", "15"));
			OverdueThresholdLong = Integer.parseInt(parser.getProperty("OverdueThresholdLong", "30"));
			
			PdfConvertServerPort = Integer.parseInt(parser.getProperty("pdfconverter-server-port", "8100"));
			
			//文件服务器各项参数
			MongoDBHost = parser.getProperty("mongodb-host", "127.0.0.1");
			MongoDBPort = Integer.parseInt(parser.getProperty("mongodb-port", "27017"));
			FileDBName = parser.getProperty("mongodb-dbname", "jlyw");
			MongoDBPoolSize = Integer.parseInt(parser.getProperty("mongodb-poolsize", "200"));
			BucketOriginalRecord = parser.getProperty("bucket-original-record", "originalrecord");
			BucketCertificate = parser.getProperty("bucket-certificate", "certificate");
			BucketTemplate = parser.getProperty("bucket-template", "template");
			BucketAttachment = parser.getProperty("bucket-attachment", "attachment");
			BucketSharing = parser.getProperty("bucket-sharing", "sharing");
			BucketOthers = parser.getProperty("bucket-others", "others");
			
			
			parser.parse(SysDynamicConfigFilePath);
			String temp = parser.getAttribute("TaskAllotRule", "value");
			if(temp != null && temp.length() > 0){
				TaskAllotRule = Integer.parseInt(temp);
			}else{
				TaskAllotRule = 0;
			}
			
			temp = parser.getAttribute("SecondLoginPeriod", "value");
			if(temp != null && temp.length() > 0){
				SecondLoginPeriod = Integer.parseInt(temp);
			}else{
				SecondLoginPeriod = 10;
			}
			
			temp = parser.getAttribute("SpecialLetters", "value");
			if(temp != null && temp.length() > 0){
				SpecialLetters = temp;
			}else{
				SpecialLetters = "";
			}
			
			temp = parser.getAttribute("DonglePID", "value");
			if(temp != null && temp.length() > 0){
				DonglePID = temp;
			}else{
				DonglePID = "";
			}
			
			temp = parser.getAttribute("DonglePIN", "value");
			if(temp != null && temp.length() > 0){
				DonglePIN = temp;
			}else{
				DonglePIN = "";
			}
			
			
			
		} catch (Exception e) {
			log.error("error in SystemCfgUtil->rebuildCfgParams()", e);
		}
	}
	
	/**
	 * 写入系统动态配置的配置文件
	 */
	public static void writeToSysDynCfgFile(){
		URL url = SystemCfgUtil.class.getClassLoader().getResource(SysDynamicConfigFilePath);
		try{
			WriteXml w = new WriteXml(new File(url.toURI()).getCanonicalPath(), "system-config");
			
			HashMap<String, String> attrMap = new HashMap<String, String>();
			attrMap.put("value", String.format("%d", TaskAllotRule));
			w.writeElement(attrMap, "TaskAllotRule");
			
			attrMap.put("value", String.format("%d", SecondLoginPeriod));
			w.writeElement(attrMap, "SecondLoginPeriod");
			
			attrMap.put("value", String.format("%s", SpecialLetters));
			w.writeElement(attrMap, "SpecialLetters");
			
			attrMap.put("value", String.format("%s", DonglePID));
			w.writeElement(attrMap, "DonglePID");
			
			attrMap.put("value", String.format("%s", DonglePIN));
			w.writeElement(attrMap, "DonglePIN");
			
			w.endDocument();
		}catch(Exception e){
			log.error("error in SystemCfgUtil->writeToSysDynCfgFile()", e);
		}
	}
	
	/**
	 * 判断一个计量标准的是否是虚拟的计量标准（名称为‘其他类计量标准’的计量标准为虚拟的标准，不在原始记录和证书上显示）;
	 * @param standardName:待检验的计量标准的名称
	 * @return 如果为虚拟的计量标准，则返回true，否则，返回false
	 */
	public static boolean checkStandardVirtual(String standardName){
		if(standardName == null)
			return false;
		if(standardName.equals("其他类计量标准"))
			return true;
		return false;
	}
}
