package com.jlyw.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class EncodingFilter implements Filter{
	private FilterConfig config=null;
	private String targetEncoding="GBK";
	
	
	public void destroy() {
		// TODO Auto-generated method stub
		this.config=null;
		
	}
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest req=(HttpServletRequest)request;
		req.setCharacterEncoding(this.targetEncoding);
		chain.doFilter(request, response);
		
	}
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		this.config=filterConfig;
		this.targetEncoding=config.getInitParameter("encoding");
		
	}
	
	
	

}
