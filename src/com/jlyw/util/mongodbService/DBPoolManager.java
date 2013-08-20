package com.jlyw.util.mongodbService;

import java.net.UnknownHostException;

import com.jlyw.util.SystemCfgUtil;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoOptions;

/**
 * MongoDB数据库连接池
 * 1.Mongo对象
 *	  内部实现了一个连接池。Mongo对象是线程安全的，因此可以只创建一个，在多线程环境下安全使用。因此，我们可以用将Mongo变量作为一个Singleton类的成员变量，从而保证只创建一个连接池。
 *   Mongo.close方法将关闭当前所有活跃的连接。所以要在web工程被从Tomcat或者GlassFish容器中注销的时候确保调用close方法。
 * 2.DB对象
 *   DB对象可以通过Mongo.get方法获得，代表了和数据库的一个连接。默认情况下，当执行完数据库的查询或者更新操作后，连接将自动回到连接池中。不需要我们手动调用代码放回池中。
 *   至于如何实现，我猜测是update,query,save方法内部有finally块，那里面有还连接到池中的代码。
 * example: 
 * 初始化：DBManager.getInstance().init("74.208.78.5",27017,200); 
 * 之后，每次通过下面的代码获取数据库对象 
 * DBManager.getInstance().getDB(); 
 * @version 1.0 
 * @author Administrator
 *
 */
public class DBPoolManager {
	public static final String MongoDBHost = SystemCfgUtil.MongoDBHost;
	public static final int MongoDBPort = SystemCfgUtil.MongoDBPort;
	public static final String DBName = SystemCfgUtil.FileDBName; 
	public static final int MongoDBPoolSize = SystemCfgUtil.MongoDBPoolSize;
  
    private Mongo mongo;  
    
    public static DBPoolManager getInstance(){  
    	return InnerHolder.INSTANCE;  
    }  
  
    /** 
     * Creates a new <code>DBManager</code> instance. 
     * 
     */  
    private DBPoolManager() {
    }  
  
    private static class InnerHolder{  
    	static final DBPoolManager INSTANCE = new DBPoolManager();  
    }  
  
    public DB getDB() throws UnknownHostException{
    	if(mongo == null){
    		init(MongoDBHost, MongoDBPort, MongoDBPoolSize);
    	}
    	return mongo.getDB(DBName);
    }  
  
   
      
  
    private void init(final String ip, int port, int poolSize) throws java.net.UnknownHostException {  
        System.setProperty("MONGO.POOLSIZE", String.valueOf(poolSize));  
        if (mongo == null) {  
            mongo = new Mongo(ip, port);  
            MongoOptions options = mongo.getMongoOptions();  
            options.autoConnectRetry = true;  
            options.connectionsPerHost = poolSize;  
        }  
    }  
}
