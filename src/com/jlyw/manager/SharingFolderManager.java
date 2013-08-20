package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.ApplianceStandardName;
import com.jlyw.hibernate.ApplianceStandardNameDAO;
import com.jlyw.hibernate.SharingFolder;
import com.jlyw.hibernate.SharingFolderDAO;
import com.jlyw.util.KeyValueWithOperator;

public class SharingFolderManager {
	private SharingFolderDAO m_dao = new SharingFolderDAO();
	
	/**
	 * 根据SharingFolder Id 查找 SharingFolder对象
	 * @param id: SharingFolder Id
	 * @return SharingFolder对象
	 */
	public SharingFolder findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条SharingFolder记录
	 * @param appSpecies SharingFolder对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(SharingFolder appSpecies){
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
	 * 更新一条SharingFolder记录
	 * @param appSpecies SharingFolder对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(SharingFolder appSpecies){
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
	 * 根据SharingFolder Id,删除SharingFolder对象
	 * @param id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			SharingFolder u = m_dao.findById(id);
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
	 * @return 分页后的SharingFolder列表
	 */
	public List<SharingFolder> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("SharingFolder", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 得到所有SharingFolder记录数
	 * @param arr 条件键值对
	 * @return SharingFolder记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("SharingFolder", arr);		
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List<SharingFolder> findByExample(SharingFolder instance) {
		return m_dao.findByExample(instance);
	}
	
	/**
	 * 
	 * @param arr
	 * @return
	 */
	public List<SharingFolder> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("SharingFolder", arr);
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
