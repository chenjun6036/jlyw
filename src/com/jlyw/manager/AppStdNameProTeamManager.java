package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.AppStdNameProTeam;
import com.jlyw.hibernate.AppStdNameProTeamDAO;
import com.jlyw.util.KeyValueWithOperator;

public class AppStdNameProTeamManager {
	private AppStdNameProTeamDAO m_dao = new AppStdNameProTeamDAO();
	
	/**
	 * 根据AppStdNameProTeam Id 查找 AppStdNameProTeam对象
	 * @param id: AppStdNameProTeam Id
	 * @return AppStdNameProTeam对象
	 */
	public AppStdNameProTeam findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条AppStdNameProTeam记录
	 * @param appSpecies AppStdNameProTeam对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(AppStdNameProTeam appSpecies){
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
	 * 更新一条AppStdNameProTeam记录
	 * @param appSpecies AppStdNameProTeam对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(AppStdNameProTeam appSpecies){
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
	 * 根据AppStdNameProTeam Id,删除AppStdNameProTeam对象
	 * @param id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			AppStdNameProTeam u = m_dao.findById(id);
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
	 * @return 分页后的AppStdNameProTeam列表
	 */
	public List<AppStdNameProTeam> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("AppStdNameProTeam", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 得到所有AppStdNameProTeam记录数
	 * @param arr 条件键值对
	 * @return AppStdNameProTeam记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("AppStdNameProTeam", arr);		
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List<AppStdNameProTeam> findByExample(AppStdNameProTeam instance) {
		return m_dao.findByExample(instance);
	}
	
	/**
	 * 
	 * @param arr
	 * @return
	 */
	public List<AppStdNameProTeam> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("AppStdNameProTeam", arr);
		}
		catch(Exception e){
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
}
