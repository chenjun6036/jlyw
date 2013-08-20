package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.BaseHibernateDAO;
import com.jlyw.hibernate.CommissionSheet;
import com.jlyw.hibernate.DetailList;
import com.jlyw.hibernate.DetailListDAO;
import com.jlyw.util.KeyValueWithOperator;

/**
 * 原始记录
 * @author Administrator
 *
 */
public class DetailListManager {
private DetailListDAO m_dao = new DetailListDAO();
	
	/**
	 * 根据DetailList Id 查找 DetailList对象
	 * @param id: DetailList Id
	 * @return DetailList对象
	 */
	public DetailList findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条DetailList记录
	 * @param appSpecies DetailList对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(DetailList appSpecies){
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
	 * 更新一条DetailList记录
	 * @param appSpecies DetailList对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(DetailList appSpecies){
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
	 * 根据DetailList Id,删除DetailList对象
	 * @param id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			DetailList u = m_dao.findById(id);
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
	 * @return 分页后的DetailList列表
	 */
	public List<DetailList> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("DetailList", currentPage,pageSize, arr);
		} catch (Exception e) {
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
	public List findPageAllByHQL(String queryString, int currentPage, int pageSize, Object...arr){
		try{
			return m_dao.findPageAllByHQL(queryString, currentPage, pageSize, arr);
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
	 * 得到记录总数
	 * @param queryString 查询语句（HQL）
	 * @param arr 查询语句中?对应的值
	 * @return
	 */
	public int getTotalCountByHQL(String queryString,List<Object> arr) {
		try{
			return m_dao.getTotalCountByHQL(queryString, arr);
		}catch(Exception ex){
			ex.printStackTrace();
			return 0;
		}
	}
	/**
	 * 得到所有DetailList记录数
	 * @param arr 条件键值对
	 * @return DetailList记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("DetailList", arr);
	}
	/**
	 * 得到所有DetailList记录数
	 * @param arr 查询条件列表
	 * @return DetailList记录数
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("DetailList", arr);
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
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List<DetailList> findByExample(DetailList instance) {
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
	* @return 分页后的数据列表- List<DetailList>
	*/
	public List<DetailList> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findPagedAllBySort("DetailList", currentPage, pageSize, orderby, asc, arr);
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
	* @return 分页后的数据列表- List<DetailList>
	*/
	public List<DetailList> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		try{
			return m_dao.findPagedAllBySort("DetailList", currentPage, pageSize, orderby, asc, arr);
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
	* 分页显示数据
	*@param queryString:查询语句（HQL）
	* @param arr 查询语句中?对应的值
	* @return 分页后的数据列表- List
	*/
	public List findByHQL(String queryString, List<Object> arr){
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
	public List<DetailList> findByPropertyBySort(String orderby, boolean asc, KeyValueWithOperator...arr){
		try{
			return m_dao.findByPropertyBySort("DetailList", orderby, asc, arr);
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
	public List<DetailList> findByPropertyBySort(String orderby, boolean asc, List<KeyValueWithOperator> arr){
		try{
			return m_dao.findByPropertyBySort("DetailList", orderby, asc, arr);
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
	public List<DetailList> findByVarProperty(KeyValueWithOperator...arr){
		return m_dao.findByVarProperty("DetailList", arr);
	}
	/**
	 * 插入一批DetailList记录
	 * @param oRecordList DetailList 对象列表
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean saveByBatch(List<DetailList> oRecordList){
		if(oRecordList == null){
			return false;
		}
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			for(DetailList oRecord : oRecordList){
				m_dao.save(oRecord);
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

}
