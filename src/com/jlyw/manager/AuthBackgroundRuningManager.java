package com.jlyw.manager;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.AuthBackgroundRuning;
import com.jlyw.hibernate.AuthBackgroundRuningDAO;
import com.jlyw.hibernate.VerifyAndAuthorize;
import com.jlyw.util.KeyValueWithOperator;

public class AuthBackgroundRuningManager {
	private static AuthBackgroundRuningManager instance = null;
	/**
	 * ����һ����̨ǩ������ͬʱ���º����ǩ�ּ�¼�ı�־�ֶΣ�IsAuthBgRuning��
	 * @param verifyAndAuthorize
	 * @return
	 */
	public static boolean AddAnAuthBackgroundRuning(VerifyAndAuthorize verifyAndAuthorize) throws Exception{
		if(instance == null){
			instance = new AuthBackgroundRuningManager();
		}
		AuthBackgroundRuningDAO dao = instance.getDAO();
		Transaction tran = dao.getSession().beginTransaction();
		try{
			AuthBackgroundRuning s = new AuthBackgroundRuning();
			s.setVerifyAndAuthorize(verifyAndAuthorize);
			s.setCreateTime(new Timestamp(System.currentTimeMillis()));
			dao.save(s);
			verifyAndAuthorize.setIsAuthBgRuning(true);
			dao.update(verifyAndAuthorize);
			tran.commit();
			return true;
		}catch(Exception e){
			tran.rollback();
			throw e;
		}finally{
			dao.closeSession();
		}
	}
	
	
	public static List<AuthBackgroundRuning> findPageAllAuthBackgroundRuningBySort(int currentPage, int pageSize, String orderby, boolean asc, KeyValueWithOperator...arr) throws Exception{
		if(instance == null){
			instance = new AuthBackgroundRuningManager();
		}
		return instance.findPagedAllBySort(currentPage, pageSize, orderby, asc, arr);
	}
	
	
	/**
	 * �Ƴ�һ����̨ǩ�ֵ�����
	 * @param info
	 * @param doneSuccess:�Ƿ��̨ǩ�ֳɹ���ɣ�true����̨ǩ�ֳɹ���ɣ�false����̨ǩ��ʧ�ܣ�
	 * @return
	 */
	public static boolean removeAnAuthBackgroundRuning(AuthBackgroundRuning info, boolean doneSuccess) throws Exception{
		if(instance == null){
			instance = new AuthBackgroundRuningManager();
		}
		AuthBackgroundRuningDAO dao = instance.getDAO();
		Transaction tran = dao.getSession().beginTransaction();
		try{
			VerifyAndAuthorize v = info.getVerifyAndAuthorize();
			dao.delete(info);
			if(doneSuccess){
				v.setIsAuthBgRuning(null);
			}else{
				v.setAuthorizeResult(null);
				v.setAuthorizeTime(null);
				v.setIsAuthBgRuning(null);
				v.setAuthorizeRemark(null);
				v.setSysUserByAuthorizeExecutorId(null);
			}
			dao.update(v);
			tran.commit();
			return true;
		}catch(Exception e){
			tran.rollback();
			throw e;
		}finally{
			dao.closeSession();
		}
	}
	
	private AuthBackgroundRuningDAO m_dao = new AuthBackgroundRuningDAO();
	/**
	 * ����AuthBackgroundRuning Id ���� AuthBackgroundRuning����
	 * @param id: AuthBackgroundRuning Id
	 * @return AuthBackgroundRuning����
	 */
	public AuthBackgroundRuning findById(int id) {
		return m_dao.findById(id);
	}
	
	public AuthBackgroundRuningDAO getDAO(){
		return m_dao;
	}
	
			
	public int delete(List<KeyValueWithOperator> condList){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			int iRet = m_dao.delete("AuthBackgroundRuning", condList);	
			tran.commit();
			return iRet;
		} catch (Exception e) {
			tran.rollback();
			return -1;
		} finally {
			m_dao.closeSession();
		}
	}
	
	/**
	* ��ҳ��ʾ����
	* @param currentPage
	* ��ǰҳ��, �� 1 ��ʼ
	* @param pageSize
	* ÿҳ��ʾ������
	* @param orderby�������ĸ��ֶ�����
	* @param asc��true ���� false ����
	* @param arr:Ϊ��ѯ������(��-ֵ)������
	* @return ��ҳ��������б�- List<Student>
	*/
	public List<AuthBackgroundRuning> findPagedAllBySort(int currentPage, int pageSize, String orderby, boolean asc, KeyValueWithOperator...arr) throws Exception{
		return m_dao.findPagedAllBySort("AuthBackgroundRuning", currentPage, pageSize, orderby, asc, arr);
	}
	/**
	 * ��ȡ���������ļ�¼����
	 * @param ���arr����ѯ����������-ֵ-�����
	 * @return ���������ļ�¼����
	 */
	public int getTotalCount(KeyValueWithOperator...arr) throws Exception{
		return m_dao.getTotalCount("AuthBackgroundRuning", arr);
	}
}
