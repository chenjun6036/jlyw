package com.jlyw.servlet.crm;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.mapping.Array;
import org.json.me.JSONArray;
import org.json.me.JSONObject;

import sun.tools.jar.resources.jar;

import com.jlyw.hibernate.ApplianceSpecies;
import com.jlyw.hibernate.ApplianceStandardName;
import com.jlyw.hibernate.BaseHibernateDAO;
import com.jlyw.hibernate.CommissionSheet;
import com.jlyw.hibernate.Customer;
import com.jlyw.hibernate.SubContract;
import com.jlyw.hibernate.crm.InsideContactor;
import com.jlyw.hibernate.crm.PotentialCustomer;
import com.jlyw.manager.ApplianceSpeciesManager;
import com.jlyw.manager.ApplianceStandardNameManager;
import com.jlyw.manager.BaseTypeManager;
import com.jlyw.manager.CertificateFeeAssignManager;
import com.jlyw.manager.CommissionSheetManager;
import com.jlyw.manager.CustomerManager;
import com.jlyw.manager.SubContractManager;
import com.jlyw.manager.crm.CustomerServiceFeeManager;
import com.jlyw.manager.crm.ExportWithSQL;
import com.jlyw.manager.crm.FeedbackManager;
import com.jlyw.manager.crm.InsideContactorManager;
import com.jlyw.manager.crm.PotentialCustomerManager;
import com.jlyw.manager.crm.Sql;
import com.jlyw.manager.crm.StatisticManager;
import com.jlyw.util.DateTimeFormatUtil;
import com.jlyw.util.ExportUtil;
import com.jlyw.util.KeyValueWithOperator;

