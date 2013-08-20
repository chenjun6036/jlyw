package com.jlyw.util;

import java.io.File;
import java.net.URL;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jlyw.util.xmlHandler.ParseXML;
import com.jlyw.util.xmlHandler.WriteXml;

/**
 * ϵͳ�����ļ�������:����ģʽ
 * @author Administrator
 *
 */
public class SystemCfgUtil {
	private static final Log log = LogFactory.getLog(SystemCfgUtil.class);
	
	public static final String ProjectName = "/jlyw/";	//��Ŀ���ƣ�����URL������Դ����
	public static final String DBPrexName = "[czjl_new].[dbo].";	//���ݿ�ǰ׺������SQL�����
	public static final String CertificateTemplateFilesetName = "CertificateTemplate";	//֤��ģ���ļ�������
	public static final String ContextAttrNameUserPrivilegesMap = "USER_PRIVILEGES_MAP";	//ServletContext�д���ѵ�¼�û�Ȩ�޵�Map��������
	public static final String SessionAttrNameLoginUser = "LOGIN_USER";	//Session�д���ѵ�¼�û���������
	public static final String SystemManagerUserName = "000000";	//ϵͳ����Ա���û���
	
	private static final String SysConfigFilePath = "META-INF/system.cfg.xml";	//ϵͳ�����ļ���·��
	private static final String SysDynamicConfigFilePath = "META-INF/system-dynamic.cfg.xml";	//ϵͳ�ɶ�̬���õ��ļ���·��
	public static final String MenuXmlFilePath = "META-INF/menu.xml";	//�˵�����XML�ļ�
	public static final String Log4jConfigFilePath = "META-INF/log4j.xml";	//Log4j�������ļ�
	public static final String UnprotectedUrlsConfigFilePath = "META-INF/unprotectedurls.xml";	//���ʲ������Ƶ�XML�����ļ�·��
	public static final String UnvalidateSessionUrlsConfigFilePath = "META-INF/unvalidatesessionurls.xml"; //����Ҫ��֤Session���Ƿ��¼����URL��XML�����ļ�·��	
	public static final String LoginInfoFilePath = "META-INF/logininfo.xml";	//���ϵͳ�û���¼��Ϣ���ļ�
	
	
	public static Integer OverdueThreSholdShort;	//����Ԥ����ֵ���̣���Ĭ��15��
	public static Integer OverdueThresholdLong;	//����Ԥ����ֵ��������Ĭ��30��
	
	public static Integer PdfConvertServerPort;	//pdfת������˿ڣ�Ĭ��8100
	
	/***********      �ļ��������������                *****************/
	public static String MongoDBHost;
	public static int MongoDBPort;
	public static String FileDBName;
	public static int MongoDBPoolSize;			//���ӳش�С
	public static String BucketOriginalRecord;	//ԭʼ��¼���ĵ���������
	public static String BucketCertificate;		//֤����ĵ���������
	public static String BucketTemplate;		//ģ���ļ��ļ�������
	public static String BucketAttachment;		//ҵ��ϵͳ�������������׼�ȵĸ�����ת��ҵ��ĸ����ȵȣ��ļ�������
	public static String BucketSharing;			//�����ļ��ļ�������
	public static String BucketOthers;			//�����ļ��ļ�������
	
	
	/***********      �ɶ�̬���õ�ϵͳ����                *****************/
	private static int TaskAllotRule = 0;	//����������0Ϊ��ҵ�������򣨴�С���󣩣�1Ϊ����ֵ���򣨴�С����
	private static int SecondLoginPeriod = 10;	//�û�����ȫ�˳���ڶ��������¼ϵͳ��ʱ��������λ�����ӣ�
	private static String SpecialLetters = "";   //���������ַ�
	private static String DonglePID = "";
	private static String DonglePIN = "";
	
	
	private static ParseXML parser = new ParseXML();	//xml����
	static{
		rebuildCfgParams();
	}
	/**
	 * ˽��Ĭ�Ϲ��캯������ֹ�ⲿʵ����
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
			
			//�ļ��������������
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
	 * д��ϵͳ��̬���õ������ļ�
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
	 * �ж�һ��������׼���Ƿ�������ļ�����׼������Ϊ�������������׼���ļ�����׼Ϊ����ı�׼������ԭʼ��¼��֤������ʾ��;
	 * @param standardName:������ļ�����׼������
	 * @return ���Ϊ����ļ�����׼���򷵻�true�����򣬷���false
	 */
	public static boolean checkStandardVirtual(String standardName){
		if(standardName == null)
			return false;
		if(standardName.equals("�����������׼"))
			return true;
		return false;
	}
}
