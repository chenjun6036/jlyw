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
	 * ����User Id ���� User����
	 * @param id User Id
	 * @return LocaleApplianceItem����
	 */
	public LocaleApplianceItem findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��LocaleApplianceItem��¼
	 * @param LocaleApplianceItem LocaleApplianceItem����
	 * @return ����ɹ�������true�����򷵻�false
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
	 * ����һ��LocaleApplianceItem��¼
	 * @param LocaleApplianceItem LocaleApplianceItem����
	 * @return ���³ɹ�������true�����򷵻�false
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
	 * ����LocaleApplianceItem Id,ɾ��LocaleApplianceItem����
	 * @param id LocaleApplianceItem id
	 * @return ɾ���ɹ�������true�����򷵻�false
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
	 * ��ҳ����
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ�ļ�¼��
	 * @return ��ҳ���LocaleApplianceItem�б�
	 */
	public List<LocaleApplianceItem> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator... arr) {
		try {
			return m_dao.findPagedAll("LocaleApplianceItem", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * �õ�����LocaleApplianceItem��¼��
	 * @return LocaleApplianceItem��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("LocaleApplianceItem",arr);		
	}
	/**
	 * �õ�����LocaleApplianceItem��¼��
	 * @param arr ��ѯ�����б�
	 * @return LocaleApplianceItem��¼��
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("LocaleApplianceItem", arr);
	}
	/**
	* ��ҳ��ʾ����
	* @param currentPage
	* ��ǰҳ��, �� 1 ��ʼ
	* @param pageSize
	* ÿҳ��ʾ������
	* @param orderby�������ĸ��ֶ�����
	* @param asc��true ���� false ����
	* @param arr:Ϊ��ѯ������(��-ֵ)���б�
	* @return ��ҳ��������б�- List<LocaleApplianceItem>
	*/
	public List<LocaleApplianceItem> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		return m_dao.findPagedAllBySort("LocaleApplianceItem", currentPage, pageSize, orderby, asc, arr);
	}
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
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
	* ��ҳ��ʾ����
	*@param queryString:��ѯ��䣨HQL��
	* @param currentPage
	* ��ǰҳ��, �� 1 ��ʼ
	* @param pageSize
	* ÿҳ��ʾ������
	* @param arr ��ѯ�����?��Ӧ��ֵ
	* @return ��ҳ��������б�- List
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
	*@param queryString:��ѯ��䣨HQL��
	* @param arr ��ѯ�����?��Ӧ��ֵ
	* @return ��ҳ��������б�- List
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
	 * �õ���¼����
	 * @param queryString ��ѯ��䣨HQL��
	 * @param arr ��ѯ�����?��Ӧ��ֵ
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
	 * �½�LocaleMission�������LocaleMission��������Ϣ
	 * 
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean saveByBatch(List<LocaleApplianceItem> localeApplianceItemList,LocaleMission localeMission) {
		if(localeApplianceItemList == null ||localeMission==null){
			return false;
		}
		LocaleMissionDAO dDAO = new LocaleMissionDAO();	//�ֳ�ҵ��DAO 
		
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
	 * �½�LocaleMission�������LocaleMission��������Ϣ,�½�������¼���½���������(�����ֳ����񲹵�)
	 * 
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean saveByBatchBD(List<LocaleApplianceItem> localeApplianceItemList,LocaleMission localeMission,DrivingVehicle drivingvehicle) {
		if(localeApplianceItemList == null ||localeMission==null){
			return false;
		}
		LocaleMissionDAO dDAO = new LocaleMissionDAO();	//�ֳ�ҵ��DAO 
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
	 * ����LocaleMission�������LocaleMission��������Ϣ
	 * 
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean updateByBatch(List<LocaleApplianceItem> localeApplianceItemList,LocaleMission localeMission) {
		if(localeApplianceItemList == null ||localeMission==null){
			return false;
		}
		LocaleMissionDAO dDAO = new LocaleMissionDAO();	//�ֳ�ҵ��DAO 
		
		Transaction tran = m_dao.getSession().beginTransaction();		
		try {			
			dDAO.update(localeMission);		
			//ɾ��
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
