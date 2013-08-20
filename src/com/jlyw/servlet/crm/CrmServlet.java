package com.jlyw.servlet.crm;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Struct;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.formula.functions.IfFunc;
import org.apache.poi.ss.usermodel.DateUtil;
import org.hibernate.Transaction;
import org.hibernate.hql.ast.SqlASTFactory;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import sun.java2d.loops.ProcessPath.EndSubPathHandler;

import com.jlyw.hibernate.ApplianceSpecies;
import com.jlyw.hibernate.ApplianceStandardName;
import com.jlyw.hibernate.BaseHibernateDAO;
import com.jlyw.hibernate.BaseType;
import com.jlyw.hibernate.CommissionSheet;
import com.jlyw.hibernate.Customer;
import com.jlyw.hibernate.CustomerContactor;
import com.jlyw.hibernate.CustomerDAO;
import com.jlyw.hibernate.Reason;
import com.jlyw.hibernate.Region;
import com.jlyw.hibernate.SubContract;
import com.jlyw.hibernate.SysUser;
import com.jlyw.hibernate.crm.CustomerCareness;
import com.jlyw.hibernate.crm.CustomerFeedback;
import com.jlyw.hibernate.crm.CustomerServiceFee;
import com.jlyw.hibernate.crm.InsideContactor;
import com.jlyw.hibernate.crm.InsideContactorFeeAssign;
import com.jlyw.hibernate.crm.MyParameters;
import com.jlyw.hibernate.crm.PotentialCustomer;
import com.jlyw.hibernate.crm.Warning;
import com.jlyw.manager.ApplianceSpeciesManager;
import com.jlyw.manager.ApplianceStandardNameManager;
import com.jlyw.manager.BaseTypeManager;
import com.jlyw.manager.CertificateFeeAssignManager;
import com.jlyw.manager.CommissionSheetManager;
import com.jlyw.manager.CustomerContactorManager;
import com.jlyw.manager.CustomerManager;
import com.jlyw.manager.MyClass;
import com.jlyw.manager.ReasonManager;
import com.jlyw.manager.RegionManager;
import com.jlyw.manager.SubContractManager;
import com.jlyw.manager.UserManager;
import com.jlyw.manager.crm.CustomerCarenessManager;
import com.jlyw.manager.crm.CustomerServiceFeeManager;
import com.jlyw.manager.crm.FeedbackManager;
import com.jlyw.manager.crm.InsideContactorFeeAssignManager;
import com.jlyw.manager.crm.InsideContactorManager;
import com.jlyw.manager.crm.PotentialCustomerManager;
import com.jlyw.manager.crm.Sql;
import com.jlyw.servlet.CustomerServlet;
import com.jlyw.util.DateTimeFormatUtil;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.LetterUtil;
import com.jspsmart.upload.Request;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.star.lib.uno.environments.remote.remote_environment;


public class CrmServlet extends HttpServlet {
	private static final Log log = LogFactory.getLog(CustomerServlet.class);
	/**
	 * Constructor of the object.
	 */

