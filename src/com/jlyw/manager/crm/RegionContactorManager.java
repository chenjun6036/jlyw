/**
 * 
 */
package com.jlyw.manager.crm;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Transaction;
import org.json.me.JSONObject;

import com.jlyw.hibernate.RegionInsideContactor;
import com.jlyw.hibernate.RegionInsideContactorDAO;
import com.jlyw.hibernate.crm.InsideContactor;
import com.jlyw.hibernate.crm.InsideContactorDAO;
import com.jlyw.util.KeyValueWithOperator;

/**
 * @author xx
 *
 */
public class RegionContactorManager {
	private RegionInsideContactorDAO m_dao=new RegionInsideContactorDAO();
	/**
	 * 
	 */
	public RegionInsideContactor FindById(int id)
	{
		return m_dao.findById(id);
	}
	
	public boolean save(RegionInsideContactor c){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			m_dao.save(c);
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
	 * ����һ��CustomerServiceFee��¼
	 * @param CustomerServiceFee CustomerServiceFee����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(RegionInsideContactor c){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(c);
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
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			RegionInsideContactor u = m_dao.findById(id);
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
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List findByExample(RegionInsideContactor instance) {
		return m_dao.findByExample(instance);
		
	}
	
	/**
	 * ��������ѯ
	 * @param arr
	 * @return
	 */
	public List<RegionInsideContactor> findByVarProperty(KeyValueWithOperator...arr){
		return m_dao.findByVarProperty("RegionInsideContactor", arr);
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
	public int getTotalCountByHQL(String queryString,List<Object> arr) {
		try{
			return m_dao.getTotalCountByHQL(queryString, arr);
		}catch(Exception ex){
			return 0;
		}
	}
	/////////////////������һ��ѯ��䣬���ؽ����
	public int getTotalCountByHQL1(String queryString,List<Object> arr) {
		try{
			return m_dao.getTotalCountByHQL1(queryString, arr);
		}catch(Exception ex){
			return 0;
		}
	}
	
	/*public List<String> formatExcel(Object obj) {
		List<String> result = new ArrayList<String>();
		JSONObject tmp = (JSONObject) obj;
		try {
			result.add(tmp.get("customerName")==null?"/":tmp.get("customerName").toString());
			result.add(tmp.get("address")==null?"/":tmp.get("address").toString());
			result.add(tmp.get("SysName")==null?"/":tmp.get("SysName").toString());
			result.add(tmp.get("jobNum")==null?"/":tmp.get("jobNum").toString());
			result.add(tmp.get("cellphone1")==null?"/":tmp.get("cellphone1").toString());
			result.add(tmp.get("cellphone2")==null?"/":tmp.get("cellphone2").toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
	
	public List<String> formatTitle() {
		List<String> result = new ArrayList<String>();
		try {
			result.add("��λ����");//���
			result.add("��ַ");
			result.add("��ϵ������");
			result.add("��ϵ�˹���");
			result.add("��ϵ�绰");
			result.add("�绰2");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}*/
	
	public RegionContactorManager() {
		// TODO Auto-generated constructor stub


}


}

