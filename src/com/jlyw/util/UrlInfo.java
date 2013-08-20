package com.jlyw.util;

import java.util.regex.Pattern;

/**
 * Url正则表达式，用于匹配Url
 * @author Zhan
 *
 */
public class UrlInfo {
	private String uri;	//请求的链接（不带参数）
	private String queryString;	//参数
	private Pattern uriPattern; 
	
	public UrlInfo(String url){
		if(url == null){
			throw new RuntimeException("invalidated url");
		}
		setUrl(url);
	}

	public String getUri() {
		return uri;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setUrl(String url) {
		if(url == null){
			throw new RuntimeException("invalidated url");
		}
		int index = url.indexOf("?");
		if(index > 0){
			this.uri = url.substring(0, index);
			this.queryString = url.substring(index + 1);
		}else{
			this.uri = url;
			this.queryString = null;
		}
		this.uriPattern = Pattern.compile(this.uri);
	}

	public Pattern getUriPattern() {
		return this.uriPattern;
	}
	
	/**
	 * 判断是否匹配一个URL
	 * @param uri
	 * @param queryString
	 * @return
	 */
	public boolean isMatchUrl(String uri, String queryString){
		if(uri == null){
			return false;
		}
		if(this.uriPattern.matcher(uri).matches()){ //URI链接匹配
			if(this.queryString == null || this.queryString.length() == 0){ //没有参数（无论带什么参数，均为匹配）
				return true;
			}
			String[] unProtUrlParams = this.queryString.split("&+");	//参数部分
			if(unProtUrlParams != null && unProtUrlParams.length > 0){
				boolean isUnprotectedMatch = true;
				for(String param : unProtUrlParams){
					if(param.length() > 0 && (queryString == null || queryString.indexOf(param) == -1)){	//所访问的链接没有相关参数：不匹配
						isUnprotectedMatch = false;
						break;
					}else if(param.length() > 0){	//所访问的链接包含该参数，判断是否等于该参数，即去除：******?method=12&a=2（所访问的链接）包含method=1（受保护资源的参数）的情况
						int index = queryString.indexOf(param);
						if((queryString.length() > index + param.length()) && (queryString.charAt(index + param.length()) != '&') && (queryString.charAt(index + param.length()) != ' ')){
							isUnprotectedMatch = false;
							break;
						}
					}
				}
				if(!isUnprotectedMatch){ //参数不匹配
					return false;
				}
			}
			return true;
		}
		return false;		
	}	
}
