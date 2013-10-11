package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.SubContract;
import com.jlyw.hibernate.SubContractDAO;
import com.jlyw.util.KeyValueWithOperator;

public class SubContractManager {
private SubContractDAO m_dao = new SubContractDAO();
	
	/**
	 * 根据SubContract Id 查找 SubContract对象
	 * @param id: SubContract Id
	 * @return SubContract对象
	 */
	public SubContract findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条SubContract记录
	 * @param appSpecies SubContract对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(SubContract appSpecies){
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
	 * 更新一条SubContract记录
	 * @param appSpecies SubContract对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(SubContract appSpecies){
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
	 * 根据SubContract Id,删除SubContract对象
	 * @param id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			SubContract u = m_dao.findById(id);
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
	 * @return 分页后的SubContract列表
	 */
	public List<SubContract> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("SubContract", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 得到所有SubContract记录数
	 * @param arr 条件键值对
	 * @return SubContract记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("SubContract", arr);
	}
	/**
	 * 得到所有SubContract记录数
	 * @param arr 查询条件列表
	 * @return SubContract记录数
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("SubContract", arr);
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List<SubContract> findByExample(SubContract instance) {
		return m_dao.findByExample(instance);
	}
	
	/**
	 * 多条件查询
	 * @param arr
	 * @return
	 */
	public List<SubContract> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("SubContract", arr);
		}
		catch(Exception e){
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
	* 分页显示数据
	*@param queryString:查询语句（HQL）
	* @param arr 查询语句中?对应的值
	* @return 分页后的数据列表- List
	*/
	public List findPagedAllByHQL(String queryString, int currentPage, int pageSize, List<Object> arr){
		try{
			return m_dao.findPageAllByHQL(queryString, currentPage, pageSize, arr);
		}catch(Exception e){
			return null;
		}
	}
}
