package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.SubContract;
import com.jlyw.hibernate.SubContractDAO;
import com.jlyw.util.KeyValueWithOperator;

public class SubContractManager {
private SubContractDAO m_dao = new SubContractDAO();
	
	/**
	 * ����SubContract Id ���� SubContract����
	 * @param id: SubContract Id
	 * @return SubContract����
	 */
	public SubContract findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��SubContract��¼
	 * @param appSpecies SubContract����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(SubContract appSpecies){
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
	 * ����һ��SubContract��¼
	 * @param appSpecies SubContract����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(SubContract appSpecies){
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
	 * ����SubContract Id,ɾ��SubContract����
	 * @param id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			SubContract u = m_dao.findById(id);
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
	 * @return ��ҳ���SubContract�б�
	 */
	public List<SubContract> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("SubContract", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * �õ�����SubContract��¼��
	 * @param arr ������ֵ��
	 * @return SubContract��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("SubContract", arr);
	}
	/**
	 * �õ�����SubContract��¼��
	 * @param arr ��ѯ�����б�
	 * @return SubContract��¼��
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("SubContract", arr);
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List<SubContract> findByExample(SubContract instance) {
		return m_dao.findByExample(instance);
	}
	
	/**
	 * ��������ѯ
	 * @param arr
	 * @return
	 */
	public List<SubContract> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("SubContract", arr);
		}
		catch(Exception e){
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
	
	/**
	* ��ҳ��ʾ����
	*@param queryString:��ѯ��䣨HQL��
	* @param arr ��ѯ�����?��Ӧ��ֵ
	* @return ��ҳ��������б�- List
	*/
	public List findPagedAllByHQL(String queryString, int currentPage, int pageSize, List<Object> arr){
		try{
			return m_dao.findPageAllByHQL(queryString, currentPage, pageSize, arr);
		}catch(Exception e){
			return null;
		}
	}
}
