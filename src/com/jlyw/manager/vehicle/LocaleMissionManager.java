package com.jlyw.manager.vehicle;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.LocaleApplianceItem;
import com.jlyw.hibernate.LocaleApplianceItemDAO;
import com.jlyw.hibernate.LocaleMission;
import com.jlyw.hibernate.LocaleMissionDAO;
import com.jlyw.hibernate.Quotation;
import com.jlyw.hibernate.QuotationDAO;
import com.jlyw.hibernate.QuotationItem;
import com.jlyw.util.KeyValueWithOperator;

public class LocaleMissionManager {
private LocaleMissionDAO m_dao = new LocaleMissionDAO();
	
	/**
	 * ����User Id ���� User����
	 * @param id User Id
	 * @return LocaleMission����
	 */
	public LocaleMission findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��LocaleMission��¼
	 * @param LocaleMission LocaleMission����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(LocaleMission LocaleMission){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			m_dao.save(LocaleMission);
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
	 * ����һ��LocaleMission��¼
	 * @param LocaleMission LocaleMission����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(LocaleMission LocaleMission){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(LocaleMission);
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
	 * ����LocaleMission Id,ɾ��LocaleMission����
	 * @param id LocaleMission id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			LocaleMission u = m_dao.findById(id);
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
	 * @return ��ҳ���LocaleMission�б�
	 */
	public List<LocaleMission> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator... arr) {
		try {
			return m_dao.findPagedAll("LocaleMission", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * �õ�����LocaleMission��¼��
	 * @return LocaleMission��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("LocaleMission",arr);		
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List findByExample(LocaleMission instance) {
		return m_dao.findByExample(instance);
		
	}
	
	/**
	 * 
	 * @param arr
	 * @return
	 */
	public List<LocaleMission> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("LocaleMission", arr);
		}
		catch(Exception e){
			return null;
		}
	}
	
	/**
	* ��ҳ��ʾ����
	* @param currentPage
	* ��ǰҳ��, �� 1 ��ʼ
	* @param pageSize
	* ÿҳ��ʾ������
	* @param arr:Ϊ��ѯ������(��-ֵ)���б�
	* @return ��ҳ��������б�- List<CommissionSheet>
	*/
	public List<LocaleMission> findPagedAll(int currentPage, int pageSize, List<KeyValueWithOperator>arr){
		try{
			return m_dao.findPagedAll("LocaleMission", currentPage, pageSize, arr);
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * �õ�����CommissionSheet��¼��
	 * @param arr ��ѯ�����б�
	 * @return CommissionSheet��¼��
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("LocaleMission", arr);
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
	public List findPageAllByHQL(String queryString, int currentPage, int pageSize, List<Object> arr){
		try{
			return m_dao.findPageAllByHQL(queryString, currentPage, pageSize, arr);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
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
	 * �õ���¼����
	 * @param queryString ��ѯ��䣨HQL��
	 * @param arr ��ѯ�����?��Ӧ��ֵ
	 * @return
	 */
	public int getTotalCountByHQL(String queryString,List<Object> arr) {
		try{
			return m_dao.getTotalCountByHQL(queryString, arr);
		}catch(Exception ex){
			return 0;
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
	* @return ��ҳ��������б�- List<LocaleMission>
	*/
	public List<LocaleMission> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findPagedAllBySort("LocaleMission", currentPage, pageSize, orderby, asc, arr);
		}catch(Exception e){
			return null;
		}
	}
		/**
	* 
	*@param queryString:��ѯ��䣨HQL��
	* @param arr ��ѯ�����?��Ӧ��ֵ
	* @return ��ҳ��������б�- List
	*/
	public List findByHQL(String queryString, Object...arr){
		try{
			return m_dao.findByHQL(queryString, arr);
		}catch(Exception e){
			return null;
		}
	}
	
	
}
