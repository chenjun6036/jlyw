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
	 * ����һ������֪ͨ
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
			s.setStatus(0);	//״̬��δ����
			s.setCreateDate(new Timestamp(System.currentTimeMillis()));
			return instance.save(s);
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * ����������
	 * @param arr
	 * @return �ɹ�����List��ʧ�ܷ���null
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
	 * ɾ��һ�����ż�¼
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
	 * ����Sms Id ���� Sms����
	 * @param id: Sms Id
	 * @return Sms����
	 */
	public Sms findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��Sms��¼
	 * @param appSpecies Sms����
	 * @return ����ɹ�������true�����򷵻�false
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
	 * ����һ��Sms��¼
	 * @param appSpecies Sms����
	 * @return ���³ɹ�������true�����򷵻�false
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
	 * ����Sms Id,ɾ��Sms����
	 * @param id
	 * @return ɾ���ɹ�������true�����򷵻�false
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
	 * ����������
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
