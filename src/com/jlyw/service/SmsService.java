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
 * ���Ͷ���֪ͨ������
 * 
 * @author Administrator
 * 
 */
public class SmsService extends TimerTask {
	private static Log log = LogFactory.getLog(SmsService.class);
	private static final int C_SCHEDULE_HOUR = 15; // ����ִ�п�ʼʱ��
	private static boolean isRunning = false;
//	private ServletContext context = null;

	public void run() {
/*		
		
//		Calendar c = Calendar.getInstance();
		if (!isRunning) {
			try {
				// if (C_SCHEDULE_HOUR == c.get(Calendar.HOUR_OF_DAY)) {	//��ʱ���͹���
				isRunning = true;
//				context.log("��ʼִ��ָ������");
				log.debug("��ʼִ��ָ������SmsService");
				
				
				List<Sms> retList = SmsManager
						.GetSmsRecord(new KeyValueWithOperator("status", 0, "=")); // δ���͵Ķ���
				if (retList != null) {
					for (Sms s : retList) {
						// .............ִ�з��͹���
						
						
						
						
						
						// s.setStatus(1); //�ѷ���
						// s.setSendDate(new
						// Timestamp(System.currentTimeMillis()));
						SmsManager.DeleteSmsRecord(s);
					}
				}


				log.debug("����ִ��ָ������SmsService");
//				context.log("ָ������ִ�н���");
			} catch (Exception e) {
				log.error("error in SmsService", e);
			} finally {
				isRunning = false;
				HibernateSessionFactory.closeSessionForFilter();	//�����ر�Hibernate Session
			}
		}
*/
	}
}