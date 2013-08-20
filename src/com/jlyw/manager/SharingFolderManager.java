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
	 * ����SharingFolder Id ���� SharingFolder����
	 * @param id: SharingFolder Id
	 * @return SharingFolder����
	 */
	public SharingFolder findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��SharingFolder��¼
	 * @param appSpecies SharingFolder����
	 * @return ����ɹ�������true�����򷵻�false
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
	 * ����һ��SharingFolder��¼
	 * @param appSpecies SharingFolder����
	 * @return ���³ɹ�������true�����򷵻�false
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
	 * ����SharingFolder Id,ɾ��SharingFolder����
	 * @param id
	 * @return ɾ���ɹ�������true�����򷵻�false
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
	 * ��ҳ����
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ�ļ�¼��
	 * @param arr ������ֵ��
	 * @return ��ҳ���SharingFolder�б�
	 */
	public List<SharingFolder> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("SharingFolder", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * �õ�����SharingFolder��¼��
	 * @param arr ������ֵ��
	 * @return SharingFolder��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("SharingFolder", arr);		
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
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
	* ��ҳ��ʾ����
	*@param queryString:��ѯ��䣨HQL��
	* @param arr ��ѯ�����?��Ӧ��ֵ
	* @return ��ҳ��������б�- List
	*/
	public List findByHQL(String queryString, Object...arr){
		try{
			return m_dao.findByHQL(queryString, arr);
		}catch(Exception e){
			return null;
		}
	}
}
