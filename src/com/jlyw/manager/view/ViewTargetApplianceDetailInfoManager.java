package com.jlyw.manager.view;

import java.util.List;

import com.jlyw.hibernate.view.ViewTargetApplianceDetailInfo;
import com.jlyw.hibernate.view.ViewTargetApplianceDetailInfoDAO;
import com.jlyw.util.KeyValueWithOperator;

public class ViewTargetApplianceDetailInfoManager {
private ViewTargetApplianceDetailInfoDAO m_dao = new ViewTargetApplianceDetailInfoDAO();
	
	/**
	 * ��ҳ����
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ�ļ�¼��
	 * @param arr ������ֵ��
	 * @return ��ҳ���ViewTargetApplianceDetailInfo�б�
	 */
	public List<ViewTargetApplianceDetailInfo> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("ViewTargetApplianceDetailInfo", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * ��ҳ����
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ�ļ�¼��
	 * @param arr ������ֵ��
	 * @return ��ҳ���ViewTargetApplianceDetailInfo�б�
	 */
	public List<ViewTargetApplianceDetailInfo> findPagedAll(int currentPage, int pageSize, List<KeyValueWithOperator> arr) {
			return m_dao.findPagedAll("ViewTargetApplianceDetailInfo", currentPage,pageSize, arr);
	}
	
	
	/**
	 * �õ�����ViewTargetApplianceDetailInfo��¼��
	 * @param arr ������ֵ��
	 * @return ViewTargetApplianceDetailInfo��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("ViewTargetApplianceDetailInfo", arr);
	}
	/**
	 * �õ�����ViewTargetApplianceDetailInfo��¼��
	 * @param arr ��ѯ�����б�
	 * @return ViewTargetApplianceDetailInfo��¼��
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		
			return m_dao.getTotalCount("ViewTargetApplianceDetailInfo", arr);
		
		
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List<ViewTargetApplianceDetailInfo> findByExample(ViewTargetApplianceDetailInfo instance) {
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
	* @return ��ҳ��������б�- List<ViewTargetApplianceDetailInfo>
	*/
	public List<ViewTargetApplianceDetailInfo> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findPagedAllBySort("ViewTargetApplianceDetailInfo", currentPage, pageSize, orderby, asc, arr);
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
	* @return ��ҳ��������б�- List<ViewTargetApplianceDetailInfo>
	*/
	public List<ViewTargetApplianceDetailInfo> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		try{
			return m_dao.findPagedAllBySort("ViewTargetApplianceDetailInfo", currentPage, pageSize, orderby, asc, arr);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * ��������ѯ
	 * @param arr
	 * @return
	 */
	public List<ViewTargetApplianceDetailInfo> findByVarProperty(List<KeyValueWithOperator>arr){
		try{
			return m_dao.findByVarProperty("ViewTargetApplianceDetailInfo", arr);
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
	public List findPageAllBySQL(String queryString, int currentPage, int pageSize, List<Object>arr) throws Exception{
		try{
			return m_dao.findPageAllBySQL(queryString, currentPage, pageSize, arr);
		}catch(Exception e){
			throw e;
		}
	}
	/**
	 * �õ���¼����
	 * @param queryString ��ѯ��䣨SQL��
	 * @param arr ��ѯ�����?��Ӧ��ֵ
	 * @return
	 */
	public int getTotalCountBySQL(String queryString,List<Object>arr) throws Exception {
		try{
			return m_dao.getTotalCountBySQL(queryString, arr);
		}catch(Exception ex){
			throw ex;
		}
	}
	
	/**
	* ��ҳ��ʾ����
	*@param queryString:��ѯ��䣨HQL��
	* @param arr ��ѯ�����?��Ӧ��ֵ
	* @return ��ҳ��������б�- List
	*/
	public List findPageAllByHQL(String queryString, int currentPage, int pageSize, List<Object>arr) throws Exception{
		try{
			return m_dao.findPageAllByHQL(queryString, currentPage, pageSize, arr);
		}catch(Exception e){
			throw e;
		}
	}
	/**
	 * �õ���¼����
	 * @param queryString ��ѯ��䣨SQL��
	 * @param arr ��ѯ�����?��Ӧ��ֵ
	 * @return
	 */
	public int getTotalCountByHQL(String queryString,List<Object>arr) throws Exception {
		try{
			return m_dao.getTotalCountByHQL(queryString, arr);
		}catch(Exception ex){
			throw ex;
		}
	}
	
}
