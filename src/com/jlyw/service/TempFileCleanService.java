package com.jlyw.service;

import java.io.File;
import java.util.Date;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jlyw.util.UIDUtil;

/**
 * 定时删除临时文件
 * 获取临时文件夹下的所有文件，如果文件的创建时间（根据文件名判断:UIDUtile类中的命名方法）为当前时间早一个小时以上，则删除该文件
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
				// if (C_SCHEDULE_HOUR == c.get(Calendar.HOUR_OF_DAY)) {	//定时发送功能
				isRunning = true;
				
				log.debug("开始执行删除临时文件任务：TempFileCleanService");
				
				Date nowTime = new Date(System.currentTimeMillis());	
				nowTime.setHours(nowTime.getHours() - 1);	//删除的时间点：比当前时间早一个小时或以上
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
									if(time.before(nowTime)){	//删除该文件
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

				log.debug("结束执行删除临时文件任务：TempFileCleanService");
			} catch (Exception e) {
				log.error("error in TempFileCleanService", e);
			} finally {
				isRunning = false;
			}
		}
		
	}

}
