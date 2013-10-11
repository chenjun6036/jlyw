package com.jlyw.manager;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.ApplianceManufacturer;
import com.jlyw.hibernate.ApplianceManufacturerDAO;
import com.jlyw.hibernate.ApplianceSpecies;
import com.jlyw.util.KeyValueWithOperator;

public class ApplianceManufacturerManager {
private ApplianceManufacturerDAO m_dao = new ApplianceManufacturerDAO();
	
	/**
	 * 根据ApplianceManufacturer Id 查找 ApplianceManufacturer对象
	 * @param id: ApplianceManufacturer Id
	 * @return ApplianceManufacturer对象
	 */
	public ApplianceManufacturer findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条ApplianceManufacturer记录
	 * @param appSpecies ApplianceManufacturer对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(ApplianceManufacturer appSpecies){
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
	 * 更新一条ApplianceManufacturer记录
	 * @param appSpecies ApplianceManufacturer对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(ApplianceManufacturer appSpecies){
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
	 * 根据ApplianceManufacturer Id,删除ApplianceManufacturer对象
	 * @param id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			ApplianceManufacturer u = m_dao.findById(id);
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
	 * @return 分页后的ApplianceManufacturer列表
	 */
	public List<ApplianceManufacturer> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("ApplianceManufacturer", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 得到所有ApplianceManufacturer记录数
	 * @param arr 条件键值对
	 * @return ApplianceManufacturer记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("ApplianceManufacturer", arr);		
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List<ApplianceManufacturer> findByExample(ApplianceManufacturer instance) {
		return m_dao.findByExample(instance);
	}
	
	/**
	 * 
	 * @param arr
	 * @return
	 */
	public List<ApplianceManufacturer> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("ApplianceManufacturer", arr);
		}
		catch(Exception e){
			return null;
		}
	}
	
	/**
	 * 批量注销生产厂商
	 * @param appManufcturers
	 * @return
	 */
	public boolean logOutByBatch(List<ApplianceManufacturer> appManufcturers){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			for(ApplianceManufacturer appManufacturer : appManufcturers){
				appManufacturer.setStatus(1);
				m_dao.update(appManufacturer);
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
	
	public List<String> formatExcel(Object obj){
		List<String> result = new ArrayList<String>();
		result.add(((ApplianceManufacturer)obj).getId().toString());
		String parentStr = "";
		ApplianceSpecies parentSpec = ((ApplianceManufacturer)obj).getApplianceStandardName().getApplianceSpecies();
		while(parentSpec!=null)
		{
			parentStr = parentSpec.getName().concat("-" + parentStr);
			parentSpec = parentSpec.getParentSpecies();
		}
		parentStr = parentStr + ((ApplianceManufacturer)obj).getApplianceStandardName().getName();
		result.add(parentStr.length()==0?"":parentStr);
		result.add(((ApplianceManufacturer)obj).getManufacturer().toString());
		result.add(((ApplianceManufacturer)obj).getBrief().toString());
		result.add(((ApplianceManufacturer)obj).getStatus().toString());
		return result;
	}
	
	public List<String> formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("序号");
		result.add("标准名称");
		result.add("生产厂商");
		result.add("拼音简码");
		result.add("状态");
		
		return result;
	}
	
}
