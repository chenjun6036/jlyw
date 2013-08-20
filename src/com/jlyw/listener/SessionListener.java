package com.jlyw.listener;

import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.jlyw.hibernate.SysUser;
import com.jlyw.servlet.user.UserLog;
import com.jlyw.util.SystemCfgUtil;

/**
 * 监听系统Session的创建与销毁（用户登录登出），用户登出时删除在线用户的权限集合列表
 * @author Administrator
 *
 */
public class SessionListener implements HttpSessionListener {

	public void sessionCreated(HttpSessionEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void sessionDestroyed(HttpSessionEvent event) {
		//用户退出
		HttpSession session = event.getSession(); 
		SysUser logoutUser = (SysUser)session.getAttribute(SystemCfgUtil.SessionAttrNameLoginUser);
		
		if(logoutUser != null){
			//判断是否是session超时还是用户退出导致的session失效
			if(session.getAttribute("FromLogout") != null && (Boolean)session.getAttribute("FromLogout")){
				//将用户从UserLog（已登录用户列表中）移除
				UserLog.getInstance().UserLogout(logoutUser.getId());
				
				//获取已登录用户的权限列表Map
				ServletContext context = session.getServletContext();
				Map<Integer, Set<String>> pMap = (Map<Integer, Set<String>>)context.getAttribute(SystemCfgUtil.ContextAttrNameUserPrivilegesMap);
				if(pMap != null){
					if(pMap.containsKey(logoutUser.getId())){
						pMap.remove(logoutUser.getId());	//将该用户的权限集合从ServletContext对象中移除
					}
				}
				
			}
			//移除属性
			session.removeAttribute(SystemCfgUtil.SessionAttrNameLoginUser);
		}
		
		//将session设置成无效  
        session.invalidate();
	}

}
