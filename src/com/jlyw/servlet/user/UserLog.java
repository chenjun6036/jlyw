package com.jlyw.servlet.user;

import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.jlyw.util.DateTimeFormatUtil;
import com.jlyw.util.SystemCfgUtil;
import com.jlyw.util.xmlHandler.WriteXml;



/**
 * 用户登入登出检测
 * @author Zhan
 * 2011-7-20
 */
public class UserLog {
	private static final String XmlRootElementName = "Login-Info";	//根标签名称
	private static final String XmlNodeElementName = "User";	//节点标签名称
	private static final String XmlNodeAttrNameId = "Id";	//xml节点属性名字-Id
	private static final String XmlNodeAttrNameLastLoginTime = "LastLoginTime";//xml节点属性名字
	private static final String XmlNodeAttrNameLastLoginIp = "LastLoginIp";//xml节点属性名字
	class LogInfo{
		private long loginTime;	//登录时间毫秒数
		private long lastActiveTime;	//上次心跳（激活）时间的毫秒数
		private String loginIp;	//登录的IP
		private int isOnline;	//是否在线：0不在线，1在线
		public LogInfo(long loginTime, String ip){
			this(loginTime, ip, 1);
		}
		public LogInfo(long loginTime, String ip, int isOnline){
			this.loginTime = loginTime;
			this.lastActiveTime = loginTime;
			if(ip == null){
				this.loginIp = "";
			}else{
				this.loginIp = ip;
			}
			this.isOnline = isOnline;
		}
		
		public long getLoginTime(){
			return this.loginTime;
		}
		public void setLastActiveTime(long time){
			this.lastActiveTime = time;
		}
		public long getLastActiveTime(){
			return this.lastActiveTime;
		}

		public String getLoginIp() {
			return loginIp;
		}

		public int getIsOnline() {
			return isOnline;
		}

		public void setIsOnline(int isOnline) {
			this.isOnline = isOnline;
		}
		
	}
	
	private static UserLog Instance = null;
	public static UserLog getInstance(){	//获取唯一实例
		if(Instance == null){
			Instance = new UserLog();
		}
		return Instance;
	}
	
	private Map<Integer,LogInfo> loginInfoMap;	//存储已登录用户的登录信息	
	private UserLog(){
		loginInfoMap = new HashMap<Integer, LogInfo>();
	}
	
	/**
	 * 用户登录，如果已登录或登录失败，则返回false，登录成功返回true
	 * @param userid
	 * @return
	 */
	public synchronized boolean UserLogin(Integer userid, String ip, HttpServletRequest request){
		if(userid == null){
			return false;
		}
		if(loginInfoMap.containsKey(userid)){
			if(loginInfoMap.get(userid).getIsOnline() == 1){	//用户在线
				long timePeriod = System.currentTimeMillis() - loginInfoMap.get(userid).getLastActiveTime();	//上次心跳距离现在的毫秒数
				if(timePeriod <= SystemCfgUtil.getSecondLoginPeriod() * 60 * 1000){	//不超过允许的二次登陆时间间隔，说明用户已登录
					return false;
				}
			}
			
			request.getSession(true).setAttribute("LastLoginIp", loginInfoMap.get(userid).getLoginIp());
			request.getSession(true).setAttribute("LastLoginTime", DateTimeFormatUtil.DateTimeFormat.format(new Date(loginInfoMap.get(userid).getLoginTime())));
		}else{
			request.getSession(true).setAttribute("LastLoginIp", "");
			request.getSession(true).setAttribute("LastLoginTime", "");
		}
		request.getSession(true).setAttribute("LoginIp", ip==null?"":ip);
		request.getSession(true).setAttribute("LoginTime", DateTimeFormatUtil.DateTimeFormat.format(new Date(System.currentTimeMillis())));
		LogInfo l = new LogInfo(System.currentTimeMillis(), ip);
		loginInfoMap.put(userid, l);
		return true;
	}
	
	/**
	 * 用户退出登录
	 * @param userid
	 * @return
	 */
	public synchronized boolean UserLogout(Integer userid){
		if(loginInfoMap.containsKey(userid)){
			loginInfoMap.get(userid).setIsOnline(0);	//不在线；
		}
		return true;
	}
	
	/**
	 * 更新用户最后心跳时间
	 * @param userid
	 * @return
	 */
	public synchronized boolean setUserActive(Integer userid){
		if(loginInfoMap.containsKey(userid)){
			LogInfo l = loginInfoMap.get(userid);
			l.setLastActiveTime(System.currentTimeMillis());
		}
		return true;

	}
	
	/**
	 * 将登录信息写入文件
	 * @param filePath
	 */
	public void writeToFile(String filePath){
		WriteXml writer = new WriteXml(filePath, XmlRootElementName);
		HashMap<String, String> attrMap = new HashMap<String, String>();
		Iterator<Entry<Integer, LogInfo>> iterator = loginInfoMap.entrySet().iterator();
		while(iterator.hasNext()){
			try{
				Entry<Integer, LogInfo> e = iterator.next();
				attrMap.put(XmlNodeAttrNameId, e.getKey().toString());
				attrMap.put(XmlNodeAttrNameLastLoginIp, e.getValue().getLoginIp());
				attrMap.put(XmlNodeAttrNameLastLoginTime, String.format("%d", e.getValue().getLoginTime()));
				writer.writeElement(attrMap, XmlNodeElementName);
			}catch(Exception e){}
		}
		writer.endDocument();
	}
	
	/**
	 * 从文件中读取登录信息文件
	 * @param filePath
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void readFromFile(URL filePath) throws Exception{
		Element rootElement = new SAXBuilder().build(filePath).getRootElement();
		List<Element> eList = rootElement.getChildren(XmlNodeElementName);
		Iterator<Element> it = eList.iterator();
		Element tmpElement = null;
		while (it.hasNext()) {
			tmpElement = it.next();
			try{
				Integer id = Integer.parseInt(tmpElement.getAttributeValue(XmlNodeAttrNameId));
				String ip = tmpElement.getAttributeValue(XmlNodeAttrNameLastLoginIp);
				long time = Long.parseLong(tmpElement.getAttributeValue(XmlNodeAttrNameLastLoginTime));
				loginInfoMap.put(id, new LogInfo(time, ip, 0));
			}catch(Exception e){}
		}
	}
}
