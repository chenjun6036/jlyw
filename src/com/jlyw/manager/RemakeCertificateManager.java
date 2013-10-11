package com.jlyw.manager;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Transaction;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.jlyw.hibernate.RemakeCertificate;
import com.jlyw.hibernate.RemakeCertificateDAO;
import com.jlyw.util.KeyValueWithOperator;

public class RemakeCertificateManager {
	private RemakeCertificateDAO m_dao = new RemakeCertificateDAO();
	/**
	 * ����RemakeCertificate Id ���� RemakeCertificate����
	 * @param id: RemakeCertificate Id
	 * @return RemakeCertificate����
	 */
	public RemakeCertificate findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��RemakeCertificate��¼
	 * @param appSpecies RemakeCertificate����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(RemakeCertificate appSpecies){
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
	 * ����һ��RemakeCertificate��¼
	 * @param appSpecies RemakeCertificate����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(RemakeCertificate appSpecies){
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
	 * ����RemakeCertificate Id,ɾ��RemakeCertificate����
	 * @param id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			RemakeCertificate u = m_dao.findById(id);
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
	 * @return ��ҳ���RemakeCertificate�б�
	 */
	public List<RemakeCertificate> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("RemakeCertificate", currentPage,pageSize, arr);
		} catch (Exception e) {
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
		try{
			return m_dao.findPageAllByHQL(queryString, currentPage, pageSize, arr);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * �õ�����RemakeCertificate��¼��
	 * @param arr ������ֵ��
	 * @return RemakeCertificate��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("RemakeCertificate", arr);
	}
	/**
	 * �õ�����RemakeCertificate��¼��
	 * @param arr ��ѯ�����б�
	 * @return RemakeCertificate��¼��
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("RemakeCertificate", arr);
	}
	
	/**
	 * �õ���¼����
	 * @param queryString ��ѯ��䣨HQL��
	 * @param arr ��ѯ�����?��Ӧ��ֵ
	 * @return
	 */
	public int getTotalCountByHQL(String queryString,Object...arr) {
		try{
			return m_dao.getTotalCountByHQL(queryString, arr);
		}catch(Exception ex){
			return 0;
		}
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List<RemakeCertificate> findByExample(RemakeCertificate instance) {
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
	* @return ��ҳ��������б�- List<RemakeCertificate>
	*/
	public List<RemakeCertificate> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findPagedAllBySort("RemakeCertificate", currentPage, pageSize, orderby, asc, arr);
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
	* @return ��ҳ��������б�- List<RemakeCertificate>
	*/
	public List<RemakeCertificate> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		try{
			return m_dao.findPagedAllBySort("RemakeCertificate", currentPage, pageSize, orderby, asc, arr);
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
	public List findByHQL(String queryString, Object...arr) throws Exception{
		try{
			return m_dao.findByHQL(queryString, arr);
		}catch(Exception e){
			throw e;
		}
	}
	
	/**
	 * �����������Բ��ң���ָ�������ֶ�
	 * @param TableName:���ĸ������ѯ
	 * @param orderby�������ĸ��ֶ�����
	 * @param asc��true ���� false ����
	 * @param arr ��������ֵ��
	 */
	public List<RemakeCertificate> findByPropertyBySort(String orderby, boolean asc, KeyValueWithOperator...arr){
		try{
			return m_dao.findByPropertyBySort("RemakeCertificate", orderby, asc, arr);
		}
		catch(Exception e){
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
	public List<RemakeCertificate> findByPropertyBySort(String orderby, boolean asc, List<KeyValueWithOperator> arr){
		try{
			return m_dao.findByPropertyBySort("RemakeCertificate", orderby, asc, arr);
		}
		catch(Exception e){
			return null;
		}
	}
	
	/**
	 * �����������Բ��ң���ָ�������ֶ�
	 * @param TableName:���ĸ������ѯ

	 * @param arr ��������ֵ��
	 */
	public List<RemakeCertificate> findByVarProperty(KeyValueWithOperator...arr){
		return m_dao.findByVarProperty("RemakeCertificate", arr);
	}
	
	
	public List<String> formatExcel(Object obj) throws JSONException{
		List<String> result = new ArrayList<String>();
		JSONObject jsonObj = (JSONObject)obj;
		result.add(jsonObj.has("CommissionCode")?jsonObj.getString("CommissionCode"):"");
		result.add(jsonObj.has("ApplianceName")?jsonObj.getString("ApplianceName"):"");
		result.add(jsonObj.has("CustomerName")?jsonObj.getString("CustomerName"):"");
		result.add(jsonObj.has("CustomerContactor")?jsonObj.getString("CustomerContactor"):"");
		result.add(jsonObj.has("CustomerContactorTel")?jsonObj.getString("CustomerContactorTel"):"");
		result.add(jsonObj.has("CommissionDate")?jsonObj.getString("CommissionDate"):"");
		result.add(jsonObj.has("CertificateCode")?jsonObj.getString("CertificateCode"):"");
		result.add(jsonObj.has("CreatorName")?jsonObj.getString("CreatorName"):"");
		result.add(jsonObj.has("CreateTime")?jsonObj.getString("CreateTime"):"");
		result.add(jsonObj.has("CreateRemark")?jsonObj.getString("CreateRemark"):"");
		result.add(jsonObj.has("ReceiverName")?jsonObj.getString("ReceiverName"):"");
		result.add(jsonObj.has("FinishTime")?jsonObj.getString("FinishTime"):"");
		result.add(jsonObj.has("FinishRemark")?jsonObj.getString("FinishRemark"):"");
		result.add(jsonObj.has("PassedTime")?jsonObj.getString("PassedTime"):"");
		
		return result;
	}

	public List<String> formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("ί�е���");
		result.add("��������");
		result.add("ί�е�λ");
		result.add("��ϵ��");
		result.add("��ϵ�˵绰");
		result.add("ί������");
		result.add("ԭ֤����");
		result.add("������");
		result.add("����ʱ��");
		result.add("��ע");
		result.add("������");
		result.add("���ʱ��");
		result.add("��ע");
		result.add("ͨ��ʱ��");
		
		return result;
	}
}
