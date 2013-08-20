package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.OriginalRecordExcel;
import com.jlyw.hibernate.OriginalRecordExcelDAO;
import com.jlyw.util.KeyValueWithOperator;

public class OriginalRecordExcelManager {
private OriginalRecordExcelDAO m_dao = new OriginalRecordExcelDAO();
	
	/**
	 * ����OriginalRecordExcel Id ���� OriginalRecordExcel����
	 * @param id: OriginalRecordExcel Id
	 * @return OriginalRecordExcel����
	 */
	public OriginalRecordExcel findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��OriginalRecordExcel��¼
	 * @param appSpecies OriginalRecordExcel����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(OriginalRecordExcel appSpecies){
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
	 * ����һ��OriginalRecordExcel��¼
	 * @param appSpecies OriginalRecordExcel����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(OriginalRecordExcel appSpecies){
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
	 * ����OriginalRecordExcel Id,ɾ��OriginalRecordExcel����
	 * @param id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			OriginalRecordExcel u = m_dao.findById(id);
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
	 * @return ��ҳ���OriginalRecordExcel�б�
	 */
	public List<OriginalRecordExcel> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("OriginalRecordExcel", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * �õ�����OriginalRecordExcel��¼��
	 * @param arr ������ֵ��
	 * @return OriginalRecordExcel��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("OriginalRecordExcel", arr);
	}
	/**
	 * �õ�����OriginalRecordExcel��¼��
	 * @param arr ��ѯ�����б�
	 * @return OriginalRecordExcel��¼��
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("OriginalRecordExcel", arr);
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List<OriginalRecordExcel> findByExample(OriginalRecordExcel instance) {
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
	* @return ��ҳ��������б�- List<OriginalRecordExcel>
	*/
	public List<OriginalRecordExcel> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findPagedAllBySort("OriginalRecordExcel", currentPage, pageSize, orderby, asc, arr);
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
	* @return ��ҳ��������б�- List<OriginalRecordExcel>
	*/
	public List<OriginalRecordExcel> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		try{
			return m_dao.findPagedAllBySort("OriginalRecordExcel", currentPage, pageSize, orderby, asc, arr);
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
	 * �����������Բ��ң���ָ�������ֶ�
	 * @param TableName:���ĸ������ѯ
	 * @param orderby�������ĸ��ֶ�����
	 * @param asc��true ���� false ����
	 * @param arr ��������ֵ��
	 */
	public List<OriginalRecordExcel> findByPropertyBySort(String orderby, boolean asc, KeyValueWithOperator...arr){
		try{
			return m_dao.findByPropertyBySort("OriginalRecordExcel", orderby, asc, arr);
		}
		catch(Exception e){
			return null;
		}
	}
}
