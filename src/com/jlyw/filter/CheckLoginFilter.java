package com.jlyw.filter;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.jlyw.util.SystemCfgUtil;
import com.jlyw.util.UrlInfo;

/**
 * 用于检测用户是否登陆的过滤器，如果未登录或超时，则重定向到指的登录页面
 * 
 * @author lulu 2011-7-21
 */
public class CheckLoginFilter implements Filter {
	private static final String ProjectName = SystemCfgUtil.ProjectName; // 项目名称：用于URL区分资源部分
	private static Log log = LogFactory.getLog(CheckLoginFilter.class);
	public static List<UrlInfo> unValidatedResJsp = null;
	public static List<UrlInfo> unValidatedResServlet = null;
	private static Element rootElement = null;

	private String unvalidateUrlsFilePath = null;

	public void destroy() {
		this.unvalidateUrlsFilePath = null;
	}

	public void init(FilterConfig arg0) throws ServletException {
		this.unvalidateUrlsFilePath = SystemCfgUtil.UnvalidateSessionUrlsConfigFilePath;
		try {
			getUnvalidatedResources();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(String.format("过滤器CheckLoginFilter初始化失败:%s", e.getMessage() == null ? "" : e.getMessage()));
		}
	}

	/**
	 * 载入配置文件
	 * 
	 * @param cfgFilePath
	 *            配置文件相对路径
	 */
	private void load(String cfgFilePath) throws Exception {
		URL url = CheckLoginFilter.class.getClassLoader().getResource(cfgFilePath);
		rootElement = new SAXBuilder().build(url).getRootElement();
	}

	/**
	 * 获得配置文件中指定名称的Element
	 * 
	 * @param elementName
	 * @return
	 */
	private Element getElement(String elementName) throws Exception {
		return rootElement.getChild(elementName);
	}

	/**
	 * 获取不受访问限制的资源信息列表；
	 */
	private void getUnvalidatedResources() throws Exception {
		if (rootElement == null) {
			load(this.unvalidateUrlsFilePath);
		}
		if (unValidatedResJsp == null) {
			unValidatedResJsp = new ArrayList<UrlInfo>();
			Element interceptors = getElement("unvalidatedurls-jsp");
			List<Element> urlList = interceptors.getChildren("unvalidatedurl");
			Iterator<Element> it = urlList.iterator();
			Element tmpElement = null;
			while (it.hasNext()) {
				tmpElement = it.next();
				String url = tmpElement.getAttributeValue("url");
				if(url != null && url.length() > 0){
					unValidatedResJsp.add(new UrlInfo(url));
				}
				
			}
		}
		if (unValidatedResServlet == null) {
			unValidatedResServlet = new ArrayList<UrlInfo>();
			Element interceptors = getElement("unvalidatedurls-servlet");
			List<Element> urlList = interceptors.getChildren("unvalidatedurl");
			Iterator<Element> it = urlList.iterator();
			Element tmpElement = null;
			while (it.hasNext()) {
				tmpElement = it.next();
				String url = tmpElement.getAttributeValue("url");
				if(url != null && url.length() > 0){
					unValidatedResServlet.add(new UrlInfo(url));
				}
			}
		}
	}

	/**
	 * 判断一个连接是否为不需要验证Session的资源
	 * 
	 * @param request
	 * @return 该链接为非保护资源，返回true；否则，返回false
	 */
	private boolean isUnvalidatedUrl(HttpServletRequest request) {
		String url = request.getRequestURI().toString();
		int index = url.indexOf(ProjectName);
		if (index > -1) {
			url = url.substring(index + ProjectName.length());
		}
		String queryString = request.getQueryString();

		List<UrlInfo> unValidatedRes = null;
		if (url.endsWith(".do")) {
			unValidatedRes = unValidatedResServlet;
		} else {
			unValidatedRes = unValidatedResJsp;
		}
		for (UrlInfo unProtUrl : unValidatedRes) {
			if(unProtUrl.isMatchUrl(url, queryString)){
				return true;
			}
		}
		return false;
	}

	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		try{
			HttpServletRequest request = (HttpServletRequest) servletRequest;
			HttpServletResponse response = (HttpServletResponse) servletResponse;
			
			if(!isUnvalidatedUrl(request) && !request.getRequestURI().equals(ProjectName)){
				HttpSession session = request.getSession();
				// 用户超时或没有登陆时跳转到登陆页面
				if (session == null || session.getAttribute(SystemCfgUtil.SessionAttrNameLoginUser) == null) {
					log.debug("Logout automatically");
					response.sendRedirect("/jlyw/");
					return ;
				}
			}
//			/**************** 设置默认登录用户：研发用 ******************/
//			HttpSession session = request.getSession(true);
//			if (session.getAttribute("LOGIN_USER") == null) {
//				SysUser user = new SysUser();
//				user.setId(2);
//				user.setName("蒋燕");
//				user.setUserName("000049");
//				session.setAttribute("LOGIN_USER", user);
//			}
			filterChain.doFilter(servletRequest, servletResponse);
		}catch(Exception e){
			log.error("error in CheckLoginFilter", e);
		}
	}
}
