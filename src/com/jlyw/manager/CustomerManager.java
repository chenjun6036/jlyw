package com.jlyw.manager;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.CommissionSheet;
import com.jlyw.hibernate.CommissionSheetDAO;
import com.jlyw.hibernate.Customer;
import com.jlyw.hibernate.CustomerContactor;
import com.jlyw.hibernate.CustomerContactorDAO;
import com.jlyw.hibernate.CustomerDAO;
import com.jlyw.hibernate.Reason;
import com.jlyw.hibernate.ReasonDAO;
import com.jlyw.hibernate.SysUser;
import com.jlyw.hibernate.crm.InsideContactor;
import com.jlyw.hibernate.crm.InsideContactorDAO;
import com.jlyw.util.KeyValueWithOperator;

public class CustomerManager {
private CustomerDAO m_dao = new CustomerDAO();
	
	/**
	 * ����User Id ���� User����
	 * @param id User Id
	 * @return Customer����
	 */
	public Customer findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��Customer��¼
	 * @param Customer Customer����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(Customer Customer){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			m_dao.save(Customer);
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
	 * ����һ��Customer��CustomerContactor��¼
	 * @param Customer Customer����
	 * @param CustomerContactor cusContactor����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(Customer Customer, CustomerContactor cusContactor){
		Transaction tran = m_dao.getSession().beginTransaction();
		CustomerContactorDAO cusContactorDAO = new CustomerContactorDAO();
		try {	
			m_dao.save(Customer);
			cusContactor.setCustomerId(Customer.getId());
			cusContactorDAO.save(cusContactor);
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
	 * ����һ��Customer��CustomerContactor��¼
	 * @param Customer Customer����
	 * @param CustomerContactor cusContactor����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(Customer Customer, CustomerContactor cusContactor,InsideContactor insideCon){
		Transaction tran = m_dao.getSession().beginTransaction();
		CustomerContactorDAO cusContactorDAO = new CustomerContactorDAO();
		InsideContactorDAO insideContactorDAO=new InsideContactorDAO();
		try {	
			m_dao.save(Customer);
			//cusContactor.setCustomer(Customer);//�޸�Ϊ
			cusContactor.setCustomerId(Customer.getId());
			cusContactorDAO.save(cusContactor);
			if(insideCon.getSysUser().getId()!=null&&insideCon.getRole()!=null)
			insideContactorDAO.save(insideCon);
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
	 * ����һ��Customer��¼
	 * @param Customer Customer����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(Customer Customer){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(Customer);
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
	 * ����һ��Customer��¼
	 * @param Customer Customer����
	 * @param CustomerContactor cusContactor����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(Customer Customer, String ContactorName, String Cellphone1, String Cellphone2){
		Transaction tran = m_dao.getSession().beginTransaction();
		CustomerContactorDAO cusContactorDAO = new CustomerContactorDAO();
		CustomerContactor cusContactor;
		try {			
			m_dao.update(Customer);
			List<CustomerContactor> list = cusContactorDAO.findByVarProperty("CustomerContactor", new KeyValueWithOperator("customerId", Customer.getId(), "="), new KeyValueWithOperator("name", ContactorName, "="));
			if(list==null||list.size()==0)
			{
				cusContactor = new CustomerContactor();
				cusContactor.setCustomerId(Customer.getId());
				cusContactor.setName(ContactorName);
				cusContactor.setCellphone1(Cellphone1);
				if (Cellphone2 != null && !Cellphone2.equals(""))
					cusContactor.setCellphone2(Cellphone2);
				cusContactor.setCount(1);
				cusContactor.setLastUse(new Timestamp(System.currentTimeMillis()));
				cusContactorDAO.save(cusContactor);
			}
			else
			{
				cusContactor = list.get(0);
				cusContactor.setCellphone1(Cellphone1);
				if (Cellphone2 != null && !Cellphone2.equals(""))
					cusContactor.setCellphone2(Cellphone2);
				cusContactorDAO.update(cusContactor);
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
	 * ����Customer Id,ɾ��Customer����
	 * @param id Customer id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			Customer u = m_dao.findById(id);
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
	 * @return ��ҳ���Customer�б�
	 */
	public List<Customer> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator... arr) {
		try {
			return m_dao.findPagedAll("Customer", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * �õ�����Customer��¼��
	 * @return Customer��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("Customer",arr);		
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List findByExample(Customer instance) {
		return m_dao.findByExample(instance);
		
	}
	
	/**
	 * ��������ѯ
	 * @param arr
	 * @return
	 */
	public List<Customer> findByVarProperty(KeyValueWithOperator...arr){
		return m_dao.findByVarProperty("Customer", arr);
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
	
	/***
	 * �ϲ�ί�е�λ
	 * @param FC   �ϲ�����λ
	 * @param Lc   ���ϲ���λ
	 * @return
	 */
	public boolean MergeCustomer(Customer FC, Customer LC, SysUser user){
		Transaction tran = m_dao.getSession().beginTransaction();
		CommissionSheetDAO cSheetDAO = new CommissionSheetDAO();
		try {
			List<CommissionSheet> cSheetList = cSheetDAO.findByVarProperty("CommissionSheet", new KeyValueWithOperator("customerId", LC.getId(), "="));
			for(CommissionSheet cSheet : cSheetList){
				cSheet.setCustomerId(FC.getId());
				cSheet.setCustomerAddress(FC.getAddress());
				cSheet.setCustomerName(FC.getName());
				cSheet.setSampleFrom(FC.getName());
				cSheet.setBillingTo(FC.getName());
				cSheet.setCustomerTel(FC.getTel());
				cSheet.setCustomerZipCode(FC.getZipCode());
				
				cSheetDAO.update(cSheet);
			}
			LC.setStatus(1);
			LC.setCancelDate(new Timestamp(System.currentTimeMillis()));
			LC.setModifyDate(new Timestamp(System.currentTimeMillis()));
			LC.setSysUserByModificatorId(user);
			ReasonDAO rDAO = new ReasonDAO();
			Timestamp today = new Timestamp(System.currentTimeMillis());
			List<Reason> rList = rDAO.findByVarProperty("Reason", new KeyValueWithOperator("reason","�ϲ�ί�е�λ","="), new KeyValueWithOperator("type", 22, "="));//����ע��ԭ��
			if(rList.size() > 0){	//����ԭ��
				Reason reason = rList.get(0);
				reason.setCount(reason.getCount()+1);
				reason.setLastUse(today);
				rDAO.update(reason);
				LC.setReason(reason);	//ע��ԭ��
			}else{	//�½�ԭ��
				Reason reason = new Reason();
				reason.setCount(1);
				reason.setLastUse(today);
				reason.setReason("�ϲ�ί�е�λ");
				reason.setStatus(0);
				reason.setType(22);	//ע���ͻ�
				rDAO.save(reason);
				LC.setReason(reason);	//ע��ԭ��
			}
			
			m_dao.update(LC);
			tran.commit();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			tran.rollback();
			return false;
		}finally{
			m_dao.closeSession();
		}
	}
	
	public List<String> formatExcel(Object obj){
		List<String> result = new ArrayList<String>();
		Object[] temp = (Object[]) obj;
		result.add(((Customer)temp[0]).getId().toString());
		result.add(((Customer)temp[0]).getName().toString());
		result.add(((Customer)temp[0]).getNameEn().toString());
		result.add(((Customer)temp[0]).getBrief().toString());
		result.add(((Customer)temp[0]).getCode().toString());
		result.add(((Customer)temp[0]).getRegion().getName().toString());
		result.add(((Customer)temp[0]).getCustomerType().toString());
		result.add(((Customer)temp[0]).getAddress().toString());
		result.add(((Customer)temp[0]).getAddressEn().toString());
		result.add(((Customer)temp[0]).getTel().toString());
		result.add(((Customer)temp[0]).getFax().toString());
		result.add(((Customer)temp[0]).getZipCode().toString());
		result.add(((Customer)temp[0]).getStatus().toString());
		result.add(((Customer)temp[0]).getCancelDate()==null?"":((Customer)temp[0]).getCancelDate().toString());
		result.add(((Customer)temp[0]).getReason()==null?"":((Customer)temp[0]).getReason().getReason().toString());
		result.add(((Customer)temp[0]).getBalance().toString());
		result.add(((Customer)temp[0]).getAccountBank().toString());
		result.add(((Customer)temp[0]).getAccount().toString());
		result.add(((Customer)temp[0]).getClassification().toString());
		result.add(((Customer)temp[0]).getFieldDemands()==null?"":((Customer)temp[0]).getFieldDemands().toString());
		result.add(((Customer)temp[0]).getCertificateDemands()==null?"":((Customer)temp[0]).getCertificateDemands().toString());
		result.add(((Customer)temp[0]).getSpecialDemands()==null?"":((Customer)temp[0]).getSpecialDemands().toString());
		result.add(((Customer)temp[0]).getCreditAmount().toString());
		result.add(((Customer)temp[0]).getRemark()==null?"":((Customer)temp[0]).getRemark().toString());
		result.add(((Customer)temp[0]).getModifyDate().toString());
		result.add(((Customer)temp[0]).getSysUserByModificatorId().getName().toString());
		result.add(((Customer)temp[0]).getSysUserByInsideContactorId()==null?"":((Customer)temp[0]).getSysUserByInsideContactorId().getName().toString());
		if(((CustomerContactor)temp[1])!=null)
		{
			result.add(((CustomerContactor)temp[1]).getName().toString());
			result.add(((CustomerContactor)temp[1]).getCellphone1().toString());
			result.add(((CustomerContactor)temp[1]).getCellphone2()==null?"":((CustomerContactor)temp[1]).getCellphone2().toString());
			result.add(((CustomerContactor)temp[1]).getEmail()==null?"":((CustomerContactor)temp[1]).getEmail().toString());
		}
		else
		{
			result.add("");
			result.add("");
			result.add("");
			result.add("");
		}
		
		return result;
	}
	
	public List<String> formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("���");
		result.add("��λ����");
		result.add("��λ����Ӣ��");
		result.add("ƴ������");
		result.add("��λ����");
		result.add("����");
		result.add("��λ����");
		result.add("��λ��ַ");
		result.add("��λ��ַӢ��");
		result.add("��λ�绰");
		result.add("��λ����");
		result.add("��������");
		result.add("��λ״̬");
		result.add("ע��ʱ��");
		result.add("ע��ԭ��");
		result.add("��λ���");
		result.add("��������");
		result.add("�����˺�");
		result.add("��ҵ����");
		result.add("�³�Ҫ��");
		result.add("֤��Ҫ��");
		result.add("����Ҫ��");
		result.add("���ö��");
		result.add("��ע");
		result.add("�޸�ʱ��");
		result.add("�޸���");
		result.add("�ڲ���ϵ��");
		result.add("��λ��ϵ��");
		result.add("��ϵ���ֻ�1");
		result.add("��ϵ���ֻ�2");
		result.add("��ϵ��Email");
		
		return result;
	}
}
