package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.Certificate;
import com.jlyw.hibernate.CertificateDAO;
import com.jlyw.util.KeyValueWithOperator;

public class CertificateManager {
private CertificateDAO m_dao = new CertificateDAO();
	
	/**
	 * ����Certificate Id ���� Certificate����
	 * @param id: Certificate Id
	 * @return Certificate����
	 */
	public Certificate findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��Certificate��¼
	 * @param appSpecies Certificate����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(Certificate appSpecies){
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
	 * ����һ��Certificate��¼
	 * @param appSpecies Certificate����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(Certificate appSpecies){
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
	 * ����Certificate Id,ɾ��Certificate����
	 * @param id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			Certificate u = m_dao.findById(id);
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
	 * @return ��ҳ���Certificate�б�
	 */
	public List<Certificate> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("Certificate", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * �õ�����Certificate��¼��
	 * @param arr ������ֵ��
	 * @return Certificate��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("Certificate", arr);
	}
	/**
	 * �õ�����Certificate��¼��
	 * @param arr ��ѯ�����б�
	 * @return Certificate��¼��
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("Certificate", arr);
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List<Certificate> findByExample(Certificate instance) {
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
	* @return ��ҳ��������б�- List<Certificate>
	*/
	public List<Certificate> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findPagedAllBySort("Certificate", currentPage, pageSize, orderby, asc, arr);
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
	* @return ��ҳ��������б�- List<Certificate>
	*/
	public List<Certificate> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		try{
			return m_dao.findPagedAllBySort("Certificate", currentPage, pageSize, orderby, asc, arr);
		}catch(Exception e){
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
		return m_dao.findPageAllByHQL(queryString, currentPage, pageSize, arr);
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
	public List findPageAllByHQL(String queryString, int currentPage, int pageSize, List<Object> arr){
		try{
			return m_dao.findPageAllByHQL(queryString, currentPage, pageSize, arr);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}/**
	 * �õ���¼����
	 * @param queryString ��ѯ��䣨HQL��
	 * @param arr ��ѯ�����?��Ӧ��ֵ
	 * @return
	 */
	public int getTotalCountByHQL(String queryString,Object...arr) throws Exception{
		return m_dao.getTotalCountByHQL(queryString, arr);
	}
	/**
	 * �õ���¼����
	 * @param queryString ��ѯ��䣨HQL��
	 * @param arr ��ѯ�����?��Ӧ��ֵ
	 * @return
	 */
	public int getTotalCountByHQL(String queryString,List<Object> arr) throws Exception{
		return m_dao.getTotalCountByHQL(queryString, arr);
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
	 * �����������Բ��ң���ָ�������ֶ�
	 * @param TableName:���ĸ������ѯ
	 * @param orderby�������ĸ��ֶ�����
	 * @param asc��true ���� false ����
	 * @param arr ��������ֵ��
	 */
	public List<Certificate> findByPropertyBySort(String orderby, boolean asc, KeyValueWithOperator...arr){
		try{
			return m_dao.findByPropertyBySort("Certificate", orderby, asc, arr);
		}
		catch(Exception e){
			return null;
		}
	}
	
	/*
	public static void main(String [] args){
		CertificateManager mgr = new CertificateManager();
		String queryString = "from Certificate as model where model.id in (?,?)";
		List<Certificate> retList = mgr.findByHQL(queryString,new Integer(1), new Integer(2));
		System.out.println(retList.size());
	}*/
}
