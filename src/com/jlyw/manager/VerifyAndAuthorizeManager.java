package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.VerifyAndAuthorize;
import com.jlyw.hibernate.VerifyAndAuthorizeDAO;
import com.jlyw.util.KeyValueWithOperator;

public class VerifyAndAuthorizeManager {
private VerifyAndAuthorizeDAO m_dao = new VerifyAndAuthorizeDAO();
	
	/**
	 * ����VerifyAndAuthorize Id ���� VerifyAndAuthorize����
	 * @param id: VerifyAndAuthorize Id
	 * @return VerifyAndAuthorize����
	 */
	public VerifyAndAuthorize findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��VerifyAndAuthorize��¼
	 * @param appSpecies VerifyAndAuthorize����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(VerifyAndAuthorize appSpecies){
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
	 * ����һ��VerifyAndAuthorize��¼
	 * @param appSpecies VerifyAndAuthorize����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(VerifyAndAuthorize appSpecies) throws Exception{
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(appSpecies);
			tran.commit();
			return true;
		} catch (Exception e) {
			tran.rollback();
			throw e;
		} finally {
			m_dao.closeSession();
		}
	}
	
	/**
	 * ����VerifyAndAuthorize Id,ɾ��VerifyAndAuthorize����
	 * @param id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			VerifyAndAuthorize u = m_dao.findById(id);
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
	 * @return ��ҳ���VerifyAndAuthorize�б�
	 */
	public List<VerifyAndAuthorize> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("VerifyAndAuthorize", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * �õ�����VerifyAndAuthorize��¼��
	 * @param arr ������ֵ��
	 * @return VerifyAndAuthorize��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("VerifyAndAuthorize", arr);
	}
	/**
	 * �õ�����VerifyAndAuthorize��¼��
	 * @param arr ��ѯ�����б�
	 * @return VerifyAndAuthorize��¼��
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("VerifyAndAuthorize", arr);
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List<VerifyAndAuthorize> findByExample(VerifyAndAuthorize instance) {
		return m_dao.findByExample(instance);
	}
	
	/**
	* ��ҳ��ʾ����
	* @param currentPage
	* ��ǰҳ��, �� 1 ��ʼ
	* @param pageSize
	* ÿҳ��ʾ������
	* @param orderby�������ĸ��ֶ�����
	* @param asc��true ���� false ����
	* @param arr:Ϊ��ѯ������(��-ֵ)������
	* @return ��ҳ��������б�- List<VerifyAndAuthorize>
	*/
	public List<VerifyAndAuthorize> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findPagedAllBySort("VerifyAndAuthorize", currentPage, pageSize, orderby, asc, arr);
		}catch(Exception e){
			return null;
		}
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
	* @return ��ҳ��������б�- List<VerifyAndAuthorize>
	*/
	public List<VerifyAndAuthorize> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		try{
			return m_dao.findPagedAllBySort("VerifyAndAuthorize", currentPage, pageSize, orderby, asc, arr);
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
	public List findByHQL(String queryString, Object...arr){
		try{
			return m_dao.findByHQL(queryString, arr);
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * ��������ѯ
	 * @param arr
	 * @return
	 */
	public List<VerifyAndAuthorize> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("VerifyAndAuthorize", arr);
		}
		catch(Exception e){
			return null;
		}
	}
}
