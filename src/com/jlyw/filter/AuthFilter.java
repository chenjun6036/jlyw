package com.jlyw.filter;


import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.jlyw.hibernate.SysUser;
import com.jlyw.manager.RolePrivilegeManager;
import com.jlyw.util.SysStringUtil;
import com.jlyw.util.SystemCfgUtil;
import com.jlyw.util.UrlInfo;
import com.sun.jmx.snmp.Enumerated;

/**
 * 验证用户权限
 * @author Zhan
 *
 */
public class AuthFilter extends HttpServlet implements Filter {
	private static final String ProjectName = SystemCfgUtil.ProjectName;	//项目名称：用于URL区分资源部分
	private static final Log log = LogFactory.getLog(AuthFilter.class);
	public static List<UrlInfo> unProtectedResJsp = null;
	public static List<UrlInfo> unProtectedResServlet = null;
	private static Element rootElement = null;
	
//	private FilterConfig config=null;
	private String unprotectedUrlsFilePath = null;

	public AuthFilter() {}

	public void init(FilterConfig filtercfg)
			throws javax.servlet.ServletException {
//		this.config = filtercfg;
//		this.unprotectedUrlsFilePath = config.getInitParameter("filepath");
		this.unprotectedUrlsFilePath = SystemCfgUtil.UnprotectedUrlsConfigFilePath;
		try {
			getUnprotectedResources();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(String.format("权限认证过滤器初始化失败:%s", e.getMessage()==null?"":e.getMessage()));
		}
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterchain) throws java.io.IOException,
			javax.servlet.ServletException {
		try {
			HttpServletRequest req = (HttpServletRequest) request;
			HttpServletResponse resp = (HttpServletResponse) response;
			
//			System.out.println("in AuthFilter->doFilter(): getRequestURL + queryString: "
//					+ req.getRequestURI().toString() + "?"
//					+ req.getQueryString());
//			System.out.println(req.getHeader("x-requested-with"));
//			Enumeration<String> e = req.getHeaderNames();
//			while(e.hasMoreElements()){
//				String name = e.nextElement();
//				System.out.println(name+"    "+req.getHeader(name));
//			}
//			System.out.println(req.getParameter("form_submit_request_type_byzhan"));//form('submit')方式提交时该字段的值为"form_submit_ajax";
			
			log.debug("in AuthFilter->doFilter(): getRequestURL + queryString: "
					+ req.getRequestURI().toString() + "?"
					+ req.getQueryString());
			boolean isUnprotected = isUnprotectedUrl(req);
			if (isUnprotected) {
				filterchain.doFilter(request, response);
				return;
			}

			
			if (req.getSession().getAttribute("LOGIN_USER") == null) {
				String requestFlag = req.getHeader("x-requested-with");//判断是否是异步提交
				if(requestFlag !=null && requestFlag.equalsIgnoreCase("XMLHttpRequest")){
					JSONObject retObj = new JSONObject();
					retObj.put("IsOK", false);
					retObj.put("msg", "您没有相关权限执行该操作！");
					retObj.put("total", 0);
					retObj.put("rows", new JSONArray());
					resp.setContentType(SysStringUtil.ResponseContentType.Type_AjaxSubmit);
					PrintWriter out = resp.getWriter();
					out.print(retObj.toString());
					out.flush();
					out.close();
				}else{//一般http请求:包括form('submit')方式（不跳转页面）和直接的form提交（页面跳转）等
					if(req.getParameter("form_submit_request_type_byzhan") != null && req.getParameter("form_submit_request_type_byzhan").equals("form_submit_ajax")){ //通过form('submit')方式提交
						JSONObject retObj = new JSONObject();
						retObj.put("IsOK", false);
						retObj.put("msg", "您没有相关权限执行该操作！");
						retObj.put("total", 0);
						retObj.put("rows", new JSONArray());
						resp.setContentType(SysStringUtil.ResponseContentType.Type_FormSubmit);
						PrintWriter out = resp.getWriter();
						out.print(retObj.toString());
						out.flush();
						out.close();
					}else{
						resp.sendRedirect(req.getContextPath()+"/noPrivilege.htm");
					}
				}
				return;
			}
			
			//受保护的资源：用户必须已登录
			SysUser loginUser = (SysUser) req.getSession().getAttribute("LOGIN_USER");
			//获取已登录用户的权限列表Map
			ServletContext context = req.getSession().getServletContext();
			Map<Integer, Map<Integer, UrlInfo>> pMap = (Map<Integer, Map<Integer, UrlInfo>>)context.getAttribute(SystemCfgUtil.ContextAttrNameUserPrivilegesMap);
			if(pMap == null || loginUser == null){
				//提示
				String requestFlag = req.getHeader("x-requested-with");//判断是否是异步提交
				if(requestFlag !=null && requestFlag.equalsIgnoreCase("XMLHttpRequest")){
					JSONObject retObj = new JSONObject();
					retObj.put("IsOK", false);
					retObj.put("msg", "您没有相关权限执行该操作！");
					retObj.put("total", 0);
					retObj.put("rows", new JSONArray());
					resp.setContentType(SysStringUtil.ResponseContentType.Type_AjaxSubmit);
					PrintWriter out = resp.getWriter();
					out.print(retObj.toString());
					out.flush();
					out.close();
				}else{//一般http请求:包括form('submit')方式（不跳转页面）和直接的form提交（页面跳转）等
					if(req.getParameter("form_submit_request_type_byzhan") != null && req.getParameter("form_submit_request_type_byzhan").equals("form_submit_ajax")){ //通过form('submit')方式提交
						JSONObject retObj = new JSONObject();
						retObj.put("IsOK", false);
						retObj.put("msg", "您没有相关权限执行该操作！");
						retObj.put("total", 0);
						retObj.put("rows", new JSONArray());
						resp.setContentType(SysStringUtil.ResponseContentType.Type_FormSubmit);
						PrintWriter out = resp.getWriter();
						out.print(retObj.toString());
						out.flush();
						out.close();
					}else{
						resp.sendRedirect(req.getContextPath()+"/noPrivilege.htm");
					}
				}
				return;
			}
			if(!pMap.containsKey(loginUser.getId())){
				Map<Integer, UrlInfo> pRetMap = new RolePrivilegeManager().getPrivilegesByUserId(loginUser.getId());
				if(pRetMap != null){
					pMap.put(loginUser.getId(), pRetMap);
				}
			}
			if (!checkSafe(req, pMap.get(loginUser.getId()))) {	//用户不被禁止，可以执行该操作
				filterchain.doFilter(request, response);
				return;
			} else {
				//提示
				String requestFlag = req.getHeader("x-requested-with");//判断是否是异步提交
				if(requestFlag !=null && requestFlag.equalsIgnoreCase("XMLHttpRequest")){
					JSONObject retObj = new JSONObject();
					retObj.put("IsOK", false);
					retObj.put("msg", "您没有相关权限执行该操作！");
					retObj.put("total", 0);
					retObj.put("rows", new JSONArray());
					resp.setContentType(SysStringUtil.ResponseContentType.Type_AjaxSubmit);
					PrintWriter out = resp.getWriter();
					out.print(retObj.toString());
					out.flush();
					out.close();
				}else{//一般http请求:包括form('submit')方式（不跳转页面）和直接的form提交（页面跳转）等
					if(req.getParameter("form_submit_request_type_byzhan") != null && req.getParameter("form_submit_request_type_byzhan").equals("form_submit_ajax")){ //通过form('submit')方式提交
						JSONObject retObj = new JSONObject();
						retObj.put("IsOK", false);
						retObj.put("msg", "您没有相关权限执行该操作！");
						retObj.put("total", 0);
						retObj.put("rows", new JSONArray());
						resp.setContentType(SysStringUtil.ResponseContentType.Type_FormSubmit);
						PrintWriter out = resp.getWriter();
						out.print(retObj.toString());
						out.flush();
						out.close();
					}else{
						resp.sendRedirect(req.getContextPath()+"/noPrivilege.htm");
					}
				}
			}

		} catch (Exception e) {
			log.error("error in authorization filter", e);
		}
	}

