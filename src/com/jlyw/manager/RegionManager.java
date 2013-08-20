package com.jlyw.manager;
import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.Customer;
import com.jlyw.hibernate.Region;
import com.jlyw.hibernate.RegionDAO;
import com.jlyw.util.KeyValueWithOperator;

public class RegionManager {
	private RegionDAO m_dao = new RegionDAO();
	/**
	 * 根据Region Id 查找 Region对象
	 * @param id: Region Id
	 * @return Region对象
	 */
	public Region findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条Region记录
	 * @param appSpecies Region对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(Region appSpecies){
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
	 * 更新一条Region记录
	 * @param appSpecies Region对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(Region appSpecies){
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
	 * 根据Region Id,删除Region对象
	 * @param id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			Region u = m_dao.findById(id);
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
	 * 按条件查找
	 * @param arr
	 * @return
	 */
	public List<Region> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("Region", arr);
		}
		catch(Exception e){
			return null;
		}
	}
	
	/**
	 * 分页函数
	 * @param currentPage 当前页码
	 * @param pageSize 每页的记录数
	 * @return 分页后的Customer列表
	 */
	public List<Region> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator... arr) {
		try {
			return m_dao.findPagedAll("Region", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 得到所有ApplianceAccuracy记录数
	 * @param arr 条件键值对
	 * @return ApplianceAccuracy记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("Region", arr);		
	}
}
