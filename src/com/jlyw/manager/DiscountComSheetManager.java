package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.CommissionSheet;
import com.jlyw.hibernate.DetailList;
import com.jlyw.hibernate.DetailListDAO;
import com.jlyw.hibernate.Discount;
import com.jlyw.hibernate.DiscountComSheet;
import com.jlyw.hibernate.DiscountComSheetDAO;
import com.jlyw.hibernate.DiscountDAO;
import com.jlyw.hibernate.OriginalRecord;
import com.jlyw.util.KeyValueWithOperator;

public class DiscountComSheetManager {
private DiscountComSheetDAO m_dao = new DiscountComSheetDAO();
	
	/**
	 * 根据DiscountComSheet Id 查找 DiscountComSheet对象
	 * @param id: DiscountComSheet Id
	 * @return DiscountComSheet对象
	 */
	public DiscountComSheet findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条DiscountComSheet记录
	 * @param appSpecies DiscountComSheet对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(DiscountComSheet appSpecies){
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
	 * 更新一条DiscountComSheet记录
	 * @param appSpecies DiscountComSheet对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(DiscountComSheet appSpecies){
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
	 * 根据DiscountComSheet Id,删除DiscountComSheet对象
	 * @param id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			DiscountComSheet u = m_dao.findById(id);
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
	 * @return 分页后的DiscountComSheet列表
	 */
	public List<DiscountComSheet> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("DiscountComSheet", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 分页函数
	 * @param currentPage 当前页码
	 * @param pageSize 每页的记录数
	 * @param arr 条件键值对
	 * @return 分页后的DiscountComSheet列表
	 */
	public List<DiscountComSheet> findPagedAll(int currentPage, int pageSize, List<KeyValueWithOperator> arr) {
		try {
			return m_dao.findPagedAll("DiscountComSheet", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 得到所有DiscountComSheet记录数
	 * @param arr 条件键值对
	 * @return DiscountComSheet记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("DiscountComSheet", arr);
	}
	/**
	 * 得到所有DiscountComSheet记录数
	 * @param arr 查询条件列表
	 * @return DiscountComSheet记录数
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("DiscountComSheet", arr);
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
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List<DiscountComSheet> findByExample(DiscountComSheet instance) {
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
	* @return 分页后的数据列表- List<DiscountComSheet>
	*/
	public List<DiscountComSheet> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findPagedAllBySort("DiscountComSheet", currentPage, pageSize, orderby, asc, arr);
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
	* @return 分页后的数据列表- List<DiscountComSheet>
	*/
	public List<DiscountComSheet> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		try{
			return m_dao.findPagedAllBySort("DiscountComSheet", currentPage, pageSize, orderby, asc, arr);
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
	public List<DiscountComSheet> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("DiscountComSheet", arr);
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
	public List<DiscountComSheet> findByVarProperty(List<KeyValueWithOperator> arr){
		try{
			return m_dao.findByVarProperty("DiscountComSheet", arr);
		}
		catch(Exception e){
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
	public List<DiscountComSheet> findByPropertyBySort(String orderby, boolean asc, KeyValueWithOperator...arr){
		return m_dao.findByPropertyBySort("DiscountComSheet", orderby, asc, arr);
	}
	
	/**

	*@param queryString:查询语句（HQL）

	* @param arr 查询语句中?对应的值
	* @return 分页后的数据列表- List
	*/
	public List findByHQL(String queryString, List<Object> arr){
		try{
			return m_dao.findByHQL(queryString, arr);
		}catch(Exception e){
			e.printStackTrace();
			return null;
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
	 * 生成折扣申请单：批量保存折扣- 委托单表
	 * 
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean saveByBatch(List<DiscountComSheet> discountComSheetList,Discount discount) throws Exception{
		if(discountComSheetList == null || discount == null){
			throw new Exception("参数为空");
		}
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			DiscountDAO dDAO = new DiscountDAO();	//折扣表的DAO 
			dDAO.save(discount);
			for(int i = 0; i < discountComSheetList.size(); i++){
				DiscountComSheet Com = discountComSheetList.get(i);
				Com.setDiscount(discount);
				m_dao.save(Com);
			}
			tran.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			tran.rollback();
			throw e;
		} finally {
			m_dao.closeSession();
		}
	}
}
