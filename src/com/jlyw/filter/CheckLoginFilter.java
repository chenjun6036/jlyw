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
 * ���ڼ���û��Ƿ��½�Ĺ����������δ��¼��ʱ�����ض���ָ�ĵ�¼ҳ��
 * 
 * @author lulu 2011-7-21
 */
public class CheckLoginFilter implements Filter {
	private static final String ProjectName = SystemCfgUtil.ProjectName; // ��Ŀ���ƣ�����URL������Դ����
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
			throw new ServletException(String.format("������CheckLoginFilter��ʼ��ʧ��:%s", e.getMessage() == null ? "" : e.getMessage()));
		}
	}

	/**
	 * ���������ļ�
	 * 
	 * @param cfgFilePath
	 *            �����ļ����·��
	 */
	private void load(String cfgFilePath) throws Exception {
		URL url = CheckLoginFilter.class.getClassLoader().getResource(cfgFilePath);
		rootElement = new SAXBuilder().build(url).getRootElement();
	}

	/**
	 * ��������ļ���ָ�����Ƶ�Element
	 * 
	 * @param elementName
	 * @return
	 */
	private Element getElement(String elementName) throws Exception {
		return rootElement.getChild(elementName);
	}

	/**
	 * ��ȡ���ܷ������Ƶ���Դ��Ϣ�б�
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
	 * �ж�һ�������Ƿ�Ϊ����Ҫ��֤Session����Դ
	 * 
	 * @param request
	 * @return ������Ϊ�Ǳ�����Դ������true�����򣬷���false
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
				// �û���ʱ��û�е�½ʱ��ת����½ҳ��
				if (session == null || session.getAttribute(SystemCfgUtil.SessionAttrNameLoginUser) == null) {
					log.debug("Logout automatically");
					response.sendRedirect("/jlyw/");
					return ;
				}
			}
//			/**************** ����Ĭ�ϵ�¼�û����з��� ******************/
//			HttpSession session = request.getSession(true);
//			if (session.getAttribute("LOGIN_USER") == null) {
//				SysUser user = new SysUser();
//				user.setId(2);
//				user.setName("����");
//				user.setUserName("000049");
//				session.setAttribute("LOGIN_USER", user);
//			}
			filterChain.doFilter(servletRequest, servletResponse);
		}catch(Exception e){
			log.error("error in CheckLoginFilter", e);
		}
	}
}