	public CrmServlet() {
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
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(",sxxcccac using the GET method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
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
	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JSONObject retJsonObject=new JSONObject();
		FeedbackManager fbm=new FeedbackManager();
		int method=Integer.parseInt(request.getParameter("method"));
		switch(method){
		case 0://新建反馈信息
		try 
		{
			String customerName=request.getParameter("CustomerName");
			///String complainAbout=request.getParameter("ComplainAbout");
			String complainer=request.getParameter("Complainer");
			//String handleLevel=request.getParameter("HandleLevel");
			//String status=request.getParameter("Status");
			//String planStartTime=request.getParameter("PlanStartTime");
			//String planEndTime=request.getParameter("PlanEndTime");
			String customerRequiredTime=request.getParameter("CustomerRequiredTime");
			String feedback=request.getParameter("Feedback");
			
			CustomerFeedback cf=new CustomerFeedback();
			CustomerManager custm=new CustomerManager();
			List<Customer> l=custm.findByVarProperty(new KeyValueWithOperator("id",Integer.parseInt(customerName) , "="));
			
			if(l!=null&&l.size()==1)//数据库中找到这个单位
			{
				cf.setCustomer(l.get(0));
				//cf.setComplainAbout(Integer.parseInt(complainAbout));
				cf.setCreateTime(new Timestamp(System.currentTimeMillis()));
				cf.setCustomerContactorName(complainer);
				cf.setFeedback(feedback);
				cf.setStatus(1);//1表示未开始
				/*cf.setHandleLevel(Integer.parseInt(handleLevel));
				if(planStartTime!="")
					cf.setPlanStartTime(new Timestamp(DateTimeFormatUtil.DateFormat.parse(planStartTime).getTime()));
				if(planEndTime!="")
					cf.setPlanEndTime(new Timestamp(DateTimeFormatUtil.DateFormat.parse(planEndTime).getTime()));*/
				if(customerRequiredTime!="")
					cf.setCustomerRequiredTime(new Timestamp(DateTimeFormatUtil.DateFormat.parse(customerRequiredTime).getTime()));
				UserManager um=new UserManager();
				int curId=((SysUser)(request.getSession().getAttribute("LOGIN_USER"))).getId();
				
				List <SysUser> r=um.findByVarProperty(new KeyValueWithOperator("id",curId,"="));
				if(r!=null&&r.size()==1)
				{
					cf.setSysUserByCreateSysUserId(r.get(0));
					if(fbm.save(cf))
					{
					retJsonObject.put("IsOk", true);
					retJsonObject.put("msg", "添加成功！");
					}
					else 
					{
						retJsonObject.put("IsOk", false);
						retJsonObject.put("msg", "添加失败！");
					}
				}
				else 
				{
					retJsonObject.put("IsOk", false);
					retJsonObject.put("msg", "未知错误！");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception

			if(e.getClass() == java.lang.Exception.class){	//自定义的消息
				log.debug("exception in CrmServlet-->case 0", e);
			}else{
				log.error("error in CrmServlet-->case 0", e);
			}
		} 
		finally
		{
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(retJsonObject.toString());
		}
		 break;
		case 1://查询反馈信息
			JSONObject res=new JSONObject();
			try {
					String customerName=request.getParameter("CustomerName");
					String startTime=request.getParameter("StartDate");
					String endTime=request.getParameter("EndDate");
					String status=request.getParameter("Status");
					String complainAbout=request.getParameter("ComplainAbout");
					CustomerManager cusm=new CustomerManager();
					List<Customer> lresult;
					List <Object> keys=new ArrayList<Object>();
					int page=1;
					if((request.getParameter("page"))!=null)
						page=Integer.parseInt(request.getParameter("page").toString());
					int rows = 10;
					if (request.getParameter("rows") != null)
						rows = Integer.parseInt(request.getParameter("rows").toString());
					String queryStr="from CustomerFeedback as model where 1=1 ";//处理过程1：只需要看到未开始的1...
					
					if(status!=null&&!status.equals(""))
					{
						String cnstr=URLDecoder.decode(status,"UTF-8");
						
						queryStr+=" and (model.status = ?)";
						keys.add(Integer.valueOf(cnstr));
			
					}
					
					if(customerName!=null&&!customerName.equals(""))
					{
						String cnstr=URLDecoder.decode(customerName,"UTF-8");
						lresult=cusm.findByVarProperty(new KeyValueWithOperator("name",cnstr,"="));
						if(lresult!=null&&lresult.size()==1)
						{
							queryStr+=" and (model.customer.id = ?)";
							keys.add(Integer.valueOf(lresult.get(0).getId()));
						}
					}
					if(startTime!=null&&!startTime.equals(""))
					{
						String str=URLDecoder.decode(startTime,"UTF-8");
						queryStr+=" and (convert(varchar(10),model.createTime,120)>= ?)";//model.createTime
						keys.add(str);
					}
					if(endTime!=null&&!endTime.equals(""))
					{
						String str=URLDecoder.decode(endTime,"UTF-8");
						queryStr+=" and (convert(varchar(10),model.createTime,120)<= ?)";//
						keys.add(str);
					}
					if(complainAbout!=null&&!complainAbout.equals(""))
					{
						String str=URLDecoder.decode(complainAbout,"UTF-8");
						queryStr+=" and (complainAbout = ?)";//
						keys.add(Integer.valueOf(str));
					}
					
						FeedbackManager fbm1=new FeedbackManager();
						//queryStr="from CustomerFeedback as model where model.customer.id = ?";
						
						//+" order by model.createTime asc"
						List<CustomerFeedback> cl=fbm1.findPageAllByHQL(queryStr+" order by model.createTime asc", page, rows, keys);
						JSONArray rets=new JSONArray();
						for(CustomerFeedback a:cl)
						{
							JSONObject ret=new JSONObject();
							ret.put("ActulEndTime", a.getActulEndTime()==null?"":DateFormat.getDateInstance().format(a.getActulEndTime()));
							ret.put("ActulStartTime", a.getActulStartTime()==null?"":DateFormat.getDateInstance().format(a.getActulStartTime()));
							ret.put("Analysis", a.getAnalysis()==null?"":a.getAnalysis());
							
							ret.put("ComplainAbout", a.getComplainAbout());
							ret.put("CreateTime", (new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(a.getCreateTime()));
							ret.put("CustomerName",a.getCustomer().getName());
							ret.put("CustomerContactorName", a.getCustomerContactorName());
							ret.put("CustomerRequiredTime", a.getCustomerRequiredTime()==null?"":DateFormat.getDateInstance().format(a.getCustomerRequiredTime()));
							ret.put("Feedback",a.getFeedback());
							ret.put("HandleLevel", a.getHandleLevel());
							ret.put("Id",a.getId());
							ret.put("Mark", a.getMark()==null?"":a.getMark());
							ret.put( "Method", a.getMethod());
							
							ret.put("PlanEndTime", a.getPlanEndTime()==null?"":DateFormat.getDateInstance().format(a.getPlanEndTime()));
							ret.put("PlanStartTime", a.getPlanStartTime()==null?"":DateFormat.getDateInstance().format(a.getPlanStartTime()));
							
							ret.put("Remark", a.getRemark()==null?"":a.getRemark());
							ret.put("ReturnVisitInfo", a.getReturnVisitInfo()==null?"":a.getReturnVisitInfo());
							ret.put("ReturnVisitType", a.getReturnVisitType()==null?"":a.getReturnVisitType());
							ret.put("Status", a.getStatus());
							ret.put("CreateSysUserName", a.getSysUserByCreateSysUserId().getName());
							SysUser sys=a.getSysUserByHandleSysUserId();
							if(sys!=null)
							{
								ret.put("HandleMan",sys.getName());
								ret.put("JobNum",sys.getJobNum());
							}else{
								ret.put("HandleMan","");
								ret.put("JobNum","");
							}
							
							rets.put(ret);
						}
						res.put("rows", rets);
			} catch (Exception e) 
			{
				// TODO: handle exception
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CrmServlet-->case 1", e);
				}else{
					log.error("error in CrmServlet-->case 1", e);
				}
			}
			finally
			{
				response.setContentType("text/json;charset=utf-8");
				response.getWriter().write(res.toString());
			}
			break;
		case 2://领导指定责任人，计划时间等
			JSONObject r=new JSONObject();
			try 
			{
//				String handleMan=request.getParameter("HandleMan");
				String meth=request.getParameter("Method");
				//String status=request.getParameter("Status2");
				String feedbackId=request.getParameter("FeedbackId");
				String id=request.getParameter("Id");//员工ID
				

				String planStartTime=request.getParameter("PlanStartTime");
				String planEndTime=request.getParameter("PlanEndTime");
				
				FeedbackManager fbm2=new FeedbackManager();
				CustomerFeedback cusf;//=new CustomerFeedback();
				cusf=fbm2.FindById(Integer.parseInt(feedbackId));
				if(cusf!=null)
				{
					if(planStartTime!="")
						cusf.setPlanStartTime(new Timestamp(DateTimeFormatUtil.DateFormat.parse(planStartTime).getTime()));
					if(planEndTime!="")
						cusf.setPlanEndTime(new Timestamp(DateTimeFormatUtil.DateFormat.parse(planEndTime).getTime()));
					cusf.setMethod(meth);
					cusf.setStatus(2);//2表示进行中
					
					SysUser s=new SysUser();
					s.setId(Integer.parseInt(id));
	
					cusf.setSysUserByHandleSysUserId(s);
					cusf.setId(Integer.parseInt(feedbackId));
					if(fbm.update(cusf))
					{
						r.put("IsOk", true);
						r.put("msg", "任务分配成功！");
					}
					else
					{
						r.put("IsOk", false);
						r.put("msg", "任务分配失败！");
					}
				}
			} catch (Exception e) 
			{
				// TODO: handle exception
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CrmServlet-->case 2", e);
				}else{
					log.error("error in CrmServlet-->case 2", e);
				}
			}
			finally
			{
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write(r.toString());
			}
			break;
		case 3://输入处理结果,已处理
			JSONObject rObj=new JSONObject();
			try 
			{
				//String returnType=request.getParameter("ReturnVisitType");
				//String returnInfo=request.getParameter("ReturnVisitInfo");
				String Remark=request.getParameter("Remark");//处理结果
				String ActualEndTime=request.getParameter("ActulEndTime");
				String mark=request.getParameter("Mark");
				String feedbackId=request.getParameter("FeedbackId");
				//FeedbackManager fbm=new FeedbackManager();
				CustomerFeedback cus=fbm.FindById(Integer.parseInt(feedbackId));
				if(cus!=null)
				{
					//cus.setReturnVisitType(Integer.parseInt(returnType));
					if(Remark!=null&&!Remark.trim().equals(""))cus.setRemark(Remark);
					cus.setActulEndTime(new Timestamp(DateTimeFormatUtil.DateFormat.parse(ActualEndTime).getTime()));
					cus.setMark(Integer.parseInt(mark));
					cus.setId(Integer.parseInt(feedbackId));
					cus.setStatus(3);//已处理
					if(fbm.update(cus))
					{
						rObj.put("IsOk", true);
						rObj.put("msg", "提交成功！");
						
					}
					else {
						rObj.put("IsOk", false);
						rObj.put("msg", "提交失败！");
					}
				}
			} catch (Exception e) 
			{
				// TODO: handle exception
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CrmServlet-->case 3", e);
				}else{
					log.error("error in CrmServlet-->case 3", e);
				}
			}
			finally
			{
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write(rObj.toString());
			}
			break;
		case 4://服务费用信息
			JSONObject jObject=new JSONObject();
		try 
		{
			String BillNum=request.getParameter("BillNum");
			int CustomerId=Integer.parseInt(request.getParameter("CustomerId"));
			int createSysId=((SysUser)(request.getSession().getAttribute("LOGIN_USER"))).getId();
			Timestamp createTime=new Timestamp(System.currentTimeMillis());
			Timestamp paidDate=new Timestamp(DateTimeFormatUtil.DateFormat.parse(request.getParameter("PaidDate")).getTime());
			String paidVia=request.getParameter("PaidVia");
			String paidSubject=request.getParameter("PaidSubject");
			String money=request.getParameter("Money");
			int paidSysUserId=Integer.parseInt(request.getParameter("PaidManId"));
			String remark=request.getParameter("Remark");
			String status=request.getParameter("Status");
//			String attachmentString;
			CustomerServiceFeeManager csfm=new CustomerServiceFeeManager();
			CustomerServiceFee csf=new CustomerServiceFee();
			
			csf.setBillNum(BillNum);
			csf.setCreateTime(createTime);
			csf.setMoney(Double.valueOf(money));
			csf.setPaidSubject(paidSubject);
			csf.setPaidTime(paidDate);
			csf.setPaidVia(Integer.parseInt(paidVia));
			csf.setRemark(remark);
			csf.setStatus(Integer.parseInt(status));
			//csfi.setAttachment(attachmentString);

			//CustomerManager custm=new CustomerManager();
			Customer c1=new Customer();
			c1.setId(CustomerId);
			SysUser s1=new SysUser();
			s1.setId(createSysId);
			SysUser s2=new SysUser();
			s2.setId(paidSysUserId);
			//Customer c1=custm.findById(CustomerId);
			//UserManager u1=new UserManager();
			//UserManager u2=new UserManager();
			//SysUser s1=u1.findById(createSysId);
			//SysUser s2=u2.findById(paidSysUserId);
			//if(s1!=null&&s2!=null&&c1!=null)
			{
				csf.setCustomer(c1);
				csf.setSysUserByCreateSysUserId(s1);
				csf.setSysUserByPaidSysUserId(s2);
				
				if(csfm.save(csf))
				{
					jObject.put("IsOk", true);
					jObject.put("msg", "添加成功！");
				}
				else 
				{
					jObject.put("IsOk", false);
					jObject.put("msg", "添加失败！");
				}
			}
		} 
		catch (Exception e) 
		{
			// TODO: handle exception
			if(e.getClass() == java.lang.Exception.class){	//自定义的消息
				log.debug("exception in CrmServlet-->case 4", e);
			}else{
				log.error("error in CrmServlet-->case 4", e);
			}
		}
		finally
		{
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(jObject.toString());
		}break;
		case 5://前端控件选中某个客户后，根据客户ID查询该客户的联系人
			//JSONObject retobj=new JSONObject();
			JSONArray jsa=new JSONArray();
			try 
			{
				String customerId=request.getParameter("CustomerId");
				CustomerContactorManager ccm=new CustomerContactorManager();
				List<CustomerContactor> lresult=ccm.findByVarProperty(new KeyValueWithOperator("id",Integer.parseInt(customerId),"="));
				if(lresult!=null)
				{
					for(CustomerContactor a:lresult)
					{
						JSONObject j=new JSONObject();
						j.put("name", a.getName());
						j.put("id", a.getId());
						jsa.put(j);
					}					
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CrmServlet-->case 5", e);
				}else{
					log.error("error in CrmServlet-->case 5", e);
				}
			}
			finally
			{
				response.setContentType("text/json;charset=utf-8");
				response.getWriter().write(jsa.toString());
			}
			break;
		case 6://关怀信息
			JSONObject ret=new JSONObject();
			try 
			{
				int customerId=Integer.parseInt(request.getParameter("CustomerId"));
				String customerName=request.getParameter("CustomerName");
//				int prior=Integer.parseInt(request.getParameter("Priority"));
				float fee = Float.parseFloat(request.getParameter("Fee"));
				Timestamp createTime=new Timestamp(System.currentTimeMillis());
				int createUserId=((SysUser)(request.getSession().getAttribute("LOGIN_USER"))).getId();
//				int status=Integer.parseInt(request.getParameter("Status"));
				Timestamp time=new Timestamp(DateTimeFormatUtil.DateFormat.parse(request.getParameter("Time")).getTime());
				int way=Integer.parseInt(request.getParameter("Way"));
				String careContactor=request.getParameter("CareContactor");
				
				String careDutyManId=request.getParameter("CareDutyManId");
				String representative=request.getParameter("RepresentativeId");
				String remark=request.getParameter("Remark");
				
				CustomerCarenessManager ccm=new CustomerCarenessManager();
				CustomerCareness cc=new CustomerCareness();
				Customer cust=new Customer();
				cust.setId(customerId);
				cc.setCustomer(cust);
				cc.setCustomerName(customerName);
				cc.setPriority(1);
				cc.setCreateTime(createTime);
				cc.setFee(fee);
				SysUser s=new SysUser();
				s.setId(createUserId);
				cc.setSysUserByCreateSysUserId(s);
				cc.setStatus(1);
				cc.setTime(time);
				cc.setWay(way);
				cc.setCareContactor(careContactor);
				if(careDutyManId!=null&&!careDutyManId.equals(""))
				{
					int careDutyId=Integer.parseInt(careDutyManId);
					s.setId(careDutyId);
					cc.setSysUserByCareDutySysUserId(s);
				}
				/**/
				if(representative!=null&&!representative.equals(""))
				{
					int representativeId=Integer.parseInt(representative);
					s.setId(representativeId);
					cc.setSysUserByRepresentativeId(s);
				}
				if(remark!=null&&!remark.equals(""))
				{
					cc.setRemark(remark);
				}
				if(ccm.save(cc))
				{
					ret.put("IsOK", true);
					ret.put("msg", "添加成功！");
				}
				else
				{
					ret.put("IsOk","false");
					ret.put("msg", "添加失败！");
				}
			} 
			catch (Exception e) 
			{
				// TODO: handle exception
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CrmServlet-->case 6", e);
				}else{
					log.error("error in CrmServlet-->case 6", e);
				}
			}
			finally
			{
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write(ret.toString());
			}
			break;
		case 7://新建内部联系人信息
			JSONObject retj=new JSONObject();
			try 
			{
				int customerId=Integer.parseInt(request.getParameter("CustomerId"));
				int Role=Integer.parseInt(request.getParameter("Role"));
				int sysId=Integer.parseInt(request.getParameter("InsideContactorId_0"));
				InsideContactor ic=new InsideContactor();
				SysUser s=new SysUser();
				s.setId(sysId);
				Customer c=new Customer();
				c.setId(customerId);
				ic.setCustomer(c);
				ic.setSysUser(s);
				ic.setRole(Role);
				
				InsideContactorManager icm=new InsideContactorManager();
				List<InsideContactor> lr=icm.findByVarProperty(new KeyValueWithOperator("customer.id",customerId,"="));
				boolean flag=true;
				if(lr.size()!=0&&lr.size()!=5)
					for(InsideContactor a:lr)
					{
						if(a.getSysUser().getId()==sysId)
							{
							retj.put("IsOk", false);
							retj.put("msg", "已存在该联系人！");
							flag=false;
							}
					}
				if(flag==true&&lr.size()<5)
				{
					
					if(icm.save(ic))
					{
						retj.put("IsOk", true);
						retj.put("msg", "添加成功！");
					}
					else 
					{
							retj.put("IsOk", false);
							retj.put("msg", "添加失败！");
					}
				}
				else if(/*flag==false&&*/lr.size()==5)
				{
					retj.put("IsOk", false);
					retj.put("msg", "联系人不能超过5个，请到修改页面操作！");
				}
			} 
			catch (Exception e) 
			{
				// TODO: handle exception
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CrmServlet-->case 7", e);
				}else{
					log.error("error in CrmServlet-->case 7", e);
				}
			}
			finally
			{
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write(retj.toString());
			}break;
		case 8://查询内部联系人列表
			JSONObject j=new JSONObject();
			JSONArray jtmp=new JSONArray();
			try {
				String cusid=request.getParameter("CustomerId");
				int page = 1;
				if (request.getParameter("page") != null)
					page = Integer.parseInt(request.getParameter("page").toString());
				int rows = 10;
				if (request.getParameter("rows") != null)
					rows = Integer.parseInt(request.getParameter("rows").toString());
				
				String queryString="select a.name,a.address,c.name,c.jobNum,c.cellphone1,c.id,b.id ,b.role " +
						"from Customer as a,InsideContactor as b,SysUser as c " +
						"where a.id=b.customer.id and b.sysUser.id=c.id ";
				List<Object> keys = new ArrayList<Object>();
				if(cusid!=null&&!cusid.equals(""))
				{
					queryString+=" and a.id = ?";
					keys.add(Integer.valueOf(cusid));
				}
				InsideContactorManager icm=new InsideContactorManager();
				List<Object[]> lo=icm.findPageAllByHQL(queryString, page, rows, keys);
				int to=icm.getTotalCountByHQL1(queryString, keys);
				
					for(Object[] a:lo)
					{
						JSONObject tmp =new JSONObject();
						tmp.put("CustomerName",a[0].toString());
						tmp.put("Address", a[1].toString());
						tmp.put("ContactorName", a[2].toString());
						tmp.put("JobNum", a[3].toString());
						tmp.put("Cellphone1",a[4].toString());
						//tmp.put("Cellphone1",a[4].toString());
						tmp.put("ContactorId",a[5].toString()/*.toString()*/);
						tmp.put("Id", a[6].toString());
						tmp.put("Role", a[7]==null?"":a[7].toString());
						jtmp.put(tmp);
					}
					j.put("total", to);
					j.put("rows", jtmp);	
			} catch (Exception e) {
				// TODO: handle exception
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CrmServlet-->case 8", e);
				}else{
					log.error("error in CrmServlet-->case 8", e);
				}
			}
			finally
			{
				response.setContentType("text/json;charset=utf-8");
				response.getWriter().write(j.toString());
			}break;
		case 9://修改内部单位-联系人匹配关系
			JSONObject jo=new JSONObject();
			try 
			{
				int Id=Integer.parseInt(request.getParameter("Id"));
				String Role=request.getParameter("Role");
				int contactorId=Integer.parseInt(request.getParameter("ContactorId"));
				InsideContactorManager icm=new InsideContactorManager();
				InsideContactor ic=icm.FindById(Id);
				if(ic!=null)
				{
					SysUser s=new SysUser();
					s.setId(contactorId);
					int id=ic.getCustomer().getId();
					List<InsideContactor> li=icm.findByVarProperty(new KeyValueWithOperator("customer.id",id,"="),new KeyValueWithOperator("sysUser.id",contactorId,"="),new KeyValueWithOperator("id",Id,"<>"));
					boolean flag=true;
					if(li!=null&&li.size()>0)
					{
						for(InsideContactor b:li)
						{
							if(b.getSysUser().getId()==contactorId)
							{
								flag=false;
								jo.put("IsOk", false);
								jo.put("msg","修改失败！该公司已存在该联系人！");
							}
						}
					}
					ic.setSysUser(s);
					if(Role!=null&&!Role.trim().equals(""))ic.setRole(Integer.parseInt(Role));
					if(flag==true&&icm.save(ic))
					{
						jo.put("IsOk", true);
						jo.put("msg","修改成功！");
					}
					else if(flag==true)
					{
						jo.put("IsOk", false);
						jo.put("msg","修改失败！");
					}
				}
			} 
			catch (Exception e) 
			{
				// TODO: handle exception
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CrmServlet-->case 9", e);
				}else{
					log.error("error in CrmServlet-->case 9", e);
				}
			}
			finally
			{
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write(jo.toString());
			}break;
		case 10:
			JSONObject retjo=new JSONObject();
			try 
			{
				int del_id=Integer.parseInt(request.getParameter("Del_id"));
				InsideContactorManager icm=new InsideContactorManager();
				if(icm.deleteById(del_id))
				{
					retjo.put("IsOk", true);
					retjo.put("msg","删除成功！");
				}
				else 
				{
					retjo.put("IsOk", false);
					retjo.put("msg","删除失败！");
				}
			} 
			catch (Exception e) 
			{
				// TODO: handle exception
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CrmServlet-->case 10", e);
				}else{
					log.error("error in CrmServlet-->case 10", e);
				}
			}
			finally
			{
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write(retjo.toString());
			
			}break;
		case 11:
			JSONArray retObj2 = new JSONArray();
			try{
				BaseTypeManager baseTypeMgr = new BaseTypeManager();
				String Type = "28";//request.getParameter("Type");
				int page = 1;
				if (request.getParameter("page") != null)
					page = Integer.parseInt(request.getParameter("page").toString());
				int rows = 10;
				if (request.getParameter("rows") != null)
					rows = Integer.parseInt(request.getParameter("rows").toString());
				List<BaseType> result = baseTypeMgr.findPagedAll(page, rows, new KeyValueWithOperator("type", Integer.valueOf(Type), "="));
				//int total = baseTypeMgr.getTotalCount(new KeyValueWithOperator("type", Integer.valueOf(Type), "="));
				//JSONArray options = new JSONArray();
				if(result!=null&&result.size()>0){
					for(BaseType temp: result){
						JSONObject option = new JSONObject();
						option.put("Id", temp.getId());
						option.put("Name", temp.getName());
						//option.put("Brief", temp.getBrief());
						//option.put("Sequence", temp.getSequence());
						option.put("Type", temp.getType());
						//option.put("Status", temp.getStatus());
						//option.put("Remark", temp.getRemark());
						
						retObj2.put(option);
					}
				}
				//retObj2.put("total", total);
				//retObj2.put("rows", options);
			}catch(Exception e){
				//try {
					//retObj2.put("total", 0);
					//retObj2.put("rows", new JSONArray());
				//} catch (JSONException e1) {}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CrmServlet-->case 11", e);
				}else{
					log.error("error in CrmServlet-->case 11", e);
				}
			}finally{
				response.setContentType("text/json;charset=utf-8");
				response.getWriter().write(retObj2.toString());
			}
			break;
		case 12://添加潜在客户信息
			JSONObject retObj3=new JSONObject();
			try 
			{
				String name=request.getParameter("Name");
				String brief=request.getParameter("Brief");
				String nameEn=request.getParameter("NameEn");
				String facilitatingAgency=request.getParameter("FacilitatingAgency");
				int regionId=Integer.parseInt(request.getParameter("RegionId"));
				int from=Integer.parseInt(request.getParameter("From"));
				String address=request.getParameter("Address");
				int intension=Integer.parseInt(request.getParameter("CooperationIntension"));
				int industry=Integer.parseInt(request.getParameter("IndustryId"));
				//int status=Integer.parseInt(request.getParameter("Status"));
				
				PotentialCustomerManager pcm=new PotentialCustomerManager();
				PotentialCustomer pc=new PotentialCustomer();
				pc.setAddress(address);
				pc.setBrief(brief);
				pc.setCooperationIntension(intension);
				pc.setIndustry(industry);
				pc.setName(name);
				pc.setNameEn(nameEn);
				pc.setPotentialCustomerFrom(from);
				Region re=new Region();
				re.setId(regionId);
				pc.setRegion(re);
				//pc.setStatus(status);
				if(facilitatingAgency!=null&&!facilitatingAgency.equals(""));
				if(pcm.save(pc))
				{
					retObj3.put("IsOk", true);
					retObj3.put("msg", "添加成功！");
					
				}
				else 
				{
					retObj3.put("IsOk", false);
					retObj3.put("msg", "添加失败！");
				}
				
				
				
				
			} 
			catch (Exception e) {
				// TODO: handle exception
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CrmServlet-->case 12", e);
				}else{
					log.error("error in CrmServlet-->case 12", e);
				}
			}
			finally{
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write(retObj3.toString());
			}break;
		case 13:
			//查询潜在客户信息：Combobox（默认最多显示符合条件的前30条记录）:录入委托单界面中使用
			JSONArray jsonArray = new JSONArray();
			try {
				PotentialCustomerManager cusmag = new PotentialCustomerManager();
				String cusNameStr = request.getParameter("CustomerName");
				if(cusNameStr != null && cusNameStr.trim().length() > 0){
					String cusName =  new String(cusNameStr.trim().getBytes("ISO-8859-1"), "GBK");	//解决URL传递中文乱码问题
					
					cusName = LetterUtil.String2Alpha(cusName);	//转换成拼音简码
					String[] queryName = cusName.split(" \\s*");	//根据空格符分割
					if(queryName.length == 0){
						return;
					}
					cusName = "";
					for(int i = 0; i < queryName.length; i++){
						cusName += queryName[i];
						if(i != queryName.length-1)
							cusName += "%";
					}
					
					cusName = "%" + cusName + "%";
					String queryString = String.format("select model.name,model.address,model.region.id,model.region.name,model.id from PotentialCustomer as model where model.brief like ? or model.name like ? ");
					List<Object[]> retList = cusmag.findPageAllByHQL(queryString, 1, 30, cusName,cusName);
					if(retList != null){
						for(Object[] objArray : retList){
							JSONObject jsonObj = new JSONObject();
							jsonObj.put("name", objArray[0].toString());
							jsonObj.put("address", objArray[1].toString());
							jsonObj.put("regionId", ((Integer)objArray[2]).intValue());
							jsonObj.put("regionName", objArray[3].toString());
							jsonObj.put("id", objArray[4].toString());
							jsonArray.put(jsonObj);	
						}
					}
				}
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CrmServlet-->case 13", e);
				}else{
					log.error("error in CrmServlet-->case 13", e);
				}
			}finally{
				response.setContentType("text/json;charset=gbk");
				response.getWriter().write(jsonArray.toString());
			}
			break;
		case 14://查询潜在客户
			JSONArray jss=new JSONArray();
			try {
				String CustomerName=request.getParameter("CustomerName");
				//if(CustomerName!=null&&!CustomerName.equals(""))
				{
					int page = 1;
					if (request.getParameter("page") != null)
						page = Integer.parseInt(request.getParameter("page").toString());
					int rows = 10;
					if (request.getParameter("rows") != null)
						rows = Integer.parseInt(request.getParameter("rows").toString());
					List<Object> keys=new ArrayList<Object>();
					String queryString=" from PotentialCustomer as a where a.name like ?";
					keys.add("%"+URLDecoder.decode(CustomerName,"UTF-8")+"%");
				PotentialCustomerManager pcm=new PotentialCustomerManager();
				List<PotentialCustomer> res1=pcm.findPageAllByHQL(queryString, page, rows, keys);
				
				
				for(PotentialCustomer p:res1)
				{
					JSONObject js=new JSONObject();
					js.put("CustomerName", p.getName());
					js.put("Brief1", p.getBrief());
					js.put("Address",p.getAddress());
					js.put("Region",p.getRegion().getName());
					js.put("RegionId",p.getRegion().getId());
					js.put("NameEn",p.getNameEn());
					js.put("FacilitatingAgency",p.getFacilitatingAgency()==null?"":p.getFacilitatingAgency());
					js.put("Status",p.getStatus());
					js.put("From", p.getPotentialCustomerFrom());
					js.put("Intension", p.getCooperationIntension());
					BaseTypeManager btm=new BaseTypeManager();
					js.put("Industry", btm.findById(p.getIndustry()).getName());
					js.put("IndustryId", p.getIndustry());
					js.put("Id", p.getId());
					jss.put(js);
				}
				}
			} catch (Exception e) {
				// TODO: handle exception
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CrmServlet-->case 14", e);
				}else{
					log.error("error in CrmServlet-->case 14", e);
				}
			}
			finally{
				response.setContentType("text/json;charset=gbk");
				response.getWriter().write(jss.toString());

			}break;
		case 15://自动加载所有的潜在客户到表格中
			JSONArray jsos=new JSONArray();
			try {
				String page=request.getParameter("Page");
				String row=request.getParameter("Row");
				//int id=Integer.parseInt(request.getParameter("CustomerId"));
				PotentialCustomerManager pcm=new PotentialCustomerManager();
				int pages=1;
				if(page!=null)pages=Integer.parseInt(page);
				int rows=10;
				if(row!=null)rows=Integer.parseInt(row);
				String query="from PotentialCustomer";
				List<Object> arr=new ArrayList<Object>();
				List<PotentialCustomer> p=pcm.findPageAllByHQL(query, pages, rows, arr);
				if(p!=null)
					for(PotentialCustomer a:p)
					{
						JSONObject jso=new JSONObject();
						jso.put("CustomerName", a.getName());
						//jso.put("CustomerNameEn",a.getNameEn());
						jso.put("Brief1", a.getBrief());
						jso.put("Address",a.getAddress());
						jso.put("Region",a.getRegion().getName());
						jso.put("RegionId",a.getRegion().getId());
						jso.put("NameEn",a.getNameEn());
						jso.put("FacilitatingAgency",a.getFacilitatingAgency()==null?"":a.getFacilitatingAgency());
						jso.put("From", a.getPotentialCustomerFrom());
						jso.put("Intension", a.getCooperationIntension());
						BaseTypeManager btm=new BaseTypeManager();
						jso.put("Industry", btm.findById(a.getIndustry()).getName());
						jso.put("IndustryId", a.getIndustry());
						jso.put("Id", a.getId());
						jsos.put(jso);
					}
			} catch (Exception e) {
				// TODO: handle exception
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CrmServlet-->case 15", e);
				}else{
					log.error("error in CrmServlet-->case 15", e);
				}
			}
			finally{
				response.setContentType("text/json;charset=gbk");
				response.getWriter().write(jsos.toString());

			}break;
		case 16://修改潜在客户信息
			JSONObject json=new JSONObject();
			try {
				int id=Integer.parseInt(request.getParameter("Id"));
				
				String name=request.getParameter("CustomerName");
				String brief=request.getParameter("Brief");
				String nameEn=request.getParameter("NameEn");
				int regionId=Integer.parseInt(request.getParameter("RegionId"));
				int from=Integer.parseInt(request.getParameter("From"));
				String address=request.getParameter("Address");
				int intension=Integer.parseInt(request.getParameter("Intension"));
				int industry=Integer.parseInt(request.getParameter("IndustryId"));
				//int status=Integer.parseInt(request.getParameter("Status"));
				String facilitatingAgency=request.getParameter("FacilitatingAgency");
				PotentialCustomerManager pcm=new PotentialCustomerManager();
				PotentialCustomer pc=pcm.FindById(id);
				String Name=pc.getName();
				String Brief=pc.getBrief();
				String NameEn=pc.getNameEn();
				int RegionId=pc.getRegion().getId();
				int From=pc.getPotentialCustomerFrom();
				String Address=pc.getAddress();
				int Intension=pc.getCooperationIntension();
				int Industry=pc.getIndustry();
				//int Status=pc.getStatus();
				if(name!=Name)pc.setName(name);
				if(nameEn!=NameEn)pc.setNameEn(nameEn);
				if(brief!=Brief)pc.setBrief(brief);
				if(regionId!=RegionId)
				{
					Region region=new Region();
					region.setId(regionId);
					pc.setRegion(region);
				}
				if(from!=From)pc.setPotentialCustomerFrom(from);
				if(address!=Address)pc.setAddress(address);
				if(intension!=Intension)pc.setCooperationIntension(intension);
				if(industry!=Industry)pc.setIndustry(industry);
				//if(status!=Status)pc.setStatus(status);
				pc.setFacilitatingAgency(facilitatingAgency);
				if(pcm.save(pc))
				{
					json.put("IsOk", true);
					json.put("msg", "修改成功！");
				}
				else 
				{
					json.put("IsOk", false);
					json.put("msg", "修改失败！");
				}
			} catch (Exception e) 
			{
				// TODO: handle exception
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CrmServlet-->case 16", e);
				}else{
					log.error("error in CrmServlet-->case 16", e);
				}
			}
			finally{
				response.setContentType("text/html;charset=gbk");
				response.getWriter().write(json.toString());
			}break;
		case 17:
			JSONObject json1=new JSONObject();
			try {
				int id=Integer.parseInt(request.getParameter("Del_id"));

				PotentialCustomerManager pcm=new PotentialCustomerManager();
				PotentialCustomer pc=pcm.FindById(id);
				pc.setStatus(1);
				
				if(pcm.save(pc))
				{
					json1.put("IsOk", true);
					json1.put("msg", "注销成功！");
				}
				else 
				{
					json1.put("IsOk", false);
					json1.put("msg", "注销失败！");
				}
			} catch (Exception e) 
			{
				// TODO: handle exception
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CrmServlet-->case 17", e);
				}else{
					log.error("error in CrmServlet-->case 17", e);
				}
			}
			finally{
				response.setContentType("text/html;charset=gbk");
				response.getWriter().write(json1.toString());
			}break;
		case 18://加载全部费用信息
			JSONArray jsobj=new JSONArray();
			try 
			{
				CustomerServiceFeeManager csfm=new CustomerServiceFeeManager();
				int page=1,rows=10;
				String Page=request.getParameter("page");
				String Rows=request.getParameter("rows");
				if(Page!=null)page=Integer.parseInt(Page);
				if(Rows!=null)rows=Integer.parseInt(Rows);
				String queryStr="select model.id,model.billNum,model.customer.id," +
						"model.customer.name,model.sysUserByCreateSysUserId.name," +
						"model.createTime,model.paidTime,model.paidVia,model.paidSubject," +
						"model.money,model.sysUserByPaidSysUserId.name,model.remark," +
						"model.status from CustomerServiceFee as model";
				List<Object> arr=new ArrayList<Object>();
				List<Object[]> csf=csfm.findPageAllByHQL(queryStr, page, rows, arr);
				if(csf!=null)
					for(Object[] a:csf)
					{
						JSONObject jso=new JSONObject();
						jso.put("Id", a[0].toString());
						jso.put("BillNum", a[1].toString());
						jso.put("CustomerId", a[2].toString());
						jso.put("CustomerName", a[3].toString());
						jso.put("CreateSysUser", a[4].toString());
						jso.put("CreateTime", a[5].toString());
						jso.put("PaidTime", a[6].toString());
						jso.put("PaidVia", a[7].toString());
						jso.put("PaidSubject", a[8].toString());
						jso.put("Money", a[9].toString());
						jso.put("PaidSysUser", a[10].toString());
						jso.put("Remark", a[11].toString());
						jso.put("Status", a[12].toString());
						//jso.put("", a[].toString());
						jsobj.put(jso);
					}
			} 
			catch (Exception e) {
				// TODO: handle exception
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CrmServlet-->case 18", e);
				}else{
					log.error("error in CrmServlet-->case 18", e);
				}
			}
			finally{
				response.setContentType("text/json;charset=gbk");
				response.getWriter().write(jsobj.toString());
			}break;
		case 19://查询费用信息
			JSONObject re=new JSONObject();
			JSONArray jArray=new JSONArray();
			try {
				
				String customerId=request.getParameter("CustomerId");
				String status=request.getParameter("Status");
				List<CustomerServiceFee> lr=null;
				CustomerServiceFeeManager m=new CustomerServiceFeeManager();
				int page=1;
				if((request.getParameter("page"))!=null)
					page=Integer.parseInt(request.getParameter("page").toString());
				int rows = 10;
				if (request.getParameter("rows") != null)
					rows = Integer.parseInt(request.getParameter("rows").toString());
				List<Object> keys=new ArrayList<Object>();
				String queryString="from CustomerServiceFee as a where 1=1";
				if(customerId!=null&&!customerId.equals(""))
				{
					keys.add(Integer.valueOf(customerId));
					queryString+=" and a.customer.id= ? ";
				}
				if(status!=null&&!status.equals("")&&!status.equals("4"))
				{
					keys.add(Integer.valueOf(status));
					queryString+=" and a.status= ? ";
				}
				
			lr=m.findPageAllByHQL(queryString, page, rows, keys);
					if(lr!=null)
					{
						for(CustomerServiceFee a:lr)
						{
							JSONObject jsonObject=new JSONObject();
							jsonObject.put("Id", a.getId());
							jsonObject.put("BillNum", a.getBillNum());
							//jsonObject.put("CustomerId", );
							jsonObject.put("CustomerName", a.getCustomer().getName());
							jsonObject.put("CreateSysUser", a.getSysUserByCreateSysUserId().getName());
							jsonObject.put("CreateTime", a.getCreateTime());
							jsonObject.put("PaidTime", a.getPaidTime());
							jsonObject.put("PaidVia", a.getPaidVia());
							jsonObject.put("PaidSubject", a.getPaidSubject());
							jsonObject.put("Money", a.getMoney());
							jsonObject.put("PaidSysUser", a.getSysUserByPaidSysUserId().getName());
							jsonObject.put("Remark", a.getRemark());
							jsonObject.put("Status", a.getStatus());
							//jsonObject.put("Attachment", a[].toString());
							jArray.put(jsonObject);
						}
					}
					List<Object> count=m.findPageAllByHQL("select sum(a.money)"+queryString, 1, 10, keys);
					JSONObject f=new JSONObject();
					JSONArray tmp=new JSONArray();
					
					f.put("CustomerName", "总计");
					f.put("BillNum", count.get(0).toString());
					tmp.put(f);
					re.put("rows", jArray);
					re.put("footer", tmp);
					
					
				
				
			} catch (Exception e) {
				// TODO: handle exception
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CrmServlet-->case 19", e);
				}else{
					log.error("error in CrmServlet-->case 19", e);
				}
			}
			finally
			{
				response.setContentType("text/json;charset=gbk");
				response.getWriter().write(re.toString());
			}break;
		case 20://添加费用分配信息
			JSONObject jsonObject=new JSONObject();
			try 
			{
				int customerId=Integer.parseInt(request.getParameter("CustomerId"));
				int insideContactorId=Integer.parseInt(request.getParameter("InsideContactorId"));
				int year=Integer.parseInt(request.getParameter("Year"));
				double fee=Double.parseDouble(request.getParameter("Fee"));
				int lastEditorId=((SysUser)(request.getSession().getAttribute("LOGIN_USER"))).getId();
				String remark=request.getParameter("Remark");
				Timestamp lastEditTime=new Timestamp(System.currentTimeMillis());
				InsideContactorFeeAssignManager icfam=new InsideContactorFeeAssignManager();
				List<InsideContactorFeeAssign> ricfa=icfam.findByVarProperty(new KeyValueWithOperator("customer.id",customerId,"="),new KeyValueWithOperator("sysUserByInsideContactorId.id",insideContactorId,"="),new KeyValueWithOperator("year",year,"="));				
				if(ricfa!=null&&ricfa.size()>0)
				{
					jsonObject.put("IsOk", false);
					jsonObject.put("msg", "该记录已存在，请进行修改！");
					
				}
				else 
				{
					InsideContactorFeeAssign a=new InsideContactorFeeAssign();
					Customer c=new Customer();
					c.setId(customerId);
					a.setCustomer(c);
					a.setFee(fee);
					a.setLastEditTime(lastEditTime);
					if(remark!=null)a.setRemark(remark);
					else a.setRemark("");
					SysUser s1=new SysUser();
					s1.setId(insideContactorId);
					a.setSysUserByInsideContactorId(s1);
					SysUser s2=new SysUser();
					s2.setId(lastEditorId);
					a.setSysUserByLastEditorId(s2);
					a.setYear(year);
					if(icfam.save(a))
					{
						jsonObject.put("IsOk", true);
						jsonObject.put("msg", "添加成功！");
					}
					else
						{
							jsonObject.put("IsOk", false);
							jsonObject.put("msg", "添加失败！");
						}
					
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CrmServlet-->case 20", e);
				}else{
					log.error("error in CrmServlet-->case 20", e);
				}
			}finally
			{
				response.setContentType("text/html;charset=gbk");
				response.getWriter().write(jsonObject.toString());
			}break;
		case 21:
			JSONArray jsonarry=new JSONArray();
			try {
				String customerId=request.getParameter("CustomerId");
				String insideContactorId=request.getParameter("InsideContactorId");
				String year=request.getParameter("Year");
				int CustomerId,InsideContactorId,Year;
				int page=1;
				if((request.getParameter("page"))!=null)
					page=Integer.parseInt(request.getParameter("page").toString());
				int rows = 10;
				if (request.getParameter("rows") != null)
					rows = Integer.parseInt(request.getParameter("rows").toString());
				String questr="from InsideContactorFeeAssign where 1=1";
				List<Object> keys=new ArrayList<Object>();
				if(customerId!=null&&!customerId.equals(""))
					{
						CustomerId=Integer.parseInt(customerId);
						questr+=" and customer.id = ? ";
						keys.add(CustomerId);
					}
				if(insideContactorId!=null&&!insideContactorId.equals(""))
				{
					InsideContactorId=Integer.parseInt(insideContactorId);
					questr+=" and sysUserByInsideContactorId.id = ? ";
					keys.add(InsideContactorId);
				}
				if(year!=null&&!year.equals(""))
				{
					Year=Integer.parseInt(year);
					questr+=" and year = ? ";
					keys.add(Year);
				}
				InsideContactorFeeAssignManager icfam=new InsideContactorFeeAssignManager();
				List<InsideContactorFeeAssign> lr=icfam.findPageAllByHQL(questr, page, rows, keys);
				if(lr!=null&&lr.size()>0)
					for(InsideContactorFeeAssign a: lr)
					{
						JSONObject tmp=new JSONObject();
						tmp.put("CustomerName",a.getCustomer().getName() );
						tmp.put("CustomerNameId", a.getCustomer().getId());
						tmp.put("CustomerAddress", a.getCustomer().getAddress());
						tmp.put("InsideContactor",a.getSysUserByInsideContactorId().getName() );
						tmp.put("JobNum",a.getSysUserByInsideContactorId().getJobNum() );
						tmp.put("InsideContactorId",a.getSysUserByInsideContactorId().getId() );
						tmp.put("Year", a.getYear());
						tmp.put("Fee",a.getFee() );
						tmp.put("LastEditor",a.getSysUserByLastEditorId().getName() );
						tmp.put("Remark",a.getRemark() );
						tmp.put("Id",a.getId() );
						tmp.put("LastEditTime",a.getLastEditTime() );
						jsonarry.put(tmp);
					}
				
				
			} catch (Exception e) {
				// TODO: handle exception
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CrmServlet-->case 21", e);
				}else{
					log.error("error in CrmServlet-->case 21", e);
				}
			}
			finally
			{
				response.setContentType("text/json;charset=gbk");
				response.getWriter().write(jsonarry.toString());
			}break;
		case 22://修改
			JSONObject j0=new JSONObject();
			try {
				String ID=request.getParameter("Id");
				String Fee=request.getParameter("Fee");
				String remark=request.getParameter("Remark");
				Timestamp timestamp=new Timestamp(System.currentTimeMillis());
				InsideContactorFeeAssignManager icfaManager=new InsideContactorFeeAssignManager();
				InsideContactorFeeAssign icfaAssign=icfaManager.FindById(Integer.parseInt(ID));
				if(icfaAssign!=null)
				{
					icfaAssign.setFee(Double.valueOf(Fee));
					icfaAssign.setLastEditTime(timestamp);
					if(remark!=null)icfaAssign.setRemark(remark);
					
					if(icfaManager.save(icfaAssign))
					{
						j0.put("IsOk",true);
						j0.put("msg","修改成功！");
					}
					else {
						j0.put("IsOk", false);
						j0.put("msg", "修改失败！");
					}
				}
				
				
			} catch (Exception e) {
				// TODO: handle exception
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CrmServlet-->case 22", e);
				}else{
					log.error("error in CrmServlet-->case 22", e);
				}
			}finally
			{
				response.setContentType("text/html;charset=gbk");
				response.getWriter().write(j0.toString());
			}break;
		case 23://删除费用分配信息
			JSONObject retjson=new JSONObject();
			try 
			{
				String ID=request.getParameter("DelId");
				InsideContactorFeeAssignManager icfaAssignManager=new InsideContactorFeeAssignManager();
				if(ID!=null&&!ID.equals(""))
				{
					if(icfaAssignManager.deleteById(Integer.parseInt(ID)))
					{
						retjson.put("IsOk", true);
						retjson.put("msg","删除成功！");
					}
					else 
						{
							retjson.put("IsOk", false);
							retjson.put("msg","删除失败！");
						}
				}
				else 
				{
					retjson.put("IsOk", false);
					retjson.put("msg","未知错误！");
				}
			} catch (Exception e) {
				// TODO: handle exception
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CrmServlet-->case 23", e);
				}else{
					log.error("error in CrmServlet-->case 23", e);
				}
			}
			finally
			{
				response.setContentType("text/html;charset=gbk");
				response.getWriter().write(retjson.toString());
			}break;
		case 24://新建客户
			JSONObject retObj=new JSONObject();
			try 
			{
				String Name = request.getParameter("Name");//@
				String NameEn = request.getParameter("NameEn");
				String Brief = request.getParameter("Brief");
				String Type = request.getParameter("CustomerType");//@
				String Code = request.getParameter("Code");
				String RegionId = request.getParameter("RegionId");//@
				String ZipCode = request.getParameter("ZipCode");//@
				
				String Address = request.getParameter("Address");//@
				String AddressEn = request.getParameter("AddressEn");
				String Tel = request.getParameter("Tel");
				String Fax = request.getParameter("Fax");
				String[] ClassificationStr = request.getParameterValues("Classification");
				String Classification = "";
				if(ClassificationStr!=null)
				for(int i = 0;i< ClassificationStr.length; i++)
					Classification = Classification + (Classification.equals("")?"":",") + ClassificationStr[i];
				//System.out.println(Classification);
				String Status = request.getParameter("Status");
				String AccountBank = request.getParameter("AccountBank");
				String Account = request.getParameter("Account");
				String CreditAmount = request.getParameter("CreditAmount");
				String Remark = request.getParameter("Remark");
				String FieldDemands = request.getParameter("FieldDemands");
				String CertificateDemands = request.getParameter("CertificateDemands");
				String SpecialDemands = request.getParameter("SpecialDemands");
				SysUser user = (SysUser)request.getSession().getAttribute("LOGIN_USER");
				//String InsideContactor = request.getParameter("InsideContactor");
				
				String PaiVia=request.getParameter("PayVia");//@
				String PaiType=request.getParameter("PayType");//@
				String AccountCycle=request.getParameter("AccountCycle");//@
				String CustomerValueLevel=request.getParameter("CustomerValueLevel");
				String CustomerLevel=request.getParameter("CustomerLevel");
				String Trendency=request.getParameter("Trendency");
				String OutputExpectation=request.getParameter("OutputExpectation");
				String ServiceFeeLimitation=request.getParameter("ServiceFeeLimitation");
				String Industry=request.getParameter("IndustryId");
				String Loyalty=request.getParameter("Loyalty");
				String Satisfaction=request.getParameter("Satisfaction");
				
				Customer customer;
				customer = new Customer();
				
				customer.setName(Name);
				customer.setNameEn(NameEn);
				customer.setBrief(Brief);
				//if(Type!=null&&!Type.equals(""))
				customer.setCustomerType(Integer.parseInt(Type));
				
				customer.setCode(Code);
				customer.setRegion((new RegionManager()).findById(Integer.parseInt(RegionId)));
				customer.setZipCode(ZipCode);
				customer.setAddress(Address);
				customer.setAddressEn(AddressEn);
				customer.setTel(Tel);
				customer.setFax(Fax);
				customer.setBalance(0.0);
				customer.setClassification(Classification);
				if(Status!=null&&!Status.equals(""))
					customer.setStatus(Integer.parseInt(Status));
				
				customer.setAccountBank(AccountBank);
				customer.setAccount(Account);
				if(CreditAmount!=null&&!CreditAmount.equals(""))
				customer.setCreditAmount(Double.parseDouble(CreditAmount));
				
				customer.setRemark(Remark);
				customer.setFieldDemands(FieldDemands);
				customer.setCertificateDemands(CertificateDemands);
				customer.setSpecialDemands(SpecialDemands);
				customer.setSysUserByModificatorId(user);
				customer.setModifyDate(new Timestamp(System.currentTimeMillis()));
				//////////////////////////////////////////////
				if(PaiVia!=null&&!PaiVia.equals(""))customer.setPayVia(Integer.parseInt(PaiVia));
				if(PaiType!=null&&!PaiType.equals(""))
				{
					customer.setPayType(Integer.parseInt(PaiType));
					if(PaiType.equals("2"))//2表示周期结账
						customer.setAccountCycle(Integer.parseInt(AccountCycle));
				}else {
					customer.setPayType(4);//4表示其它类型,也是因为不能为空
					customer.setAccountCycle(13);//13表示不是同期结账，本来应为空的，但由于数据库设计该字段不能为空
				}
				
				if(CustomerValueLevel!=null&&!CustomerValueLevel.equals(""))customer.setCustomerValueLevel(Integer.parseInt(CustomerValueLevel));
				if(CustomerLevel!=null&&!CustomerLevel.equals(""))customer.setCustomerLevel(Integer.parseInt(CustomerLevel));
				if(Trendency!=null&&!Trendency.equals(""))customer.setTrendency(Integer.parseInt(Trendency));
				if(OutputExpectation!=null&&!OutputExpectation.equals(""))customer.setOutputExpectation(Double.parseDouble(OutputExpectation));
				if(ServiceFeeLimitation!=null&&!ServiceFeeLimitation.equals(""))customer.setServiceFeeLimitation(Double.parseDouble(ServiceFeeLimitation));
				if(Industry!=null&&!Industry.equals(""))customer.setIndustry(Integer.parseInt(Industry));
				
				if(Loyalty!=null&&!Loyalty.equals(""))customer.setLoyalty(Integer.parseInt(Loyalty));
				if(Satisfaction!=null&&!Satisfaction.equals(""))customer.setSatisfaction(Integer.parseInt(Satisfaction));
				//////////////////////////////////////////////
				String Contactor = request.getParameter("Contactor");//@
				String ContactorTel1 = request.getParameter("ContactorTel1");
				String ContactorTel2 = request.getParameter("ContactorTel2");
				CustomerContactor customercontactor = new CustomerContactor();
				customercontactor.setName(Contactor);
				if (ContactorTel1 != null && !ContactorTel1.equals(""))
					customercontactor.setCellphone1(ContactorTel1);
				else customercontactor.setCellphone1("");
				if (ContactorTel2 != null && !ContactorTel2.equals(""))
					customercontactor.setCellphone2(ContactorTel2);
				customercontactor.setLastUse(customer.getModifyDate());
				customercontactor.setCount(1);//1？
				
				CustomerManager cusmag = new CustomerManager();
				
				
				String InsideContactorId=request.getParameter("InsideContactorId");
				String Role=request.getParameter("Role");
			
				InsideContactor ic=new InsideContactor();
				SysUser s=new SysUser();
				s.setId(Integer.parseInt(InsideContactorId));
				
				ic.setSysUser(s);
				ic.setCustomer(customer);
				ic.setRole(Integer.parseInt(Role));
				//InsideContactorManager icm=new InsideContactorManager();
				boolean resu = cusmag.save(customer, customercontactor,ic);
				retObj.put("IsOK", resu);
				retObj.put("msg", resu?"添加成功！":"添加失败，请重新添加！");
			
				
			} catch (Exception e) {
				// TODO: handle exception
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CrmServlet-->case 24", e);
				}else{
					log.error("error in CrmServlet-->case 24", e);
				}
			}finally
			{
				response.setContentType("text/html;charset=gbk");
				response.getWriter().write(retObj.toString());
			}break;
		case 25:// 客户查询
			JSONObject resobj = new JSONObject();
			try {
				String queryName = request.getParameter("queryname");
				String queryZipCode = request.getParameter("queryZipCode");
				String queryAddress = request.getParameter("queryAddress");
				String queryTel = request.getParameter("queryTel");
				String queryInsideContactor = request.getParameter("queryInsideContactor");
				String queryClassi = request.getParameter("queryClassi");
				String queryCredit = request.getParameter("queryCredit");
				String queryContactor = request.getParameter("queryContactor");
				String queryContactorTel = request.getParameter("queryContactorTel");
				
				int page = 1;
				if (request.getParameter("page") != null)
					page = Integer.parseInt(request.getParameter("page").toString());
				int rows = 10;
				if (request.getParameter("rows") != null)
					rows = Integer.parseInt(request.getParameter("rows").toString());
				
				String queryStr = "from Customer as model where 1=1 ";
				List<Object> keys = new ArrayList<Object>();
				
				if(queryName!=null&&!queryName.equals(""))
				{
					String cusNameStr = URLDecoder.decode(queryName, "UTF-8");
					queryStr = queryStr + " and (model.name like ? or model.nameEn like ? or model.brief like ? or model.code like ?)";
					keys.add("%" + cusNameStr + "%");
					keys.add("%" + cusNameStr + "%");
					keys.add("%" + cusNameStr + "%");
					keys.add("%" + cusNameStr + "%");
				}
				if(queryZipCode!=null&&!queryZipCode.equals(""))
				{
					String cusZipCodeStr = URLDecoder.decode(queryZipCode, "UTF-8");
					queryStr = queryStr + " and model.zipCode like ?";
					keys.add("%" + cusZipCodeStr + "%");
				}
				if(queryAddress!=null&&!queryAddress.equals(""))
				{
					String cusAddressStr = URLDecoder.decode(queryAddress, "UTF-8");
					queryStr = queryStr + " and model.address like ?";
					keys.add("%" + cusAddressStr + "%");
				}
				if(queryTel!=null&&!queryTel.equals(""))
				{
					String cusTelStr = URLDecoder.decode(queryTel, "UTF-8");
					queryStr = queryStr + " and (model.tel like ? or model.fax like ?)";
					keys.add("%" + cusTelStr + "%");
					keys.add("%" + cusTelStr + "%");
				}
				if(queryInsideContactor!=null&&!queryInsideContactor.equals(""))
				{
					String cusInsideContactorStr = URLDecoder.decode(queryInsideContactor, "UTF-8");
					queryStr = queryStr + " and (model.sysUserByInsideContactorId.name like ? or model.sysUserByInsideContactorId.brief like ?)";
					keys.add("%" + cusInsideContactorStr + "%");
					keys.add("%" + cusInsideContactorStr + "%");
				}
				if(queryClassi!=null&&!queryClassi.equals(""))
				{
					String cusClassiStr = URLDecoder.decode(queryClassi, "UTF-8");
					queryStr = queryStr + " and model.classification like ?";
					keys.add("%" + cusClassiStr + "%");
				}
				if(queryCredit!=null&&!queryCredit.equals(""))
				{
					String cusCreditStr = URLDecoder.decode(queryCredit, "UTF-8");
					queryStr = queryStr + " and model.creditAmount = ?";
					keys.add(Double.valueOf(cusCreditStr));
				}
				if(queryContactor!=null&&!queryContactor.equals(""))
				{
					String cusContactorStr = URLDecoder.decode(queryContactor, "UTF-8");
					queryStr = queryStr + " and model.id in (select model1.customerId from CustomerContactor as model1 where model1.name like ?)";
					keys.add("%" + cusContactorStr + "%");
				}
				if(queryContactorTel!=null&&!queryContactorTel.equals(""))
				{
					String cusContactorTelStr = URLDecoder.decode(queryContactorTel, "UTF-8");
					queryStr = queryStr + " and model.id in (select model1.customerId from CustomerContactor as model1 where (model1.cellphone1 like ? or model1.cellphone2 like ?))";
					keys.add("%" + cusContactorTelStr + "%");
					keys.add("%" + cusContactorTelStr + "%");
				}
				
				List<Customer> result;
				int total;
				CustomerManager cusmag=new CustomerManager();
				result = cusmag.findPageAllByHQL(queryStr + " order by model.status asc, model.id asc", page, rows, keys);
				total = cusmag.getTotalCountByHQL("select count(*) "+queryStr, keys);
				JSONArray options = new JSONArray();
				
				for (Customer cus : result) {
					JSONObject option = new JSONObject();
					option.put("Id", cus.getId());
					option.put("Name", cus.getName());
					option.put("NameEn", cus.getNameEn());
					option.put("Brief", cus.getBrief());
					option.put("RegionId", cus.getRegion().getId());
					option.put("CustomerType", cus.getCustomerType());
					option.put("Code", cus.getCode());
					option.put("Address", cus.getAddress());
					option.put("AddressEn", cus.getAddressEn());
					option.put("Tel", cus.getTel());
					option.put("Fax", cus.getFax());
					option.put("ZipCode", cus.getZipCode());
					option.put("Status", cus.getStatus());
					option.put("Balance", cus.getBalance());
					option.put("AccountBank", cus.getAccountBank());
					option.put("Account", cus.getAccount());
					option.put("Classification", cus.getClassification());
					option.put("FieldDemands", cus.getFieldDemands());
					option.put("CertificateDemands", cus.getCertificateDemands());
					option.put("SpecialDemands", cus.getSpecialDemands());
					option.put("CreditAmount", cus.getCreditAmount());
					option.put("CancelDate", cus.getCancelDate()==null?"未注销":cus.getCancelDate());
					option.put("CancelReason", cus.getCancelDate()==null?"":cus.getReason().getReason());
					option.put("Remark", cus.getRemark());
					option.put("ModifyDate", cus.getModifyDate());
					option.put("Modificator", cus.getSysUserByModificatorId().getName());
					
					CustomerContactorManager ccm=new CustomerContactorManager();
					List<CustomerContactor> lcon=ccm.findByVarProperty(new KeyValueWithOperator("customer.id",cus.getId(),"="));
					if(lcon.size()!=0&&lcon.get(0)!=null)
					option.put("InsideContactor",lcon.get(0).getName()+"等");
					else option.put("InsideContactor", "");
					//option.put("InsideContactor", cus.getSysUserByInsideContactorId()==null?"":cus.getSysUserByInsideContactorId().getName());
					
					////////////////////////////////////////////////////////////////////////////
					option.put("PayVia",cus.getPayVia()==null?" ":cus.getPayVia());
					option.put("PayType",cus.getPayType()==null?"":cus.getPayType());
					option.put("AccountCycle",cus.getAccountCycle()==null?"":cus.getAccountCycle());
					option.put("CustomerValueLevel",cus.getCustomerValueLevel()==null?"":cus.getCustomerValueLevel());
					option.put("CustomerLevel",cus.getCustomerLevel()==null?"":cus.getCustomerLevel());
					option.put("Trendency",cus.getTrendency()==null?"":cus.getTrendency());
					option.put("OutputExpectation",cus.getOutputExpectation()==null?"":cus.getOutputExpectation());
					//option.put("Output",cus.getOutput());
					option.put("ServiceFeeLimitation",cus.getServiceFeeLimitation()==null?"":cus.getServiceFeeLimitation());
					Integer t=cus.getIndustry();
					BaseType bt=null;
					if(t!=null)
					{
					 bt=(new BaseTypeManager()).findById(t);
					}
					option.put("Industry",bt==null?"":bt.getName());
					
					option.put("Loyalty", cus.getLoyalty()==null?"":cus.getLoyalty());
					option.put("Satisfaction", cus.getSatisfaction()==null?"":cus.getSatisfaction());
					///////////////////////////////////////////////////////////////////////////
					CustomerContactorManager cusconmag1 = new CustomerContactorManager();
					CustomerContactor cuscon ;
					List<CustomerContactor> resultList = cusconmag1.findByPropertyBySort("lastUse", false,
							new KeyValueWithOperator("customer.id", cus.getId(), "="));
					if(resultList != null&& resultList.size()>0)
					{
						cuscon = resultList.get(0);
						option.put("Contactor", cuscon.getName());
						option.put("ContactorTel1", cuscon.getCellphone1());
						option.put("ContactorTel2", cuscon.getCellphone2());
					}
	
					options.put(option);
				}
				resobj.put("total", total);
				resobj.put("rows", options);
			} catch (Exception ex) {
				if(ex.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CustomerServlet-->case 25", ex);
				}else{
					log.error("error in CustomerServlet-->case 25", ex);
				}
			}finally{
				response.setContentType("text/json;charset=utf-8");
				response.getWriter().write(resobj.toString());
				//System.out.println(res.toString());
			}
			break;
		case 26: // 修改客户
			JSONObject retObj1=new JSONObject();
			try{
				String Name = request.getParameter("Name");
				String NameEn = request.getParameter("NameEn");
				String Brief = request.getParameter("Brief");
				String Type = request.getParameter("CustomerType");
				String Code = request.getParameter("Code");
				String RegionId = request.getParameter("RegionId");
				String ZipCode = request.getParameter("ZipCode");
				
				String Address = request.getParameter("Address");
				String AddressEn = request.getParameter("AddressEn");
				String Tel = request.getParameter("Tel");
				String Fax = request.getParameter("Fax");
				String[] ClassificationStr = request.getParameterValues("Classification");
				String Classification = "";
				for(int i = 0;i< ClassificationStr.length; i++)
					Classification = Classification + (Classification.equals("")?"":",") + ClassificationStr[i];
				//System.out.println(Classification);
				String Status = request.getParameter("Status");
				String AccountBank = request.getParameter("AccountBank");
				String Account = request.getParameter("Account");
				String CreditAmount = request.getParameter("CreditAmount");
				String Remark = request.getParameter("Remark");
				String FieldDemands = request.getParameter("FieldDemands");
				String CertificateDemands = request.getParameter("CertificateDemands");
				String SpecialDemands = request.getParameter("SpecialDemands");
				SysUser user = (SysUser)request.getSession().getAttribute("LOGIN_USER");
				String InsideContactor = request.getParameter("InsideContactor");

				String PaiVia=request.getParameter("PayVia");
				String PaiType=request.getParameter("PayType");
				String AccountCycle=request.getParameter("AccountCycle");
				String CustomerValueLevel=request.getParameter("CustomerValueLevel");
				String CustomerLevel=request.getParameter("CustomerLevel");
				String Trendency=request.getParameter("Trendency");
				String OutputExpectation=request.getParameter("OutputExpectation");
				String ServiceFeeLimitation=request.getParameter("ServiceFeeLimitation");
				String Industry=request.getParameter("IndustryId");
				
				String Loyalty=request.getParameter("Loyalty");
				String Satisfaction=request.getParameter("Satisfaction");
				String EditId=request.getParameter("Edit_id");
				
				Customer customer;
				CustomerManager cusmag = new CustomerManager();
				customer = cusmag.findById(Integer.valueOf(EditId));
				
				customer.setName(Name);
				customer.setNameEn(NameEn);
				customer.setBrief(Brief);
				customer.setCustomerType(Integer.parseInt(Type));
				customer.setCode(Code);
				customer.setRegion((new RegionManager()).findById(Integer.parseInt(RegionId)));
				customer.setZipCode(ZipCode);
				customer.setAddress(Address);
				customer.setAddressEn(AddressEn);
				customer.setTel(Tel);
				customer.setFax(Fax);
				customer.setBalance(0.0);
				customer.setClassification(Classification);
				if(customer.getStatus()!=null&&customer.getStatus()==1&&Integer.parseInt(Status)==0)
				{
					customer.setStatus(Integer.parseInt(Status));
					customer.setCancelDate(null);
					customer.setReason(null);
				}
				else{
					customer.setStatus(Integer.parseInt(Status));
				}
				customer.setAccountBank(AccountBank);
				customer.setAccount(Account);
				if(CreditAmount!=null&&CreditAmount.trim()!="")
				customer.setCreditAmount(Double.parseDouble(CreditAmount));
				/*if(InsideContactor!=null&!InsideContactor.equals(""))
				{
					customer.setSysUserByInsideContactorId((new UserManager()).findByVarProperty(new KeyValueWithOperator("name", InsideContactor, "=")).get(0));
				}
				else
				{
					customer.setSysUserByInsideContactorId(null);
				}*/
				customer.setRemark(Remark);
				customer.setFieldDemands(FieldDemands);
				customer.setCertificateDemands(CertificateDemands);
				customer.setSpecialDemands(SpecialDemands);
				customer.setSysUserByModificatorId(user);
				customer.setModifyDate(new Timestamp(System.currentTimeMillis()));
				//////////////////////////////////////////////
				if(PaiVia!=null&&PaiVia.trim()!="")customer.setPayVia(Integer.parseInt(PaiVia));
				if(PaiType!=null&&PaiType.trim()!="")customer.setPayType(Integer.parseInt(PaiType));
				if(AccountCycle!=null&&!AccountCycle.trim().equals(""))customer.setAccountCycle(Integer.parseInt(AccountCycle));
				if(CustomerValueLevel!=null&&CustomerValueLevel.trim()!="")customer.setCustomerValueLevel(Integer.parseInt(CustomerValueLevel));
				if(CustomerLevel!=null&&CustomerLevel.trim()!="")customer.setCustomerLevel(Integer.parseInt(CustomerLevel));
				if(Trendency!=null&&Trendency.trim()!="")customer.setTrendency(Integer.parseInt(Trendency));
				if(OutputExpectation!=null&&OutputExpectation.trim()!="")customer.setOutputExpectation(Double.parseDouble(OutputExpectation));
				if(ServiceFeeLimitation!=null&&ServiceFeeLimitation.trim()!="")customer.setServiceFeeLimitation(Double.parseDouble(ServiceFeeLimitation));
				if(Industry!=null&&Industry.trim()!="")customer.setIndustry(Integer.parseInt(Industry));
				if(Loyalty!=null&&Loyalty.trim()!="")customer.setLoyalty(Integer.parseInt(Loyalty));
				if(Satisfaction!=null&&Satisfaction.trim()!="")customer.setSatisfaction(Integer.parseInt(Satisfaction));
				//////////////////////////////////////////////
				//////////////////////////////////////////////////////////////////
				//Customer cus = initCustomer(request, Integer.valueOf(request.getParameter("edit_Id")));
				String Contactor = request.getParameter("Contactor");
				String ContactorTel1 = request.getParameter("ContactorTel1");
				String ContactorTel2 = request.getParameter("ContactorTel2");
				CustomerManager cusmag1=new CustomerManager();
				boolean res1 = cusmag1.update(customer, Contactor, ContactorTel1, ContactorTel2);
				retObj1.put("IsOK", res1);
				retObj1.put("msg", res1?"修改成功！":"修改失败，请重新修改！");
			}catch(Exception e){
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CustomerServlet-->case 26", e);
				}else{
					log.error("error in CustomerServlet-->case 26", e);
				}
				try {
					retObj1.put("IsOK", false);
					retObj1.put("msg", String.format("修改失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
			}finally{
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write(retObj1.toString());
			}
			break;
		case 27: // 注销客户
			JSONObject retobj2=new JSONObject();
			Timestamp today = new Timestamp(System.currentTimeMillis());
			try {
				int id = Integer.parseInt(request.getParameter("del_id"));
				CustomerManager cusmag =new CustomerManager();
				Customer customer1 = cusmag.findById(id);
				String Reason = request.getParameter("reason");
				ReasonManager rMgr = new ReasonManager();
				List<Reason> rList = rMgr.findByVarProperty(new KeyValueWithOperator("reason",Reason.trim(),"="), new KeyValueWithOperator("type", 22, "="));//查找注销原因
				if(rList.size() > 0){	//更新原因
					Reason reason = rList.get(0);
					reason.setCount(reason.getCount()+1);
					reason.setLastUse(today);
					rMgr.update(reason);
					customer1.setReason(reason);	//注销原因
				}else{	//新建原因
					Reason reason = new Reason();
					reason.setCount(1);
					reason.setLastUse(today);
					reason.setReason(Reason.trim());
					reason.setStatus(0);
					reason.setType(22);	//注销客户
					rMgr.save(reason);
					customer1.setReason(reason);	//注销原因
				}
				customer1.setCancelDate(today);
				customer1.setStatus(1);
				customer1.setModifyDate(today);
				customer1.setSysUserByModificatorId((SysUser)request.getSession().getAttribute("LOGIN_USER"));
				boolean res1 = cusmag.update(customer1);
				retobj2.put("IsOK", res1);
				retobj2.put("msg", res1?"注销成功！":"注销失败，请重新注销！");
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CustomerServlet-->case 27", e);
				}else{
					log.error("error in CustomerServlet-->case 27", e);
				}
				try{
					retobj2.put("IsOK", false);
					retobj2.put("msg", "注销失败，请重新注销！");
				}catch(Exception ex){}
			}finally{
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write(retobj2.toString());
			}
			break;
		case 28://查询关怀信息
			JSONArray retArray=new JSONArray();
			try 
			{
				String customerName=request.getParameter("CustomerName");
				String customerLevel=request.getParameter("CustomerLevel");
				String careContactor=request.getParameter("CareContactor");
				String careDutySysUser=request.getParameter("CareDutySysUser");
				String code=request.getParameter("Code");
				String startTime=request.getParameter("StartTime");
				String endTime=request.getParameter("EndTime");
				String way=request.getParameter("Way");
				int page=1,rows=10;
				String p=request.getParameter("page");
				String rs=request.getParameter("rows");
				if(p!=null&&!p.equals(""))page=Integer.parseInt(p);
				if(rs!=null&&rs.equals(""))rows=Integer.parseInt(rs);
				String  query="from CustomerCareness where 1=1";
				List<Object> key=new ArrayList<Object>();
				if(customerName!=null&&!customerName.equals(""))
				{
					query+=" and (customerName like ? )";
					key.add("%"+URLDecoder.decode(customerName,"UTF-8")+"%");
				}
				if(customerLevel!=null&&!customerLevel.equals(""))
				{
					query+=" and (customer.customerLevel = ? )";
					key.add(Integer.parseInt(customerLevel));
				}
				if(careContactor!=null&&!careContactor.equals(""))
				{
					query+=" and (careContactor like ? )";
					key.add("%"+URLDecoder.decode(careContactor,"UTF-8")+"%");
				}
				if(careDutySysUser!=null&&!careDutySysUser.equals(""))
				{
					query+=" and (sysUserByCareDutySysUserId.name like ? )";
					key.add(URLDecoder.decode(careDutySysUser,"UTF-8"));
				}
				if(code!=null&&!code.equals(""))
				{
					query+=" and (customer.code like ? )";
					key.add("%"+URLDecoder.decode(code,"UTF-8")+"%");
				}
				if(way!=null&&!way.equals(""))
				{
					query+=" and (way= ? )";
					key.add(Integer.valueOf(way));
				}
				if(startTime!=null&&!startTime.equals(""))
				{
					query+="and (convert(varchar(10),time,120) >= ?)";
					key.add(startTime);
				}
				if(endTime!=null&&!endTime.equals(""))
				{
					query+="and (convert(varchar(10),time,120) <= ?)";
					key.add(endTime);
				}
				CustomerCarenessManager ccm=new CustomerCarenessManager();
				List<CustomerCareness> lq=ccm.findPageAllByHQL(query, page, rows, key);
				if(lq!=null&&lq.size()>0)
				{
					for(CustomerCareness a:lq)
					{
						JSONObject ja=new JSONObject();
						ja.put("CustomerName",a.getCustomer().getName());
						ja.put("Priority",a.getPriority());
						ja.put("CreateTime",a.getCreateTime());
						ja.put("CreateSysUser",a.getSysUserByCreateSysUserId().getName());
						ja.put("Time",a.getTime());
						ja.put("Way",a.getWay());
						ja.put("CareDutySysUser",a.getSysUserByCareDutySysUserId().getName());
						ja.put("CareContactor",a.getCareContactor());
						ja.put("Remark",a.getRemark());
						ja.put("Status",a.getStatus());
						ja.put("Id",a.getId());
						ja.put("Fee",a.getFee());
						retArray.put(ja);
					}
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CrmServlet-->case 28", e);
				}else{
					log.error("error in CrmServlet-->case 28", e);
				}
			}
		finally{
			response.setContentType("text/json;charset=utf-8");
			response.getWriter().write(retArray.toString());
		}
		break;
		case 29://产值明细
			JSONObject retObj4 = new JSONObject();
			try{
				//String Code = request.getParameter("Code");
				//String CommissionType = request.getParameter("CommissionType");
				//String ReportType = request.getParameter("ReportType");
				String CustomerId = request.getParameter("CustomerId");
				//String ZipCode = request.getParameter("ZipCode");
				String RegionId = request.getParameter("RegionId");
				String Classi = request.getParameter("Classfication");
				//String CommissionDateFrom = request.getParameter("CommissionDateFrom");
				//String CommissionDateEnd = request.getParameter("CommissionDateEnd");
				//String FinishDateFrom = request.getParameter("FinishDateFrom");
				//String FinishDateEnd = request.getParameter("FinishDateEnd");
				String CheckOutDateFrom = request.getParameter("CheckOutDateFrom");
				String CheckOutDateEnd = request.getParameter("CheckOutDateEnd");
				String Status = "4";// request.getParameter("Status");
				//String Receiver = request.getParameter("Receiver");
				//String FinishUser = request.getParameter("FinishUser");
				//String CheckOutUser = request.getParameter("CheckOutUser");
				//String SpeciesType = request.getParameter("SpeciesType");
				//String ApplianceSpeciesId = request.getParameter("ApplianceSpeciesId");
				//String InsideContactor = request.getParameter("InsideContactor");
				String CustomerLevel=request.getParameter("CustomerLevel");
				
				String queryStr = "from CommissionSheet as model,Customer as c where model.customerId = c.id and 1=1 ";
				String queryStringAllFee = " select SUM(a.testFee),SUM(a.repairFee),SUM(a.materialFee),SUM(a.carFee),SUM(a.debugFee),SUM(a.otherFee),SUM(a.totalFee) " +
											" from CertificateFeeAssign as a, CommissionSheet as model, Customer as c " +
											" where model.customerId = c.id and a.commissionSheet.id = model.id and ( " +
											" 	(a.originalRecord is null and a.certificate is null) or " +
											"	(a.originalRecord in (from OriginalRecord as o where o.status<>1 and o.taskAssign.status<>1 and o.certificate = a.certificate)) " +
											" ) ";
				
				List<Object> keys = new ArrayList<Object>();

				if(CustomerLevel!=null&&!CustomerLevel.equals(""))
				{
					String cl=URLDecoder.decode(CustomerLevel,"UTF-8");
					queryStr+=" and c.customerLevel = ? ";
					queryStringAllFee+=" and c.customerLevel = ? ";
					keys.add(Integer.valueOf(cl));
				}
				if(CustomerId!=null&&!CustomerId.equals("")){
					String cusIdStr = URLDecoder.decode(CustomerId, "UTF-8");
					queryStr = queryStr + " and model.customerId = ?";
					queryStringAllFee = queryStringAllFee + " and model.customerId = ?";
					keys.add(Integer.valueOf(cusIdStr));
				}
		
				if(RegionId!=null&&!RegionId.equals("")){
					String RegionIdStr = URLDecoder.decode(RegionId, "UTF-8");
					queryStr = queryStr + " and c.region.id = ?";
					queryStringAllFee = queryStringAllFee + " and c.region.id = ?";
					keys.add(Integer.valueOf(RegionIdStr));
				}
				if(Classi!=null&&!Classi.equals(""))
				{
					String cusClassiStr = URLDecoder.decode(Classi, "UTF-8");
					queryStr = queryStr + " and c.classification like ?";
					queryStringAllFee = queryStringAllFee + " and c.classification like ?";
					keys.add("%" + cusClassiStr + "%");
				}
				
				if(CheckOutDateFrom!=null&&!CheckOutDateFrom.equals("")){
					Timestamp Start = Timestamp.valueOf(DateTimeFormatUtil.DateTimeFormat.format(Date.valueOf(URLDecoder.decode(CheckOutDateFrom, "utf-8"))));
					
					queryStr = queryStr + " and (model.checkOutDate >= ? )";
					queryStringAllFee = queryStringAllFee + " and (model.checkOutDate >= ? )";
					keys.add(Start);
					
				}
				if(CheckOutDateEnd!=null&&!CheckOutDateEnd.equals(""))
				{
					Timestamp End = Timestamp.valueOf(DateTimeFormatUtil.DateTimeFormat.format(Date.valueOf(URLDecoder.decode(CheckOutDateEnd, "utf-8"))));
					queryStr = queryStr + " and (model.checkOutDate <= ? )";
					queryStringAllFee = queryStringAllFee + " and (model.checkOutDate <= ? )";
					keys.add(End);
				}
				if(Status!=null&&!Status.equals("")){
					String statusStr = URLDecoder.decode(Status, "UTF-8");
					queryStr = queryStr + " and model.status = ? ";
					queryStringAllFee = queryStringAllFee + " and model.status = ? ";
					keys.add(Integer.valueOf(statusStr));
				}
				
				int page = 1;
				if (request.getParameter("page") != null)
					page = Integer.parseInt(request.getParameter("page").toString());
				int rows = 10;
				if (request.getParameter("rows") != null)
					rows = Integer.parseInt(request.getParameter("rows").toString());
				
				CommissionSheetManager cSheetMgr = new CommissionSheetManager();
				CertificateFeeAssignManager feeMgr=new CertificateFeeAssignManager();
				int total = 0;
				JSONArray options = new JSONArray();
				JSONArray foot = new JSONArray();
				
				List<CommissionSheet> result = cSheetMgr.findPageAllByHQL("select model " + queryStr + " order by model.commissionDate desc, model.id desc", page, rows, keys);
				total = cSheetMgr.getTotalCountByHQL("select count(model) " + queryStr, keys);

				if(result!=null&&result.size()>0){
					for(CommissionSheet cSheet : result){
						JSONObject option = new JSONObject();
						option.put("Code", cSheet.getCode());
						option.put("Pwd", cSheet.getPwd());
						option.put("CustomerName", cSheet.getCustomerName());
						option.put("CommissionDate", cSheet.getCommissionDate());
						option.put("ApplianceName", cSheet.getApplianceName());
						option.put("ApplianceModel", cSheet.getApplianceModel());
						if(cSheet.getSpeciesType()){	//器具授权（分类）名称
							option.put("SpeciesType", 1);	//器具分类类型
							ApplianceSpecies spe = (new ApplianceSpeciesManager()).findById(cSheet.getApplianceSpeciesId());
							if(spe != null){
								option.put("ApplianceSpeciesName", spe.getName());
								option.put("ApplianceSpeciesNameStatus", spe.getStatus());
							}else{
								continue;
							}
						}else{	//器具标准名称
							option.put("SpeciesType", 0);
							ApplianceStandardName stName = (new ApplianceStandardNameManager()).findById(cSheet.getApplianceSpeciesId());
							if(stName != null){
								option.put("ApplianceSpeciesName", stName.getName());
								option.put("ApplianceSpeciesNameStatus", stName.getStatus());
							}else{
								continue;
							}
						}
						option.put("ApplianceName", cSheet.getApplianceName()==null?"":cSheet.getApplianceName());	//器具名称（常用名称）
						option.put("ApplianceCode", cSheet.getAppFactoryCode());	//出厂编号
						option.put("AppManageCode", cSheet.getAppManageCode());	//管理编号
						option.put("Model", cSheet.getApplianceName()==null?"":cSheet.getApplianceModel()==null?"":cSheet.getApplianceModel());	//型号规格
						option.put("Range", cSheet.getRange());		//测量范围
						option.put("Accuracy", cSheet.getAccuracy());	//精度等级
						option.put("Manufacturer", cSheet.getManufacturer()==null?"":cSheet.getManufacturer());	//制造厂商
						option.put("Quantity", cSheet.getQuantity()==null?"":cSheet.getQuantity());	//台/件数
						option.put("MandatoryInspection", cSheet.getMandatory()?1:0);	//强制检验
						option.put("Urgent", cSheet.getUrgent()?1:0);	//加急
						option.put("Trans", cSheet.getSubcontract()?1:0);	//转包
						if(!cSheet.getSubcontract()){	//0：转包
							List<SubContract> subRetList = (new SubContractManager()).findByVarProperty(new KeyValueWithOperator("commissionSheet.id", cSheet.getId(),"="), new KeyValueWithOperator("status", 0, "="));
							if(subRetList != null && subRetList.size() > 0){
								option.put("SubContractor", subRetList.get(0).getSubContractor().getName());	//转包方
							}else{
								option.put("SubContractor", "");	//转包方
							}
						}else{
							option.put("SubContractor", "");
						}
						option.put("Appearance", cSheet.getAppearance()==null?"":cSheet.getAppearance());	//外观附件
						option.put("Repair", cSheet.getRepair()?1:0);	//修理
						option.put("ReportType", cSheet.getReportType());	//报告形式
						option.put("OtherRequirements", cSheet.getOtherRequirements()==null?"":cSheet.getOtherRequirements());	//其它要求
						option.put("Location", cSheet.getLocation()==null?"":cSheet.getLocation());	//存放位置
						option.put("Allotee", cSheet.getAllotee()==null?"":cSheet.getAllotee());	//派定人
						SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
						option.put("CommissionDate", sf.format(cSheet.getCommissionDate()));		//委托日期
						option.put("Status", cSheet.getStatus());	//委托单状态
						
						/***********在CertificateFeeAssign(原始记录证书费用分配)中查找委托单的费用详情***********/
						
						List<Object[]> FList=feeMgr.findByHQL(CertificateFeeAssignManager.queryStringAllAllFeeByCommissionSheetId, cSheet.getId());		
																
						if(FList.isEmpty()){
							option.put("TestFee", 0.0);
					    	option.put("RepairFee", 0.0);
							option.put("MaterialFee", 0.0);
							option.put("CarFee", 0.0);
							option.put("DebugFee", 0.0);
							option.put("OtherFee", 0.0);
							option.put("TotalFee", 0.0);
					    }else{
						    for(Object[] fee:FList){							    	
						    	option.put("TestFee", (Double)fee[0]==null?0.0:(Double)fee[0]);
								option.put("RepairFee", (Double)fee[1]==null?0.0:(Double)fee[1]);
								option.put("MaterialFee", (Double)fee[2]==null?0.0:(Double)fee[2]);
								option.put("CarFee", (Double)fee[3]==null?0.0:(Double)fee[3]);
								option.put("DebugFee", (Double)fee[4]==null?0.0:(Double)fee[4]);
								option.put("OtherFee", (Double)fee[5]==null?0.0:(Double)fee[5]);
								option.put("TotalFee", (Double)fee[6]==null?0.0:(Double)fee[6]);
						   }
					    }
						String hqlQueryString_WithdrawQuantity = "select sum(model.number) from Withdraw as model where model.commissionSheet.id=? and model.executeResult=?";	//已批准的退样器具数量
						List<Long> wQuantityList = cSheetMgr.findByHQL(hqlQueryString_WithdrawQuantity, cSheet.getId(), true);	//退样器具数量
						if(wQuantityList != null && wQuantityList.size() > 0 && wQuantityList.get(0) != null){
							option.put("WithdrawQuantity", ((Long)wQuantityList.get(0)).intValue());	//有效器具数量
						}else{
							option.put("WithdrawQuantity", 0);
						}
						
						String hqlQueryString_FinishQuantity = "select sum(model.quantity) from OriginalRecord as model where model.commissionSheet.id=? and model.status<>1 and model.taskAssign.status<>1 and model.verifyAndAuthorize.authorizeResult=? and model.verifyAndAuthorize.isAuthBgRuning is null ";	//签字通过的原始记录的器具总数(签字已通过且不是正在后台执行)
						String hqlQueryString_WithdrawQuantity1 = "select sum(model.number) from Withdraw as model where model.commissionSheet.id=? and model.executeResult=?";	//已批准的退样器具数量
						//查询完工器具数量和退样器具数量，以及是否转包
						List<Long> fQuantityList = cSheetMgr.findByHQL(hqlQueryString_FinishQuantity, cSheet.getId(), true);	//完工器具数量
						if(fQuantityList != null && fQuantityList.size() > 0 && fQuantityList.get(0) != null){
							option.put("FinishQuantity", fQuantityList.get(0));	//完工器具数量
						}else{
							option.put("FinishQuantity", 0);
						}
						List<Long> wQuantityList1 = cSheetMgr.findByHQL(hqlQueryString_WithdrawQuantity1, cSheet.getId(), true);	//退样器具数量
						if(wQuantityList1 != null && wQuantityList1.size() > 0 && wQuantityList1.get(0) != null){
							option.put("EffectQuantity", cSheet.getQuantity() - ((Long)wQuantityList1.get(0)).intValue());	//有效器具数量
						}else{
							option.put("EffectQuantity", cSheet.getQuantity());
						}
						String hqlQueryString_SubContract = "select count(*) from SubContract as model where model.commissionSheet.id=? and model.status<>1 and model.receiveDate is not null";
						int iSubContract = cSheetMgr.getTotalCountByHQL(hqlQueryString_SubContract, cSheet.getId());
						if(iSubContract > 0 || cSheet.getCommissionType() == 5){	//该委托单有转包(或该委托单为其他业务)，则完工确认时不需要判断‘完工器具数量是否大于等于有效器具数量’;
							option.put("IsSubContract", true);
						}else{
							option.put("IsSubContract", false);
						}
												
						options.put(option);
					}
				}
				List<Object[]> feeList = feeMgr.findByHQL(queryStringAllFee, keys);
				JSONObject jsonObj = new JSONObject();
				
				if(feeList.isEmpty()){
					jsonObj.put("CustomerName", "总计");
					jsonObj.put("TestFee", 0.0);
					jsonObj.put("RepairFee", 0.0);
					jsonObj.put("MaterialFee", 0.0);
					jsonObj.put("CarFee", 0.0);
					jsonObj.put("DebugFee", 0.0);
					jsonObj.put("OtherFee", 0.0);
					jsonObj.put("TotalFee", 0.0);
			    }else{
				    for(Object[] fee:feeList){
						jsonObj.put("CustomerName", "总计");
				    	jsonObj.put("TestFee", (Double)fee[0]==null?0.0:(Double)fee[0]);
				    	jsonObj.put("RepairFee", (Double)fee[1]==null?0.0:(Double)fee[1]);
				    	jsonObj.put("MaterialFee", (Double)fee[2]==null?0.0:(Double)fee[2]);
				    	jsonObj.put("CarFee", (Double)fee[3]==null?0.0:(Double)fee[3]);
				    	jsonObj.put("DebugFee", (Double)fee[4]==null?0.0:(Double)fee[4]);
				    	jsonObj.put("OtherFee", (Double)fee[5]==null?0.0:(Double)fee[5]);
				    	jsonObj.put("TotalFee", (Double)fee[6]==null?0.0:(Double)fee[6]);
				   }
			    }
				foot.put(jsonObj);
				retObj4.put("total", total);
				retObj4.put("rows", options);
				retObj4.put("footer", foot);
			}catch(Exception e){
				try{
					retObj4.put("total", 0);
					retObj4.put("rows", new JSONArray());
					JSONArray foot = new JSONArray();
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("CustomerName", "总计");
					jsonObj.put("TestFee", 0.0);
					jsonObj.put("RepairFee", 0.0);
					jsonObj.put("MaterialFee", 0.0);
					jsonObj.put("CarFee", 0.0);
					jsonObj.put("DebugFee", 0.0);
					jsonObj.put("OtherFee", 0.0);
					jsonObj.put("TotalFee", 0.0);
					foot.put(jsonObj);
					retObj4.put("footer", foot);
				} catch(Exception e1){
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in StatisticServlet-->case 29", e);
				}else{
					log.error("error in StatisticServlet-->case 29", e);
				}
			}finally{
				response.setContentType("text/json;charset=utf-8");
				response.getWriter().write(retObj4.toString());
			}
			break;
			
		case 30://产值下限
			JSONArray retObj5 = new JSONArray();
			JSONObject ret5=new JSONObject();
			try{
				String CheckOutDateFrom = request.getParameter("CheckOutDateFrom");
				String CheckOutDateEnd = request.getParameter("CheckOutDateEnd");
				String Status ="4";// request.getParameter("Status");
				
				String Type=request.getParameter("Type");
				String LeastOutput=request.getParameter("LeastOutput");
				String qselect="";
				String qfrom="";
				String qwhere="";
				String qgroupby="";
				String qhaving="";
				String q="";
				List<Object> keys = new ArrayList<Object>();
				int page = 1;
				if (request.getParameter("page") != null)
					page = Integer.parseInt(request.getParameter("page").toString());
				int rows = 10;
				if (request.getParameter("rows") != null)
					rows = Integer.parseInt(request.getParameter("rows").toString());
				
				CommissionSheetManager cSheetMgr = new CommissionSheetManager();
				//if(Type.equals("1"))//按单位查
				{
					qselect+=" select c.id as ID,c.name as name,sum(a.totalFee) as totalFee,MIN(b.commissionDate) as startDate," +
							"MAX(b.commissionDate) as endDate,COUNT(b.id) as counts,AVG(a.totalFee) as avgs " +
							",sum(a.testFee) as testFee,sum(a.repairFee) as repairFee,sum(a.materialFee) as materialFee," +
							"sum(a.debugFee) as debugFee,sum(a.carFee) as carFee,sum(a.otherFee) as otherFee";
					
					qfrom+=" from CertificateFeeAssign as a,CommissionSheet as b,Customer as c";
					qwhere+=" where c.id=b.customerId and b.id=a.commissionSheet.id and b.status=4";
					qgroupby+=" group by c.id, c.name";
					qhaving+=" having sum(a.totalFee)> ? ";
					String q1=" select c.id as ID"+qfrom+qwhere+qgroupby+qhaving;
					keys.add(Double.valueOf(LeastOutput));
					q=qselect+qfrom+qwhere+qgroupby+qhaving;
					List<Object[]> lo=cSheetMgr.findPageAllByHQL(q, page, rows, keys);
					List<Object[]> total=cSheetMgr.findPageAllByHQL("select count(tmp.id) from Customer as tmp where tmp.id in ("+q1+") ", page, rows, keys);
					int totals=0;
					if(total!=null&&total.size()==1)
					{
						Object o=total.get(0);
						String sr=o.toString();
							totals=Integer.parseInt(sr);
						
					}
					if(lo!=null&&lo.size()>0)
					{
						for(Object[] a:lo)
						{
						JSONObject tmp=new JSONObject();
						tmp.put("Id",a[0].toString());
						tmp.put("CustomerName",a[1].toString());
						tmp.put("TotalFee",a[2].toString());
						tmp.put("CommissionDateStart",a[3].toString());
						tmp.put("CommissionDateEnd",a[4].toString());
						tmp.put("Count",a[5].toString());
						tmp.put("Avg",a[6].toString());
						tmp.put("TestFee",a[7].toString());
						tmp.put("RepairFee",a[8].toString());
						tmp.put("MaterialFee",a[9].toString());
						tmp.put("DebugFee",a[10].toString());
						tmp.put("CarFee",a[11].toString());
						tmp.put("OtherFee",a[12].toString());
						retObj5.put(tmp);
						}
					}
					ret5.put("total",totals);
					ret5.put("rows", retObj5);
					
					
				}
				}
			catch(Exception e)
			{
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in StatisticServlet-->case 30", e);
				}else{
					log.error("error in StatisticServlet-->case 30", e);
				}
			}finally{
				response.setContentType("text/json;charset=utf-8");
				response.getWriter().write(ret5.toString());
			}
			break;
		case 31://内部联系人产值统计
			JSONArray retJsonArray=new JSONArray();
			try 
			{
				String insideContactorId=request.getParameter("InsideContactorId");
				String startDate=request.getParameter("StartDate");
				String endDate=request.getParameter("EndDate");
				
				int page = 1;
				if (request.getParameter("page") != null)
					page = Integer.parseInt(request.getParameter("page").toString());
				int rows = 10;
				if (request.getParameter("rows") != null)
					rows = Integer.parseInt(request.getParameter("rows").toString());

				InsideContactorFeeAssignManager icfam=new InsideContactorFeeAssignManager();
				String queryString="select c.name,b.name,a.year,a.fee,a.sysUserByLastEditorId.name,a.lastEditTime,a.remark" +
						" from InsideContactorFeeAssign as a,SysUser as b,Customer as c" +
						" where a.sysUserByInsideContactorId.id=b.id and c.id=a.customer.id";
				List<Object> key=new ArrayList<Object>();
				if(insideContactorId!=null&&!insideContactorId.equals(""))
				{
					queryString+=" and b.id=? ";
					key.add(Integer.valueOf(insideContactorId));
				}
				if(startDate!=null&&!startDate.equals(""))
				{
					queryString+=" and a.year >= ?";
					key.add(Integer.valueOf((startDate.substring(0, 4))));
				}
				if(endDate!=null&&!endDate.equals(""))
				{
					queryString+=" and a.year <= ?";
					key.add(Integer.valueOf(endDate.substring(0, 4)));
				}
				List<Object[]> listobj=icfam.findPageAllByHQL(queryString, page, rows, key);
				
				for(Object [] a:listobj)
				{
					JSONObject tmp=new JSONObject();
					tmp.put("CustomerName", a[0].toString());
					tmp.put("InsideContactor", a[1].toString());
					tmp.put("Year",a[2].toString() );
					tmp.put("Fee",a[3].toString() );
					tmp.put("LastEditTime", a[4].toString());
					tmp.put("LastEditor", a[5].toString());
					tmp.put("Remark", a[6]);
					retJsonArray.put(tmp);
				}
			
			} catch (Exception e) {
				// TODO: handle exception
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in StatisticServlet-->case 31", e);
				}else{
					log.error("error in StatisticServlet-->case 31", e);
				}
			}
			finally{
				response.setContentType("text/json;charset=utf-8");
				response.getWriter().write(retJsonArray.toString());
			}
			break;
		case 32://器具大类combobox控件
			JSONArray jsonArray2=new JSONArray();
			try 
			{
				ApplianceSpeciesManager asm=new ApplianceSpeciesManager();
				String query="from ApplianceSpecies where parentSpecies is null";
				List<Object> key=new ArrayList<Object>();
				//key.add(new Integer(1));//=asm.findByVarProperty(new KeyValueWithOperator("id",1,"="));//
				List<ApplianceSpecies> la=asm.findPageAllByHQL(query, 1, 50, key);

				for(ApplianceSpecies a:la)
				{
					JSONObject tmp=new JSONObject();
					tmp.put("Id",a.getId());
					tmp.put("Name",a.getName());
					jsonArray2.put(tmp);					
				}
			} catch (Exception e) {
				// TODO: handle exception
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in StatisticServlet-->case 32", e);
				}else{
					log.error("error in StatisticServlet-->case 32", e);
				}
			}finally{
				response.setContentType("text/json;charset=utf-8");
				response.getWriter().write(jsonArray2.toString());
			}
			break;
		case 33://器具大类统计
			JSONObject fret=new JSONObject();
			JSONArray rjs=new JSONArray();
		try 
		{
			//double total=0.0;
			CommissionSheetManager csm=new CommissionSheetManager();
			ApplianceSpeciesManager asm=new ApplianceSpeciesManager();
			int page = 1;
			if (request.getParameter("page") != null)
				page = Integer.parseInt(request.getParameter("page").toString());
			int rows = 10;
			if (request.getParameter("rows") != null)
				rows = Integer.parseInt(request.getParameter("rows").toString());
			List<Object> keys=new ArrayList<Object>();
			String query="select*from (select s.SpeciesId as SPeciesId,SUM(tmp2.total) as TOTAL,SUM(tmp2.test) as TEST,SUM(tmp2.repair) as REPAIR," +
					"SUM(tmp2.material) as MATERIAL,SUM(tmp2.debug) as DEBUG,SUM(tmp2.car) as CAR,SUM(tmp2.other) as OTHER from" +
					"(select t.StandardNameId as STD_NameID,sum(tmp1.TOTAL) as total,sum(tmp1.TEST) as test,SUM(tmp1.REPAIR) as repair," +
					"SUM(tmp1.MATERIAL) as material,SUM(tmp1.DEBUG) as debug,SUM(tmp1.CAR) as car,SUM(tmp1.OTHER) as other from" +
					"(select o.TgtAppId as TAGId,SUM(o.TotalFee) as TOTAL,SUM(o.TestFee) as TEST,SUM(o.RepairFee) as REPAIR," +
					"SUM(o.MaterialFee) as MATERIAL,SUM(o.DebugFee) as DEBUG,SUM(o.CarFee) as CAR,SUM(o.OtherFee)as OTHER from OriginalRecord as o " +
					"where o.Status=0 and o.CommissionSheetId  in (select Id from CommissionSheet as c where c.Status=4) " +
					"group by o.TgtAppId)tmp1 left outer join targetappliance as t on t.Id=tmp1.TAGId group by t.StandardNameId)tmp2 " +
					"left outer join ApplianceStandardName as s on tmp2.STD_NameID=s.Id group by s.SpeciesId)tmp3 " +
					"left outer join ApplianceSpecies as ap on ap.Id=tmp3.SPeciesId";
			Sql sql=new Sql();
			Connection conn=sql.getMyConn();
			double []rec;
			String query2="select * from ApplianceSpecies as a where a.ParentId is null";
			PreparedStatement ps1=conn.prepareStatement(query2);
			ResultSet rs1=ps1.executeQuery();
			int i=0;
			HashMap<Integer, List<Double>> h=new HashMap<Integer, List<Double>>();
			while(rs1.next())
			{
				
				List<Double> lDoubles=new ArrayList<Double>();
				lDoubles.add(0.0);
				lDoubles.add(0.0);
				lDoubles.add(0.0);
				lDoubles.add(0.0);
				lDoubles.add(0.0);
				lDoubles.add(0.0);
				lDoubles.add(0.0);
				h.put(rs1.getInt(1), lDoubles);
			}
			
			
			PreparedStatement ps=conn.prepareStatement(query);		
			ResultSet rs=ps.executeQuery();
			
			while(rs.next())
			{
				ApplianceSpecies save=asm.findById(rs.getInt(9));
				ApplianceSpecies tmp=save;
				while(tmp!=null)
				{
					save=tmp;
					tmp=tmp.getParentSpecies();
				}
				Integer id=save.getId();
				List<Double> fees=new ArrayList<Double>();
				List<Double> tmpfee=h.get(id);
				
				double total=tmpfee.get(0)+rs.getDouble(2);//totalfee
				double test=tmpfee.get(1)+rs.getDouble(3);//testfee
				double repair=tmpfee.get(2)+rs.getDouble(4);//repairfee
				double material=tmpfee.get(3)+rs.getDouble(5);//material
				double debug=tmpfee.get(4)+rs.getDouble(6);//debugfee
				double car=tmpfee.get(5)+rs.getDouble(7);//carfee
				double other=tmpfee.get(6)+rs.getDouble(8);//otherfee
				fees.add(total);
				fees.add(test);
				fees.add(repair);
				fees.add(material);
				fees.add(debug);
				fees.add(car);
				fees.add(other);
				
				h.remove(id);
				h.put(id, fees);
			}
			Iterator iter = h.entrySet().iterator();
			while (iter.hasNext()) 
			{
				if(++i>(page)*rows)break;
				if(i<(page-1)*rows)continue;
			JSONObject ja=new JSONObject();
			Map.Entry entry = (Map.Entry) iter.next();
			Integer key = Integer.parseInt(entry.getKey().toString());
			List<Double> val =(List<Double>) entry.getValue();
			ja.put("Id", key);
			ja.put("Name", asm.findById(key)==null?"":asm.findById(key).getName());
			ja.put("TotalFee", val.get(0));
			ja.put("TestFee", val.get(1));
			ja.put("RepairFee", val.get(2));
			ja.put("MaterialFee", val.get(3));
			ja.put("DebugFee", val.get(4));
			ja.put("CarFee", val.get(5));
			ja.put("OtherFee", val.get(6));
			rjs.put(ja);
			} 
			fret.put("rows", rjs);
			fret.put("total", h.size());
				
			}
		catch (Exception e) 
		{
			// TODO: handle exception
			if(e.getClass() == java.lang.Exception.class){	//自定义的消息
				log.debug("exception in StatisticServlet-->case 33", e);
			}else{
				log.error("error in StatisticServlet-->case 33", e);
			}
		}
		finally
		{
			response.setContentType("text/json;charset=utf-8");
			response.getWriter().write(fret.toString());
		}
		break;
		case 34://同比环比
			JSONArray jsa1=new JSONArray();
			JSONObject retJson=new JSONObject();
			try 
			{
				String startDateString=request.getParameter("StartDate");
				String endDateString=request.getParameter("EndDate");
				int page = 1;
				if (request.getParameter("page") != null)
					page = Integer.parseInt(request.getParameter("page").toString());
				int rows = 10;
				if (request.getParameter("rows") != null)
					rows = Integer.parseInt(request.getParameter("rows").toString());
				/*String q1="use czjl_new select a.Id,a.Name,SUM(b.TestFee),SUM(b.DebugFee)," +
						"SUM(b.RepairFee),SUM(b.MaterialFee),SUM(b.CarFee ),SUM(b.OtherFee),SUM(b.TotalFee),AVG(b.TotalFee) as o" +
						" from Customer as a  left outer join CommissionSheet as b";
				String on=" on a.Id=b.CustomerId" ;
				String where=" where (b.Status is null) or (b.Status=4 and b.CommissionDate>=? and b.CommissionDate<=?)";
				
				String group=" group by a.Id,a.Name order by o desc";*/
				
				String query="use czjl_new select tmp.ID,tmp.NAME,tmp.TEST as test1,tmp.DEBUG as debug1,tmp.MATERIAL as material1," +
						"tmp.CAR as car1,tmp.REPAIR as repair1,tmp.OTHER as other1,tmp.AVG as avg1,tmp.TOTAL as total1," +
						"tm2.TEST as test2,tm2.DEBUG as debug2,tm2.MATERIAL as material2,tm2.CAR as car2,tm2.REPAIR as repair2," +
						"tm2.OTHER as other2,tm2.AVG as avg2,tm2.TOTAL ,(tm2.TOTAL-tmp.TOTAL)as Increase ,tmp.CID,tm2.CID " +
						"from (select a.Id as ID,count(a.Id) as CID,a.Name as NAME,SUM(b.TestFee) as TEST,SUM(b.DebugFee) as DEBUG," +
						"SUM(b.RepairFee) AS REPAIR,SUM(b.MaterialFee) AS MATERIAL,SUM(b.CarFee ) AS CAR," +
						"SUM(b.OtherFee) AS OTHER,SUM(b.TotalFee) AS TOTAL,AVG(b.TotalFee) as AVG " +
						"from Customer as a  left outer join CommissionSheet as b " +
						"on a.Id=b.CustomerId " +
						"where  ( b.status=4 and b.CommissionDate>=? and b.CommissionDate<=?) or ( b.Status is null)" +
						" group by a.Id,a.Name ) tmp full outer join " +
						"(select a.Id as ID,count(a.Id) as CID,a.Name as NAME,SUM(b.TestFee) as TEST,SUM(b.DebugFee) as DEBUG," +
						"SUM(b.RepairFee) AS REPAIR,SUM(b.MaterialFee) AS MATERIAL,SUM(b.CarFee ) AS CAR," +
						"SUM(b.OtherFee) AS OTHER,SUM(b.TotalFee) AS TOTAL,AVG(b.TotalFee) as AVG " +
						"from Customer as a  left outer join CommissionSheet as b on a.Id=b.CustomerId " +
						"where  ( b.status=4 and b.CommissionDate>=? and b.CommissionDate<=?) or ( b.Status is null)" +
						" group by a.Id,a.Name ) tm2 " +
						"on tmp.ID=tm2.ID " +
						"where tmp.TOTAL is not null and tm2.TOTAL is not null";
				
				Sql sql=new Sql();
				Connection conn=sql.getMyConn();
				//if(conn!=null)
					//Statement stmt=conn.createStatement();
					PreparedStatement ps=conn.prepareStatement(query);//q1+on+where+group);
					
					Integer year=Integer.parseInt(startDateString.substring(0, 4))-1;
					
					ps.setString(1,year.toString()+startDateString.substring(4,10));
					ps.setString(2,year.toString()+endDateString.substring(4,10));
					ps.setString(3, startDateString);
					ps.setString(4, endDateString);
					/*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					java.sql.Date date=new java.sql.Date(2010,1,1);//sdf.parse("2010-01-01 00:00:00").getTime());
					ps.setDate(1, date);
					java.sql.Date date1=new java.sql.Date(2012,1,1);//sdf.parse("2013-01-01 00:00:00").getTime());
					ps.setDate(2, date1);*/
				//----执行存储过程：stmt.execute(<String sql>)
				//----以批处理方式执行多个语句：stmt.executeBatch()
				//----更新处理：   int updateCount=stmt.executeUpdate(<String  sql>)
				/*----查询 */            
				ResultSet rs=ps.executeQuery();
				/*常用的是:*/  
				int  tatal=0;
				int i=0,k=0;
				while (rs.next())
				{
					if(k++<(page-1)*rows)continue;
					++tatal;
					/*if(i>=rows)
						break;*/
					++i;
					if(i<=rows)
					{
					JSONObject tmp=new JSONObject();
					//tmp.put("Times",a[0].toString() );
					tmp.put("CustomerName",rs.getString(2));
					tmp.put("TestFee",rs.getDouble(3));
					tmp.put("DebugFee",rs.getDouble(4) );
					tmp.put("MaterialFee",rs.getDouble(5) );
					tmp.put("CarFee",rs.getDouble(6));
					tmp.put("RepairFee",rs.getDouble(7));
					tmp.put("OtherFee",rs.getDouble(8) );
					Double d=rs.getDouble(9) ;
					tmp.put("Avg",String.format("%.2f", d));
					tmp.put("TotalFee",rs.getDouble(10));

					tmp.put("TestFee2",rs.getDouble(11));
					tmp.put("DebugFee2",rs.getDouble(12) );
					tmp.put("MaterialFee2",rs.getDouble(13) );
					tmp.put("CarFee2",rs.getDouble(14));
					tmp.put("RepairFee2",rs.getDouble(15));
					tmp.put("OtherFee2",rs.getDouble(16) );
					d=rs.getDouble(17) ;
					tmp.put("Avg2",String.format("%.2f", d));
					tmp.put("TotalFee2",rs.getDouble(18));
					tmp.put("Increase",rs.getDouble(19));
					tmp.put("Times1",rs.getInt(20));
					tmp.put("Times2",rs.getInt(21));
					Double db=0.0;
					if(rs.getDouble(10)>0.0)db=new Double(rs.getDouble(19)/rs.getDouble(10))*100;
					tmp.put("IncreaseRatio",rs.getDouble(10)<=0.0?"-":String.format("%.2f",db)+"%");
					jsa1.put(tmp);
					}
				}
				
				/*key.add(startDateString);
				key.add(endDateString);
				Integer year=Integer.parseInt(startDateString.substring(0, 4))+1;
				key1.add(year.toString()+startDateString.substring(4,10));
				key1.add(year.toString()+endDateString.substring(4,10));
				CommissionSheetManager csManager=new CommissionSheetManager();
				List<Object[]> retObjects=csManager.findBySQL(q3, key2);
				int tatal=retObjects.size();*/
				retJson.put("total", tatal);
				retJson.put("rows",jsa1);
				conn.close();
				
			}
			catch (Exception e) 
			{
				// TODO: handle exception
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in StatisticServlet-->case 34", e);
				}else{
					log.error("error in StatisticServlet-->case 34", e);
				}
			}
			finally
			{
				response.setContentType("text/json;charset=utf-8");
				response.getWriter().write(retJson.toString());
			}
			break;
		case 35://分类到计量标准一级
			JSONObject returnobj=new JSONObject();
			try 
			{
				String cusID=request.getParameter("CustomerId");
				int page = 1;
				if (request.getParameter("page") != null)
					page = Integer.parseInt(request.getParameter("page").toString());
				int rows = 10;
				if (request.getParameter("rows") != null)
					rows = Integer.parseInt(request.getParameter("rows").toString());
				String  qString="select *from " +
						"(select SUM(total) as TOTAL,SUM(test) as TEST,SUM(debug) as DEBUG,sum(material) as MATERIAL," +
						"sum(car) as CAR,sum(repair) as REPAIR,sum(other) as Other,AVG(Avgs) as Avgs,asn.SpeciesId as SpeciesID " +
						"from (select sum(tmp2.TOTAL) as total,sum(tmp2.Test) as test,sum(tmp2.Debug) as debug," +
						"sum(tmp2.Material) as material,sum(tmp2.Car) as car,sum(tmp2.Repair) as repair,sum(tmp2.other) as other,AVG(tmp2.AVGS) as Avgs,t.StandardNameId as STD_NameID " +
						"from (select sum(c.TotalFee) as Total,SUM(c.TestFee) as Test,SUM(c.DebugFee) as Debug," +
						"SUM(c.MaterialFee) as Material,SUM(c.CarFee) as Car,SUM(c.RepairFee) as Repair,SUM(c.otherfee) as Other,AVG(c.TotalFee) as AVGS," +
						"o.TgtAppId as TAGETID from CertificateFeeAssign as c left outer join OriginalRecord as o on c.OriginalRecordId=o.Id " +
						"where (c.CommissionSheetId in (select Id from CommissionSheet as a where a.Status=4  " ;
			
						String qand=")) and o.Status=0 and c.CertificateId is not null and c.CommissionSheetId is not null " +
						"group by o.TgtAppId) tmp2 left join TargetAppliance as t on tmp2.TAGETID=t.Id " +
						"group by t.StandardNameId) tmp3 left join ApplianceStandardName as asn on tmp3.STD_NameID= asn.Id " +
						"group by SpeciesId)tmp4 left outer join ApplianceSpecies as ass on ass.Id=tmp4.SpeciesID";
						
						if(cusID!=null&&!cusID.equals(""))
						{
							qString+=" and a.CustomerId=? ";
						}
				Sql sql=new Sql();
				//sql.init();
				Connection conn=sql.getMyConn();
				PreparedStatement ps=conn.prepareStatement(qString+qand);
				if(cusID!=null&&!cusID.equals(""))
				{
					//ps.setInt(1, Integer.valueOf(cusID));
					ps.setString(1, cusID);
				}
				JSONArray jsas=new JSONArray();
				/*----查询 */            
				ResultSet rs=ps.executeQuery();
				/*常用的是:*/  
				int  tatal=0;
				int i=0,k=0;
				while (rs.next())
				{
					if(k++<(page-1)*rows)continue;
					++tatal;
					/*if(i>=rows)
						break;*/
					++i;
					if(i<=rows)
					{
					JSONObject tmp=new JSONObject();
					//tmp.put("Times",a[0].toString() );
					//tmp.put("CustomerName",rs.getString(2));
					tmp.put("TotalFee",rs.getDouble(1));
					tmp.put("TestFee",rs.getDouble(2));
					tmp.put("DebugFee",rs.getDouble(3) );
					tmp.put("MaterialFee",rs.getDouble(4) );
					tmp.put("CarFee",rs.getDouble(5));
					tmp.put("RepairFee",rs.getDouble(6));
					tmp.put("OtherFee",rs.getDouble(7) );
					Double db=new Double(rs.getDouble(8));
					tmp.put("Avg",String.format("%.2f", db) );
					
					tmp.put("SpeciesId",rs.getInt(10) );
					tmp.put("Name",rs.getString(12) );
				
					//Double db=new Double(rs.getDouble(19)/rs.getDouble(10))*100;
					//tmp.put("IncreaseRatio",rs.getDouble(10)==0.0?"-":String.format("%.2f",db)+"%");
					jsas.put(tmp);
					}
				}
				returnobj.put("rows", jsas);
				returnobj.put("total", tatal);
				conn.close();
			} catch (Exception e) {
				// TODO: handle exception
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in StatisticServlet-->case 35", e);
				}else{
					log.error("error in StatisticServlet-->case 35", e);
				}
			}finally
			{
				response.setContentType("text/json;charset=utf-8");
				response.getWriter().write(returnobj.toString());
			}
			break;
		case 36://按年份统计-----continus3Year.jsp
			JSONObject returnObject=new JSONObject();
			try 
			{
				String custIdString=request.getParameter("CustomerId");
				int page = 1;
				if (request.getParameter("page") != null)
					page = Integer.parseInt(request.getParameter("page").toString());
				int rows = 10;
				if (request.getParameter("rows") != null)
					rows = Integer.parseInt(request.getParameter("rows").toString());
				String qString="select year(a.CommissionDate) as  Y,MONTH(a.CommissionDate) as M, " +
						"COUNT(a.Id) as counter,SUM(a.TotalFee ) as total ,SUM(a.TestFee) as test,SUM(a.DebugFee) as debug," +
						"SUM(a.MaterialFee) as material,SUM(a.CarFee) as car,SUM(a.RepairFee) as repair,SUM(a.OtherFee) as other,AVG(a.TotalFee) as avgs " +
						"from CommissionSheet as a where a.Status=4 " ;
				String qString1="group by year(a.CommissionDate),MONTH(a.CommissionDate) order by Y,M";
				
				Sql sql=new Sql();
				Connection conn=sql.getMyConn();
				if(custIdString!=null&&!custIdString.equals(""))qString+=" and a.CustomerId= ? ";
				PreparedStatement ps=conn.prepareStatement(qString+qString1);
				if(custIdString!=null&&!custIdString.equals(""))ps.setString(1, custIdString);
				JSONArray jsas=new JSONArray();            
				ResultSet rs=ps.executeQuery();
				int  tatal=0;
				int i=0,k=0;
				while (rs.next())
				{
					if(k++<(page-1)*rows)continue;
					++tatal;
					++i;
					if(i<=rows)
					{
					JSONObject tmp=new JSONObject();
					//tmp.put("Times",a[0].toString() );
					//tmp.put("CustomerName",rs.getString(2));
					tmp.put("Year",rs.getInt(1));
					tmp.put("Month",rs.getInt(2));
					tmp.put("counter",rs.getInt(3) );
					tmp.put("TotalFee",rs.getDouble(4));
					tmp.put("TestFee",rs.getDouble(5));
					tmp.put("DebugFee",rs.getDouble(6));
					tmp.put("MaterialFee",rs.getDouble(7) );
					tmp.put("CarFee",rs.getDouble(8));
					tmp.put("RepairFee",rs.getDouble(9));
					tmp.put("OtherFee",rs.getDouble(10) );
					Double db=new Double(rs.getDouble(11));
					tmp.put("Avg",String.format("%.2f", db) );
					jsas.put(tmp);
					}
				}
				returnObject.put("rows", jsas);
				returnObject.put("total", tatal);
				conn.close();
			} catch (Exception e) {
				// TODO: handle exception
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in StatisticServlet-->case 36", e);
				}else{
					log.error("error in StatisticServlet-->case 36", e);
				}
			}finally
			{
				response.setContentType("text/json;charset=utf-8");
				response.getWriter().write(returnObject.toString());
			}
			break;
		case 37://等级设置页面查询
			JSONObject returnObject1=new JSONObject();
			try {
				String cusid=request.getParameter("Id");
				int page = 1;
				if (request.getParameter("page") != null)
					page = Integer.parseInt(request.getParameter("page").toString());
				int rows = 10;
				if (request.getParameter("rows") != null)
					rows = Integer.parseInt(request.getParameter("rows").toString());
				String qString="select a.Id,a.Name,SUM(b.TotalFee) as TOTAL,AVG(b.TotalFee) as AVGS,COUNT(b.TotalFee) as cc" +
						" ,SUM(b.TotalFee)/? as percentage,a.CustomerValueLevel " +
						"from Customer as a left outer join CommissionSheet as b on a.Id=b.CustomerId " ;
				String qString2="group by a.Id,a.Name,a.CustomerValueLevel order by percentage desc, TOTAL desc, cc desc";
				String qString1="select sum(a.TotalFee) from CommissionSheet as a";
				Sql sql=new Sql();
				Connection conn=sql.getMyConn();
				double Tot=1;
				PreparedStatement ps1=conn.prepareStatement(qString1);
				ResultSet rs1=ps1.executeQuery();
				if(rs1.next())
				{
					Tot=rs1.getDouble(1);
					
				}
				if(cusid!=null&&!cusid.equals(""))qString+=" where a.Id= ? ";
					
				PreparedStatement ps=conn.prepareStatement(qString+qString2);
				if(cusid!=null&&!cusid.equals(""))ps.setString(2, cusid);
				JSONArray jsas=new JSONArray();        
				ps.setDouble(1, Tot);
				ResultSet rs=ps.executeQuery();
				int  tatal=0;
				int i=0,k=0;
				while (rs.next())
				{
					if(k++<(page-1)*rows)continue;
					++tatal;
					++i;
					if(i<=rows)
					{
					JSONObject tmp=new JSONObject();
					//tmp.put("Times",a[0].toString() );
					tmp.put("Id",rs.getInt(1));
					tmp.put("Name",rs.getString(2));
					tmp.put("Total",rs.getDouble(3) );
					Double db=new Double(rs.getDouble(4));
					tmp.put("Avg",String.format("%.2f", db));
					tmp.put("Counter",rs.getInt(5));
					db=new Double(rs.getDouble(6)*100);
					tmp.put("Percentage",String.format("%.4f", db)+"%" );
					tmp.put("Level",rs.getInt(7));
					jsas.put(tmp);
					}
				}
				returnObject1.put("rows", jsas);
				returnObject1.put("total", tatal);
				conn.close();
				
			
			} catch (Exception e) {
				// TODO: handle exception
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in StatisticServlet-->case 37", e);
				}else{
					log.error("error in StatisticServlet-->case 37", e);
				}
			}finally
			{
				response.setContentType("text/json;charset=utf-8");
				response.getWriter().write(returnObject1.toString());
			}
			break;
		case 38:
			JSONObject jreturn=new JSONObject();
		try 
		{
			String code=request.getParameter("CheckCode");
			CustomerManager cm=new CustomerManager();
			List<Customer> lr=cm.findByVarProperty(new KeyValueWithOperator("code",code,"="));
			if(lr!=null&&lr.size()>=1)
			{
				jreturn.put("IsOk", false);
				jreturn.put("msg", "企业代码不唯一！\n请修改！");
			}
			else jreturn.put("IsOk", true);
			
		} catch (Exception e) {
			// TODO: handle exception
			if(e.getClass() == java.lang.Exception.class){	//自定义的消息
				log.debug("exception in StatisticServlet-->case 38", e);
			}else{
				log.error("error in StatisticServlet-->case 38", e);
			}
		}finally
		{
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(jreturn.toString());
			
		}break;
		case 39://查询内部联系人到combobox
			JSONObject jret=new JSONObject();
			JSONArray tmpj=new JSONArray();
			try {
				String cusid=request.getParameter("CustomerId");
				int page = 1;
				if (request.getParameter("page") != null)
					page = Integer.parseInt(request.getParameter("page").toString());
				int rows = 10;
				if (request.getParameter("rows") != null)
					rows = Integer.parseInt(request.getParameter("rows").toString());
				
				String queryString="select a.name,a.address,c.name,c.jobNum,c.cellphone1,c.id,b.id " +
						"from Customer as a,InsideContactor as b,SysUser as c " +
						"where a.id=b.customer.id and b.sysUser.id=c.id ";
				List<Object> keys = new ArrayList<Object>();
				if(cusid!=null&&!cusid.equals(""))
				{
					queryString+=" and a.id = ?";
					keys.add(Integer.valueOf(cusid));
				}
				InsideContactorManager icm=new InsideContactorManager();
				List<Object[]> lo=icm.findPageAllByHQL(queryString, page, rows, keys);
				
					for(Object[] a:lo)
					{
						JSONObject tmp =new JSONObject();
						tmp.put("CustomerName",a[0].toString());
						tmp.put("Address", a[1].toString());
						tmp.put("ContactorName", a[2].toString());
						tmp.put("JobNum", a[3].toString());
						tmp.put("Cellphone1",a[4].toString());
						//tmp.put("Cellphone1",a[4].toString());
						tmp.put("ContactorId",a[5].toString()/*.toString()*/);
						tmp.put("Id", a[6].toString());
						tmpj.put(tmp);
					}
					//j.put("total", to);
					//j.put("rows", jtmp);	
			} catch (Exception e) {
				// TODO: handle exception
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in StatisticServlet-->case 39", e);
				}else{
					log.error("error in StatisticServlet-->case 39", e);
				}
			}
			finally
			{
				response.setContentType("text/json;charset=utf-8");
				response.getWriter().write(tmpj.toString());
			}break;
		case 40://等级设置
			JSONObject retj40=new JSONObject();
			try {
				String idString=request.getParameter("Id");
				String levelString=request.getParameter("Level");
				CustomerManager c=new CustomerManager();
				Customer customer=c.findById(Integer.valueOf(idString));
				if(customer!=null)
				{
					customer.setCustomerValueLevel(Integer.valueOf(levelString));
					if(c.save(customer))
					{
						retj40.put("IsOk", true);
						retj40.put("msg", "设置成功");
					}
					else {
						retj40.put("IsOk", false);
						retj40.put("msg", "设置失败！");
					}
				}
				else {
					retj40.put("IsOk", false);
					retj40.put("msg", "设置失败！");
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in StatisticServlet-->case 40", e);
				}else{
					log.error("error in StatisticServlet-->case 40", e);
				}
			}finally
			{
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write(retj40.toString());
			}break;
		case 41://升级潜在客户
			JSONObject retObj41=new JSONObject();
			try 
			{
				String Del_id=request.getParameter("Del_id");
				String Name = request.getParameter("Name");//@
				String NameEn = request.getParameter("NameEn");
				String Brief = request.getParameter("Brief");
				String Type = request.getParameter("CustomerType");//@
				String Code = request.getParameter("Code");
				String RegionId = request.getParameter("RegionId");//@
				String ZipCode = request.getParameter("ZipCode");//@
				
				String Address = request.getParameter("Address");//@
				String AddressEn = request.getParameter("AddressEn");
				String Tel = request.getParameter("Tel");
				String Fax = request.getParameter("Fax");
				String[] ClassificationStr = request.getParameterValues("Classification");
				String Classification = "";
				if(ClassificationStr!=null)
				for(int i = 0;i< ClassificationStr.length; i++)
					Classification = Classification + (Classification.equals("")?"":",") + ClassificationStr[i];
				//System.out.println(Classification);
				String Status = request.getParameter("Status");
				String AccountBank = request.getParameter("AccountBank");
				String Account = request.getParameter("Account");
				String CreditAmount = request.getParameter("CreditAmount");
				String Remark = request.getParameter("Remark");
				String FieldDemands = request.getParameter("FieldDemands");
				String CertificateDemands = request.getParameter("CertificateDemands");
				String SpecialDemands = request.getParameter("SpecialDemands");
				SysUser user = (SysUser)request.getSession().getAttribute("LOGIN_USER");
				//String InsideContactor = request.getParameter("InsideContactor");
				
				String PaiVia=request.getParameter("PayVia");//@
				String PaiType=request.getParameter("PayType");//@
				String AccountCycle=request.getParameter("AccountCycle");//@
				String CustomerValueLevel=request.getParameter("CustomerValueLevel");
				String CustomerLevel=request.getParameter("CustomerLevel");
				String Trendency=request.getParameter("Trendency");
				String OutputExpectation=request.getParameter("OutputExpectation");
				String ServiceFeeLimitation=request.getParameter("ServiceFeeLimitation");
				String Industry=request.getParameter("IndustryId");
				String Loyalty=request.getParameter("Loyalty");
				String Satisfaction=request.getParameter("Satisfaction");
				
				Customer customer;
				customer = new Customer();
				
				customer.setName(Name);
				customer.setNameEn(NameEn);
				customer.setBrief(Brief);
				//if(Type!=null&&!Type.equals(""))
				customer.setCustomerType(Integer.parseInt(Type));
				
				customer.setCode(Code);
				customer.setRegion((new RegionManager()).findById(Integer.parseInt(RegionId)));
				customer.setZipCode(ZipCode);
				customer.setAddress(Address);
				customer.setAddressEn(AddressEn);
				customer.setTel(Tel);
				customer.setFax(Fax);
				customer.setBalance(0.0);
				customer.setClassification(Classification);
				if(Status!=null&&!Status.equals(""))
					customer.setStatus(Integer.parseInt(Status));
				
				customer.setAccountBank(AccountBank);
				customer.setAccount(Account);
				if(CreditAmount!=null&&!CreditAmount.equals(""))
				customer.setCreditAmount(Double.parseDouble(CreditAmount));
				
				customer.setRemark(Remark);
				customer.setFieldDemands(FieldDemands);
				customer.setCertificateDemands(CertificateDemands);
				customer.setSpecialDemands(SpecialDemands);
				customer.setSysUserByModificatorId(user);
				customer.setModifyDate(new Timestamp(System.currentTimeMillis()));
				//////////////////////////////////////////////
				if(PaiVia!=null&&!PaiVia.equals(""))customer.setPayVia(Integer.parseInt(PaiVia));
				if(PaiType!=null&&!PaiType.equals(""))
				{
					customer.setPayType(Integer.parseInt(PaiType));
					if(PaiType.equals("2"))//2表示周期结账
						customer.setAccountCycle(Integer.parseInt(AccountCycle));
				}else {
					customer.setPayType(4);//4表示其它类型,也是因为不能为空
					customer.setAccountCycle(13);//13表示不是同期结账，本来应为空的，但由于数据库设计该字段不能为空
				}
				
				if(CustomerValueLevel!=null&&!CustomerValueLevel.equals(""))customer.setCustomerValueLevel(Integer.parseInt(CustomerValueLevel));
				if(CustomerLevel!=null&&!CustomerLevel.equals(""))customer.setCustomerLevel(Integer.parseInt(CustomerLevel));
				if(Trendency!=null&&!Trendency.equals(""))customer.setTrendency(Integer.parseInt(Trendency));
				if(OutputExpectation!=null&&!OutputExpectation.equals(""))customer.setOutputExpectation(Double.parseDouble(OutputExpectation));
				if(ServiceFeeLimitation!=null&&!ServiceFeeLimitation.equals(""))customer.setServiceFeeLimitation(Double.parseDouble(ServiceFeeLimitation));
				if(Industry!=null&&!Industry.equals(""))customer.setIndustry(Integer.parseInt(Industry));
				
				if(Loyalty!=null&&!Loyalty.equals(""))customer.setLoyalty(Integer.parseInt(Loyalty));
				if(Satisfaction!=null&&!Satisfaction.equals(""))customer.setSatisfaction(Integer.parseInt(Satisfaction));
				//////////////////////////////////////////////
				String Contactor = request.getParameter("Contactor");//@
				String ContactorTel1 = request.getParameter("ContactorTel1");
				String ContactorTel2 = request.getParameter("ContactorTel2");
				CustomerContactor customercontactor = new CustomerContactor();
				customercontactor.setName(Contactor);
				if (ContactorTel1 != null && !ContactorTel1.equals(""))
					customercontactor.setCellphone1(ContactorTel1);
				else customercontactor.setCellphone1("");
				if (ContactorTel2 != null && !ContactorTel2.equals(""))
					customercontactor.setCellphone2(ContactorTel2);
				customercontactor.setLastUse(customer.getModifyDate());
				customercontactor.setCount(1);//1？
				
				PotentialCustomerManager pcm=new PotentialCustomerManager();
				CustomerManager cusmag = new CustomerManager();
				
				String InsideContactorId=request.getParameter("InsideContactorId");
				String Role=request.getParameter("Role");
			
				InsideContactor ic=new InsideContactor();
				SysUser s=new SysUser();
				s.setId(Integer.parseInt(InsideContactorId));
				
				ic.setSysUser(s);
				ic.setCustomer(customer);
				ic.setRole(Integer.parseInt(Role));
				boolean resu = cusmag.save(customer, customercontactor,ic);
				boolean resu2=  pcm.deleteById(Integer.parseInt(Del_id));
				
				retObj41.put("IsOK", resu&&resu2);
				retObj41.put("msg", resu?"升级成功！":"升级失败，请重试！");
			
				
			} catch (Exception e) {
				// TODO: handle exception
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in StatisticServlet-->case 41", e);
				}else{
					log.error("error in StatisticServlet-->case 41", e);
				}
			}finally
			{
				response.setContentType("text/html;charset=gbk");
				response.getWriter().write(retObj41.toString());
			}break;
		case 42://不予处理
			JSONObject retObj42=new JSONObject();
			try {
				String IgnoreId=request.getParameter("IgnoreId");
				FeedbackManager mf=new FeedbackManager();
				CustomerFeedback c=mf.FindById(Integer.parseInt(IgnoreId));
				if(c!=null)
				{
					c.setStatus(4);//不予处理，已结束
					if(mf.save(c))
					{
						retObj42.put("IsOk", true);
						retObj42.put("msg", "已忽略！");
						
					}
					else {
						retObj42.put("IsOk", false);
						retObj42.put("msg", "忽略失败，请重试！");
					}
				}
				else {
					retObj42.put("IsOk", false);
					retObj42.put("msg", "错误，请重试！");
				}
				
				
			} catch (Exception e) {
				// TODO: handle exception
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in StatisticServlet-->case 42", e);
				}else{
					log.error("error in StatisticServlet-->case 42", e);
				}
			}
			finally
			{
				response.setContentType("text/html;charset=gbk");
				response.getWriter().write(retObj42.toString());
			}break;
		case 43://输入领导意见
			JSONObject retObj43=new JSONObject();
			try 
			{
				//String returnInfo=request.getParameter("ReturnVisitInfo");
				String Analysis=request.getParameter("Analysis");
				String feedbackId=request.getParameter("FeedbackId");
				//FeedbackManager fbm=new FeedbackManager();
				CustomerFeedback cus=fbm.FindById(Integer.parseInt(feedbackId));
				if(cus!=null)
				{
					cus.setAnalysis(Analysis);
					cus.setStatus(4);//已结束
					if(fbm.update(cus))
					{
						retObj43.put("IsOk", true);
						retObj43.put("msg", "提交成功！");
						
					}
					else {
						retObj43.put("IsOk", false);
						retObj43.put("msg", "提交失败！");
					}
				}
			} catch (Exception e) 
			{
				// TODO: handle exception
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CrmServlet-->case 3", e);
				}else{
					log.error("error in CrmServlet-->case 3", e);
				}
			}
			finally
			{
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write(retObj43.toString());
			}
			break;
		case 44://回访信息
			JSONObject retObj44=new JSONObject();
			try 
			{
				String returnInfo=request.getParameter("ReturnVisitInfo");
				//String Analysis=request.getParameter("Analysis");
				String feedbackId=request.getParameter("FeedbackId");
				CustomerFeedback cus=fbm.FindById(Integer.parseInt(feedbackId));
				if(cus!=null)
				{
					cus.setReturnVisitInfo(returnInfo);
					cus.setStatus(5);//已回访
					if(fbm.update(cus))
					{
						retObj44.put("IsOk", true);
						retObj44.put("msg", "提交成功！");
						
					}
					else {
						retObj44.put("IsOk", false);
						retObj44.put("msg", "提交失败！");
					}
				}
				else {
					retObj44.put("IsOk", false);
					retObj44.put("msg", "错误,提交失败！");
				}
			} catch (Exception e) 
			{
				// TODO: handle exception
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CrmServlet-->case 3", e);
				}else{
					log.error("error in CrmServlet-->case 3", e);
				}
			}
			finally
			{
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write(retObj44.toString());
			}
			break;
		case 45:
			JSONObject retObj45=new JSONObject();
			try {
				
				MyParameters mpMyParameters=new MyParameters();
				mpMyParameters.init();
				
				Double theta1=mpMyParameters.getTheta1();
				Double theta2=mpMyParameters.getTheta2();
				Double theta3=mpMyParameters.getTheta3();
				//Double w[];
				Double total1=0.0;
				//Double total2=0.0;
				Double avg1=0.0;
				Double avg2=0.0;
				
				double qj[]={0.05,0.05,0.05,0.05,0.05,0.05,0.7};//默认值
				qj[0]=mpMyParameters.getLevelPercentage1();
				qj[1]=qj[0]+mpMyParameters.getLevelPercentage2();
				qj[2]=qj[1]+mpMyParameters.getLevelPercentage3();
				qj[3]=qj[2]+mpMyParameters.getLevelPercentage4();
				qj[4]=qj[3]+mpMyParameters.getLevelPercentage5();
				qj[5]=qj[4]+mpMyParameters.getLevelPercentage6();
				qj[6]=qj[5]+mpMyParameters.getLevelPercentage7();
				
				
				
				Integer TotalCustomer=0;
				
				String str0="use czjl_new select Id from Customer";
				String str1="select COUNT(a.Id) as Cishu,sum(a.TotalFee) as TOTAL,AVG(a.TotalFee) as AVERAGE " +
						"from Customer as c left outer join CommissionSheet as a on c.Id=a.CustomerId " +
						"where a.Status=4";//查询总产值和全所平均每次产值
				String str2="select sum(b.TotalFee) as Total,AVG(b.TotalFee) as Average,COUNT(b.Id) as JianShu " +
						"from CommissionSheet as a left outer join OriginalRecord as b on a.Id=b.CommissionSheetId " +
						"where a.Status=4 and b.Status=0";//查询总产值和全所平均每件产值
				String str3="select c.Id,c.Name,sum(a.TotalFee) as TOTAL,AVG(a.TotalFee) as AVERAGE " +
						"from Customer as c left outer join CommissionSheet as a on c.Id=a.CustomerId " +
						"group by c.Id,c.Name";//客户总产值和平均每次产值
				String str4="select c.Id,Total,Average from customer as c left outer join " +
						"(select a.CustomerId,sum(b.TotalFee) as Total,AVG(b.TotalFee) as Average,COUNT(b.Id) as JianShu " +
						"from CommissionSheet as a left outer join OriginalRecord as b on a.Id=b.CommissionSheetId " +
						"where a.Status=4 and b.Status=0 " +
						"group by a.CustomerId)as d on c.Id=d.CustomerId";//客户总产值和平均每件产值
				BaseHibernateDAO d=new BaseHibernateDAO();
				List<Object> keys=new ArrayList<Object>();
				
				List<Object[]> lObjects=d.findBySQL(str0, keys);
				
				TotalCustomer=lObjects.size();//这里用select count(*) from Customer,用a[0]取值会有异常
				
				int c[]={0,0,0,0,0,0,0};
				for(int i=0;i<7;++i)
				{
					c[i]=(int) (TotalCustomer*qj[i]);
				}
					
				lObjects=d.findBySQL(str1, keys);
				for(Object[] a:lObjects)//查询总产值和全所平均每次产值
				{
					total1=(Double)a[1];
					avg1=(Double)a[2];
				}
				lObjects=d.findBySQL(str2, keys);
				for(Object[] a:lObjects)//查询总产值和全所平均每件产值
				{
					//total2=(Double)a[0];
					avg2=(Double)a[1];
				}
				
				List<MyClass> lm=new ArrayList<MyClass>();
				lObjects=d.findBySQL(str3, keys);
				for(Object[] a:lObjects)//客户总产值和客户平均每次产值
				{
					MyClass tmp=new MyClass();
					tmp.setID((Integer)a[0]);
					tmp.setAvg((Double)a[3]==null?0.0:(Double)a[3]);
					tmp.setTotal((Double)a[2]==null?0.0:(Double)a[2]);
					lm.add(tmp);
				}
				
				List<MyClass> lm2=new ArrayList<MyClass>();
				lObjects=d.findBySQL(str4, keys);
				for(Object[] a:lObjects)////客户总产值和客户平均每件产值
				{
					MyClass tmp=new MyClass();
					tmp.setID((Integer)a[0]);
					//tmp.setTotal((Double)a[1]==null?0.0:(Double)a[1]);
					tmp.setAvg((Double)a[2]==null?0.0:(Double)a[2]);
					lm2.add(tmp);
				}
				ArrayList<MyClass> w=new ArrayList<MyClass>();
				for(int i=0;i<TotalCustomer;++i)
				{
					MyClass e=new MyClass();
					Double tmp=theta1*lm.get(i).getTotal()/total1+theta2*lm2.get(i).getAvg()/avg2+theta3*lm.get(i).getAvg()/avg1;
					e.setID(lm.get(i).getID());
					e.setTotal(tmp);
					w.add(e);
				}
	
				Collections.sort(w);
				//for(int i=0;i<7;++i)
				CustomerDAO m_dao = new CustomerDAO();
				Transaction tran = m_dao.getSession().beginTransaction();
				try 
				{
					int i=0;
					for(int jj=0;jj<TotalCustomer;++jj)
					{
						CustomerManager cManager=new CustomerManager();
						Customer customer=new Customer();
						customer=cManager.findById(w.get(jj).getID());
							
						customer.setCustomerValueLevel(1+i);
						if(i==0||i==1)//VIP客户
						{
							customer.setCustomerLevel(1);//VIP客户
						}
						if(i==2||i==3)//重点客户
						{
							customer.setCustomerLevel(2);//重点客户
						}
						if(i==4||i==5)//重要客户
						{
							customer.setCustomerLevel(3);//重要客户
						}
						if(i==6)//一般客户
						{
							customer.setCustomerLevel(4);//一般客户
						}
						m_dao.save(customer);
						if(i<7&&jj+1>c[i])++i;
					}
					tran.commit();
					retObj45.put("IsOk", true);
					retObj45.put("msg"," 更新成功！");
				
				} catch (Exception e) {
					e.printStackTrace();
					tran.rollback();
					retObj45.put("IsOk", false);
					retObj45.put("msg"," 更新失败！");
				} finally {
					m_dao.closeSession();
				}//w[i]=theta1*lm[i].Total/tatal1+theta2*lm2[i].Avg/avg2+theta3*lm[i].Avg/avg1
				
			} catch (Exception e) {
				// TODO: handle exception
				try {
					retObj45.put("IsOk", false);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					retObj45.put("msg"," 更新失败！");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			finally
			{
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write(retObj45.toString());
			}
			break;
		case 46://自动加载参数到页面
			JSONObject retObj46=new JSONObject();
			try {
				MyParameters mParameters=new MyParameters();
				mParameters.init();
				String theta1=mParameters.getTheta1().toString();
				String theta2=mParameters.getTheta2().toString();
				String theta3=mParameters.getTheta3().toString();
				
				String level1=mParameters.getLevelPercentage1().toString();
				String level2=mParameters.getLevelPercentage2().toString();
				String level3=mParameters.getLevelPercentage3().toString();
				String level4=mParameters.getLevelPercentage4().toString();
				String level5=mParameters.getLevelPercentage5().toString();
				String level6=mParameters.getLevelPercentage6().toString();
				String level7=mParameters.getLevelPercentage7().toString();
				
				retObj46.put("Theta1", theta1);
				retObj46.put("Theta2", theta2);
				retObj46.put("Theta3", theta3);
				retObj46.put("Level1",level1 );
				retObj46.put("Level2",level2 );
				retObj46.put("Level3",level3 );
				retObj46.put("Level4",level4 );
				retObj46.put("Level5",level5 );
				retObj46.put("Level6",level6 );
				retObj46.put("Level7",level7 );
				
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			finally
			{
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write(retObj46.toString());
			}
			break;
			
		case 47://修改参数//这里需要校验level1+level2+...+level7等于1等条件
			JSONObject retObj47=new JSONObject();
			try {
				MyParameters mParameters=new MyParameters();
				mParameters.init();
				String theta1=request.getParameter("Theta1");
				String theta2=request.getParameter("Theta2");
				String theta3=request.getParameter("Theta3");
				
				String level1=request.getParameter("Level1");
				String level2=request.getParameter("Level2");
				String level3=request.getParameter("Level3");
				String level4=request.getParameter("Level4");
				String level5=request.getParameter("Level5");
				String level6=request.getParameter("Level6");
				String level7=request.getParameter("Level7");
				mParameters.setLevelPercentage1(Double.parseDouble(level1));
				mParameters.setLevelPercentage2(Double.parseDouble(level2));
				mParameters.setLevelPercentage3(Double.parseDouble(level3));
				mParameters.setLevelPercentage4(Double.parseDouble(level4));
				mParameters.setLevelPercentage5(Double.parseDouble(level5));
				mParameters.setLevelPercentage6(Double.parseDouble(level6));
				mParameters.setLevelPercentage7(Double.parseDouble(level7));
				
				mParameters.setTheta1(Double.parseDouble(theta1));
				mParameters.setTheta2(Double.parseDouble(theta2));
				mParameters.setTheta3(Double.parseDouble(theta3));
				if(mParameters.save())
				{
					retObj47.put("IsOk", true);
					retObj47.put("msg", "修改成功！");
				}
				else
				{
					retObj47.put("IsOk", false);
					retObj47.put("msg", "修改失败！");
				}
				
				
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			finally
			{
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write(retObj47.toString());
			}
			break;
		case 48://查询预警信息
			JSONObject retObj48=new JSONObject();
			try {
				int page=1;
				if((request.getParameter("page"))!=null)
					page=Integer.parseInt(request.getParameter("page").toString());
				int rows = 10;
				if (request.getParameter("rows") != null)
					rows = Integer.parseInt(request.getParameter("rows").toString());
				SysUser sysUser = (SysUser)(request.getSession().getAttribute("LOGIN_USER"));
				Calendar calendar = Calendar.getInstance();
				int day = calendar.get(Calendar.DAY_OF_YEAR);
				calendar.set(Calendar.DAY_OF_YEAR, day + 10);
				java.util.Date endDate = calendar.getTime();
				calendar.set(Calendar.DAY_OF_YEAR, day - 10);
				java.util.Date beginDate = calendar.getTime();
				calendar.set(Calendar.DAY_OF_YEAR, day - 30);
				java.util.Date monthAgo = calendar.getTime();
				String queryStr="select out from InsideContactor as inside ,CustomerContactor as out where inside.customer = out.customer and out.status = 0 and  inside.sysUser = ? and out.birthday > ? and out.birthday < ? and out.customer not in (select cc.customer from CustomerCareness as cc where cc.time > ?)";
				CustomerCarenessManager customerCarenessManager = new CustomerCarenessManager();
				List<CustomerContactor> customerContactors = customerCarenessManager.findPageAllByHQL(queryStr, page, rows, sysUser,beginDate,endDate,monthAgo);
				List<Warning> warnings = new ArrayList<Warning>();
				for (CustomerContactor customerContactor : customerContactors) {
					Warning warning = new Warning();
					warning.setCustomerContactor(customerContactor);
					warning.setType(1);
					if(customerContactor.getCustomer().getCustomerLevel() == 1)
						warning.setPriority(1);
					else if(customerContactor.getCustomer().getCustomerLevel() == 2 || customerContactor.getCustomer().getCustomerLevel() == 3)
						warning.setPriority(2);
					else 
						warning.setPriority(3);
					warnings.add(warning);
				}
				JSONArray options = new JSONArray();
				for (Warning warning : warnings) {
					JSONObject object = new JSONObject();
					object.put("name", warning.getCustomerContactor().getName());
					object.put("birthday", warning.getCustomerContactor().getBirthday()==null?"":DateFormat.getDateInstance().format(warning.getCustomerContactor().getBirthday()));
					object.put("type",warning.getType());
					object.put("customer",warning.getCustomerContactor().getCustomer().getName());
					object.put("priority",warning.getPriority());
					options.put(object);
				}
				retObj48.put("rows", options);
				retObj48.put("total", options.length());
			} catch (Exception e) {
				// TODO: handle exception
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CrmServlet-->case 48", e);
				}else{
					log.error("error in CrmServlet-->case 48", e);
				}
			}
			finally
			{
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write(retObj48.toString());
			}
			break;
		case 49://查询预警信息
			JSONObject retObj49=new JSONObject();
			try {
				SysUser sysUser = (SysUser)(request.getSession().getAttribute("LOGIN_USER"));
				Calendar calendar = Calendar.getInstance();
				int day = calendar.get(Calendar.DAY_OF_YEAR);
				calendar.set(Calendar.DAY_OF_YEAR, day + 10);
				java.util.Date endDate = calendar.getTime();
				calendar.set(Calendar.DAY_OF_YEAR, day - 10);
				java.util.Date beginDate = calendar.getTime();
				calendar.set(Calendar.DAY_OF_YEAR, day - 30);
				java.util.Date monthAgo = calendar.getTime();
				String queryStr="select count(*) from InsideContactor as inside ,CustomerContactor as out where inside.customer = out.customer and out.status = 0 and  inside.sysUser = ? and out.birthday > ? and out.birthday < ? and out.customer not in (select cc.customer from CustomerCareness as cc where cc.time > ?)";
				CustomerCarenessManager customerCarenessManager = new CustomerCarenessManager();
//				List<CustomerContactor> customerContactors = customerCarenessManager.findPageAllByHQL(queryStr, page, rows, sysUser,beginDate,endDate,monthAgo);
				int total = customerCarenessManager.getTotalCountByHQL(queryStr, sysUser,beginDate,endDate,monthAgo);
				retObj49.put("total", total);
				retObj49.put("IsOk", true);
			} catch (Exception e) {
				// TODO: handle exception
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CrmServlet-->case 48", e);
				}else{
					log.error("error in CrmServlet-->case 48", e);
				}
			}
			finally
			{
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write(retObj49.toString());
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
	

}