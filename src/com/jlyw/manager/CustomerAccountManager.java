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
	 * 根据CustomerAccount Id 查找 CustomerAccount对象
	 * @param id: CustomerAccount Id
	 * @return CustomerAccount对象
	 */
	public CustomerAccount findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条CustomerAccount记录
	 * @param appSpecies CustomerAccount对象
	 * @return 插入成功，返回true；否则返回false
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
	 * 更新一条CustomerAccount记录
	 * @param appSpecies CustomerAccount对象
	 * @return 更新成功，返回true；否则返回false
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
	 * 根据CustomerAccount Id,删除CustomerAccount对象
	 * @param id
	 * @return 删除成功，返回true；否则返回false
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
	 * 分页函数
	 * @param currentPage 当前页码
	 * @param pageSize 每页的记录数
	 * @param arr 条件键值对
	 * @return 分页后的CustomerAccount列表
	 */
	public List<CustomerAccount> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("CustomerAccount", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 得到所有CustomerAccount记录数
	 * @param arr 条件键值对
	 * @return CustomerAccount记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("CustomerAccount", arr);
	}
	/**
	 * 得到所有CustomerAccount记录数
	 * @param arr 查询条件列表
	 * @return CustomerAccount记录数
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("CustomerAccount", arr);
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List<CustomerAccount> findByExample(CustomerAccount instance) {
		return m_dao.findByExample(instance);
	}
	
	/**
	* 分页显示数据
	* @param currentPage
	* 当前页码, 从 1 开始
	* @param pageSize
	* 每页显示数据量
	* @param orderby：按照哪个字段排序
	* @param asc：true 增序 false 减序
	* @param arr:为查询条件的(键-值)对数组
	* @return 分页后的数据列表- List<CustomerAccount>
	*/
	public List<CustomerAccount> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findPagedAllBySort("CustomerAccount", currentPage, pageSize, orderby, asc, arr);
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	* 分页显示数据
	* @param currentPage
	* 当前页码, 从 1 开始
	* @param pageSize
	* 每页显示数据量
	* @param orderby：按照哪个字段排序
	* @param asc：true 增序 false 减序
	* @param arr:为查询条件的(键-值)对列表
	* @return 分页后的数据列表- List<CustomerAccount>
	*/
	public List<CustomerAccount> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		try{
			return m_dao.findPagedAllBySort("CustomerAccount", currentPage, pageSize, orderby, asc, arr);
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	* 分页显示数据
	*@param queryString:查询语句（HQL）
	* @param arr 查询语句中?对应的值
	* @return 分页后的数据列表- List
	*/
	public List findByHQL(String queryString, Object...arr){
		try{
			return m_dao.findByHQL(queryString, arr);
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * 多条件查询
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
	 * 多条件查询
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
	 * 结账操作
	 * @param cSheetList   需要更新的CommissionSheet列表
	 * @param oldDetailList  老的费用清单，需注销
	 * @param newDetailList  重新生成的费用清单，保存
	 * @param cus       更新Customer中的账户余额信息
	 * @param cusAccount    新增CustomerAccount记录
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
