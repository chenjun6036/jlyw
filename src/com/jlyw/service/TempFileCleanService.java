package com.jlyw.service;

import java.io.File;
import java.util.Date;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jlyw.util.UIDUtil;

/**
 * ��ʱɾ����ʱ�ļ�
 * ��ȡ��ʱ�ļ����µ������ļ�������ļ��Ĵ���ʱ�䣨�����ļ����ж�:UIDUtile���е�����������Ϊ��ǰʱ����һ��Сʱ���ϣ���ɾ�����ļ�
 * @author Zhan
 *
 */
public class TempFileCleanService extends TimerTask {
	private static Log log = LogFactory.getLog(TempFileCleanService.class);
	private static boolean isRunning = false;

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		if (!isRunning) {
			try {
				// if (C_SCHEDULE_HOUR == c.get(Calendar.HOUR_OF_DAY)) {	//��ʱ���͹���
				isRunning = true;
				
				log.debug("��ʼִ��ɾ����ʱ�ļ�����TempFileCleanService");
				
				Date nowTime = new Date(System.currentTimeMillis());	
				nowTime.setHours(nowTime.getHours() - 1);	//ɾ����ʱ��㣺�ȵ�ǰʱ����һ��Сʱ������
				File tempFile = File.createTempFile(UIDUtil.get22BitUID(), ".txt");
				File dir = tempFile.getParentFile();
				if(dir != null && dir.isDirectory() && dir.exists()){
					File []files = dir.listFiles();
					for(File file : files){
						if(file.exists() && file.isFile()){
							String fileName = file.getName();
							int index = fileName.indexOf('_');
							if(index > -1){
								try{
									Date time = UIDUtil.formater.parse(fileName.substring(0, index));
									if(time.before(nowTime)){	//ɾ�����ļ�
										file.delete();
										file = null;
									}
								}catch(Exception e){
									continue;
								}
							}
						}
					}//end for
				}

				log.debug("����ִ��ɾ����ʱ�ļ�����TempFileCleanService");
			} catch (Exception e) {
				log.error("error in TempFileCleanService", e);
			} finally {
				isRunning = false;
			}
		}
		
	}

}
