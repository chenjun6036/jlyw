package com.jlyw.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.jlyw.hibernate.Address;
import com.jlyw.hibernate.ApplianceManufacturer;
import com.jlyw.hibernate.AppliancePopularName;
import com.jlyw.hibernate.ApplianceSpecies;
import com.jlyw.hibernate.ApplianceStandardName;
import com.jlyw.hibernate.CommissionSheet;
import com.jlyw.hibernate.Customer;
import com.jlyw.hibernate.CustomerContactor;
import com.jlyw.hibernate.LocaleApplianceItem;
import com.jlyw.hibernate.Reason;
import com.jlyw.hibernate.SubContract;
import com.jlyw.hibernate.SubContractor;
import com.jlyw.hibernate.SysUser;
import com.jlyw.hibernate.view.ViewApplianceSpecialStandardNamePopularName;
import com.jlyw.manager.AddressManager;
import com.jlyw.manager.ApplianceManufacturerManager;
import com.jlyw.manager.AppliancePopularNameManager;
import com.jlyw.manager.ApplianceSpeciesManager;
import com.jlyw.manager.ApplianceStandardNameManager;
import com.jlyw.manager.CertificateFeeAssignManager;
import com.jlyw.manager.CommissionSheetManager;
import com.jlyw.manager.CustomerContactorManager;
import com.jlyw.manager.CustomerManager;
import com.jlyw.manager.OriginalRecordManager;
import com.jlyw.manager.QualificationManager;
import com.jlyw.manager.ReasonManager;
import com.jlyw.manager.SubContractManager;
import com.jlyw.manager.SubContractorManager;
import com.jlyw.manager.UserManager;
import com.jlyw.manager.quotation.QuotationManager;
import com.jlyw.manager.vehicle.LocaleApplianceItemManager;
import com.jlyw.manager.view.ViewAppSpeStandardPopularNameManager;
import com.jlyw.util.CommissionSheetFlagUtil;
import com.jlyw.util.DateTimeFormatUtil;
import com.jlyw.util.FlagUtil;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.LetterUtil;
import com.jlyw.util.SystemCfgUtil;
import com.jlyw.util.UIDUtil;
import com.jlyw.util.FlagUtil.CommissionSheetStatus;
import com.jlyw.util.SysStringUtil.ResponseContentType;


