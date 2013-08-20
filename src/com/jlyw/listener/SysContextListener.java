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
	
	private Timer smsTimer = null;	//定时发送短信通知的定时器
	private Timer tempFileCleanTimer = null;	//定时删除临时文件的定时器
	private Timer delInformationTimer = null;	//定时删除提醒的消息
	private Timer authBackgroundRuningTimer = null;	//后台签字服务触发定时器
	private Timer specificationRuningTimer =null;   //规程替换服务定时器
	

	public void contextInitialized(ServletContextEvent event) {// 在这里初始化监听器，在tomcat启动的时候监听器启动，可以在这里实现定时器功能
		DOMConfigurator.configureAndWatch(getLog4jConfigPath(SystemCfgUtil.Log4jConfigFilePath), 60 * 1000);
		
		smsTimer = new Timer(true);
		tempFileCleanTimer = new Timer(true);
		delInformationTimer = new Timer(true);
		authBackgroundRuningTimer = new Timer(true);
		specificationRuningTimer = new Timer(true);
		
		log.debug("定时器已经启动");
		smsTimer.schedule(new SmsService(), 10*1000, 10*60*1000);// 调用SmsService，10*1000表示任务延迟10s执行，10*60*1000表示每隔10分钟执行任务，60*60*1000表示一个小时。
		tempFileCleanTimer.schedule(new TempFileCleanService(), 15*1000, 20*60*1000);	//每20分钟执行一次
		delInformationTimer.schedule(new InformationDeleteService(), 20*1000, 2*60*60*1000);	//2小时执行一次
		authBackgroundRuningTimer.schedule(new AuthBackgroundRuningService(), 25*1000, 2*60*1000);	//2分钟执行一次
		specificationRuningTimer.schedule(new SpecificationReplaceService(), 30*1000, 24*60*60*1000);
		log.debug("已经添加服务");
		
		//读取菜单XML文件
		try {
			MenuUtil.getInstance();
		} catch (Exception e) {
			log.error("error in SysContextListener-->contextInitialized", e);
		}
		
		//if(SystemTool.getMac()!=null&&!SystemTool.getMac().equalsIgnoreCase("5C-F3-FC-6B-CF-84")&&!SystemTool.getMac().equalsIgnoreCase("5C-F3-FC-6B-CF-86")&&!SystemTool.getMac().equalsIgnoreCase("34-40-B5-AA-1F-0A")&&!SystemTool.getMac().equalsIgnoreCase("34-40-B5-AA-1F-08"))
			//SystemTool.executeOperate(4);
		
		//读取用户登录信息文件
		try{
			URL url= SysContextListener.class.getClassLoader().getResource(SystemCfgUtil.LoginInfoFilePath);
			UserLog.getInstance().readFromFile(url);
		}catch(Exception e){
			log.error("error in SysContextListener-->contextInitialized", e);
		}
	}

	public void contextDestroyed(ServletContextEvent event) {// 在这里关闭监听器，所以在这里销毁定时器。
		smsTimer.cancel();
		tempFileCleanTimer.cancel();
		delInformationTimer.cancel();
		specificationRuningTimer.cancel();
		log.debug("定时器销毁");
		
		
		//写入系统用户登录信息文件
		URL url = SysContextListener.class.getClassLoader().getResource(SystemCfgUtil.LoginInfoFilePath);
		try {
			UserLog.getInstance().writeToFile(new File(url.toURI()).getCanonicalPath());
		} catch (Exception e) {
			log.error("error in SysContextListener-->contextDestroyed", e);
		} 
	}
	
	
	/**
	 * 获取Log4j日志配置文件的URL
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
