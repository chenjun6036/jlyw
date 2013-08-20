package com.jlyw.manager.quotation;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.Quotation;
import com.jlyw.hibernate.QuotationDAO;
import com.jlyw.util.KeyValueWithOperator;

public class QuotationManager {
private QuotationDAO m_dao = new QuotationDAO();
	
	/**
	 * ����Quotation Id ���� Quotation����
	 * @param id: Quotation Id
	 * @return Quotation����
	 */
	public Quotation findById(String id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��Quotation��¼
	 * @param appSpecies Quotation����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(Quotation appSpecies){
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
	 * ����һ��Quotation��¼
	 * @param appSpecies Quotation����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(Quotation appSpecies){
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
	 * ����Quotation Id,ɾ��Quotation����
	 * @param id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(String id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			Quotation u = m_dao.findById(id);
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
	public List<Object> findBySQL(String queryString, Object...arr){
		return m_dao.findBySQL(queryString, arr);
	}
	/**
	 * ��ҳ����
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ�ļ�¼��
	 * @param arr ������ֵ��
	 * @return ��ҳ���Quotation�б�
	 */
	public List<Quotation> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("Quotation", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * �õ�����Quotation��¼��
	 * @param arr ������ֵ��
	 * @return Quotation��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("Quotation", arr);
	}
	/**
	 * �õ�����Quotation��¼��
	 * @param arr ��ѯ�����б�
	 * @return Quotation��¼��
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("Quotation", arr);
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List<Quotation> findByExample(Quotation instance) {
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
	* @return ��ҳ��������б�- List<Quotation>
	*/
	public List<Quotation> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findPagedAllBySort("Quotation", currentPage, pageSize, orderby, asc, arr);
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
	* @return ��ҳ��������б�- List<Quotation>
	*/
	public List<Quotation> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		try{
			return m_dao.findPagedAllBySort("Quotation", currentPage, pageSize, orderby, asc, arr);
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	
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
	 * 	* @param orderby�������ĸ��ֶ�����
	* @param asc��true ���� false ����
	 * @param arr
	 * @return
	 */
	public List<Quotation> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("Quotation", arr);
		}
		catch(Exception e){
			return null;
		}
	}
	/**
	 * ��������ѯ
	 * @param arr
	 * @return
	 */
	public List<Quotation> findByVarProperty(String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("Quotation", arr);
		}
		catch(Exception e){
			return null;
		}
	}
	
	/**
	 * ��������ѯ
	 * @param arr
	 * @return
	 */
	public List<Quotation> findByVarProperty(List<KeyValueWithOperator> arr){
		try{
			return m_dao.findByVarProperty("Quotation", arr);
		}
		catch(Exception e){
			return null;
		}
	}
}
