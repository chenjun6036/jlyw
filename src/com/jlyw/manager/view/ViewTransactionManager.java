package com.jlyw.manager.view;

import java.util.ArrayList;
import java.util.List;

import com.jlyw.hibernate.Customer;
import com.jlyw.hibernate.CustomerContactor;
import com.jlyw.hibernate.view.ViewTransaction;
import com.jlyw.hibernate.view.ViewTransactionDAO;
import com.jlyw.util.KeyValueWithOperator;

public class ViewTransactionManager {
private ViewTransactionDAO m_dao = new ViewTransactionDAO();
	
	/**
	 * ��ҳ����
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ�ļ�¼��
	 * @param arr ������ֵ��
	 * @return ��ҳ���ViewTransaction�б�
	 */
	public List<ViewTransaction> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("ViewTransaction", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * �õ�����ViewTransaction��¼��
	 * @param arr ������ֵ��
	 * @return ViewTransaction��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("ViewTransaction", arr);
	}
	/**
	 * �õ�����ViewTransaction��¼��
	 * @param arr ��ѯ�����б�
	 * @return ViewTransaction��¼��
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("ViewTransaction", arr);
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List<ViewTransaction> findByExample(ViewTransaction instance) {
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
	* @return ��ҳ��������б�- List<ViewTransaction>
	*/
	public List<ViewTransaction> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findPagedAllBySort("ViewTransaction", currentPage, pageSize, orderby, asc, arr);
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
	* @return ��ҳ��������б�- List<ViewTransaction>
	*/
	public List<ViewTransaction> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		try{
			return m_dao.findPagedAllBySort("ViewTransaction", currentPage, pageSize, orderby, asc, arr);
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
		
			return m_dao.findByHQL(queryString, arr);

	}
	
	/**
	* ��ҳ��ʾ����
	*@param queryString:��ѯ��䣨HQL��
	* @param arr ��ѯ�����?��Ӧ��ֵ
	* @return ��ҳ��������б�- List
	*/
	public List findByHQL(String queryString, List<Object> arr){
		
			return m_dao.findByHQL(queryString, arr);

	}
	
	/**
	 * ��������ѯ
	 * @param arr
	 * @return
	 */
	public List<ViewTransaction> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("ViewTransaction", arr);
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
	public List<ViewTransaction> findByVarProperty(List<KeyValueWithOperator>arr){
		try{
			return m_dao.findByVarProperty("ViewTransaction", arr);
		}
		catch(Exception e){
			return null;
		}
	}
	/**
	 * ��������ѯ
	 * @param orderby�������ĸ��ֶ�����
	 * @param asc��true ���� false ����
	 * @param arr
	 * @return
	 */
	public List<ViewTransaction> findByVarPropertyBySort(String orderby,boolean asc,List<KeyValueWithOperator>arr){
		try{
			return m_dao.findByPropertyBySort("ViewTransaction",orderby, asc,arr);
		}
		catch(Exception e){
			return null;
		}
	}
	
}
