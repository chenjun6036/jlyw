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
	public static final int MaxDateOfInformation = 7;	//消息提醒的最大天数：7天之内（删除超过7天的并且是已经提醒过的消息）
	public static final int MaxDateOfDelInformation = 14;	//强制删除超过14天的消息（无论是否已经提醒）
	private static boolean isRunning = false;

	@SuppressWarnings("deprecation")
	public void run() {
		if (!isRunning) {
			try {
				// if (C_SCHEDULE_HOUR == c.get(Calendar.HOUR_OF_DAY)) {	//定时发送功能
				isRunning = true;
				log.debug("开始执行指定任务：InformationDeleteService");
				
				List<KeyValueWithOperator> condList = new ArrayList<KeyValueWithOperator>();
				Timestamp beginTime = new Timestamp(System.currentTimeMillis());
				
				//删除14天以前的所有消息
				beginTime.setDate(beginTime.getDate() - MaxDateOfDelInformation);
				condList.add(new KeyValueWithOperator("createDate", beginTime, "<"));
				InformationManager.deleteInformation(condList);
				
				//删除7天以前的已提醒的消息
				condList.clear();
				beginTime.setDate(beginTime.getDate() + (MaxDateOfDelInformation - MaxDateOfInformation));
				condList.add(new KeyValueWithOperator("createDate", beginTime, "<"));
				condList.add(new KeyValueWithOperator("status", 1, "="));
				InformationManager.deleteInformation(condList);
				
				log.debug("结束执行指定任务：InformationDeleteService");
			} catch (Exception e) {
				log.error("error in InformationDeleteService", e);
			} finally {
				isRunning = false;
				HibernateSessionFactory.closeSessionForFilter();	//真正关闭Hibernate Session
			}
		}
	}
}
