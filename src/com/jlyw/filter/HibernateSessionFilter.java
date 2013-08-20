package com.jlyw.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.hibernate.Session;

import com.jlyw.hibernate.HibernateSessionFactory;

public class HibernateSessionFilter implements Filter{

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		Session session = HibernateSessionFactory.getSession();	//��ȡ���ݿ�����
		
		chain.doFilter(request, response);
		
		if(session != null && session.isOpen()){	//�ر�����
			HibernateSessionFactory.closeSessionForFilter();
		}
		
	}

	public void init(FilterConfig arg0) throws ServletException {
	}

}
