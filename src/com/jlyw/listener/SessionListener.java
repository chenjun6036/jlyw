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
 * ����ϵͳSession�Ĵ��������٣��û���¼�ǳ������û��ǳ�ʱɾ�������û���Ȩ�޼����б�
 * @author Administrator
 *
 */
public class SessionListener implements HttpSessionListener {

	public void sessionCreated(HttpSessionEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void sessionDestroyed(HttpSessionEvent event) {
		//�û��˳�
		HttpSession session = event.getSession(); 
		SysUser logoutUser = (SysUser)session.getAttribute(SystemCfgUtil.SessionAttrNameLoginUser);
		
		if(logoutUser != null){
			//�ж��Ƿ���session��ʱ�����û��˳����µ�sessionʧЧ
			if(session.getAttribute("FromLogout") != null && (Boolean)session.getAttribute("FromLogout")){
				//���û���UserLog���ѵ�¼�û��б��У��Ƴ�
				UserLog.getInstance().UserLogout(logoutUser.getId());
				
				//��ȡ�ѵ�¼�û���Ȩ���б�Map
				ServletContext context = session.getServletContext();
				Map<Integer, Set<String>> pMap = (Map<Integer, Set<String>>)context.getAttribute(SystemCfgUtil.ContextAttrNameUserPrivilegesMap);
				if(pMap != null){
					if(pMap.containsKey(logoutUser.getId())){
						pMap.remove(logoutUser.getId());	//�����û���Ȩ�޼��ϴ�ServletContext�������Ƴ�
					}
				}
				
			}
			//�Ƴ�����
			session.removeAttribute(SystemCfgUtil.SessionAttrNameLoginUser);
		}
		
		//��session���ó���Ч  
        session.invalidate();
	}

}
