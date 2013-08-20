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
 * �û�����ǳ����
 * @author Zhan
 * 2011-7-20
 */
public class UserLog {
	private static final String XmlRootElementName = "Login-Info";	//����ǩ����
	private static final String XmlNodeElementName = "User";	//�ڵ��ǩ����
	private static final String XmlNodeAttrNameId = "Id";	//xml�ڵ���������-Id
	private static final String XmlNodeAttrNameLastLoginTime = "LastLoginTime";//xml�ڵ���������
	private static final String XmlNodeAttrNameLastLoginIp = "LastLoginIp";//xml�ڵ���������
	class LogInfo{
		private long loginTime;	//��¼ʱ�������
		private long lastActiveTime;	//�ϴ����������ʱ��ĺ�����
		private String loginIp;	//��¼��IP
		private int isOnline;	//�Ƿ����ߣ�0�����ߣ�1����
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
	public static UserLog getInstance(){	//��ȡΨһʵ��
		if(Instance == null){
			Instance = new UserLog();
		}
		return Instance;
	}
	
	private Map<Integer,LogInfo> loginInfoMap;	//�洢�ѵ�¼�û��ĵ�¼��Ϣ	
	private UserLog(){
		loginInfoMap = new HashMap<Integer, LogInfo>();
	}
	
	/**
	 * �û���¼������ѵ�¼���¼ʧ�ܣ��򷵻�false����¼�ɹ�����true
	 * @param userid
	 * @return
	 */
	public synchronized boolean UserLogin(Integer userid, String ip, HttpServletRequest request){
		if(userid == null){
			return false;
		}
		if(loginInfoMap.containsKey(userid)){
			if(loginInfoMap.get(userid).getIsOnline() == 1){	//�û�����
				long timePeriod = System.currentTimeMillis() - loginInfoMap.get(userid).getLastActiveTime();	//�ϴ������������ڵĺ�����
				if(timePeriod <= SystemCfgUtil.getSecondLoginPeriod() * 60 * 1000){	//����������Ķ��ε�½ʱ������˵���û��ѵ�¼
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
	 * �û��˳���¼
	 * @param userid
	 * @return
	 */
	public synchronized boolean UserLogout(Integer userid){
		if(loginInfoMap.containsKey(userid)){
			loginInfoMap.get(userid).setIsOnline(0);	//�����ߣ�
		}
		return true;
	}
	
	/**
	 * �����û��������ʱ��
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
	 * ����¼��Ϣд���ļ�
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
	 * ���ļ��ж�ȡ��¼��Ϣ�ļ�
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
