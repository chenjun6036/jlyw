package com.jlyw.manager;



import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Transaction;

import com.jlyw.hibernate.SysUser;
import com.jlyw.hibernate.SysUserDAO;
import com.jlyw.util.KeyValueWithOperator;


public class UserManager {
	private SysUserDAO m_dao = new SysUserDAO();
	
	/**
	 * ����User Id ���� User����
	 * @param id User Id
	 * @return User����
	 */
	public SysUser findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��User��¼
	 * @param user User����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(SysUser user){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			m_dao.save(user);
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
	 * ����һ��User��¼
	 * @param user User����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(SysUser user){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(user);
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
	 * ����User Id,ɾ��User����
	 * @param id User id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			SysUser u = m_dao.findById(id);
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
	 * @return ��ҳ���User�б�
	 */
	public List<SysUser> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("SysUser", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * ��ҳ����
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ�ļ�¼��
	 * @return ��ҳ���Specification�б�
	 */
	public List<SysUser> findPagedAll(int currentPage, int pageSize, List<KeyValueWithOperator> arr) {
		try {
			return m_dao.findPagedAll("SysUser", currentPage, pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	
	/**
	 * �õ�����User��¼��
	 * @return User��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("SysUser", arr);		
	}
	
	/**
	 * �õ�����User��¼��
	 * @return User��¼��
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("SysUser",arr);		
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List findByExample(SysUser instance) {
		return m_dao.findByExample(instance);
	}
	
	/**
	 * �ж��û����Ƿ��Ѿ�����
	 * @param userName �û���
	 * @return ���ڷ���true�������ڷ���false
	 */
	public boolean isUserNameExist(String userName){
		SysUser user = new SysUser();
		user.setUserName(userName);
		List<SysUser> list= m_dao.findByExample(user);
		if(list==null||list.size()==0){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 
	 * @param arr
	 * @return
	 */
	public List<SysUser> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("SysUser", arr);
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
	* ��ҳ��ʾ����
	* @param currentPage
	* ��ǰҳ��, �� 1 ��ʼ
	* @param pageSize
	* ÿҳ��ʾ������
	* @param orderby�������ĸ��ֶ�����
	* @param asc��true ���� false ����
	* @param arr:Ϊ��ѯ������(��-ֵ)������
	* @return ��ҳ��������б�- List<TaskAssign>
	*/
	public List<SysUser> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator> condList){
		try{
			return m_dao.findPagedAllBySort("SysUser", currentPage, pageSize, orderby, asc, condList);
		}catch(Exception e){
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
	* ����HQL����
	* @param updateString HQL��䣨update ���� set �ֶ�=ֵ where ������
	* @param arr ����
	* @return ���²���Ӱ��ļ�¼��
	*/
	public int updateByHQL(String updateString, Object...arr) {		
		Transaction tran = m_dao.getSession().beginTransaction();
		try{
			int i = m_dao.updateByHQL(updateString, arr);
			tran.commit();
			return i;
		} catch (Exception e) {
			e.printStackTrace();
			tran.rollback();
			return 0;
		}finally {
			m_dao.closeSession();
		}
	}
	
	public List<String> formatExcel(Object obj){
		List<String> result = new ArrayList<String>();
		result.add(((SysUser)obj).getJobNum().toString());
		result.add(((SysUser)obj).getName().toString());
		result.add(((SysUser)obj).getGender()?"��":"Ů");
		result.add(((SysUser)obj).getWorkLocationId().toString());
		result.add(((SysUser)obj).getBirthday().toString());
		result.add(((SysUser)obj).getBirthplace().toString());
		result.add(((SysUser)obj).getIdnum().toString());
		result.add(((SysUser)obj).getPoliticsStatus().toString());
		result.add(((SysUser)obj).getNation().toString());
		result.add(((SysUser)obj).getWorkSince().toString());
		result.add(((SysUser)obj).getWorkHereSince().toString());
		result.add(((SysUser)obj).getEducation().toString());
		result.add(((SysUser)obj).getEducationDate().toString());
		result.add(((SysUser)obj).getEducationFrom().toString());
		result.add(((SysUser)obj).getDegree().toString());
		result.add(((SysUser)obj).getDegreeDate().toString());
		result.add(((SysUser)obj).getDegreeFrom().toString());
		result.add(((SysUser)obj).getJobTitle().toString());
		result.add(((SysUser)obj).getSpecialty().toString());
		result.add(((SysUser)obj).getAdministrationPost().toString());
		result.add(((SysUser)obj).getPartyPost()==null?"":((SysUser)obj).getPartyPost().toString());
		result.add(((SysUser)obj).getPartyDate()==null?"":((SysUser)obj).getPartyDate().toString());
		result.add(((SysUser)obj).getHomeAdd().toString());
		result.add(((SysUser)obj).getWorkAdd().toString());
		result.add(((SysUser)obj).getTel().toString());
		result.add(((SysUser)obj).getCellphone1().toString());
		result.add(((SysUser)obj).getCellphone2()==null?"":((SysUser)obj).getCellphone2().toString());
		result.add(((SysUser)obj).getEmail().toString());
		result.add(((SysUser)obj).getProjectTeamId().toString());
		result.add(((SysUser)obj).getStatus().toString());
		result.add(((SysUser)obj).getType().toString());
		result.add(((SysUser)obj).getCancelDate()==null?"":((SysUser)obj).getCancelDate().toString());
		result.add(((SysUser)obj).getRemark()==null?"":((SysUser)obj).getRemark().toString());
		
		return result;
	}
	
	public List<String> formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("����");
		result.add("����");
		result.add("�Ա�");
		result.add("��������");
		result.add("��������");
		result.add("������");
		result.add("���֤��");
		result.add("������ò");
		result.add("����");
		result.add("�μӹ�������");
		result.add("������������");
		result.add("ѧ��");
		result.add("ȡ��ѧ��ʱ��");
		result.add("ѧ����ҵԺУ");
		result.add("ѧλ");
		result.add("ȡ��ѧλʱ��");
		result.add("ѧλ��ҵԺУ");
		result.add("ְ��");
		result.add("רҵ");
		result.add("����ְ��");
		result.add("����ְ��");
		result.add("�뵳ʱ��");
		result.add("��ͥסַ");
		result.add("������ַ");
		result.add("�칫�绰");
		result.add("��ϵ�绰1");
		result.add("��ϵ�绰2");
		result.add("Email");
		result.add("������Ŀ��");
		result.add("״̬");
		result.add("��Ա����");
		result.add("ע��ʱ��");
		result.add("��ע");
		
		return result;
	}
	
}

