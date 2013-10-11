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
	 * 根据User Id 查找 User对象
	 * @param id User Id
	 * @return Customer对象
	 */
	public Customer findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条Customer记录
	 * @param Customer Customer对象
	 * @return 插入成功，返回true；否则返回false
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
	 * 插入一条Customer和CustomerContactor记录
	 * @param Customer Customer对象
	 * @param CustomerContactor cusContactor对象
	 * @return 插入成功，返回true；否则返回false
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
	 * 插入一条Customer和CustomerContactor记录
	 * @param Customer Customer对象
	 * @param CustomerContactor cusContactor对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(Customer Customer, CustomerContactor cusContactor,InsideContactor insideCon){
		Transaction tran = m_dao.getSession().beginTransaction();
		CustomerContactorDAO cusContactorDAO = new CustomerContactorDAO();
		InsideContactorDAO insideContactorDAO=new InsideContactorDAO();
		try {	
			m_dao.save(Customer);
			//cusContactor.setCustomer(Customer);//修改为
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
	 * 更新一条Customer记录
	 * @param Customer Customer对象
	 * @return 更新成功，返回true；否则返回false
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
	 * 更新一条Customer记录
	 * @param Customer Customer对象
	 * @param CustomerContactor cusContactor对象
	 * @return 更新成功，返回true；否则返回false
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
	 * 根据Customer Id,删除Customer对象
	 * @param id Customer id
	 * @return 删除成功，返回true；否则返回false
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
	 * 分页函数
	 * @param currentPage 当前页码
	 * @param pageSize 每页的记录数
	 * @return 分页后的Customer列表
	 */
	public List<Customer> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator... arr) {
		try {
			return m_dao.findPagedAll("Customer", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 得到所有Customer记录数
	 * @return Customer记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("Customer",arr);		
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List findByExample(Customer instance) {
		return m_dao.findByExample(instance);
		
	}
	
	/**
	 * 多条件查询
	 * @param arr
	 * @return
	 */
	public List<Customer> findByVarProperty(KeyValueWithOperator...arr){
		return m_dao.findByVarProperty("Customer", arr);
	}
	
		
	
	/**
	* 分页显示数据
	*@param queryString:查询语句（HQL）
	* @param currentPage
	* 当前页码, 从 1 开始
	* @param pageSize
	* 每页显示数据量
	* @param arr 查询语句中?对应的值
	* @return 分页后的数据列表- List
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
	 * 得到记录总数
	 * @param queryString 查询语句（HQL）
	 * @param arr 查询语句中?对应的值
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
	* 分页显示数据
	*@param queryString:查询语句（HQL）
	* @param currentPage
	* 当前页码, 从 1 开始
	* @param pageSize
	* 每页显示数据量
	* @param arr 查询语句中?对应的值
	* @return 分页后的数据列表- List
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
	 * 得到记录总数
	 * @param queryString 查询语句（HQL）
	 * @param arr 查询语句中?对应的值
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
	 * 合并委托单位
	 * @param FC   合并至单位
	 * @param Lc   被合并单位
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
			List<Reason> rList = rDAO.findByVarProperty("Reason", new KeyValueWithOperator("reason","合并委托单位","="), new KeyValueWithOperator("type", 22, "="));//查找注销原因
			if(rList.size() > 0){	//更新原因
				Reason reason = rList.get(0);
				reason.setCount(reason.getCount()+1);
				reason.setLastUse(today);
				rDAO.update(reason);
				LC.setReason(reason);	//注销原因
			}else{	//新建原因
				Reason reason = new Reason();
				reason.setCount(1);
				reason.setLastUse(today);
				reason.setReason("合并委托单位");
				reason.setStatus(0);
				reason.setType(22);	//注销客户
				rDAO.save(reason);
				LC.setReason(reason);	//注销原因
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
		result.add("序号");
		result.add("单位名称");
		result.add("单位名称英文");
		result.add("拼音简码");
		result.add("单位代码");
		result.add("地区");
		result.add("单位类型");
		result.add("单位地址");
		result.add("单位地址英文");
		result.add("单位电话");
		result.add("单位传真");
		result.add("邮政编码");
		result.add("单位状态");
		result.add("注销时间");
		result.add("注销原因");
		result.add("单位余额");
		result.add("开户银行");
		result.add("开户账号");
		result.add("企业分类");
		result.add("下场要求");
		result.add("证书要求");
		result.add("特殊要求");
		result.add("信用额度");
		result.add("备注");
		result.add("修改时间");
		result.add("修改人");
		result.add("内部联系人");
		result.add("单位联系人");
		result.add("联系人手机1");
		result.add("联系人手机2");
		result.add("联系人Email");
		
		return result;
	}
}
