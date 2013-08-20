package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.Reason;
import com.jlyw.hibernate.ReasonDAO;
import com.jlyw.util.KeyValueWithOperator;

public class ReasonManager {
private ReasonDAO m_dao = new ReasonDAO();
	
	/**
	 * 根据Reason Id 查找 Reason对象
	 * @param id: Reason Id
	 * @return Reason对象
	 */
	public Reason findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条Reason记录
	 * @param appSpecies Reason对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(Reason appSpecies){
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
	 * 更新一条Reason记录
	 * @param appSpecies Reason对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(Reason appSpecies){
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
	 * 根据Reason Id,删除Reason对象
	 * @param id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			Reason u = m_dao.findById(id);
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
	 * @param list 条件键值对
	 * @return 分页后的Reason列表
	 */
	public List<Reason> findPagedAll(int currentPage, int pageSize, List<KeyValueWithOperator>list) {
		try {
			return m_dao.findPagedAll("Reason", currentPage,pageSize, list);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 得到所有Reason记录数
	 * @param arr 条件键值对
	 * @return Reason记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("Reason", arr);
	}
	/**
	 * 得到所有Reason记录数
	 * @param arr 查询条件列表
	 * @return Reason记录数
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("Reason", arr);
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List<Reason> findByExample(Reason instance) {
		return m_dao.findByExample(instance);
	}
	/**
	* 显示数据
	*@param queryString:查询语句（SQL）
	* @param arr 查询语句中?对应的值
	* @return 分页后的数据列表- List
	*/
	public List findBySQL(String queryString, Object...arr){
		try{
			return m_dao.findBySQL(queryString, arr);
		}catch(Exception e){
			e.printStackTrace();
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
	* @param arr:为查询条件的(键-值)对数组
	* @return 分页后的数据列表- List<Reason>
	*/
	public List<Reason> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findPagedAllBySort("Reason", currentPage, pageSize, orderby, asc, arr);
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
	* @return 分页后的数据列表- List<Reason>
	*/
	public List<Reason> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		try{
			return m_dao.findPagedAllBySort("Reason", currentPage, pageSize, orderby, asc, arr);
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
	public List<Reason> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("Reason", arr);
		}
		catch(Exception e){
			return null;
		}
	}
}
