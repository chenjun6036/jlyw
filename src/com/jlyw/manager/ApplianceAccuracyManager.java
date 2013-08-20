package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.ApplianceAccuracy;
import com.jlyw.hibernate.ApplianceAccuracyDAO;
import com.jlyw.util.KeyValueWithOperator;

public class ApplianceAccuracyManager {
	private ApplianceAccuracyDAO m_dao = new ApplianceAccuracyDAO();
	
	/**
	 * ����ApplianceAccuracy Id ���� ApplianceAccuracy����
	 * @param id: ApplianceAccuracy Id
	 * @return ApplianceAccuracy����
	 */
	public ApplianceAccuracy findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��ApplianceAccuracy��¼
	 * @param appSpecies ApplianceAccuracy����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(ApplianceAccuracy appSpecies){
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
	 * ����һ��ApplianceAccuracy��¼
	 * @param appSpecies ApplianceAccuracy����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(ApplianceAccuracy appSpecies){
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
	 * ����ApplianceAccuracy Id,ɾ��ApplianceAccuracy����
	 * @param id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			ApplianceAccuracy u = m_dao.findById(id);
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
	 * @return ��ҳ���ApplianceAccuracy�б�
	 */
	public List<ApplianceAccuracy> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("ApplianceAccuracy", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * �õ�����ApplianceAccuracy��¼��
	 * @param arr ������ֵ��
	 * @return ApplianceAccuracy��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("ApplianceAccuracy", arr);		
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List<ApplianceAccuracy> findByExample(ApplianceAccuracy instance) {
		return m_dao.findByExample(instance);
	}
	
	/**
	 * 
	 * @param arr
	 * @return
	 */
	public List<ApplianceAccuracy> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("ApplianceAccuracy", arr);
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
	* ��ʾ����
	*@param queryString:��ѯ��䣨HQL��
	* @param arr ��ѯ�����?��Ӧ��ֵ
	* @return ��ҳ��������б�- List
	*/
	public List findByHQL(String queryString, Object...arr){
		try{
			return m_dao.findByHQL(queryString, arr);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