public class CommissionSheetServlet extends HttpServlet{
	private static Log log = LogFactory.getLog(CommissionSheetServlet.class);
	private static Object MutexObjectOfNewCommissionSheet = new Object();		//用于互斥访问新建委托单
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doPost(req, resp);
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
			Integer method = Integer.parseInt(req.getParameter("method"));	//判断请求的方法类型
			CommissionSheetManager cSheetMgr = new CommissionSheetManager();
			switch (method) {
			case 0: // 新增委托单
				try {
					String QuotationId = req.getParameter("QuotationId");	//报价单号
					String CommissionDate = req.getParameter("CommissionDate");		//委托日期
					String PromiseDate = req.getParameter("PromiseDate").trim();			//承诺日期
					String CommissionType = req.getParameter("CommissionType");		//委托形式
					String CustomerName  = req.getParameter("CustomerName").trim();		//委托单位
					String CustomerTel = req.getParameter("CustomerTel").trim();
					String CustomerAddress = req.getParameter("CustomerAddress").trim();
					String CustomerZipCode = req.getParameter("CustomerZipCode").trim();
					String ContactPerson = req.getParameter("ContactPerson").trim();
					String ContactorTel = req.getParameter("ContactorTel").trim();
					String SampleFrom = req.getParameter("SampleFrom");	//证书单位
					String BillingTo = req.getParameter("BillingTo");	//开票单位
					
					String CustomerHandler = req.getParameter("CustomerHandler");	//委托人
//					String ReceiverName = req.getParameter("ReceiverName").trim();	//接收人
					
					String HeadNameId = req.getParameter("HeadName").trim();	//台头名称ID
					String RecipientAddress = req.getParameter("RecipientAddress");	//送样地址
					String PickupAddress = req.getParameter("PickupAddress");		//取件地址
					
					String Appliances = req.getParameter("Appliances").trim();	//检验的器具
					JSONArray appliancesArray = new JSONArray(Appliances);	//检查的器具
					String LocaleCommissionCode = null;	//现场委托单号
					Timestamp LocaleCommissionDate = null;	//现场检测时间
					Integer LocaleStaffId = null;	//现场检测负责人ID
					
					if(QuotationId != null){
						QuotationId = QuotationId.trim();
					}else{
						QuotationId = "";
					}
					if(CommissionType.length() == 0 || CustomerName.length() == 0 || appliancesArray.length() == 0){
						throw new Exception("委托单信息或器具信息录入不完整！");
					}
					if((CommissionDate == null || CommissionDate.length() == 0) && !CommissionType.equals("2")){
						throw new Exception("委托日期为空！");
					}
					if(SampleFrom.length() == 0){
						SampleFrom = CustomerName;
					}
					if(BillingTo.length() == 0){
						BillingTo = CustomerName;
					}
					if(RecipientAddress == null || RecipientAddress.length() == 0){
						RecipientAddress = null;
					}
					if(PickupAddress == null || PickupAddress.length() == 0){
						PickupAddress = null;
					}
					
					Timestamp today = new Timestamp(System.currentTimeMillis());
					CustomerManager cusMgr = new CustomerManager();		//客户管理Mgr
					Integer CustomerId;
					List<Customer> cusList = cusMgr.findByVarProperty(new KeyValueWithOperator("name",CustomerName,"="), 
							new KeyValueWithOperator("status", 1, "<>"));	//查找委托单位的ID
					if(cusList != null && cusList.size() == 1){
						CustomerId = cusList.get(0).getId();
					}else if(cusList != null && cusList.size() > 1){
						throw new Exception("数据库中找到多个名称相同的委托单位:"+CustomerName+", 请到‘委托单位信息管理’进行修改！");
					}else{
						throw new Exception("委托单位不存在，新客户请先新建委托单位！");
					}
					
					if(QuotationId.length() > 0){
						if(new QuotationManager().findById(QuotationId) == null){
							throw new Exception(String.format("报价单号:%s 无效！", QuotationId));
						}
					}
					if(CommissionType.equals("2")){	//现场检测
						LocaleCommissionCode = req.getParameter("LocaleCommissionCode");	//现场委托单号
						String LocaleCommissionDateStr = req.getParameter("LocaleCommissionDate");	//现场检测时间
						String LocaleStaffName = req.getParameter("LocaleStaffId");	//现场负责人姓名
						if(LocaleCommissionCode == null || LocaleCommissionCode.length() == 0 ||
								LocaleCommissionDateStr == null || LocaleCommissionDateStr.length() == 0 ||
								LocaleStaffName == null || LocaleStaffName.length() == 0){
							throw new Exception("现场检测信息填写不完整！");
						}
						CommissionDate = LocaleCommissionDateStr;	//委托日期即为现场检测日期
						LocaleCommissionDate = new Timestamp(DateTimeFormatUtil.DateFormat.parse(LocaleCommissionDateStr).getTime());
						UserManager userMgr = new UserManager();
						List<SysUser> userList = userMgr.findByVarProperty(new KeyValueWithOperator("name", LocaleStaffName, "="), new KeyValueWithOperator("status", 0, "="));
						if(userList != null && userList.size() > 0){
							LocaleStaffId = userList.get(0).getId();
						}else{
							throw new Exception(String.format("现场检测负责人 '%s' 不存在，请重新选择！", LocaleStaffName));
						}
					}
										
//					Integer ReceiverId2 = null;	//接收人
//					if(ReceiverName == null || ReceiverName.trim().length() == 0){
//						ReceiverName = null;
//						ReceiverId2 = null;
//					}else{
//						UserManager userMgr = new UserManager();
//						List<SysUser> userList = userMgr.findByVarProperty(new KeyValueWithOperator("name",ReceiverName.trim(),"="));
//						if(userList != null && userList.size() > 0){
//							ReceiverName = userList.get(0).getName();
//							ReceiverId2 = userList.get(0).getId();
//						}else{
//							throw new Exception(String.format("接收人 '%s' 不存在，请重新选择！", ReceiverName));
//						}
//					}

					
					
					ApplianceSpeciesManager speciesMgr = new ApplianceSpeciesManager();	//器具分类管理Mgr
					ApplianceStandardNameManager sNameMgr = new ApplianceStandardNameManager();	//器具标准名称管理Mgr
					AppliancePopularNameManager popNameMgr = new AppliancePopularNameManager();	//器具常用名称管理Mgr
					SubContractorManager subConMgr = new SubContractorManager();	//转包方管理Mgr
					ApplianceManufacturerManager mafMgr = new ApplianceManufacturerManager();	//制造厂管理Mgr
					
					AddressManager addrMgr = new AddressManager();
					Address HeadNameAddr = new AddressManager().findById(Integer.parseInt(HeadNameId));	//台头名称的单位
					
					QualificationManager qualMgr = new QualificationManager();	//检测人员资质管理Mgr
					List<Integer> qualList = new ArrayList<Integer>();
					qualList.add(FlagUtil.QualificationType.Type_Jianding);
					qualList.add(FlagUtil.QualificationType.Type_Jianyan);
					qualList.add(FlagUtil.QualificationType.Type_Jiaozhun);
					
					List<CommissionSheet> comList = new ArrayList<CommissionSheet>();	//委托单列表
					List<Integer> idList = new ArrayList<Integer>();	//委托单列表中条目对应前台表格记录的ID
					List<SubContractor> subConList = new ArrayList<SubContractor>();	//委托单列表对应的转包方：如委托单没有转包方，则为null
					List<SysUser> alloteeList = new ArrayList<SysUser>();	//委托单列表对应的派定人：如委托单没有派定人，则为null
					Timestamp commissionDate = new Timestamp(Date.valueOf(CommissionDate).getTime());	//委托日期
					LocaleApplianceItemManager locAppItemMgr = new LocaleApplianceItemManager();	//现场检测器具条目的Mgr
					
					synchronized(MutexObjectOfNewCommissionSheet) {
						//begin-查询本委托单类型最大的委托单号
						String queryCode = String.format("%d%d", today.getYear()+1900,Integer.parseInt(CommissionType));		//查询委托单的样式：根据委托形式不同标志位不同
						String queryString = "select max(model.code) from CommissionSheet as model where model.code like ?";
						List<Object> retList = cSheetMgr.findByHQL(queryString, queryCode+"%");
						Integer codeBeginInt = Integer.parseInt("000001");	//委托单编号
						if(retList.size() > 0 && retList.get(0) != null){
							codeBeginInt = Integer.parseInt(retList.get(0).toString().substring(5)) + 1;
						}
						//end-查询本委托单类型最大的委托单号
						
						/********************   存委托单    ******************/
						for(int i = 0; i < appliancesArray.length(); i++){
							JSONObject jsonObj = appliancesArray.getJSONObject(i);
							idList.add(jsonObj.getInt("Id"));		//前端页面表格记录的ID
							CommissionSheet comSheet = new CommissionSheet();
							comSheet.setCommissionDate(commissionDate);	//委托日期
							if(PromiseDate.length() > 0){
								comSheet.setPromiseDate(Date.valueOf(PromiseDate));	//承诺日期
							}
							if(QuotationId.length() == 0){			//报价单号
								comSheet.setQuotationId(null);
							}else{
								comSheet.setQuotationId(QuotationId);
							}
							
							comSheet.setCommissionType(Integer.parseInt(CommissionType));//委托形式
							comSheet.setCustomerId(CustomerId);	//委托单位ID
							comSheet.setCustomerName(CustomerName);
							comSheet.setCustomerTel(CustomerTel);
							comSheet.setCustomerAddress(CustomerAddress);
							comSheet.setCustomerZipCode(CustomerZipCode);
							comSheet.setCustomerContactor(ContactPerson);
							comSheet.setCustomerContactorTel(ContactorTel);
							comSheet.setSampleFrom(SampleFrom);
							comSheet.setBillingTo(BillingTo);
							comSheet.setCustomerHandler(CustomerHandler);	//委托人
							comSheet.setReceiverId(((SysUser)req.getSession().getAttribute("LOGIN_USER")).getId());	//接收人ID
							comSheet.setReceiverName(((SysUser)req.getSession().getAttribute("LOGIN_USER")).getName());	//接收人姓名
							
							comSheet.setHeadNameId(HeadNameAddr.getId());	//台头名称ID
							comSheet.setHeadName(HeadNameAddr.getHeadName());	//台头名称
							comSheet.setHeadNameEn(HeadNameAddr.getHeadNameEn()==null?"":HeadNameAddr.getHeadNameEn());	//台头名称英文
							comSheet.setSampleAddress(RecipientAddress==null?null:Integer.parseInt(RecipientAddress));//样品接收地点
							comSheet.setReportAddress(PickupAddress==null?null:Integer.parseInt(PickupAddress));	//取样、取报告地点
							
							comSheet.setLocaleCommissionCode(LocaleCommissionCode);	//现场委托书号
							comSheet.setLocaleCommissionDate(LocaleCommissionDate);	//现场检测时间
							comSheet.setLocaleStaffId(LocaleStaffId);	//现场负责人ID
							
							//委托单其他信息（必填信息）
							comSheet.setCode(queryCode+String.format("%06d", codeBeginInt++));	//委托单编号
							int pwd =  (int) (Math.random()*9000+1000);	//密码：四位随机数（1000~9999）
							comSheet.setPwd(new Integer(pwd).toString());
							comSheet.setCreatorId(((SysUser)req.getSession().getAttribute("LOGIN_USER")).getId());	//委托单创建人ID
							comSheet.setCreateDate(today);	//委托单创建时间
							comSheet.setStatus(0);	//委托单状态：已收件
							comSheet.setAttachment(UIDUtil.get22BitUID());	//附件集名称
							
							/**********************   现场业务信息   ************************/
							if(CommissionType.equals("2")){
								if(!jsonObj.has("LocaleApplianceId") || jsonObj.get("LocaleApplianceId").toString().length() == 0){	//现场检测条目Id
									throw new Exception("现场检测信息不完整：现场检测条目ID为空！");
								}
								String LocaleApplianceId = jsonObj.getString("LocaleApplianceId").toString();	//现场检测器具条目的ID
								LocaleApplianceItem locAppItem = locAppItemMgr.findById(Integer.parseInt(LocaleApplianceId));
								if(locAppItem == null){
									throw new Exception("找不到现场检测条目ID为‘"+LocaleApplianceId+"’的记录！");
								}else if(!LocaleCommissionCode.equalsIgnoreCase(locAppItem.getLocaleMission().getCode())){
									throw new Exception("现场委托书号："+LocaleCommissionCode+" 下找不到检测条目ID为‘"+LocaleApplianceId+"’的记录！");								
								}else if(!CustomerId.equals(locAppItem.getLocaleMission().getCustomer().getId())){
									throw new Exception("现场委托书号："+LocaleCommissionCode+" 所对应的委托单位ID与输入的委托单位ID不一致！");
								}
								//判断委托单数据库中是否已经存在该检测条目ID对应的委托单
								if(cSheetMgr.getTotalCount(new KeyValueWithOperator("status", 10, "<>"),
										new KeyValueWithOperator("localeApplianceItemId", locAppItem.getId(), "=")) > 0){
									throw new Exception("现场检测条目ID为‘"+LocaleApplianceId+"’的记录已经生成委托单了，不能重复生成！");
								}
								comSheet.setLocaleApplianceItemId(locAppItem.getId());							
							}
							/**********************   添加器具信息    ************************/
							String SpeciesType = jsonObj.get("SpeciesType").toString();	//器具分类类型
							String ApplianceSpeciesId = jsonObj.get("ApplianceSpeciesId").toString();	//器具类别ID/标准名称ID
							String ApplianceName = jsonObj.getString("ApplianceName");	//器具名称
							String Manufacturer= jsonObj.getString("Manufacturer");	//制造厂
							
							if(Integer.parseInt(SpeciesType) == 0){	//0:标准名称；1：分类名称
								comSheet.setSpeciesType(false);	
								String stdName = sNameMgr.findById(Integer.parseInt(ApplianceSpeciesId)).getName();
								if(ApplianceName == null || ApplianceName.trim().length() == 0){
									ApplianceName = stdName;	//器具名称未填写，则默认为标准名称或分类名称
								}else{	//如果已填写，判断是否等于标准名称，如果不等于标准名称，则存入常用名称表中
									if(!stdName.equalsIgnoreCase(ApplianceName.trim())){
										List<AppliancePopularName> popRetList = popNameMgr.findByVarProperty(new KeyValueWithOperator("applianceStandardName.id", Integer.parseInt(ApplianceSpeciesId), "="), new KeyValueWithOperator("popularName", ApplianceName.trim(), "="));
										if(popRetList != null && popRetList.size() == 0){
											ApplianceStandardName sNameTemp = new ApplianceStandardName();
											sNameTemp.setId(Integer.parseInt(ApplianceSpeciesId));
											AppliancePopularName popNameTemp = new AppliancePopularName();
											popNameTemp.setApplianceStandardName(sNameTemp);
											popNameTemp.setPopularName(ApplianceName);
											popNameTemp.setBrief(LetterUtil.String2Alpha(ApplianceName.trim()));
											popNameTemp.setStatus(0);
											popNameMgr.save(popNameTemp);
										}
									}
								}
								
								//按需增加制造厂
								if(Manufacturer != null && Manufacturer.trim().length() > 0){
									int intRet = mafMgr.getTotalCount(new KeyValueWithOperator("applianceStandardName.id", Integer.parseInt(ApplianceSpeciesId), "="), new KeyValueWithOperator("manufacturer", Manufacturer.trim(), "="));
									if(intRet == 0){
										ApplianceStandardName sNameTemp = new ApplianceStandardName();
										sNameTemp.setId(Integer.parseInt(ApplianceSpeciesId));
										ApplianceManufacturer maf = new ApplianceManufacturer();
										maf.setApplianceStandardName(sNameTemp);
										maf.setManufacturer(Manufacturer.trim());
										maf.setBrief(LetterUtil.String2Alpha(Manufacturer.trim()));
										maf.setStatus(0);
										mafMgr.save(maf);
									}
								}
							}else{
								comSheet.setSpeciesType(true);	
								if(ApplianceName == null || ApplianceName.trim().length() == 0){
									ApplianceName = speciesMgr.findById(Integer.parseInt(ApplianceSpeciesId)).getName();;	//器具名称未填写，则默认为标准名称或分类名称
								}
							}
							comSheet.setApplianceSpeciesId(Integer.parseInt(ApplianceSpeciesId));
							
							comSheet.setApplianceName(ApplianceName);	//存器具名称
							comSheet.setAppFactoryCode(jsonObj.getString("ApplianceCode"));		//出厂编号
							comSheet.setAppManageCode(jsonObj.getString("AppManageCode"));		//管理编号
							comSheet.setApplianceModel(jsonObj.getString("Model"));		//型号规格
							comSheet.setRange(jsonObj.getString("Range"));		//测量范围
							comSheet.setAccuracy(jsonObj.getString("Accuracy"));	//精度等级
							comSheet.setManufacturer(jsonObj.getString("Manufacturer"));		//制造厂商
							comSheet.setQuantity(Integer.parseInt(jsonObj.get("Quantity").toString()));		//台件数
							comSheet.setMandatory(jsonObj.getInt("MandatoryInspection")==0?false:true);	//强制检验
							comSheet.setUrgent(jsonObj.getInt("Urgent")==0?false:true);		//是否加急
							comSheet.setSubcontract(jsonObj.getInt("Trans")==0?false:true);		//是否转包（0：转包，1:不需要转包）
							String SubContractor = jsonObj.getString("SubContractor");
							if(jsonObj.getInt("Trans")==0 && SubContractor!= null && SubContractor.trim().length() > 0){	//转包
								List<SubContractor> subConRetList = subConMgr.findByVarProperty(new KeyValueWithOperator("name",SubContractor.trim(),"="));
								if(subConRetList != null && subConRetList.size() > 0){
									subConList.add(subConRetList.get(0));
								}else{
									subConList.add(null);
								}
							}else{
								subConList.add(null);
							}
							comSheet.setAppearance(jsonObj.getString("Appearance"));//外观附件
							comSheet.setRepair(jsonObj.getInt("Repair")==0?false:true);		//需修理否
							comSheet.setReportType(jsonObj.getInt("ReportType"));	//报告形式
							comSheet.setOtherRequirements(jsonObj.getString("OtherRequirements"));	//其他要求
							comSheet.setLocation(jsonObj.getString("Location"));		//存放位置						
										
							/**********************  判断派定人是否存在及有效，并加入到alloteeList   ****************************/
							String Allotee = jsonObj.getString("Allotee");
							if(Allotee != null && Allotee.trim().length() > 0){
								Allotee = Allotee.trim();
								comSheet.setAllotee(Allotee);		//派定人
								List<Object[]> qualRetList = qualMgr.getQualifyUsers(Allotee, comSheet.getApplianceSpeciesId(), comSheet.getSpeciesType()?1:0, qualList);
								if(qualRetList != null && qualRetList.size() > 0){
									boolean alloteeChecked = false;
									for(Object[] objArray : qualRetList){
										if(!qualMgr.checkUserQualify((Integer)objArray[0], comSheet.getApplianceSpeciesId(), comSheet.getSpeciesType()?1:0, FlagUtil.QualificationType.Type_Except)){	//没有该检验项目的检验排外属性
											alloteeChecked = true;
											SysUser tempUser = new SysUser();
											tempUser.setId((Integer)objArray[0]);
											tempUser.setName((String)objArray[1]);
											
											alloteeList.add(tempUser);
											comSheet.setStatus(1);	//设置委托单状态：已分配
											break;
										}
									}
									
									if(!alloteeChecked){
										throw new Exception(String.format("派定人 '%s' 不存在或没有资质检验项目：%s，请重新选择！", Allotee, comSheet.getApplianceName()));
									}
								}else{
									throw new Exception(String.format("派定人 '%s' 不存在或没有资质检验项目：%s，请重新选择！", Allotee, comSheet.getApplianceName()));
								}
							}else{
								comSheet.setAllotee(null);		//派定人
								alloteeList.add(null);
							}
							comList.add(comSheet);
						}
						//如果为现场检测，判断本次检测的器具数目是否与现场委托书的器具数目相等
						if(CommissionType.equals("2")){
							int totalLocAppItem = locAppItemMgr.getTotalCount(new KeyValueWithOperator("localeMission.code", LocaleCommissionCode, "="));
							if(totalLocAppItem != comList.size()){
								throw new Exception("现场委托书号："+LocaleCommissionCode+" 下的器具条目数量与本次提交的器具条目数量不一致！");
							}
						}
						
						if(cSheetMgr.saveByBatch(comList,subConList,alloteeList,(SysUser)req.getSession().getAttribute("LOGIN_USER"),(SysUser)req.getSession().getAttribute("LOGIN_USER"),today)){
							try{
								/************  更新或新增委托单位联系人   *************/
								if(ContactPerson.length() > 0){
									CustomerContactorManager cusConMgr = new CustomerContactorManager();
									List<CustomerContactor> cusConList = cusConMgr.findByVarProperty(new KeyValueWithOperator("customerId", CustomerId, "="), new KeyValueWithOperator("name", ContactPerson, "="));
									if(cusConList != null){
										if(cusConList.size() > 0){
											CustomerContactor c = cusConList.get(0);
											if(ContactorTel.length() > 0){
												if(!ContactorTel.equalsIgnoreCase(c.getCellphone1()) && (c.getCellphone2() == null || c.getCellphone2().length() == 0)){
													c.setCellphone2(c.getCellphone1());
												}
												c.setCellphone1(ContactorTel);
											}
											c.setLastUse(today);
											c.setCount(c.getCount()+1);
											cusConMgr.update(c);
										}else{
											CustomerContactor c = new CustomerContactor();
											///////////////////////////////////////
											Customer a=new Customer();
											a.setId(CustomerId);
											c.setCustomer(a);
											//////////////////////////////////////
											//c.setCustomerId(CustomerId);
											c.setName(ContactPerson);
											c.setCellphone1(ContactorTel);
											c.setLastUse(today);
											c.setCount(1);
											cusConMgr.save(c);
										}
									}
								}
							}catch(Exception e){ }
							JSONObject retObj=new JSONObject();
							retObj.put("IsOK", true);
							JSONArray retArray = new JSONArray();
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
	//						UserManager userMgr = new UserManager();
	//						String ReceiverName = ReceiverId.length()>0?userMgr.findById(Integer.parseInt(ReceiverId)).getName():"";//接收人姓名
							
							Address RecipientAddressObj = RecipientAddress==null?null:addrMgr.findById(Integer.parseInt(RecipientAddress)); //样品接收地点
							Address PickupAddressObj = PickupAddress==null?null:addrMgr.findById(Integer.parseInt(PickupAddress));	//取样、取报告地点
							UserManager uMgr = new UserManager();
							for(int i = 0; i < comList.size(); i++){
								CommissionSheet comSheet = comList.get(i);
								JSONObject record = new JSONObject();
								record.put("Id", idList.get(i));
								record.put("CommissionNumber", comSheet.getCode());		//委托单号
								JSONObject printRecord = new JSONObject();
								printRecord.put("Code", comSheet.getCode());	//委托单号
								printRecord.put("Pwd", comSheet.getPwd());	//委托单密码
								printRecord.put("CommissionDate", sdf.format(comSheet.getCommissionDate()));	//委托日期
								printRecord.put("CustomerName", comSheet.getCustomerName());	//委托单位名称
								printRecord.put("CustomerTel", comSheet.getCustomerTel());	//委托单位电话
								printRecord.put("CustomerAddress", comSheet.getCustomerAddress());	//委托单位地址
								printRecord.put("CustomerZipCode", comSheet.getCustomerZipCode());	//委托单位邮政编码
								printRecord.put("SampleFrom", comSheet.getSampleFrom());	//证书单位名称
								printRecord.put("BillingTo", comSheet.getBillingTo());	//开票单位名称
								
								printRecord.put("ApplianceName", comSheet.getApplianceName());	//器具名称
								printRecord.put("ApplianceInfo",String.format("%s/%s/%s/%s", comSheet.getApplianceModel(),comSheet.getRange(),comSheet.getAccuracy(),comSheet.getManufacturer()));//器具信息
								printRecord.put("ApplianceNumber",String.format("%s[%s]", comSheet.getAppFactoryCode(),comSheet.getAppManageCode()));//器具编号
								printRecord.put("Quantity",comSheet.getQuantity().toString());//台件数
								printRecord.put("MandatoryInspection", CommissionSheetFlagUtil.getMandatoryByFlag(comSheet.getMandatory()));//强制检验
								printRecord.put("Appearance", comSheet.getAppearance());//外观附件
								printRecord.put("Repair", CommissionSheetFlagUtil.getRepairByFlag(comSheet.getRepair()));//需修理否
								printRecord.put("ReportType",CommissionSheetFlagUtil.getReportTypeByFlag(comSheet.getReportType()));//报告形式
								printRecord.put("OtherRequirements", comSheet.getOtherRequirements());//其他要求
								printRecord.put("Location", comSheet.getLocation());	//存放位置
								printRecord.put("Allotee", comSheet.getAllotee());	//派定人
								printRecord.put("ReceiverName", ((SysUser)req.getSession().getAttribute("LOGIN_USER")).getName());	//接收人姓名
								
								printRecord.put("HeadName",HeadNameAddr.getHeadName());	//台头名称
								printRecord.put("RecipientAddressName", RecipientAddressObj==null?"":RecipientAddressObj.getAddress());	//送样地址
								printRecord.put("RecipientAddressTel", (RecipientAddressObj==null || RecipientAddressObj.getTel()==null)?"":RecipientAddressObj.getTel());
								printRecord.put("PickupAddressName", PickupAddressObj==null?"":PickupAddressObj.getAddress());	//取样地址
								printRecord.put("PickupAddressTel", (PickupAddressObj==null || PickupAddressObj.getTel()==null)?"":PickupAddressObj.getTel());
								
								
								SysUser allotee = alloteeList.get(i);
								if(allotee != null){
									 SysUser tempUser = uMgr.findById(allotee.getId());
									printRecord.put("AlloteeJobNum",tempUser.getJobNum());	//员工工号
								}else{
									printRecord.put("AlloteeJobNum","");	//员工工号
								}
								
								record.put("PrintObj", printRecord);
								retArray.put(record);
							}
							retObj.put("CommissionSheetList", retArray);

							resp.setContentType("text/html;charset=utf-8");
							resp.getWriter().write(retObj.toString());
						}else{
							throw new Exception("保存委托单信息失败！");
						}
					} //end of synchronized
				} catch(NumberFormatException e){	//字符串转Integer错误
					
					JSONObject retObj=new JSONObject();
					try {
						retObj.put("IsOK", false);
						retObj.put("msg", String.format("处理失败！错误信息：数据输入不完整或格式错误！"));
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					if(e.getClass() == java.lang.NumberFormatException.class){	//自定义的消息
						log.debug("exception in CommissionSheetServlet-->case 0", e);
					}else{
						log.error("error in CommissionSheetServlet-->case 0", e);
					}
					resp.setContentType("text/html;charset=utf-8");
					resp.getWriter().write(retObj.toString());
				}catch (Exception e){
					
					JSONObject retObj=new JSONObject();
					try {
						retObj.put("IsOK", false);
						retObj.put("msg", String.format("处理失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					if(e.getClass() == java.lang.Exception.class){	//自定义的消息
						log.debug("exception in CommissionSheetServlet-->case 0", e);
					}else{
						log.error("error in CommissionSheetServlet-->case 0", e);
					}
					resp.setContentType("text/html;charset=utf-8");
					resp.getWriter().write(retObj.toString());
				}
				break;
			case 1: //查找委托单位的历史送检器具
				JSONObject retJSON = new JSONObject();
				int totalSize = 0;
				try {
					JSONArray jsonArray = new JSONArray();
					int page = 0;	//当前页面
					if (req.getParameter("page") != null)
						page = Integer.parseInt(req.getParameter("page").toString());
					int rows = 10;	//页面大小
					if (req.getParameter("rows") != null)
						rows = Integer.parseInt(req.getParameter("rows").toString());
					String CustomerName  = req.getParameter("CustomerName");
					if(CustomerName != null && CustomerName.trim().length() > 0){
						String ApplianceName = req.getParameter("ApplianceName");
						String BeginDate = req.getParameter("BeginDate");
						String EndDate = req.getParameter("EndDate");
						String cusName =  URLDecoder.decode(CustomerName.trim(), "UTF-8"); //解决jquery传递中文乱码问题
						
						List<KeyValueWithOperator> condList = new ArrayList<KeyValueWithOperator>();
						condList.add(new KeyValueWithOperator("status", 10, "<>"));//选择委托单尚未注销的
						condList.add(new KeyValueWithOperator("customerName",cusName,"="));
						if(BeginDate != null && BeginDate.length() > 0){
							condList.add(new KeyValueWithOperator("commissionDate", new Timestamp(Date.valueOf(BeginDate).getTime()), ">="));
						}
						if(EndDate != null && EndDate.length() > 0){
							condList.add(new KeyValueWithOperator("commissionDate", new Timestamp(Date.valueOf(EndDate).getTime()), "<"));
						}
						if(ApplianceName != null && ApplianceName.trim().length() > 0 ){
							String appName = URLDecoder.decode(ApplianceName.trim(), "UTF-8");
							condList.add(new KeyValueWithOperator("applianceName", "%"+appName+"%", "like"));
						}
						totalSize = cSheetMgr.getTotalCount(condList);
						List<CommissionSheet> retList = cSheetMgr.findPagedAllBySort(page, rows, "commissionDate", false, condList);
						if(retList != null && retList.size() > 0){
							ApplianceSpeciesManager speciesMgr = new ApplianceSpeciesManager();
							ApplianceStandardNameManager standardNameMgr = new ApplianceStandardNameManager();
							SubContractManager subConMgr = new SubContractManager();	//转包记录Mgr
							SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
							for(CommissionSheet cSheet : retList){
								JSONObject jsonObj = new JSONObject();
								jsonObj.put("CustomerName", cSheet.getCustomerName());	//委托单位
								jsonObj.put("CommissionCode", cSheet.getCode());	//委托单号
								jsonObj.put("ApplianceSpeciesId", cSheet.getApplianceSpeciesId());	//器具分类Id(或器具标准名称ID)
								if(cSheet.getSpeciesType()){	//器具授权（分类）名称
									jsonObj.put("SpeciesType", 1);	//器具分类类型
									ApplianceSpecies spe = speciesMgr.findById(cSheet.getApplianceSpeciesId());
									if(spe != null){
										jsonObj.put("ApplianceSpeciesName", spe.getName());
										jsonObj.put("ApplianceSpeciesNameStatus", spe.getStatus());
									}else{
										continue;
									}
								}else{	//器具标准名称
									jsonObj.put("SpeciesType", 0);
									ApplianceStandardName stName = standardNameMgr.findById(cSheet.getApplianceSpeciesId());
									if(stName != null){
										jsonObj.put("ApplianceSpeciesName", stName.getName());
										jsonObj.put("ApplianceSpeciesNameStatus", stName.getStatus());
									}else{
										continue;
									}
								}
								jsonObj.put("ApplianceName", cSheet.getApplianceName()==null?"":cSheet.getApplianceName());	//器具名称（常用名称）
								jsonObj.put("ApplianceCode", cSheet.getAppFactoryCode());	//出厂编号
								jsonObj.put("AppManageCode", cSheet.getAppManageCode());	//管理编号
								jsonObj.put("Model", cSheet.getApplianceName()==null?"":cSheet.getApplianceModel()==null?"":cSheet.getApplianceModel());	//型号规格
								jsonObj.put("Range", cSheet.getRange());		//测量范围
								jsonObj.put("Accuracy", cSheet.getAccuracy());	//精度等级
								jsonObj.put("Manufacturer", cSheet.getManufacturer()==null?"":cSheet.getManufacturer());	//制造厂商
								jsonObj.put("Quantity", cSheet.getQuantity()==null?"":cSheet.getQuantity());	//台/件数
								jsonObj.put("MandatoryInspection", cSheet.getMandatory()?1:0);	//强制检验
								jsonObj.put("Urgent", cSheet.getUrgent()?1:0);	//加急
								jsonObj.put("Trans", cSheet.getSubcontract()?1:0);	//转包
								if(!cSheet.getSubcontract()){	//0：转包
									List<SubContract> subRetList = subConMgr.findByVarProperty(new KeyValueWithOperator("commissionSheet.id", cSheet.getId(),"="), new KeyValueWithOperator("status", 0, "="));
									if(subRetList != null && subRetList.size() > 0){
										jsonObj.put("SubContractor", subRetList.get(0).getSubContractor().getName());	//转包方
									}else{
										jsonObj.put("SubContractor", "");	//转包方
									}
								}else{
									jsonObj.put("SubContractor", "");
								}
								jsonObj.put("Appearance", cSheet.getAppearance()==null?"":cSheet.getAppearance());	//外观附件
								jsonObj.put("Repair", cSheet.getRepair()?1:0);	//修理
								jsonObj.put("ReportType", cSheet.getReportType());	//报告形式
								jsonObj.put("OtherRequirements", cSheet.getOtherRequirements()==null?"":cSheet.getOtherRequirements());	//其它要求
								jsonObj.put("Location", cSheet.getLocation()==null?"":cSheet.getLocation());	//存放位置
								jsonObj.put("Allotee", cSheet.getAllotee()==null?"":cSheet.getAllotee());	//派定人
								jsonObj.put("CommissionDate", sf.format(cSheet.getCommissionDate()));		//委托日期
								jsonArray.put(jsonObj);	
							}
						}
					}
					retJSON.put("total", totalSize);
					retJSON.put("rows", jsonArray);
				} catch (Exception e) {
					
					try {
						retJSON.put("total", 0);
						retJSON.put("rows", new JSONArray());
					} catch (JSONException e1) {
					}
					if(e.getClass() == java.lang.Exception.class){	//自定义的消息
						log.debug("exception in CommissionSheetServlet-->case 1", e);
					}else{
						log.error("error in CommissionSheetServlet-->case 1", e);
					}
				}finally{
					resp.setContentType("text/json;charset=utf-8");
					resp.getWriter().write(retJSON.toString());
				}
				break;
			case 2:	//注销委托单(用于在新建委托单的界面上注销，单独注销委托单的功能在case 12)
				JSONObject retJSON2 = new JSONObject();
				try {
					String Code = req.getParameter("Code");	//委托单号
					String Pwd = req.getParameter("Pwd");	//委托单密码
					
					if(Code == null || Code.trim().length() == 0 || Pwd == null || Pwd.trim().length() == 0){
						throw new Exception("委托单号或密码为空！");
					}
					List<CommissionSheet> cSheetRetList = cSheetMgr.findByVarProperty(new KeyValueWithOperator("code", Code, "="),new KeyValueWithOperator("pwd", Pwd, "="));
					if(cSheetRetList != null && cSheetRetList.size() > 0){
						CommissionSheet cSheet = cSheetRetList.get(0);
						if(cSheet.getStatus() != FlagUtil.CommissionSheetStatus.Status_YiZhuXiao){	//委托单尚未注销
							cSheet.setStatus(FlagUtil.CommissionSheetStatus.Status_YiZhuXiao);
							//设置注销操作信息
							cSheet.setCancelDate(new Timestamp(System.currentTimeMillis()));
							cSheet.setCancelExecuterId(((SysUser)req.getSession().getAttribute("LOGIN_USER")).getId()); //注销操作执行人
							if(!cSheetMgr.update(cSheet)){
								throw new Exception("提交服务器失败！");
							}
						}
						retJSON2.put("IsOK", true);
					}else{
						throw new Exception("委托单号或密码错误！");
					}
				} catch (Exception e){
					
					try {
						retJSON2.put("IsOK", false);
						retJSON2.put("msg", String.format("注销委托单失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					if(e.getClass() == java.lang.Exception.class){	//自定义的消息
						log.debug("exception in CommissionSheetServlet-->case 2", e);
					}else{
						log.error("error in CommissionSheetServlet-->case 2", e);
					}
				}finally{
					resp.setContentType("text/json;charset=utf-8");
					resp.getWriter().write(retJSON2.toString());
				}
				break;
			case 3:	//查找委托单的信息
				JSONObject retJSON3 = new JSONObject();
				try {
					String Code = req.getParameter("Code");	//委托单号
					String Pwd = req.getParameter("Pwd");	//委托单密码
					
					if(Code == null || Code.trim().length() == 0 || Pwd == null || Pwd.trim().length() == 0){
						throw new Exception("委托单号或密码为空！");
					}
					ViewAppSpeStandardPopularNameManager mainMgr = new ViewAppSpeStandardPopularNameManager();
					List<CommissionSheet> cSheetRetList=new ArrayList<CommissionSheet>();
					if(req.getParameter("Type")==null){
						cSheetRetList = cSheetMgr.findByVarProperty(new KeyValueWithOperator("code", Code, "="),new KeyValueWithOperator("pwd", Pwd, "="));
					}else{
						cSheetRetList = cSheetMgr.findByVarProperty(new KeyValueWithOperator("code", Code, "="),new KeyValueWithOperator("pwd", Pwd, "="),new KeyValueWithOperator("status", -1, "="));
					}
						
					if(cSheetRetList != null && cSheetRetList.size() > 0){
						CommissionSheet cSheet = cSheetRetList.get(0);
						SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
						retJSON3.put("IsOK", true);
						
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("CommissionId", cSheet.getId());	//委托单ID
						jsonObj.put("CommissionCode", cSheet.getCode());
						jsonObj.put("CommissionPwd", cSheet.getPwd());
						jsonObj.put("CommissionStatus", cSheet.getStatus());	//委托单状态
						jsonObj.put("CommissionType", cSheet.getCommissionType());
						jsonObj.put("CommissionDate", sf.format((cSheet.getCommissionType()==2 && cSheet.getLocaleCommissionDate()!=null)?cSheet.getLocaleCommissionDate():cSheet.getCommissionDate()));
						jsonObj.put("CustomerName", cSheet.getCustomerName());
						jsonObj.put("CustomerTel", cSheet.getCustomerTel());
						jsonObj.put("CustomerAddress", cSheet.getCustomerAddress());
						jsonObj.put("CustomerZipCode", cSheet.getCustomerZipCode());
						jsonObj.put("ContactPerson", cSheet.getCustomerContactor());
						jsonObj.put("ContactorTel", cSheet.getCustomerContactorTel());
						jsonObj.put("SampleFrom", cSheet.getSampleFrom());
						jsonObj.put("CustomerHandler", cSheet.getCustomerHandler());//委托人
						jsonObj.put("BillingTo", cSheet.getBillingTo());
						jsonObj.put("ApplianceName", cSheet.getApplianceName());	//器具名称	
						if(cSheet.getSpeciesType()!=null){
							jsonObj.put("SpeciesType", cSheet.getSpeciesType()?1:0);
							jsonObj.put("ApplianceSpeciesId", cSheet.getApplianceSpeciesId());
							String queryString = "from ViewApplianceSpecialStandardNamePopularName as model " +
								" where model.id.id = ? and model.id.type = ? ";
							List<ViewApplianceSpecialStandardNamePopularName> vRetList = mainMgr.findPageAllByHQL(queryString, 1, 30, cSheet.getApplianceSpeciesId(), jsonObj.getInt("SpeciesType"));
							String ApplianceSpeciesName="";
							
							if(vRetList!=null&&vRetList.size()>0){
								ApplianceSpeciesName=vRetList.get(0).getName();
							}
							
							jsonObj.put("ApplianceSpeciesName", ApplianceSpeciesName);
						}
						jsonObj.put("HeadName", cSheet.getHeadNameId());
						jsonObj.put("RecipientAddress", cSheet.getSampleAddress());	//送样地址
						jsonObj.put("PickupAddress", cSheet.getReportAddress());	//取样地址
						
						jsonObj.put("Model", cSheet.getApplianceModel());
						jsonObj.put("ApplianceCode", cSheet.getAppFactoryCode());	//出厂编号
						jsonObj.put("ApplianceManageCode", cSheet.getAppManageCode());	//管理编号
						jsonObj.put("Manufacturer", cSheet.getManufacturer());
						jsonObj.put("Quantity", cSheet.getQuantity());
						jsonObj.put("Mandatory", cSheet.getMandatory()?1:0);	//强制检定
						jsonObj.put("Ness", cSheet.getUrgent()?1:0);			//是否加急
						jsonObj.put("Repair", cSheet.getRepair()?1:0);			//是否修理
						jsonObj.put("Trans", cSheet.getSubcontract()?1:0);			//是否转包
						jsonObj.put("Allotee", cSheet.getAllotee()==null?"":cSheet.getAllotee());//派定人
						jsonObj.put("Appearance", cSheet.getAppearance());	//外观附件
						jsonObj.put("OtherRequirements", cSheet.getOtherRequirements());	//其他要求
						jsonObj.put("Status", cSheet.getStatus());	//委托单状态
						jsonObj.put("ReportType", cSheet.getReportType());	//报告形式
						jsonObj.put("Location", cSheet.getLocation()==null?"":cSheet.getLocation());	//存放位置
						jsonObj.put("TotalFee", cSheet.getTotalFee());	//委托单总计费用
						jsonObj.put("Attachment", cSheet.getAttachment());
						jsonObj.put("Remark", cSheet.getRemark());
						jsonObj.put("AlloteeRule", SystemCfgUtil.getTaskAllotRule()==0?0:1);
						retJSON3.put("CommissionObj", jsonObj);
					}else{	
						if(req.getParameter("Type")==null){
							throw new Exception("委托单号或密码错误！"+req.getParameter("Type"));
						}else{
							throw new Exception("委托单号或密码错误，或者该委托单不是预留委托单！"+req.getParameter("Type"));
						}
					}
				} catch (Exception e){
					
					try {
						retJSON3.put("IsOK", false);
						retJSON3.put("msg", String.format("查询委托单失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					if(e.getClass() == java.lang.Exception.class){	//自定义的消息
						log.debug("exception in CommissionSheetServlet-->case 3", e);
					}else{
						log.error("error in CommissionSheetServlet-->case 3", e);
					}
				}finally{
					resp.setContentType("text/html;charset=utf-8");
					resp.getWriter().write(retJSON3.toString());
				}
				break;
			case 4://根据委托单号、委托单位等查询委托单信息
				JSONObject retJSON4 = new JSONObject();
				try {
					String Code = req.getParameter("Code");	//委托单号
					String CustomerId = req.getParameter("CustomerId");//委托单位
					String CustomerName = req.getParameter("CustomerName");//委托单位名称
					String DateFrom = req.getParameter("DateFrom");
					String DateEnd = req.getParameter("DateEnd");
					String Status = req.getParameter("Status");
					int page = 1;
					if (req.getParameter("page") != null)
						page = Integer.parseInt(req.getParameter("page").toString());
					int rows = 10;
					if (req.getParameter("rows") != null)
						rows = Integer.parseInt(req.getParameter("rows").toString());
					
					List<KeyValueWithOperator> keys = new ArrayList<KeyValueWithOperator>();
					if(Code != null && Code.length() > 0){
						keys.add(new KeyValueWithOperator("code", "%"+Code+"%", "like"));
					}
					if(CustomerId!=null && CustomerId.length() > 0){
						keys.add(new KeyValueWithOperator("customerId", Integer.valueOf(CustomerId), "="));
					}
					if(CustomerName!=null && CustomerName.length() > 0){
						String CustomerNameStr = URLDecoder.decode(CustomerName, "utf-8");
						keys.add(new KeyValueWithOperator("customerName", "%"+CustomerNameStr+"%", "like"));
					}
					if(DateFrom != null && DateFrom.length() > 0){
						Timestamp Start = new Timestamp(Date.valueOf(URLDecoder.decode(DateFrom, "utf-8")).getTime());
						keys.add(new KeyValueWithOperator("commissionDate", Start, ">="));
					}
					if(DateEnd != null && DateEnd.length() > 0){
						Timestamp End = new Timestamp(Date.valueOf(URLDecoder.decode(DateEnd, "utf-8")).getTime());
						keys.add(new KeyValueWithOperator("commissionDate", End, "<="));
					}
					if(Status != null && Status.length() > 0 ){
						keys.add(new KeyValueWithOperator("status", Integer.valueOf(Status), "="));
					}
					
					List<CommissionSheet> cSheetRetList = cSheetMgr.findPagedAllBySort(page, rows, "commissionDate", false, keys);
					int total = cSheetMgr.getTotalCount(keys);
					JSONArray options = new JSONArray();
					CertificateFeeAssignManager feeMgr=new CertificateFeeAssignManager();
					if(cSheetRetList != null && cSheetRetList.size() > 0){
						for(CommissionSheet cSheet : cSheetRetList)
						{
							JSONObject jsonObj = new JSONObject();
							jsonObj.put("Id", cSheet.getId());
							jsonObj.put("DetailListCode", cSheet.getDetailListCode());
							jsonObj.put("Code", cSheet.getCode());
							jsonObj.put("Pwd", cSheet.getPwd());
							jsonObj.put("CustomerId", cSheet.getCustomerId());
							jsonObj.put("CustomerName", cSheet.getCustomerName());
							jsonObj.put("ApplianceSpeciesId", cSheet.getApplianceSpeciesId());	//器具分类Id(或器具标准名称ID)
							if(cSheet.getSpeciesType()){	//器具授权（分类）名称
								jsonObj.put("SpeciesType", 1);	//器具分类类型
								ApplianceSpecies spe = (new ApplianceSpeciesManager()).findById(cSheet.getApplianceSpeciesId());
								if(spe != null){
									jsonObj.put("ApplianceSpeciesName", spe.getName());
									jsonObj.put("ApplianceSpeciesNameStatus", spe.getStatus());
								}else{
									continue;
								}
							}else{	//器具标准名称
								jsonObj.put("SpeciesType", 0);
								ApplianceStandardName stName = (new ApplianceStandardNameManager()).findById(cSheet.getApplianceSpeciesId());
								if(stName != null){
									jsonObj.put("ApplianceSpeciesName", stName.getName());
									jsonObj.put("ApplianceSpeciesNameStatus", stName.getStatus());
								}else{
									continue;
								}
							}
							jsonObj.put("ApplianceName", cSheet.getApplianceName()==null?"":cSheet.getApplianceName());	//器具名称（常用名称）
							jsonObj.put("ApplianceCode", cSheet.getAppFactoryCode());	//出厂编号
							jsonObj.put("AppManageCode", cSheet.getAppManageCode());	//管理编号
							jsonObj.put("Model", cSheet.getApplianceName()==null?"":cSheet.getApplianceModel()==null?"":cSheet.getApplianceModel());	//型号规格
							jsonObj.put("Range", cSheet.getRange());		//测量范围
							jsonObj.put("Accuracy", cSheet.getAccuracy());	//精度等级
							jsonObj.put("Manufacturer", cSheet.getManufacturer()==null?"":cSheet.getManufacturer());	//制造厂商
							jsonObj.put("Quantity", cSheet.getQuantity()==null?"":cSheet.getQuantity());	//台/件数
							jsonObj.put("MandatoryInspection", cSheet.getMandatory()?1:0);	//强制检验
							jsonObj.put("Urgent", cSheet.getUrgent()?1:0);	//加急
							jsonObj.put("Trans", cSheet.getSubcontract()?1:0);	//转包
							if(!cSheet.getSubcontract()){	//0：转包
								List<SubContract> subRetList = (new SubContractManager()).findByVarProperty(new KeyValueWithOperator("commissionSheet.id", cSheet.getId(),"="), new KeyValueWithOperator("status", 0, "="));
								if(subRetList != null && subRetList.size() > 0){
									jsonObj.put("SubContractor", subRetList.get(0).getSubContractor().getName());	//转包方
								}else{
									jsonObj.put("SubContractor", "");	//转包方
								}
							}else{
								jsonObj.put("SubContractor", "");
							}
							jsonObj.put("Appearance", cSheet.getAppearance()==null?"":cSheet.getAppearance());	//外观附件
							jsonObj.put("Repair", cSheet.getRepair()?1:0);	//修理
							jsonObj.put("ReportType", cSheet.getReportType());	//报告形式
							jsonObj.put("OtherRequirements", cSheet.getOtherRequirements()==null?"":cSheet.getOtherRequirements());	//其它要求
							jsonObj.put("Location", cSheet.getLocation()==null?"":cSheet.getLocation());	//存放位置
							jsonObj.put("Allotee", cSheet.getAllotee()==null?"":cSheet.getAllotee());	//派定人
							SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
							jsonObj.put("CommissionDate", sf.format(cSheet.getCommissionDate()));		//委托日期
							jsonObj.put("Status", cSheet.getStatus());	//委托单状态
							
							jsonObj.put("Attachment", cSheet.getAttachment());
							
							/***********在CertificateFeeAssign(原始记录证书费用分配)中查找委托单的费用详情***********/
							List<Object[]> FList=feeMgr.findByHQL(CertificateFeeAssignManager.queryStringAllAllFeeByCommissionSheetId, cSheet.getId());		
																	
							if(FList.isEmpty()){
						    	jsonObj.put("TestFee", 0.0);
								jsonObj.put("RepairFee", 0.0);
								jsonObj.put("MaterialFee", 0.0);
								jsonObj.put("CarFee", 0.0);
								jsonObj.put("DebugFee", 0.0);
								jsonObj.put("OtherFee", 0.0);
								jsonObj.put("TotalFee", 0.0);
								jsonObj.put("OldTestFee", 0.0);
								jsonObj.put("OldRepairFee", 0.0);
								jsonObj.put("OldMaterialFee", 0.0);
								jsonObj.put("OldCarFee", 0.0);
								jsonObj.put("OldDebugFee", 0.0);
								jsonObj.put("OldOtherFee", 0.0);
								jsonObj.put("OldTotalFee", 0.0);
						    }else{
							    for(Object[] fee:FList){							    	
									jsonObj.put("TestFee", (Double)fee[0]==null?0.0:(Double)fee[0]);
									jsonObj.put("RepairFee", (Double)fee[1]==null?0.0:(Double)fee[1]);
									jsonObj.put("MaterialFee", (Double)fee[2]==null?0.0:(Double)fee[2]);
									jsonObj.put("CarFee", (Double)fee[3]==null?0.0:(Double)fee[3]);
									jsonObj.put("DebugFee", (Double)fee[4]==null?0.0:(Double)fee[4]);
									jsonObj.put("OtherFee", (Double)fee[5]==null?0.0:(Double)fee[5]);
									jsonObj.put("TotalFee", (Double)fee[6]==null?0.0:(Double)fee[6]);
									jsonObj.put("OldTestFee", (Double)fee[7]==null?0.0:(Double)fee[7]);
									jsonObj.put("OldRepairFee", (Double)fee[8]==null?0.0:(Double)fee[8]);
									jsonObj.put("OldMaterialFee", (Double)fee[9]==null?0.0:(Double)fee[9]);
									jsonObj.put("OldCarFee", (Double)fee[10]==null?0.0:(Double)fee[10]);
									jsonObj.put("OldDebugFee", (Double)fee[11]==null?0.0:(Double)fee[11]);
									jsonObj.put("OldOtherFee", (Double)fee[12]==null?0.0:(Double)fee[12]);
									jsonObj.put("OldTotalFee", (Double)fee[13]==null?0.0:(Double)fee[13]);
							   }
						    }
							
							options.put(jsonObj);
						}
					}
					retJSON4.put("total", total);
					retJSON4.put("rows", options);
				} catch (Exception e){
					try {
						retJSON4.put("total", 0);
						retJSON4.put("rows", new JSONArray());
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					if(e.getClass() == java.lang.Exception.class){	//自定义的消息
						log.debug("exception in CommissionSheetServlet-->case 4", e);
					}else{
						log.error("error in CommissionSheetServlet-->case 4", e);
					}
				}finally{
					resp.setContentType("text/json;charset=utf-8");
					resp.getWriter().write(retJSON4.toString());
				}
				break;
			case 5: // 完工确认   --刘振
				JSONObject retObj5=new JSONObject();
				try {
					String CommissionSheetIds = req.getParameter("CommissionSheetId");	//多份委托单之间用“;”分割
					if(CommissionSheetIds == null || CommissionSheetIds.trim().length() == 0){
						throw new Exception("参数不完整！");
					}
					
					String[] cSheetIdArray = CommissionSheetIds.split(";");
					if(cSheetIdArray == null || cSheetIdArray.length == 0){
						throw new Exception("参数不完整！");
					}
					
					Timestamp today = new Timestamp(System.currentTimeMillis());// 当前时间
					SysUser Handler = (SysUser) req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser);// 当前操作用户
					
					
					String hqlQueryString_FinishQuantity = "select sum(model.quantity) from OriginalRecord as model where model.commissionSheet.id=? and model.status<>1 and model.taskAssign.status<>1 and model.verifyAndAuthorize.authorizeResult=? and model.verifyAndAuthorize.isAuthBgRuning is null ";	//签字通过的原始记录的器具总数
					String hqlQueryString_WithdrawQuantity = "select sum(model.number) from Withdraw as model where model.commissionSheet.id=? and model.executeResult=?";	//已批准的退样器具数量
					String hqlQueryString_SubContract = "select count(*) from SubContract as model where model.commissionSheet.id=? and model.status<>1 and model.receiveDate is not null";
					
					
					int doneSuccessed = 0;	//完工确认成功的委托单数量
					for(String CommissionSheetId : cSheetIdArray){
						int CommissionId = Integer.parseInt(CommissionSheetId.trim());	//委托单ID
						
						String FinishLocation = req.getParameter("FinishLocation");
						CommissionSheet cSheetRet = cSheetMgr.findById(CommissionId);
						if(cSheetRet.getStatus() == 3 || 	//完工确认（已完工）
								cSheetRet.getStatus() == 4 || //已结账
								cSheetRet.getStatus() == 9 || //已结束
								cSheetRet.getStatus() == 10){	//已注销
							if(cSheetIdArray.length == 1){
								throw new Exception("该委托单不允许再次完工确认！");
							}else{
								continue;
							}
						}else{
							//查询完工器具数量和退样器具数量，以及是否转包
							List<Long> fQuantityList = cSheetMgr.findByHQL(hqlQueryString_FinishQuantity, cSheetRet.getId(), true);	//完工器具数量
							int finishQuantity = 0;
							if(fQuantityList != null && fQuantityList.size() > 0 && fQuantityList.get(0) != null){
								finishQuantity = fQuantityList.get(0).intValue();
							}
							List<Long> wQuantityList = cSheetMgr.findByHQL(hqlQueryString_WithdrawQuantity, cSheetRet.getId(), true);	//退样器具数量
							int wQuatity = cSheetRet.getQuantity();
							if(wQuantityList != null && wQuantityList.size() > 0 && wQuantityList.get(0) != null){
								wQuatity = cSheetRet.getQuantity() - ((Long)wQuantityList.get(0)).intValue();  //有效器具数量
							}
							int iSubContract = cSheetMgr.getTotalCountByHQL(hqlQueryString_SubContract, cSheetRet.getId());
							if(iSubContract > 0 || cSheetRet.getCommissionType() == 5 ){	//该委托单有转包或该业务为其他业务，则完工确认时不需要判断‘完工器具数量是否大于等于有效器具数量’;
								//无操作
							}else{
								if(finishQuantity < wQuatity){
									if(cSheetIdArray.length == 1){
										throw new Exception(String.format("该委托单不能完工：完工器具数量'%d'少于委托单的有效器具数量'%d'！", finishQuantity, wQuatity));
									}else{
										continue;
									}
								}else if(finishQuantity > wQuatity){
									if(cSheetIdArray.length == 1){
										throw new Exception(String.format("该委托单不能完工：完工器具数量'%d'多于委托单的有效器具数量'%d'！", finishQuantity, wQuatity));
									}else{
										continue;
									}
								}
							}
							try{
								cSheetMgr.commissionSheetFinishConfirm(cSheetRet, Handler, FinishLocation, today, wQuatity);
								doneSuccessed++;
							}catch(Exception e){
								if(cSheetIdArray.length == 1){
									throw e;
								}else{
									continue;
								}
							}
						}	
					}
					if(doneSuccessed == 0){
						throw new Exception("无。请尝试对单张委托单进行完工确认！");
					}else{
						retObj5.put("IsOK", true);
						retObj5.put("msg", String.format("完工确认成功!本次完工确认的委托单数：%d", doneSuccessed));
					}
				}catch (Exception e){
					
					try {
						retObj5.put("IsOK", false);
						retObj5.put("msg", String.format("完工确认失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					if(e.getClass() == java.lang.Exception.class){	//自定义的消息
						log.debug("exception in CommissionSheetServlet-->case 5", e);
					}else{
						log.error("error in CommissionSheetServlet-->case 5", e);
					}
				}finally{
					resp.setContentType("text/html;charset=utf-8");
					resp.getWriter().write(retObj5.toString());
				}
				break;
			case 6:	//查找委托单的信息(委托单号查询,用于补打委托单)
				JSONObject retJSON6 = new JSONObject();
				try {
					String Code = req.getParameter("Code");	//委托单号
					String Pwd = req.getParameter("Pwd");	//委托单密码
					
					if(Code == null || Code.trim().length() == 0 || Pwd == null || Pwd.trim().length() == 0){
						throw new Exception("委托单号或密码为空！");
					}
					AddressManager addrMgr = new AddressManager();
					
					List<CommissionSheet> cSheetRetList = cSheetMgr.findByVarProperty(new KeyValueWithOperator("code", Code, "="),new KeyValueWithOperator("pwd", Pwd, "="));
					if(cSheetRetList != null && cSheetRetList.size() > 0){
						CommissionSheet cSheet = cSheetRetList.get(0);
						SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
						retJSON6.put("IsOK", true);
						
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("CommissionId", cSheet.getId());	//委托单ID
						jsonObj.put("CommissionCode", cSheet.getCode());
						jsonObj.put("CommissionPwd", cSheet.getPwd());
						jsonObj.put("CommissionStatus", cSheet.getStatus());	//委托单状态
						jsonObj.put("CommissionType", cSheet.getCommissionType());
						jsonObj.put("CommissionDate", sf.format(cSheet.getCommissionDate()));
						jsonObj.put("CustomerName", cSheet.getCustomerName());
						jsonObj.put("CustomerTel", cSheet.getCustomerTel());
						jsonObj.put("CustomerAddress", cSheet.getCustomerAddress());
						jsonObj.put("CustomerZipCode", cSheet.getCustomerZipCode());
						jsonObj.put("ContactPerson", cSheet.getCustomerContactor());
						jsonObj.put("ContactorTel", cSheet.getCustomerContactorTel());
						jsonObj.put("SampleFrom", cSheet.getSampleFrom());
						jsonObj.put("BillingTo", cSheet.getBillingTo());
						jsonObj.put("ApplianceName", cSheet.getApplianceName());	//器具名称
						jsonObj.put("Model", cSheet.getApplianceModel());
						jsonObj.put("ApplianceCode", cSheet.getAppFactoryCode());	//出厂编号
						jsonObj.put("ApplianceManageCode", cSheet.getAppManageCode());	//管理编号
						jsonObj.put("Manufacturer", cSheet.getManufacturer());
						jsonObj.put("Quantity", cSheet.getQuantity());
						jsonObj.put("Mandatory", cSheet.getMandatory()?1:0);	//强制检定
						jsonObj.put("Ness", cSheet.getUrgent()?1:0);			//是否加急
						jsonObj.put("Appearance", cSheet.getAppearance());	//外观附件
						jsonObj.put("OtherRequirements", cSheet.getOtherRequirements());	//其他要求
						jsonObj.put("Status", cSheet.getStatus());	//委托单状态
						
						jsonObj.put("TotalFee", cSheet.getTotalFee());	//委托单总计费用
						
						retJSON6.put("CommissionObj", jsonObj);
						JSONObject printRecord = new JSONObject();
						printRecord.put("Code", cSheet.getCode());	//委托单号
						printRecord.put("Pwd", cSheet.getPwd());	//委托单密码
						printRecord.put("CommissionDate", sf.format(cSheet.getCommissionDate()));	//委托日期
						printRecord.put("CustomerName", cSheet.getCustomerName());	//委托单位名称
						printRecord.put("CustomerTel", cSheet.getCustomerTel());	//委托单位电话
						printRecord.put("CustomerAddress", cSheet.getCustomerAddress());	//委托单位地址
						printRecord.put("CustomerZipCode", cSheet.getCustomerZipCode());	//委托单位邮政编码
						printRecord.put("SampleFrom", cSheet.getSampleFrom());	//证书单位名称
						printRecord.put("BillingTo", cSheet.getBillingTo());	//开票单位名称
						
						printRecord.put("ApplianceName", cSheet.getApplianceName());	//器具名称
						printRecord.put("ApplianceInfo",String.format("%s/%s/%s/%s", cSheet.getApplianceModel(),cSheet.getRange(),cSheet.getAccuracy(),cSheet.getManufacturer()));//器具信息
						printRecord.put("ApplianceNumber",String.format("%s[%s]", cSheet.getAppFactoryCode(),cSheet.getAppManageCode()));//器具编号
						printRecord.put("Quantity",cSheet.getQuantity().toString());//台件数
						printRecord.put("MandatoryInspection", CommissionSheetFlagUtil.getMandatoryByFlag(cSheet.getMandatory()));//强制检验
						printRecord.put("Appearance", cSheet.getAppearance());//外观附件
						printRecord.put("Repair", CommissionSheetFlagUtil.getRepairByFlag(cSheet.getRepair()));//需修理否
						printRecord.put("ReportType",CommissionSheetFlagUtil.getReportTypeByFlag(cSheet.getReportType()));//报告形式
						printRecord.put("OtherRequirements", cSheet.getOtherRequirements());//其他要求
						printRecord.put("Location", cSheet.getLocation());	//存放位置
						printRecord.put("Allotee", cSheet.getAllotee());	//派定人
						printRecord.put("ReceiverName", ((SysUser)req.getSession().getAttribute("LOGIN_USER")).getName());	//接收人姓名
						
						Address RecipientAddressObj = cSheet.getSampleAddress()==null?null:addrMgr.findById(cSheet.getSampleAddress()); //样品接收地点
						Address PickupAddressObj = cSheet.getReportAddress()==null?null:addrMgr.findById(cSheet.getReportAddress());	//取样、取报告地点
						printRecord.put("HeadName",cSheet.getHeadName());	//台头名称
						printRecord.put("RecipientAddressName", RecipientAddressObj==null?"":RecipientAddressObj.getAddress());	//接收地点
						printRecord.put("RecipientAddressTel", (RecipientAddressObj==null || RecipientAddressObj.getTel()==null)?"":RecipientAddressObj.getTel());
						printRecord.put("PickupAddressName", PickupAddressObj==null?"":PickupAddressObj.getAddress());	//取样地址
						printRecord.put("PickupAddressTel", (PickupAddressObj==null || PickupAddressObj.getTel()==null)?"":PickupAddressObj.getTel());
						
						retJSON6.put("PrintObj", printRecord);
					}else{
						throw new Exception("委托单号或密码错误！");
						
					}
				} catch (Exception e){
					
					try {
						retJSON6.put("IsOK", false);
						retJSON6.put("msg", String.format("查询委托单失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					if(e.getClass() == java.lang.Exception.class){	//自定义的消息
						log.debug("exception in CommissionSheetServlet-->case 6", e);
					}else{
						log.error("error in CommissionSheetServlet-->case 6", e);
					}
				}finally{
					resp.setContentType("text/html;charset=utf-8");
					resp.getWriter().write(retJSON6.toString());
				}
				break;
			case 7: // 客户取样   --刘振
				JSONObject retObj7=new JSONObject();
				try {
					int CommissionId = Integer.parseInt(req.getParameter("CommissionSheetId"));	//委托单ID
					
					CommissionSheet cSheetRet = cSheetMgr.findById(CommissionId);
					if(cSheetRet.getCheckOutDate() == null){
						throw new Exception("该委托单尚未结账，不能取样！");
					}
					if(cSheetRet.getStatus() == 9){
						throw new Exception("该委托单已取样，不能重复操作！");
					}else{
						cSheetRet.setStatus(FlagUtil.CommissionSheetStatus.Status_YiJieShu);     //9客户取样，结束状态				
//						SysUser Handler = (SysUser) req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser);// 当前操作用户
//						cSheetRet.setFinishStaffId(Handler.getId()); //更新委托单
	
//						cSheetRet.setFinishDate(today);
						boolean yes = cSheetMgr.update(cSheetRet);
						if(!yes){     //更新委托单
							throw new Exception("委托单更新失败！");
						} 
						retObj7.put("IsOK", true);
						retObj7.put("msg", "客户取样确认成功！");
					}	
				}catch (Exception e){
					
					try {
						retObj7.put("IsOK", false);
						retObj7.put("msg", String.format("客户取样确认失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					if(e.getClass() == java.lang.Exception.class){	//自定义的消息
						log.debug("exception in CommissionSheetServlet-->case 7", e);
					}else{
						log.error("error in CommissionSheetServlet-->case 7", e);
					}
				}finally{
					resp.setContentType("text/html;charset=utf-8");
					resp.getWriter().write(retObj7.toString());
				}
				break;
			case 8:	//多条件查找委托单的信息（带打印信息）：用于补打委托单
				JSONObject retJSON8 = new JSONObject();
				int totalSize8 = 0;
				try {
					JSONArray jsonArray = new JSONArray();
					int page = 0;	//当前页面
					if (req.getParameter("page") != null)
						page = Integer.parseInt(req.getParameter("page").toString());
					int rows = 10;	//页面大小
					if (req.getParameter("rows") != null)
						rows = Integer.parseInt(req.getParameter("rows").toString());
						String CustomerName  = req.getParameter("CustomerName");
					
						String ApplianceName = req.getParameter("ApplianceName");
						String BeginDate = req.getParameter("BeginDate");
						String EndDate = req.getParameter("EndDate");
						String Code = req.getParameter("Code");	//委托单号
						
						
						List<KeyValueWithOperator> condList = new ArrayList<KeyValueWithOperator>();
						condList.add(new KeyValueWithOperator("status", 10, "<>"));//选择委托单尚未注销的
						if(CustomerName != null && CustomerName.length() > 0){
							String cusName =  URLDecoder.decode(CustomerName.trim(), "UTF-8"); //解决jquery传递中文乱码问题
							condList.add(new KeyValueWithOperator("customerName",cusName,"="));
						}
						if(Code != null && Code.length() > 0){
							condList.add(new KeyValueWithOperator("code", Code, "="));
						}
						if(BeginDate != null && BeginDate.length() > 0){
							condList.add(new KeyValueWithOperator("commissionDate", new Timestamp(Date.valueOf(BeginDate).getTime()), ">="));
						}
						if(EndDate != null && EndDate.length() > 0){
							condList.add(new KeyValueWithOperator("commissionDate", new Timestamp(Date.valueOf(EndDate).getTime()), "<"));
						}
						if(ApplianceName != null && ApplianceName.trim().length() > 0 ){
							String appName = URLDecoder.decode(ApplianceName.trim(), "UTF-8");
							condList.add(new KeyValueWithOperator("applianceName", "%"+appName+"%", "like"));
						}
						totalSize8 = cSheetMgr.getTotalCount(condList);
						AddressManager addrMgr = new AddressManager();
						List<CommissionSheet> retList = cSheetMgr.findPagedAllBySort(page, rows, "commissionDate", false, condList);
						if(retList != null && retList.size() > 0){
							ApplianceSpeciesManager speciesMgr = new ApplianceSpeciesManager();
							ApplianceStandardNameManager standardNameMgr = new ApplianceStandardNameManager();
							SubContractManager subConMgr = new SubContractManager();	//转包记录Mgr
							SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
							for(CommissionSheet cSheet : retList){
								JSONObject jsonObj = new JSONObject();
								jsonObj.put("Id", cSheet.getId());
								jsonObj.put("Code", cSheet.getCode());
								jsonObj.put("Pwd", cSheet.getPwd());
								jsonObj.put("CustomerName", cSheet.getCustomerName());
								jsonObj.put("ApplianceSpeciesId", cSheet.getApplianceSpeciesId());	//器具分类Id(或器具标准名称ID)
								if(cSheet.getSpeciesType()){	//器具授权（分类）名称
									jsonObj.put("SpeciesType", 1);	//器具分类类型
									ApplianceSpecies spe = speciesMgr.findById(cSheet.getApplianceSpeciesId());
									if(spe != null){
										jsonObj.put("ApplianceSpeciesName", spe.getName());
										jsonObj.put("ApplianceSpeciesNameStatus", spe.getStatus());
									}else{
										continue;
									}
								}else{	//器具标准名称
									jsonObj.put("SpeciesType", 0);
									ApplianceStandardName stName = standardNameMgr.findById(cSheet.getApplianceSpeciesId());
									if(stName != null){
										jsonObj.put("ApplianceSpeciesName", stName.getName());
										jsonObj.put("ApplianceSpeciesNameStatus", stName.getStatus());
									}else{
										continue;
									}
								}
								jsonObj.put("ApplianceName", cSheet.getApplianceName()==null?"":cSheet.getApplianceName());	//器具名称（常用名称）
								jsonObj.put("ApplianceCode", cSheet.getAppFactoryCode());	//出厂编号
								jsonObj.put("AppManageCode", cSheet.getAppManageCode());	//管理编号
								jsonObj.put("Model", cSheet.getApplianceName()==null?"":cSheet.getApplianceModel()==null?"":cSheet.getApplianceModel());	//型号规格
								jsonObj.put("Range", cSheet.getRange());		//测量范围
								jsonObj.put("Accuracy", cSheet.getAccuracy());	//精度等级
								jsonObj.put("Manufacturer", cSheet.getManufacturer()==null?"":cSheet.getManufacturer());	//制造厂商
								jsonObj.put("Quantity", cSheet.getQuantity()==null?"":cSheet.getQuantity());	//台/件数
								jsonObj.put("MandatoryInspection", cSheet.getMandatory()?1:0);	//强制检验
								jsonObj.put("Urgent", cSheet.getUrgent()?1:0);	//加急
								jsonObj.put("Trans", cSheet.getSubcontract()?1:0);	//转包
								if(!cSheet.getSubcontract()){	//0：转包
									List<SubContract> subRetList = subConMgr.findByVarProperty(new KeyValueWithOperator("commissionSheet.id", cSheet.getId(),"="), new KeyValueWithOperator("status", 0, "="));
									if(subRetList != null && subRetList.size() > 0){
										jsonObj.put("SubContractor", subRetList.get(0).getSubContractor().getName());	//转包方
									}else{
										jsonObj.put("SubContractor", "");	//转包方
									}
								}else{
									jsonObj.put("SubContractor", "");
								}
								jsonObj.put("Appearance", cSheet.getAppearance()==null?"":cSheet.getAppearance());	//外观附件
								jsonObj.put("Repair", cSheet.getRepair()?1:0);	//修理
								jsonObj.put("ReportType", cSheet.getReportType());	//报告形式
								jsonObj.put("OtherRequirements", cSheet.getOtherRequirements()==null?"":cSheet.getOtherRequirements());	//其它要求
								jsonObj.put("Location", cSheet.getLocation()==null?"":cSheet.getLocation());	//存放位置
								jsonObj.put("Allotee", cSheet.getAllotee()==null?"":cSheet.getAllotee());	//派定人
								jsonObj.put("CommissionDate", sf.format(cSheet.getCommissionDate()));		//委托日期
								jsonObj.put("Status", cSheet.getStatus());	//委托单状态
								
								JSONObject printRecord = new JSONObject();
								printRecord.put("Code", cSheet.getCode());	//委托单号
								printRecord.put("Pwd", cSheet.getPwd());	//委托单密码
								printRecord.put("CommissionDate", sf.format(cSheet.getCommissionDate()));	//委托日期
								printRecord.put("CustomerName", cSheet.getCustomerName());	//委托单位名称
								printRecord.put("CustomerTel", cSheet.getCustomerTel());	//委托单位电话
								printRecord.put("CustomerAddress", cSheet.getCustomerAddress());	//委托单位地址
								printRecord.put("CustomerZipCode", cSheet.getCustomerZipCode());	//委托单位邮政编码
								printRecord.put("SampleFrom", cSheet.getSampleFrom());	//证书单位名称
								printRecord.put("BillingTo", cSheet.getBillingTo());	//开票单位名称
								
								printRecord.put("ApplianceName", cSheet.getApplianceName());	//器具名称
								printRecord.put("ApplianceInfo",String.format("%s/%s/%s/%s", cSheet.getApplianceModel(),cSheet.getRange(),cSheet.getAccuracy(),cSheet.getManufacturer()));//器具信息
								printRecord.put("ApplianceNumber",String.format("%s[%s]", cSheet.getAppFactoryCode(),cSheet.getAppManageCode()));//器具编号
								printRecord.put("Quantity",cSheet.getQuantity().toString());//台件数
								printRecord.put("MandatoryInspection", CommissionSheetFlagUtil.getMandatoryByFlag(cSheet.getMandatory()));//强制检验
								printRecord.put("Appearance", cSheet.getAppearance());//外观附件
								printRecord.put("Repair", CommissionSheetFlagUtil.getRepairByFlag(cSheet.getRepair()));//需修理否
								printRecord.put("ReportType",CommissionSheetFlagUtil.getReportTypeByFlag(cSheet.getReportType()));//报告形式
								printRecord.put("OtherRequirements", cSheet.getOtherRequirements());//其他要求
								printRecord.put("Location", cSheet.getLocation());	//存放位置
								printRecord.put("Allotee", cSheet.getAllotee());	//派定人
								printRecord.put("ReceiverName", ((SysUser)req.getSession().getAttribute("LOGIN_USER")).getName());	//接收人姓名
								
								Address RecipientAddressObj = cSheet.getSampleAddress()==null?null:addrMgr.findById(cSheet.getSampleAddress()); //样品接收地点
								Address PickupAddressObj = cSheet.getReportAddress()==null?null:addrMgr.findById(cSheet.getReportAddress());	//取样、取报告地点
								printRecord.put("HeadName",cSheet.getHeadName());	//台头名称
								printRecord.put("RecipientAddressName", RecipientAddressObj==null?"":RecipientAddressObj.getAddress());	//接收地点
								printRecord.put("RecipientAddressTel", (RecipientAddressObj==null || RecipientAddressObj.getTel()==null)?"":RecipientAddressObj.getTel());
								printRecord.put("PickupAddressName", PickupAddressObj==null?"":PickupAddressObj.getAddress());	//取样地址
								printRecord.put("PickupAddressTel", (PickupAddressObj==null || PickupAddressObj.getTel()==null)?"":PickupAddressObj.getTel());
								
								jsonObj.put("PrintObj", printRecord);
								
								jsonArray.put(jsonObj);	
							}
						}
					
					retJSON8.put("total", totalSize8);
					retJSON8.put("rows", jsonArray);
				} catch (Exception e) {
					
					try {
						retJSON8.put("total", 0);
						retJSON8.put("rows", new JSONArray());
					} catch (JSONException e1) {
					}
					if(e.getClass() == java.lang.Exception.class){	//自定义的消息
						log.debug("exception in CommissionSheetServlet-->case 8", e);
					}else{
						log.error("error in CommissionSheetServlet-->case 8", e);
					}
				}finally{
					resp.setContentType("text/json;charset=utf-8");
					resp.getWriter().write(retJSON8.toString());
				}
				
				break;
			case 9:	//多条件查找委托单的信息（不带打印信息）:用于完工确认等
				JSONObject retJSON9 = new JSONObject();
				int totalSize9 = 0;
				try {
					JSONArray jsonArray = new JSONArray();
					int page = 0;	//当前页面
					if (req.getParameter("page") != null)
						page = Integer.parseInt(req.getParameter("page").toString());
					int rows = 10;	//页面大小
					if (req.getParameter("rows") != null)
						rows = Integer.parseInt(req.getParameter("rows").toString());
						String CustomerName  = req.getParameter("CustomerName");
					
						String ApplianceName = req.getParameter("ApplianceName");
						String BeginDate = req.getParameter("BeginDate");
						String EndDate = req.getParameter("EndDate");
						String Code = req.getParameter("Code");	//委托单号
						
						String CommissionStatus = req.getParameter("CommissionStatus");	//委托单状态：空字符串为全部；1为未完工；2为已完工确认的
						
						List<KeyValueWithOperator> condList = new ArrayList<KeyValueWithOperator>();
						condList.add(new KeyValueWithOperator("status", 10, "<>"));//选择委托单尚未注销的
						if(CustomerName != null && CustomerName.length() > 0){
							String cusName =  URLDecoder.decode(CustomerName.trim(), "UTF-8"); //解决jquery传递中文乱码问题
							condList.add(new KeyValueWithOperator("customerName",cusName,"="));
						}
						if(Code != null && Code.length() > 0){
							condList.add(new KeyValueWithOperator("code", "%"+Code+"%", "like"));
						}
						if(BeginDate != null && BeginDate.length() > 0){
							condList.add(new KeyValueWithOperator("commissionDate", new Timestamp(Date.valueOf(BeginDate).getTime()), ">="));
						}
						if(EndDate != null && EndDate.length() > 0){
							condList.add(new KeyValueWithOperator("commissionDate", new Timestamp(Date.valueOf(EndDate).getTime()), "<="));
						}
						if(ApplianceName != null && ApplianceName.trim().length() > 0 ){
							String appName = URLDecoder.decode(ApplianceName.trim(), "UTF-8");
							condList.add(new KeyValueWithOperator("applianceName", "%"+appName+"%", "like"));
						}
						if(CommissionStatus != null && CommissionStatus.length() > 0){
							if(CommissionStatus.equals("1")){
								condList.add(new KeyValueWithOperator("status", 3, "<"));//未完工
							}else if(CommissionStatus.equals("2")){
								condList.add(new KeyValueWithOperator("status", 3, ">="));//已完工确认
							}
						}
						totalSize9 = cSheetMgr.getTotalCount(condList);
						List<CommissionSheet> retList = cSheetMgr.findPagedAllBySort(page, rows, "commissionDate", false, condList);
						if(retList != null && retList.size() > 0){
							ApplianceSpeciesManager speciesMgr = new ApplianceSpeciesManager();
							ApplianceStandardNameManager standardNameMgr = new ApplianceStandardNameManager();
							SubContractManager subConMgr = new SubContractManager();	//转包记录Mgr
							SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
							
							String hqlQueryString_FinishQuantity = "select sum(model.quantity) from OriginalRecord as model where model.commissionSheet.id=? and model.status<>1 and model.taskAssign.status<>1 and model.verifyAndAuthorize.authorizeResult=? and model.verifyAndAuthorize.isAuthBgRuning is null ";	//签字通过的原始记录的器具总数(签字已通过且不是正在后台执行)
							String hqlQueryString_WithdrawQuantity = "select sum(model.number) from Withdraw as model where model.commissionSheet.id=? and model.executeResult=?";	//已批准的退样器具数量
							String hqlQueryString_SubContract = "select count(*) from SubContract as model where model.commissionSheet.id=? and model.status<>1 and model.receiveDate is not null";
							for(CommissionSheet cSheet : retList){
								JSONObject jsonObj = new JSONObject();
								jsonObj.put("Id", cSheet.getId());
								jsonObj.put("Code", cSheet.getCode());
								jsonObj.put("Pwd", cSheet.getPwd());
								jsonObj.put("CustomerName", cSheet.getCustomerName());
								jsonObj.put("ApplianceSpeciesId", cSheet.getApplianceSpeciesId());	//器具分类Id(或器具标准名称ID)
								if(cSheet.getSpeciesType()){	//器具授权（分类）名称
									jsonObj.put("SpeciesType", 1);	//器具分类类型
									ApplianceSpecies spe = speciesMgr.findById(cSheet.getApplianceSpeciesId());
									if(spe != null){
										jsonObj.put("ApplianceSpeciesName", spe.getName());
										jsonObj.put("ApplianceSpeciesNameStatus", spe.getStatus());
									}else{
										continue;
									}
								}else{	//器具标准名称
									jsonObj.put("SpeciesType", 0);
									ApplianceStandardName stName = standardNameMgr.findById(cSheet.getApplianceSpeciesId());
									if(stName != null){
										jsonObj.put("ApplianceSpeciesName", stName.getName());
										jsonObj.put("ApplianceSpeciesNameStatus", stName.getStatus());
									}else{
										continue;
									}
								}
								jsonObj.put("ApplianceName", cSheet.getApplianceName()==null?"":cSheet.getApplianceName());	//器具名称（常用名称）
								jsonObj.put("ApplianceCode", cSheet.getAppFactoryCode());	//出厂编号
								jsonObj.put("AppManageCode", cSheet.getAppManageCode());	//管理编号
								jsonObj.put("Model", cSheet.getApplianceName()==null?"":cSheet.getApplianceModel()==null?"":cSheet.getApplianceModel());	//型号规格
								jsonObj.put("Range", cSheet.getRange());		//测量范围
								jsonObj.put("Accuracy", cSheet.getAccuracy());	//精度等级
								jsonObj.put("Manufacturer", cSheet.getManufacturer()==null?"":cSheet.getManufacturer());	//制造厂商
								jsonObj.put("Quantity", cSheet.getQuantity()==null?"":cSheet.getQuantity());	//台/件数
								jsonObj.put("MandatoryInspection", cSheet.getMandatory()?1:0);	//强制检验
								jsonObj.put("Urgent", cSheet.getUrgent()?1:0);	//加急
								jsonObj.put("Trans", cSheet.getSubcontract()?1:0);	//转包
								if(!cSheet.getSubcontract()){	//0：转包
									List<SubContract> subRetList = subConMgr.findByVarProperty(new KeyValueWithOperator("commissionSheet.id", cSheet.getId(),"="), new KeyValueWithOperator("status", 0, "="));
									if(subRetList != null && subRetList.size() > 0){
										jsonObj.put("SubContractor", subRetList.get(0).getSubContractor().getName());	//转包方
									}else{
										jsonObj.put("SubContractor", "");	//转包方
									}
								}else{
									jsonObj.put("SubContractor", "");
								}
								jsonObj.put("Appearance", cSheet.getAppearance()==null?"":cSheet.getAppearance());	//外观附件
								jsonObj.put("Repair", cSheet.getRepair()?1:0);	//修理
								jsonObj.put("ReportType", cSheet.getReportType());	//报告形式
								jsonObj.put("OtherRequirements", cSheet.getOtherRequirements()==null?"":cSheet.getOtherRequirements());	//其它要求
								jsonObj.put("Location", cSheet.getLocation()==null?"":cSheet.getLocation());	//存放位置
								jsonObj.put("Allotee", cSheet.getAllotee()==null?"":cSheet.getAllotee());	//派定人
								jsonObj.put("CommissionDate", sf.format((cSheet.getCommissionType()==2 && cSheet.getLocaleCommissionDate()!=null)?cSheet.getLocaleCommissionDate():cSheet.getCommissionDate()));		//委托日期								
								jsonObj.put("Remark", cSheet.getRemark()==null?"":cSheet.getRemark());	//备注
								//费用信息
								jsonObj.put("TestFee", cSheet.getTestFee()==null?0:cSheet.getTestFee());
								jsonObj.put("RepairFee", cSheet.getRepairFee()==null?0:cSheet.getRepairFee());
								jsonObj.put("MaterialFee", cSheet.getMaterialFee()==null?0:cSheet.getMaterialFee());
								jsonObj.put("CarFee", cSheet.getCarFee()==null?0:cSheet.getCarFee());
								jsonObj.put("DebugFee", cSheet.getDebugFee()==null?0:cSheet.getDebugFee());
								jsonObj.put("OtherFee", cSheet.getOtherFee()==null?0:cSheet.getOtherFee());
								jsonObj.put("TotalFee", cSheet.getTotalFee()==null?0:cSheet.getTotalFee());
								
								jsonObj.put("Status", cSheet.getStatus());
								jsonObj.put("CommissionType", cSheet.getCommissionType());	//委托形式
								jsonObj.put("CommissionTypeName", CommissionSheetFlagUtil.getCommissionTypeByFlag(cSheet.getCommissionType()));
								
								//查询完工器具数量和退样器具数量，以及是否转包
								List<Long> fQuantityList = cSheetMgr.findByHQL(hqlQueryString_FinishQuantity, cSheet.getId(), true);	//完工器具数量
								if(fQuantityList != null && fQuantityList.size() > 0 && fQuantityList.get(0) != null){
									jsonObj.put("FinishQuantity", fQuantityList.get(0));	//完工器具数量
								}else{
									jsonObj.put("FinishQuantity", 0);
								}
								List<Long> wQuantityList = cSheetMgr.findByHQL(hqlQueryString_WithdrawQuantity, cSheet.getId(), true);	//退样器具数量
								if(wQuantityList != null && wQuantityList.size() > 0 && wQuantityList.get(0) != null){
									jsonObj.put("EffectQuantity", cSheet.getQuantity() - ((Long)wQuantityList.get(0)).intValue());	//有效器具数量
								}else{
									jsonObj.put("EffectQuantity", cSheet.getQuantity());
								}
								int iSubContract = cSheetMgr.getTotalCountByHQL(hqlQueryString_SubContract, cSheet.getId());
								if(iSubContract > 0 || cSheet.getCommissionType() == 5){	//该委托单有转包(或该委托单为其他业务)，则完工确认时不需要判断‘完工器具数量是否大于等于有效器具数量’;
									jsonObj.put("IsSubContract", true);
								}else{
									jsonObj.put("IsSubContract", false);
								}
								//查询直接生成证书的证书数量
								int ZGCerCount=0;
								List<Long> ZGCerCountList = new OriginalRecordManager().findByHQL("select count(*) from OriginalRecord as model where model.commissionSheet.id=? and model.status<>1 and model.taskAssign.status<>1 " +
										" and model.originalRecordExcel.doc='' and model.certificate.pdf is not null ", cSheet.getId());
								if(ZGCerCountList.get(0) != null){
									ZGCerCount = ZGCerCountList.get(0).intValue();
								}
								jsonObj.put("ZGCerCount", ZGCerCount);
								
								jsonArray.put(jsonObj);	
							}
						}
					
					retJSON9.put("total", totalSize9);
					retJSON9.put("rows", jsonArray);
				} catch (Exception e) {
					
					try {
						retJSON9.put("total", 0);
						retJSON9.put("rows", new JSONArray());
					} catch (JSONException e1) {
					}
					if(e.getClass() == java.lang.Exception.class){	//自定义的消息
						log.debug("exception in CommissionSheetServlet-->case 9", e);
					}else{
						log.error("error in CommissionSheetServlet-->case 9", e);
					}
				}finally{
					resp.setContentType("text/json;charset=utf-8");
					resp.getWriter().write(retJSON9.toString());
				}
				break;
			case 10://仅根据委托单号查询委托单信息，用于业务查询等
				JSONObject retJSON10 = new JSONObject();
				try {
					String Code = req.getParameter("Code");	//委托单号
					
					if(Code == null || Code.trim().length() == 0){
						throw new Exception("委托单号为空！");
					}
					List<CommissionSheet> cSheetRetList = cSheetMgr.findByVarProperty(new KeyValueWithOperator("code", Code, "="));
					if(cSheetRetList != null && cSheetRetList.size() > 0){
						CommissionSheet cSheet = cSheetRetList.get(0);
						SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
						retJSON10.put("IsOK", true);
						
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("CommissionId", cSheet.getId());	//委托单ID
						jsonObj.put("CommissionCode", cSheet.getCode());
						jsonObj.put("CommissionPwd", cSheet.getPwd());
						jsonObj.put("CommissionStatus", cSheet.getStatus());	//委托单状态
						jsonObj.put("CommissionType", cSheet.getCommissionType());
						jsonObj.put("ReportType", cSheet.getReportType());
						jsonObj.put("CommissionDate", sf.format((cSheet.getCommissionType()==2 && cSheet.getLocaleCommissionDate()!=null)?cSheet.getLocaleCommissionDate():cSheet.getCommissionDate()));
						jsonObj.put("CustomerName", cSheet.getCustomerName());
						jsonObj.put("CustomerTel", cSheet.getCustomerTel());
						jsonObj.put("CustomerAddress", cSheet.getCustomerAddress());
						jsonObj.put("CustomerZipCode", cSheet.getCustomerZipCode());
						jsonObj.put("ContactPerson", cSheet.getCustomerContactor());
						jsonObj.put("ContactorTel", cSheet.getCustomerContactorTel());
						jsonObj.put("SampleFrom", cSheet.getSampleFrom());
						jsonObj.put("BillingTo", cSheet.getBillingTo());
						jsonObj.put("ApplianceName", cSheet.getApplianceName());	//器具名称
						jsonObj.put("Model", cSheet.getApplianceModel());
						jsonObj.put("ApplianceCode", cSheet.getAppFactoryCode());	//出厂编号
						jsonObj.put("ApplianceManageCode", cSheet.getAppManageCode());	//管理编号
						jsonObj.put("Manufacturer", cSheet.getManufacturer());
						jsonObj.put("Quantity", cSheet.getQuantity());
						jsonObj.put("Mandatory", cSheet.getMandatory()?1:0);	//强制检定
						jsonObj.put("Ness", cSheet.getUrgent()?1:0);			//是否加急
						jsonObj.put("Appearance", cSheet.getAppearance());	//外观附件
						jsonObj.put("OtherRequirements", cSheet.getOtherRequirements());	//其他要求
						jsonObj.put("Status", cSheet.getStatus());	//委托单状态
						jsonObj.put("Receiver", cSheet.getReceiverName());
						jsonObj.put("FinishStaff", cSheet.getFinishStaffId()==null?"":(new UserManager()).findById(cSheet.getFinishStaffId()).getName());
						jsonObj.put("FinishDate", cSheet.getFinishDate()==null?"":cSheet.getFinishDate());
						jsonObj.put("CustomerClassification", (new CustomerManager()).findById(cSheet.getCustomerId()).getClassification());
						jsonObj.put("LocaleCommissionCode", cSheet.getLocaleCommissionCode());
						jsonObj.put("LocaleCommissionDate", cSheet.getLocaleCommissionDate());
						jsonObj.put("CheckOutStaffId", cSheet.getCheckOutStaffId());
						jsonObj.put("CheckOutStaff", cSheet.getCheckOutStaffId()==null?"":(new UserManager()).findById(cSheet.getCheckOutStaffId()).getName());
						jsonObj.put("CheckOutTime", cSheet.getCheckOutDate()==null?"":cSheet.getCheckOutDate());
//						String detailqueryStr = "select model.sysUser.name,model.lastEditTime from DetailList as model,DetailListCom as model1 where model.status = ? and model.id = model1.detailList.id and model1.commissionSheet.id = ?";
//						
//						List<Object[]> detail = (new DetailListManager()).findByHQL(detailqueryStr, new Integer(2), cSheet.getId());
//						if(detail!=null&detail.size()>0){
//							Object[] temp = detail.get(0);
//							jsonObj.put("CheckOutStaff", temp[0].toString());
//							jsonObj.put("CheckOutTime", temp[1].toString());
//						}
//						else{
//							jsonObj.put("CheckOutStaff", "");
//							jsonObj.put("CheckOutTime", "");
//						}
						jsonObj.put("Remark", cSheet.getRemark());
						jsonObj.put("TotalFee", cSheet.getTotalFee());	//委托单总计费用
						JSONObject printRecord = new JSONObject();
						printRecord.put("Code", cSheet.getCode());	//委托单号
						printRecord.put("Pwd", cSheet.getPwd());	//委托单密码
						printRecord.put("CommissionDate", sf.format(cSheet.getCommissionDate()));	//委托日期
						printRecord.put("CustomerName", cSheet.getCustomerName());	//委托单位名称
						printRecord.put("CustomerTel", cSheet.getCustomerTel());	//委托单位电话
						printRecord.put("CustomerAddress", cSheet.getCustomerAddress());	//委托单位地址
						printRecord.put("CustomerZipCode", cSheet.getCustomerZipCode());	//委托单位邮政编码
						printRecord.put("SampleFrom", cSheet.getSampleFrom());	//证书单位名称
						printRecord.put("BillingTo", cSheet.getBillingTo());	//开票单位名称
						
						printRecord.put("ApplianceName", cSheet.getApplianceName());	//器具名称
						printRecord.put("ApplianceInfo",String.format("%s/%s/%s/%s", cSheet.getApplianceModel(),cSheet.getRange(),cSheet.getAccuracy(),cSheet.getManufacturer()));//器具信息
						printRecord.put("ApplianceNumber",String.format("%s[%s]", cSheet.getAppFactoryCode(),cSheet.getAppManageCode()));//器具编号
						printRecord.put("Quantity",cSheet.getQuantity().toString());//台件数
						printRecord.put("MandatoryInspection", CommissionSheetFlagUtil.getMandatoryByFlag(cSheet.getMandatory()));//强制检验
						printRecord.put("Appearance", cSheet.getAppearance());//外观附件
						printRecord.put("Repair", CommissionSheetFlagUtil.getRepairByFlag(cSheet.getRepair()));//需修理否
						printRecord.put("ReportType",CommissionSheetFlagUtil.getReportTypeByFlag(cSheet.getReportType()));//报告形式
						printRecord.put("OtherRequirements", cSheet.getOtherRequirements());//其他要求
						printRecord.put("Location", cSheet.getLocation());	//存放位置
						printRecord.put("Allotee", cSheet.getAllotee());	//派定人
						printRecord.put("ReceiverName", ((SysUser)req.getSession().getAttribute("LOGIN_USER")).getName());	//接收人姓名
						
						Address RecipientAddressObj = cSheet.getSampleAddress()==null?null:(new AddressManager()).findById(cSheet.getSampleAddress()); //样品接收地点
						Address PickupAddressObj = cSheet.getReportAddress()==null?null:(new AddressManager()).findById(cSheet.getReportAddress());	//取样、取报告地点
						printRecord.put("HeadName",cSheet.getHeadName());	//台头名称
						printRecord.put("RecipientAddressName", RecipientAddressObj==null?"":RecipientAddressObj.getAddress());	//接收地点
						printRecord.put("RecipientAddressTel", (RecipientAddressObj==null || RecipientAddressObj.getTel()==null)?"":RecipientAddressObj.getTel());
						printRecord.put("PickupAddressName", PickupAddressObj==null?"":PickupAddressObj.getAddress());	//取样地址
						printRecord.put("PickupAddressTel", (PickupAddressObj==null || PickupAddressObj.getTel()==null)?"":PickupAddressObj.getTel());
						
						retJSON10.put("PrintObj", printRecord);
						retJSON10.put("CommissionObj", jsonObj);
					}else{
						throw new Exception("委托单号错误！");
					}
				} catch (Exception e){
					
					try {
						retJSON10.put("IsOK", false);
						retJSON10.put("msg", String.format("查询委托单失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					if(e.getClass() == java.lang.Exception.class){	//自定义的消息
						log.debug("exception in CommissionSheetServlet-->case 10", e);
					}else{
						log.error("error in CommissionSheetServlet-->case 10", e);
					}
				}finally{
					resp.setContentType("text/html;charset=utf-8");
					resp.getWriter().write(retJSON10.toString());
				}
				break;
			case 11://修改委托单备注
				JSONObject retObj11 = new JSONObject();
				try{
					String CommissionId = req.getParameter("CommissionId");
					String Remark = req.getParameter("Remark");
					CommissionSheet cSheet = cSheetMgr.findById(Integer.valueOf(CommissionId));
					cSheet.setRemark(Remark);
					boolean res = cSheetMgr.save(cSheet);
					retObj11.put("IsOK", res);
					retObj11.put("msg", res?"修改成功！":"修改失败，请重新修改！");
				}catch(Exception e){
					try {
						retObj11.put("IsOK", false);
						retObj11.put("msg", String.format("修改委托单备注失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					if(e.getClass() == java.lang.Exception.class){	//自定义的消息
						log.debug("exception in CommissionSheetServlet-->case 11", e);
					}else{
						log.error("error in CommissionSheetServlet-->case 11", e);
					}
				}finally{
					resp.setContentType(ResponseContentType.Type_FormSubmit);
					resp.getWriter().write(retObj11.toString());
				}
				break;
			case 12://注销委托单
				JSONObject retObj12 = new JSONObject();
				Timestamp today = new Timestamp(System.currentTimeMillis());
				try{
					String CommissionId = req.getParameter("CommissionId");
					String Reason = req.getParameter("Reason");
					CommissionSheet cSheet = cSheetMgr.findById(Integer.valueOf(CommissionId));
					if(cSheet.getCheckOutDate() != null &&
							(cSheet.getStatus() == FlagUtil.CommissionSheetStatus.Status_YiJieZhang || cSheet.getStatus() == FlagUtil.CommissionSheetStatus.Status_YiJieShu))
					{
						throw new Exception("该委托单已结账，不可注销！");
					}
					if(cSheet.getStatus() == FlagUtil.CommissionSheetStatus.Status_YiZhuXiao)
					{
						throw new Exception("该委托单已注销，不可重复注销！");
					}
					cSheet.setStatus(FlagUtil.CommissionSheetStatus.Status_YiZhuXiao);
					cSheet.setCancelDate(today);
					cSheet.setCancelExecuterId(((SysUser)req.getSession().getAttribute("LOGIN_USER")).getId());
					ReasonManager rMgr = new ReasonManager();
					List<Reason> rList = rMgr.findByVarProperty(new KeyValueWithOperator("reason",Reason.trim(),"="), new KeyValueWithOperator("type", FlagUtil.ReasonType.Type_CancelCommissionSheet, "="));//查找注销原因
					if(rList.size() > 0){	//更新原因
						Reason reason = rList.get(0);
						reason.setCount(reason.getCount()+1);
						reason.setLastUse(today);
						rMgr.update(reason);
						cSheet.setCancelReason(reason.getId());	//注销原因
					}else{	//新建原因
						Reason reason = new Reason();
						reason.setCount(1);
						reason.setLastUse(today);
						reason.setReason(Reason.trim());
						reason.setStatus(0);
						reason.setType(FlagUtil.ReasonType.Type_CancelCommissionSheet);	//注销客户
						rMgr.save(reason);
						cSheet.setCancelReason(reason.getId());	//注销原因
					}
					boolean res1 = cSheetMgr.cancel(cSheet);
					retObj12.put("IsOK", res1);
					retObj12.put("msg", res1?"注销成功！":"注销失败，请重新注销！");
				}catch(Exception e){
					try {
						retObj12.put("IsOK", false);
						retObj12.put("msg", String.format("注销委托单失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					if(e.getClass() == java.lang.Exception.class){	//自定义的消息
						log.debug("exception in CommissionSheetServlet-->case 12", e);
					}else{
						log.error("error in CommissionSheetServlet-->case 12", e);
					}
				}finally{
					resp.setContentType(ResponseContentType.Type_FormSubmit);
					resp.getWriter().write(retObj12.toString());
				}
				break;
			case 13://根据委托单号执行结账挡回
				JSONObject retObj13 = new JSONObject();
				try{
					String CommissionId = req.getParameter("CommissionId");
					if(CommissionId == null || CommissionId.length() == 0){
						throw new Exception("参数未指定！");
					}
					CommissionSheet cSheet = cSheetMgr.findById(Integer.valueOf(CommissionId));
					if(cSheet == null){
						throw new Exception("找不到指定的委托单！");
					}
					if(cSheet.getCheckOutDate() == null){
						throw new Exception("该委托单尚未结账，不能执行结账挡回！");
					}
					SysUser loginUser = (SysUser)req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser);
					cSheetMgr.checkOutReject(cSheet, loginUser);
					retObj13.put("IsOK", true);
					retObj13.put("msg", "挡回成功！");
				}catch(Exception e){
					try {
						retObj13.put("IsOK", false);
						retObj13.put("msg", String.format("挡回失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					if(e.getClass() == java.lang.Exception.class){	//自定义的消息
						log.debug("exception in CommissionSheetServlet-->case 13", e);
					}else{
						log.error("error in CommissionSheetServlet-->case 13", e);
					}
				}finally{
					resp.setContentType("text/html;charset=utf-8");
					resp.getWriter().write(retObj13.toString());
				}
				break;
			case 14://根据委托单号将委托单从“注销”挡回至“收件”状态
				JSONObject retObj14 = new JSONObject();
				try{
					String CommissionId = req.getParameter("CommissionId");
					if(CommissionId == null || CommissionId.length() == 0){
						throw new Exception("参数未指定！");
					}
					CommissionSheet cSheet = cSheetMgr.findById(Integer.valueOf(CommissionId));
					if(cSheet == null){
						throw new Exception("找不到指定的委托单！");
					}
					if(cSheet.getCancelDate() == null){
						throw new Exception("该委托单尚未注销，不能执行注销挡回！");
					}
					if(cSheet.getCommissionType()==2&&cSheet.getLocaleApplianceItemId()!=null){
						List<KeyValueWithOperator> keys = new ArrayList<KeyValueWithOperator>();
						keys.add(new KeyValueWithOperator("localeApplianceItemId", cSheet.getLocaleApplianceItemId(), "="));
						keys.add(new KeyValueWithOperator("status", new Integer(10), "<>"));
						List<CommissionSheet> check = cSheetMgr.findByVarProperty(keys);
						if(check!=null&&check.size()>0){
							throw new Exception("已存在相同检测条目且未注销的现场检测委托单，不能执行注销挡回！");
						}
					}
					cSheet.setFinishLocation(null);
					cSheet.setFinishStaffId(null);
					cSheet.setFinishDate(null);
					cSheet.setCancelDate(null);
					cSheet.setCancelExecuterId(null);
					cSheet.setCancelReason(null);
					cSheet.setCancelRequesterId(null);
					cSheet.setStatus(FlagUtil.CommissionSheetStatus.Status_YiShouJian);
					cSheetMgr.update(cSheet);
					retObj14.put("IsOK", true);
					retObj14.put("msg", "挡回成功！");
				}catch(Exception e){
					try {
						retObj14.put("IsOK", false);
						retObj14.put("msg", String.format("挡回失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					if(e.getClass() == java.lang.Exception.class){	//自定义的消息
						log.debug("exception in CommissionSheetServlet-->case 13", e);
					}else{
						log.error("error in CommissionSheetServlet-->case 13", e);
					}
				}finally{
					resp.setContentType("text/html;charset=utf-8");
					resp.getWriter().write(retObj14.toString());
				}
				break;
			case 15://根据委托单号、委托单位等查询委托单信息(打印)
				JSONObject retJSON15 = new JSONObject();
				try {
					String Code = req.getParameter("Code");	//委托单号
					String CustomerId = req.getParameter("CustomerId");//委托单位
					String CustomerName = req.getParameter("CustomerName");//委托单位名称
					String DateFrom = req.getParameter("DateFrom");
					String DateEnd = req.getParameter("DateEnd");
					String Status = req.getParameter("Status");
					int page = 1;
					if (req.getParameter("page") != null)
						page = Integer.parseInt(req.getParameter("page").toString());
					int rows = 10;
					if (req.getParameter("rows") != null)
						rows = Integer.parseInt(req.getParameter("rows").toString());
					
					List<KeyValueWithOperator> keys = new ArrayList<KeyValueWithOperator>();
					if(Code != null && Code.length() > 0){
						keys.add(new KeyValueWithOperator("code", "%"+Code+"%", "like"));
					}
					if(CustomerId!=null && CustomerId.length() > 0){
						keys.add(new KeyValueWithOperator("customerId", Integer.valueOf(CustomerId), "="));
					}
					if(CustomerName!=null && CustomerName.length() > 0){
						String CustomerNameStr = URLDecoder.decode(CustomerName, "utf-8");
						keys.add(new KeyValueWithOperator("customerName", "%"+CustomerNameStr+"%", "like"));
					}
					if(DateFrom != null && DateFrom.length() > 0){
						Timestamp Start = new Timestamp(Date.valueOf(URLDecoder.decode(DateFrom, "utf-8")).getTime());
						keys.add(new KeyValueWithOperator("commissionDate", Start, ">="));
					}
					if(DateEnd != null && DateEnd.length() > 0){
						Timestamp End = new Timestamp(Date.valueOf(URLDecoder.decode(DateEnd, "utf-8")).getTime());
						keys.add(new KeyValueWithOperator("commissionDate", End, "<="));
					}
					if(Status != null && Status.length() > 0 ){
						keys.add(new KeyValueWithOperator("status", Integer.valueOf(Status), "="));
					}
					
					List<CommissionSheet> cSheetRetList = cSheetMgr.findByPropertyBySort("commissionDate", false, keys);
					int total = cSheetMgr.getTotalCount(keys);
					JSONArray options = new JSONArray();
					CertificateFeeAssignManager feeMgr=new CertificateFeeAssignManager();
					if(cSheetRetList != null && cSheetRetList.size() > 0){
						for(CommissionSheet cSheet : cSheetRetList)
						{
							JSONObject jsonObj = new JSONObject();
							jsonObj.put("Id", cSheet.getId());
							jsonObj.put("DetailListCode", cSheet.getDetailListCode());
							jsonObj.put("Code", cSheet.getCode());
							jsonObj.put("Pwd", cSheet.getPwd());
							jsonObj.put("CustomerId", cSheet.getCustomerId());
							jsonObj.put("CustomerName", cSheet.getCustomerName());
							jsonObj.put("ApplianceSpeciesId", cSheet.getApplianceSpeciesId());	//器具分类Id(或器具标准名称ID)
							if(cSheet.getSpeciesType()){	//器具授权（分类）名称
								jsonObj.put("SpeciesType", 1);	//器具分类类型
								ApplianceSpecies spe = (new ApplianceSpeciesManager()).findById(cSheet.getApplianceSpeciesId());
								if(spe != null){
									jsonObj.put("ApplianceSpeciesName", spe.getName());
									jsonObj.put("ApplianceSpeciesNameStatus", spe.getStatus());
								}else{
									continue;
								}
							}else{	//器具标准名称
								jsonObj.put("SpeciesType", 0);
								ApplianceStandardName stName = (new ApplianceStandardNameManager()).findById(cSheet.getApplianceSpeciesId());
								if(stName != null){
									jsonObj.put("ApplianceSpeciesName", stName.getName());
									jsonObj.put("ApplianceSpeciesNameStatus", stName.getStatus());
								}else{
									continue;
								}
							}
							jsonObj.put("ApplianceName", cSheet.getApplianceName()==null?"":cSheet.getApplianceName());	//器具名称（常用名称）
							jsonObj.put("ApplianceCode", cSheet.getAppFactoryCode());	//出厂编号
							jsonObj.put("AppManageCode", cSheet.getAppManageCode());	//管理编号
							jsonObj.put("Model", cSheet.getApplianceName()==null?"":cSheet.getApplianceModel()==null?"":cSheet.getApplianceModel());	//型号规格
							jsonObj.put("Range", cSheet.getRange());		//测量范围
							jsonObj.put("Accuracy", cSheet.getAccuracy());	//精度等级
							jsonObj.put("Manufacturer", cSheet.getManufacturer()==null?"":cSheet.getManufacturer());	//制造厂商
							jsonObj.put("Quantity", cSheet.getQuantity()==null?"":cSheet.getQuantity());	//台/件数
							jsonObj.put("MandatoryInspection", cSheet.getMandatory()?"非强制检定":"强制检定");	//强制检验
							jsonObj.put("Urgent", cSheet.getUrgent()?1:0);	//加急
							jsonObj.put("Trans", cSheet.getSubcontract()?1:0);	//转包
							if(!cSheet.getSubcontract()){	//0：转包
								List<SubContract> subRetList = (new SubContractManager()).findByVarProperty(new KeyValueWithOperator("commissionSheet.id", cSheet.getId(),"="), new KeyValueWithOperator("status", 0, "="));
								if(subRetList != null && subRetList.size() > 0){
									jsonObj.put("SubContractor", subRetList.get(0).getSubContractor().getName());	//转包方
								}else{
									jsonObj.put("SubContractor", "");	//转包方
								}
							}else{
								jsonObj.put("SubContractor", "");
							}
							jsonObj.put("Appearance", cSheet.getAppearance()==null?"":cSheet.getAppearance());	//外观附件
							jsonObj.put("Repair", cSheet.getRepair()?1:0);	//修理
							
							jsonObj.put("ReportType", CommissionSheetFlagUtil.getReportTypeByFlag(cSheet.getReportType()));	//报告形式
							
							jsonObj.put("OtherRequirements", cSheet.getOtherRequirements()==null?"":cSheet.getOtherRequirements());	//其它要求
							jsonObj.put("Location", cSheet.getLocation()==null?"":cSheet.getLocation());	//存放位置
							jsonObj.put("Allotee", cSheet.getAllotee()==null?"":cSheet.getAllotee());	//派定人
							SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
							jsonObj.put("CommissionDate", sf.format(cSheet.getCommissionDate()));		//委托日期
							String status="";
							if(cSheet.getStatus()==0){
								status="已收件";
							}else if(cSheet.getStatus()==1){
								status="已分配";
							}else if(cSheet.getStatus()==2){
								status="转包中";
							}else if(cSheet.getStatus()==3){
								status="已完工";
							}else if(cSheet.getStatus()==4){
								status="已结账";
							}else if(cSheet.getStatus()==9){
								status="已结束";
							}else if(cSheet.getStatus()==10){
								status="已注销";
							}
							jsonObj.put("Status",status);	//委托单状态
							
							jsonObj.put("Attachment", cSheet.getAttachment());
							
							/***********在CertificateFeeAssign(原始记录证书费用分配)中查找委托单的费用详情***********/
							List<Object[]> FList=feeMgr.findByHQL(CertificateFeeAssignManager.queryStringAllAllFeeByCommissionSheetId, cSheet.getId());		
																	
							if(FList.isEmpty()){
						    	jsonObj.put("TestFee", 0.0);
								jsonObj.put("RepairFee", 0.0);
								jsonObj.put("MaterialFee", 0.0);
								jsonObj.put("CarFee", 0.0);
								jsonObj.put("DebugFee", 0.0);
								jsonObj.put("OtherFee", 0.0);
								jsonObj.put("TotalFee", 0.0);
								jsonObj.put("OldTestFee", 0.0);
								jsonObj.put("OldRepairFee", 0.0);
								jsonObj.put("OldMaterialFee", 0.0);
								jsonObj.put("OldCarFee", 0.0);
								jsonObj.put("OldDebugFee", 0.0);
								jsonObj.put("OldOtherFee", 0.0);
								jsonObj.put("OldTotalFee", 0.0);
						    }else{
							    for(Object[] fee:FList){							    	
									jsonObj.put("TestFee", (Double)fee[0]==null?0.0:(Double)fee[0]);
									jsonObj.put("RepairFee", (Double)fee[1]==null?0.0:(Double)fee[1]);
									jsonObj.put("MaterialFee", (Double)fee[2]==null?0.0:(Double)fee[2]);
									jsonObj.put("CarFee", (Double)fee[3]==null?0.0:(Double)fee[3]);
									jsonObj.put("DebugFee", (Double)fee[4]==null?0.0:(Double)fee[4]);
									jsonObj.put("OtherFee", (Double)fee[5]==null?0.0:(Double)fee[5]);
									jsonObj.put("TotalFee", (Double)fee[6]==null?0.0:(Double)fee[6]);
									jsonObj.put("OldTestFee", (Double)fee[7]==null?0.0:(Double)fee[7]);
									jsonObj.put("OldRepairFee", (Double)fee[8]==null?0.0:(Double)fee[8]);
									jsonObj.put("OldMaterialFee", (Double)fee[9]==null?0.0:(Double)fee[9]);
									jsonObj.put("OldCarFee", (Double)fee[10]==null?0.0:(Double)fee[10]);
									jsonObj.put("OldDebugFee", (Double)fee[11]==null?0.0:(Double)fee[11]);
									jsonObj.put("OldOtherFee", (Double)fee[12]==null?0.0:(Double)fee[12]);
									jsonObj.put("OldTotalFee", (Double)fee[13]==null?0.0:(Double)fee[13]);
							   }
						    }
							
							options.put(jsonObj);
						}
					}
					retJSON15.put("total", total);
					retJSON15.put("rows", options);
					
					
					retJSON15.put("IsOK", true);
					req.getSession().setAttribute("MissionLookList", retJSON15);
					
					resp.sendRedirect("/jlyw/StatisticLook/MissionLookPrint.jsp");
				}catch(Exception e){
					
					try{
						retJSON15.put("total", 0);
						retJSON15.put("rows", new JSONArray());
						retJSON15.put("IsOK", false);
						req.getSession().setAttribute("MissionLookList", retJSON15);
						resp.sendRedirect("/jlyw/StatisticLook/MissionLookPrint.jsp");
					}catch(Exception e1){}
					if(e.getClass() == java.lang.Exception.class){	//自定义的消息
						log.debug("exception in CommissionSheetServlet-->case 15", e);
					}else{
						log.error("error in CommissionSheetServlet-->case 15", e);
					}
				}finally{
					
				}
				break;
			case 16: // 预留委托单
				try {
					String QuotationId = req.getParameter("QuotationId");	//报价单号
					String CommissionDate = req.getParameter("CommissionDate");		//委托日期
					String PromiseDate = req.getParameter("PromiseDate").trim();			//承诺日期
					String CommissionType = req.getParameter("CommissionType");		//委托形式
					String CustomerName  = req.getParameter("CustomerName").trim();		//委托单位
					String CustomerTel = req.getParameter("CustomerTel").trim();
					String CustomerAddress = req.getParameter("CustomerAddress").trim();
					String CustomerZipCode = req.getParameter("CustomerZipCode").trim();
					String ContactPerson = req.getParameter("ContactPerson").trim();
					String ContactorTel = req.getParameter("ContactorTel").trim();
					String SampleFrom = req.getParameter("SampleFrom");	//证书单位
					String BillingTo = req.getParameter("BillingTo");	//开票单位
					
					String CustomerHandler = req.getParameter("CustomerHandler");	//委托人
//					String ReceiverName = req.getParameter("ReceiverName").trim();	//接收人
					
					String HeadNameId = req.getParameter("HeadName").trim();	//台头名称ID
					String RecipientAddress = req.getParameter("RecipientAddress");	//送样地址
					String PickupAddress = req.getParameter("PickupAddress");		//取件地址					
					String Appliances = req.getParameter("Appliances").trim();	//检验的器具

					String LocaleCommissionCode = null;	//现场委托单号
					Timestamp LocaleCommissionDate = null;	//现场检测时间
					Integer LocaleStaffId = null;	//现场检测负责人ID
					
					if(QuotationId != null){
						QuotationId = QuotationId.trim();
					}else{
						QuotationId = "";
					}
					
					if((CommissionDate == null || CommissionDate.length() == 0) && !CommissionType.equals("2")){
						throw new Exception("委托日期为空！");
					}
					if(SampleFrom.length() == 0){
						SampleFrom = CustomerName;
					}
					if(BillingTo.length() == 0){
						BillingTo = CustomerName;
					}
					if(RecipientAddress == null || RecipientAddress.length() == 0){
						RecipientAddress = null;
					}
					if(PickupAddress == null || PickupAddress.length() == 0){
						PickupAddress = null;
					}
					
					Timestamp now = new Timestamp(System.currentTimeMillis());
					CustomerManager cusMgr = new CustomerManager();		//客户管理Mgr
					Integer CustomerId;
					List<Customer> cusList = cusMgr.findByVarProperty(new KeyValueWithOperator("name",CustomerName,"="), 
							new KeyValueWithOperator("status", 1, "<>"));	//查找委托单位的ID
					if(cusList != null && cusList.size() == 1){
						CustomerId = cusList.get(0).getId();
					}else if(cusList != null && cusList.size() > 1){
						throw new Exception("数据库中找到多个名称相同的委托单位:"+CustomerName+", 请到‘委托单位信息管理’进行修改！");
					}else{
						throw new Exception("委托单位不存在，新客户请先新建委托单位！");
					}
					
					if(QuotationId.length() > 0){
						if(new QuotationManager().findById(QuotationId) == null){
							throw new Exception(String.format("报价单号:%s 无效！", QuotationId));
						}
					}

					//begin-查询本委托单类型最大的委托单号
					String queryCode = String.format("%d%d", now.getYear()+1900,Integer.parseInt(CommissionType));		//查询委托单的样式：根据委托形式不同标志位不同
					String queryString = "select max(model.code) from CommissionSheet as model where model.code like ?";
					List<Object> retList = cSheetMgr.findByHQL(queryString, queryCode+"%");
					Integer codeBeginInt = Integer.parseInt("000001");	//委托单编号
					if(retList.size() > 0 && retList.get(0) != null){
						codeBeginInt = Integer.parseInt(retList.get(0).toString().substring(5)) + 1;
					}
					//end-查询本委托单类型最大的委托单号
					
					ApplianceSpeciesManager speciesMgr = new ApplianceSpeciesManager();	//器具分类管理Mgr
					ApplianceStandardNameManager sNameMgr = new ApplianceStandardNameManager();	//器具标准名称管理Mgr
					AppliancePopularNameManager popNameMgr = new AppliancePopularNameManager();	//器具常用名称管理Mgr
					SubContractorManager subConMgr = new SubContractorManager();	//转包方管理Mgr
					ApplianceManufacturerManager mafMgr = new ApplianceManufacturerManager();	//制造厂管理Mgr
					
					AddressManager addrMgr = new AddressManager();
					Address HeadNameAddr = new AddressManager().findById(Integer.parseInt(HeadNameId));	//台头名称的单位
					
					QualificationManager qualMgr = new QualificationManager();	//检测人员资质管理Mgr
					List<Integer> qualList = new ArrayList<Integer>();
					qualList.add(FlagUtil.QualificationType.Type_Jianding);
					qualList.add(FlagUtil.QualificationType.Type_Jianyan);
					qualList.add(FlagUtil.QualificationType.Type_Jiaozhun);
					
					List<CommissionSheet> comList = new ArrayList<CommissionSheet>();	//委托单列表
					
					List<SubContractor> subConList = new ArrayList<SubContractor>();	//委托单列表对应的转包方：如委托单没有转包方，则为null
					List<SysUser> alloteeList = new ArrayList<SysUser>();	//委托单列表对应的派定人：如委托单没有派定人，则为null
					Timestamp commissionDate = new Timestamp(Date.valueOf(CommissionDate).getTime());	//委托日期
					LocaleApplianceItemManager locAppItemMgr = new LocaleApplianceItemManager();	//现场检测器具条目的Mgr
					/********************   存委托单    ******************/
					for(int i=0;i<Integer.parseInt(req.getParameter("YLNumber"));i++){//增加与预留数目相等的委托单
						//JSONObject jsonObj = appliancesArray.getJSONObject(i);
						//idList.add(jsonObj.getInt("Id"));		//前端页面表格记录的ID
						
						CommissionSheet comSheet = new CommissionSheet();
						comSheet.setCommissionDate(commissionDate);	//委托日期
						if(PromiseDate.length() > 0){
							comSheet.setPromiseDate(Date.valueOf(PromiseDate));	//承诺日期
						}
						if(QuotationId.length() == 0){			//报价单号
							comSheet.setQuotationId(null);
						}else{
							comSheet.setQuotationId(QuotationId);
						}
						
						comSheet.setCommissionType(Integer.parseInt(CommissionType));//委托形式
						comSheet.setCustomerId(CustomerId);	//委托单位ID
						comSheet.setCustomerName(CustomerName);
						comSheet.setCustomerTel(CustomerTel);
						comSheet.setCustomerAddress(CustomerAddress);
						comSheet.setCustomerZipCode(CustomerZipCode);
						comSheet.setCustomerContactor(ContactPerson);
						comSheet.setCustomerContactorTel(ContactorTel);
						comSheet.setSampleFrom(SampleFrom);
						comSheet.setBillingTo(BillingTo);
						comSheet.setCustomerHandler(CustomerHandler);	//委托人
						comSheet.setReceiverId(((SysUser)req.getSession().getAttribute("LOGIN_USER")).getId());	//接收人ID
						comSheet.setReceiverName(((SysUser)req.getSession().getAttribute("LOGIN_USER")).getName());	//接收人姓名
						
						comSheet.setHeadNameId(HeadNameAddr.getId());	//台头名称ID
						comSheet.setHeadName(HeadNameAddr.getHeadName());	//台头名称
						comSheet.setHeadNameEn(HeadNameAddr.getHeadNameEn()==null?"":HeadNameAddr.getHeadNameEn());	//台头名称英文
						comSheet.setSampleAddress(RecipientAddress==null?null:Integer.parseInt(RecipientAddress));//样品接收地点
						comSheet.setReportAddress(PickupAddress==null?null:Integer.parseInt(PickupAddress));	//取样、取报告地点
						
						comSheet.setLocaleCommissionCode(LocaleCommissionCode);	//现场委托书号
						comSheet.setLocaleCommissionDate(LocaleCommissionDate);	//现场检测时间
						comSheet.setLocaleStaffId(LocaleStaffId);	//现场负责人ID
						
						//委托单其他信息（必填信息）
						comSheet.setCode(queryCode+String.format("%06d", codeBeginInt++));	//委托单编号
						int pwd =  (int) (Math.random()*9000+1000);	//密码：四位随机数（1000~9999）
						comSheet.setPwd(new Integer(pwd).toString());
						comSheet.setCreatorId(((SysUser)req.getSession().getAttribute("LOGIN_USER")).getId());	//委托单创建人ID
						comSheet.setCreateDate(now);	//委托单创建时间
						comSheet.setStatus(0);	//委托单状态：已收件
						comSheet.setAttachment(UIDUtil.get22BitUID());	//附件集名称
						
						
						/**********************   添加器具信息    ************************/
						String SpeciesType = req.getParameter("SpeciesType");	//器具分类类型
						String ApplianceSpeciesId = req.getParameter("ApplianceSpeciesId");	//器具类别ID/标准名称ID
						String ApplianceName = req.getParameter("ApplianceName").trim();	//器具名称
						String Manufacturer= req.getParameter("Manufacturer");	//制造厂
						
						if(Integer.parseInt(SpeciesType) == 0){	//0:标准名称；1：分类名称
							comSheet.setSpeciesType(false);	
							String stdName = sNameMgr.findById(Integer.parseInt(ApplianceSpeciesId)).getName();
							if(ApplianceName == null || ApplianceName.trim().length() == 0){
								ApplianceName = stdName;	//器具名称未填写，则默认为标准名称或分类名称
							}else{	//如果已填写，判断是否等于标准名称，如果不等于标准名称，则存入常用名称表中
								if(!stdName.equalsIgnoreCase(ApplianceName.trim())){
									List<AppliancePopularName> popRetList = popNameMgr.findByVarProperty(new KeyValueWithOperator("applianceStandardName.id", Integer.parseInt(ApplianceSpeciesId), "="), new KeyValueWithOperator("popularName", ApplianceName.trim(), "="));
									if(popRetList != null && popRetList.size() == 0){
										ApplianceStandardName sNameTemp = new ApplianceStandardName();
										sNameTemp.setId(Integer.parseInt(ApplianceSpeciesId));
										AppliancePopularName popNameTemp = new AppliancePopularName();
										popNameTemp.setApplianceStandardName(sNameTemp);
										popNameTemp.setPopularName(ApplianceName);
										popNameTemp.setBrief(LetterUtil.String2Alpha(ApplianceName.trim()));
										popNameTemp.setStatus(0);
										popNameMgr.save(popNameTemp);
									}
								}
							}
							
							//按需增加制造厂
							if(Manufacturer != null && Manufacturer.trim().length() > 0){
								int intRet = mafMgr.getTotalCount(new KeyValueWithOperator("applianceStandardName.id", Integer.parseInt(ApplianceSpeciesId), "="), new KeyValueWithOperator("manufacturer", Manufacturer.trim(), "="));
								if(intRet == 0){
									ApplianceStandardName sNameTemp = new ApplianceStandardName();
									sNameTemp.setId(Integer.parseInt(ApplianceSpeciesId));
									ApplianceManufacturer maf = new ApplianceManufacturer();
									maf.setApplianceStandardName(sNameTemp);
									maf.setManufacturer(Manufacturer.trim());
									maf.setBrief(LetterUtil.String2Alpha(Manufacturer.trim()));
									maf.setStatus(0);
									mafMgr.save(maf);
								}
							}
						}else{
							comSheet.setSpeciesType(true);	
							if(ApplianceName == null || ApplianceName.trim().length() == 0){
								ApplianceName = speciesMgr.findById(Integer.parseInt(ApplianceSpeciesId)).getName();;	//器具名称未填写，则默认为标准名称或分类名称
							}
						}
						comSheet.setApplianceSpeciesId(Integer.parseInt(ApplianceSpeciesId));
						
						comSheet.setApplianceName(ApplianceName);	//存器具名称
						comSheet.setAppFactoryCode(req.getParameter("ApplianceCode").trim());		//出厂编号
						comSheet.setAppManageCode(req.getParameter("AppManageCode").trim());		//管理编号
						comSheet.setApplianceModel(req.getParameter("Model"));		//型号规格
						comSheet.setRange(req.getParameter("Range"));		//测量范围
						comSheet.setAccuracy(req.getParameter("Accuracy"));	//精度等级
						comSheet.setManufacturer(req.getParameter("Manufacturer"));		//制造厂商
						comSheet.setQuantity(Integer.parseInt(req.getParameter("Quantity")));		//台件数
						
						comSheet.setMandatory(Integer.parseInt(req.getParameter("Mandatory"))==0?false:true);	//强制检验
						comSheet.setUrgent(req.getParameter("Ness")==null?true:false);		//是否加急
						comSheet.setSubcontract(req.getParameter("Trans")==null?true:false);		//是否转包（0：转包，1:不需要转包）
						
						String SubContractor = req.getParameter("SubContractor");
						if(req.getParameter("Trans")!=null && SubContractor!= null && SubContractor.trim().length() > 0){	//转包
							List<SubContractor> subConRetList = subConMgr.findByVarProperty(new KeyValueWithOperator("name",SubContractor.trim(),"="));
							if(subConRetList != null && subConRetList.size() > 0){
								subConList.add(subConRetList.get(0));
							}else{
								subConList.add(null);
							}
						}else{
							subConList.add(null);
						}
						comSheet.setAppearance(req.getParameter("Appearance"));//外观附件
						comSheet.setRepair(Integer.parseInt(req.getParameter("Repair"))==0?false:true);		//需修理否
						comSheet.setReportType(Integer.parseInt(req.getParameter("ReportType")));	//报告形式
						comSheet.setOtherRequirements(req.getParameter("OtherRequirements"));	//其他要求
						comSheet.setLocation(req.getParameter("Location"));		//存放位置						
						comSheet.setStatus(-1);//已预留			
						/**********************  判断派定人是否存在及有效，并加入到alloteeList   ****************************/
						String Allotee = req.getParameter("Allotee");
						if(Allotee != null && Allotee.trim().length() > 0){
							Allotee = Allotee.trim();
							comSheet.setAllotee(Allotee);		//派定人
							List<Object[]> qualRetList = qualMgr.getQualifyUsers(Allotee, comSheet.getApplianceSpeciesId(), comSheet.getSpeciesType()?1:0, qualList);
							if(qualRetList != null && qualRetList.size() > 0){
								boolean alloteeChecked = false;
								for(Object[] objArray : qualRetList){
									if(!qualMgr.checkUserQualify((Integer)objArray[0], comSheet.getApplianceSpeciesId(), comSheet.getSpeciesType()?1:0, FlagUtil.QualificationType.Type_Except)){	//没有该检验项目的检验排外属性
										alloteeChecked = true;
										SysUser tempUser = new SysUser();
										tempUser.setId((Integer)objArray[0]);
										tempUser.setName((String)objArray[1]);
										
										alloteeList.add(tempUser);
										comSheet.setStatus(1);	//设置委托单状态：已分配
										break;
									}
								}
								
								if(!alloteeChecked){
									throw new Exception(String.format("派定人 '%s' 不存在或没有资质检验项目：%s，请重新选择！", Allotee, comSheet.getApplianceName()));
								}
							}else{
								throw new Exception(String.format("派定人 '%s' 不存在或没有资质检验项目：%s，请重新选择！", Allotee, comSheet.getApplianceName()));
							}
						}else{
							comSheet.setAllotee(null);		//派定人
							alloteeList.add(null);
						}
			
						comList.add(comSheet);					
					}//委托单对象循环添加结束
					
				
					if(cSheetMgr.saveByBatchYL(comList,(SysUser)req.getSession().getAttribute("LOGIN_USER"),now)){
						try{
							/************  更新或新增委托单位联系人   *************/
							if(ContactPerson.length() > 0){
								CustomerContactorManager cusConMgr = new CustomerContactorManager();
								List<CustomerContactor> cusConList = cusConMgr.findByVarProperty(new KeyValueWithOperator("customerId", CustomerId, "="), new KeyValueWithOperator("name", ContactPerson, "="));
								if(cusConList != null){
									if(cusConList.size() > 0){
										CustomerContactor c = cusConList.get(0);
										if(ContactorTel.length() > 0){
											if(!ContactorTel.equalsIgnoreCase(c.getCellphone1()) && (c.getCellphone2() == null || c.getCellphone2().length() == 0)){
												c.setCellphone2(c.getCellphone1());
											}
											c.setCellphone1(ContactorTel);
										}
										c.setLastUse(now);
										c.setCount(c.getCount()+1);
										cusConMgr.update(c);
									}else{
										CustomerContactor c = new CustomerContactor();
										///////////////////////////////////////
										Customer a=new Customer();
										a.setId(CustomerId);
										c.setCustomer(a);
										//////////////////////////////////////
										//c.setCustomerId(CustomerId);
										c.setName(ContactPerson);
										c.setCellphone1(ContactorTel);
										c.setLastUse(now);
										c.setCount(1);
										cusConMgr.save(c);
									}
								}
							}
						}catch(Exception e){ }
						JSONObject retObj=new JSONObject();
						retObj.put("IsOK", true);
						JSONArray retArray = new JSONArray();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
//						UserManager userMgr = new UserManager();
//						String ReceiverName = ReceiverId.length()>0?userMgr.findById(Integer.parseInt(ReceiverId)).getName():"";//接收人姓名
						
						Address RecipientAddressObj = RecipientAddress==null?null:addrMgr.findById(Integer.parseInt(RecipientAddress)); //样品接收地点
						Address PickupAddressObj = PickupAddress==null?null:addrMgr.findById(Integer.parseInt(PickupAddress));	//取样、取报告地点
						UserManager uMgr = new UserManager();
						for(int i = 0; i < comList.size(); i++){
							CommissionSheet cSheet = comList.get(i);
							JSONObject record = new JSONObject();
							record.put("Id", i+1);
							record.put("CommissionNumber", cSheet.getCode());		//委托单号
							
							record.put("Code", cSheet.getCode());	//委托单号
							record.put("Pwd", cSheet.getPwd());	//委托单密码
						
							retArray.put(record);
						}
						retObj.put("CommissionSheetList", retArray);
    
						resp.setContentType("text/html;charset=utf-8");
						resp.getWriter().write(retObj.toString());
					}else{
						throw new Exception("保存委托单信息失败！");
					}
					
				} catch(NumberFormatException e){	//字符串转Integer错误
					
					JSONObject retObj=new JSONObject();
					try {
						retObj.put("IsOK", false);
						retObj.put("msg", String.format("处理失败！错误信息：数据输入不完整或格式错误！"));
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					if(e.getClass() == java.lang.NumberFormatException.class){	//自定义的消息
						log.debug("exception in CommissionSheetServlet-->case 16", e);
					}else{
						log.error("error in CommissionSheetServlet-->case 16", e);
					}
					resp.setContentType("text/html;charset=utf-8");
					resp.getWriter().write(retObj.toString());
				}catch (Exception e){
					
					JSONObject retObj=new JSONObject();
					try {
						retObj.put("IsOK", false);
						retObj.put("msg", String.format("处理失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					if(e.getClass() == java.lang.Exception.class){	//自定义的消息
						log.debug("exception in CommissionSheetServlet-->case 16", e);
					}else{
						log.error("error in CommissionSheetServlet-->case 16", e);
					}
					resp.setContentType("text/html;charset=utf-8");
					resp.getWriter().write(retObj.toString());
				}
				break;
			case 17: // 修改预留委托单
				try {
					String QuotationId = req.getParameter("QuotationId");	//报价单号
					String CommissionDate = req.getParameter("CommissionDate");		//委托日期
					String PromiseDate = req.getParameter("PromiseDate").trim();			//承诺日期
					String CommissionType = req.getParameter("CommissionType");		//委托形式
					String CustomerName  = req.getParameter("CustomerName").trim();		//委托单位
					String CustomerTel = req.getParameter("CustomerTel").trim();
					String CustomerAddress = req.getParameter("CustomerAddress").trim();
					String CustomerZipCode = req.getParameter("CustomerZipCode").trim();
					String ContactPerson = req.getParameter("ContactPerson").trim();
					String ContactorTel = req.getParameter("ContactorTel").trim();
					String SampleFrom = req.getParameter("SampleFrom");	//证书单位
					String BillingTo = req.getParameter("BillingTo");	//开票单位
					
					String CustomerHandler = req.getParameter("CustomerHandler");	//委托人
//					String ReceiverName = req.getParameter("ReceiverName").trim();	//接收人
					
					String HeadNameId = req.getParameter("HeadName").trim();	//台头名称ID
					String RecipientAddress = req.getParameter("RecipientAddress");	//送样地址
					String PickupAddress = req.getParameter("PickupAddress");		//取件地址					
					
					String comSheetCode = req.getParameter("comSheetCode").trim();	//检验的器具
					
					String LocaleCommissionCode = null;	//现场委托单号
					Timestamp LocaleCommissionDate = null;	//现场检测时间
					Integer LocaleStaffId = null;	//现场检测负责人ID
					
					if(QuotationId != null){
						QuotationId = QuotationId.trim();
					}else{
						QuotationId = "";
					}
					
					if((CommissionDate == null || CommissionDate.length() == 0) && !CommissionType.equals("2")){
						throw new Exception("委托日期为空！");
					}
					if(SampleFrom.length() == 0){
						SampleFrom = CustomerName;
					}
					if(BillingTo.length() == 0){
						BillingTo = CustomerName;
					}
					if(RecipientAddress == null || RecipientAddress.length() == 0){
						RecipientAddress = null;
					}
					if(PickupAddress == null || PickupAddress.length() == 0){
						PickupAddress = null;
					}
					
					Timestamp now = new Timestamp(System.currentTimeMillis());
					CustomerManager cusMgr = new CustomerManager();		//客户管理Mgr
					Integer CustomerId;
					List<Customer> cusList = cusMgr.findByVarProperty(new KeyValueWithOperator("name",CustomerName,"="), 
							new KeyValueWithOperator("status", 1, "<>"));	//查找委托单位的ID
					if(cusList != null && cusList.size() == 1){
						CustomerId = cusList.get(0).getId();
					}else if(cusList != null && cusList.size() > 1){
						throw new Exception("数据库中找到多个名称相同的委托单位:"+CustomerName+", 请到‘委托单位信息管理’进行修改！");
					}else{
						throw new Exception("委托单位不存在，新客户请先新建委托单位！");
					}
					
					if(QuotationId.length() > 0){
						if(new QuotationManager().findById(QuotationId) == null){
							throw new Exception(String.format("报价单号:%s 无效！", QuotationId));
						}
					}
					//begin-查询本委托单类型最大的委托单号
					String queryCode = String.format("%d%d", now.getYear()+1900,Integer.parseInt(CommissionType));		//查询委托单的样式：根据委托形式不同标志位不同
					String queryString = "select max(model.code) from CommissionSheet as model where model.code like ?";
					List<Object> retList = cSheetMgr.findByHQL(queryString, queryCode+"%");
					Integer codeBeginInt = Integer.parseInt("000001");	//委托单编号
					if(retList.size() > 0 && retList.get(0) != null){
						codeBeginInt = Integer.parseInt(retList.get(0).toString().substring(5)) + 1;
					}
					//end-查询本委托单类型最大的委托单号
					
					ApplianceSpeciesManager speciesMgr = new ApplianceSpeciesManager();	//器具分类管理Mgr
					ApplianceStandardNameManager sNameMgr = new ApplianceStandardNameManager();	//器具标准名称管理Mgr
					AppliancePopularNameManager popNameMgr = new AppliancePopularNameManager();	//器具常用名称管理Mgr
					SubContractorManager subConMgr = new SubContractorManager();	//转包方管理Mgr
					ApplianceManufacturerManager mafMgr = new ApplianceManufacturerManager();	//制造厂管理Mgr
					
					AddressManager addrMgr = new AddressManager();
					Address HeadNameAddr = new AddressManager().findById(Integer.parseInt(HeadNameId));	//台头名称的单位
					
					QualificationManager qualMgr = new QualificationManager();	//检测人员资质管理Mgr
					List<Integer> qualList = new ArrayList<Integer>();
					qualList.add(FlagUtil.QualificationType.Type_Jianding);
					qualList.add(FlagUtil.QualificationType.Type_Jianyan);
					qualList.add(FlagUtil.QualificationType.Type_Jiaozhun);
					
					List<CommissionSheet> comList = new ArrayList<CommissionSheet>();	//委托单列表
					
					List<SubContractor> subConList = new ArrayList<SubContractor>();	//委托单列表对应的转包方：如委托单没有转包方，则为null
					List<SysUser> alloteeList = new ArrayList<SysUser>();	//委托单列表对应的派定人：如委托单没有派定人，则为null
					Timestamp commissionDate = new Timestamp(Date.valueOf(CommissionDate).getTime());	//委托日期
					LocaleApplianceItemManager locAppItemMgr = new LocaleApplianceItemManager();	//现场检测器具条目的Mgr
					/********************   存委托单    ******************/
			
					CommissionSheet comSheet = cSheetMgr.findByVarProperty(new KeyValueWithOperator("code", comSheetCode, "=")).get(0);
					
					comSheet.setCommissionDate(commissionDate);	//委托日期
					if(PromiseDate.length() > 0){
						comSheet.setPromiseDate(Date.valueOf(PromiseDate));	//承诺日期
					}
					if(QuotationId.length() == 0){			//报价单号
						comSheet.setQuotationId(null);
					}else{
						comSheet.setQuotationId(QuotationId);
					}
					
					comSheet.setCommissionType(Integer.parseInt(CommissionType));//委托形式
					comSheet.setCustomerId(CustomerId);	//委托单位ID
					comSheet.setCustomerName(CustomerName);
					comSheet.setCustomerTel(CustomerTel);
					comSheet.setCustomerAddress(CustomerAddress);
					comSheet.setCustomerZipCode(CustomerZipCode);
					comSheet.setCustomerContactor(ContactPerson);
					comSheet.setCustomerContactorTel(ContactorTel);
					comSheet.setSampleFrom(SampleFrom);
					comSheet.setBillingTo(BillingTo);
					comSheet.setCustomerHandler(CustomerHandler);	//委托人
					comSheet.setReceiverId(((SysUser)req.getSession().getAttribute("LOGIN_USER")).getId());	//接收人ID
					comSheet.setReceiverName(((SysUser)req.getSession().getAttribute("LOGIN_USER")).getName());	//接收人姓名
					
					comSheet.setHeadNameId(HeadNameAddr.getId());	//台头名称ID
					comSheet.setHeadName(HeadNameAddr.getHeadName());	//台头名称
					comSheet.setHeadNameEn(HeadNameAddr.getHeadNameEn()==null?"":HeadNameAddr.getHeadNameEn());	//台头名称英文
					comSheet.setSampleAddress(RecipientAddress==null?null:Integer.parseInt(RecipientAddress));//样品接收地点
					comSheet.setReportAddress(PickupAddress==null?null:Integer.parseInt(PickupAddress));	//取样、取报告地点
					
					comSheet.setLocaleCommissionCode(LocaleCommissionCode);	//现场委托书号
					comSheet.setLocaleCommissionDate(LocaleCommissionDate);	//现场检测时间
					comSheet.setLocaleStaffId(LocaleStaffId);	//现场负责人ID
					
					//委托单其他信息（必填信息）				
					/**********************   添加器具信息    ************************/
					
					String SpeciesType = req.getParameter("SpeciesType");	//器具分类类型
					String ApplianceSpeciesId = req.getParameter("ApplianceSpeciesId");	//器具类别ID/标准名称ID
					String ApplianceName = req.getParameter("ApplianceName").trim();	//器具名称
					String Manufacturer= req.getParameter("Manufacturer");	//制造厂
					//System.out.println("SpeciesType:"+SpeciesType+"ApplianceSpeciesId:"+ApplianceSpeciesId);
					if(Integer.parseInt(SpeciesType) == 0){	//0:标准名称；1：分类名称
						comSheet.setSpeciesType(false);	
						String stdName = sNameMgr.findById(Integer.parseInt(ApplianceSpeciesId)).getName();
						if(ApplianceName == null || ApplianceName.trim().length() == 0){
							ApplianceName = stdName;	//器具名称未填写，则默认为标准名称或分类名称
						}else{	//如果已填写，判断是否等于标准名称，如果不等于标准名称，则存入常用名称表中
							if(!stdName.equalsIgnoreCase(ApplianceName.trim())){
								List<AppliancePopularName> popRetList = popNameMgr.findByVarProperty(new KeyValueWithOperator("applianceStandardName.id", Integer.parseInt(ApplianceSpeciesId), "="), new KeyValueWithOperator("popularName", ApplianceName.trim(), "="));
								if(popRetList != null && popRetList.size() == 0){
									ApplianceStandardName sNameTemp = new ApplianceStandardName();
									sNameTemp.setId(Integer.parseInt(ApplianceSpeciesId));
									AppliancePopularName popNameTemp = new AppliancePopularName();
									popNameTemp.setApplianceStandardName(sNameTemp);
									popNameTemp.setPopularName(ApplianceName);
									popNameTemp.setBrief(LetterUtil.String2Alpha(ApplianceName.trim()));
									popNameTemp.setStatus(0);
									popNameMgr.save(popNameTemp);
								}
							}
						}
						
						//按需增加制造厂
						if(Manufacturer != null && Manufacturer.trim().length() > 0){
							int intRet = mafMgr.getTotalCount(new KeyValueWithOperator("applianceStandardName.id", Integer.parseInt(ApplianceSpeciesId), "="), new KeyValueWithOperator("manufacturer", Manufacturer.trim(), "="));
							if(intRet == 0){
								ApplianceStandardName sNameTemp = new ApplianceStandardName();
								sNameTemp.setId(Integer.parseInt(ApplianceSpeciesId));
								ApplianceManufacturer maf = new ApplianceManufacturer();
								maf.setApplianceStandardName(sNameTemp);
								maf.setManufacturer(Manufacturer.trim());
								maf.setBrief(LetterUtil.String2Alpha(Manufacturer.trim()));
								maf.setStatus(0);
								mafMgr.save(maf);
							}
						}
					}else{
						comSheet.setSpeciesType(true);	
						if(ApplianceName == null || ApplianceName.trim().length() == 0){
							ApplianceName = speciesMgr.findById(Integer.parseInt(ApplianceSpeciesId)).getName();;	//器具名称未填写，则默认为标准名称或分类名称
						}
					}
					comSheet.setApplianceSpeciesId(Integer.parseInt(ApplianceSpeciesId));
					
					comSheet.setApplianceName(ApplianceName);	//存器具名称
					comSheet.setAppFactoryCode(req.getParameter("ApplianceCode").trim());		//出厂编号
					comSheet.setAppManageCode(req.getParameter("AppManageCode").trim());		//管理编号
					comSheet.setApplianceModel(req.getParameter("Model"));		//型号规格
					comSheet.setRange(req.getParameter("Range"));		//测量范围
					comSheet.setAccuracy(req.getParameter("Accuracy"));	//精度等级
					comSheet.setManufacturer(req.getParameter("Manufacturer"));		//制造厂商
					comSheet.setQuantity(Integer.parseInt(req.getParameter("Quantity")));		//台件数
					//System.out.println("Mandatory:"+req.getParameter("Mandatory"));
					comSheet.setMandatory(Integer.parseInt(req.getParameter("Mandatory").trim())==0?false:true);	//强制检验
					comSheet.setUrgent(req.getParameter("Ness")==null?true:false);		//是否加急
					comSheet.setSubcontract(req.getParameter("Trans")==null?true:false);		//是否转包（0：转包，1:不需要转包）
					
					String SubContractor = req.getParameter("SubContractor");
					if(req.getParameter("Trans")!=null && SubContractor!= null && SubContractor.trim().length() > 0){	//转包
						List<SubContractor> subConRetList = subConMgr.findByVarProperty(new KeyValueWithOperator("name",SubContractor.trim(),"="));
						if(subConRetList != null && subConRetList.size() > 0){
							subConList.add(subConRetList.get(0));
						}else{
							subConList.add(null);
						}
					}else{
						subConList.add(null);
					}
					comSheet.setAppearance(req.getParameter("Appearance"));//外观附件
					comSheet.setRepair(Integer.parseInt(req.getParameter("Repair"))==0?false:true);		//需修理否
					comSheet.setReportType(Integer.parseInt(req.getParameter("ReportType")));	//报告形式
					comSheet.setOtherRequirements(req.getParameter("OtherRequirements"));	//其他要求
					comSheet.setLocation(req.getParameter("Location"));		//存放位置						
					
					comSheet.setStatus(0);//已收件	
					
					/**********************  判断派定人是否存在及有效，并加入到alloteeList   ****************************/
					String Allotee = req.getParameter("Allotee");
					if(Allotee != null && Allotee.trim().length() > 0){
						Allotee = Allotee.trim();
						comSheet.setAllotee(Allotee);		//派定人
						List<Object[]> qualRetList = qualMgr.getQualifyUsers(Allotee, comSheet.getApplianceSpeciesId(), comSheet.getSpeciesType()?1:0, qualList);
						if(qualRetList != null && qualRetList.size() > 0){
							boolean alloteeChecked = false;
							for(Object[] objArray : qualRetList){
								if(!qualMgr.checkUserQualify((Integer)objArray[0], comSheet.getApplianceSpeciesId(), comSheet.getSpeciesType()?1:0, FlagUtil.QualificationType.Type_Except)){	//没有该检验项目的检验排外属性
									alloteeChecked = true;
									SysUser tempUser = new SysUser();
									tempUser.setId((Integer)objArray[0]);
									tempUser.setName((String)objArray[1]);
									
									alloteeList.add(tempUser);
									comSheet.setStatus(1);	//设置委托单状态：已分配
									break;
								}
							}
							
							if(!alloteeChecked){
								throw new Exception(String.format("派定人 '%s' 不存在或没有资质检验项目：%s，请重新选择！", Allotee, comSheet.getApplianceName()));
							}
						}else{
							throw new Exception(String.format("派定人 '%s' 不存在或没有资质检验项目：%s，请重新选择！", Allotee, comSheet.getApplianceName()));
						}
					}else{
						comSheet.setAllotee(null);		//派定人
						alloteeList.add(null);
					}
		
					comList.add(comSheet);					
					//委托单对象添加结束
			
					if(cSheetMgr.updateByBatch(comList,subConList,alloteeList,(SysUser)req.getSession().getAttribute("LOGIN_USER"),(SysUser)req.getSession().getAttribute("LOGIN_USER"),now)){
						try{
							/************  更新或新增委托单位联系人   *************/
							if(ContactPerson.length() > 0){
								CustomerContactorManager cusConMgr = new CustomerContactorManager();
								List<CustomerContactor> cusConList = cusConMgr.findByVarProperty(new KeyValueWithOperator("customerId", CustomerId, "="), new KeyValueWithOperator("name", ContactPerson, "="));
								if(cusConList != null){
									if(cusConList.size() > 0){
										CustomerContactor c = cusConList.get(0);
										if(ContactorTel.length() > 0){
											if(!ContactorTel.equalsIgnoreCase(c.getCellphone1()) && (c.getCellphone2() == null || c.getCellphone2().length() == 0)){
												c.setCellphone2(c.getCellphone1());
											}
											c.setCellphone1(ContactorTel);
										}
										c.setLastUse(now);
										c.setCount(c.getCount()+1);
										cusConMgr.update(c);
									}else{
										CustomerContactor c = new CustomerContactor();
										///////////////////////////////////////
										Customer a=new Customer();
										a.setId(CustomerId);
										c.setCustomer(a);
										//////////////////////////////////////
										//c.setCustomerId(CustomerId);
										c.setName(ContactPerson);
										c.setCellphone1(ContactorTel);
										c.setLastUse(now);
										c.setCount(1);
										cusConMgr.save(c);
									}
								}
							}
						}catch(Exception e){ }
						JSONObject retObj=new JSONObject();
						retObj.put("IsOK", true);
						JSONArray retArray = new JSONArray();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
//						UserManager userMgr = new UserManager();
//						String ReceiverName = ReceiverId.length()>0?userMgr.findById(Integer.parseInt(ReceiverId)).getName():"";//接收人姓名
						
						Address RecipientAddressObj = RecipientAddress==null?null:addrMgr.findById(Integer.parseInt(RecipientAddress)); //样品接收地点
						Address PickupAddressObj = PickupAddress==null?null:addrMgr.findById(Integer.parseInt(PickupAddress));	//取样、取报告地点
						UserManager uMgr = new UserManager();
					
						
						JSONObject record = new JSONObject();
						record.put("Id", 1);
						record.put("CommissionNumber", comSheet.getCode());		//委托单号
						JSONObject printRecord = new JSONObject();
						printRecord.put("Code", comSheet.getCode());	//委托单号
						printRecord.put("Pwd", comSheet.getPwd());	//委托单密码
						printRecord.put("CommissionDate", sdf.format(comSheet.getCommissionDate()));	//委托日期
						printRecord.put("CustomerName", comSheet.getCustomerName());	//委托单位名称
						printRecord.put("CustomerTel", comSheet.getCustomerTel());	//委托单位电话
						printRecord.put("CustomerAddress", comSheet.getCustomerAddress());	//委托单位地址
						printRecord.put("CustomerZipCode", comSheet.getCustomerZipCode());	//委托单位邮政编码
						printRecord.put("SampleFrom", comSheet.getSampleFrom());	//证书单位名称
						printRecord.put("BillingTo", comSheet.getBillingTo());	//开票单位名称
						
						printRecord.put("ApplianceName", comSheet.getApplianceName());	//器具名称
						printRecord.put("ApplianceInfo",String.format("%s/%s/%s/%s", comSheet.getApplianceModel(),comSheet.getRange(),comSheet.getAccuracy(),comSheet.getManufacturer()));//器具信息
						printRecord.put("ApplianceNumber",String.format("%s[%s]", comSheet.getAppFactoryCode(),comSheet.getAppManageCode()));//器具编号
						printRecord.put("Quantity",comSheet.getQuantity().toString());//台件数
						printRecord.put("MandatoryInspection", CommissionSheetFlagUtil.getMandatoryByFlag(comSheet.getMandatory()));//强制检验
						printRecord.put("Appearance", comSheet.getAppearance());//外观附件
						printRecord.put("Repair", CommissionSheetFlagUtil.getRepairByFlag(comSheet.getRepair()));//需修理否
						printRecord.put("ReportType",CommissionSheetFlagUtil.getReportTypeByFlag(comSheet.getReportType()));//报告形式
						printRecord.put("OtherRequirements", comSheet.getOtherRequirements());//其他要求
						printRecord.put("Location", comSheet.getLocation());	//存放位置
						printRecord.put("Allotee", comSheet.getAllotee());	//派定人
						printRecord.put("ReceiverName", ((SysUser)req.getSession().getAttribute("LOGIN_USER")).getName());	//接收人姓名
						
						printRecord.put("HeadName",HeadNameAddr.getHeadName());	//台头名称
						printRecord.put("RecipientAddressName", RecipientAddressObj==null?"":RecipientAddressObj.getAddress());	//送样地址
						printRecord.put("RecipientAddressTel", (RecipientAddressObj==null || RecipientAddressObj.getTel()==null)?"":RecipientAddressObj.getTel());
						printRecord.put("PickupAddressName", PickupAddressObj==null?"":PickupAddressObj.getAddress());	//取样地址
						printRecord.put("PickupAddressTel", (PickupAddressObj==null || PickupAddressObj.getTel()==null)?"":PickupAddressObj.getTel());
						
						SysUser allotee = alloteeList.get(0);
						if(allotee != null){
							 SysUser tempUser = uMgr.findById(allotee.getId());
							printRecord.put("AlloteeJobNum",tempUser.getJobNum());	//员工工号
						}else{
							printRecord.put("AlloteeJobNum","");	//员工工号
						}
						
						//record.put("PrintObj", printRecord);
						//retArray.put(record);
						
						//retObj.put("CommissionSheetList", retArray);
						retObj.put("PrintObj", printRecord);
    
						resp.setContentType("text/html;charset=utf-8");
						resp.getWriter().write(retObj.toString());
					}else{
						throw new Exception("保存委托单信息失败！");
					}
					
				} catch(NumberFormatException e){	//字符串转Integer错误
					
					JSONObject retObj=new JSONObject();
					try {
						retObj.put("IsOK", false);
						retObj.put("msg", String.format("处理失败！错误信息：数据输入不完整或格式错误！"));
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					if(e.getClass() == java.lang.NumberFormatException.class){	//自定义的消息
						log.debug("exception in CommissionSheetServlet-->case 17", e);
					}else{
						log.error("error in CommissionSheetServlet-->case 17", e);
					}
					resp.setContentType("text/html;charset=utf-8");
					resp.getWriter().write(retObj.toString());
				}catch (Exception e){
					
					JSONObject retObj=new JSONObject();
					try {
						retObj.put("IsOK", false);
						retObj.put("msg", String.format("处理失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					if(e.getClass() == java.lang.Exception.class){	//自定义的消息
						log.debug("exception in CommissionSheetServlet-->case 17", e);
					}else{
						log.error("error in CommissionSheetServlet-->case 17", e);
					}
					resp.setContentType("text/html;charset=utf-8");
					resp.getWriter().write(retObj.toString());
				}
				break;
			case 18://查询未使用的预留委托单号
				JSONObject retJSON18 = new JSONObject();
				try{
					
					String DateFrom = req.getParameter("DateFrom");
					String DateEnd = req.getParameter("DateEnd");
					
					int page = 1;
					if (req.getParameter("page") != null)
						page = Integer.parseInt(req.getParameter("page").toString());
					int rows = 10;
					if (req.getParameter("rows") != null)
						rows = Integer.parseInt(req.getParameter("rows").toString());
					
					List<KeyValueWithOperator> keys = new ArrayList<KeyValueWithOperator>();
					
					if(DateFrom != null && DateFrom.length() > 0){
						Timestamp Start = new Timestamp(Date.valueOf(URLDecoder.decode(DateFrom, "utf-8")).getTime());
						keys.add(new KeyValueWithOperator("commissionDate", Start, ">="));
					}
					if(DateEnd != null && DateEnd.length() > 0){
						Timestamp End = new Timestamp(Date.valueOf(URLDecoder.decode(DateEnd, "utf-8")).getTime());
						keys.add(new KeyValueWithOperator("commissionDate", End, "<="));
					}
					
					keys.add(new KeyValueWithOperator("status", CommissionSheetStatus.Status_YuLiuZhong, "="));
					List<CommissionSheet> retList = cSheetMgr.findPagedAllBySort(page, rows, "code", true, keys);
					int total = cSheetMgr.getTotalCount(keys);
					JSONArray options = new JSONArray();
					for(CommissionSheet cSheet : retList){
						JSONObject option = new JSONObject();
						option.put("Code", cSheet.getCode());
						option.put("Pwd", cSheet.getPwd());
						option.put("CommissionDate", cSheet.getCommissionDate());
						
						options.put(option);
					}
					
					retJSON18.put("total", total);
					retJSON18.put("rows", options);
				}catch(Exception e){
					try{
						retJSON18.put("total", 0);
						retJSON18.put("rows", new JSONArray());
					}catch(Exception e1){}
					if(e.getClass() == java.lang.Exception.class){	//自定义的消息
						log.debug("exception in CommissionSheetServlet-->case 18", e);
					}else{
						log.error("error in CommissionSheetServlet-->case 18", e);
					}
				}finally{
					resp.setContentType("text/json;charset=utf-8");
					resp.getWriter().write(retJSON18.toString());
				}
				break;
			}
		
		
	}

}
