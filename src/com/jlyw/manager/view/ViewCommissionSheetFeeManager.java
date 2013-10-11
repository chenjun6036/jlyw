package com.jlyw.manager.view;

import java.util.ArrayList;
import java.util.List;

import com.jlyw.hibernate.Customer;
import com.jlyw.hibernate.CustomerContactor;
import com.jlyw.hibernate.view.ViewCommissionSheetFee;
import com.jlyw.hibernate.view.ViewCommissionSheetFeeDAO;
import com.jlyw.util.KeyValueWithOperator;

public class ViewCommissionSheetFeeManager {
private ViewCommissionSheetFeeDAO m_dao = new ViewCommissionSheetFeeDAO();
	
	/**
	 * ��ҳ����
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ�ļ�¼��
	 * @param arr ������ֵ��
	 * @return ��ҳ���ViewCommissionSheetFee�б�
	 */
	public List<ViewCommissionSheetFee> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("ViewCommissionSheetFee", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * �õ�����ViewCommissionSheetFee��¼��
	 * @param arr ������ֵ��
	 * @return ViewCommissionSheetFee��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("ViewCommissionSheetFee", arr);
	}
	/**
	 * �õ�����ViewCommissionSheetFee��¼��
	 * @param arr ��ѯ�����б�
	 * @return ViewCommissionSheetFee��¼��
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("ViewCommissionSheetFee", arr);
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List<ViewCommissionSheetFee> findByExample(ViewCommissionSheetFee instance) {
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
	* @return ��ҳ��������б�- List<ViewCommissionSheetFee>
	*/
	public List<ViewCommissionSheetFee> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findPagedAllBySort("ViewCommissionSheetFee", currentPage, pageSize, orderby, asc, arr);
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
	* @return ��ҳ��������б�- List<ViewCommissionSheetFee>
	*/
	public List<ViewCommissionSheetFee> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		try{
			return m_dao.findPagedAllBySort("ViewCommissionSheetFee", currentPage, pageSize, orderby, asc, arr);
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
	public List<ViewCommissionSheetFee> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("ViewCommissionSheetFee", arr);
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
	public List<ViewCommissionSheetFee> findByVarProperty(List<KeyValueWithOperator>arr){
		try{
			return m_dao.findByVarProperty("ViewCommissionSheetFee", arr);
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
	public List<ViewCommissionSheetFee> findByVarPropertyBySort(String orderby,boolean asc,List<KeyValueWithOperator>arr){
		try{
			return m_dao.findByPropertyBySort("ViewCommissionSheetFee",orderby, asc,arr);
		}
		catch(Exception e){
			return null;
		}
	}
	
}
