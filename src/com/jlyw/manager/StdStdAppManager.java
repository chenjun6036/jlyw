package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.StdStdApp;
import com.jlyw.hibernate.StdStdApp;
import com.jlyw.hibernate.StdStdAppDAO;
import com.jlyw.util.KeyValue;
import com.jlyw.util.KeyValueWithOperator;

public class StdStdAppManager {
private StdStdAppDAO m_dao = new StdStdAppDAO();
	
	/**
	 * ����User Id ���� User����
	 * @param id User Id
	 * @return StdStdApp����
	 */
	public StdStdApp findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��StdStdApp��¼
	 * @param StdStdApp StdStdApp����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(StdStdApp StdStdApp){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			m_dao.save(StdStdApp);
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
	 * ����һ��StdStdApp��¼
	 * @param StdStdApp StdStdApp����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(StdStdApp StdStdApp){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(StdStdApp);
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
			m_dao.update("StdStdApp",list_KeyValue,arr);
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
	 * ����StdStdApp Id,ɾ��StdStdApp����
	 * @param id StdStdApp id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			StdStdApp u = m_dao.findById(id);
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
	 * @return ��ҳ���StdStdApp�б�
	 */
	public List<StdStdApp> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator... arr) {
		try {
			return m_dao.findPagedAll("StdStdApp", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * ��ҳ����
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ�ļ�¼��
	 * @return ��ҳ���Specification�б�
	 */
	public List<StdStdApp> findPagedAll(int currentPage, int pageSize, List<KeyValueWithOperator> arr) {
		try {
			return m_dao.findPagedAll("StdStdApp", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * �õ�����Specification��¼��
	 * @return Specification��¼��
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("StdStdApp",arr);		
	}
	
	/**
	 * �õ�����StdStdApp��¼��
	 * @return StdStdApp��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("StdStdApp",arr);		
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List findByExample(StdStdApp instance) {
		return m_dao.findByExample(instance);
		
	}
	
	/**
	 * 
	 * @param arr
	 * @return
	 */
	public List<StdStdApp> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("StdStdApp", arr);
		}
		catch(Exception e){
			return null;
		}
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
		try{
			return m_dao.findPageAllByHQL(queryString, currentPage, pageSize, arr);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
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
	public List findPageAllByHQL(String queryString, int currentPage, int pageSize, List<Object> arr) throws Exception{
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
	
	
	/**
	 * ����һ��StdStdApp��¼
	 * @param StdStdAppList StdStdApp����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean saveByBatch(List<StdStdApp> StdStdAppList){
		if(StdStdAppList == null || StdStdAppList.size() == 0){
			return true;
		}
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			for(StdStdApp StdStdApp : StdStdAppList){
				m_dao.save(StdStdApp);
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
	 * ����һ��StdStdApp��¼
	 * @param StdStdAppList StdStdApp����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean updateByBatch(List<StdStdApp> StdStdAppList){
		if(StdStdAppList == null || StdStdAppList.size() == 0){
			return true;
		}
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			for(StdStdApp StdStdApp : StdStdAppList){
				m_dao.update(StdStdApp);
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
	
}
