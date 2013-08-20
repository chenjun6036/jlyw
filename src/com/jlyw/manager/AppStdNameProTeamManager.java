package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.AppStdNameProTeam;
import com.jlyw.hibernate.AppStdNameProTeamDAO;
import com.jlyw.util.KeyValueWithOperator;

public class AppStdNameProTeamManager {
	private AppStdNameProTeamDAO m_dao = new AppStdNameProTeamDAO();
	
	/**
	 * ����AppStdNameProTeam Id ���� AppStdNameProTeam����
	 * @param id: AppStdNameProTeam Id
	 * @return AppStdNameProTeam����
	 */
	public AppStdNameProTeam findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��AppStdNameProTeam��¼
	 * @param appSpecies AppStdNameProTeam����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(AppStdNameProTeam appSpecies){
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
	 * ����һ��AppStdNameProTeam��¼
	 * @param appSpecies AppStdNameProTeam����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(AppStdNameProTeam appSpecies){
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
	 * ����AppStdNameProTeam Id,ɾ��AppStdNameProTeam����
	 * @param id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			AppStdNameProTeam u = m_dao.findById(id);
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
	 * @return ��ҳ���AppStdNameProTeam�б�
	 */
	public List<AppStdNameProTeam> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("AppStdNameProTeam", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * �õ�����AppStdNameProTeam��¼��
	 * @param arr ������ֵ��
	 * @return AppStdNameProTeam��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("AppStdNameProTeam", arr);		
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List<AppStdNameProTeam> findByExample(AppStdNameProTeam instance) {
		return m_dao.findByExample(instance);
	}
	
	/**
	 * 
	 * @param arr
	 * @return
	 */
	public List<AppStdNameProTeam> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("AppStdNameProTeam", arr);
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
}
