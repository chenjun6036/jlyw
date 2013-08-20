package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.OriginalRecordExcel;
import com.jlyw.hibernate.OriginalRecordExcelDAO;
import com.jlyw.util.KeyValueWithOperator;

public class OriginalRecordExcelManager {
private OriginalRecordExcelDAO m_dao = new OriginalRecordExcelDAO();
	
	/**
	 * 根据OriginalRecordExcel Id 查找 OriginalRecordExcel对象
	 * @param id: OriginalRecordExcel Id
	 * @return OriginalRecordExcel对象
	 */
	public OriginalRecordExcel findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条OriginalRecordExcel记录
	 * @param appSpecies OriginalRecordExcel对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(OriginalRecordExcel appSpecies){
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
	 * 更新一条OriginalRecordExcel记录
	 * @param appSpecies OriginalRecordExcel对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(OriginalRecordExcel appSpecies){
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
	 * 根据OriginalRecordExcel Id,删除OriginalRecordExcel对象
	 * @param id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			OriginalRecordExcel u = m_dao.findById(id);
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
	 * @return 分页后的OriginalRecordExcel列表
	 */
	public List<OriginalRecordExcel> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("OriginalRecordExcel", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 得到所有OriginalRecordExcel记录数
	 * @param arr 条件键值对
	 * @return OriginalRecordExcel记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("OriginalRecordExcel", arr);
	}
	/**
	 * 得到所有OriginalRecordExcel记录数
	 * @param arr 查询条件列表
	 * @return OriginalRecordExcel记录数
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("OriginalRecordExcel", arr);
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List<OriginalRecordExcel> findByExample(OriginalRecordExcel instance) {
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
	* @return 分页后的数据列表- List<OriginalRecordExcel>
	*/
	public List<OriginalRecordExcel> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findPagedAllBySort("OriginalRecordExcel", currentPage, pageSize, orderby, asc, arr);
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
	* @return 分页后的数据列表- List<OriginalRecordExcel>
	*/
	public List<OriginalRecordExcel> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		try{
			return m_dao.findPagedAllBySort("OriginalRecordExcel", currentPage, pageSize, orderby, asc, arr);
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
	 * 按任意条件对查找，可指定排序字段
	 * @param TableName:从哪个表里查询
	 * @param orderby：按照哪个字段排序
	 * @param asc：true 增序 false 减序
	 * @param arr ：条件与值对
	 */
	public List<OriginalRecordExcel> findByPropertyBySort(String orderby, boolean asc, KeyValueWithOperator...arr){
		try{
			return m_dao.findByPropertyBySort("OriginalRecordExcel", orderby, asc, arr);
		}
		catch(Exception e){
			return null;
		}
	}
}
