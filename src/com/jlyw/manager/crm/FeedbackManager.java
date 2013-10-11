/**
 * 
 */
package com.jlyw.manager.crm;


import java.util.ArrayList;
import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.Customer;
import com.jlyw.hibernate.crm.CustomerFeedback;
import com.jlyw.hibernate.crm.CustomerFeedbackDAO;
import com.jlyw.hibernate.crm.PotentialCustomer;
import com.jlyw.manager.BaseTypeManager;
import com.jlyw.util.KeyValueWithOperator;

/**
 * @author xx
 *
 */
public class FeedbackManager {
private CustomerFeedbackDAO m_dao=new CustomerFeedbackDAO();
	/**
	 * 
	 */
	public CustomerFeedback FindById(int id)
	{
		return m_dao.findById(id);
	}
	
	public boolean save(CustomerFeedback customerfeedback){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			m_dao.save(customerfeedback);
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
	 * ����һ��CustomerFeedback��¼
	 * @param CustomerFeedback CustomerFeedback����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(CustomerFeedback customerfeedback){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(customerfeedback);
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
			CustomerFeedback u = m_dao.findById(id);
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
	public List findByExample(CustomerFeedback instance) {
		return m_dao.findByExample(instance);
		
	}
	
	/**
	 * ��������ѯ
	 * @param arr
	 * @return
	 */
	public List<CustomerFeedback> findByVarProperty(KeyValueWithOperator...arr){
		return m_dao.findByVarProperty("CustomerFeedback", arr);
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
			CustomerFeedback tmp=(CustomerFeedback)obj;
				result.add((tmp).getId().toString());
				result.add((tmp).getCustomer()==null?"/":tmp.getCustomer().getName());
				/*Integer ca=tmp.getComplainAbout();
				if(ca!=null)
				{
				int c=ca;
				switch (c) {
				case 0:
					result.add("����");
					break;
				case 1:
					result.add("����Ա");
					break;
				case 2:
					result.add("֤��");
					break;
				case 3:
					result.add("�շ�");
					break;
				case 4:
					result.add("����");
					break;

				default:
					result.add("����");
					break;
				}
				}
				else {
					result.add("");
				}*/
				 
				result.add(tmp.getCustomerContactorName()==null?"/":tmp.getCustomerContactorName());
				result.add(tmp.getFeedback()==null?"":tmp.getFeedback());
				result.add(tmp.getCreateTime()==null?"":tmp.getCreateTime().toString());
				
				Integer s=tmp.getStatus();
				if(s!=null)
				{
					int ss=s;
					switch (ss) {
					case 0:
						result.add("δ��ʼ");
						break;
					case 1:
						result.add("������");
						break;
					case 2:
						result.add("�ѽ���");
						break;
					default:
						result.add("/");
						break;
					}
				}
				else result.add("");
				
				/*Integer hl=tmp.getHandleLevel();
				if(hl!=null)
				{
					int h=hl;
					switch (h) {
					case 1:
						result.add("�� ");
						break;
					case 2:
						result.add("��");
						break;
					case 0:
						result.add("��");
						break;
					default:
						result.add("/");
						break;
					}
				}else {
					result.add("");
				}*/
				result.add(tmp.getPlanStartTime()==null?"":tmp.getPlanStartTime().toString());
				result.add(tmp.getPlanEndTime()==null?"":tmp.getPlanEndTime().toString());
				result.add(tmp.getSysUserByHandleSysUserId()==null?"":tmp.getSysUserByHandleSysUserId().getName());
				result.add(tmp.getMethod()==null?"":tmp.getMethod());
				result.add(tmp.getActulStartTime()==null?"":tmp.getActulStartTime().toString());
				result.add(tmp.getActulEndTime()==null?"":tmp.getActulEndTime().toString());
				result.add(tmp.getCustomerRequiredTime()==null?"":tmp.getCustomerRequiredTime().toString());
				Integer t=tmp.getReturnVisitType();
				if(t!=null)
				{
					int tt=t;
					switch (tt) {
					case 0:
						result.add("����");
						break;
					case 1:
						result.add("��Ͷ��");
						break;
					case 2:
						result.add("��������");
						break;
					case 3:
						result.add("����");
						break;
					case 4:
						result.add("����");
						break;
					case 5:
						result.add("����");
						break;

					default:result.add("����");
						break;
					}
				}
				else result.add("");
				result.add(tmp.getReturnVisitInfo()==null?"":tmp.getReturnVisitInfo());
				result.add(tmp.getRemark()==null?"":tmp.getRemark());
				result.add(tmp.getMark()==null?"":tmp.getMark().toString());
				result.add(tmp.getAnalysis()==null?"":tmp.getAnalysis());
				result.add(tmp.getSysUserByCreateSysUserId()==null?"":tmp.getSysUserByCreateSysUserId().getName());
		
				return result;
				
			}
			public List<String> formatTitle() {
				List<String> result=new ArrayList<String>();
				result.add("���");
				result.add("Ͷ�ߵ�λ");
				//result.add("Ͷ�߶���");
				result.add("Ͷ����");
				result.add("Ͷ������");
				result.add("Ͷ��ʱ��");
				result.add("״̬");
				//result.add("������");
				result.add("�ƻ���ʼʱ��");
				result.add("�ƻ�����ʱ��");
				result.add("��������");
				result.add("����취");
				result.add("ʵ�ʿ�ʼʱ��");
				result.add("ʵ�ʽ���ʱ��");
				result.add("�ͻ�Ҫ�����ʱ��");
				result.add("�ط���Ϣ����");
				result.add("�ط���Ϣ");
				result.add("������");
				result.add("�ͻ�����");
				result.add("�ۺϷ���");
				//result.add("����ʱ��");
				result.add("¼����");
				return result;
				
			}
	public FeedbackManager() {
		// TODO Auto-generated constructor stub
	}

}
