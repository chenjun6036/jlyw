package com.jlyw.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Transaction;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.jlyw.hibernate.CommissionSheet;
import com.jlyw.hibernate.QuotationItem;
import com.jlyw.hibernate.Role;
import com.jlyw.hibernate.RolePrivilege;
import com.jlyw.hibernate.RolePrivilegeDAO;
import com.jlyw.hibernate.SysUser;
import com.jlyw.hibernate.UserRole;
import com.jlyw.hibernate.UserRoleDAO;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.UrlInfo;

public class RolePrivilegeManager {

private RolePrivilegeDAO m_dao = new RolePrivilegeDAO();
	
	/**
	 * ����Role Id ���� Role����
	 * @param id Role Id
	 * @return User����
	 */
	public RolePrivilege findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��Role��¼
	 * @param role Role����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(RolePrivilege role){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			m_dao.save(role);
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
	 * ����һ��Role��¼
	 * @param role Role����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(RolePrivilege role){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(role);
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
	 * ����Role Id,ɾ��Role����
	 * @param id Role id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			RolePrivilege u = m_dao.findById(id);
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
	 * @return ��ҳ���Role�б�
	 */
	public List<RolePrivilege> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("RolePrivilege", currentPage,pageSize, arr);
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
	public List<RolePrivilege> findPagedAll(int currentPage, int pageSize, List<KeyValueWithOperator> arr) {
		try {
			return m_dao.findPagedAll("RolePrivilege", currentPage, pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	
	/**
	 * �õ�����Role��¼��
	 * @return Role��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("RolePrivilege", arr);		
	}
	
	/**
	 * �õ�����Role��¼��
	 * @return Role��¼��
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("RolePrivilege",arr);		
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List findByExample(RolePrivilege instance) {
		return m_dao.findByExample(instance);
	}
	
	/**
	 * 
	 * @param arr
	 * @return
	 */
	public List<RolePrivilege> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("RolePrivilege", arr);
		}
		catch(Exception e){
			return null;
		}
	}
	public List<RolePrivilege> findByPropertyBySort(String orderby,boolean asc,KeyValueWithOperator...arr){
		return m_dao.findByPropertyBySort("RolePrivilege",orderby, asc, arr);
	}
	/**
	 * ����һ��RolePrivilege��¼
	 * @param RolePrivilegeList RolePrivilege����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean saveByBatch(List<RolePrivilege> RolePrivilegeList){
		if(RolePrivilegeList == null || RolePrivilegeList.size() == 0){
			return false;
		}
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			for(RolePrivilege RolePrivilege : RolePrivilegeList){
				m_dao.save(RolePrivilege);
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
	 * ����һ���û�������Ȩ����Ϣ���������ɫ�ĸ���ɫ��Ȩ��
	 * @param userid
	 * @return ��������Ȩ��(Url?����)Map<Integer, UrlInfo>�ļ���(Integer���Ȩ�޵�ID)�������򷵻�null
	 */
	@SuppressWarnings("unchecked")
	public Map<Integer, UrlInfo> getPrivilegesByUserId(Integer userid){
		try{
			Map<Integer, UrlInfo> privilegeMap = new HashMap<Integer, UrlInfo>();	//���е�Ȩ�޼���
			
			UserRoleDAO urDao = new UserRoleDAO();
			List<UserRole> urList = urDao.findByVarProperty("UserRole",
					new KeyValueWithOperator("sysUser.id", userid, "="),
					new KeyValueWithOperator("status", 1, "<>"),
					new KeyValueWithOperator("role.status", 1, "<>"));
			Set<Integer> roleSet = new HashSet<Integer>();	//�Ѳ��ҵĽ�ɫ����
			KeyValueWithOperator k1 = new KeyValueWithOperator("privilege.status", 1, "<>");
			KeyValueWithOperator k2 = new KeyValueWithOperator("status", 1, "<>");
			KeyValueWithOperator k3 = new KeyValueWithOperator("role.status", 1, "<>");
			for(UserRole ur :urList){
				Role role = ur.getRole();
				boolean bFlag = true;				
				while(bFlag){	//�ݹ���ҽ�ɫ���丸��ɫӵ�е�Ȩ��
					if(roleSet.contains(role.getId())){
						bFlag = false;
						continue;
					}
					roleSet.add(role.getId());
					List<RolePrivilege> retList = m_dao.findByVarProperty("RolePrivilege", 
							new KeyValueWithOperator("role.id", role.getId(), "="), k1, k2, k3);
					for(RolePrivilege rp : retList){
						String url = String.format("%s%s", rp.getPrivilege().getSysResources().getMappingUrl(), 
								(rp.getPrivilege().getParameters()==null || rp.getPrivilege().getParameters().length() == 0)?"":"?"+rp.getPrivilege().getParameters() );
						if(!privilegeMap.containsKey(rp.getPrivilege().getId())){
							privilegeMap.put(rp.getPrivilege().getId(), new UrlInfo(url));
						}
					}
					role = role.getRole();	//����ɫ
					if(role == null){
						bFlag = false;
					}
				}
			}
			
			return privilegeMap;
		}catch(Exception e){
			return null;
		}
	}
	/**�����û�Ȩ��
	 * ����һ���û�������Ȩ����Ϣ���������ɫ�ĸ���ɫ��Ȩ��
	 * @param userid
	 * @return
	 */
	public List<JSONObject> ExportPrivilegesByUserId(Integer userid){
		try{
			Map<Integer, UrlInfo> privilegeMap = new HashMap<Integer, UrlInfo>();	//���е�Ȩ�޼���
			List<JSONObject> result=new ArrayList<JSONObject>();
			
			UserRoleDAO urDao = new UserRoleDAO();
			List<UserRole> urList=new ArrayList<UserRole>();
			
			if(userid==null){				
				urList = urDao.findByVarProperty("UserRole",
						new KeyValueWithOperator("status", 1, "<>"),
						new KeyValueWithOperator("role.status", 1, "<>"));
			}else{
				
				urList = urDao.findByVarProperty("UserRole",
					new KeyValueWithOperator("sysUser.id", userid, "="),
					new KeyValueWithOperator("status", 1, "<>"),
					new KeyValueWithOperator("role.status", 1, "<>"));
			}
			Set<Integer> roleSet = new HashSet<Integer>();	//�Ѳ��ҵĽ�ɫ����
			KeyValueWithOperator k1 = new KeyValueWithOperator("privilege.status", 1, "<>");
			KeyValueWithOperator k2 = new KeyValueWithOperator("status", 1, "<>");
			KeyValueWithOperator k3 = new KeyValueWithOperator("role.status", 1, "<>");
			SysUser user=new SysUser();
			for(UserRole ur :urList){
				
				Role role = ur.getRole();
				user=ur.getSysUser();
				boolean bFlag = true;				
				while(bFlag){	//�ݹ���ҽ�ɫ���丸��ɫӵ�е�Ȩ��
					if(roleSet.contains(role.getId())){
						bFlag = false;
						continue;
					}
					roleSet.add(role.getId());
					List<RolePrivilege> retList = m_dao.findByVarProperty("RolePrivilege", 
							new KeyValueWithOperator("role.id", role.getId(), "="), k1, k2, k3);
					for(RolePrivilege rp : retList){
						String url = String.format("%s%s", rp.getPrivilege().getSysResources().getMappingUrl(), 
								(rp.getPrivilege().getParameters()==null || rp.getPrivilege().getParameters().length() == 0)?"":"?"+rp.getPrivilege().getParameters() );
						if(!privilegeMap.containsKey(rp.getPrivilege().getId())){//��ͬ��ɫ�Ľ���Ȩ��ֻ����һ��Ȩ��
							privilegeMap.put(rp.getPrivilege().getId(), new UrlInfo(url));
							JSONObject record=new JSONObject();
							record.put("UserName", user.getUserName());
							record.put("JobNum", user.getJobNum());
							record.put("Name", user.getName());
							record.put("RoleName", rp.getRole().getName());
							record.put("PrivilegeName", rp.getPrivilege().getName());
							result.add(record);
						}
					}
					role = role.getRole();	//����ɫ
					if(role == null){
						bFlag = false;
					}
				}
			}
			
			return result;
		}catch(Exception e){
			return null;
		}
	}
	
	public List<String> formatExcel(Object obj) throws JSONException{
		List<String> result = new ArrayList<String>();
		JSONObject jsonObj = (JSONObject)obj;
		result.add(jsonObj.has("UserName")?jsonObj.getString("UserName"):"");
		//result.add(jsonObj.has("JobNum")?jsonObj.getString("CommissionTypeName"):"");
		result.add(jsonObj.has("Name")?jsonObj.getString("Name"):"");
		result.add(jsonObj.has("RoleName")?jsonObj.getString("RoleName"):"");
		result.add(jsonObj.has("PrivilegeName")?jsonObj.getString("PrivilegeName"):"");	
		
		return result;
	}
	
	public List<String> formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("�û���");
		result.add("�û�����");
		result.add("��ɫ��");
		result.add("Ȩ����");
		
		return result;
	}
	
}
