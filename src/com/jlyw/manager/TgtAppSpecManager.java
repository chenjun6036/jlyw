package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.TgtAppSpec;
import com.jlyw.hibernate.TgtAppSpecDAO;
import com.jlyw.util.KeyValue;
import com.jlyw.util.KeyValueWithOperator;

public class TgtAppSpecManager {
private TgtAppSpecDAO m_dao = new TgtAppSpecDAO();
	
	/**
	 * ����User Id ���� User����
	 * @param id User Id
	 * @return TgtAppSpec����
	 */
	public TgtAppSpec findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��TgtAppSpec��¼
	 * @param TgtAppSpec TgtAppSpec����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(TgtAppSpec TgtAppSpec){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			m_dao.save(TgtAppSpec);
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
	 * ����һ��TgtAppSpec��¼
	 * @param TgtAppSpec TgtAppSpec����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(TgtAppSpec TgtAppSpec){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(TgtAppSpec);
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
			m_dao.update("TgtAppSpec",list_KeyValue,arr);
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
	 * ����TgtAppSpec Id,ɾ��TgtAppSpec����
	 * @param id TgtAppSpec id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			TgtAppSpec u = m_dao.findById(id);
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
	 * @return ��ҳ���TgtAppSpec�б�
	 */
	public List<TgtAppSpec> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator... arr) {
		return m_dao.findPagedAll("TgtAppSpec", currentPage,pageSize, arr);
	}
	
	/**
	 * ��ҳ����
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ�ļ�¼��
	 * @return ��ҳ���Specification�б�
	 */
	public List<TgtAppSpec> findPagedAll(int currentPage, int pageSize, List<KeyValueWithOperator> arr) {
		return m_dao.findPagedAll("TgtAppSpec", currentPage,pageSize, arr);
	}
	
	/**
	 * �õ�����Specification��¼��
	 * @return Specification��¼��
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("TgtAppSpec",arr);		
	}
	
	/**
	 * �õ�����TgtAppSpec��¼��
	 * @return TgtAppSpec��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("TgtAppSpec",arr);		
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List findByExample(TgtAppSpec instance) {
		return m_dao.findByExample(instance);
		
	}
	
	/**
	 * 
	 * @param arr
	 * @return
	 */
	public List<TgtAppSpec> findByVarProperty(KeyValueWithOperator...arr){
		return m_dao.findByVarProperty("TgtAppSpec", arr);
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
	
	
	/**
	 * �õ���¼����
	 * @param queryString ��ѯ��䣨HQL��
	 * @param arr ��ѯ�����?��Ӧ��ֵ
	 * @return
	 */
	public int getTotalCountByHQL(String queryString,Object...arr) {
		try{
			return m_dao.getTotalCountByHQL(queryString, arr);
		}catch(Exception ex){
			return 0;
		}
	}
	public List findByHQL(String queryString,Object...arr) {
		return m_dao.findByHQL(queryString, arr);
	}
}
