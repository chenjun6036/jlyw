package com.jlyw.manager;

import java.lang.reflect.Method;
import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.BaseType;
import com.jlyw.hibernate.BaseTypeDAO;
import com.jlyw.hibernate.Reason;
import com.jlyw.util.FlagUtil;
import com.jlyw.util.KeyValueWithOperator;

public class BaseTypeManager {
	private BaseTypeDAO m_dao = new BaseTypeDAO();
	/**
	 * ����BaseType Id ���� BaseType����
	 * @param id: BaseType Id
	 * @return BaseType����
	 */
	public BaseType findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��BaseType��¼
	 * @param appSpecies BaseType����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(BaseType appSpecies){
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
	 * ����һ��BaseType��¼
	 * @param appSpecies BaseType����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(BaseType appSpecies){
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
	 * ����һ��BaseType��¼
	 * @param appSpecies BaseType����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(BaseType appSpecies, String OldName){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			String NewName = appSpecies.getName();
			if(!OldName.equals(NewName)){
				String[] attrs = FlagUtil.BaseTypeInfoType.getBaseTypeAttr(appSpecies.getType()).split("\\|");
				String className = attrs[0];
				String key = attrs[1];
				Class cDAO = Class.forName(String.format("com.jlyw.hibernate.%sDAO", className));
				Method mSet, mFind, mUpdate;
				mFind = cDAO.getMethod("findByVarProperty", String.class, KeyValueWithOperator[].class);
				String temp = key;
				temp = temp.replaceFirst(temp.substring(0, 1), temp.substring(0,1).toLowerCase());
				KeyValueWithOperator k;
				if(appSpecies.getType()==FlagUtil.BaseTypeInfoType.Type_CustomerType)
					k = new KeyValueWithOperator(temp, "%" + OldName + "%", "like");
				else
					k = new KeyValueWithOperator(temp, OldName, "=");
				mFind.getParameterTypes();
				List<Object> retList = (List<Object>) mFind.invoke(cDAO.newInstance(), className, (Object)new KeyValueWithOperator[]{k});
				
				for(Object obj : retList){
					String str;
					if(appSpecies.getType()==FlagUtil.BaseTypeInfoType.Type_CustomerType){
						Method mGet = obj.getClass().getMethod(String.format("get%s", key.toString()));
						str = (String)mGet.invoke(obj);
						str = str.replace(OldName, NewName);						
					}
					else
						str = NewName;
					mSet = obj.getClass().getMethod(String.format("set%s", key.toString()), String.class);
					mSet.invoke(obj, str);
					mUpdate = cDAO.getMethod("update", Object.class);
					mUpdate.invoke(cDAO.newInstance(), (Object)obj);
				}
			}
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
	 * ����BaseType Id,ɾ��BaseType����
	 * @param id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			BaseType u = m_dao.findById(id);
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
	 * ����������
	 * @param arr
	 * @return
	 */
	public List<BaseType> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("BaseType", arr);
		}
		catch(Exception e){
			return null;
		}
	}
	
	/**
	 * �õ�����ApplianceAccuracy��¼��
	 * @param arr ������ֵ��
	 * @return ApplianceAccuracy��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("BaseType", arr);		
	}
	
	/**
	 * ��ҳ����
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ�ļ�¼��
	 * @param list ������ֵ��
	 * @return ��ҳ���Reason�б�
	 */
	public List<BaseType> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("BaseType", currentPage,pageSize, arr);
		} catch (Exception e) {
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
