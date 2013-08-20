package com.jlyw.manager;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.Information;
import com.jlyw.hibernate.InformationDAO;
import com.jlyw.hibernate.SysUser;
import com.jlyw.util.KeyValueWithOperator;

public class InformationManager {
	private static InformationManager instance = null;
	/**
	 * ����һ����Ϣ֪ͨ
	 * @param number
	 * @param content
	 * @param receiver
	 * @param type
	 * @return
	 */
	public static boolean AddInformation(String Url, String content, SysUser receiver, int type){
		try{
			if(instance == null){
				instance = new InformationManager();
			}
			Information s = new Information();
			s.setUrl(Url);
			s.setContents(content);
			s.setSysUser(receiver);//������
			s.setType(type);
			s.setStatus(0);	//״̬��δ����
			s.setCreateDate(new Timestamp(System.currentTimeMillis()));
			return instance.save(s);
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * ����������
	 * @param arr
	 * @return �ɹ�����List��ʧ�ܷ���null
	 */
	public static List<Information> GetInformation(KeyValueWithOperator...arr){
		try{
			if(instance == null){
				instance = new InformationManager();
			}
			return instance.findByVarProperty(arr);
		}catch(Exception e){
			return null;
		}
	}
	
	public static List<Information> findPageAllInformationBySort(int currentPage, int pageSize, String orderby, boolean asc, KeyValueWithOperator...arr){
		try{
			if(instance == null){
				instance = new InformationManager();
			}
			return instance.findPagedAllBySort(currentPage, pageSize, orderby, asc, arr);
		}catch(Exception e){
			return null;
		}
	}
	
	public static int getTotalCountInformarion(KeyValueWithOperator...arr){
		try{
			if(instance == null){
				instance = new InformationManager();
			}
			return instance.getTotalCount(arr);
		}catch(Exception e){
			return 0;
		}
	}
	
	/**
	 * ����һ����Ϣ�Ѷ�
	 * @param info
	 * @return
	 */
	public static boolean setInformationReaded(Information info){
		try{
			if(instance == null){
				instance = new InformationManager();
			}
			info.setStatus(1);
			return instance.update(info);
		}catch(Exception e){
			return false;
		}
	}
	/**
	 * ������ɾ��
	 * @param sms
	 * @return
	 */
	public static int deleteInformation(List<KeyValueWithOperator> condList){
		try{
			if(instance == null){
				instance = new InformationManager();
			}
			return instance.delete(condList);
		}catch(Exception e){
			return -1;
		}
	}
	
	private InformationDAO m_dao = new InformationDAO();
	/**
	 * ����Information Id ���� Information����
	 * @param id: Information Id
	 * @return Information����
	 */
	public Information findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��Information��¼
	 * @param appSpecies Information����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(Information appSpecies){
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
	 * ����һ��Information��¼
	 * @param appSpecies Information����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(Information appSpecies){
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
	 * ����Information Id,ɾ��Information����
	 * @param id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			Information u = m_dao.findById(id);
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
	public int delete(List<KeyValueWithOperator> condList){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			int iRet = m_dao.delete("Information", condList);	
			tran.commit();
			return iRet;
		} catch (Exception e) {
			tran.rollback();
			return -1;
		} finally {
			m_dao.closeSession();
		}
	}

	/**
	 * ����������
	 * @param arr
	 * @return
	 */
	public List<Information> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("Information", arr);
		}
		catch(Exception e){
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
	* @param arr:Ϊ��ѯ������(��-ֵ)������
	* @return ��ҳ��������б�- List<Student>
	*/
	public List<Information> findPagedAllBySort(int currentPage, int pageSize, String orderby, boolean asc, KeyValueWithOperator...arr){
		try{
			return m_dao.findPagedAllBySort("Information", currentPage, pageSize, orderby, asc, arr);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * ��ȡ���������ļ�¼����
	 * @param ���arr����ѯ����������-ֵ-�����
	 * @return ���������ļ�¼����
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		try{
			return m_dao.getTotalCount("Information", arr);
		}catch(Exception e){
			return 0;
		}
	}
}
