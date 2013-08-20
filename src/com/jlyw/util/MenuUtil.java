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
		private Map<String, Object> attrs = null;	//节点属性；如text属性：<String,String>;attributes属性：<String,Map<String,String>>
		private List<MenuNode> children = null;	//子节点
		
		public MenuNode(){
			attrs = new HashMap<String, Object>();
			children = null;
		}
		/**
		 * 添加一个属性
		 * @param key
		 * @param value
		 */
		public void putAttributes(String key, Object value){
			attrs.put(key, value);
		}
		/**
		 * 添加一个子节点
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
	private List<MenuNode> rootNodeList = null;	//顶层菜单列表
	
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
				throw new Exception("菜单助手(MenuUtil)实例初始化失败！");
			}
		}
		return Instance;
	}
	
	/**
	 * 获取树节点
	 */
	@SuppressWarnings("unchecked")
	private void getTreeNodes(){
		List<Element> rootNodesList = rootElement.getChildren();	//第一级菜单节点
		rootNodeList = getChildren(rootNodesList);
	}
	
	/**
	 * 获取子节点列表
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
			
			//处理属性节点
			Element attrElement = nodeElement.getChild("attributes");
			if(attrElement != null && attrElement.getAttributes() != null && attrElement.getAttributes().size() > 0){
				Map<String, String> attrMap = new HashMap<String, String>();
				for(Attribute attr : (List<Attribute>)attrElement.getAttributes()){
					attrMap.put(attr.getName(), attr.getValue());
				}
				node.putAttributes("attributes", attrMap);
			}
			
			//处理子节点
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
	 * 生成用户的菜单树
	 * @param unProtectedResJsp:不受访问的资源列表：页面
	 * @param unProtectedResServlet：不受访问的资源列表：servlet
	 * @param pUrlSet：用户可访问的资源列表（权限列表）
	 * @param nodeList：总的菜单树节点列表
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
			boolean isLeaf = true;	//是否为叶子节点
			boolean isValid = true;	//该节点为叶子节点时是否有效（有权限访问）
			
			//存储子节点
			List<MenuNode> childrenList = menuNode.getChildren();
			JSONArray childJSONArray = generateTreeMenu(unProtectedResJsp, unProtectedResServlet, pUrlMap, childrenList);
			if(childJSONArray != null && childJSONArray.length() > 0){
				obj.put("children", childJSONArray);
				isLeaf = false;
			}
			
			
			//存储属性：“text”、“attributes”等
			Map<String, Object> attrs = menuNode.getAttributes();
			if(isLeaf){	//是叶子节点且该节点不含属性：attributes下的url属性，则为无效节点
				if(!attrs.containsKey("attributes") || !((Map<String, String>)attrs.get("attributes")).containsKey("url")){
					continue;
				}
			}
			Iterator<Entry<String,Object>> iter = attrs.entrySet().iterator();
			while(iter.hasNext()){
				Entry<String, Object> attr = iter.next();
				if(attr.getKey().toLowerCase().equals("attributes")){	//attributes属性
					Map<String, String> aMap = (Map<String, String>) attr.getValue();
					JSONObject attrObj = new JSONObject();
					Iterator<Entry<String,String>> iter2 = aMap.entrySet().iterator();
					while(iter2.hasNext()){
						Entry<String, String> a = iter2.next();
						if(a.getKey().toLowerCase().equals("url")){	//叶子节点的URL
							if(!isLeaf){	//该节点不是叶子节点：忽略该URL
								continue;
							}
							if(!isUnprotectedUrl(a.getValue(), unProtectedResJsp, unProtectedResServlet) && checkSafe(a.getValue(), pUrlMap)){	//没有权限访问
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
			
			if((isLeaf && isValid) || (!isLeaf && childJSONArray != null && childJSONArray.length() > 0)){	//叶子并有权限访问，或者不是叶子节点并且具有子节点
				retArray.put(obj);
			}
		}
		
		return retArray;
	}
	
	/**
	 * 判断一个连接是否为非保护资源
	 * @param request
	 * @return 该链接为非保护资源，返回true；否则，返回false
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
	 * 判断一个用户是否 不拥有（禁止操作） 某个资源的权限
	 * @param request：用户请求的资源
	 * @param pSet :用户拥有的权限Url(格式：URI或者URI?参数)
	 * @return 用户不拥有（禁止操作） 该资源的权限则返回true，否则，返回false
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
			if(e.getValue().isMatchUrl(uri, queryString)){	//是否拥有权限
				return true;
			}
		}
		return false;
	}
}