	/**
	 * 判断一个连接是否为非保护资源
	 * @param request
	 * @return 该链接为非保护资源，返回true；否则，返回false
	 */
	private boolean isUnprotectedUrl(HttpServletRequest request) {
		String url = request.getRequestURI().toString();
		int index = url.indexOf(ProjectName);
		if (index > -1) {
			url = url.substring(index + ProjectName.length());
		}
		String queryString = request.getQueryString();
		
		List<UrlInfo> unProtectedRes = null;
		if(url.endsWith(".do")){
			unProtectedRes = unProtectedResServlet;
		}else{
			unProtectedRes = unProtectedResJsp;
		}
		for(UrlInfo unProtUrl : unProtectedRes) {
			if(unProtUrl.isMatchUrl(url, queryString)){	//是否匹配
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断一个用户是否 不拥有（禁止操作） 某个资源的权限
	 * @param request：用户请求的资源
	 * @param pSet :用户拥有的权限Url(格式：URI或者URI?参数)
	 * @return 用户 不拥有（禁止操作） 该资源的权限则返回true，否则，返回false
	 */
	private boolean checkSafe(HttpServletRequest request, Map<Integer, UrlInfo> pUrlMap) {
		if(pUrlMap == null){
			return false;
		}
		String uri = request.getRequestURI().toString();
		int index = uri.indexOf(ProjectName);
		if (index > -1) {
			uri = uri.substring(index + ProjectName.length());
		}
		String queryString = request.getQueryString();
		
		Iterator<Entry<Integer, UrlInfo>> iter = pUrlMap.entrySet().iterator();
		while(iter.hasNext()){
			Entry<Integer, UrlInfo> e = iter.next();
			if(e.getValue().isMatchUrl(uri, queryString)){	//是否拥有权限
				return true;
			}
		}
		return false;
	}

	public void destroy() {
//		this.config = null;
		this.unprotectedUrlsFilePath = null;
	}

	/**
	 * 载入配置文件
	 * @param cfgFilePath 配置文件相对路径
	 */
	private void load(String cfgFilePath) throws Exception {
		URL url = AuthFilter.class.getClassLoader().getResource(cfgFilePath);
		rootElement = new SAXBuilder().build(url).getRootElement();
	}

	/**
	 * 获得配置文件中指定名称的Element
	 * @param elementName
	 * @return
	 */
	private Element getElement(String elementName) throws Exception {
		return rootElement.getChild(elementName);
	}

	/**
	 * 获取不受访问限制的资源信息列表；
	 */
	private void getUnprotectedResources() throws Exception {
		if(rootElement == null){
			load(this.unprotectedUrlsFilePath);
		}
		if (unProtectedResJsp == null) {
			unProtectedResJsp = new ArrayList<UrlInfo>();
			Element interceptors = getElement("unprotectedurls-jsp");
			List<Element> urlList = interceptors.getChildren("unprotectedurl");
			Iterator<Element> it = urlList.iterator();
			Element tmpElement = null;
			while (it.hasNext()) {
				tmpElement = it.next();
				String url = tmpElement.getAttributeValue("url");
				if(url != null){
					unProtectedResJsp.add(new UrlInfo(url));
				}
				
			}
		}
		if (unProtectedResServlet == null) {
			unProtectedResServlet = new ArrayList<UrlInfo>();
			Element interceptors = getElement("unprotectedurls-servlet");
			List<Element> urlList = interceptors.getChildren("unprotectedurl");
			Iterator<Element> it = urlList.iterator();
			Element tmpElement = null;
			while (it.hasNext()) {
				tmpElement = it.next();
				String url = tmpElement.getAttributeValue("url");
				if(url != null){
					unProtectedResServlet.add(new UrlInfo(url));
				}
			}
		}
	}

	public static void main(String[] args) {

		AuthFilter tools = new AuthFilter();
		try{
			tools.load("META-INF/unprotectedurls.xml");
			tools.getUnprotectedResources();
			// System.out.println(unProtectedRes.toString());
//			for(String str : unProtectedResJsp){
//				System.out.println(str);
//			}
		}catch(Exception e){
			e.printStackTrace();
		}

	}

}
