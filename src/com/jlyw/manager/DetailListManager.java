package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.BaseHibernateDAO;
import com.jlyw.hibernate.CommissionSheet;
import com.jlyw.hibernate.DetailList;
import com.jlyw.hibernate.DetailListDAO;
import com.jlyw.util.KeyValueWithOperator;

/**
 * ԭʼ��¼
 * @author Administrator
 *
 */
public class DetailListManager {
private DetailListDAO m_dao = new DetailListDAO();
	
	/**
	 * ����DetailList Id ���� DetailList����
	 * @param id: DetailList Id
	 * @return DetailList����
	 */
	public DetailList findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��DetailList��¼
	 * @param appSpecies DetailList����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(DetailList appSpecies){
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
	 * ����һ��DetailList��¼
	 * @param appSpecies DetailList����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(DetailList appSpecies){
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
	 * ����DetailList Id,ɾ��DetailList����
	 * @param id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			DetailList u = m_dao.findById(id);
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
	 * @return ��ҳ���DetailList�б�
	 */
	public List<DetailList> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("DetailList", currentPage,pageSize, arr);
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
			ex.printStackTrace();
			return 0;
		}
	}
	/**
	 * �õ�����DetailList��¼��
	 * @param arr ������ֵ��
	 * @return DetailList��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("DetailList", arr);
	}
	/**
	 * �õ�����DetailList��¼��
	 * @param arr ��ѯ�����б�
	 * @return DetailList��¼��
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("DetailList", arr);
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
	public List<DetailList> findByExample(DetailList instance) {
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
	* @return ��ҳ��������б�- List<DetailList>
	*/
	public List<DetailList> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findPagedAllBySort("DetailList", currentPage, pageSize, orderby, asc, arr);
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
	* @return ��ҳ��������б�- List<DetailList>
	*/
	public List<DetailList> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		try{
			return m_dao.findPagedAllBySort("DetailList", currentPage, pageSize, orderby, asc, arr);
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
	* ��ҳ��ʾ����
	*@param queryString:��ѯ��䣨HQL��
	* @param arr ��ѯ�����?��Ӧ��ֵ
	* @return ��ҳ��������б�- List
	*/
	public List findByHQL(String queryString, List<Object> arr){
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
	public List<DetailList> findByPropertyBySort(String orderby, boolean asc, KeyValueWithOperator...arr){
		try{
			return m_dao.findByPropertyBySort("DetailList", orderby, asc, arr);
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
	public List<DetailList> findByPropertyBySort(String orderby, boolean asc, List<KeyValueWithOperator> arr){
		try{
			return m_dao.findByPropertyBySort("DetailList", orderby, asc, arr);
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
	public List<DetailList> findByVarProperty(KeyValueWithOperator...arr){
		return m_dao.findByVarProperty("DetailList", arr);
	}
	/**
	 * ����һ��DetailList��¼
	 * @param oRecordList DetailList �����б�
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean saveByBatch(List<DetailList> oRecordList){
		if(oRecordList == null){
			return false;
		}
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			for(DetailList oRecord : oRecordList){
				m_dao.save(oRecord);
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

}
