package com.jlyw.util;

import java.util.regex.Pattern;

/**
 * Url������ʽ������ƥ��Url
 * @author Zhan
 *
 */
public class UrlInfo {
	private String uri;	//��������ӣ�����������
	private String queryString;	//����
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
	 * �ж��Ƿ�ƥ��һ��URL
	 * @param uri
	 * @param queryString
	 * @return
	 */
	public boolean isMatchUrl(String uri, String queryString){
		if(uri == null){
			return false;
		}
		if(this.uriPattern.matcher(uri).matches()){ //URI����ƥ��
			if(this.queryString == null || this.queryString.length() == 0){ //û�в��������۴�ʲô��������Ϊƥ�䣩
				return true;
			}
			String[] unProtUrlParams = this.queryString.split("&+");	//��������
			if(unProtUrlParams != null && unProtUrlParams.length > 0){
				boolean isUnprotectedMatch = true;
				for(String param : unProtUrlParams){
					if(param.length() > 0 && (queryString == null || queryString.indexOf(param) == -1)){	//�����ʵ�����û����ز�������ƥ��
						isUnprotectedMatch = false;
						break;
					}else if(param.length() > 0){	//�����ʵ����Ӱ����ò������ж��Ƿ���ڸò�������ȥ����******?method=12&a=2�������ʵ����ӣ�����method=1���ܱ�����Դ�Ĳ����������
						int index = queryString.indexOf(param);
						if((queryString.length() > index + param.length()) && (queryString.charAt(index + param.length()) != '&') && (queryString.charAt(index + param.length()) != ' ')){
							isUnprotectedMatch = false;
							break;
						}
					}
				}
				if(!isUnprotectedMatch){ //������ƥ��
					return false;
				}
			}
			return true;
		}
		return false;		
	}	
}
