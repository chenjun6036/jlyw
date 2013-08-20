package com.jlyw.service;

import java.util.List;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jlyw.hibernate.HibernateSessionFactory;
import com.jlyw.hibernate.Sms;
import com.jlyw.manager.SmsManager;
import com.jlyw.util.KeyValueWithOperator;

/**
 * 发送短信通知服务类
 * 
 * @author Administrator
 * 
 */
public class SmsService extends TimerTask {
	private static Log log = LogFactory.getLog(SmsService.class);
	private static final int C_SCHEDULE_HOUR = 15; // 任务执行开始时间
	private static boolean isRunning = false;
//	private ServletContext context = null;

	public void run() {
/*		
		
//		Calendar c = Calendar.getInstance();
		if (!isRunning) {
			try {
				// if (C_SCHEDULE_HOUR == c.get(Calendar.HOUR_OF_DAY)) {	//定时发送功能
				isRunning = true;
//				context.log("开始执行指定任务");
				log.debug("开始执行指定任务：SmsService");
				
				
				List<Sms> retList = SmsManager
						.GetSmsRecord(new KeyValueWithOperator("status", 0, "=")); // 未发送的短信
				if (retList != null) {
					for (Sms s : retList) {
						// .............执行发送功能
						
						
						
						
						
						// s.setStatus(1); //已发送
						// s.setSendDate(new
						// Timestamp(System.currentTimeMillis()));
						SmsManager.DeleteSmsRecord(s);
					}
				}


				log.debug("结束执行指定任务：SmsService");
//				context.log("指定任务执行结束");
			} catch (Exception e) {
				log.error("error in SmsService", e);
			} finally {
				isRunning = false;
				HibernateSessionFactory.closeSessionForFilter();	//真正关闭Hibernate Session
			}
		}
*/
	}
}