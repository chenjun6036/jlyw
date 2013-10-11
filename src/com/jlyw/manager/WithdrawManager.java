package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.CommissionSheet;
import com.jlyw.hibernate.CommissionSheetDAO;
import com.jlyw.hibernate.Withdraw;
import com.jlyw.hibernate.WithdrawDAO;
import com.jlyw.util.KeyValueWithOperator;

/**
 * ����
 * @author Administrator
 *
 */
public class WithdrawManager {
private WithdrawDAO m_dao = new WithdrawDAO();
	
	/**
	 * ����Withdraw Id ���� Withdraw����
	 * @param id: Withdraw Id
	 * @return Withdraw����
	 */
	public Withdraw findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��Withdraw��¼
	 * @param appSpecies Withdraw����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(Withdraw appSpecies){
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
	 * ����һ��Withdraw��¼
	 * @param appSpecies Withdraw����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(Withdraw appSpecies){
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
	 * ����Withdraw Id,ɾ��Withdraw����
	 * @param id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			Withdraw u = m_dao.findById(id);
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
	 * @return ��ҳ���Withdraw�б�
	 */
	public List<Withdraw> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("Withdraw", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * �õ�����Withdraw��¼��
	 * @param arr ������ֵ��
	 * @return Withdraw��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("Withdraw", arr);
	}
	/**
	 * �õ�����Withdraw��¼��
	 * @param arr ��ѯ�����б�
	 * @return Withdraw��¼��
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("Withdraw", arr);
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List<Withdraw> findByExample(Withdraw instance) {
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
	* @return ��ҳ��������б�- List<Withdraw>
	*/
	public List<Withdraw> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findPagedAllBySort("Withdraw", currentPage, pageSize, orderby, asc, arr);
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
	* @return ��ҳ��������б�- List<Withdraw>
	*/
	public List<Withdraw> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		try{
			return m_dao.findPagedAllBySort("Withdraw", currentPage, pageSize, orderby, asc, arr);
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
	public List<Withdraw> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("Withdraw", arr);
		}
		catch(Exception e){
			return null;
		}
	}
	
	
	/**
	 * ����
	 * ����һ��Withdraw��¼
	 * @param appSpecies Withdraw����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean withdrawUpdate(Withdraw appSpecies){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(appSpecies);
			CommissionSheet comSheet= appSpecies.getCommissionSheet();
			String queryString = "from Withdraw as a where a.commissionSheet.id = ? and a.commissionSheet.status != ? and a.executeResult = true";
			List<Withdraw> withdrawList = m_dao.findByHQL(queryString, comSheet.getId(),10);//��ע��
			int number=0;
			for(Withdraw w:withdrawList){
				number +=w.getNumber();
			}
			if(number==comSheet.getQuantity()){
				m_dao.updateByHQL("update CommissionSheet set status = 9,finishLocation = ? where id = ? ",appSpecies.getLocation(),comSheet.getId());	//���ѽ���״̬
			}		
			tran.commit() ;
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
