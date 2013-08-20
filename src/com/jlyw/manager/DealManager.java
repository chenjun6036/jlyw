package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.Deal;
import com.jlyw.hibernate.DealDAO;
import com.jlyw.hibernate.DealItem;
import com.jlyw.hibernate.DealItemDAO;
import com.jlyw.hibernate.Quotation;
import com.jlyw.util.KeyValueWithOperator;

public class DealManager {
private DealDAO m_dao = new DealDAO();
	
	/**
	 * ����User Id ���� User����
	 * @param id User Id
	 * @return Deal����
	 */
	public Deal findById(String id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��Deal��¼
	 * @param Deal Deal����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(Deal Deal){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			m_dao.save(Deal);
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
	 * ����һ��Deal��¼
	 * @param Deal Deal����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(Deal Deal){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(Deal);
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
	 * ����Deal Id,ɾ��Deal����
	 * @param id Deal id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(String id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			Deal u = m_dao.findById(id);
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
	 * @return ��ҳ���Deal�б�
	 */
	public List<Deal> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator... arr) {
		try {
			return m_dao.findPagedAll("Deal", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * �õ�����Deal��¼��
	 * @return Deal��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("Deal",arr);		
	}
	
	/**
	 * �õ�����Deal��¼��
	 * @return Deal��¼��
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("Deal",arr);		
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List findByExample(Deal instance) {
		return m_dao.findByExample(instance);
		
	}
	
	/**
	 * ��������ѯ
	 * @param arr
	 * @return
	 */
	public List<Deal> findByVarProperty(KeyValueWithOperator...arr){
		return m_dao.findByVarProperty("Deal", arr);
	}
	
/**
	
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
	* ��ҳ��ʾ����
	* @param currentPage
	* ��ǰҳ��, �� 1 ��ʼ
	* @param pageSize
	* ÿҳ��ʾ������
	* @param orderby�������ĸ��ֶ�����
	* @param asc��true ���� false ����
	* @param arr:Ϊ��ѯ������(��-ֵ)���б�
	* @return ��ҳ��������б�- List<Quotation>
	*/
	public List<Deal> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator> arr){
		try{	
			return m_dao.findPagedAllBySort("Deal", currentPage, pageSize, orderby, asc, arr);
		}catch(Exception ex){
			return null;
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
	
	
	public boolean savaByBatch(Deal deal, List<DealItem> dealItemList){
		if(dealItemList == null || deal==null){
			return false;
		}
		DealItemDAO dDAO = new DealItemDAO(); 
		
		Transaction tran = m_dao.getSession().beginTransaction();		
		try {			
			m_dao.save(deal);			
			for(int i = 0; i < dealItemList.size(); i++){
				DealItem dealItem = dealItemList.get(i);
				dDAO.save(dealItem);				
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
