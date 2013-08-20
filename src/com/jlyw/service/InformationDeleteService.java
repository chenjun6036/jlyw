package com.jlyw.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jlyw.hibernate.HibernateSessionFactory;
import com.jlyw.manager.InformationManager;
import com.jlyw.util.KeyValueWithOperator;

public class InformationDeleteService extends TimerTask  {
	private static Log log = LogFactory.getLog(InformationDeleteService.class);
	public static final int MaxDateOfInformation = 7;	//��Ϣ���ѵ����������7��֮�ڣ�ɾ������7��Ĳ������Ѿ����ѹ�����Ϣ��
	public static final int MaxDateOfDelInformation = 14;	//ǿ��ɾ������14�����Ϣ�������Ƿ��Ѿ����ѣ�
	private static boolean isRunning = false;

	@SuppressWarnings("deprecation")
	public void run() {
		if (!isRunning) {
			try {
				// if (C_SCHEDULE_HOUR == c.get(Calendar.HOUR_OF_DAY)) {	//��ʱ���͹���
				isRunning = true;
				log.debug("��ʼִ��ָ������InformationDeleteService");
				
				List<KeyValueWithOperator> condList = new ArrayList<KeyValueWithOperator>();
				Timestamp beginTime = new Timestamp(System.currentTimeMillis());
				
				//ɾ��14����ǰ��������Ϣ
				beginTime.setDate(beginTime.getDate() - MaxDateOfDelInformation);
				condList.add(new KeyValueWithOperator("createDate", beginTime, "<"));
				InformationManager.deleteInformation(condList);
				
				//ɾ��7����ǰ�������ѵ���Ϣ
				condList.clear();
				beginTime.setDate(beginTime.getDate() + (MaxDateOfDelInformation - MaxDateOfInformation));
				condList.add(new KeyValueWithOperator("createDate", beginTime, "<"));
				condList.add(new KeyValueWithOperator("status", 1, "="));
				InformationManager.deleteInformation(condList);
				
				log.debug("����ִ��ָ������InformationDeleteService");
			} catch (Exception e) {
				log.error("error in InformationDeleteService", e);
			} finally {
				isRunning = false;
				HibernateSessionFactory.closeSessionForFilter();	//�����ر�Hibernate Session
			}
		}
	}
}
