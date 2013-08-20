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
	 * 更新一条CustomerServiceFee记录
	 * @param CustomerServiceFee CustomerServiceFee对象
	 * @return 更新成功，返回true；否则返回false
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
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List findByExample(CustomerServiceFee instance) {
		return m_dao.findByExample(instance);
		
	}
	
	/**
	 * 多条件查询
	 * @param arr
	 * @return
	 */
	public List<CustomerServiceFee> findByVarProperty(KeyValueWithOperator...arr){
		return m_dao.findByVarProperty("CustomerServiceFee", arr);
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
				result.add("现金");
				break;
			case 1:
				result.add("信用卡");
				break;
			case 2:
				result.add("其它");
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
				result.add("正在审核");
				break;
			case 2:
				result.add("通过审核");
				break;
			case 3:
				result.add("未通过");
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
		result.add("编号");
		result.add("委托单位");
		result.add("票据号");
		result.add("录入人");
		result.add("创建时间");
		result.add("支付日期");
		result.add("支付方式");
		result.add("费用科目");
		result.add("金额");
		result.add("服务费用支付人");
		result.add("备注");
		result.add("审核状态");
		//result.add("");
		
		return result;
		
	}
	
	public CustomerServiceFeeManager() {
		// TODO Auto-generated constructor stub
	}

}
