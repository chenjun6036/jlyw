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
     *  �ٹر�Session(�ղ���)�������ر�Session��������һ���������ʱ�رգ�HibernateSessionFilter����ɣ�
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
     * ��������ʹ�ã�
     * ������HibernateSessionFilter�࣬������һ����������ʱ���µ�Session�����������رո�Session
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
	 * ��ʼ��ClassMap--by zhan
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
	 * ��ȡʵ���Ӧ���Ψһ�����ֶ����Ƶ�ʵ��������
	 * 
	 * @param clazz ʵ�������ƣ�������package��
	 * @return �����ֶε���������   û�л��������ֶβ�Ψһ�򷵻�null
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
	 * ��ȡָ�������µ�����
	 * @param classNodeName��ʵ����������ƣ�������package��
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
	 * ��������:Hibernate��һ��JDBC session���ܴ��ڶ������һ�������ܿ�Խ���session��JTA������ԣ�
	 * Ҫ�������������֮ǰ����Manager���update��save�ȷ����������¿������������
	 * @return
	 * @throws HibernateException
	 */
	public static Transaction beginTransaction() throws HibernateException{
		return getSession().beginTransaction();
	}

}