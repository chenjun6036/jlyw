package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.StdTgtApp;
import com.jlyw.hibernate.StdTgtAppDAO;
import com.jlyw.util.KeyValue;
import com.jlyw.util.KeyValueWithOperator;

public class StdTgtAppManager {
private StdTgtAppDAO m_dao = new StdTgtAppDAO();
	
	/**
	 * ����User Id ���� User����
	 * @param id User Id
	 * @return StdTgtApp����
	 */
	public StdTgtApp findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��StdTgtApp��¼
	 * @param StdTgtApp StdTgtApp����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(StdTgtApp StdTgtApp){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			m_dao.save(StdTgtApp);
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
	 * ����һ��StdTgtApp��¼
	 * @param StdTgtApp StdTgtApp����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(StdTgtApp StdTgtApp){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(StdTgtApp);
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
			m_dao.update("StdTgtApp",list_KeyValue,arr);
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
	 * ����StdTgtApp Id,ɾ��StdTgtApp����
	 * @param id StdTgtApp id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			StdTgtApp u = m_dao.findById(id);
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
	 * @return ��ҳ���StdTgtApp�б�
	 */
	public List<StdTgtApp> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator... arr) {
		return m_dao.findPagedAll("StdTgtApp", currentPage,pageSize, arr);
	}
	
	/**
	 * ��ҳ����
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ�ļ�¼��
	 * @return ��ҳ���Specification�б�
	 */
	public List<StdTgtApp> findPagedAll(int currentPage, int pageSize, List<KeyValueWithOperator> arr) {
		return m_dao.findPagedAll("StdTgtApp", currentPage,pageSize, arr);
	}
	
	/**
	 * �õ�����Specification��¼��
	 * @return Specification��¼��
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("StdTgtApp",arr);		
	}
	
	/**
	 * �õ�����StdTgtApp��¼��
	 * @return StdTgtApp��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("StdTgtApp",arr);		
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List findByExample(StdTgtApp instance) {
		return m_dao.findByExample(instance);
		
	}
	
	/**
	 * 
	 * @param arr
	 * @return
	 */
	public List<StdTgtApp> findByVarProperty(KeyValueWithOperator...arr){
		return m_dao.findByVarProperty("StdTgtApp", arr);
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
	public List findPageAllByHQL(String queryString, int currentPage, int pageSize, Object...arr) throws Exception{
		return m_dao.findPageAllByHQL(queryString, currentPage, pageSize, arr);
	}
	
	
	/**
	 * �õ���¼����
	 * @param queryString ��ѯ��䣨HQL��
	 * @param arr ��ѯ�����?��Ӧ��ֵ
	 * @return
	 */
	public int getTotalCountByHQL(String queryString,Object...arr) throws Exception {
		return m_dao.getTotalCountByHQL(queryString, arr);
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
	* @return ��ҳ��������б�- List<StdTgtApp>
	*/
	public List<StdTgtApp> findPagedAllBySort(int currentPage, int pageSize, String orderby, boolean asc,List<KeyValueWithOperator>arr) throws Exception{
		return m_dao.findPagedAllBySort("StdTgtApp", currentPage, pageSize, orderby, asc, arr);
	}
	
	public List findByHQL(String queryString,Object...arr) {
		return m_dao.findByHQL(queryString, arr);
	}
}
