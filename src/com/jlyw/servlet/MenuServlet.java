package com.jlyw.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.me.JSONArray;

import com.jlyw.filter.AuthFilter;
import com.jlyw.hibernate.SysUser;
import com.jlyw.util.MenuUtil;
import com.jlyw.util.SystemCfgUtil;
import com.jlyw.util.UrlInfo;

public class MenuServlet extends HttpServlet {
	private static final Log log = LogFactory.getLog(MenuServlet.class);
	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Integer method = Integer.parseInt(req.getParameter("method"));	//�ж�����ķ�������
		switch (method) {
		case 0: //���ɲ˵���
			JSONArray retJSONArray = null;
			try{
				SysUser loginUser = (SysUser)req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser);
				if(loginUser != null){
					//��ȡ�ѵ�¼�û���Ȩ���б�Map
					ServletContext context = this.getServletContext();
					Map<Integer, Map<Integer, UrlInfo>> pMap = (Map<Integer, Map<Integer, UrlInfo>>)context.getAttribute(SystemCfgUtil.ContextAttrNameUserPrivilegesMap);
					
					retJSONArray = MenuUtil.getInstance().generateTreeMenu(AuthFilter.unProtectedResJsp, AuthFilter.unProtectedResServlet, pMap==null?null:pMap.get(loginUser.getId()), MenuUtil.getInstance().getRootNodeList());
				}
			}catch(Exception e){
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in MenuServlet-->case 0", e);
				}else{
					log.error("error in MenuServlet-->case 0", e);
				}
				retJSONArray = null;
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSONArray==null?"[]":retJSONArray.toString());
			}
			break;
		}
	}

}
