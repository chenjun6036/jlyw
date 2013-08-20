package com.jlyw.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.jlyw.util.KeyValue;
import com.jlyw.util.KeyValueWithOperator;


/**
 * Data access object (DAO) for domain model
 * �����ѯ����Ϊ���ֶ�aΪ��/���գ���KeyValueWithOperatorΪ��"key",null,"is null"��/��"key",null,"is not null"��;
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
	 * �����������Բ��ң���ָ�������ֶ�
	 * @param TableName:���ĸ������ѯ
	 * @param orderby�������ĸ��ֶ�����
	 * @param asc��true ���� false ����
	 * @param arr ��������ֵ��
	 * @return������һ��List
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
			
			//������Ҫ��Ӱ���������Ψһ�ԣ���֤��ҳ���ݲ��ظ���--Ĭ�ϰ�������������
			String pkName = HibernateSessionFactory.getUniquePkColumnName(TableName);	//Ψһ����������
			if(pkName != null && !pkName.equalsIgnoreCase(orderby) && !orderby.contains(pkName+".")){	//�����ֶ�orderby�������������Ҳ��������µ�ĳ���������ƣ�����Ӱ������ֶε�������
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
	 * �����������Բ��ң���ָ�������ֶ�
	 * @param TableName:���ĸ������ѯ
	 * @param orderby�������ĸ��ֶ�����
	 * @param asc��true ���� false ����
	 * @param arr ��������ֵ��
	 * @return������һ��List
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
			
			//������Ҫ��Ӱ���������Ψһ�ԣ���֤��ҳ���ݲ��ظ���--Ĭ�ϰ�������������
			String pkName = HibernateSessionFactory.getUniquePkColumnName(TableName);	//Ψһ����������
			if(pkName != null && !pkName.equalsIgnoreCase(orderby) && !orderby.contains(pkName+".")){	//�����ֶ�orderby�������������Ҳ��������µ�ĳ���������ƣ�����Ӱ������ֶε�������
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
	 * ���������ֵ�Բ�ѯ
	 * @param TableName:���ĸ������ѯ
	 * @param arr ��������ֵ��
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
	 * ���������ֵ�Բ�ѯ
	 * @param TableName:���ĸ������ѯ
	 * @param arr ��������ֵ��
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
	* �����û�����:arrΪ����������(��-ֵ)�����飬list_KeyValueΪ�����µģ��ֶ�-ֵ���ļ���(���ٵø���һ���ֶΣ����������쳣)
	* @param transientInstance �����µĶ���
	* @return ���²���Ӱ��ļ�¼��
	*/
	public int update(String TableName,List<KeyValue> list_KeyValue ,KeyValue...arr) {
		try {
			String queryString = "update "+TableName+" set ";
			
			//���¸��ֶε�����
			for(int k=0;k<list_KeyValue.size();k++){
				queryString+=list_KeyValue.get(k).m_keyName;
				queryString+="= ? ";
				if(k!=list_KeyValue.size()-1){
					queryString+=", ";
				}
			}			
			//�����������
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
			//��дʵ�ʵ�ֵ
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
	 * �õ���¼����
	 * @param tableName:��Ӧ�����ݿ����
	 * @param arr:��ѯ������(��-ֵ)������
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
	 * ��ȡ���������ļ�¼����
	 * @param tableName
	 * @param ���arr����ѯ����������-ֵ-�����
	 * @return ���������ļ�¼����
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
	 * ��ȡ���������ļ�¼����
	 * @param tableName
	 * @param arr����ѯ����������-ֵ-���)�б�
	 * @return ���������ļ�¼����
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
	 * �����������Բ��ң��ɰ�ָ���ֶη���
	 * @param TableName�����ĸ������ѯ
	 * @param groupby�������ĸ��ֶη���
	 * @param arr:������ֵ��
	 * @return ����һ��List
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
	 * ����������ѯ��¼
	 * @param tableName
	 * @param condList:�����б�
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
	* ��ҳ��ʾ����
	*@param tableName:��Ӧ���ݿ�ı��
	* @param currentPage
	* ��ǰҳ��, �� 1 ��ʼ
	* @param pageSize
	* ÿҳ��ʾ������
	* @param arr:Ϊ��ѯ������(��-ֵ)������
	* @return ��ҳ��������б�- List<Student>
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
	 * ��ҳ��ʾ����
	 *@param arrΪ��ѯ������(��-ֵ)���б�
 	 *@param tableName:��Ӧ���ݿ�ı��
	 * @param currentPage
	 * ��ǰҳ��, �� 1 ��ʼ
	 * @param pageSize
	 * ÿҳ��ʾ������
	 * @return ��ҳ��������б�- List<Student>
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
	* ��ҳ��ʾ����
	*@param tableName:��Ӧ���ݿ�ı��
	* @param currentPage
	* ��ǰҳ��, �� 1 ��ʼ
	* @param pageSize
	* ÿҳ��ʾ������
	* @param orderby�������ĸ��ֶ�����
	* @param asc��true ���� false ����
	* @param arr:Ϊ��ѯ������(��-ֵ)������
	* @return ��ҳ��������б�- List<Student>
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
			
			//������Ҫ��Ӱ���������Ψһ�ԣ���֤��ҳ���ݲ��ظ���--Ĭ�ϰ�������������
			String pkName = HibernateSessionFactory.getUniquePkColumnName(tableName);	//Ψһ����������
			if(pkName != null && !pkName.equalsIgnoreCase(orderby) && !orderby.contains(pkName+".")){	//�����ֶ�orderby�������������Ҳ��������µ�ĳ���������ƣ�����Ӱ������ֶε�������
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
	* ��ҳ��ʾ����
	*@param tableName:��Ӧ���ݿ�ı��
	* @param currentPage
	* ��ǰҳ��, �� 1 ��ʼ
	* @param pageSize
	* ÿҳ��ʾ������
	* @param orderby�������ĸ��ֶ�����
	* @param asc��true ���� false ����
	* @param arr:Ϊ��ѯ������(��-ֵ)���б�
	* @return ��ҳ��������б�- List<Student>
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
			
			//������Ҫ��Ӱ���������Ψһ�ԣ���֤��ҳ���ݲ��ظ���--Ĭ�ϰ�������������
			String pkName = HibernateSessionFactory.getUniquePkColumnName(tableName);	//Ψһ����������
			if(pkName != null && !pkName.equalsIgnoreCase(orderby) && !orderby.contains(pkName+".")){	//�����ֶ�orderby�������������Ҳ��������µ�ĳ���������ƣ�����Ӱ������ֶε�������
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
	 * ����һ��ʵ��
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
	 * ���������б�ɾ����¼
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
	 * ��������ɾ����¼
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
	 * ����HQL����ѯ
	 * @param queryString ��ѯ��䣨HQL��
	 * @param arr ��ѯ�����?��Ӧ��ֵ
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
	 * ����HQL����ѯ
	 * @param queryString ��ѯ��䣨HQL��
	 * @param arr ��ѯ�����?��Ӧ��ֵ
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
	 * ����SQL����ѯ
	 * @param queryString ��ѯ��䣨HQL��
	 * @param arr ��ѯ�����?��Ӧ��ֵ
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
	 * ����SQL����ѯ
	 * @param queryString ��ѯ��䣨HQL��
	 * @param arr ��ѯ�����?��Ӧ��ֵ
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
	 * �õ���¼����
	 * @param queryString ��ѯ��䣨HQL��
	 * @param arr ��ѯ�����?��Ӧ��ֵ
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
	 * �õ���¼����
	 * @param queryString ��ѯ��䣨HQL��
	 * @param arr ��ѯ�����?��Ӧ��ֵ
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
	/////////////////������һ��ѯ��䣬���ؽ����
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
	* ��ҳ��ʾ����
	*@param queryString:��ѯ��䣨SQL��
	* @param currentPage
	* ��ǰҳ��, �� 1 ��ʼ
	* @param pageSize
	* ÿҳ��ʾ������
	* @param arr ��ѯ�����?��Ӧ��ֵ
	* @return ��ҳ��������б�- List
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
	* ��ҳ��ʾ����
	*@param queryString:��ѯ��䣨SQL��
	* @param currentPage
	* ��ǰҳ��, �� 1 ��ʼ
	* @param pageSize
	* ÿҳ��ʾ������
	* @param arr ��ѯ�����?��Ӧ��ֵ
	* @return ��ҳ��������б�- List
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
	 * �õ���¼����
	 * @param queryString ��ѯ��䣨HQL��
	 * @param arr ��ѯ�����?��Ӧ��ֵ
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
	 * �õ���¼����
	 * @param queryString ��ѯ��䣨SQL��
	 * @param arr ��ѯ�����?��Ӧ��ֵ
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
	* ����HQL����
	* @param updateString HQL��䣨update ���� set �ֶ�=ֵ where ������
	* @param arr ����
	* @return ���²���Ӱ��ļ�¼��
	*/
	public int updateByHQL(String updateString, Object...arr) {
		try {
			Query queryObject = getSession().createQuery(updateString);			
			//��дʵ�ʵ�ֵ
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