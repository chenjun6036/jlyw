/**
 * 
 */
package com.jlyw.manager.crm;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.crm.CustomerServiceFee;
import com.jlyw.hibernate.crm.CustomerServiceFeeDAO;
import com.jlyw.util.KeyValueWithOperator;


/**
 * @author xx
 *
 */
public class CustomerServiceFeeManager {
private CustomerServiceFeeDAO m_dao=new CustomerServiceFeeDAO();
	/**
	 * 
	 */
	public CustomerServiceFee FindById(int id)
	{
		return m_dao.findById(id);
	}
	
	public boolean save(CustomerServiceFee customerfee){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			m_dao.save(customerfee);
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
	public boolean update(CustomerServiceFee customerfee){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(customerfee);
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
			CustomerServiceFee u = m_dao.findById(id);
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
	public List findByExample(CustomerServiceFee instance) {
		return m_dao.findByExample(instance);
		
	}
	
	/**
	 * ��������ѯ
	 * @param arr
	 * @return
	 */
	public List<CustomerServiceFee> findByVarProperty(KeyValueWithOperator...arr){
		return m_dao.findByVarProperty("CustomerServiceFee", arr);
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
		CustomerServiceFee tmp=(CustomerServiceFee)obj;
		result.add(tmp.getId().toString());
		result.add(tmp.getCustomer()==null?"":tmp.getCustomer().getName());
		result.add(tmp.getBillNum()==null?"":tmp.getBillNum());
		result.add(tmp.getSysUserByCreateSysUserId()==null?"":tmp.getSysUserByCreateSysUserId().getName());
		result.add(tmp.getCreateTime()==null?"":tmp.getCreateTime().toString());
		result.add(tmp.getPaidTime()==null?"":tmp.getPaidTime().toString());
		Integer w=tmp.getPaidVia();
		if(w!=null)
		{
			int ww=w;
			switch (ww) {
			case 0:
				result.add("�ֽ�");
				break;
			case 1:
				result.add("���ÿ�");
				break;
			case 2:
				result.add("����");
				break;
			default:
				break;
			}
			
		}
		else result.add("/");
		result.add(tmp.getPaidSubject()==null?"":tmp.getPaidSubject());
		result.add(tmp.getMoney()==null?"":tmp.getMoney().toString());
		result.add(tmp.getSysUserByPaidSysUserId()==null?"":tmp.getSysUserByPaidSysUserId().getName());
		result.add(tmp.getRemark()==null?"":tmp.getRemark());
		Integer s=tmp.getStatus();
		if(s!=null)
		{
			int ss=s;
			switch (ss) {
			case 1:
				result.add("�������");
				break;
			case 2:
				result.add("ͨ�����");
				break;
			case 3:
				result.add("δͨ��");
			default:
				break;
			}
			
		}
		else
		result.add("/");
		return result;
	}
	
	public List<String> formatTitle() {
		List<String> result=new ArrayList<String>();
		result.add("���");
		result.add("ί�е�λ");
		result.add("Ʊ�ݺ�");
		result.add("¼����");
		result.add("����ʱ��");
		result.add("֧������");
		result.add("֧����ʽ");
		result.add("���ÿ�Ŀ");
		result.add("���");
		result.add("�������֧����");
		result.add("��ע");
		result.add("���״̬");
		//result.add("");
		
		return result;
		
	}
	
	public CustomerServiceFeeManager() {
		// TODO Auto-generated constructor stub
	}

}
