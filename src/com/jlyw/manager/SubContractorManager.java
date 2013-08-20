package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.SubContractor;
import com.jlyw.hibernate.SubContractorDAO;
import com.jlyw.util.KeyValueWithOperator;

/**
 * 转包方Manager
 * @author Administrator
 *
 */
public class SubContractorManager {
private SubContractorDAO m_dao = new SubContractorDAO();
	
	/**
	 * 根据SubContractor Id 查找 SubContractor对象
	 * @param id: SubContractor Id
	 * @return SubContractor对象
	 */
	public SubContractor findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条SubContractor记录
	 * @param appSpecies SubContractor对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(SubContractor appSpecies){
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
	 * 更新一条SubContractor记录
	 * @param appSpecies SubContractor对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(SubContractor appSpecies){
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
	 * 根据SubContractor Id,删除SubContractor对象
	 * @param id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			SubContractor u = m_dao.findById(id);
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
	 * @return 分页后的SubContractor列表
	 */
	public List<SubContractor> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("SubContractor", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 得到所有SubContractor记录数
	 * @param arr 条件键值对
	 * @return SubContractor记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("SubContractor", arr);
	}
	/**
	 * 得到所有SubContractor记录数
	 * @param arr 查询条件列表
	 * @return SubContractor记录数
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("SubContractor", arr);
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List<SubContractor> findByExample(SubContractor instance) {
		return m_dao.findByExample(instance);
	}
	
	
	/**
	* 通过HQL查找数据
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
	 * 多条件查找
	 * @param arr
	 * @return
	 */
	public List<SubContractor> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("SubContractor", arr);
		}
		catch(Exception e){
			return null;
		}
	}
}
