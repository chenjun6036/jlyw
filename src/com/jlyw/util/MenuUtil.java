package com.jlyw.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

public class MenuUtil {
	private class MenuNode{
		private Map<String, Object> attrs = null;	//�ڵ����ԣ���text���ԣ�<String,String>;attributes���ԣ�<String,Map<String,String>>
		private List<MenuNode> children = null;	//�ӽڵ�
		
		public MenuNode(){
			attrs = new HashMap<String, Object>();
			children = null;
		}
		/**
		 * ���һ������
		 * @param key
		 * @param value
		 */
		public void putAttributes(String key, Object value){
			attrs.put(key, value);
		}
		/**
		 * ���һ���ӽڵ�
		 * @param node
		 */
		public void setChildren(List<MenuNode> nodeList){
			children = nodeList;
		}
		public Map<String, Object> getAttributes(){
			return this.attrs;
		}
		public List<MenuNode> getChildren(){
			return this.children;
		}
	}
	
	
	private static MenuUtil Instance = null;
	private Element rootElement = null;
	private List<MenuNode> rootNodeList = null;	//����˵��б�
	
	private MenuUtil() throws Exception{
		URL url = MenuUtil.class.getClassLoader().getResource(SystemCfgUtil.MenuXmlFilePath);
		rootElement = new SAXBuilder().build(url).getRootElement();
		getTreeNodes();
	}
	public List<MenuNode> getRootNodeList(){
		return this.rootNodeList;
	}
	
	public static MenuUtil getInstance() throws Exception{
		if(Instance == null){
			try{
				Instance = new MenuUtil();
			}catch(Exception e){
				throw new Exception("�˵�����(MenuUtil)ʵ����ʼ��ʧ�ܣ�");
			}
		}
		return Instance;
	}
	
	/**
	 * ��ȡ���ڵ�
	 */
	@SuppressWarnings("unchecked")
	private void getTreeNodes(){
		List<Element> rootNodesList = rootElement.getChildren();	//��һ���˵��ڵ�
		rootNodeList = getChildren(rootNodesList);
	}
	
