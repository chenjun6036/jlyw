package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.SubContractor;
import com.jlyw.hibernate.SubContractorDAO;
import com.jlyw.util.KeyValueWithOperator;

/**
 * ת����Manager
 * @author Administrator
 *
 */
public class SubContractorManager {
private SubContractorDAO m_dao = new SubContractorDAO();
	
	/**
	 * ����SubContractor Id ���� SubContractor����
	 * @param id: SubContractor Id
	 * @return SubContractor����
	 */
	public SubContractor findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��SubContractor��¼
	 * @param appSpecies SubContractor����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(SubContractor appSpecies){
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
	 * ����һ��SubContractor��¼
	 * @param appSpecies SubContractor����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(SubContractor appSpecies){
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
	 * ����SubContractor Id,ɾ��SubContractor����
	 * @param id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			SubContractor u = m_dao.findById(id);
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
	 * @return ��ҳ���SubContractor�б�
	 */
	public List<SubContractor> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("SubContractor", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * �õ�����SubContractor��¼��
	 * @param arr ������ֵ��
	 * @return SubContractor��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("SubContractor", arr);
	}
	/**
	 * �õ�����SubContractor��¼��
	 * @param arr ��ѯ�����б�
	 * @return SubContractor��¼��
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("SubContractor", arr);
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List<SubContractor> findByExample(SubContractor instance) {
		return m_dao.findByExample(instance);
	}
	
	
	/**
	* ͨ��HQL��������
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
	 * ����������
	 * @param arr
	 * @return
	 */
	public List<SubContractor> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("SubContractor", arr);
		}
		catch(Exception e){
			return null;
		}
	}
}
