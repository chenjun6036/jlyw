package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.VerifyAndAuthorize;
import com.jlyw.hibernate.VerifyAndAuthorizeDAO;
import com.jlyw.util.KeyValueWithOperator;

public class VerifyAndAuthorizeManager {
private VerifyAndAuthorizeDAO m_dao = new VerifyAndAuthorizeDAO();
	
	/**
	 * 根据VerifyAndAuthorize Id 查找 VerifyAndAuthorize对象
	 * @param id: VerifyAndAuthorize Id
	 * @return VerifyAndAuthorize对象
	 */
	public VerifyAndAuthorize findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条VerifyAndAuthorize记录
	 * @param appSpecies VerifyAndAuthorize对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(VerifyAndAuthorize appSpecies){
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
	 * 更新一条VerifyAndAuthorize记录
	 * @param appSpecies VerifyAndAuthorize对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(VerifyAndAuthorize appSpecies) throws Exception{
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(appSpecies);
			tran.commit();
			return true;
		} catch (Exception e) {
			tran.rollback();
			throw e;
		} finally {
			m_dao.closeSession();
		}
	}
	
	/**
	 * 根据VerifyAndAuthorize Id,删除VerifyAndAuthorize对象
	 * @param id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			VerifyAndAuthorize u = m_dao.findById(id);
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
	 * @return 分页后的VerifyAndAuthorize列表
	 */
	public List<VerifyAndAuthorize> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("VerifyAndAuthorize", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 得到所有VerifyAndAuthorize记录数
	 * @param arr 条件键值对
	 * @return VerifyAndAuthorize记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("VerifyAndAuthorize", arr);
	}
	/**
	 * 得到所有VerifyAndAuthorize记录数
	 * @param arr 查询条件列表
	 * @return VerifyAndAuthorize记录数
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("VerifyAndAuthorize", arr);
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List<VerifyAndAuthorize> findByExample(VerifyAndAuthorize instance) {
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
	* @return 分页后的数据列表- List<VerifyAndAuthorize>
	*/
	public List<VerifyAndAuthorize> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findPagedAllBySort("VerifyAndAuthorize", currentPage, pageSize, orderby, asc, arr);
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
	* @return 分页后的数据列表- List<VerifyAndAuthorize>
	*/
	public List<VerifyAndAuthorize> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		try{
			return m_dao.findPagedAllBySort("VerifyAndAuthorize", currentPage, pageSize, orderby, asc, arr);
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
	public List<VerifyAndAuthorize> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("VerifyAndAuthorize", arr);
		}
		catch(Exception e){
			return null;
		}
	}
}
