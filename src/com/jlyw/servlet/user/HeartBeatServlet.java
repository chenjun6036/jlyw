package com.jlyw.servlet.user;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.jlyw.hibernate.Information;
import com.jlyw.hibernate.SysUser;
import com.jlyw.manager.InformationManager;
import com.jlyw.service.InformationDeleteService;
import com.jlyw.util.FlagUtil;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.SystemCfgUtil;

/**
 * 心跳包
 * @author lulu
 * 2011-7-21
 */
public class HeartBeatServlet extends HttpServlet {
	private static final Log log = LogFactory.getLog(HeartBeatServlet.class);
	
	@SuppressWarnings("deprecation")
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JSONObject retJSON = new JSONObject();
		try{
			SysUser loginUser = (SysUser)request.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser);
			if(loginUser != null){
				UserLog.getInstance().setUserActive(loginUser.getId());
				retJSON.put("IsOnline", true);
				try{	//查询任务——弹窗提醒
					String IsLogin = request.getParameter("IsLogin");
					if(IsLogin != null && IsLogin.equalsIgnoreCase("1")){	//刚登录进来
						retJSON.put("IsNewMsg", false);
						return;
					}
					StringBuffer msg = new StringBuffer("<table width='100%' cellSpacing='0' cellPadding='0' style='border: #adc9ff 1px solid; border-collapse:collapse;' ><thead><tr bgcolor='#E6F2FE' height='30px'><td width='80px'>类型</td><td width='150px'>内容</td><td width='60px'>操作</td></tr></thead><tbody>");
					Timestamp beginTime = new Timestamp(System.currentTimeMillis());
					beginTime.setDate(beginTime.getDate() - InformationDeleteService.MaxDateOfInformation);
					KeyValueWithOperator k1 = new KeyValueWithOperator("createDate",beginTime, ">=");
					KeyValueWithOperator k2 = new KeyValueWithOperator("status", 1, "<>");
					KeyValueWithOperator k3 = new KeyValueWithOperator("sysUser.id", loginUser.getId(), "=");
					List<Information> retList = InformationManager.findPageAllInformationBySort(1, 5, "createDate", true, k1, k2, k3);
					
					if(retList.size() > 0){
						for(Information i : retList){
							msg.append("<tr height='30px'><td style='border: #adc9ff 1px solid;'>")
								.append(FlagUtil.SmsAndInfomationType.getMsgTypeInfo(i.getType()))
								.append("</td><td style='border: #adc9ff 1px solid;'>")
								.append(i.getContents())
								.append("</td><td style='border: #adc9ff 1px solid;'>");
							if(i.getUrl() != null && i.getUrl().length() > 0){
								msg.append("<a href='javascript:void(0)' style='text-decoration:underline' onClick='doOpenUrlFromMessager(\"")
									.append(i.getUrl())
									.append("\")'><span style='color: #0033FF'>查看</span></a></td>");
							}else{
								msg.append("</td>");
							}
							InformationManager.setInformationReaded(i);
						}
						msg.append("</tbody></table>");
						retJSON.put("IsNewMsg", true);
						retJSON.put("NewMsg", msg.toString());
					}else{
						retJSON.put("IsNewMsg", false);
					}
					
				}catch(Exception e){
					if(e.getClass() == java.lang.Exception.class){ //自定义的消息
						log.debug("exception in HeartBeatServlet--->1", e);
					}else{
						log.error("error in HeartBeatServlet--->1", e);
					} 

					retJSON.put("IsNewMsg", false);
				}
			}else{
				retJSON.put("IsOnline", false);
				retJSON.put("IsNewMsg", false);
			}
		}catch(Exception e){
			if(e.getClass() == java.lang.Exception.class){ //自定义的消息
				log.debug("exception in HeartBeatServlet--->2", e);
			}else{
				log.error("error in HeartBeatServlet--->2", e);
			}
			try {
				retJSON.put("IsOnline", false);
				retJSON.put("IsNewMsg", false);
			} catch (JSONException e1) {}
		}finally{
			response.setContentType("text/json;charset=utf-8");
			response.getWriter().write(retJSON.toString());
		}
	}	
}
