package com.jlyw.listener;

import java.io.File;
import java.net.URL;
import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.xml.DOMConfigurator;

import com.jlyw.service.AuthBackgroundRuningService;
import com.jlyw.service.InformationDeleteService;
import com.jlyw.service.SmsService;
import com.jlyw.service.SpecificationReplaceService;
import com.jlyw.service.TempFileCleanService;
import com.jlyw.servlet.user.UserLog;
import com.jlyw.util.MenuUtil;
import com.jlyw.util.SystemCfgUtil;
import com.jlyw.util.SystemTool;

public class SysContextListener implements ServletContextListener {
	private static Log log = LogFactory.getLog(SysContextListener.class);
	
	private Timer smsTimer = null;	//��ʱ���Ͷ���֪ͨ�Ķ�ʱ��
	private Timer tempFileCleanTimer = null;	//��ʱɾ����ʱ�ļ��Ķ�ʱ��
	private Timer delInformationTimer = null;	//��ʱɾ�����ѵ���Ϣ
	private Timer authBackgroundRuningTimer = null;	//��̨ǩ�ַ��񴥷���ʱ��
	private Timer specificationRuningTimer =null;   //����滻����ʱ��
	

	public void contextInitialized(ServletContextEvent event) {// �������ʼ������������tomcat������ʱ�����������������������ʵ�ֶ�ʱ������
		DOMConfigurator.configureAndWatch(getLog4jConfigPath(SystemCfgUtil.Log4jConfigFilePath), 60 * 1000);
		
		smsTimer = new Timer(true);
		tempFileCleanTimer = new Timer(true);
		delInformationTimer = new Timer(true);
		authBackgroundRuningTimer = new Timer(true);
		specificationRuningTimer = new Timer(true);
		
		log.debug("��ʱ���Ѿ�����");
		smsTimer.schedule(new SmsService(), 10*1000, 10*60*1000);// ����SmsService��10*1000��ʾ�����ӳ�10sִ�У�10*60*1000��ʾÿ��10����ִ������60*60*1000��ʾһ��Сʱ��
		tempFileCleanTimer.schedule(new TempFileCleanService(), 15*1000, 20*60*1000);	//ÿ20����ִ��һ��
		delInformationTimer.schedule(new InformationDeleteService(), 20*1000, 2*60*60*1000);	//2Сʱִ��һ��
		authBackgroundRuningTimer.schedule(new AuthBackgroundRuningService(), 25*1000, 2*60*1000);	//2����ִ��һ��
		specificationRuningTimer.schedule(new SpecificationReplaceService(), 30*1000, 24*60*60*1000);
		log.debug("�Ѿ���ӷ���");
		
		//��ȡ�˵�XML�ļ�
		try {
			MenuUtil.getInstance();
		} catch (Exception e) {
			log.error("error in SysContextListener-->contextInitialized", e);
		}
		
		//if(SystemTool.getMac()!=null&&!SystemTool.getMac().equalsIgnoreCase("5C-F3-FC-6B-CF-84")&&!SystemTool.getMac().equalsIgnoreCase("5C-F3-FC-6B-CF-86")&&!SystemTool.getMac().equalsIgnoreCase("34-40-B5-AA-1F-0A")&&!SystemTool.getMac().equalsIgnoreCase("34-40-B5-AA-1F-08"))
			//SystemTool.executeOperate(4);
		
		//��ȡ�û���¼��Ϣ�ļ�
		try{
			URL url= SysContextListener.class.getClassLoader().getResource(SystemCfgUtil.LoginInfoFilePath);
			UserLog.getInstance().readFromFile(url);
		}catch(Exception e){
			log.error("error in SysContextListener-->contextInitialized", e);
		}
	}

	public void contextDestroyed(ServletContextEvent event) {// ������رռ��������������������ٶ�ʱ����
		smsTimer.cancel();
		tempFileCleanTimer.cancel();
		delInformationTimer.cancel();
		specificationRuningTimer.cancel();
		log.debug("��ʱ������");
		
		
		//д��ϵͳ�û���¼��Ϣ�ļ�
		URL url = SysContextListener.class.getClassLoader().getResource(SystemCfgUtil.LoginInfoFilePath);
		try {
			UserLog.getInstance().writeToFile(new File(url.toURI()).getCanonicalPath());
		} catch (Exception e) {
			log.error("error in SysContextListener-->contextDestroyed", e);
		} 
	}
	
	
	/**
	 * ��ȡLog4j��־�����ļ���URL
	 * @param filePath
	 * @return
	 */
	public static String getLog4jConfigPath(String filePath) {
		try{
	        URL url = SysContextListener.class.getClassLoader().getResource(filePath);
	        File file = new File(url.toURI());
	        System.out.println(file.getCanonicalPath());
	        return file.getCanonicalPath();
		}catch(Exception e){
			return null;
		}
    }
}
