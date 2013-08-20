package com.jlyw.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jlyw.hibernate.HibernateSessionFactory;
import com.jlyw.hibernate.Specification;
import com.jlyw.manager.SpecificationManager;
import com.jlyw.util.KeyValueWithOperator;

public class SpecificationReplaceService extends TimerTask {
	private static Log log = LogFactory.getLog(SpecificationReplaceService.class);
	private static final int C_SCHEDULE_HOUR = 1; // 任务执行开始时间
	private static boolean isRunning = false;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(!isRunning){
			try{
				//Calendar c = Calendar.getInstance();
				//if(C_SCHEDULE_HOUR == c.get(Calendar.HOUR_OF_DAY)){
					isRunning = true;
					log.debug("开始执行规程替代任务 : SpecificationReplaceService");
					SpecificationManager specMgr = new SpecificationManager();
					List<Specification> retList = specMgr.findByVarProperty(new KeyValueWithOperator("status", 2, "="));
					List<Specification> specList = new ArrayList<Specification>();
					if(retList!=null&&retList.size()!=0){
						for(Specification temp : retList){
							Date today = new Date(System.currentTimeMillis());
							if(today.equals(temp.getEffectiveDate())){
								//执行替代
								specList.add(temp);
							}
						}
					}
					specMgr.replaceByList(specList);
				//}
			}catch(Exception e){
				log.error("error in SpecificationReplaceService", e);
			}finally{
				isRunning = false;
				HibernateSessionFactory.closeSessionForFilter();
			}
		}
	}

}
