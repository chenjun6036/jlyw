/**
 * 
 */
package com.jlyw.manager.crm;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.crm.PotentialCustomer;
import com.jlyw.hibernate.crm.PotentialCustomerDAO;
import com.jlyw.manager.BaseTypeManager;
import com.jlyw.util.KeyValueWithOperator;


/**
 * @author xx
 *
 */
public class PotentialCustomerManager {
private PotentialCustomerDAO m_dao=new PotentialCustomerDAO();
	/**
	 * 
	 */
	public PotentialCustomer FindById(int id)
	{
		return m_dao.findById(id);
	}
	
	public boolean save(PotentialCustomer customer){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			m_dao.save(customer);
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
	 * ����һ��CustomerServiceFee��¼
	 * @param CustomerServiceFee CustomerServiceFee����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(PotentialCustomer customer){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(customer);
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
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			PotentialCustomer u = m_dao.findById(id);
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
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List findByExample(PotentialCustomer instance) {
		return m_dao.findByExample(instance);
		
	}
	
	/**
	 * ��������ѯ
	 * @param arr
	 * @return
	 */
	public List<PotentialCustomer> findByVarProperty(KeyValueWithOperator...arr){
		return m_dao.findByVarProperty("PotentialCustomer", arr);
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
	public List findByHQL(String queryString, List<Object> arr){
		try{
			return m_dao.findByHQL(queryString, arr);
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
	public int getTotalCountByHQL(String queryString,List<Object> arr) {
		try{
			return m_dao.getTotalCountByHQL(queryString, arr);
		}catch(Exception ex){
			return 0;
		}
	}
	public List<String> formatExcel(Object obj) {
List<String> result=new ArrayList<String>();
	PotentialCustomer tmp=(PotentialCustomer)obj;
		result.add((tmp).getId().toString());
		result.add((tmp).getName());
		result.add((tmp).getAddress());
		result.add((tmp).getRegion()==null?"/":(tmp).getRegion().getName());
		result.add((tmp).getNameEn());
		Integer s=(tmp).getStatus();
		if(s!=null)
		result.add(s.equals(0)==true?"����":"ע��");
		else result.add("/");
		Integer f=(tmp).getPotentialCustomerFrom();
		if(f!=null)
		{
			int ff=f;
			switch (ff) {
			case 1:
				result.add("�绰");
				break;
			case 2:
				result.add("����");
				break;
			case 3:
				result.add("����");
				break;
			case 4:
				result.add("����");
				break;

			default:
				result.add("����");
				break;
			}
		}
		
		Integer c=(tmp).getCooperationIntension();
		if(c!=null)
		{
			int cc=c;
			switch (cc) {
			case 1:
				result.add("��ȷ��������");
				break;
			case 2:
				result.add("������ϵ��������");
				break;
			case 3:
				result.add("������������ȷ");
				break;
			case 4:
				result.add("���Ծܾ�");
				break;

			default:
				result.add("����");
				break;
			}
		}
		BaseTypeManager btm=new BaseTypeManager();
		Integer ind=(tmp).getIndustry();
		if(ind!=null)
		result.add(btm.findById(ind)==null?"/":btm.findById(ind).getName().toString());
		else 
			result.add("/");
		return result;
		
	}
	public List<String> formatTitle() {
		List<String> result=new ArrayList<String>();
		result.add("���");
		result.add("����");
		result.add("��ַ");
		result.add("����");
		result.add("Ӣ������");
		result.add("״̬");
		result.add("��Դ");
		result.add("��������");
		result.add("��ҵ");
		return result;
		
	}
	public PotentialCustomerManager() {
		// TODO Auto-generated constructor stub
	}

}
