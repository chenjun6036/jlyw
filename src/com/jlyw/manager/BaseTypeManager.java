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
	 * 根据BaseType Id 查找 BaseType对象
	 * @param id: BaseType Id
	 * @return BaseType对象
	 */
	public BaseType findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条BaseType记录
	 * @param appSpecies BaseType对象
	 * @return 插入成功，返回true；否则返回false
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
	 * 更新一条BaseType记录
	 * @param appSpecies BaseType对象
	 * @return 更新成功，返回true；否则返回false
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
	 * 更新一条BaseType记录
	 * @param appSpecies BaseType对象
	 * @return 更新成功，返回true；否则返回false
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
	 * 根据BaseType Id,删除BaseType对象
	 * @param id
	 * @return 删除成功，返回true；否则返回false
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
	 * 按条件查找
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
	 * 得到所有ApplianceAccuracy记录数
	 * @param arr 条件键值对
	 * @return ApplianceAccuracy记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("BaseType", arr);		
	}
	
	/**
	 * 分页函数
	 * @param currentPage 当前页码
	 * @param pageSize 每页的记录数
	 * @param list 条件键值对
	 * @return 分页后的Reason列表
	 */
	public List<BaseType> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("BaseType", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	* 分页显示数据
	*@param queryString:查询语句（HQL）
	* @param arr 查询语句中?对应的值
	* @return 分页后的数据列表- List
	*/
	public List findByHQL(String queryString, Object...arr){
		try{
			return m_dao.findByHQL(queryString, arr);
		}catch(Exception e){
			return null;
		}
	}
	
}
