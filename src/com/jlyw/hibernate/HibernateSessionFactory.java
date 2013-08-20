package com.jlyw.hibernate;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.Table;

/**
 * Configures and provides access to Hibernate sessions, tied to the
 * current thread of execution.  Follows the Thread Local Session
 * pattern, see {@link http://hibernate.org/42.html }.
 */
public class HibernateSessionFactory {

    /** 
     * Location of hibernate.cfg.xml file.
     * Location should be on the classpath as Hibernate uses  
     * #resourceAsStream style lookup for its configuration file. 
     * The default classpath location of the hibernate config file is 
     * in the default package. Use #setConfigFile() to update 
     * the location of the configuration file for the current session.   
     */
    private static String CONFIG_FILE_LOCATION = "/hibernate.cfg.xml";
	private static final ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();
    private  static Configuration configuration = new Configuration();    
    private static org.hibernate.SessionFactory sessionFactory;
    private static String configFile = CONFIG_FILE_LOCATION;

    private static Map<String, PersistentClass> mapClass = null;
	static {
    	try {
			configuration.configure(configFile);
			sessionFactory = configuration.buildSessionFactory();
			
			initClassMap();
		} catch (Exception e) {
			System.err.println("%%%% Error Creating SessionFactory %%%%");
			e.printStackTrace();
		}
    }
    private HibernateSessionFactory() {
    }
	
	/**
     * Returns the ThreadLocal Session instance.  Lazy initialize
     * the <code>SessionFactory</code> if needed.
     *
     *  @return Session
     *  @throws HibernateException
     */
    public static Session getSession() throws HibernateException {
        Session session = (Session) threadLocal.get();

		if (session == null || !session.isOpen() || !session.isConnected()) {
			if (sessionFactory == null) {
				rebuildSessionFactory();
			}
			session = (sessionFactory != null) ? sessionFactory.openSession()
					: null;
			threadLocal.set(session);
		}

        return session;
    }

	/**
     *  Rebuild hibernate session factory
     *
     */
	public static void rebuildSessionFactory() {
		try {
			configuration.configure(configFile);
			sessionFactory = configuration.buildSessionFactory();
		} catch (Exception e) {
			System.err
					.println("%%%% Error Creating SessionFactory %%%%");
			e.printStackTrace();
		}
	}

	/**
     *  Close the single hibernate session instance.
     *  假关闭Session(空操作)，真正关闭Session操作是在一个请求结束时关闭（HibernateSessionFilter中完成）
     *  @throws HibernateException
     */
    public static void closeSession() throws HibernateException {
//        Session session = (Session) threadLocal.get();
//        threadLocal.set(null);
//
//        if (session != null) {
//            session.close();
//        }
    }
    
    /**
     * 其余人勿使用！
     * 仅用于HibernateSessionFilter类，用于在一个请求来临时打开新的Session，请求结束后关闭该Session
     * @throws HibernateException
     */
    public static void closeSessionForFilter() throws HibernateException {
    	Session session = (Session) threadLocal.get();
        threadLocal.set(null);

        if (session != null) {
            session.close();
        }
    }

	/**
     *  return session factory
     *
     */
	public static org.hibernate.SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
     *  return session factory
     *
     *	session factory will be rebuilded in the next call
     */
	public static void setConfigFile(String configFile) {
		HibernateSessionFactory.configFile = configFile;
		sessionFactory = null;
	}

	/**
     *  return hibernate configuration
     *
     */
	public static Configuration getConfiguration() {
		return configuration;
	}
	
	/**
	 * 初始化ClassMap--by zhan
	 */
	private static void initClassMap(){
		mapClass = new HashMap<String, PersistentClass>();
		Iterator<PersistentClass> iter = configuration.getClassMappings();
		while(iter.hasNext()){
			PersistentClass obj = (PersistentClass)iter.next();
			mapClass.put(obj.getNodeName(), obj);
		}
	}
	
	/**
	 * 获取实体对应表的唯一主键字段名称的实体属性名
	 * 
	 * @param clazz 实体类名称（不包含package）
	 * @return 主键字段的属性名称   没有或者主键字段不唯一则返回null
	 */
	public static String getUniquePkColumnName(String classNodeName) {
		if(mapClass == null){
			initClassMap();
		}
		PersistentClass persistentClass = null;
		if(mapClass.containsKey(classNodeName)){
			persistentClass = mapClass.get(classNodeName);
		}
		if(persistentClass==null)
			return null;
		
		Table table = persistentClass.getTable();
		if(table.getPrimaryKey().getColumnSpan() != 1){
			return null;
		}
		return table.getPrimaryKey().getColumn(0).getCanonicalName();
	}
	/**
	 * 获取指定类名下的属性
	 * @param classNodeName：实体类的类名称（不包含package）
	 * @param propertyName
	 * @return
	 */
	public static String getColumnName(String classNodeName, String propertyName){
		if(mapClass == null){
			initClassMap();
		}
		PersistentClass persistentClass = null;
		if(mapClass.containsKey(classNodeName)){
			persistentClass = mapClass.get(classNodeName);
		}
		if(persistentClass==null)
			return null;
		
		Property property = persistentClass.getProperty(propertyName);
		Iterator it = property.getColumnIterator();
		if (it.hasNext()) {
			Column column = (Column) it.next();
			return column.getName();
		}
		return null;
	}
	
	
	/**
	 * 开启事务:Hibernate的一个JDBC session不能存在多个事务，一个事务不能跨越多个session（JTA事务可以）
	 * 要避免在事务结束之前调用Manager类的update或save等方法（即重新开启或结束事务）
	 * @return
	 * @throws HibernateException
	 */
	public static Transaction beginTransaction() throws HibernateException{
		return getSession().beginTransaction();
	}

}