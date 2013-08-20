package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.CommissionSheet;
import com.jlyw.hibernate.CommissionSheetDAO;
import com.jlyw.hibernate.Overdue;
import com.jlyw.hibernate.OverdueDAO;
import com.jlyw.util.KeyValueWithOperator;

public class OverdueManager {
private OverdueDAO m_dao = new OverdueDAO();
	
	/**
	 * 根据Overdue Id 查找 Overdue对象
	 * @param id: Overdue Id
	 * @return Overdue对象
	 */
	public Overdue findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条Overdue记录
	 * @param appSpecies Overdue对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(Overdue appSpecies){
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
	 * 更新一条Overdue记录
	 * @param appSpecies Overdue对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(Overdue appSpecies){
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
	 * 根据Overdue Id,删除Overdue对象
	 * @param id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			Overdue u = m_dao.findById(id);
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
	 * @return 分页后的Overdue列表
	 */
	public List<Overdue> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("Overdue", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 得到所有Overdue记录数
	 * @param arr 条件键值对
	 * @return Overdue记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("Overdue", arr);
	}
	/**
	 * 得到所有Overdue记录数
	 * @param arr 查询条件列表
	 * @return Overdue记录数
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("Overdue", arr);
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List<Overdue> findByExample(Overdue instance) {
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
	* @return 分页后的数据列表- List<Overdue>
	*/
	public List<Overdue> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findPagedAllBySort("Overdue", currentPage, pageSize, orderby, asc, arr);
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
	* @return 分页后的数据列表- List<Overdue>
	*/
	public List<Overdue> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		try{
			return m_dao.findPagedAllBySort("Overdue", currentPage, pageSize, orderby, asc, arr);
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
	public List<Overdue> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("Overdue", arr);
		}
		catch(Exception e){
			return null;
		}
	}
	
	/**
	 * 超期办理，如果审批通过，则将委托单的承诺检出日期更新
	 * @param o
	 * @param cSheet
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public boolean overdueHandle(Overdue o, CommissionSheet cSheet) throws Exception{
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(o);
			if(o.getExecuteResult() && cSheet.getPromiseDate() != null && o.getDelayDays() != null){
				CommissionSheetDAO cDao = new CommissionSheetDAO();
				java.sql.Date d = cSheet.getPromiseDate();
				d.setDate(d.getDate() + o.getDelayDays());
				cSheet.setPromiseDate(d);
				cDao.update(cSheet);
			}
			tran.commit();
			return true;
		} catch (Exception e) {
			tran.rollback();
			throw new Exception(String.format("更新数据库失败，消息：%s", e.getMessage()==null?"无":e.getMessage()));
		} finally {
			m_dao.closeSession();
		}
	}
}
