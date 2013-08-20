package com.jlyw.manager;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.Customer;
import com.jlyw.hibernate.CustomerContactor;
import com.jlyw.hibernate.Department;
import com.jlyw.hibernate.DepartmentDAO;
import com.jlyw.util.KeyValueWithOperator;

public class DepartmentManager {
private DepartmentDAO m_dao = new DepartmentDAO();
	
	/**
	 * ����User Id ���� User����
	 * @param id User Id
	 * @return Department����
	 */
	public Department findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��Department��¼
	 * @param Department Department����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(Department Department){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			m_dao.save(Department);
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
	 * ����һ��Department��¼
	 * @param Department Department����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(Department Department){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(Department);
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
	 * ����Department Id,ɾ��Department����
	 * @param id Department id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			Department u = m_dao.findById(id);
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
	 * @return ��ҳ���Department�б�
	 */
	public List<Department> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator... arr) {
		try {
			return m_dao.findPagedAll("Department", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * ��ҳ����
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ�ļ�¼��
	 * @return ��ҳ���Department�б�
	 */
	public List<Department> findPagedAll(int currentPage, int pageSize, List<KeyValueWithOperator> arr) {
		try {
			return m_dao.findPagedAll("Department", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * �õ�����Department��¼��
	 * @return Department��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("Department",arr);		
	}
	
	/**
	 * �õ�����Department��¼��
	 * @return Department��¼��
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("Department",arr);		
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List findByExample(Department instance) {
		return m_dao.findByExample(instance);
		
	}
	
	/**
	 * ��������ѯ
	 * @param arr
	 * @return
	 */
	public List<Department> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("Department", arr);
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
	
	public List<String> formatExcel(Object obj){
		List<String> result = new ArrayList<String>();
		
		result.add(((Department)obj).getId().toString());
		result.add(((Department)obj).getName().toString());
		result.add(((Department)obj).getBrief().toString());
		result.add(((Department)obj).getStatus().toString());
		result.add(((Department)obj).getDeptCode());
	
		return result;
	}
	
	public List<String> formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("���");
		result.add("��λ����");
		result.add("ƴ������");		
		result.add("����״̬");		
		result.add("���Ŵ���");
		
		return result;
	}
}