public class CrmExportServlet extends HttpServlet {

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
		out.println(", using the GET method");
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
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String method=request.getParameter("method");
		if(method!=null&&!method.equals(""))
		{
			int m=Integer.parseInt(method);
			switch (m) {
			
			case 1://导出等级/产值比例信息
				String p1=request.getParameter("Par");
				JSONObject ret=new JSONObject();
				try {
					String cusid="";
					JSONObject pars1=new JSONObject(p1);
					if(pars1.length()!=0)
					{
						cusid=pars1.getString("Id");
						
					}
					
					String qString="select a.Id,a.Name,SUM(b.TotalFee) as TOTAL,AVG(b.TotalFee) as AVGS,COUNT(b.TotalFee) as cc" +
							" ,SUM(b.TotalFee)/? as percentage,a.CustomerValueLevel " +
							"from Customer as a left outer join CommissionSheet as b on a.Id=b.CustomerId " ;
					String qString2="group by a.Id,a.Name,a.CustomerValueLevel order by percentage desc, TOTAL desc, cc desc";
					String qString1="select sum(a.TotalFee) from CommissionSheet as a";
					Sql sql=new Sql();
					Connection conn=sql.getMyConn();
					double Tot=1.0;
					PreparedStatement ps1=conn.prepareStatement(qString1);
					ResultSet rs1=ps1.executeQuery();
					if(rs1.next())
					{
						Tot=rs1.getDouble(1);
						
					}
					
					List<Object> keys=new ArrayList<Object>();
					keys.add(Double.valueOf(Tot));
					if(cusid!=null&&!cusid.equals("")){
						qString+=" where a.Id= ? ";
						keys.add(Integer.valueOf(cusid));
					}
					
					String filePath=ExportWithSQL.ExportToExcel(qString+qString2, keys,"formatTitleOfCustomerLevel","formatExcelOfCustomerLevel",StatisticManager.class);
					ret.put("IsOk", filePath.equals("")?false:true);
					ret.put("Path", filePath);
					conn.close();
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				finally
				{
					response.setContentType("text/html;charset=utf-8");
					response.getWriter().write(ret.toString());
				}
				break;
			case 2://导出内部联系人信息
				JSONObject ret2=new JSONObject();
				try {
					String p=request.getParameter("Par");
					Sql sql=new Sql();
					Connection conn=sql.getMyConn();

		
					String queryString="select a.Name,a.Address,c.Name,c.JobNum,c.Cellphone1,c.Cellphone2 " +
								"from Customer as a,InsideContactor as b,SysUser as c " +
								"where a.Id=b.CustomerId and b.InsideContactorId=c.Id ";
					List<String> title=new ArrayList<String>();
					title.add("单位名称");//编号
					title.add("地址");
					title.add("联系人姓名");
					title.add("联系人工号");
					title.add("联系电话");
					title.add("电话2");
					
					PreparedStatement ps1=conn.prepareStatement(queryString);
					if(p!=null&&!p.equals(""))
					{
						/*JSONObject pars02=new JSONObject(p);
						String str=pars02.getString("customerId1");*/
						queryString+="and a.Id= ? ";
						ps1.setInt(1, Integer.valueOf(p));
					}
					ResultSet rs1=ps1.executeQuery();
					List<JSONObject>  lj=new ArrayList<JSONObject>();
					while(rs1.next())
					{
						JSONObject tmp=new JSONObject();
						tmp.put("customerName",rs1.getObject(1));
						tmp.put("address",rs1.getObject(2));
						tmp.put("SysName",rs1.getObject(3));
						tmp.put("jobNum",rs1.getObject(4));
						tmp.put("cellphone1",rs1.getObject(5));
						tmp.put("cellphone2",rs1.getObject(6));
						lj.add(tmp);	
					}
					conn.close();
					String filePath=ExportUtil.ExportToExcelByResultSet(lj,null,"formatExcel","formatTitle",InsideContactorManager.class);
					ret2.put("IsOk", filePath.equals("")?false:true);
					ret2.put("Path", filePath);
				} catch (Exception e) {
					// TODO: handle exception
				}finally
				{
					response.setContentType("text/html;charset=utf-8");
					response.getWriter().write(ret2.toString());
				}
				break;
			case 3://导出潜在客户信息
				JSONObject ret3=new JSONObject();
				try {
					String CustomerName=request.getParameter("Par");
					//if(CustomerName!=null&&!CustomerName.equals(""))
					{
						List<Object> keys=new ArrayList<Object>();
						String queryString=" from PotentialCustomer as a where a.name like ?";
						keys.add("%"+URLDecoder.decode(CustomerName,"UTF-8")+"%");
					
					String filePath=ExportUtil.ExportToExcel(queryString,keys,null,"formatExcel","formatTitle",PotentialCustomerManager.class);
					ret3.put("IsOK", filePath.equals("")?false:true);
					ret3.put("Path", filePath);
					
					
					}
					
				} catch (Exception e) {
					// TODO: handle exception
				}finally
				{
					response.setContentType("text/html;charset=utf-8");
					response.getWriter().write(ret3.toString());
				}
				break;
			case 4://导出反馈信息
				JSONObject ret4=new JSONObject();
				String par=request.getParameter("Par");
				try {
					JSONObject pars=new JSONObject(par);
					String cusName="";
					String startDate="";
					String endDate="";
					String status="";
					if(pars.length()!=0)
					{
						 cusName=pars.getString("CustomerName");
						 startDate=pars.getString("StartDate");
						 endDate=pars.getString("EndDate");
						 status=pars.getString("Status");
						 if(status.equals("4"))//
								 status="";
					}
					
					List<Object> keys=new ArrayList<Object>();
					String queryString=" from CustomerFeedback as a where a.customer.name like ?";
					keys.add("%"+URLDecoder.decode(cusName,"UTF-8")+"%");
					if(startDate!=null&&!startDate.equals(""))
					{
						String str=URLDecoder.decode(startDate,"UTF-8");
						queryString+=" and (convert(varchar(10),a.createTime,120)>= ?)";//model.createTime
						keys.add(str);
					}
					
					
					if(endDate!=null&&!endDate.equals(""))
					{
						String str=URLDecoder.decode(endDate,"UTF-8");
						queryString+=" and (convert(varchar(10),a.createTime,120)<= ?)";//
						keys.add(str);
					}
					
					if(status!=null&&!status.equals(""))
					{
						String str=URLDecoder.decode(status,"UTF-8");
						queryString+=" and (a.status = ?)";//
						keys.add(Integer.valueOf(str));
					}
					
						
					String filePath=ExportUtil.ExportToExcel(queryString,keys,null,"formatExcel","formatTitle",FeedbackManager.class);
					ret4.put("IsOK", filePath.equals("")?false:true);
					ret4.put("Path", filePath);
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				finally
				{
					response.setContentType("text/html;charset=utf-8");
					response.getWriter().write(ret4.toString());
				}
				break;
			case 5://导出费用信息
				JSONObject ret5=new JSONObject();
				String par2=request.getParameter("Par");
				try 
				{
					JSONObject pars2=new JSONObject(par2);
					String cusId="";
					String sta="";
					if(pars2.length()!=0)
					{
						cusId=pars2.getString("CustomerId");
						sta=pars2.getString("Status");
					}
					String queryString="from CustomerServiceFee as a where 1=1";
					List<Object> keys=new ArrayList<Object>();
					if(cusId!=null&&!cusId.equals(""))
					{
						keys.add(Integer.valueOf(cusId));
						queryString+=" and a.customer.id= ? ";
					}
					if(sta!=null&&!sta.equals("")&&!sta.equals("4"))
					{
						keys.add(Integer.valueOf(sta));
						queryString+=" and a.status= ? ";
					}
					
					String filePath=ExportUtil.ExportToExcel(queryString,keys,null,"formatExcel","formatTitle",CustomerServiceFeeManager.class);
					ret5.put("IsOK", filePath.equals("")?false:true);
					ret5.put("Path", filePath);
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				finally
				{
					response.setContentType("text/html;charset=utf-8");
					response.getWriter().write(ret5.toString());
				}break;
			case 6://导出全年产值明细
				JSONObject ret6=new JSONObject();
				String par3=request.getParameter("Par");
				try 
				{
					JSONObject tmpJsonObject=new JSONObject(par3);
					String CustomerId = null;
					String RegionId = null;
					String Classi = null;
					String CheckOutDateFrom = null;
					String CheckOutDateEnd = null;
					String CustomerLevel=null;
					if(tmpJsonObject.length()!=0)
					{
					 CustomerId = tmpJsonObject.getString("CustomerId");
					 RegionId = tmpJsonObject.getString("RegionId");
					 Classi = tmpJsonObject.getString("Classfication");
					 CheckOutDateFrom = tmpJsonObject.getString("CheckOutDateFrom");
					 CheckOutDateEnd = tmpJsonObject.getString("CheckOutDateEnd");
					 CustomerLevel=tmpJsonObject.getString("CustomerLevel");
					}
					
					
					String Status = "4";
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
					
					CommissionSheetManager cSheetMgr = new CommissionSheetManager();
					CertificateFeeAssignManager feeMgr=new CertificateFeeAssignManager();
					int total = 0;
					JSONArray options = new JSONArray();
					JSONArray foot = new JSONArray();
					BaseHibernateDAO dao=new BaseHibernateDAO();
					List<CommissionSheet> result = dao.findByHQL("select model " + queryStr + " order by model.commissionDate desc, model.id desc", keys);
					total = cSheetMgr.getTotalCountByHQL("select count(model) " + queryStr, keys);
					
					List<JSONObject> lj=new ArrayList<JSONObject>();

					if(result!=null&&result.size()>0){
						for(CommissionSheet cSheet : result){
							JSONObject option = new JSONObject();
							option.put("Code", cSheet.getCode());
							option.put("Pwd", cSheet.getPwd());
							option.put("CustomerName", cSheet.getCustomerName());
							//option.put("CommissionDate", cSheet.getCommissionDate());
							//option.put("ApplianceName", cSheet.getApplianceName());
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
													
							//options.put(option);
							lj.add(option);
						}
					}				    
	
					String filePath=ExportUtil.ExportToExcelByResultSet(lj,null,"formateExcelOfFullYear","formateTitleOfFullYear",StatisticManager.class);
					ret6.put("IsOK", filePath.equals("")?false:true);
					ret6.put("Path", filePath);
				} catch (Exception e) {
					// TODO: handle exception
				}
				finally
				{
					response.setContentType("text/html;charset=utf-8");
					response.getWriter().write(ret6.toString());
				}break;
			case 7://导出按下限查询结果
				JSONObject ret7=new JSONObject();
				String par4=request.getParameter("Par");
				try 
				{
					String LeastOutput="0";
					JSONObject pars4=new JSONObject(par4);
					if(pars4.length()!=0)
					{
						LeastOutput=pars4.getString("LeastOutput");
					}
					String CheckOutDateFrom = request.getParameter("CheckOutDateFrom");
					String CheckOutDateEnd = request.getParameter("CheckOutDateEnd");
					String Status ="4";
					
					String Type=request.getParameter("Type");
					
					String qselect="";
					String qfrom="";
					String qwhere="";
					String qgroupby="";
					String qhaving="";
					String q="";
					List<Object> keys = new ArrayList<Object>();
					
					
					CommissionSheetManager cSheetMgr = new CommissionSheetManager();
					
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
					
					
					String filePath=ExportUtil.ExportToExcel(q,keys,null,"formateExcelOfLeast","formateTitleOfLeast",StatisticManager.class);
					ret7.put("IsOK", filePath.equals("")?false:true);
					ret7.put("Path", filePath);

				} catch (Exception e) {
					// TODO: handle exception
				}finally
				{
					response.setContentType("text/html;charset=utf-8");
					response.getWriter().write(ret7.toString());
				}break;
				
			case 8://导出同比环比
				JSONObject ret8=new JSONObject();
				String par5=request.getParameter("Par");
				try 
				{
					JSONObject pars5=new JSONObject(par5);
					String date1="";
					String date2="";
					if(pars5.length()!=0)
					{
						date1=pars5.getString("StartDate");
						date2=pars5.getString("EndDate");
					}
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
					List<Object> keys=new ArrayList<Object>();
					Integer year=Integer.parseInt(date1.substring(0, 4))-1;
					
					keys.add(year.toString()+date1.substring(4,10));
					keys.add(year.toString()+date2.substring(4,10));
					keys.add(date1);
					keys.add(date2);
					
					String filePath=ExportWithSQL.ExportToExcel(query,keys,"formateTitleOfRatio","formateExcelOfRatio",StatisticManager.class);
					ret8.put("IsOK", filePath.equals("")?false:true);
					ret8.put("Path", filePath);
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				finally
				{
					response.setContentType("text/html;charset=utf-8");
					response.getWriter().write(ret8.toString());
				}break;
			case 9://导出分类到计量标准一级
				JSONObject ret9=new JSONObject();
				String par6=request.getParameter("Par");
				try 
				{
					JSONObject pars6=new JSONObject(par6);
					String cusIdString="";
					if(pars6.length()!=0)
					{
						cusIdString=pars6.getString("CustomerId");
					}
					
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
					
							List<Object> keys=new ArrayList<Object>();
							if(cusIdString!=null&&!cusIdString.equals(""))
							{
								qString+=" and a.CustomerId=? ";
								keys.add(cusIdString);
							}
							
							String filePath=ExportWithSQL.ExportToExcel(qString+qand,keys,"formateTitleOf1LevelClassify","formateExcelOf1LevelClassify",StatisticManager.class);
							ret9.put("IsOK", filePath.equals("")?false:true);
							ret9.put("Path", filePath);
				} catch (Exception e) {
					// TODO: handle exception
				}finally
				{
					response.setContentType("text/html;charset=utf-8");
					response.getWriter().write(ret9.toString());
				}break;
			case 10://器具大类统计 导出
				JSONObject ret10=new JSONObject();
				try 
				{
					ApplianceSpeciesManager asm=new ApplianceSpeciesManager();
					
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
					List<JSONObject> result=new ArrayList<JSONObject>();
					while (iter.hasNext()) 
					{
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
					result.add(ja);
					} 
					
					String filePath=ExportUtil.ExportToExcelByResultSet(result, null, "formateExcelOfTopLevel", "formateTitleOfTopLevel", StatisticManager.class);
					ret10.put("IsOK", filePath.equals("")?false:true);
					ret10.put("Path", filePath);
					
				} catch (Exception e) {
					// TODO: handle exception
				}finally
				{
					response.setContentType("text/html;charset=utf-8");
					response.getWriter().write(ret10.toString());
				}break;
			case 11://按年份汇总
				JSONObject ret11=new JSONObject();
				String par11=request.getParameter("Par");
				try 
				{
					JSONObject pars11=new JSONObject(par11);
					String cusIdString="";
					if(pars11.length()!=0)
					{
						cusIdString=pars11.getString("CustomerId");
					}
					
					String qString="select year(a.CommissionDate) as  Y,MONTH(a.CommissionDate) as M, " +
							"COUNT(a.Id) as counter,SUM(a.TotalFee ) as total ,SUM(a.TestFee) as test,SUM(a.DebugFee) as debug," +
							"SUM(a.MaterialFee) as material,SUM(a.CarFee) as car,SUM(a.RepairFee) as repair,SUM(a.OtherFee) as other,AVG(a.TotalFee) as avgs " +
							"from CommissionSheet as a where a.Status=4 " ;
					String qString1="group by year(a.CommissionDate),MONTH(a.CommissionDate) order by Y,M";
					List<Object> keys=new ArrayList<Object>();
					if(cusIdString!=null&&!cusIdString.equals(""))
						{
							qString+=" and a.CustomerId= ? ";
							keys.add(cusIdString);
						}
					String filePath=ExportWithSQL.ExportToExcel(qString+qString1,keys,"formateTitleOfYearAll","formateExcelOfYearAll",StatisticManager.class);
					ret11.put("IsOK", filePath.equals("")?false:true);
					ret11.put("Path", filePath);
					
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				finally
				{
					response.setContentType("text/html;charset=utf-8");
					response.getWriter().write(ret11.toString());
				}break;
			case 12://内部联系人产值统计---导出
				JSONObject ret12=new JSONObject();
				String par12=request.getParameter("Par");
				try 
				{
					JSONObject pars12=new JSONObject(par12);
					String insideContactorId="";
					String startDate="";
					String endDate="";
					if(pars12.length()!=0)
					{
						insideContactorId=pars12.getString("InsideContactorId");
						startDate=pars12.getString("StartDate");
						endDate=pars12.getString("EndDate");
					}
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
					
					String filePath=ExportUtil.ExportToExcel(queryString,key,null,"formatExcelOfInsideContactorFee","formatTitleOfInsideContactorFee",StatisticManager.class);
					ret12.put("IsOK", filePath.equals("")?false:true);
					ret12.put("Path", filePath);
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				finally
				{
					response.setContentType("text/html;charset=utf-8");
					response.getWriter().write(ret12.toString());
				}break;
			case 13://内部联系人产值统计---导出
				JSONObject ret13=new JSONObject();
				String par13=request.getParameter("Par");
				try 
				{
					JSONObject pars13=new JSONObject(par13);
					String customerName="";
					String customerLevel="";
					String careContactor="";
					String careDutySysUser="";
					String startTime="";
					String endTime="";
					String way="";
					if(pars13.length()!=0)
					{
						 customerName=pars13.getString("CustomerName");
						 customerLevel=pars13.getString("CustomerLevel");
						 careContactor=pars13.getString("CareContactor");
						 careDutySysUser=pars13.getString("CareDutySysUser");
						// code=pars13.getString("Code");
						 startTime=pars13.getString("StartTime");
						 endTime=pars13.getString("EndTime");
						 way=pars13.getString("Way");
					}
					
					
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
					/*if(code!=null&&!code.equals(""))
					{
						query+=" and (customer.code like ? )";
						key.add("%"+URLDecoder.decode(code,"UTF-8")+"%");
					}*/
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
					
					String filePath=ExportUtil.ExportToExcel(query,key,null,"formatExcelOfCareness","formatTitleOfCareness",StatisticManager.class);
					ret13.put("IsOK", filePath.equals("")?false:true);
					ret13.put("Path", filePath);
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				finally
				{
					response.setContentType("text/html;charset=utf-8");
					response.getWriter().write(ret13.toString());
				}break;
			}
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
