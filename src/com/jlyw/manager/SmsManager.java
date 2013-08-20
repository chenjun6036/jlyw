package com.jlyw.manager;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Transaction;
import com.jlyw.hibernate.Sms;
import com.jlyw.hibernate.SmsDAO;
import com.jlyw.util.KeyValueWithOperator;

public class SmsManager {
	private static SmsManager instance = null;
	/**
	 * 增加一条短信通知
	 * @param number
	 * @param content
	 * @param receiver
	 * @param type
	 * @return
	 */
	public static boolean AddSmsRecord(String number, String content, String receiver, int type){
		try{
			if(instance == null){
				instance = new SmsManager();
			}
			Sms s = new Sms();
			s.setNumber(number);
			s.setContents(content);
			s.setReceiver(receiver);
			s.setType(type);
			s.setStatus(0);	//状态：未发送
			s.setCreateDate(new Timestamp(System.currentTimeMillis()));
			return instance.save(s);
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * 按条件查找
	 * @param arr
	 * @return 成功返回List，失败返回null
	 */
	public static List<Sms> GetSmsRecord(KeyValueWithOperator...arr){
		try{
			if(instance == null){
				instance = new SmsManager();
			}
			return instance.findByVarProperty(arr);
		}catch(Exception e){
			return null;
		}
	}
	/**
	 * 删除一条短信记录
	 * @param sms
	 * @return
	 */
	public static boolean DeleteSmsRecord(Sms sms){
		try{
			if(instance == null){
				instance = new SmsManager();
			}
			return instance.deleteById(sms.getId());
		}catch(Exception e){
			return false;
		}
	}
	
	private SmsManager(){	}
	
	private SmsDAO m_dao = new SmsDAO();
	/**
	 * 根据Sms Id 查找 Sms对象
	 * @param id: Sms Id
	 * @return Sms对象
	 */
	public Sms findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条Sms记录
	 * @param appSpecies Sms对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(Sms appSpecies){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			m_dao.save(appSpecies);
			tran.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			tran.rollback();
			return false;
		} finally {
			m_dao.closeSession();
		}
	}
	
	/**
	 * 更新一条Sms记录
	 * @param appSpecies Sms对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(Sms appSpecies){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(appSpecies);
			tran.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			tran.rollback();
			return false;
		} finally {
			m_dao.closeSession();
		}
	}
	
	/**
	 * 根据Sms Id,删除Sms对象
	 * @param id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			Sms u = m_dao.findById(id);
			if(u == null){
				return true;
			}else{
				m_dao.delete(u);
			}			
			tran.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			tran.rollback();
			return false;
		} finally {
			m_dao.closeSession();
		}
	}	

	/**
	 * 按条件查找
	 * @param arr
	 * @return
	 */
	public List<Sms> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("Sms", arr);
		}
		catch(Exception e){
			return null;
		}
	}
}
