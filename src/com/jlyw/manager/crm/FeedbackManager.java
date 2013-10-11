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
	 * 更新一条CustomerFeedback记录
	 * @param CustomerFeedback CustomerFeedback对象
	 * @return 更新成功，返回true；否则返回false
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
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List findByExample(CustomerFeedback instance) {
		return m_dao.findByExample(instance);
		
	}
	
	/**
	 * 多条件查询
	 * @param arr
	 * @return
	 */
	public List<CustomerFeedback> findByVarProperty(KeyValueWithOperator...arr){
		return m_dao.findByVarProperty("CustomerFeedback", arr);
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
			CustomerFeedback tmp=(CustomerFeedback)obj;
				result.add((tmp).getId().toString());
				result.add((tmp).getCustomer()==null?"/":tmp.getCustomer().getName());
				/*Integer ca=tmp.getComplainAbout();
				if(ca!=null)
				{
				int c=ca;
				switch (c) {
				case 0:
					result.add("部门");
					break;
				case 1:
					result.add("检验员");
					break;
				case 2:
					result.add("证书");
					break;
				case 3:
					result.add("收费");
					break;
				case 4:
					result.add("其它");
					break;

				default:
					result.add("其它");
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
						result.add("未开始");
						break;
					case 1:
						result.add("处理中");
						break;
					case 2:
						result.add("已结束");
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
						result.add("中 ");
						break;
					case 2:
						result.add("低");
						break;
					case 0:
						result.add("高");
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
						result.add("批评");
						break;
					case 1:
						result.add("再投诉");
						break;
					case 2:
						result.add("新增需求");
						break;
					case 3:
						result.add("表扬");
						break;
					case 4:
						result.add("建议");
						break;
					case 5:
						result.add("其它");
						break;

					default:result.add("其它");
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
				result.add("编号");
				result.add("投诉单位");
				//result.add("投诉对象");
				result.add("投诉人");
				result.add("投诉内容");
				result.add("投诉时间");
				result.add("状态");
				//result.add("处理级别");
				result.add("计划开始时间");
				result.add("计划结束时间");
				result.add("处理负责人");
				result.add("处理办法");
				result.add("实际开始时间");
				result.add("实际结束时间");
				result.add("客户要求完成时间");
				result.add("回访信息类型");
				result.add("回访信息");
				result.add("处理结果");
				result.add("客户评分");
				result.add("综合分析");
				//result.add("创建时间");
				result.add("录入人");
				return result;
				
			}
	public FeedbackManager() {
		// TODO Auto-generated constructor stub
	}

}
