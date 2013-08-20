package com.jlyw.manager.view;

import java.util.List;

import com.jlyw.hibernate.view.ViewApplianceSpecialStandardNamePopularName;
import com.jlyw.hibernate.view.ViewApplianceSpecialStandardNamePopularNameDAO;
import com.jlyw.util.KeyValueWithOperator;

public class ViewAppSpeStandardPopularNameManager {
	private ViewApplianceSpecialStandardNamePopularNameDAO m_dao = new ViewApplianceSpecialStandardNamePopularNameDAO();

	/**
	 * ��ҳ����
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ�ļ�¼��
	 * @param arr ������ֵ��
	 * @return ��ҳ���ViewApplianceSpecialStandardNamePopularName�б�
	 */
	public List<ViewApplianceSpecialStandardNamePopularName> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("ViewApplianceSpecialStandardNamePopularName", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * �õ�����ViewApplianceSpecialStandardNamePopularName��¼��
	 * @param arr ������ֵ��
	 * @return ViewApplianceSpecialStandardNamePopularName��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("ViewApplianceSpecialStandardNamePopularName", arr);
	}
	/**
	 * �õ�����ViewApplianceSpecialStandardNamePopularName��¼��
	 * @param arr ��ѯ�����б�
	 * @return ViewApplianceSpecialStandardNamePopularName��¼��
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("ViewApplianceSpecialStandardNamePopularName", arr);
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List<ViewApplianceSpecialStandardNamePopularName> findByExample(ViewApplianceSpecialStandardNamePopularName instance) {
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
	* @return ��ҳ��������б�- List<ViewApplianceSpecialStandardNamePopularName>
	*/
	public List<ViewApplianceSpecialStandardNamePopularName> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findPagedAllBySort("ViewApplianceSpecialStandardNamePopularName", currentPage, pageSize, orderby, asc, arr);
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
	* @return ��ҳ��������б�- List<ViewApplianceSpecialStandardNamePopularName>
	*/
	public List<ViewApplianceSpecialStandardNamePopularName> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		try{
			return m_dao.findPagedAllBySort("ViewApplianceSpecialStandardNamePopularName", currentPage, pageSize, orderby, asc, arr);
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
	public List<ViewApplianceSpecialStandardNamePopularName> findByVarProperty(List<KeyValueWithOperator>arr){
		try{
			return m_dao.findByVarProperty("ViewApplianceSpecialStandardNamePopularName", arr);
		}
		catch(Exception e){
			return null;
		}
	}
	
	/**
	 * ��HQL��ҳ��ѯ
	 * @param queryString
	 * @param currentPage
	 * @param pageSize
	 * @param arr
	 * @return
	 */
	public List findPageAllByHQL(String queryString, int currentPage, int pageSize, Object...arr){
		try{
			return m_dao.findPageAllByHQL(queryString, currentPage, pageSize, arr);
		}catch(Exception e){
			return null;
		}
	}
}
