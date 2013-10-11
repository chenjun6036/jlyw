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
	 * ����ApplianceManufacturer Id ���� ApplianceManufacturer����
	 * @param id: ApplianceManufacturer Id
	 * @return ApplianceManufacturer����
	 */
	public ApplianceManufacturer findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��ApplianceManufacturer��¼
	 * @param appSpecies ApplianceManufacturer����
	 * @return ����ɹ�������true�����򷵻�false
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
	 * ����һ��ApplianceManufacturer��¼
	 * @param appSpecies ApplianceManufacturer����
	 * @return ���³ɹ�������true�����򷵻�false
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
	 * ����ApplianceManufacturer Id,ɾ��ApplianceManufacturer����
	 * @param id
	 * @return ɾ���ɹ�������true�����򷵻�false
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
	 * ��ҳ����
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ�ļ�¼��
	 * @param arr ������ֵ��
	 * @return ��ҳ���ApplianceManufacturer�б�
	 */
	public List<ApplianceManufacturer> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("ApplianceManufacturer", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * �õ�����ApplianceManufacturer��¼��
	 * @param arr ������ֵ��
	 * @return ApplianceManufacturer��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("ApplianceManufacturer", arr);		
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
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
	 * ����ע����������
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
		result.add("���");
		result.add("��׼����");
		result.add("��������");
		result.add("ƴ������");
		result.add("״̬");
		
		return result;
	}
	
}
