package com.jlyw.manager.view;

import java.util.ArrayList;
import java.util.List;

import com.jlyw.hibernate.Customer;
import com.jlyw.hibernate.CustomerContactor;
import com.jlyw.hibernate.view.ViewQjtz;
import com.jlyw.hibernate.view.ViewQjtzDAO;
import com.jlyw.util.KeyValueWithOperator;

public class ViewQjtzManager {
private ViewQjtzDAO m_dao = new ViewQjtzDAO();
	
	/**
	 * ��ҳ����
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ�ļ�¼��
	 * @param arr ������ֵ��
	 * @return ��ҳ���ViewQjtz�б�
	 */
	public List<ViewQjtz> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("ViewQjtz", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * �õ�����ViewQjtz��¼��
	 * @param arr ������ֵ��
	 * @return ViewQjtz��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("ViewQjtz", arr);
	}
	/**
	 * �õ�����ViewQjtz��¼��
	 * @param arr ��ѯ�����б�
	 * @return ViewQjtz��¼��
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("ViewQjtz", arr);
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List<ViewQjtz> findByExample(ViewQjtz instance) {
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
	* @return ��ҳ��������б�- List<ViewQjtz>
	*/
	public List<ViewQjtz> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findPagedAllBySort("ViewQjtz", currentPage, pageSize, orderby, asc, arr);
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
	* @return ��ҳ��������б�- List<ViewQjtz>
	*/
	public List<ViewQjtz> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		try{
			return m_dao.findPagedAllBySort("ViewQjtz", currentPage, pageSize, orderby, asc, arr);
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
	public List<ViewQjtz> findByVarProperty(List<KeyValueWithOperator>arr){
		try{
			return m_dao.findByVarProperty("ViewQjtz", arr);
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
	public List<ViewQjtz> findByVarPropertyBySort(String orderby,boolean asc,List<KeyValueWithOperator>arr){
		try{
			return m_dao.findByPropertyBySort("ViewQjtz",orderby, asc,arr);
		}
		catch(Exception e){
			return null;
		}
	}
	
	public List<String> formatExcel(Object obj){
		List<String> result = new ArrayList<String>();
		result.add(((ViewQjtz)obj).getId().toString());
		result.add(((ViewQjtz)obj).getCustomerName().toString());
		result.add(((ViewQjtz)obj).getCustomerZipCode().toString());
		result.add(((ViewQjtz)obj).getCustomerAddress().toString());
		result.add(((ViewQjtz)obj).getApplianceName().toString());
		result.add(((ViewQjtz)obj).getMandatoryCode().toString());
		result.add(((ViewQjtz)obj).getApplianceModel().toString());
		result.add(((ViewQjtz)obj).getTestCycle().toString());
		result.add(((ViewQjtz)obj).getRemark().toString());
		result.add(((ViewQjtz)obj).getMandatoryDate().toString());
		
		return result;
	}
	
	public List<String> formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("���");
		result.add("ί�е�λ����");
		result.add("ί�е�λ�ʱ�");
		result.add("ί�е�λ��ַ");
		result.add("��������");
		result.add("ǿ��Ψһ�Ա��");
		result.add("�����ͺŹ��");
		result.add("�춨����");
		result.add("��ע");
		result.add("�춨ʱ��");
		
		return result;
	}
}
