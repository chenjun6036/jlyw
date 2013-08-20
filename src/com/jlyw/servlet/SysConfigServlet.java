package com.jlyw.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.me.JSONObject;

import com.jlyw.util.SystemCfgUtil;

public class SysConfigServlet extends HttpServlet {
	private static final Log log = LogFactory.getLog(SysConfigServlet.class);

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Integer method = Integer.parseInt(req.getParameter("method"));	//判断请求的方法类型
		switch(method){
		case 0://配置系统运行参数
			JSONObject retJSON = new JSONObject();
			try{
				String AllotRule = req.getParameter("AllotRule");
				String SecondLoginPeriod = req.getParameter("SecondLoginPeriod");
				String SpecialLetters = req.getParameter("SpecialLetters");
				if(AllotRule != null){
					SystemCfgUtil.setTaskAllotRule(Integer.parseInt(AllotRule));
				}
				if(SecondLoginPeriod != null){
					SystemCfgUtil.setSecondLoginPeriod(Integer.parseInt(SecondLoginPeriod));
				}
				if(SpecialLetters != null){
					SystemCfgUtil.setSpecialLetters(SpecialLetters);
				}
				SystemCfgUtil.writeToSysDynCfgFile();
				retJSON.put("IsOK", true);
			}catch(Exception e){
				try{
					retJSON.put("IsOK", false);
					retJSON.put("msg", String.format("配置系统运行参数失败，原因：%s", e.getMessage()==null?"":e.getMessage()));
				}catch(Exception ex){}
				log.error("error in SysConfigServlet-->case 0", e);
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON.toString());
			}
			break;
		}
	}

}
