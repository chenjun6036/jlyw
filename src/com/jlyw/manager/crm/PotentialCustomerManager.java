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
	 * 更新一条CustomerServiceFee记录
	 * @param CustomerServiceFee CustomerServiceFee对象
	 * @return 更新成功，返回true；否则返回false
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
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List findByExample(PotentialCustomer instance) {
		return m_dao.findByExample(instance);
		
	}
	
	/**
	 * 多条件查询
	 * @param arr
	 * @return
	 */
	public List<PotentialCustomer> findByVarProperty(KeyValueWithOperator...arr){
		return m_dao.findByVarProperty("PotentialCustomer", arr);
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
	public List findByHQL(String queryString, List<Object> arr){
		try{
			return m_dao.findByHQL(queryString, arr);
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
	PotentialCustomer tmp=(PotentialCustomer)obj;
		result.add((tmp).getId().toString());
		result.add((tmp).getName());
		result.add((tmp).getAddress());
		result.add((tmp).getRegion()==null?"/":(tmp).getRegion().getName());
		result.add((tmp).getNameEn());
		Integer s=(tmp).getStatus();
		if(s!=null)
		result.add(s.equals(0)==true?"正常":"注销");
		else result.add("/");
		Integer f=(tmp).getPotentialCustomerFrom();
		if(f!=null)
		{
			int ff=f;
			switch (ff) {
			case 1:
				result.add("电话");
				break;
			case 2:
				result.add("传真");
				break;
			case 3:
				result.add("来访");
				break;
			case 4:
				result.add("其它");
				break;

			default:
				result.add("其它");
				break;
			}
		}
		
		Integer c=(tmp).getCooperationIntension();
		if(c!=null)
		{
			int cc=c;
			switch (cc) {
			case 1:
				result.add("明确合作意向");
				break;
			case 2:
				result.add("初步联系、有意向");
				break;
			case 3:
				result.add("观望、意向不明确");
				break;
			case 4:
				result.add("明显拒绝");
				break;

			default:
				result.add("其它");
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
		result.add("编号");
		result.add("名称");
		result.add("地址");
		result.add("区域");
		result.add("英文名称");
		result.add("状态");
		result.add("来源");
		result.add("合作意向");
		result.add("行业");
		return result;
		
	}
	public PotentialCustomerManager() {
		// TODO Auto-generated constructor stub
	}

}
