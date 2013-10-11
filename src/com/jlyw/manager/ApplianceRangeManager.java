package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.ApplianceRange;
import com.jlyw.hibernate.ApplianceRangeDAO;
import com.jlyw.util.KeyValueWithOperator;

public class ApplianceRangeManager {
	private ApplianceRangeDAO m_dao = new ApplianceRangeDAO();
	
	/**
	 * ����ApplianceRange Id ���� ApplianceRange����
	 * @param id: ApplianceRange Id
	 * @return ApplianceRange����
	 */
	public ApplianceRange findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��ApplianceRange��¼
	 * @param appSpecies ApplianceRange����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(ApplianceRange appSpecies){
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
	 * ����һ��ApplianceRange��¼
	 * @param appSpecies ApplianceRange����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(ApplianceRange appSpecies){
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
	 * ����ApplianceRange Id,ɾ��ApplianceRange����
	 * @param id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			ApplianceRange u = m_dao.findById(id);
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
	 * ����ɾ��
	 * @param ranges�б�
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteByBatch(List<ApplianceRange> ranges){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			for(ApplianceRange range : ranges){
				m_dao.delete(range);
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
	 * @return ��ҳ���ApplianceRange�б�
	 */
	public List<ApplianceRange> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("ApplianceRange", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * �õ�����ApplianceRange��¼��
	 * @param arr ������ֵ��
	 * @return ApplianceRange��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("ApplianceRange", arr);		
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List<ApplianceRange> findByExample(ApplianceRange instance) {
		return m_dao.findByExample(instance);
	}
	
	/**
	 * 
	 * @param arr
	 * @return
	 */
	public List<ApplianceRange> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("ApplianceRange", arr);
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
