package com.jlyw.manager.quotation;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.Quotation;
import com.jlyw.hibernate.QuotationDAO;
import com.jlyw.util.KeyValueWithOperator;

public class QuotationManager {
private QuotationDAO m_dao = new QuotationDAO();
	
	/**
	 * 根据Quotation Id 查找 Quotation对象
	 * @param id: Quotation Id
	 * @return Quotation对象
	 */
	public Quotation findById(String id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条Quotation记录
	 * @param appSpecies Quotation对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(Quotation appSpecies){
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
	 * 更新一条Quotation记录
	 * @param appSpecies Quotation对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(Quotation appSpecies){
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
	 * 根据Quotation Id,删除Quotation对象
	 * @param id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(String id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			Quotation u = m_dao.findById(id);
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
	public List<Object> findBySQL(String queryString, Object...arr){
		return m_dao.findBySQL(queryString, arr);
	}
	/**
	 * 分页函数
	 * @param currentPage 当前页码
	 * @param pageSize 每页的记录数
	 * @param arr 条件键值对
	 * @return 分页后的Quotation列表
	 */
	public List<Quotation> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("Quotation", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 得到所有Quotation记录数
	 * @param arr 条件键值对
	 * @return Quotation记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("Quotation", arr);
	}
	/**
	 * 得到所有Quotation记录数
	 * @param arr 查询条件列表
	 * @return Quotation记录数
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("Quotation", arr);
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List<Quotation> findByExample(Quotation instance) {
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
	* @return 分页后的数据列表- List<Quotation>
	*/
	public List<Quotation> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findPagedAllBySort("Quotation", currentPage, pageSize, orderby, asc, arr);
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
	* @return 分页后的数据列表- List<Quotation>
	*/
	public List<Quotation> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		try{
			return m_dao.findPagedAllBySort("Quotation", currentPage, pageSize, orderby, asc, arr);
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	
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
	 * 	* @param orderby：按照哪个字段排序
	* @param asc：true 增序 false 减序
	 * @param arr
	 * @return
	 */
	public List<Quotation> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("Quotation", arr);
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
	public List<Quotation> findByVarProperty(String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("Quotation", arr);
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
	public List<Quotation> findByVarProperty(List<KeyValueWithOperator> arr){
		try{
			return m_dao.findByVarProperty("Quotation", arr);
		}
		catch(Exception e){
			return null;
		}
	}
}
