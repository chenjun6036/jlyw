package com.jlyw.servlet.user;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.jlyw.hibernate.Discount;
import com.jlyw.hibernate.ProjectTeam;
import com.jlyw.hibernate.SysUser;
import com.jlyw.hibernate.UserRole;
import com.jlyw.manager.AddressManager;
import com.jlyw.manager.DiscountComSheetManager;
import com.jlyw.manager.OverdueManager;
import com.jlyw.manager.RolePrivilegeManager;
import com.jlyw.manager.UserManager;
import com.jlyw.manager.UserRoleManager;
import com.jlyw.manager.WithdrawManager;
import com.jlyw.util.ExportUtil;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.LetterUtil;
import com.jlyw.util.MongoDBUtil;
import com.jlyw.util.SystemCfgUtil;
import com.jlyw.util.UIDUtil;
import com.jlyw.util.UrlInfo;

public class UserServlet extends HttpServlet {

	private static Log log = LogFactory.getLog(UserServlet.class);
	/**
	 * Constructor of the object.
	 */
	public UserServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		UserManager userMgr = new UserManager();	
		//String userId = request.getParameter("userId");
		Integer method = Integer.parseInt(request.getParameter("method"));	//判断请求的方法类型
		//System.out.println("session created");
		switch (method) {
		case 0: // 分页查询
			JSONObject res = new JSONObject();
			try {
				String queryname = request.getParameter("queryname");
				String queryGender = request.getParameter("queryGender");
				String queryJobTitle = request.getParameter("queryJobTitle");
				String queryDepartment = request.getParameter("queryDepartment");
				String queryProjectTeam = request.getParameter("queryProjectTeam");
				String queryStatus = request.getParameter("queryStatus");
				String queryType = request.getParameter("queryType");
				String queryTel = request.getParameter("queryTel");
				String queryIDNum = request.getParameter("queryIDNum");
				String queryPolStatus = request.getParameter("queryPolStatus");
				String queryStr = "from SysUser as model,ProjectTeam as pro where 1=1 and model.projectTeamId = pro.id ";
				List<Object> list = new ArrayList<Object>();
				if(queryname != null&&!queryname.equals(""))
				{
					String userNameStr = URLDecoder.decode(queryname, "UTF-8");
					
					list.add("%" + userNameStr + "%");
					list.add("%" + userNameStr + "%");
					queryStr = queryStr + "and (model.brief like ? or model.name like ?) ";
				}
				if(queryGender != null&&!queryGender.equals("undefined"))
				{
					String userGenderStr = URLDecoder.decode(queryGender, "UTF-8");
					list.add(Integer.valueOf(userGenderStr).equals(0));
					queryStr = queryStr + "and model.gender = ? ";
				}
				if(queryJobTitle != null&&!queryJobTitle.equals(""))
				{
					String userJobTitleStr = URLDecoder.decode(queryJobTitle, "UTF-8");
					list.add("%" + userJobTitleStr + "%");
					queryStr = queryStr + "and model.jobTitle like ? ";
				}
				if(queryDepartment != null&&!queryDepartment.equals(""))
				{
					String userDepartmentStr = URLDecoder.decode(queryDepartment, "UTF-8");
					list.add(LetterUtil.isNumeric(userDepartmentStr)?Integer.valueOf(userDepartmentStr):0);
					list.add("%" + userDepartmentStr + "%");
					list.add("%" + userDepartmentStr + "%");
					queryStr = queryStr + "and model.projectTeamId in (select model1.id from ProjectTeam as model1 where model1.department.id = ? or model1.department.name like ? or model1.department.brief like ?) ";
				}
				if(queryProjectTeam != null&&!queryProjectTeam.equals(""))
				{
					String userProjectTeamStr = URLDecoder.decode(queryProjectTeam, "UTF-8");
					list.add(LetterUtil.isNumeric(userProjectTeamStr)?Integer.valueOf(userProjectTeamStr):0);
					list.add("%" + userProjectTeamStr + "%");
					list.add("%" + userProjectTeamStr + "%");
					queryStr = queryStr + "and (model.projectTeamId = ? or model.projectTeamId in (select model2.id from ProjectTeam as model2 where model2.name like ? or model2.brief like ?)) ";
				}
				if(queryStatus != null&&!queryStatus.equals(""))
				{
					String userStatusStr = URLDecoder.decode(queryStatus, "UTF-8");
					list.add(LetterUtil.isNumeric(userStatusStr)?Integer.valueOf(userStatusStr):0);
					queryStr = queryStr + "and model.status = ? ";
				}
				if(queryType != null&&!queryType.equals(""))
				{
					String userTypeStr = URLDecoder.decode(queryType, "UTF-8");
					list.add(userTypeStr);
					queryStr = queryStr + "and model.type = ? ";
				}
				if(queryTel != null&&!queryTel.equals(""))
				{
					String userTelStr = URLDecoder.decode(queryTel, "UTF-8");
					list.add("%" + userTelStr + "%");
					list.add("%" + userTelStr + "%");
					list.add("%" + userTelStr + "%");
					queryStr = queryStr + "and (model.tel like ? or model.cellphone1 like ? or model.cellphone2 like ?) ";
				}
				if(queryIDNum != null&&!queryIDNum.equals(""))
				{
					String userIDNumStr = URLDecoder.decode(queryIDNum, "UTF-8");
					list.add("%" + userIDNumStr + "%");
					queryStr = queryStr + "and model.idnum like ? ";
				}
				if(queryPolStatus != null&&!queryPolStatus.equals(""))
				{
					String userPolStatusStr = URLDecoder.decode(queryPolStatus, "UTF-8");
					list.add("%" + userPolStatusStr + "%");
					queryStr = queryStr + "and model.politicsStatus like ? ";
				}
				
				int page = 1;
				if (request.getParameter("page") != null)
					page = Integer.parseInt(request.getParameter("page").toString());
				int rows = 10;
				if (request.getParameter("rows") != null)
					rows = Integer.parseInt(request.getParameter("rows").toString());
				List<Object[]> result;
				int total;
				result = userMgr.findPageAllByHQL("select model,pro " + queryStr + " order by pro.proTeamCode asc,model.jobNum asc", page, rows, list);
				total = userMgr.getTotalCountByHQL("select count(model) "+queryStr, list);
				JSONArray options = new JSONArray();
					for (Object[] obj : result) {
						SysUser user = (SysUser)obj[0];
						ProjectTeam pro = (ProjectTeam)obj[1];
						JSONObject option = new JSONObject();
						option.put("Id", user.getId());
						option.put("Name", user.getName());
						option.put("Brief", user.getBrief());
						option.put("userName", user.getUserName());
						option.put("Gender", user.getGender());
						option.put("JobNum", user.getJobNum());
						option.put("WorkLocation", user.getWorkLocationId());
						option.put("WorkLocationName", (new AddressManager()).findById(user.getWorkLocationId()).getHeadName());
						option.put("Birthplace", user.getBirthplace());
						option.put("Birthday", user.getBirthday());
						option.put("IDNum", user.getIdnum());
						option.put("PoliticsStatus", user.getPoliticsStatus());
						option.put("Nation", user.getNation());
						option.put("WorkSince", user.getWorkSince());
						option.put("WorkHereSince", user.getWorkHereSince());
						option.put("Education", user.getEducation());
						option.put("EducationDate", user.getEducationDate());
						option.put("EducationFrom", user.getEducationFrom());
						option.put("Degree", user.getDegree());
						option.put("DegreeDate", user.getDegreeDate());
						option.put("DegreeFrom", user.getDegreeFrom());
						option.put("JobTitle", user.getJobTitle());
						option.put("Specialty", user.getSpecialty());
						option.put("AdministrationPost", user.getAdministrationPost());
						option.put("PartyPost", user.getPartyPost());
						option.put("PartyDate", user.getPartyDate());
						option.put("HomeAdd", user.getHomeAdd());
						option.put("WorkAdd", user.getWorkAdd());
						option.put("Tel", user.getTel());
						option.put("Cellphone1", user.getCellphone1());
						option.put("Cellphone2", user.getCellphone2());
						option.put("ProjectTeamId", user.getProjectTeamId());
						option.put("ProjectTeamName", pro.getName());
						option.put("DepartmentId", pro.getDepartment().getId());
						option.put("Email", user.getEmail());
						option.put("Status", user.getStatus());
						option.put("Type", user.getType());
						option.put("CancelDate", user.getCancelDate());
						if(user.getSignature()!=null&&user.getSignature()!="")
						{
							HashMap<String, Object> h = new HashMap<String, Object>();
							h.put(MongoDBUtil.KEYNAME_FileSetName, user.getSignature());
							JSONObject file = MongoDBUtil.getLastUploadFileInfo(h, MongoDBUtil.CollectionType.Others);
							option.put("Signature", file==null?"":file);
						}
						else
						{
							option.put("Signature", "");
						}
						if(user.getPhotograph()!=null&&user.getPhotograph()!="")
						{
							HashMap<String, Object> h = new HashMap<String, Object>();
							h.put(MongoDBUtil.KEYNAME_FileSetName, user.getPhotograph());
							JSONObject file = MongoDBUtil.getLastUploadFileInfo(h, MongoDBUtil.CollectionType.Others);
							option.put("Photograph", file==null?"":file);
						}
						else
						{
							option.put("Photograph", "");
						}
						option.put("Remark", user.getRemark());
						option.put("NeedDongle", user.getNeedDongle()!=null&&user.getNeedDongle()?1:0);
						
						options.put(option);
					}
					res.put("total", total);
					res.put("rows", options);
				} catch (Exception e) {
					try {
						res.put("total", 0);
						res.put("rows", new JSONArray());
					} catch (JSONException ex) {
						ex.printStackTrace();
					}
					if(e.getClass() == java.lang.Exception.class){ //自定义的消息
						log.debug("exception in UserServlet-->case 0", e);
					}else{
						log.error("error in UserServlet-->case 0", e);
					} 

				}finally{
					response.setContentType("text/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(res.toString());
					//System.out.println(res.toString());
				}
			break;
		case 1:	//新增用户
			SysUser sysuser = initUser(request, 0);		
			if(userMgr.isUserNameExist(sysuser.getUserName())){//用户名已存在
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write("{'IsOK':false,'msg':'用户名已存在'}");	
			}else{	//用户名不存在
				try{
					
					//MessageDigest md = MessageDigest.getInstance("SHA-1");	//设置初始密码，密码用SHA-1算法加密后存放
					//user.setPassword(new String(md.digest(user.getJobNum().getBytes())));	
					sysuser.setPassword(sysuser.getJobNum());
					JSONObject retObj=new JSONObject();
					boolean res1 = userMgr.save(sysuser);
					retObj.put("IsOK", res1);
					retObj.put("msg", res1?"员工添加成功！":"员工添加失败，请重新添加！");
					retObj.put("signature_filesetname", sysuser.getSignature());
					retObj.put("photo_filesetname", sysuser.getPhotograph());
					response.setContentType("text/html;charset=utf-8");
					response.getWriter().write(retObj.toString());
				}catch(Exception e){
					if(e.getClass() == java.lang.Exception.class){ //自定义的消息
						log.debug("exception in UserServlet-->case 1", e);
					}else{
						log.error("error in UserServlet-->case 1", e);
					} 
					response.setContentType("text/html;charset=utf-8");
					response.getWriter().write("{'IsOK':false,'msg':'员工添加失败，请重新添加！'}");
				}
			}
			break;
		case 2:	//修改用户信息
			try{
				SysUser user = initUser(request, Integer.valueOf(request.getParameter("Id")));
				if(user == null){
					response.setContentType("text/html;charset=utf-8");
					response.getWriter().write("{'IsOK':false,'msg':'修改失败，该用户不存在'}");	
				}else{
					if(userMgr.update(user)){
						response.setContentType("text/html;charset=utf-8");
						response.getWriter().write("{'IsOK':true,'msg':'修改成功','signature_filesetname':'"+ user.getSignature() + "','photo_filesetname':'" + user.getPhotograph() + "'}");	
					}else{
						response.setContentType("text/html;charset=utf-8");
						response.getWriter().write("{'IsOK':false,'msg':'修改失败'}");	
					}
				}					
			}catch(Exception e){
				if(e.getClass() == java.lang.Exception.class){ //自定义的消息
					log.debug("exception in UserServlet-->case 2", e);
				}else{
					log.error("error in UserServlet-->case 2", e);
				} 
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write("{'IsOK':false,'msg':'修改失败'}");	
			}		
			break;
		case 3:	//重置密码
			try{
				List<SysUser> check = userMgr.findByVarProperty(new KeyValueWithOperator("userName", request.getParameter("userName"), "="));
				if(check == null||check.size()==0){
					response.setContentType("text/html;charset=utf-8");
					response.getWriter().write("{'IsOK':false,'msg':'重置密码失败，该用户不存在'}");	
				}else{
					SysUser user = check.get(0);
					//MessageDigest md = MessageDigest.getInstance("SHA-1");	//重置初始密码，密码用SHA-1算法加密后存放
					user.setPassword(user.getPassword());						
					if(userMgr.update(user)){
						response.setContentType("text/html;charset=utf-8");
						response.getWriter().write("{'IsOK':true,'msg':'重置密码成功'}");	
					}else{
						response.setContentType("text/html;charset=utf-8");
						response.getWriter().write("{'IsOK':false,'msg':'重置密码失败'}");	
					}
				}					
			}catch(Exception e){
				if(e.getClass() == java.lang.Exception.class){ //自定义的消息
					log.debug("exception in UserServlet-->case 3", e);
				}else{
					log.error("error in UserServlet-->case 3", e);
				} 
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write("{'IsOK':false,'msg':'重置密码失败'}");	
			}		
			break;
		case 4:	//用户登录
			JSONObject retJSON = new JSONObject();
			try{
				String pwd = request.getParameter("password");
				//MessageDigest md = MessageDigest.getInstance("SHA-1");	//重置初始密码，密码用SHA-1算法加密后存放	
				SysUser filter = new SysUser();
				String userName = request.getParameter("userName");
				filter.setUserName(userName);
				filter.setStatus(0);
				List<SysUser> result1 = userMgr.findByExample(filter);
				if(result1 != null && result1.size() == 1){
					if(!result1.get(0).getPassword().equals(pwd)){
						throw new Exception("用户名或者密码不正确！");
					}
					
					//存储已登录用户的权限列表Map
					ServletContext context = this.getServletContext();
					Map<Integer, Map<Integer,UrlInfo>> pMap = (Map<Integer, Map<Integer,UrlInfo>>)context.getAttribute(SystemCfgUtil.ContextAttrNameUserPrivilegesMap);
					if(pMap == null){	//第一个用户登录
						pMap = new HashMap<Integer, Map<Integer,UrlInfo>>();
						context.setAttribute(SystemCfgUtil.ContextAttrNameUserPrivilegesMap, pMap);
					}
					if(!pMap.containsKey(result1.get(0).getId())){
						Map<Integer, UrlInfo> pRetMap = new RolePrivilegeManager().getPrivilegesByUserId(result1.get(0).getId());
						if(pRetMap != null){
							pMap.put(result1.get(0).getId(), pRetMap);
						}
					}
					
					Map<Integer, UrlInfo> pRetMap = pMap.get(result1.get(0).getId());
					Iterator iterator = pRetMap.keySet().iterator();
					boolean IsOverdueNumber=true,isWithDraw=true,isDiscount=true;   //延期、退样、折扣
					while(iterator.hasNext()) {
						UrlInfo temp = pRetMap.get(iterator.next());						
						if(IsOverdueNumber&&temp.getUri().equals("TaskManage/OverdueTask.jsp")){
							IsOverdueNumber = false;
							continue;
						}
						if(isWithDraw&&temp.getUri().equals("TaskManage/WithdrawApply.jsp")){
							isWithDraw = false;
							continue;
						}
						if(isDiscount&&temp.getUri().equals("FeeManage/DiscountTask.jsp")){
							isDiscount = false;
							continue;
						}
					}
					HttpSession session = request.getSession(true);
					if(IsOverdueNumber){
						List<KeyValueWithOperator> condList = new ArrayList<KeyValueWithOperator>();	//查询条件
						condList.add(new KeyValueWithOperator("commissionSheet.status", 3, "<"));	//委托单尚未完工或尚未注销的
	//					condList.add(new KeyValueWithOperator("sysUserByExecutorId.id", ((SysUser)req.getSession().getAttribute("LOGIN_USER")).getId(), "="));  //办理人
						condList.add(new KeyValueWithOperator("executeTime", null, "is null"));	//审批任务尚未完成的
						OverdueManager overdueMgr = new OverdueManager();
						int total = overdueMgr.getTotalCount(condList);						
						session.setAttribute("OverdueNumber", total);
					}else{						
						session.setAttribute("OverdueNumber", 0);
					}
					if(isWithDraw){
						List<KeyValueWithOperator> condList = new ArrayList<KeyValueWithOperator>();	//查询条件
						condList.add(new KeyValueWithOperator("commissionSheet.status", 3, "<"));	//委托单尚未完工或尚未注销的
//						condList.add(new KeyValueWithOperator("sysUserByExecutorId.id", ((SysUser)req.getSession().getAttribute("LOGIN_USER")).getId(), "="));  //办理人
						condList.add(new KeyValueWithOperator("executeTime", null, "is null"));	//审批任务尚未完成的
						WithdrawManager withdrawMgr = new WithdrawManager();
						int total = withdrawMgr.getTotalCount(condList);				
						session.setAttribute("WithdrawNumber", total);
					}else{						
						session.setAttribute("WithdrawNumber", 0);
					}
					if(isDiscount){
						String QueryHQL = " from Discount as d where  d.id not in (select dc.discount.id from DiscountComSheet as dc where dc.commissionSheet.status=10) ";						
						QueryHQL = QueryHQL+"and d.executeTime is null ";
						DiscountComSheetManager discountcomSheetMgr=new DiscountComSheetManager();
						List<Object> keys = new ArrayList<Object>();
						int total = discountcomSheetMgr.getTotalCountByHQL("select count(*) "+QueryHQL, keys);	
						session.setAttribute("DiscountNumber", total);
					}else{						
						session.setAttribute("DiscountNumber", 0);
					}
					session.setAttribute("isOverdue", IsOverdueNumber?"1":"0");
					session.setAttribute("isWithDraw", isWithDraw?"1":"0");
					session.setAttribute("isDiscount", isDiscount?"1":"0");
					if(UserLog.getInstance().UserLogin(result1.get(0).getId(), request.getRemoteAddr(),request)){	//登录成功
						session.setAttribute(SystemCfgUtil.SessionAttrNameLoginUser, result1.get(0));
						retJSON.put("IsOK", true);
						return ;
					}else{	//已登录
						throw new Exception("用户已登录，一个账号不允许同时在两个地方登录！");
					}
					

				}
				throw new Exception("用户名或者密码不正确！");
			}catch(Exception e){
				if(e.getClass() == java.lang.Exception.class){ //自定义的消息
					log.debug("exception in UserServlet-->case 4", e);
				}else{
					log.error("error in UserServlet-->case 4", e);
				} 
				try {
					retJSON.put("IsOK", false);
					retJSON.put("msg", String.format("登陆失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {}
			}finally{
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write(retJSON.toString());
			}
			break;
		case 5:	//用户登出
			JSONObject retJSON5 = new JSONObject();
			try{
				HttpSession session = request.getSession();
				if(session != null){
					session.setAttribute("FromLogout", true);
					session.invalidate();
				}
//				request.getRequestDispatcher("/index.jsp").forward(request, response);
				retJSON5.put("IsOK", true);
				//System.out.println("userlogged out");
			}catch(Exception e){
				try {
					retJSON5.put("IsOK", false);
					retJSON5.put("msg", String.format("退出系统失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {}
				
				if(e.getClass() == java.lang.Exception.class){ //自定义的消息
					log.debug("exception in UserServlet-->case 5", e);
				}else{
					log.error("error in UserServlet-->case 5", e);
				}
			}finally{
				response.setContentType("text/json;charset=utf-8");
				response.getWriter().write(retJSON5.toString());
			}
			break;
		case 6:	//前端控件中输入用户名AutoComplete查询符合条件的用户列表（默认前三十个）,业务过程中使用，不包括已注销人员
			String userNameStr = request.getParameter("QueryName");	//查询的用户姓名
			JSONArray jsonArray = new JSONArray();
			try {
				if(userNameStr != null && userNameStr.trim().length() > 0){
					String cusName =  new String(userNameStr.trim().getBytes("ISO-8859-1"), "GBK");	//解决URL传递中文乱码问题
					//cusName = LetterUtil.String2Alpha(cusName);	//返回字母简码
					String queryStr = "from SysUser model where (model.brief like ? or model.name like ? or model.jobNum like ?) and model.status = 0";
					List<SysUser> retList = userMgr.findPageAllByHQL(queryStr, 1, 30, "%" + cusName + "%", "%" + cusName + "%", "%" + cusName + "%");
					if(retList != null){
						for(SysUser user : retList){
							JSONObject jsonObj = new JSONObject();
							jsonObj.put("jobNum", user.getJobNum());
							jsonObj.put("name", user.getName());
							jsonObj.put("id", user.getId());
							jsonArray.put(jsonObj);	
						}
					}
				}
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){ //自定义的消息
					log.debug("exception in UserServlet-->case 6", e);
				}else{
					log.error("error in UserServlet-->case 6", e);
				} 
			}finally{
				response.setContentType("text/json;charset=gbk");
				response.getWriter().write(jsonArray.toString());
			}
			break;
		case 7://修改密码
			String pwd = request.getParameter("old_pwd");
			String new_pwd = request.getParameter("pwd");
			SysUser user = (SysUser)request.getSession().getAttribute("LOGIN_USER");
			if(!pwd.equals(user.getPassword()))
			{
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write("{'IsOK':false,'msg':'原密码错误'}");
			}
			else{
				user.setPassword(new_pwd);
				if(userMgr.update(user)){
					response.setContentType("text/html;charset=utf-8");
					response.getWriter().write("{'IsOK':true,'msg':'密码修改成功'}");	
				}else{
					response.setContentType("text/html;charset=utf-8");
					response.getWriter().write("{'IsOK':false,'msg':'密码修改失败'}");	
				}
			}
			break;
		case 8://注销用户
			String id = request.getParameter("id");
			SysUser del_user = userMgr.findById(Integer.valueOf(id));
			del_user.setStatus(1);
			del_user.setCancelDate(new Timestamp(System.currentTimeMillis()));
			if(userMgr.update(del_user)){
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write("{'IsOK':true,'msg':'注销成功！'}");	
			}else{
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write("{'IsOK':false,'msg':'注销失败！'}");	
			}
			break;
		case 9://重置密码
			String username = request.getParameter("userName");
			String jobNum = request.getParameter("JobNum");
			List<SysUser> check = userMgr.findByVarProperty(new KeyValueWithOperator("userName", username, "="),new KeyValueWithOperator("jobNum", jobNum, "="));
			if(check==null||check.size()==0)
			{
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write("{'IsOK':false,'msg':'用户名与工号不一致，请确认！'}");	
			}
			else
			{
				SysUser user1 = check.get(0);
				user1.setPassword(user1.getJobNum());
				if(userMgr.update(user1)){
					response.setContentType("text/html;charset=utf-8");
					response.getWriter().write("{'IsOK':true,'msg':'重置密码成功！'}");	
				}else{
					response.setContentType("text/html;charset=utf-8");
					response.getWriter().write("{'IsOK':false,'msg':'重置密码失败！'}");	
				}
			}
			break;
		case 10://导出
			String paramsStr = request.getParameter("paramsStr");
			JSONObject retObj7 = new JSONObject();
			try {
				JSONObject params = new JSONObject(paramsStr);
				String queryStr = "select model from SysUser as model,ProjectTeam as model1 where 1=1 and model.projectTeamId = model1.id ";
				List<Object> list = new ArrayList<Object>();
				if(params.length()!=0)
				{
					String queryname = params.getString("queryname");
					String queryGender = params.getString("queryGender");
					String queryJobTitle = params.getString("queryJobTitle");
					String queryDepartment = params.getString("queryDepartment");
					String queryProjectTeam = params.getString("queryProjectTeam");
					String queryStatus = params.getString("queryStatus");
					String queryType = params.getString("queryType");
					String queryTel = params.getString("queryTel");
					String queryIDNum = params.getString("queryIDNum");
					String queryPolStatus = params.getString("queryPolStatus");
					if(queryname != null&&!queryname.equals(""))
					{
						String userName = URLDecoder.decode(queryname, "UTF-8");
						list.add("%" + userName + "%");
						list.add("%" + userName + "%");
						queryStr = queryStr + "and (model.brief like ? or model.name like ?) ";
					}
					if(queryGender != null&&!queryGender.equals("undefined"))
					{
						String userGenderStr = URLDecoder.decode(queryGender, "UTF-8");
						list.add(Integer.valueOf(userGenderStr).equals(0));
						queryStr = queryStr + "and model.gender = ? ";
					}
					if(queryJobTitle != null&&!queryJobTitle.equals(""))
					{
						String userJobTitleStr = URLDecoder.decode(queryJobTitle, "UTF-8");
						list.add("%" + userJobTitleStr + "%");
						queryStr = queryStr + "and model.jobTitle like ? ";
					}
					if(queryDepartment != null&&!queryDepartment.equals(""))
					{
						String userDepartmentStr = URLDecoder.decode(queryDepartment, "UTF-8");
						list.add(LetterUtil.isNumeric(userDepartmentStr)?Integer.valueOf(userDepartmentStr):0);
						list.add("%" + userDepartmentStr + "%");
						list.add("%" + userDepartmentStr + "%");
						queryStr = queryStr + "and model.projectTeamId in (select model1.id from ProjectTeam as model1 where model1.department.id = ? or model1.department.name like ? or model1.department.brief like ?) ";
					}
					if(queryProjectTeam != null&&!queryProjectTeam.equals(""))
					{
						String userProjectTeamStr = URLDecoder.decode(queryProjectTeam, "UTF-8");
						list.add(LetterUtil.isNumeric(userProjectTeamStr)?Integer.valueOf(userProjectTeamStr):0);
						list.add("%" + userProjectTeamStr + "%");
						list.add("%" + userProjectTeamStr + "%");
						queryStr = queryStr + "and (model.projectTeamId = ? or model.projectTeamId in (select model1.id from ProjectTeam as model1 where model1.name like ? or model1.brief like ?)) ";
					}
					if(queryStatus != null&&!queryStatus.equals(""))
					{
						String userStatusStr = URLDecoder.decode(queryStatus, "UTF-8");
						list.add(LetterUtil.isNumeric(userStatusStr)?Integer.valueOf(userStatusStr):0);
						queryStr = queryStr + "and model.status = ? ";
					}
					if(queryType != null&&!queryType.equals(""))
					{
						String userTypeStr = URLDecoder.decode(queryType, "UTF-8");
						list.add(LetterUtil.isNumeric(userTypeStr)?Integer.valueOf(userTypeStr):0);
						queryStr = queryStr + "and model.type = ? ";
					}
					if(queryTel != null&&!queryTel.equals(""))
					{
						String userTelStr = URLDecoder.decode(queryTel, "UTF-8");
						list.add("%" + userTelStr + "%");
						list.add("%" + userTelStr + "%");
						list.add("%" + userTelStr + "%");
						queryStr = queryStr + "and (model.tel like ? or model.cellphone1 like ? or model.cellphone2 like ?) ";
					}
					if(queryIDNum != null&&!queryIDNum.equals(""))
					{
						String userIDNumStr = URLDecoder.decode(queryIDNum, "UTF-8");
						list.add("%" + userIDNumStr + "%");
						queryStr = queryStr + "and model.idnum like ? ";
					}
					if(queryPolStatus != null&&!queryPolStatus.equals(""))
					{
						String userPolStatusStr = URLDecoder.decode(queryPolStatus, "UTF-8");
						list.add("%" + userPolStatusStr + "%");
						queryStr = queryStr + "and model.politicsStatus like ? ";
					}
				}
				String filePath = ExportUtil.ExportToExcel(queryStr + "order by model1.proTeamCode asc,model.jobNum asc", list, null, "formatExcel", "formatTitle", UserManager.class);				
				retObj7.put("IsOK", filePath.equals("")?false:true);
				retObj7.put("Path", filePath);
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in UserServlet-->case 10", e);
				}else{
					log.error("error in UserServlet-->case 10", e);
				}
				try {
					retObj7.put("IsOK", false);
					retObj7.put("Path", "");
				} catch (JSONException e1) {}
			}finally{
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write(retObj7.toString());
			}
			break;
		case 11://设置用户登录是否需要软件狗(单用户)
			JSONObject retJSON11 = new JSONObject();
			try{
				String Id = request.getParameter("id");
				SysUser user11 = userMgr.findById(Integer.valueOf(Id));
				if(user11.getNeedDongle()==null){
					user11.setNeedDongle(true);
				}
				else{
					user11.setNeedDongle(!user11.getNeedDongle());
				}
				boolean res11 = userMgr.update(user11);
				retJSON11.put("IsOK", res11);
				retJSON11.put("msg", res11?"设置成功！":"设置失败！");
			}catch(Exception e){
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in UserServlet-->case 11", e);
				}else{
					log.error("error in UserServlet-->case 11", e);
				}
				try {
					retJSON11.put("IsOK", false);
					retJSON11.put("msg", "");
				} catch (JSONException e1) {}
			}finally{
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write(retJSON11.toString());	
			}
			break;
		case 12://设置用户登录是否需要软件狗(所有用户)
			JSONObject retJSON12 = new JSONObject();
			try{
				String updateStr = "update SysUser as model set model.needDongle = ?";
				SysUser u = userMgr.findByVarProperty(new KeyValueWithOperator("jobNum", "000000", "=")).get(0);
				boolean a = u.getNeedDongle()==null?false:u.getNeedDongle();
				int res11 = userMgr.updateByHQL(updateStr, !a);
				retJSON12.put("IsOK", res11);
				retJSON12.put("msg", res11!=0?"设置成功！":"设置失败！");
			}catch(Exception e){
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in UserServlet-->case 12", e);
				}else{
					log.error("error in UserServlet-->case 12", e);
				}
				try {
					retJSON12.put("IsOK", false);
					retJSON12.put("msg", "");
				} catch (JSONException e1) {}
			}finally{
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write(retJSON12.toString());	
			}
			break;
		case 13: // 根据角色名和姓名查询是否授权
			JSONObject res2= new JSONObject();;	
			int total=0;
			try {
				String roleId = request.getParameter("roleId");
				String queryname = request.getParameter("queryname");
				if(roleId==null)
				{
					throw new Exception("角色为空");//角色为空
				}
				String userNameStr1="";
				if(queryname != null&&!queryname.equals(""))
				{
					userNameStr1 = URLDecoder.decode(queryname, "UTF-8");
				}
				UserRoleManager role_user_mag = new UserRoleManager();
				total=role_user_mag.getTotalCount(new KeyValueWithOperator("role.id",Integer.valueOf(URLDecoder.decode(roleId, "UTF-8")),"="),
						new KeyValueWithOperator("status", 0, "="),new KeyValueWithOperator("sysUser.name", "%" + userNameStr1 + "%", "like"));
				List<UserRole> role_users = role_user_mag.findByVarProperty(new KeyValueWithOperator("role.id",Integer.valueOf(URLDecoder.decode(roleId, "UTF-8")),"="),
						new KeyValueWithOperator("status", 0, "="),new KeyValueWithOperator("sysUser.name", "%" + userNameStr1 + "%", "like"));
				JSONArray options2 = new JSONArray();
				if(role_users== null||role_users.size()==0)
				{
					throw new Exception("查询结果为空");//查询结果为空
				}
				for (UserRole temp1:role_users) {
					JSONObject option = new JSONObject();
					option.put("userroleId", temp1.getId());
					option.put("roleId", temp1.getRole().getId());
					option.put("Id", temp1.getSysUser().getId());
					option.put("JobNum", temp1.getSysUser().getJobNum());  //用户工号
					option.put("Name", temp1.getSysUser().getName());//用户姓名
					option.put("userName", temp1.getSysUser().getUserName());//用户名
					option.put("JobTitle", temp1.getSysUser().getJobTitle());//行政职务
					options2.put(option);
				}
				res2.put("total", total);
				res2.put("rows", options2);
			} catch (Exception ex) {
				if(ex.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in UserServlet-->case 13", ex);
				}else{
					log.error("error in UserServlet-->case 13", ex);
				}
				try {
					res2.put("total", 0);
					res2.put("rows", new JSONArray());
				} catch (JSONException e1) {}
			}finally{
				response.setContentType("text/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(res2.toString());
			}
			break;
		case 14://导出个人或者全部权限信息
			
			JSONObject retObj14 = new JSONObject();
			try {
				String paramsStr14 = request.getParameter("paramsStr");
				List<JSONObject> result= new ArrayList<JSONObject>();
				if(paramsStr14==null||paramsStr14.length()==0){
					result= new RolePrivilegeManager().ExportPrivilegesByUserId(null);//导出全部用户名的权限
				}else{
					int userid=Integer.parseInt(paramsStr14);	
					result = new RolePrivilegeManager().ExportPrivilegesByUserId(userid);//导出个人的权限
				}
				String filePath = ExportUtil.ExportToExcelByResultSet(result,null, "formatExcel", "formatTitle", RolePrivilegeManager.class);				
				retObj14.put("IsOK", filePath.equals("")?false:true);
				retObj14.put("Path", filePath);
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in UserServlet-->case 10", e);
				}else{
					log.error("error in UserServlet-->case 10", e);
				}
				try {
					retObj14.put("IsOK", false);
					retObj14.put("Path", "");
				} catch (JSONException e1) {}
			}finally{
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write(retObj14.toString());
			}
			break;
		case 15: // 分页查询(GrantRole)中使用
			JSONObject res15 = new JSONObject();
			try {
				String queryname = request.getParameter("queryname");
				
				String queryStr = "from SysUser as model,ProjectTeam as pro where 1=1 and model.projectTeamId = pro.id and model.status = 0 ";
				List<Object> list = new ArrayList<Object>();
				if(queryname != null&&!queryname.equals(""))
				{
					String userNameStr15 = URLDecoder.decode(queryname, "UTF-8");
					
					list.add("%" + userNameStr15 + "%");
					list.add("%" + userNameStr15 + "%");
					queryStr = queryStr + "and (model.brief like ? or model.name like ?) ";
				}
				
				int page = 1;
				if (request.getParameter("page") != null)
					page = Integer.parseInt(request.getParameter("page").toString());
				int rows = 10;
				if (request.getParameter("rows") != null)
					rows = Integer.parseInt(request.getParameter("rows").toString());
				List<Object[]> result;
				int total15;
				List<JSONObject> resultP;
				StringBuilder priString;
				result = userMgr.findPageAllByHQL("select model,pro " + queryStr + " order by pro.proTeamCode asc,model.jobNum asc", page, rows, list);
				total15 = userMgr.getTotalCountByHQL("select count(model) "+queryStr, list);
				JSONArray options = new JSONArray();
					for (Object[] obj : result) {
						SysUser user15 = (SysUser)obj[0];
						ProjectTeam pro = (ProjectTeam)obj[1];
						JSONObject option = new JSONObject();
						option.put("Id", user15.getId());
						option.put("Name", user15.getName());
						option.put("Brief", user15.getBrief());
						option.put("userName", user15.getUserName());
						option.put("JobNum", user15.getJobNum());		
						option.put("IDNum", user15.getIdnum());						
						option.put("JobTitle", user15.getJobTitle());
						option.put("AdministrationPost", user15.getAdministrationPost());
						
						option.put("ProjectTeamId", user15.getProjectTeamId());
						option.put("ProjectTeamName", pro.getName());
						option.put("DepartmentId", pro.getDepartment().getId());
						option.put("Status", user15.getStatus());
						
						resultP = new ArrayList<JSONObject>();
						priString = new StringBuilder();
						resultP = new RolePrivilegeManager().ExportAlonePrivilegesByUserId(user15.getId());//导出个人的权限
						for(int j = 0;j<resultP.size();j++)
						{							
							JSONObject objpri = (JSONObject)resultP.get(j);
							String RoleName = objpri.getString("RoleName");
							if(!priString.toString().contains(RoleName+";")){
								priString.append(objpri.getString("RoleName")).append(";");
							}
							
						}
						option.put("UserRoles",priString.toString());
						
						options.put(option);
					}
					res15.put("total", total15);
					res15.put("rows", options);
				} catch (Exception e) {
					try {
						res15.put("total", 0);
						res15.put("rows", new JSONArray());
					} catch (JSONException ex) {
						ex.printStackTrace();
					}
					if(e.getClass() == java.lang.Exception.class){ //自定义的消息
						log.debug("exception in UserServlet-->case 15", e);
					}else{
						log.error("error in UserServlet-->case 15", e);
					} 

				}finally{
					response.setContentType("text/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(res15.toString());
					//System.out.println(res.toString());
				}
			break;
		case 16:	//前端控件中输入用户名AutoComplete查询符合条件的用户列表（默认前三十个）,查询统计中使用，包括已注销人员
			String userNameStr16 = request.getParameter("QueryName");	//查询的用户姓名
			JSONArray jsonArray16 = new JSONArray();
			try {
				if(userNameStr16 != null && userNameStr16.trim().length() > 0){
					String cusName =  new String(userNameStr16.trim().getBytes("ISO-8859-1"), "GBK");	//解决URL传递中文乱码问题
					//cusName = LetterUtil.String2Alpha(cusName);	//返回字母简码
					String queryStr = "from SysUser model where model.brief like ? or model.name like ? or model.jobNum like ?";
					List<SysUser> retList = userMgr.findPageAllByHQL(queryStr, 1, 30, "%" + cusName + "%", "%" + cusName + "%", "%" + cusName + "%");
					if(retList != null){
						for(SysUser temp : retList){
							JSONObject jsonObj = new JSONObject();
							jsonObj.put("jobNum", temp.getJobNum());
							jsonObj.put("name", temp.getName());
							jsonObj.put("id", temp.getId());
							jsonArray16.put(jsonObj);	
						}
					}
				}
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){ //自定义的消息
					log.debug("exception in UserServlet-->case 16", e);
				}else{
					log.error("error in UserServlet-->case 16", e);
				} 
			}finally{
				response.setContentType("text/json;charset=gbk");
				response.getWriter().write(jsonArray16.toString());
			}
			break;
		}
		
		
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}
	
	
	private SysUser initUser(HttpServletRequest req, int id) {
		SysUser user;
		if(id==0)
		{
			user = new SysUser();
			user.setSignature(UIDUtil.get22BitUID());
			user.setPhotograph(UIDUtil.get22BitUID());
		}
		else
		{
			UserManager um = new UserManager();
			user = um.findById(id);
			if(user==null)
				return null;
		}
				
		String Name = req.getParameter("Name");
		String Brief = req.getParameter("Brief");
		String userName =  req.getParameter("userName");
		String JobNum = req.getParameter("JobNum");
		String WorkLocation = req.getParameter("WorkLocation");
		String Gender = req.getParameter("Gender");
		String Birthplace = req.getParameter("Birthplace");
		Date Birthday = Date.valueOf(req.getParameter("Birthday"));
		String IDNum = req.getParameter("IDNum");
		String PoliticsStatus = req.getParameter("PoliticsStatus");
		String Nation = req.getParameter("Nation");
		String Type = req.getParameter("Type");
		Date WorkSince = Date.valueOf(req.getParameter("WorkSince"));
		Date WorkHereSince = Date.valueOf(req.getParameter("WorkHereSince"));
		String JobTitle = req.getParameter("JobTitle");
		String Education = req.getParameter("Education");
		String EducationDate = req.getParameter("EducationDate");
		String EducationFrom = req.getParameter("EducationFrom");
		String Degree = req.getParameter("Degree");
		String DegreeDate = req.getParameter("DegreeDate");
		String DegreeFrom = req.getParameter("DegreeFrom");
		String Specialty = req.getParameter("Specialty");
		String AdministrationPost = req.getParameter("AdministrationPost");
		String PartyPost = req.getParameter("PartyPost");
		if(req.getParameter("PartyDate")!=null&&!req.getParameter("PartyDate").trim().toString().equals(""))
		{
			Date PartyDate = Date.valueOf(req.getParameter("PartyDate"));
			user.setPartyDate(PartyDate);
		}
		String HomeAdd = req.getParameter("HomeAdd");
		String WorkAdd = req.getParameter("WorkAdd");
		String Tel = req.getParameter("Tel");
		String Cellphone1 = req.getParameter("Cellphone1");
		String Cellphone2 = req.getParameter("Cellphone2");
		String Email = req.getParameter("Email");
		int ProjectTeamId = Integer.valueOf(req.getParameter("ProjectTeamId"));
		int Status = Integer.valueOf(req.getParameter("Status"));
		if(user.getStatus()!=null&&user.getStatus()==1&&Status==0)
		{
			user.setStatus(Status);
			user.setCancelDate(null);
		}
		else
			user.setStatus(Status);
		String Remark = req.getParameter("Remark");				
		
		user.setName(Name);
		user.setBrief(Brief);
		user.setJobNum(JobNum);
		user.setWorkLocationId(Integer.valueOf(WorkLocation));
		user.setUserName(id==0?userName:user.getUserName());
		user.setGender(Integer.valueOf(Gender).equals(0));
		user.setBirthplace(Birthplace);
		user.setBirthday(Birthday);
		user.setIdnum(IDNum);
		user.setPoliticsStatus(PoliticsStatus);
		user.setNation(Nation);
		user.setType(Type);
		user.setWorkSince(WorkSince);
		user.setWorkHereSince(WorkHereSince);
		user.setJobTitle(JobTitle);
		user.setEducation(Education);
		user.setEducationDate(EducationDate);
		user.setEducationFrom(EducationFrom);
		user.setDegree(Degree);
		user.setDegreeDate(DegreeDate);
		user.setDegreeFrom(DegreeFrom);
		user.setPartyPost(PartyPost);
		user.setSpecialty(Specialty);
		user.setAdministrationPost(AdministrationPost);
		user.setHomeAdd(HomeAdd);
		user.setWorkAdd(WorkAdd);
		user.setTel(Tel);
		user.setCellphone1(Cellphone1);
		user.setCellphone2(Cellphone2);
		user.setEmail(Email);
		user.setProjectTeamId(ProjectTeamId);
		user.setStatus(Status);
		user.setRemark(Remark);
		
		return user;
	}

}
