package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.CommissionSheet;
import com.jlyw.hibernate.CommissionSheetDAO;
import com.jlyw.hibernate.Customer;
import com.jlyw.hibernate.CustomerAccount;
import com.jlyw.hibernate.CustomerAccountDAO;
import com.jlyw.hibernate.CustomerDAO;
import com.jlyw.hibernate.DetailList;
import com.jlyw.hibernate.DetailListDAO;
import com.jlyw.util.KeyValueWithOperator;

public class CustomerAccountManager {
private CustomerAccountDAO m_dao = new CustomerAccountDAO();
	
	/**
	 * ����CustomerAccount Id ���� CustomerAccount����
	 * @param id: CustomerAccount Id
	 * @return CustomerAccount����
	 */
	public CustomerAccount findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��CustomerAccount��¼
	 * @param appSpecies CustomerAccount����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(CustomerAccount appSpecies){
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
	 * ����һ��CustomerAccount��¼
	 * @param appSpecies CustomerAccount����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(CustomerAccount appSpecies){
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
	 * ����CustomerAccount Id,ɾ��CustomerAccount����
	 * @param id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			CustomerAccount u = m_dao.findById(id);
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
	 * @param arr ������ֵ��
	 * @return ��ҳ���CustomerAccount�б�
	 */
	public List<CustomerAccount> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("CustomerAccount", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * �õ�����CustomerAccount��¼��
	 * @param arr ������ֵ��
	 * @return CustomerAccount��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("CustomerAccount", arr);
	}
	/**
	 * �õ�����CustomerAccount��¼��
	 * @param arr ��ѯ�����б�
	 * @return CustomerAccount��¼��
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("CustomerAccount", arr);
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List<CustomerAccount> findByExample(CustomerAccount instance) {
		return m_dao.findByExample(instance);
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
	* @return ��ҳ��������б�- List<CustomerAccount>
	*/
	public List<CustomerAccount> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findPagedAllBySort("CustomerAccount", currentPage, pageSize, orderby, asc, arr);
		}catch(Exception e){
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
	* @return ��ҳ��������б�- List<CustomerAccount>
	*/
	public List<CustomerAccount> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		try{
			return m_dao.findPagedAllBySort("CustomerAccount", currentPage, pageSize, orderby, asc, arr);
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	* ��ҳ��ʾ����
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
	 * ��������ѯ
	 * @param arr
	 * @return
	 */
	public List<CustomerAccount> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("CustomerAccount", arr);
		}
		catch(Exception e){
			return null;
		}
	}
	
	/**
	 * ��������ѯ
	 * @param arr
	 * @return
	 */
	public List<CustomerAccount> findByVarProperty(List<KeyValueWithOperator> arr){
		try{
			return m_dao.findByVarProperty("CustomerAccount", arr);
		}
		catch(Exception e){
			return null;
		}
	}
	
	/**
	 * ���˲���
	 * @param cSheetList   ��Ҫ���µ�CommissionSheet�б�
	 * @param oldDetailList  �ϵķ����嵥����ע��
	 * @param newDetailList  �������ɵķ����嵥������
	 * @param cus       ����Customer�е��˻������Ϣ
	 * @param cusAccount    ����CustomerAccount��¼
	 * @return
	 */
	public boolean saveByBatch(List<CommissionSheet> cSheetList, List<DetailList> oldDetailListList, DetailList newDetailList, Customer cus, CustomerAccount cusAccount){
		CommissionSheetDAO cSheetDAO = new CommissionSheetDAO();
		DetailListDAO detailListDAO = new DetailListDAO();
		CustomerDAO cusDAO = new CustomerDAO(); 
		CustomerAccountDAO customerAccountDAO = new CustomerAccountDAO();
		Transaction tran = m_dao.getSession().beginTransaction();
		try{
			
			for(CommissionSheet cSheet : cSheetList){
				cSheetDAO.update(cSheet);
			}
			if(oldDetailListList != null && oldDetailListList.size() > 0){
				for(DetailList temp : oldDetailListList){
					detailListDAO.update(temp);
				}
			}
			detailListDAO.save(newDetailList);
			cusDAO.update(cus);
			if(cusAccount!=null){
				customerAccountDAO.save(cusAccount);
			}
			tran.commit();
			return true;
		}catch(Exception e){
			tran.rollback();
			return false;
		}finally {
			m_dao.closeSession();
		}
		
	}
}
