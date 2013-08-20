package com.jlyw.manager.vehicle;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.CommissionSheet;
import com.jlyw.hibernate.Customer;
import com.jlyw.hibernate.CustomerContactor;
import com.jlyw.hibernate.DrivingVehicle;
import com.jlyw.hibernate.DrivingVehicleDAO;
import com.jlyw.hibernate.LocaleApplianceItem;
import com.jlyw.hibernate.LocaleApplianceItemDAO;
import com.jlyw.hibernate.LocaleMission;
import com.jlyw.hibernate.LocaleMissionDAO;
import com.jlyw.hibernate.Vehicle;
import com.jlyw.hibernate.VehicleDAO;
import com.jlyw.hibernate.VehicleMission;
import com.jlyw.hibernate.VehicleMissionDAO;
import com.jlyw.util.KeyValue;
import com.jlyw.util.KeyValueWithOperator;

public class LocaleApplianceItemManager {
private LocaleApplianceItemDAO m_dao = new LocaleApplianceItemDAO();
	
	/**
	 * 根据User Id 查找 User对象
	 * @param id User Id
	 * @return LocaleApplianceItem对象
	 */
	public LocaleApplianceItem findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条LocaleApplianceItem记录
	 * @param LocaleApplianceItem LocaleApplianceItem对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(LocaleApplianceItem LocaleApplianceItem){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			m_dao.save(LocaleApplianceItem);
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
	 * 更新一条LocaleApplianceItem记录
	 * @param LocaleApplianceItem LocaleApplianceItem对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(LocaleApplianceItem LocaleApplianceItem){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(LocaleApplianceItem);
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
	 * 根据LocaleApplianceItem Id,删除LocaleApplianceItem对象
	 * @param id LocaleApplianceItem id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			LocaleApplianceItem u = m_dao.findById(id);
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
	 * @return 分页后的LocaleApplianceItem列表
	 */
	public List<LocaleApplianceItem> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator... arr) {
		try {
			return m_dao.findPagedAll("LocaleApplianceItem", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 得到所有LocaleApplianceItem记录数
	 * @return LocaleApplianceItem记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("LocaleApplianceItem",arr);		
	}
	/**
	 * 得到所有LocaleApplianceItem记录数
	 * @param arr 查询条件列表
	 * @return LocaleApplianceItem记录数
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("LocaleApplianceItem", arr);
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
	* @return 分页后的数据列表- List<LocaleApplianceItem>
	*/
	public List<LocaleApplianceItem> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		return m_dao.findPagedAllBySort("LocaleApplianceItem", currentPage, pageSize, orderby, asc, arr);
	}
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List findByExample(LocaleApplianceItem instance) {
		return m_dao.findByExample(instance);
		
	}
	
	/**
	 * 
	 * @param arr
	 * @return
	 */
	public List<LocaleApplianceItem> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("LocaleApplianceItem", arr);
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
	*@param queryString:查询语句（HQL）
	* @param arr 查询语句中?对应的值
	* @return 分页后的数据列表- List
	*/
	public List findByHQL(String queryString,Object...arr){
		try{
			return m_dao.findByHQL(queryString, arr);
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
	public int getTotalCountByHQL(String queryString,Object...arr) {
		try{
			return m_dao.getTotalCountByHQL(queryString, arr);
		}catch(Exception ex){
			return 0;
		}
	}
	/**
	 * 新建LocaleMission，并添加LocaleMission的器具信息
	 * 
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean saveByBatch(List<LocaleApplianceItem> localeApplianceItemList,LocaleMission localeMission) {
		if(localeApplianceItemList == null ||localeMission==null){
			return false;
		}
		LocaleMissionDAO dDAO = new LocaleMissionDAO();	//现场业务DAO 
		
		Transaction tran = m_dao.getSession().beginTransaction();		
		try {			
			dDAO.save(localeMission);			
			for(int i = 0; i < localeApplianceItemList.size(); i++){
				LocaleApplianceItem localeApplianceItem = localeApplianceItemList.get(i);
				localeApplianceItem.setLocaleMission(localeMission);
				
				m_dao.save(localeApplianceItem);				
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
	 * 新建LocaleMission，并添加LocaleMission的器具信息,新建出车记录，新建出车任务(用于现场任务补登)
	 * 
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean saveByBatchBD(List<LocaleApplianceItem> localeApplianceItemList,LocaleMission localeMission,DrivingVehicle drivingvehicle) {
		if(localeApplianceItemList == null ||localeMission==null){
			return false;
		}
		LocaleMissionDAO dDAO = new LocaleMissionDAO();	//现场业务DAO 
		DrivingVehicleDAO driVeDAO=new DrivingVehicleDAO();
		VehicleMissionDAO veMIDAO=new VehicleMissionDAO();
		Transaction tran = m_dao.getSession().beginTransaction();		
		try {			
			dDAO.save(localeMission);			
			for(int i = 0; i < localeApplianceItemList.size(); i++){
				LocaleApplianceItem localeApplianceItem = localeApplianceItemList.get(i);
				localeApplianceItem.setLocaleMission(localeMission);
				
				m_dao.save(localeApplianceItem);				
			}
			driVeDAO.save(drivingvehicle);
			VehicleMission veMission=new VehicleMission();
			veMission.setDrivingVehicle(drivingvehicle);
			veMission.setLocaleMission(localeMission);
			veMIDAO.save(veMission);
			
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
	 * 更新LocaleMission，并添加LocaleMission的器具信息
	 * 
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean updateByBatch(List<LocaleApplianceItem> localeApplianceItemList,LocaleMission localeMission) {
		if(localeApplianceItemList == null ||localeMission==null){
			return false;
		}
		LocaleMissionDAO dDAO = new LocaleMissionDAO();	//现场业务DAO 
		
		Transaction tran = m_dao.getSession().beginTransaction();		
		try {			
			dDAO.update(localeMission);		
			//删除
			//m_dao.delete("LocaleApplianceItem", 
			//		new KeyValue("localeMission.id", localeMission.getId()));
			
			String Ids="";
			for(int i = 0; i < localeApplianceItemList.size(); i++){
				
				LocaleApplianceItem localeApplianceItem = localeApplianceItemList.get(i);
				localeApplianceItem.setLocaleMission(localeMission);
				if(localeApplianceItem.getId() != null){
					m_dao.update(localeApplianceItem);
				}else{					
					m_dao.save(localeApplianceItem);	
				}
				if(Ids.length()==0){
					Ids = Ids + localeApplianceItem.getId().toString();
				}else{
					Ids = Ids + "," + localeApplianceItem.getId().toString();
				}
			}
			if(Ids.length()>0){
				String StringHQL="delete LocaleApplianceItem as l where l.id not in ( " + Ids + " ) and l.localeMission = ?" ;
				
				m_dao.updateByHQL(StringHQL,localeMission);
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
