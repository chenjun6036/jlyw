package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.CommissionSheet;
import com.jlyw.hibernate.CommissionSheetDAO;
import com.jlyw.hibernate.Overdue;
import com.jlyw.hibernate.OverdueDAO;
import com.jlyw.util.KeyValueWithOperator;

public class OverdueManager {
private OverdueDAO m_dao = new OverdueDAO();
	
	/**
	 * ����Overdue Id ���� Overdue����
	 * @param id: Overdue Id
	 * @return Overdue����
	 */
	public Overdue findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��Overdue��¼
	 * @param appSpecies Overdue����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(Overdue appSpecies){
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
	 * ����һ��Overdue��¼
	 * @param appSpecies Overdue����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(Overdue appSpecies){
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
	 * ����Overdue Id,ɾ��Overdue����
	 * @param id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			Overdue u = m_dao.findById(id);
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
	 * @return ��ҳ���Overdue�б�
	 */
	public List<Overdue> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("Overdue", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * �õ�����Overdue��¼��
	 * @param arr ������ֵ��
	 * @return Overdue��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("Overdue", arr);
	}
	/**
	 * �õ�����Overdue��¼��
	 * @param arr ��ѯ�����б�
	 * @return Overdue��¼��
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("Overdue", arr);
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List<Overdue> findByExample(Overdue instance) {
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
	* @return ��ҳ��������б�- List<Overdue>
	*/
	public List<Overdue> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findPagedAllBySort("Overdue", currentPage, pageSize, orderby, asc, arr);
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
	* @return ��ҳ��������б�- List<Overdue>
	*/
	public List<Overdue> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		try{
			return m_dao.findPagedAllBySort("Overdue", currentPage, pageSize, orderby, asc, arr);
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
	public List<Overdue> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("Overdue", arr);
		}
		catch(Exception e){
			return null;
		}
	}
	
	/**
	 * ���ڰ����������ͨ������ί�е��ĳ�ŵ������ڸ���
	 * @param o
	 * @param cSheet
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public boolean overdueHandle(Overdue o, CommissionSheet cSheet) throws Exception{
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(o);
			if(o.getExecuteResult() && cSheet.getPromiseDate() != null && o.getDelayDays() != null){
				CommissionSheetDAO cDao = new CommissionSheetDAO();
				java.sql.Date d = cSheet.getPromiseDate();
				d.setDate(d.getDate() + o.getDelayDays());
				cSheet.setPromiseDate(d);
				cDao.update(cSheet);
			}
			tran.commit();
			return true;
		} catch (Exception e) {
			tran.rollback();
			throw new Exception(String.format("�������ݿ�ʧ�ܣ���Ϣ��%s", e.getMessage()==null?"��":e.getMessage()));
		} finally {
			m_dao.closeSession();
		}
	}
}
