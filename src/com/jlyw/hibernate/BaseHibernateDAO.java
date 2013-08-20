package com.jlyw.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.jlyw.util.KeyValue;
import com.jlyw.util.KeyValueWithOperator;


/**
 * Data access object (DAO) for domain model
 * 如果查询条件为：字段a为空/不空，则KeyValueWithOperator为（"key",null,"is null"）/（"key",null,"is not null"）;
 * @author MyEclipse Persistence Tools
 */
public class BaseHibernateDAO implements IBaseHibernateDAO {
	
	public Session getSession() {
		return HibernateSessionFactory.getSession();
	}

	public void closeSession(){
		HibernateSessionFactory.closeSession();
	}
	/**
	 * 按任意条件对查找，可指定排序字段
	 * @param TableName:从哪个表里查询
	 * @param orderby：按照哪个字段排序
	 * @param asc：true 增序 false 减序
	 * @param arr ：条件与值对
	 * @return：返回一个List
	 */
	public  List findByPropertyBySort(String TableName,String orderby,boolean asc,KeyValueWithOperator...arr) {
		try {
			String queryString = "from "+TableName+" as model ";
			if(arr.length>0){
				queryString+="where ";
			}
			int j=1;
			for(KeyValueWithOperator i:arr){
				queryString+="model.";
				queryString+=i.m_keyName;
				if(i.m_value != null){
					queryString+=" "+i.m_operator+" ? ";
				}else{
					queryString+=" "+i.m_operator+" ";
				}
				
				if(j<arr.length){
					queryString+="and ";					
				}						
				j++;
			}
			queryString+=" order by model."+orderby;
			if(asc==true)
				queryString+=" asc";
			else
				queryString+=" desc";
			
			//根据需要添加按主键排序（唯一性，保证分页数据不重复）--默认按主键倒序排列
			String pkName = HibernateSessionFactory.getUniquePkColumnName(TableName);	//唯一主键属性名
			if(pkName != null && !pkName.equalsIgnoreCase(orderby) && !orderby.contains(pkName+".")){	//排序字段orderby不是主键，并且不是主键下的某个属性名称，则添加按主键字段倒序排列
				queryString += ", model."+pkName+" desc ";
			}
			
			Query queryObject = getSession().createQuery(queryString);
			j=0;
			for(KeyValueWithOperator i:arr){
				if(i.m_value != null){
					queryObject.setParameter(j++, i.m_value);
				}				
			}
			return queryObject.list();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	
	/**
	 * 按任意条件对查找，可指定排序字段
	 * @param TableName:从哪个表里查询
	 * @param orderby：按照哪个字段排序
	 * @param asc：true 增序 false 减序
	 * @param arr ：条件与值对
	 * @return：返回一个List
	 */
	public  List findByPropertyBySort(String TableName,String orderby,boolean asc, List<KeyValueWithOperator> arr) {
		try {
			String queryString = "from "+TableName+" as model ";
			if(arr.size()>0){
				queryString+="where ";
			}
			int j=1;
			for(KeyValueWithOperator i:arr){
				queryString+="model.";
				queryString+=i.m_keyName;
				if(i.m_value != null){
					queryString+=" "+i.m_operator+" ? ";
				}else{
					queryString+=" "+i.m_operator+" ";
				}
				
				if(j<arr.size()){
					queryString+="and ";					
				}						
				j++;
			}
			queryString+=" order by model."+orderby;
			if(asc==true)
				queryString+=" asc";
			else
				queryString+=" desc";
			
			//根据需要添加按主键排序（唯一性，保证分页数据不重复）--默认按主键倒序排列
			String pkName = HibernateSessionFactory.getUniquePkColumnName(TableName);	//唯一主键属性名
			if(pkName != null && !pkName.equalsIgnoreCase(orderby) && !orderby.contains(pkName+".")){	//排序字段orderby不是主键，并且不是主键下的某个属性名称，则添加按主键字段倒序排列
				queryString += ", model."+pkName+" desc ";
			}
			
			Query queryObject = getSession().createQuery(queryString);
			j=0;
			for(KeyValueWithOperator i:arr){
				if(i.m_value != null){
					queryObject.setParameter(j++, i.m_value);
				}				
			}
			return queryObject.list();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	
	/**
	 * 根据任意键值对查询
	 * @param TableName:从哪个表里查询
	 * @param arr ：条件与值对
	 */	
	public  List findByVarProperty(String TableName,KeyValue...arr) {
		try {
			String queryString = "from "+TableName+" as model ";
			if(arr.length>0){
				queryString+="where ";
			}
			int j=1;
			for(KeyValue i:arr){
				queryString+="model.";
				queryString+=i.m_keyName;
				queryString+= "= ? ";
				if(j<arr.length){
					queryString+="and ";					
				}						
				j++;
			}
			//queryString+=" order by staffId asc";
			Query queryObject =getSession().createQuery(queryString);
			j=0;
			for(KeyValue i:arr){
				queryObject.setParameter(j++, i.m_value);
			}
			return queryObject.list();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	
	/**
	 * 根据任意键值对查询
	 * @param TableName:从哪个表里查询
	 * @param arr ：条件与值对
	 */	
	public  List findByVarProperty(String TableName,KeyValueWithOperator...arr) {
		try {
			String queryString = "from "+TableName+" as model ";
			if(arr.length > 0){
				queryString+="where ";
			}
			int j=1;
			for(KeyValueWithOperator i:arr){
				queryString+="model.";
				queryString+=i.m_keyName;
				if(i.m_value != null){
					queryString+=" "+i.m_operator+" ? ";
				}else{
					queryString+=" "+i.m_operator+" ";
				}
				if(j<arr.length){
					queryString+="and ";					
				}						
				j++;
			}
			//queryString+=" order by staffId asc";
			Query queryObject =getSession().createQuery(queryString);
			j=0;
			for(KeyValueWithOperator i:arr){
				if(i.m_value != null){
					queryObject.setParameter(j++, i.m_value);
				}
			}
			return queryObject.list();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	
	/**
	* 更新用户对象:arr为更新条件的(键-值)对数组，list_KeyValue为待更新的（字段-值）的集合(至少得更新一个字段，否则会产生异常)
	* @param transientInstance 被更新的对象
	* @return 更新操作影响的记录数
	*/
	public int update(String TableName,List<KeyValue> list_KeyValue ,KeyValue...arr) {
		try {
			String queryString = "update "+TableName+" set ";
			
			//更新各字段的内容
			for(int k=0;k<list_KeyValue.size();k++){
				queryString+=list_KeyValue.get(k).m_keyName;
				queryString+="= ? ";
				if(k!=list_KeyValue.size()-1){
					queryString+=", ";
				}
			}			
			//增加条件语句
			if(arr.length>0){
				queryString+=" where ";
			}
			int j=1;
			for(KeyValue i:arr){

				queryString+=i.m_keyName;
				queryString+= "= ? ";
				if(j<arr.length){
					queryString+="and ";					
				}						
				j++;
			}
			Query queryObject = getSession().createQuery(queryString);			
			//填写实际的值
			j=0;
			for(;j<list_KeyValue.size();j++){
				queryObject.setParameter(j, list_KeyValue.get(j).m_value);
			}
			for(KeyValue i:arr){
				queryObject.setParameter(j++, i.m_value);
			}
			int ret=queryObject.executeUpdate();
			return ret;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	/**
	 * 得到记录总数
	 * @param tableName:对应的数据库表名
	 * @param arr:查询条件的(键-值)对数组
	 * @return
	 */
	public int getTotalCount(String tableName,KeyValue...arr) {
		try{
			String queryString = "select count(*) from "+tableName+" ";
			if(arr.length>0){
				queryString+=" as model where ";
			}
			int j=1;
			for(KeyValue i:arr){
				queryString+="model.";
				queryString+=i.m_keyName;
				queryString+= "= ? ";
				if(j<arr.length){
					queryString+="and ";					
				}						
				j++;
			}
			Query queryObject = getSession().createQuery(queryString);
			j=0;
			for(KeyValue i:arr){
				queryObject.setParameter(j++, i.m_value);
			}
			
			List cc = queryObject.list();
			Long a = (Long) cc.get(0);
			return a.intValue();
		}catch(RuntimeException re){
			throw re;
		}
	}
	
	/**
	 * 获取符合条件的记录个数
	 * @param tableName
	 * @param 变参arr：查询的条件（键-值-算符）
	 * @return 符合条件的记录个数
	 */
	public int getTotalCount(String tableName,KeyValueWithOperator...arr) {		
		try{
			String queryString = "select count(*) from "+tableName+" as model ";
			if(arr.length>0){
				queryString+="where ";
			}
			int j=1;
			for(KeyValueWithOperator i:arr){
				queryString+="model.";
				queryString+=i.m_keyName;
				if(i.m_value != null){
					queryString+=" "+i.m_operator+" ? ";
				}else{
					queryString+=" "+i.m_operator+" ";
				}
				if(j<arr.length){
					queryString+="and ";					
				}						
				j++;
			}
			Query queryObject = getSession().createQuery(queryString);
			j=0;
			for(KeyValue i:arr){
				if(i.m_value != null){
					queryObject.setParameter(j++, i.m_value);
				}
			}
			
			List cc = queryObject.list();
			Long a = (Long) cc.get(0);
			return a.intValue();
		}catch(RuntimeException re){
			throw re;
		}
	}
	/**
	 * 获取符合条件的记录个数
	 * @param tableName
	 * @param arr：查询的条件（键-值-算符)列表
	 * @return 符合条件的记录个数
	 */
	public int getTotalCount(String tableName,List<KeyValueWithOperator> arr) {		
		try{
			String queryString = "select count(*) from "+tableName+" as model ";
			if(arr.size()>0){
				queryString+="where ";
			}
			for(int i=0;i<arr.size();i++){
				KeyValueWithOperator k=arr.get(i);
				queryString+="model.";
				queryString+=k.m_keyName;
				if(k.m_value != null){
					queryString+=" "+k.m_operator+" ? ";
				}else{
					queryString+=" "+k.m_operator+" ";
				}
				if(i<arr.size()-1){
					queryString+="and ";					
				}						
			}
			Query queryObject = getSession().createQuery(queryString);
			int j=0;
			for(KeyValueWithOperator i:arr){
				if(i.m_value != null){
					queryObject.setParameter(j++, i.m_value);
				}
			}
			
			List cc = queryObject.list();
			Long a = (Long) cc.get(0);
			return a.intValue();
		}catch(RuntimeException re){
			throw re;
		}
	}
	
	
	/**
	 * 按任意条件对查找，可按指定字段分组
	 * @param TableName：从哪个表里查询
	 * @param groupby：按照哪个字段分组
	 * @param arr:条件与值对
	 * @return 返回一个List
	 */
	public List findByPropertyByGroup(String TableName,String groupby,KeyValue...arr) {
		try {
			String queryString = "from "+TableName+" as model ";
			if(arr.length>0){
				queryString+="where ";
			}
			int j=1;
			for(KeyValue i:arr){
				queryString+="model.";
				queryString+=i.m_keyName;
				queryString+= "= ? ";
				if(j<arr.length){
					queryString+="and ";					
				}						
				j++;
			}
			queryString+=" group by model."+groupby;
			Query queryObject = getSession().createQuery(queryString);
			j=0;
			for(KeyValue i:arr){
				queryObject.setParameter(j++, i.m_value);
			}
			return queryObject.list();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	
	
	
	
	/**
	 * 根据条件查询记录
	 * @param tableName
	 * @param condList:条件列表
	 * @return
	 */
	public List findByVarProperty(String tableName,List<KeyValueWithOperator> condList) {		
		try{
			String queryString = "from "+tableName+" as model ";
			if(condList.size()>0){
				queryString+="where ";
			}
			for(int i=0;i<condList.size();i++){
				KeyValueWithOperator k=condList.get(i);
				queryString+="model.";
				queryString+=k.m_keyName;
				if(k.m_value != null){
					queryString+=" "+k.m_operator+" ? ";
				}else{
					queryString+=" "+k.m_operator+" ";
				}
				if(i<condList.size()-1){
					queryString+=" and ";					
				}						
			}
			Query queryObject = getSession().createQuery(queryString);
			int j=0;
			for(KeyValueWithOperator i:condList){
				if(i.m_value != null){
					queryObject.setParameter(j++, i.m_value);
				}
			}		
			return queryObject.list();
		}catch (RuntimeException re) {
			throw re;
		}
	}
	
	/**
	* 分页显示数据
	*@param tableName:对应数据库的表格
	* @param currentPage
	* 当前页码, 从 1 开始
	* @param pageSize
	* 每页显示数据量
	* @param arr:为查询条件的(键-值)对数组
	* @return 分页后的数据列表- List<Student>
	*/
	public List findPagedAll(String tableName,int currentPage, int pageSize,KeyValueWithOperator...arr){
		try {
			if (currentPage == 0) {
				currentPage = 1;
			}
			String queryString = "from "+tableName+" ";
			if(arr.length>0){ 
				queryString+=" as model where ";
			}
			int j=1;
			for(KeyValueWithOperator i:arr){
				queryString+=" model.";
				queryString+=i.m_keyName;
				if(i.m_value != null){
					queryString+=" "+i.m_operator+" ? ";
				}else{
					queryString+=" "+i.m_operator+" ";
				}
				if(j<arr.length){
					queryString+="and ";					
				}						
				j++;
			}
			Query queryObject = getSession().createQuery(queryString);
			j=0;
			for(KeyValueWithOperator i:arr){
				if(i.m_value != null){
					queryObject.setParameter(j++, i.m_value);
				}
			}
			
			queryObject.setFirstResult((currentPage - 1) * pageSize);
			queryObject.setMaxResults(pageSize);
			return queryObject.list();
		} catch (RuntimeException re) {
			throw re;
		}
	}	
	
	/**
	 * 分页显示数据
	 *@param arr为查询条件的(键-值)对列表
 	 *@param tableName:对应数据库的表格
	 * @param currentPage
	 * 当前页码, 从 1 开始
	 * @param pageSize
	 * 每页显示数据量
	 * @return 分页后的数据列表- List<Student>
	*/
	public List findPagedAll(String tableName,int currentPage, int pageSize,List<KeyValueWithOperator>arr){
		try {
			if (currentPage == 0) {
				currentPage = 1;
			}
			String queryString = "from "+tableName+" ";
			if(arr.size()>0){ 
				queryString+=" as model where ";
			}
			int j=1;
			for(KeyValueWithOperator i:arr){
				queryString+=" model.";
				queryString+=i.m_keyName;
				if(i.m_value != null){
					queryString+=" "+i.m_operator+" ? ";
				}else{
					queryString+=" "+i.m_operator+" ";
				}
				if(j<arr.size()){
					queryString+="and ";					
				}						
				j++;
			}
			Query queryObject = getSession().createQuery(queryString);
			j=0;
			for(KeyValue i:arr){
				if(i.m_value != null){
					queryObject.setParameter(j++, i.m_value);
				}
			}
			
			queryObject.setFirstResult((currentPage - 1) * pageSize);
			queryObject.setMaxResults(pageSize);
			return queryObject.list();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	/**
	* 分页显示数据
	*@param tableName:对应数据库的表格
	* @param currentPage
	* 当前页码, 从 1 开始
	* @param pageSize
	* 每页显示数据量
	* @param orderby：按照哪个字段排序
	* @param asc：true 增序 false 减序
	* @param arr:为查询条件的(键-值)对数组
	* @return 分页后的数据列表- List<Student>
	*/
	public List findPagedAllBySort(String tableName,int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		try {
			if (currentPage == 0) {
				currentPage = 1;
			}
			String queryString = "from "+tableName+" as model ";
			if(arr.length>0){ 
				queryString+=" where ";
			}
			int j=1;
			for(KeyValueWithOperator i:arr){
				queryString+=" model.";
				queryString+=i.m_keyName;
				if(i.m_value != null){
					queryString+=" "+i.m_operator+" ? ";
				}else{
					queryString+=" "+i.m_operator+" ";
				}
				if(j<arr.length){
					queryString+="and ";					
				}						
				j++;
			}
			queryString+=" order by model."+orderby;
			if(asc==true)
				queryString+=" asc";
			else
				queryString+=" desc";
			
			//根据需要添加按主键排序（唯一性，保证分页数据不重复）--默认按主键倒序排列
			String pkName = HibernateSessionFactory.getUniquePkColumnName(tableName);	//唯一主键属性名
			if(pkName != null && !pkName.equalsIgnoreCase(orderby) && !orderby.contains(pkName+".")){	//排序字段orderby不是主键，并且不是主键下的某个属性名称，则添加按主键字段倒序排列
				queryString += ", model."+pkName+" desc ";
			}
			
			Query queryObject = getSession().createQuery(queryString);
			j=0;
			for(KeyValueWithOperator i:arr){
				if(i.m_value != null){
					queryObject.setParameter(j++, i.m_value);
				}
			}
			
			queryObject.setFirstResult((currentPage - 1) * pageSize);
			queryObject.setMaxResults(pageSize);
			return queryObject.list();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	/**
	* 分页显示数据
	*@param tableName:对应数据库的表格
	* @param currentPage
	* 当前页码, 从 1 开始
	* @param pageSize
	* 每页显示数据量
	* @param orderby：按照哪个字段排序
	* @param asc：true 增序 false 减序
	* @param arr:为查询条件的(键-值)对列表
	* @return 分页后的数据列表- List<Student>
	*/
	public List findPagedAllBySort(String tableName,int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>condList){
		try {
			if (currentPage == 0) {
				currentPage = 1;
			}
			String queryString = "from "+tableName+" as model ";
			if(condList.size()>0){ 
				queryString+=" where ";
			}
			int j=1;
			for(KeyValueWithOperator i:condList){
				queryString+=" model.";
				queryString+=i.m_keyName;
				if(i.m_value != null){
					queryString+=" "+i.m_operator+" ? ";
				}else{
					queryString+=" "+i.m_operator+" ";
				}
				if(j<condList.size()){
					queryString+="and ";					
				}						
				j++;
			}
			queryString+=" order by model."+orderby;
			if(asc==true)
				queryString+=" asc";
			else
				queryString+=" desc";
			
			//根据需要添加按主键排序（唯一性，保证分页数据不重复）--默认按主键倒序排列
			String pkName = HibernateSessionFactory.getUniquePkColumnName(tableName);	//唯一主键属性名
			if(pkName != null && !pkName.equalsIgnoreCase(orderby) && !orderby.contains(pkName+".")){	//排序字段orderby不是主键，并且不是主键下的某个属性名称，则添加按主键字段倒序排列
				queryString += ", model."+pkName+" desc ";
			}
			
			Query queryObject = getSession().createQuery(queryString);
			j=0;
			for(KeyValueWithOperator i:condList){
				if(i.m_value != null){
					queryObject.setParameter(j++, i.m_value);
				}
			}	
			
			queryObject.setFirstResult((currentPage - 1) * pageSize);
			queryObject.setMaxResults(pageSize);
			
			return queryObject.list();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	/**
	 * 更新一个实例
	 * @param transientInstance
	 */
	public void update(Object transientInstance) {
		try {
			getSession().update(transientInstance);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	/**
	 * 根据条件列表删除记录
	 * @param tableName
	 * @param condList
	 * @return
	 */
	public int delete(String tableName,List<KeyValueWithOperator> condList){
		try{
			String queryString = "delete "+tableName+" ";
			if(condList.size()>0){
				queryString+="where ";
			}
			for(int i=0;i<condList.size();i++){
				KeyValueWithOperator k=condList.get(i);
				queryString+=k.m_keyName;
				if(k.m_value != null){
					queryString+=" "+k.m_operator+" ? ";
				}else{
					queryString+=" "+k.m_operator+" ";
				}
				if(i<condList.size()-1){
					queryString+=" and ";					
				}						
			}
			Query queryObject = getSession().createQuery(queryString);
			int j=0;
			for(KeyValueWithOperator i:condList){
				if(i.m_value != null){
					queryObject.setParameter(j++, i.m_value);
				}
			}		
			return queryObject.executeUpdate();
		}catch (RuntimeException re) {
			throw re;
		}
	}
	
	/**
	 * 根据条件删除记录
	 * @param tableName
	 * @param arr
	 * @return
	 */
	public int delete(String tableName,KeyValue...arr){
		try{
			String queryString = "delete "+tableName+" ";
			if(arr.length>0){
				queryString+="where ";
			}
			int j=1;
			for(KeyValue i:arr){
				queryString+=i.m_keyName;
				queryString+= "= ? ";
				if(j<arr.length){
					queryString+="and ";					
				}						
				j++;
			}
			Query queryObject = getSession().createQuery(queryString);
			j=0;
			for(KeyValue i:arr){
				queryObject.setParameter(j++, i.m_value);
			}	
			return queryObject.executeUpdate();
		}catch (RuntimeException re) {
			throw re;
		}
	}
	
	/**
	 * 根据HQL语句查询
	 * @param queryString 查询语句（HQL）
	 * @param arr 查询语句中?对应的值
	 * @return
	 */
	public List findByHQL(String queryString, Object...arr){
		try{
			Query queryObject = getSession().createQuery(queryString);
			int j=0;
			for(Object i:arr){
				queryObject.setParameter(j++, i);
			}
			return queryObject.list();
		}catch(RuntimeException re){
			throw re;
		}
	}
	/**
	 * 根据HQL语句查询
	 * @param queryString 查询语句（HQL）
	 * @param arr 查询语句中?对应的值
	 * @return
	 */
	public List findByHQL(String queryString, List<Object> arr){
		try{
			Query queryObject = getSession().createQuery(queryString);
			int j=0;
			for(Object i:arr){
				queryObject.setParameter(j++, i);
			}
			return queryObject.list();
		}catch(RuntimeException re){
			throw re;
		}
	}
	
	/**
	 * 根据SQL语句查询
	 * @param queryString 查询语句（HQL）
	 * @param arr 查询语句中?对应的值
	 * @return
	 */
	public List findBySQL(String queryString, Object...arr){
		try{
			Query queryObject = getSession().createSQLQuery(queryString);
			int j=0;
			for(Object i:arr){
				queryObject.setParameter(j++, i);
			}
			return queryObject.list();
		}catch(RuntimeException re){
			throw re;
		}
	}
	
	/**
	 * 根据SQL语句查询
	 * @param queryString 查询语句（HQL）
	 * @param arr 查询语句中?对应的值
	 * @return
	 */
	public List findBySQL(String queryString, List<Object> arr){
		try{
			Query queryObject = getSession().createSQLQuery(queryString);
			int j=0;
			for(Object i:arr){
				queryObject.setParameter(j++, i);
			}
			return queryObject.list();
		}catch(RuntimeException re){
			throw re;
		}
	}
	/**
	* 分页显示数据
	*@param queryString:查询语句（HQL）
	* @param currentPage
	* 当前页码, 从 1 开始
	* @param pageSize
	* 每页显示数据量
	* @param arr 查询语句中?对应的值
	* @return 分页后的数据列表- List
	*/
	public List findPageAllByHQL(String queryString, int currentPage, int pageSize, Object...arr){
		try{
			Query queryObject = getSession().createQuery(queryString);
			int j=0;
			for(Object i:arr){
				queryObject.setParameter(j++, i);
			}
			queryObject.setFirstResult((currentPage - 1) * pageSize);
			queryObject.setMaxResults(pageSize);
			return queryObject.list();
		}catch(RuntimeException re){
			throw re;
		}
	}
	
	
	/**
	* 分页显示数据
	*@param queryString:查询语句（HQL）
	* @param currentPage
	* 当前页码, 从 1 开始
	* @param pageSize
	* 每页显示数据量
	* @param arr 查询语句中?对应的值
	* @return 分页后的数据列表- List
	*/
	public List findPageAllByHQL(String queryString, int currentPage, int pageSize, List<Object> arr){
		try{
			Query queryObject = getSession().createQuery(queryString);
			int j=0;
			for(Object i:arr){
				queryObject.setParameter(j++, i);
			}
			queryObject.setFirstResult((currentPage - 1) * pageSize);
			queryObject.setMaxResults(pageSize);
			return queryObject.list();
		}catch(RuntimeException re){
			throw re;
		}
	}
	
	
	public List findPageAllByHQLWithAnyIterm(String queryString, int start, int end, List<Object> arr){
		try{
			Query queryObject = getSession().createQuery(queryString);
			int j=0;
			for(Object i:arr){
				queryObject.setParameter(j++, i);
			}
			queryObject.setFirstResult(start-1);
			queryObject.setMaxResults(end);
			return queryObject.list();
		}catch(RuntimeException re){
			throw re;
		}
	}
	
	/**
	 * 得到记录总数
	 * @param queryString 查询语句（HQL）
	 * @param arr 查询语句中?对应的值
	 * @return
	 */
	public int getTotalCountByHQL(String queryString,Object...arr) {
		try{
			Query queryObject = getSession().createQuery(queryString);
			int j=0;
			for(Object i:arr){
				queryObject.setParameter(j++, i);
			}
			List cc = queryObject.list();
			Long a = (Long) cc.get(0);
			return a.intValue();
		}catch(RuntimeException re){
			throw re;
		}
	}
	
	
	/**
	 * 得到记录总数
	 * @param queryString 查询语句（HQL）
	 * @param arr 查询语句中?对应的值
	 * @return
	 */
	public int getTotalCountByHQL(String queryString,List<Object> arr) {
		try{
			Query queryObject = getSession().createQuery(queryString);
			int j=0;
			for(Object i:arr){
				queryObject.setParameter(j++, i);
			}
			List cc = queryObject.list();
			Long a = (Long) cc.get(0);
			return a.intValue();
		}catch(RuntimeException re){
			throw re;
		}
	}
	/////////////////传进来一查询语句，返回结果数
	public int getTotalCountByHQL1(String queryString,List<Object> arr) {
		try{
			Query queryObject = getSession().createQuery(queryString);
			int j=0;
			for(Object i:arr){
				queryObject.setParameter(j++, i);
			}
			List cc = queryObject.list();
			
			return cc.size();
		}catch(RuntimeException re){
			throw re;
		}
	}
	
	/**
	* 分页显示数据
	*@param queryString:查询语句（SQL）
	* @param currentPage
	* 当前页码, 从 1 开始
	* @param pageSize
	* 每页显示数据量
	* @param arr 查询语句中?对应的值
	* @return 分页后的数据列表- List
	*/
	public List findPageAllBySQL(String queryString, int currentPage, int pageSize, Object...arr){
		try{
			SQLQuery queryObject = getSession().createSQLQuery(queryString);
			int j=0;
			for(Object i:arr){
				queryObject.setParameter(j++, i);
			}
			queryObject.setFirstResult((currentPage - 1) * pageSize);
			queryObject.setMaxResults(pageSize);
			return queryObject.list();
		}catch(RuntimeException re){
			throw re;
		}
	}
	/**
	* 分页显示数据
	*@param queryString:查询语句（SQL）
	* @param currentPage
	* 当前页码, 从 1 开始
	* @param pageSize
	* 每页显示数据量
	* @param arr 查询语句中?对应的值
	* @return 分页后的数据列表- List
	*/
	public List findPageAllBySQL(String queryString, int currentPage, int pageSize, List<Object>arr){
		try{
			SQLQuery queryObject = getSession().createSQLQuery(queryString);
			int j=0;
			for(Object i:arr){
				queryObject.setParameter(j++, i);
			}
			queryObject.setFirstResult((currentPage - 1) * pageSize);
			queryObject.setMaxResults(pageSize);
			return queryObject.list();
		}catch(RuntimeException re){
			throw re;
		}
	}
	/**
	 * 得到记录总数
	 * @param queryString 查询语句（HQL）
	 * @param arr 查询语句中?对应的值
	 * @return
	 */
	public int getTotalCountBySQL(String queryString,Object...arr) {
		try{
			SQLQuery queryObject = getSession().createSQLQuery(queryString);
			int j=0;
			for(Object i:arr){
				queryObject.setParameter(j++, i);
			}
			List cc = queryObject.list();
			Integer a = (Integer) cc.get(0);
			return a.intValue();
		}catch(RuntimeException re){
			throw re;
		}
	}
	/**
	 * 得到记录总数
	 * @param queryString 查询语句（SQL）
	 * @param arr 查询语句中?对应的值
	 * @return
	 */
	public int getTotalCountBySQL(String queryString,List<Object>arr) {
		try{
			SQLQuery queryObject = getSession().createSQLQuery(queryString);
			int j=0;
			for(Object i:arr){
				queryObject.setParameter(j++, i);
			}
			List cc = queryObject.list();
			Integer a = (Integer) cc.get(0);
			return a.intValue();
		}catch(RuntimeException re){
			throw re;
		}
	}
	
	/**
	* 根据HQL更新
	* @param updateString HQL语句（update 表名 set 字段=值 where 条件）
	* @param arr 参数
	* @return 更新操作影响的记录数
	*/
	public int updateByHQL(String updateString, Object...arr) {
		try {
			Query queryObject = getSession().createQuery(updateString);			
			//填写实际的值
			int j=0;
			for(Object o : arr){
				queryObject.setParameter(j++, o);
			}
			int ret=queryObject.executeUpdate();
			return ret;
		} catch (RuntimeException re) {
			throw re;
		}
	}

}