	/**
	 * ��ȡ�ӽڵ��б�
	 * @param childrenElementList
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<MenuNode> getChildren(List<Element> childrenElementList){
		if(childrenElementList == null || childrenElementList.size() == 0){
			return null;
		}
		List<MenuNode> retList = new ArrayList<MenuNode>();
		for(Element nodeElement : childrenElementList){
			MenuNode node = new MenuNode();
			List<Attribute> attrList = nodeElement.getAttributes();
			for(Attribute attr : attrList){
				node.putAttributes(attr.getName(), attr.getValue());
			}
			
			//�������Խڵ�
			Element attrElement = nodeElement.getChild("attributes");
			if(attrElement != null && attrElement.getAttributes() != null && attrElement.getAttributes().size() > 0){
				Map<String, String> attrMap = new HashMap<String, String>();
				for(Attribute attr : (List<Attribute>)attrElement.getAttributes()){
					attrMap.put(attr.getName(), attr.getValue());
				}
				node.putAttributes("attributes", attrMap);
			}
			
			//�����ӽڵ�
			Element childElement = nodeElement.getChild("children");
			if(childElement != null && childElement.getChildren() != null && childElement.getChildren().size() > 0){
				List<Element> childrenElementList2 = childElement.getChildren();
				node.setChildren(getChildren(childrenElementList2));
			}
			
			retList.add(node);
		}
		return retList;
	}
	
	
	/**
	 * �����û��Ĳ˵���
	 * @param unProtectedResJsp:���ܷ��ʵ���Դ�б�ҳ��
	 * @param unProtectedResServlet�����ܷ��ʵ���Դ�б�servlet
	 * @param pUrlSet���û��ɷ��ʵ���Դ�б�Ȩ���б�
	 * @param nodeList���ܵĲ˵����ڵ��б�
	 * @return
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public JSONArray generateTreeMenu(final List<UrlInfo> unProtectedResJsp, final List<UrlInfo>unProtectedResServlet, final Map<Integer, UrlInfo> pUrlMap, final List<MenuNode> nodeList) throws JSONException{
		if(nodeList == null || nodeList.size() == 0){
			return null;
		}
		JSONArray retArray = new JSONArray();
		for(MenuNode menuNode : nodeList){
			JSONObject obj = new JSONObject();
			boolean isLeaf = true;	//�Ƿ�ΪҶ�ӽڵ�
			boolean isValid = true;	//�ýڵ�ΪҶ�ӽڵ�ʱ�Ƿ���Ч����Ȩ�޷��ʣ�
			
			//�洢�ӽڵ�
			List<MenuNode> childrenList = menuNode.getChildren();
			JSONArray childJSONArray = generateTreeMenu(unProtectedResJsp, unProtectedResServlet, pUrlMap, childrenList);
			if(childJSONArray != null && childJSONArray.length() > 0){
				obj.put("children", childJSONArray);
				isLeaf = false;
			}
			
			
			//�洢���ԣ���text������attributes����
			Map<String, Object> attrs = menuNode.getAttributes();
			if(isLeaf){	//��Ҷ�ӽڵ��Ҹýڵ㲻�����ԣ�attributes�µ�url���ԣ���Ϊ��Ч�ڵ�
				if(!attrs.containsKey("attributes") || !((Map<String, String>)attrs.get("attributes")).containsKey("url")){
					continue;
				}
			}
			Iterator<Entry<String,Object>> iter = attrs.entrySet().iterator();
			while(iter.hasNext()){
				Entry<String, Object> attr = iter.next();
				if(attr.getKey().toLowerCase().equals("attributes")){	//attributes����
					Map<String, String> aMap = (Map<String, String>) attr.getValue();
					JSONObject attrObj = new JSONObject();
					Iterator<Entry<String,String>> iter2 = aMap.entrySet().iterator();
					while(iter2.hasNext()){
						Entry<String, String> a = iter2.next();
						if(a.getKey().toLowerCase().equals("url")){	//Ҷ�ӽڵ��URL
							if(!isLeaf){	//�ýڵ㲻��Ҷ�ӽڵ㣺���Ը�URL
								continue;
							}
							if(!isUnprotectedUrl(a.getValue(), unProtectedResJsp, unProtectedResServlet) && checkSafe(a.getValue(), pUrlMap)){	//û��Ȩ�޷���
								isValid = false;
								break;
							}
						}
						attrObj.put(a.getKey(), a.getValue());
					}
					if(!isValid){
						break;
					}
					obj.put("attributes", attrObj);
				}else{
					obj.put(attr.getKey(), attr.getValue());
				}
			}
			
			if((isLeaf && isValid) || (!isLeaf && childJSONArray != null && childJSONArray.length() > 0)){	//Ҷ�Ӳ���Ȩ�޷��ʣ����߲���Ҷ�ӽڵ㲢�Ҿ����ӽڵ�
				retArray.put(obj);
			}
		}
		
		return retArray;
	}
	
	/**
	 * �ж�һ�������Ƿ�Ϊ�Ǳ�����Դ
	 * @param request
	 * @return ������Ϊ�Ǳ�����Դ������true�����򣬷���false
	 */
	private boolean isUnprotectedUrl(final String requestUrl, final List<UrlInfo> unProtectedResJsp, final List<UrlInfo>unProtectedResServlet) {
		String url = requestUrl;
		int index = url.indexOf(SystemCfgUtil.ProjectName);
		if (index > -1) {
			url = url.substring(index + SystemCfgUtil.ProjectName.length());
		}
		
		String queryString = "";
		index = url.indexOf('?');
		if(index > -1){
			queryString = url.substring(index + 1);
			url = url.substring(0, index);
		}
		
		List<UrlInfo> unProtectedRes = null;
		if(url.endsWith(".do")){
			unProtectedRes = unProtectedResServlet;
		}else{
			unProtectedRes = unProtectedResJsp;
		}
		for(UrlInfo unProtUrl : unProtectedRes) {
			if(unProtUrl.isMatchUrl(url, queryString)){
				return true;
			}
		}
		return false;
	}

	/**
	 * �ж�һ���û��Ƿ� ��ӵ�У���ֹ������ ĳ����Դ��Ȩ��
	 * @param request���û��������Դ
	 * @param pSet :�û�ӵ�е�Ȩ��Url(��ʽ��URI����URI?����)
	 * @return �û���ӵ�У���ֹ������ ����Դ��Ȩ���򷵻�true�����򣬷���false
	 */
	private boolean checkSafe(final String requestUrl, final Map<Integer, UrlInfo> pUrlMap) {
		if(pUrlMap == null){
			return false;
		}
		String uri = requestUrl;
		int index = uri.indexOf(SystemCfgUtil.ProjectName);
		if (index > -1) {
			uri = uri.substring(index + SystemCfgUtil.ProjectName.length());
		}
		String queryString = "";
		index = uri.indexOf('?');
		if(index > -1){
			queryString = uri.substring(index + 1);
			uri = uri.substring(0, index);
		}
		
		Iterator<Entry<Integer, UrlInfo>> iter = pUrlMap.entrySet().iterator();
		while(iter.hasNext()){
			Entry<Integer, UrlInfo> e = iter.next();
			if(e.getValue().isMatchUrl(uri, queryString)){	//�Ƿ�ӵ��Ȩ��
				return true;
			}
		}
		return false;
	}
}
