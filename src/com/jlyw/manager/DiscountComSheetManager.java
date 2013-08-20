package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.CommissionSheet;
import com.jlyw.hibernate.DetailList;
import com.jlyw.hibernate.DetailListDAO;
import com.jlyw.hibernate.Discount;
import com.jlyw.hibernate.DiscountComSheet;
import com.jlyw.hibernate.DiscountComSheetDAO;
import com.jlyw.hibernate.DiscountDAO;
import com.jlyw.hibernate.OriginalRecord;
import com.jlyw.util.KeyValueWithOperator;

public class DiscountComSheetManager {
private DiscountComSheetDAO m_dao = new DiscountComSheetDAO();
	
	/**
	 * ����DiscountComSheet Id ���� DiscountComSheet����
	 * @param id: DiscountComSheet Id
	 * @return DiscountComSheet����
	 */
	public DiscountComSheet findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��DiscountComSheet��¼
	 * @param appSpecies DiscountComSheet����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(DiscountComSheet appSpecies){
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
	 * ����һ��DiscountComSheet��¼
	 * @param appSpecies DiscountComSheet����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(DiscountComSheet appSpecies){
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
	 * ����DiscountComSheet Id,ɾ��DiscountComSheet����
	 * @param id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			DiscountComSheet u = m_dao.findById(id);
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
	 * @return ��ҳ���DiscountComSheet�б�
	 */
	public List<DiscountComSheet> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("DiscountComSheet", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * ��ҳ����
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ�ļ�¼��
	 * @param arr ������ֵ��
	 * @return ��ҳ���DiscountComSheet�б�
	 */
	public List<DiscountComSheet> findPagedAll(int currentPage, int pageSize, List<KeyValueWithOperator> arr) {
		try {
			return m_dao.findPagedAll("DiscountComSheet", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * �õ�����DiscountComSheet��¼��
	 * @param arr ������ֵ��
	 * @return DiscountComSheet��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("DiscountComSheet", arr);
	}
	/**
	 * �õ�����DiscountComSheet��¼��
	 * @param arr ��ѯ�����б�
	 * @return DiscountComSheet��¼��
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("DiscountComSheet", arr);
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
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List<DiscountComSheet> findByExample(DiscountComSheet instance) {
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
	* @return ��ҳ��������б�- List<DiscountComSheet>
	*/
	public List<DiscountComSheet> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findPagedAllBySort("DiscountComSheet", currentPage, pageSize, orderby, asc, arr);
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
	* @return ��ҳ��������б�- List<DiscountComSheet>
	*/
	public List<DiscountComSheet> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		try{
			return m_dao.findPagedAllBySort("DiscountComSheet", currentPage, pageSize, orderby, asc, arr);
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
	public List<DiscountComSheet> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("DiscountComSheet", arr);
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
	public List<DiscountComSheet> findByVarProperty(List<KeyValueWithOperator> arr){
		try{
			return m_dao.findByVarProperty("DiscountComSheet", arr);
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
	public List<DiscountComSheet> findByPropertyBySort(String orderby, boolean asc, KeyValueWithOperator...arr){
		return m_dao.findByPropertyBySort("DiscountComSheet", orderby, asc, arr);
	}
	
	/**

	*@param queryString:��ѯ��䣨HQL��

	* @param arr ��ѯ�����?��Ӧ��ֵ
	* @return ��ҳ��������б�- List
	*/
	public List findByHQL(String queryString, List<Object> arr){
		try{
			return m_dao.findByHQL(queryString, arr);
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
	 * �����ۿ����뵥�����������ۿ�- ί�е���
	 * 
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean saveByBatch(List<DiscountComSheet> discountComSheetList,Discount discount) throws Exception{
		if(discountComSheetList == null || discount == null){
			throw new Exception("����Ϊ��");
		}
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			DiscountDAO dDAO = new DiscountDAO();	//�ۿ۱��DAO 
			dDAO.save(discount);
			for(int i = 0; i < discountComSheetList.size(); i++){
				DiscountComSheet Com = discountComSheetList.get(i);
				Com.setDiscount(discount);
				m_dao.save(Com);
			}
			tran.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			tran.rollback();
			throw e;
		} finally {
			m_dao.closeSession();
		}
	}
}
