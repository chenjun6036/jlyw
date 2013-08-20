package com.jlyw.manager;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Transaction;

import com.jlyw.hibernate.Customer;
import com.jlyw.hibernate.CustomerContactor;
import com.jlyw.hibernate.CustomerContactorDAO;
import com.jlyw.hibernate.CustomerDAO;
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
	public boolean save(Customer Customer, CustomerContactor cusContactor,InsideContactor insideCon){
		Transaction tran = m_dao.getSession().beginTransaction();
		CustomerContactorDAO cusContactorDAO = new CustomerContactorDAO();
		InsideContactorDAO insideContactorDAO=new InsideContactorDAO();
		try {	
			m_dao.save(Customer);
			//cusContactor.setCustomerId(Customer.getId());//修改为
			cusContactor.setCustomer(Customer);
			cusContactorDAO.save(cusContactor);
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
			List<CustomerContactor> list = cusContactorDAO.findByVarProperty("CustomerContactor", new KeyValueWithOperator("customer.id", Customer.getId(), "="), new KeyValueWithOperator("name", ContactorName, "="));
			if(list==null||list.size()==0)
			{
				cusContactor = new CustomerContactor();
				//cusContactor.setCustomerId(Customer.getId());
				cusContactor.setCustomer(Customer);
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
		/////////////////////////////////////////////////////////////////////////////////
		Integer payVia=((Customer)temp[0]).getPayVia();
		if(payVia!=null)
		{
			int s=payVia;
			switch (s) {
				case 1:
				result.add("现金");
				break;
				case 2:
					result.add("支票");				
				break;
				case 3:
					result.add("付汇");
				break;
				case 4:
					result.add("POS机");
				break;
				case 5:
					result.add("其它");
				break;
			default:
				result.add("/");
				break;
			}
		}
		else result.add("/");
		Integer payType=((Customer)temp[0]).getPayType();
		if(payType!=null)
		{
			int t=payType;
			switch (t) {
				case 1:
				result.add("检后结账");
				break;
				case 2:
					result.add("周期结账");				
				break;
				case 3:
					result.add("预付款");
				break;
				case 4:
					result.add("其它");
				break;
			default:
				result.add("/");
				break;
			}
		}else result.add("/");
		Integer cycle=((Customer)temp[0]).getAccountCycle();
		if(cycle!=null)
		{
			result.add(cycle.toString()+"个月");
		}else result.add("/");
		Integer v=((Customer)temp[0]).getCustomerValueLevel();
		if(v!=null)
		{
			result.add(v.toString()+"级");
		}else result.add("/");
		Integer clevel=((Customer)temp[0]).getCustomerLevel();
		if(clevel!=null)
		{
			int s=clevel;
			switch (s) {
				case 1:
				result.add("VIP客户");
				break;
				case 2:
					result.add("重点客户");				
				break;
				case 3:
					result.add("重要客户");
				break;
				case 4:
					result.add("一般客户");
				break;
				case 5:
					result.add("特殊客户");
				break;
			default:
				result.add("/");
				break;
			}
		}
		else result.add("/");
		Integer trendency=((Customer)temp[0]).getTrendency();
		if(trendency!=null)
		{
			int t=trendency;
			switch (t) {
				case 1:
				result.add("升级可能");
				break;
				case 2:
					result.add("降级可能");				
				break;
				case 3:
					result.add("维持现状");
				break;
			default:
				result.add("/");
				break;
			}
		}else result.add("/");
		result.add(((Customer)temp[0]).getOutput()==null?"/":((Customer)temp[0]).getOutput().toString());
		result.add(((Customer)temp[0]).getOutputExpectation()==null?"/":((Customer)temp[0]).getOutputExpectation().toString());
		result.add(((Customer)temp[0]).getServiceFeeLimitation()==null?"/":((Customer)temp[0]).getServiceFeeLimitation().toString());
		
		/////////////////////////////////////////////////////////////////////////////////
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
	
	////////////////////////////////////////////////////////////////////////////////////
	
	////////////////////////////////////////////////////////////////////////////////////
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
		
		/////////////////////////
		result.add("付款方式");
		result.add("付款类型");
		result.add("结账周期");
		result.add("价值等级");
		result.add("企业等级");
		result.add("变动趋势");
		result.add("产值当年值");
		result.add("产值期望值");
		result.add("服务费用限值");
		////////////////////////
		result.add("单位联系人");
		result.add("联系人手机1");
		result.add("联系人手机2");
		result.add("联系人Email");
		
		return result;
	}
}
