package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.TgtAppStdApp;
import com.jlyw.hibernate.TgtAppStdApp;
import com.jlyw.hibernate.TgtAppStdAppDAO;
import com.jlyw.util.KeyValue;
import com.jlyw.util.KeyValueWithOperator;

public class TgtAppStdAppManager {
private TgtAppStdAppDAO m_dao = new TgtAppStdAppDAO();
	
	/**
	 * ����User Id ���� User����
	 * @param id User Id
	 * @return TgtAppStdApp����
	 */
	public TgtAppStdApp findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��TgtAppStdApp��¼
	 * @param TgtAppStdApp TgtAppStdApp����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(TgtAppStdApp TgtAppStdApp){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			m_dao.save(TgtAppStdApp);
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
	 * ����һ��TgtAppStdApp��¼
	 * @param TgtAppStdApp TgtAppStdApp����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(TgtAppStdApp TgtAppStdApp){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(TgtAppStdApp);
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
	
	public boolean update(List<KeyValue> list_KeyValue ,KeyValue...arr){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update("TgtAppStdApp",list_KeyValue,arr);
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
	 * ����TgtAppStdApp Id,ɾ��TgtAppStdApp����
	 * @param id TgtAppStdApp id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			TgtAppStdApp u = m_dao.findById(id);
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
	 * ��ҳ����
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ�ļ�¼��
	 * @return ��ҳ���TgtAppStdApp�б�
	 */
	public List<TgtAppStdApp> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator... arr) {
		return m_dao.findPagedAll("TgtAppStdApp", currentPage,pageSize, arr);
	}
	
	/**
	 * ��ҳ����
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ�ļ�¼��
	 * @return ��ҳ���Specification�б�
	 */
	public List<TgtAppStdApp> findPagedAll(int currentPage, int pageSize, List<KeyValueWithOperator> arr) {
		return m_dao.findPagedAll("TgtAppStdApp", currentPage,pageSize, arr);
	}
	
	/**
	 * �õ�����Specification��¼��
	 * @return Specification��¼��
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("TgtAppStdApp",arr);		
	}
	
	/**
	 * �õ�����TgtAppStdApp��¼��
	 * @return TgtAppStdApp��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("TgtAppStdApp",arr);		
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List findByExample(TgtAppStdApp instance) {
		return m_dao.findByExample(instance);
		
	}
	
	/**
	 * 
	 * @param arr
	 * @return
	 */
	public List<TgtAppStdApp> findByVarProperty(KeyValueWithOperator...arr){
		return m_dao.findByVarProperty("TgtAppStdApp", arr);
	}
	
		
	
	/**
	* ��ҳ��ʾ����
	*@param queryString:��ѯ��䣨HQL��
	* @param currentPage
	* ��ǰҳ��, �� 1 ��ʼ
	* @param pageSize
	* ÿҳ��ʾ������
	* @param arr ��ѯ�����?��Ӧ��ֵ
	* @return ��ҳ��������б�- List
	*/
	public List findPageAllByHQL(String queryString, int currentPage, int pageSize, Object...arr){
		return m_dao.findPageAllByHQL(queryString, currentPage, pageSize, arr);
	}
	public List findPageAllByHQL(String queryString, int currentPage, int pageSize, List<Object>arr){
		return m_dao.findPageAllByHQL(queryString, currentPage, pageSize, arr);
	}
	
	
	/**
	 * �õ���¼����
	 * @param queryString ��ѯ��䣨HQL��
	 * @param arr ��ѯ�����?��Ӧ��ֵ
	 * @return
	 */
	public int getTotalCountByHQL(String queryString,Object...arr) {
		return m_dao.getTotalCountByHQL(queryString, arr);
	}
	
	/**
	 * ����һ��TgtAppStdApp��¼
	 * @param TgtAppStdAppList TgtAppStdApp����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean saveByBatch(List<TgtAppStdApp> TgtAppStdAppList){
		if(TgtAppStdAppList == null || TgtAppStdAppList.size() == 0){
			return true;
		}
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			for(TgtAppStdApp TgtAppStdApp : TgtAppStdAppList){
				m_dao.save(TgtAppStdApp);
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
	
	public List findByHQL(String queryString,Object...arr) {
		return m_dao.findByHQL(queryString, arr);
	}
}
