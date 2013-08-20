package com.jlyw.manager;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.StandardAppliance;
import com.jlyw.hibernate.StandardApplianceDAO;
import com.jlyw.util.KeyValueWithOperator;

public class StandardApplianceManager {
private StandardApplianceDAO m_dao = new StandardApplianceDAO();
	
	/**
	 * ����User Id ���� User����
	 * @param id User Id
	 * @return StandardAppliance����
	 */
	public StandardAppliance findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��StandardAppliance��¼
	 * @param StandardAppliance StandardAppliance����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(StandardAppliance StandardAppliance){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			m_dao.save(StandardAppliance);
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
	 * ����һ��StandardAppliance��¼
	 * @param StandardAppliance StandardAppliance����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(StandardAppliance StandardAppliance){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(StandardAppliance);
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
	 * ����StandardAppliance Id,ɾ��StandardAppliance����
	 * @param id StandardAppliance id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			StandardAppliance u = m_dao.findById(id);
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
	 * @return ��ҳ���StandardAppliance�б�
	 */
	public List<StandardAppliance> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator... arr) {
		try {
			return m_dao.findPagedAll("StandardAppliance", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * ��ҳ����
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ�ļ�¼��
	 * @return ��ҳ���StandardAppliance�б�
	 */
	public List<StandardAppliance> findPagedAll(int currentPage, int pageSize, List<KeyValueWithOperator> arr) {
		try {
			return m_dao.findPagedAll("StandardAppliance", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * �õ�����StandardAppliance��¼��
	 * @return StandardAppliance��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("StandardAppliance",arr);		
	}
	
	/**
	 * �õ�����StandardAppliance��¼��
	 * @return StandardAppliance��¼��
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("StandardAppliance",arr);		
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List findByExample(StandardAppliance instance) {
		return m_dao.findByExample(instance);
		
	}
	
	/**
	 * 
	 * @param arr
	 * @return
	 */
	public List<StandardAppliance> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("StandardAppliance", arr);
		}
		catch(Exception e){
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
	 * �õ���¼����
	 * @param queryString ��ѯ��䣨HQL��
	 * @param arr ��ѯ�����?��Ӧ��ֵ
	 * @return
	 */
	public int getTotalCountByHQL(String queryString,List<Object> arr) {
		try{
			return m_dao.getTotalCountByHQL(queryString, arr);
		}catch(Exception ex){
			return 0;
		}
	}
	
	/**
	* ��ʾ����
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
	
	public List<String> formatExcel(Object obj){
		List<String> result = new ArrayList<String>();
		result.add(((StandardAppliance)obj).getId().toString());
		result.add(((StandardAppliance)obj).getName().toString());
		result.add(((StandardAppliance)obj).getLocaleCode().toString());
		result.add(((StandardAppliance)obj).getBrief().toString());
		result.add(((StandardAppliance)obj).getModel().toString());
		result.add(((StandardAppliance)obj).getRange().toString());
		result.add(((StandardAppliance)obj).getUncertain().toString());
		result.add(((StandardAppliance)obj).getTestCycle().toString());
		result.add(((StandardAppliance)obj).getSeriaNumber().toString());
		result.add(((StandardAppliance)obj).getManufacturer().toString());
		result.add(((StandardAppliance)obj).getReleaseDate()==null?"":((StandardAppliance)obj).getReleaseDate().toString());
		result.add(((StandardAppliance)obj).getReleaseNumber().toString());
		result.add(((StandardAppliance)obj).getNum().toString());
		result.add(((StandardAppliance)obj).getStatus().toString());
		result.add(((StandardAppliance)obj).getPrice().toString());
		result.add(((StandardAppliance)obj).getSysUser().getName().toString());
		result.add(((StandardAppliance)obj).getPermanentCode().toString());
		result.add(((StandardAppliance)obj).getProjectCode().toString());
		result.add(((StandardAppliance)obj).getBudget().toString());
		result.add(((StandardAppliance)obj).getInspectMonth()==null?"":((StandardAppliance)obj).getInspectMonth().toString());
		result.add(((StandardAppliance)obj).getWarnSlot()==null?"":((StandardAppliance)obj).getWarnSlot().toString());
		result.add(((StandardAppliance)obj).getValidDate()==null?"":((StandardAppliance)obj).getValidDate().toString());
		result.add(((StandardAppliance)obj).getRemark()==null?"":((StandardAppliance)obj).getRemark().toString());		
		return result;
	}
	
	public List<String> formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("���");
		result.add("��������");
		result.add("���ڱ��");
		result.add("ƴ������");
		result.add("�ͺŹ��");
		result.add("������Χ");
		result.add("��ȷ����");
		result.add("�춨����");
		result.add("֤���");
		result.add("��������");
		result.add("��������");
		result.add("�������");
		result.add("��������");
		result.add("����״̬");
		result.add("���߼۸�");
		result.add("������");
		result.add("�̶��ʲ����");
		result.add("��Ŀ���");
		result.add("Ԥ���ʽ�");
		result.add("�ܼ��·�");
		result.add("��Ч��Ԥ������");
		result.add("��Ч��");
		result.add("��ע");
		
		return result;
	}
	
}
