package com.jlyw.servlet.vehicle;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import com.jlyw.hibernate.ApplianceManufacturer;
import com.jlyw.hibernate.AppliancePopularName;
import com.jlyw.hibernate.ApplianceSpecies;
import com.jlyw.hibernate.ApplianceStandardName;
import com.jlyw.hibernate.CommissionSheet;
import com.jlyw.hibernate.Customer;
import com.jlyw.hibernate.CustomerContactor;
import com.jlyw.hibernate.DrivingVehicle;
import com.jlyw.hibernate.LocaleApplianceItem;
import com.jlyw.hibernate.LocaleMission;
import com.jlyw.hibernate.Quotation;
import com.jlyw.hibernate.QuotationItem;
import com.jlyw.hibernate.Region;
import com.jlyw.hibernate.SubContract;
import com.jlyw.hibernate.SubContractor;
import com.jlyw.hibernate.SysUser;
import com.jlyw.hibernate.Vehicle;
import com.jlyw.hibernate.VehicleMission;
import com.jlyw.manager.ApplianceManufacturerManager;
import com.jlyw.manager.AppliancePopularNameManager;
import com.jlyw.manager.ApplianceSpeciesManager;
import com.jlyw.manager.ApplianceStandardNameManager;
import com.jlyw.manager.CommissionSheetManager;
import com.jlyw.manager.CustomerContactorManager;
import com.jlyw.manager.CustomerManager;
import com.jlyw.manager.DrivingVehicleManager;
import com.jlyw.manager.QualificationManager;
import com.jlyw.manager.SubContractManager;
import com.jlyw.manager.UserManager;
import com.jlyw.manager.vehicle.LocaleApplianceItemManager;
import com.jlyw.manager.vehicle.LocaleMissionManager;
import com.jlyw.manager.vehicle.VehicleManager;
import com.jlyw.manager.vehicle.VehicleMissionManager;
import com.jlyw.util.DateTimeFormatUtil;
import com.jlyw.util.FlagUtil;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.LetterUtil;
import com.jlyw.util.SysStringUtil;
import com.jlyw.util.SystemCfgUtil;

public class LocaleMissionServlet extends HttpServlet {
	private static final Log log = LogFactory.getLog(LocaleMissionServlet.class);
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// System.out.println(req.getParameter("method"));
		Integer method = Integer.parseInt(req.getParameter("method"));
		LocaleMissionManager locmissMgr = new LocaleMissionManager();
		LocaleApplianceItemManager locAppItemMgr = new LocaleApplianceItemManager();
		
		switch (method) {
		case 1: // 添加现场任务委托单
			JSONObject retObj = new JSONObject();
			try{
				String Name = req.getParameter("Name").trim();
				int CustomerId = Integer.parseInt(req.getParameter("CustomerId").trim());
				String ZipCode = req.getParameter("ZipCode");
				String Department = req.getParameter("Department");
				String Address = req.getParameter("Address");
				String Tel = req.getParameter("Tel");
				String ContactorTel = req.getParameter("ContactorTel");
				String TentativeDate = req.getParameter("TentativeDate");
				//String MissionDesc = req.getParameter("MissionDesc").trim();	//检测项目及台件数
				//String Staffs = req.getParameter("Staffs").trim();
				String SiteManagerId = req.getParameter("SiteManagerId");
				String Contactor = req.getParameter("Contactor");
				String RegionId = req.getParameter("RegionId");
				String Remark = req.getParameter("Remark");
				String Appliances = req.getParameter("Appliances").trim();	//检验的器具
				JSONArray appliancesArray = new JSONArray(Appliances);	//检查的器具
				
				
				/************ 检查输入的人员信息中的用户是否存在 *******************/
				/*StringBuffer staffs = new StringBuffer("");
				if(Staffs!=null&&Staffs.length()>0){
					Staffs = Staffs.replace('；', ';');// 分号若为全角转为半角
					UserManager userMgr = new UserManager();
					KeyValueWithOperator k1 = new KeyValueWithOperator("status", 0, "="); 
					String[] staff = Staffs.split(";+");
					
					for(String user : staff){
						KeyValueWithOperator k2 = new KeyValueWithOperator("name", user, "="); 
						if(userMgr.getTotalCount(k1,k2) <= 0){
							throw new Exception(String.format("现场检测人员 ‘%s’ 不存在或无效！", user));
						}
						staffs.append(user).append(";");
					}
				}*/
				Timestamp ts;
                if(TentativeDate==null||TentativeDate.length()==0){
				   ts = null;	//暂定日期
                }else{
			       ts = new Timestamp(DateTimeFormatUtil.DateFormat.parse(TentativeDate).getTime());	//暂定日期
                }
                String Brief = com.jlyw.util.LetterUtil.String2Alpha(Name);
				
                Customer cus=new Customer();
                cus.setId(CustomerId);
				LocaleMission localmission = new LocaleMission();
				localmission.setAddress(Address==null?"":Address);
				localmission.setCustomer(cus);
				localmission.setCustomerName(Name);
				localmission.setBrief(Brief);
				localmission.setZipCode(ZipCode==null?"":ZipCode);
				localmission.setContactor(Contactor==null?"":Contactor);
				localmission.setTel(Tel==null?"":Tel);
				localmission.setContactorTel(ContactorTel==null?"":ContactorTel);
				//localmission.setMissionDesc(MissionDesc);
				localmission.setDepartment(Department);
				//localmission.setStaffs(staffs.toString());
				localmission.setTentativeDate(ts);
				Region region = new Region();
				region.setId(Integer.parseInt(RegionId));
				localmission.setRegion(region);
				localmission.setRemark(Remark);
				
				//生成现场任务委托书号
				String year = String.format("%04d", Calendar.getInstance().get(Calendar.YEAR));
				String queryString = "select max(model.code) from LocaleMission as model where model.code like ?";
				List<Object> retList = locmissMgr.findByHQL(queryString, year+"%");
				Integer codeBeginInt = Integer.parseInt("000000");
				if(retList.size() > 0 && retList.get(0) != null){
					codeBeginInt = Integer.parseInt(retList.get(0).toString().substring(4,10)) + 1;
				}
				String Code = year + String.format("%06d", codeBeginInt);
				localmission.setCode(Code);
				
				//System.out.println();
				
				SysUser newUser = new SysUser();;
				if(SiteManagerId!=null&&SiteManagerId.length()>0){
					newUser.setId(Integer.parseInt(SiteManagerId));
					localmission.setSysUserBySiteManagerId(newUser);
				}else{
					localmission.setSysUserBySiteManagerId(null);
				}
				localmission.setStatus(4);// status: 1 未完成 2 已完成 3已删除 4负责人未核定 5负责人已核定				

				
				Timestamp now = new Timestamp(System.currentTimeMillis());// 取当前时间
				localmission.setCreateTime(now);// 创建时间为当前时间
				localmission.setSysUserByCreatorId((SysUser) req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser));
				localmission.setModificatorDate(now);// 记录时间为当前时间
				localmission.setSysUserByModificatorId((SysUser) req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser));
				
				ApplianceSpeciesManager speciesMgr = new ApplianceSpeciesManager();	//器具分类管理Mgr
				ApplianceStandardNameManager sNameMgr = new ApplianceStandardNameManager();	//器具标准名称管理Mgr
				AppliancePopularNameManager popNameMgr = new AppliancePopularNameManager();	//器具常用名称管理Mgr
				QualificationManager qualMgr = new QualificationManager();	//检测人员资质管理Mgr
				ApplianceManufacturerManager mafMgr = new ApplianceManufacturerManager();	//制造厂管理Mgr
				List<LocaleApplianceItem> localeAppItemList = new ArrayList<LocaleApplianceItem>();	//现场业务中要添加的器具
				List<SysUser> alloteeList = new ArrayList<SysUser>();	//委托单列表对应的派定人：
				List<Integer> qualList = new ArrayList<Integer>();
				
				
				/********************   添加器具信息   ******************/
				for(int i = 0; i < appliancesArray.length(); i++){
					JSONObject jsonObj = appliancesArray.getJSONObject(i);
					LocaleApplianceItem localeAppItem = new LocaleApplianceItem();
					
					/**********************   添加器具信息    ************************/
					String SpeciesType = jsonObj.get("SpeciesType").toString();	//器具分类类型
					String ApplianceSpeciesId = jsonObj.get("ApplianceSpeciesId").toString();	//器具类别ID/标准名称ID
					String ApplianceName = jsonObj.getString("ApplianceName");	//器具名称
					String Manufacturer= jsonObj.getString("Manufacturer");	//制造厂
					
					if(SpeciesType!=null&&SpeciesType.length()>0){
						if(Integer.parseInt(SpeciesType) == 0){	//0:标准名称；1：分类名称
							localeAppItem.setSpeciesType(false);	
						
							String stdName = sNameMgr.findById(Integer.parseInt(ApplianceSpeciesId)).getName();
							if(ApplianceName == null || ApplianceName.trim().length() == 0){
								ApplianceName = stdName;	//器具名称未填写，则默认为标准名称或分类名称
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
							localeAppItem.setSpeciesType(true);	
							if(ApplianceName == null || ApplianceName.trim().length() == 0){
								ApplianceName = speciesMgr.findById(Integer.parseInt(ApplianceSpeciesId)).getName();;	//器具名称未填写，则默认为标准名称或分类名称
							}
						}
						localeAppItem.setApplianceSpeciesId(Integer.parseInt(ApplianceSpeciesId));					
						localeAppItem.setApplianceName(ApplianceName);	//存器具名称		
					}
					localeAppItem.setAppFactoryCode(jsonObj.getString("ApplianceCode"));		//出厂编号
					localeAppItem.setAppManageCode(jsonObj.getString("AppManageCode"));		//管理编号
					localeAppItem.setModel(jsonObj.getString("Model"));		//型号规格
					localeAppItem.setRange(jsonObj.getString("Range"));		//测量范围
					localeAppItem.setAccuracy(jsonObj.getString("Accuracy"));	//精度等级
					localeAppItem.setManufacturer(jsonObj.getString("Manufacturer"));		//制造厂商
					localeAppItem.setCertType(jsonObj.getString("ReportType"));	//报告形式
					if(jsonObj.has("TestFee")&&jsonObj.getString("TestFee").length()>0&&jsonObj.getString("TestFee")!=null){
						localeAppItem.setTestCost(Double.valueOf(jsonObj.getString("TestFee")));
					}
					if(jsonObj.has("RepairFee")&&jsonObj.getString("RepairFee").length()>0&&jsonObj.getString("RepairFee")!=null){
						localeAppItem.setRepairCost(Double.valueOf(jsonObj.getString("RepairFee")));
					}
					if(jsonObj.has("MaterialFee")&&jsonObj.getString("MaterialFee").length()>0&&jsonObj.getString("MaterialFee")!=null){
						localeAppItem.setMaterialCost(Double.valueOf(jsonObj.getString("MaterialFee")));
					}
					localeAppItem.setQuantity(Integer.parseInt(jsonObj.get("Quantity").toString()));//台件数	
					if(jsonObj.has("AssistStaff")&&jsonObj.getString("AssistStaff")!=null&&jsonObj.getString("AssistStaff").trim().length()>0){
						localeAppItem.setAssistStaff(jsonObj.getString("AssistStaff"));	//存辅助派定人或替代人		
					}		
					/**********************  判断派定人是否存在及有效，并加入到alloteeList   ****************************/
					String Allotee = jsonObj.getString("WorkStaff");
					if(Allotee != null && Allotee.trim().length() > 0){
						Allotee = Allotee.trim();
						
						List<Object[]> qualRetList = qualMgr.getQualifyUsers(Allotee, localeAppItem.getApplianceSpeciesId(), localeAppItem.getSpeciesType()?1:0, qualList);
						if(qualRetList != null && qualRetList.size() > 0){
							boolean alloteeChecked = false;
							for(Object[] objArray : qualRetList){
								if(!qualMgr.checkUserQualify((Integer)objArray[0], localeAppItem.getApplianceSpeciesId(), localeAppItem.getSpeciesType()?1:0, FlagUtil.QualificationType.Type_Except)){	//没有该检验项目的检验排外属性
									alloteeChecked = true;
									SysUser tempUser = new SysUser();
									tempUser.setId((Integer)objArray[0]);
									tempUser.setName((String)objArray[1]);
																		
									localeAppItem.setSysUser(tempUser);		//派定人
									break;
								}
							}
							
							if(!alloteeChecked){
								throw new Exception(String.format("派定人 '%s' 不存在或没有资质检验项目：%s，请重新选择！", Allotee, localeAppItem.getApplianceName()));
							}
						}else{
							throw new Exception(String.format("派定人 '%s' 不存在或没有资质检验项目：%s，请重新选择！", Allotee, localeAppItem.getApplianceName()));
						}
					}else{
						localeAppItem.setSysUser(null);		//派定人
						
					}					
					localeAppItemList.add(localeAppItem);
				}
				
				
				if (locAppItemMgr.saveByBatch(localeAppItemList,localmission)) { // 添加成功
					retObj.put("IsOK", true);
				} else {
					throw new Exception("写入数据库失败！");
				}

			}catch (Exception e) {
				try {
					retObj.put("IsOK", false);
					retObj.put("msg", String.format("处理失败！错误信息：%s",(e != null && e.getMessage() != null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if (e.getClass() == java.lang.Exception.class) { // 自定义的消息
					log.debug("exception in LocaleMissionServlet-->case 1", e);
				} else {
					log.error("error in LocaleMissionServlet-->case 1", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj.toString());
			}
			break;
			
		case 2: // 通过customerName查询现场任务委托单
			JSONObject resObj2 = new JSONObject();
			try {
				String localmissionName = req.getParameter("QueryName");

				if (localmissionName == null) {// 避免NullPointerException
					localmissionName = "";
				}
				String LocalemissionName = URLDecoder.decode(localmissionName,"UTF-8");
				
				int page = 1;// 查询页码
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;// 每页显示10行
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				List<LocaleMission> result;
				int total = 0;// 查询结果总数
				if (LocalemissionName == null || LocalemissionName.trim().length() == 0){// 默认查询，不输入单位名称
					result = locmissMgr.findPagedAll(page, rows, new KeyValueWithOperator("status", 3, "<>"));// 查询未被删除的任务
					total = locmissMgr.getTotalCount(new KeyValueWithOperator("status", 3, "<>"));// 未被删除的任务总数
				} else{// 输入单位名称
					result = locmissMgr.findPagedAll(page, rows, 
							new KeyValueWithOperator("customerName", LocalemissionName,"="), 
							new KeyValueWithOperator("status", 3, "<>"));// 根据单位名称查询未被删除任务
					total = locmissMgr.getTotalCount(new KeyValueWithOperator("customer", LocalemissionName, "="),
							new KeyValueWithOperator("status", 3, "<>"));// 根据单位名称查询未被删除的任务总数
				}
				Timestamp today = new Timestamp(System.currentTimeMillis());// 取当前时间
				
				JSONArray options = new JSONArray();
				for (LocaleMission loc : result) {
					// System.out.println("loc.getStatus():"+loc.getStatus());
					if (loc.getStatus() != 3) {
						JSONObject option = new JSONObject();
						option.put("Id", loc.getId());
						option.put("Name", loc.getCustomerName());
						option.put("Code", loc.getCode());
						option.put("CustomerId", loc.getCustomer().getId());
						option.put("ZipCode", loc.getZipCode());
						option.put("Brief", loc.getBrief());
						option.put("Region", loc.getRegion().getName());
						option.put("RegionId", loc.getRegion().getId());
						option.put("Address", loc.getAddress());
						option.put("Remark", loc.getRemark()==null?"":loc.getRemark());
						option.put("Tel", loc.getTel());
						option.put("ZipCode", loc.getZipCode());
						option.put("Status", loc.getStatus());
						option.put("Contactor", loc.getContactor());
						option.put("ContactorTel", loc.getContactorTel());
						option.put("Department", loc.getDepartment());
						option.put("ExactTime", loc.getExactTime()==null?"":DateTimeFormatUtil.DateTimeFormat.format(loc.getExactTime()));
						//option.put("MissionDesc", loc.getMissionDesc());
						option.put("ModificatorDate", loc.getModificatorDate()==null?"":DateTimeFormatUtil.DateFormat.format(loc.getModificatorDate()));
						option.put("ModificatorId", loc.getSysUserByModificatorId().getName());
						option.put("SiteManagerName", loc.getSysUserBySiteManagerId()==null?"":loc.getSysUserBySiteManagerId().getName());
						option.put("SiteManagerId", loc.getSysUserBySiteManagerId()==null?"":loc.getSysUserBySiteManagerId().getId());
						option.put("Staffs", loc.getStaffs());
						//option.put("MissionDescEnter", loc.getMissionDesc().replace("\r\n", "<br/>"));
						option.put("Status", loc.getStatus());
						option.put("Tag", "0");//已核定
						option.put("CheckDate", loc.getCheckDate()==null?"":DateTimeFormatUtil.DateTimeFormat.format(loc.getCheckDate()));//核定时间
						option.put("TentativeDate", loc.getTentativeDate()==null?"":DateTimeFormatUtil.DateFormat.format(loc.getTentativeDate()));
						if(loc.getStatus()==4&&loc.getTentativeDate()!=null){  //未核定
							if(loc.getTentativeDate().after(today)){//已到暂定日期可是未安排
								option.put("Tag", "1");//未核定；；；已到暂定日期可是未安排
							}else{
								option.put("Tag", "2");//未核定；；；未到暂定日期
								
							}
								
						}	
						options.put(option);
						
					}
				}
				resObj2.put("total", total);
				resObj2.put("rows", options);
			} catch (Exception e) {
				try{
					resObj2.put("total", 0);
					resObj2.put("rows", new JSONArray());
				}catch(Exception ex){}
				if (e.getClass() == java.lang.Exception.class) { // 自定义的消息
					log.debug("exception in LocaleMissionServlet-->case 2", e);
				} else {
					log.error("error in LocaleMissionServlet-->case 2", e);
				}
			}finally{
				
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(resObj2.toString());
			}
			break;

		case 3: // 修改现场任务委托单
			JSONObject retObj3 = new JSONObject();
			try {
				String Id = req.getParameter("Id").trim();
				String CustomerName = req.getParameter("Name");
				int CustomerId=Integer.parseInt(req.getParameter("CustomerId"));
				String ZipCode = req.getParameter("ZipCode");
				String Department = req.getParameter("Department");
				String Address = req.getParameter("Address");
				String Tel = req.getParameter("Tel");
				String ContactorTel = req.getParameter("ContactorTel");
				String TentativeDate = req.getParameter("TentativeDate");
				String CheckDate = req.getParameter("CheckDate");//核定日期
				
				String SiteManagerId = req.getParameter("SiteManagerId");
			
				String Contactor = req.getParameter("Contactor");
				String RegionId = req.getParameter("RegionId");
				String Remark = req.getParameter("Remark");
				String Appliances = req.getParameter("Appliances").trim();	//检验的器具
				
				JSONArray appliancesArray = new JSONArray(Appliances);	//检查的器具
				
				int lid = Integer.parseInt(Id);// 获取Id
				LocaleMission localmission = locmissMgr.findById(lid);// 根据查询现场任务
				if(localmission == null){
					throw new Exception("该现场检测业务不存在！");
				}
				if(localmission.getStatus()==2||localmission.getStatus()==3){
					throw new Exception("该现场检测业务'已完工'或‘已注销’,不能修改！");
				}
				
				Timestamp ts;
                if(TentativeDate==null||TentativeDate.length()==0){
				   ts = null;	//暂定日期
                }else{
			       ts = new Timestamp(DateTimeFormatUtil.DateFormat.parse(TentativeDate).getTime());	//暂定日期
                }
                Timestamp now = new Timestamp(System.currentTimeMillis());// 取当前时间
                
                localmission.setCustomerName(CustomerName==null?"":CustomerName);
				localmission.setAddress(Address==null?"":Address);
				localmission.setZipCode(ZipCode==null?"":ZipCode);
				localmission.setContactor(Contactor==null?"":Contactor);
				localmission.setTel(Tel==null?"":Tel);
				localmission.setContactorTel(ContactorTel==null?"":ContactorTel);
				//localmission.setMissionDesc(MissionDesc);
				localmission.setDepartment(Department);
				//localmission.setStaffs(staffs.toString());
				localmission.setTentativeDate(ts);
				Region region = new Region();
				region.setId(Integer.parseInt(RegionId));
				localmission.setRegion(region);
				localmission.setRemark(Remark);
				/************更新委托单位信息***********/
				Customer cus=(new CustomerManager()).findById(CustomerId);
				if(CustomerName!=null&&CustomerName.length()>0){
					cus.setName(CustomerName);
				}
				if(Address!=null&&Address.length()>0){
					cus.setAddress(Address);
				}
				if(Tel!=null&&Tel.length()>0){
					cus.setTel(Tel);
				}
				if(ZipCode!=null&&ZipCode.length()>0){
					cus.setZipCode(ZipCode);
				}
				if(!(new CustomerManager()).update(cus)){
					throw new Exception("更新委托单位失败！");				
				}
				/************更新委托单位联系人信息***********/	
				CustomerContactorManager cusConMgr = new CustomerContactorManager();
				List<CustomerContactor> cusConList = cusConMgr.findByVarProperty(new KeyValueWithOperator("customerId", CustomerId, "="), new KeyValueWithOperator("name", Contactor, "="));
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
						if(!cusConMgr.update(c))
							throw new Exception("更新单位联系人失败！");		
					}else{
						CustomerContactor c = new CustomerContactor();
						Customer a=new Customer();
						a.setId(CustomerId);
						c.setCustomer(a);
						c.setName(Contactor);
						c.setCellphone1(ContactorTel);
						c.setLastUse(now);
						c.setCount(1);
						if(!cusConMgr.save(c))
							throw new Exception("更新单位联系人失败！");		
					}
				}
								
				if(CheckDate!=null&&CheckDate.length()>0){  //核定
					Timestamp CheckTime = new Timestamp(DateTimeFormatUtil.DateFormat.parse(CheckDate).getTime());
					localmission.setCheckDate(CheckTime);
					if(localmission.getStatus()==4){
						//localmission.setStatus(5);//已核定
					}
				}
				
				SysUser newUser = new SysUser();
				if(SiteManagerId!=null&&SiteManagerId.length()>0){  //负责人
					newUser.setId(Integer.parseInt(SiteManagerId));
					localmission.setSysUserBySiteManagerId(newUser);	
				}else{
					localmission.setSysUserBySiteManagerId(null);	
				}
				
				
				localmission.setModificatorDate(now);// 记录时间为当前时间
				localmission.setSysUserByModificatorId((SysUser) req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser));
				
				ApplianceSpeciesManager speciesMgr = new ApplianceSpeciesManager();	//器具分类管理Mgr
				ApplianceStandardNameManager sNameMgr = new ApplianceStandardNameManager();	//器具标准名称管理Mgr
				AppliancePopularNameManager popNameMgr = new AppliancePopularNameManager();	//器具常用名称管理Mgr
				QualificationManager qualMgr = new QualificationManager();	//检测人员资质管理Mgr
				ApplianceManufacturerManager mafMgr = new ApplianceManufacturerManager();	//制造厂管理Mgr
				
				List<LocaleApplianceItem> localeAppItemList = new ArrayList<LocaleApplianceItem>();	//现场业务中要添加的器具
				List<SysUser> alloteeList = new ArrayList<SysUser>();	//委托单列表对应的派定人：
				List<Integer> qualList = new ArrayList<Integer>();
			
				/********************   添加器具信息   ******************/
				for(int i = 0; i < appliancesArray.length(); i++){
					JSONObject jsonObj = appliancesArray.getJSONObject(i);
					LocaleApplianceItem localeAppItem = new LocaleApplianceItem();
					
					/**********************   添加器具信息    ************************/
					String SpeciesType = (jsonObj.has("SpeciesType")?jsonObj.get("SpeciesType").toString():"");	//器具分类类型
					String ApplianceSpeciesId = (jsonObj.has("ApplianceSpeciesId")?jsonObj.get("ApplianceSpeciesId").toString():"");	//器具类别ID/标准名称ID
					String ApplianceName = jsonObj.getString("ApplianceName");	//器具名称
					String Manufacturer= jsonObj.getString("Manufacturer");	//制造厂
					if(SpeciesType!=null&&SpeciesType.length()>0){
						if(Integer.parseInt(SpeciesType) == 0){	//0:标准名称；1：分类名称
							localeAppItem.setSpeciesType(false);	
							String stdName = sNameMgr.findById(Integer.parseInt(ApplianceSpeciesId)).getName();
							if(ApplianceName == null || ApplianceName.trim().length() == 0){
								ApplianceName = stdName;	//器具名称未填写，则默认为标准名称或分类名称
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
							localeAppItem.setSpeciesType(true);	
							if(ApplianceName == null || ApplianceName.trim().length() == 0){
								ApplianceName = speciesMgr.findById(Integer.parseInt(ApplianceSpeciesId)).getName();;	//器具名称未填写，则默认为标准名称或分类名称
							}
						}
						localeAppItem.setApplianceSpeciesId(Integer.parseInt(ApplianceSpeciesId));					
						localeAppItem.setApplianceName(ApplianceName);	//存器具名称		
					}
					localeAppItem.setAppFactoryCode(jsonObj.getString("ApplianceCode"));		//出厂编号
					localeAppItem.setAppManageCode(jsonObj.getString("AppManageCode"));		//管理编号
					localeAppItem.setModel(jsonObj.getString("Model"));		//型号规格
					localeAppItem.setRange(jsonObj.getString("Range"));		//测量范围
					localeAppItem.setAccuracy(jsonObj.getString("Accuracy"));	//精度等级
					localeAppItem.setManufacturer(jsonObj.getString("Manufacturer"));		//制造厂商
					localeAppItem.setCertType(jsonObj.getString("ReportType"));	//报告形式
					if(jsonObj.has("TestFee")&&jsonObj.getString("TestFee").length()>0&&jsonObj.getString("TestFee")!=null){
						localeAppItem.setTestCost(Double.valueOf(jsonObj.getString("TestFee")));
					}
					if(jsonObj.has("RepairFee")&&jsonObj.getString("RepairFee").length()>0&&jsonObj.getString("RepairFee")!=null){
						localeAppItem.setRepairCost(Double.valueOf(jsonObj.getString("RepairFee")));
					}
					if(jsonObj.has("MaterialFee")&&jsonObj.getString("MaterialFee").length()>0&&jsonObj.getString("MaterialFee")!=null){
						localeAppItem.setMaterialCost(Double.valueOf(jsonObj.getString("MaterialFee")));
					}
					localeAppItem.setQuantity(Integer.parseInt(jsonObj.get("Quantity").toString()));//台件数	
					if(jsonObj.has("AssistStaff")&&jsonObj.getString("AssistStaff")!=null&&jsonObj.getString("AssistStaff").trim().length()>0)
						localeAppItem.setAssistStaff(jsonObj.getString("AssistStaff"));	//存辅助派定人或替代人		
								
					/**********************  判断派定人是否存在及有效，并加入到alloteeList   ****************************/
					String Allotee = jsonObj.getString("WorkStaff");
					if(Allotee != null && Allotee.trim().length() > 0){
						Allotee = Allotee.trim();
						
						List<Object[]> qualRetList = qualMgr.getQualifyUsers(Allotee, localeAppItem.getApplianceSpeciesId(), localeAppItem.getSpeciesType()?1:0, qualList);
						if(qualRetList != null && qualRetList.size() > 0){
							boolean alloteeChecked = false;
							for(Object[] objArray : qualRetList){
								if(!qualMgr.checkUserQualify((Integer)objArray[0], localeAppItem.getApplianceSpeciesId(), localeAppItem.getSpeciesType()?1:0, FlagUtil.QualificationType.Type_Except)){	//没有该检验项目的检验排外属性
									alloteeChecked = true;
									SysUser tempUser = new SysUser();
									tempUser.setId((Integer)objArray[0]);
									tempUser.setName((String)objArray[1]);
																		
									localeAppItem.setSysUser(tempUser);		//派定人
									break;
								}
							}
							
							if(!alloteeChecked){
								throw new Exception(String.format("派定人 '%s' 不存在或没有资质检验项目：%s，请重新选择！", Allotee, localeAppItem.getApplianceName()));
							}
						}else{
							throw new Exception(String.format("派定人 '%s' 不存在或没有资质检验项目：%s，请重新选择！", Allotee, localeAppItem.getApplianceName()));
						}
					}else{
						localeAppItem.setSysUser(null);		//派定人
						
					}	
					
					if(jsonObj.has("Id")&&jsonObj.getString("Id")!=null&&jsonObj.getString("Id").trim().length()>0)
						localeAppItem.setId(Integer.parseInt(jsonObj.getString("Id")));
					localeAppItemList.add(localeAppItem);
				}
				
				
				if (locAppItemMgr.updateByBatch(localeAppItemList,localmission)) { // 修改现场业务，删除原现场业务中的器具信息，添加新的器具信息
					retObj3.put("IsOK", true);
				} else {
					throw new Exception("更新数据库失败！");
				}

			} catch (Exception e) {
				try {
					retObj3.put("IsOK", false);
					retObj3.put("msg", String.format("处理失败！错误信息：%s",(e != null && e.getMessage() != null)?e.getMessage() : "无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				
				if (e.getClass() == java.lang.Exception.class) { // 自定义的消息
					log.debug("exception in LocaleMissionServlet-->case 3", e);
				} else {
					log.error("error in LocaleMissionServlet-->case 3", e);
				}
			} finally {
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj3.toString());
			}
			break;
		case 4: // 删除现场任务委托单
			JSONObject retJSON4 = new JSONObject();
			try{
				int id = Integer.parseInt(req.getParameter("id"));
				LocaleMission localmission = locmissMgr.findById(id);
				if(localmission.getStatus()==2||localmission.getStatus()==3){
					throw new Exception("该现场检测业务'已完工'或‘已注销’,不能修改！");
				}
				localmission.setStatus(3);// 将指定Id任务状态置为3，表示已删除
				if(locmissMgr.update(localmission)){
					retJSON4.put("IsOK", true);
				}else{
					throw new Exception("更新数据库失败！");
				}
			}catch(Exception e){
				try{
					retJSON4.put("IsOK", false);
					retJSON4.put("msg", String.format("处理失败！错误信息：%s",(e != null && e.getMessage() != null)?e.getMessage() : "无"));
				}catch(Exception ex){}
				
				if (e.getClass() == java.lang.Exception.class) { // 自定义的消息
					log.debug("exception in LocaleMissionServlet-->case 3", e);
				} else {
					log.error("error in LocaleMissionServlet-->case 3", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON4.toString());
			}
			break;
		case 5: // 查询车辆出车情况
		{
			
			String dateTime = req.getParameter("dateTime");
			Timestamp ts = Timestamp.valueOf(String.format("%s 00:00:00",
					dateTime.trim()));
			String CarNo = "";
			if (req.getParameter("Lisence") != null)
				CarNo = URLDecoder.decode(req.getParameter("Lisence").trim(),
						"UTF-8"); // 解决jquery传递中文乱码问题
			VehicleMissionManager vemissmag = new VehicleMissionManager();
			VehicleManager vemag = new VehicleManager();
			JSONObject res = new JSONObject();
			try {
				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer
							.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer
							.parseInt(req.getParameter("rows").toString());
				List<VehicleMission> vehiclemission;
				List<Vehicle> vehicle;
				int total = 0;
				int limit=0;
				JSONArray options = new JSONArray();

				if (CarNo.length() == 0) {// 没有填写车辆牌照
					// System.out.println("0001111");
					vehicle = vemag.findPagedAll(page, rows,
							new KeyValueWithOperator("status", 0, "="));// 查询正常车辆
					total = vemag.getTotalCount(new KeyValueWithOperator(
							"status", 0, "="));// 正常车辆数量
					for (Vehicle ve : vehicle) {
						int[] CCCS = {0,0,0,0,0,0,0,0,0,0,0,0,0,0};
					
						int veid = ve.getId();
						String license = vemag.findById(veid).getLicence();// 根据车辆id获取车牌号
						limit= (vemag.findById(veid).getLimit()==null?0:vemag.findById(veid).getLimit());
						String driverName = "";
						if (vemag.findById(veid).getSysUser() != null) {
							driverName = vemag.findById(veid).getSysUser().getName();
							
							// System.out.println(license);
						}
						// System.out.println(driverName);
						Calendar ca = Calendar.getInstance();// 新建一个日历实例
						ca.setTime(ts);
						int dow = ca.get(Calendar.DAY_OF_WEEK);// 得到选择的日期是一周的第几天
						//System.out.println(dow);
						if(dow==1)//选择的日期为周日，系统默认周日为一周第一天，可是咱们的周日为第七天
							ca.add(Calendar.DATE, 2 - dow-7);// 获取该周的星期一的日期，周一为一周第一天 (刘振修改了，因为发现之前得到的是周日)
						else
							ca.add(Calendar.DATE, 2 - dow);// 获取该周的星期一的日期，周一为一周第一天 (刘振修改了，因为发现之前得到的是周日)
						Timestamp mon = new Timestamp(ca.getTimeInMillis());
						
						//System.out.println(mon);
						ca.add(Calendar.DATE, 7);// 获取该周周日的日期，周日为一周最后一天
						Timestamp nextmon = new Timestamp(ca.getTimeInMillis());
						//System.out.println(nextmon);
						
						vehiclemission = vemissmag.findByVarProperty(new KeyValueWithOperator("drivingVehicle.vehicle", ve, "="),
								new KeyValueWithOperator("drivingVehicle.beginDate", nextmon,"<"), new KeyValueWithOperator("drivingVehicle.endDate", mon, ">="),
								new KeyValueWithOperator("localeMission.status", 3, "<>"),new KeyValueWithOperator("drivingVehicle.vehicle.status", 1, "<>"));// 查询任务安排在该周的现场任务
						if (vehiclemission.size() > 0) {// 如果存在查询结果
							String[] available = timeJudge(vehiclemission, mon,CCCS);
							JSONObject option = new JSONObject();
							option.put("driverName", driverName);
							option.put("limit", limit);
							option.put("vehicleid", license);
							option.put("onea", available[0]);
							option.put("onep", available[1]);
							option.put("twoa", available[2]);
							option.put("twop", available[3]);
							option.put("threea", available[4]);
							option.put("threep", available[5]);
							option.put("foura", available[6]);
							option.put("fourp", available[7]);
							option.put("fivea", available[8]);
							option.put("fivep", available[9]);
							option.put("sixa", available[10]);
							option.put("sixp", available[11]);
							option.put("sevena", available[12]);
							option.put("sevenp", available[13]);
							options.put(option);
						} else {// 查找不到任务说明本周都是空闲
							JSONObject option = new JSONObject();
							option.put("driverName", driverName);
							option.put("limit", limit);
							option.put("vehicleid", license);
							option.put("onea", "空闲");
							option.put("onep", "空闲");
							option.put("twoa", "空闲");
							option.put("twop", "空闲");
							option.put("threea", "空闲");
							option.put("threep", "空闲");
							option.put("foura", "空闲");
							option.put("fourp", "空闲");
							option.put("fivea", "空闲");
							option.put("fivep", "空闲");
							option.put("sixa", "空闲");
							option.put("sixp", "空闲");
							option.put("sevena", "空闲");
							option.put("sevenp", "空闲");
							options.put(option);
						}
					}
				} else {// 填写了车辆牌照
					vehicle = vemag.findPagedAll(page, rows,
							new KeyValueWithOperator("licence", "%"+CarNo+"%", "like"),
							new KeyValueWithOperator("status", 0, "="));// 根据填写车牌查找车辆
					int[] CCCS = {0,0,0,0,0,0,0,0,0,0,0,0,0,0};
					if (vehicle.size() > 0) {// 查找到车辆
						for (Vehicle ve : vehicle) {
							total = 1;
							int veid = ve.getId();// 获取车辆Id
							String driverName = "";
							limit= (vemag.findById(veid).getLimit()==null?0:vemag.findById(veid).getLimit());
							if (vemag.findById(veid).getSysUser() != null) {
								driverName = vemag.findById(veid).getSysUser()
										.getName();
								
							}
							Calendar ca = Calendar.getInstance();// 新建一个日历实例
							ca.setTime(ts);
							int dow = ca.get(Calendar.DAY_OF_WEEK);// 得到选择的日期是一周的第几天
							//System.out.println(dow);
							if(dow==1)//选择的日期为周日，系统默认周日为一周第一天，可是咱们的周日为第七天
								ca.add(Calendar.DATE, 2 - dow-7);// 获取该周的星期一的日期，周一为一周第一天 (刘振修改了，因为发现之前得到的是周日)
							else
								ca.add(Calendar.DATE, 2 - dow);// 获取该周的星期一的日期，周一为一周第一天 (刘振修改了，因为发现之前得到的是周日)
							Timestamp mon = new Timestamp(ca.getTimeInMillis());
							
							//System.out.println(mon);
							ca.add(Calendar.DATE, 7);// 获取该周周日的日期，周日为一周最后一天
							Timestamp nextmon = new Timestamp(ca.getTimeInMillis());
							vehiclemission = vemissmag.findByVarProperty(new KeyValueWithOperator("drivingVehicle.vehicle", ve, "="),
									new KeyValueWithOperator("drivingVehicle.beginDate", nextmon,"<"), new KeyValueWithOperator("drivingVehicle.beginDate",mon, ">="),
									new KeyValueWithOperator("localeMission.status", 3, "<>"),new KeyValueWithOperator("drivingVehicle.vehicle.status", 1, "<>"));
							if (vehiclemission.size() > 0) {
								String[] available = timeJudge(vehiclemission, mon,CCCS);
								JSONObject option = new JSONObject();
								option.put("driverName", ve.getSysUser()==null?"":ve.getSysUser().getName());
								option.put("vehicleid", ve.getLicence());
								option.put("limit", limit);
								option.put("onea", available[0]);
								option.put("onep", available[1]);
								option.put("twoa", available[2]);
								option.put("twop", available[3]);
								option.put("threea", available[4]);
								option.put("threep", available[5]);
								option.put("foura", available[6]);
								option.put("fourp", available[7]);
								option.put("fivea", available[8]);
								option.put("fivep", available[9]);
								option.put("sixa", available[10]);
								option.put("sixp", available[11]);
								option.put("sevena", available[12]);
								option.put("sevenp", available[13]);
								options.put(option);
							} else {
								JSONObject option = new JSONObject();
								option.put("driverName", ve.getSysUser()==null?"":ve.getSysUser().getName());
								option.put("vehicleid", ve.getLicence());
								option.put("limit", limit);
								option.put("onea", "空闲");
								option.put("onep", "空闲");
								option.put("twoa", "空闲");
								option.put("twop", "空闲");
								option.put("threea", "空闲");
								option.put("threep", "空闲");
								option.put("foura", "空闲");
								option.put("fourp", "空闲");
								option.put("fivea", "空闲");
								option.put("fivep", "空闲");
								option.put("sixa", "空闲");
								option.put("sixp", "空闲");
								option.put("sevena", "空闲");
								option.put("sevenp", "空闲");
								options.put(option);
							}
						}
					}
				}

				res.put("total", total);
				res.put("rows", options);
			} catch (Exception e) {
				try{
					res.put("total", 0);
					res.put("rows", new JSONArray());
				}catch(Exception e1){}
				if (e.getClass() == java.lang.Exception.class) { // 自定义的消息
					log.debug("exception in LocaleMissionServlet-->case 5", e);
				} else {
					log.error("error in LocaleMissionServlet-->case 5", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(res.toString());
			}
			break;
		}
		case 6:// 业务调度中查询现场业务
		{
			JSONObject res = new JSONObject();
			try {
				String QueryName = req.getParameter("QueryName");
				String BeginDate = req.getParameter("History_BeginDate");
				String EndDate = req.getParameter("History_EndDate");
				//String MissionStatus = req.getParameter("MissionStatus");
				String Department = req.getParameter("Department");
				if (QueryName == null) {// 避免NullPointerException
					QueryName = "";
				}
				if (BeginDate == null) {// 避免NullPointerException
					BeginDate = "";
				}
				if (EndDate == null) {// 避免NullPointerException
					EndDate = "";
				}
				//if (MissionStatus == null) {// 避免NullPointerException
				//	MissionStatus = "";
				//}
				if (Department == null) {// 避免NullPointerException
					Department = "";
				}
				int page = 1;// 查询页码
				if (req.getParameter("page") != null)
					page = Integer
							.parseInt(req.getParameter("page").toString());
				int rows = 10;// 每页显示10行
				if (req.getParameter("rows") != null)
					rows = Integer
							.parseInt(req.getParameter("rows").toString());

				String queryStr = "from LocaleMission as model where (model.status = 1 or model.status = 5)"; //已分配或已核定
				List<Object> keys = new ArrayList<Object>();
				//List<KeyValueWithOperator> condList = new ArrayList<KeyValueWithOperator>();
				//condList.add(new KeyValueWithOperator("status", 3, "<>"));//注销
				
				if (QueryName != null && QueryName.trim().length() > 0) {
					String Name = URLDecoder.decode(QueryName.trim(),
							"UTF-8");
					
					//condList.add(new KeyValueWithOperator("customerName",Name, "="));
					queryStr=queryStr+" and model.customerName like ?";
					keys.add("%" + Name + "%");
				}

				if (Department != null && Department.trim().length() > 0) {
					String department = URLDecoder.decode(Department.trim(),
							"UTF-8");
					
					
					queryStr=queryStr+" and model.department like ?";
					keys.add("%" + department + "%");
				}
				if (BeginDate != null && BeginDate.trim().length() > 0) {
					Timestamp beginTs = Timestamp.valueOf(String.format("%s 00:00:00", BeginDate.trim()));
					
					queryStr=queryStr+" and (model.tentativeDate >= ? or model.checkDate >= ? or model.exactTime >= ?)";
					keys.add(beginTs);
					keys.add(beginTs);
					keys.add(beginTs);
					
				}
				if (EndDate != null && EndDate.trim().length() > 0) {
					Timestamp endTs = Timestamp.valueOf(String.format(
							"%s 23:59:00", EndDate.trim()));
				   // condList.add(new KeyValueWithOperator("tentativeDate",endTs, "<="));
					queryStr=queryStr+" and (model.tentativeDate <= ? or model.checkDate <= ? or model.exactTime <= ?)";
					keys.add(endTs);
					keys.add(endTs);
					keys.add(endTs);
				}

				List<LocaleMission> result = locmissMgr.findPageAllByHQL(queryStr+" order by model.createTime desc",page,rows, keys);// 
				int total = locmissMgr.getTotalCountByHQL("select count(model)"+queryStr,keys);// 

				JSONArray options = new JSONArray();
				Timestamp today = new Timestamp(System.currentTimeMillis());// 取当前时间
				for (LocaleMission loc : result) {
					// System.out.println("loc.getStatus():"+loc.getStatus());
					
						JSONObject option = new JSONObject();
						option.put("Id", loc.getId());
						option.put("Name", loc.getCustomerName());
						option.put("Code", loc.getCode());
						option.put("CustomerId", loc.getCustomer().getId());
						option.put("CreatorName", loc.getSysUserByCreatorId().getName());
						option.put("CreateDate",DateTimeFormatUtil.DateTimeFormat.format(loc.getCreateTime()) + loc.getSysUserByCreatorId().getName());
						option.put("ZipCode", loc.getZipCode());
						option.put("Brief", loc.getBrief());
						option.put("RegionId", loc.getRegion().getId());
						option.put("Region", loc.getRegion().getName());
						option.put("Address", loc.getAddress());
						option.put("Remark", loc.getRemark()==null?"":loc.getRemark());
						option.put("Tel", loc.getTel());
						option.put("ZipCode", loc.getZipCode());
						option.put("Status", loc.getStatus());
						option.put("Contactor", loc.getContactor());
						option.put("ContactorTel", loc.getContactorTel());
						option.put("Department", loc.getDepartment());
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						option.put("ExactTime", loc.getExactTime()==null?"":DateTimeFormatUtil.DateFormat.format(loc.getExactTime()));
						//option.put("MissionDesc", loc.getMissionDesc());
						option.put("ModificatorDate", loc.getModificatorDate()==null?"":DateTimeFormatUtil.DateFormat.format(loc.getModificatorDate()));
						option.put("ModificatorId", loc.getSysUserByModificatorId().getName());
						option.put("SiteManagerName", loc.getSysUserBySiteManagerId()==null?"":loc.getSysUserBySiteManagerId().getName());
						option.put("SiteManagerId", loc.getSysUserBySiteManagerId()==null?"":loc.getSysUserBySiteManagerId().getId());
						//找现场业务下面的各个器具指派人
						String staffs="";
						String queryString="from LocaleApplianceItem as a where a.localeMission.id = ?";
						List<SysUser>  workStaffs= locAppItemMgr.findByHQL("select distinct a.sysUser "+queryString, loc.getId());
						if(!workStaffs.isEmpty()){
							
							for (SysUser user : workStaffs) {
								staffs = staffs + user.getName()+";"; 							
							}
						}
						if(loc.getSysUserBySiteManagerId()!=null){
							if(staffs.indexOf(loc.getSysUserBySiteManagerId().getName()+";")<0){
								staffs = staffs + loc.getSysUserBySiteManagerId().getName()+";"; 		
							}
						}
						//找现场业务下面的各个器具信息
						String missionDesc="";
						List<LocaleApplianceItem>  locAppList= locAppItemMgr.findByHQL(queryString, loc.getId());
						if(!locAppList.isEmpty()){						
							for (LocaleApplianceItem locApp : locAppList) {
								missionDesc = missionDesc + locApp.getApplianceName()+"  "+locApp.getQuantity()+"<br/>"; 							
							}
						}
						
						option.put("Staffs", staffs);
						option.put("VehicleLisences", loc.getVehicleLisences()==null?"":loc.getVehicleLisences());
						option.put("MissionDesc", missionDesc);
						option.put("Status", loc.getStatus());
						option.put("Tag", "0");//已核定
						option.put("Feedback", loc.getFeedback()==null?"":loc.getFeedback());//反馈
						option.put("CheckDate", loc.getCheckDate()==null?"":DateTimeFormatUtil.DateFormat.format(loc.getCheckDate()));//核定时间
						option.put("TentativeDate", loc.getTentativeDate()==null?"":DateTimeFormatUtil.DateFormat.format(loc.getTentativeDate()));
						if(loc.getStatus()==4&&loc.getTentativeDate()!=null){  //未核定
							if(loc.getTentativeDate().after(today)){//已到暂定日期可是未安排
								option.put("Tag", "1");//未核定；；；已到暂定日期可是未安排
							}else{
								option.put("Tag", "2");//未核定；；；未到暂定日期
								
							}
								
						}	
						options.put(option);
				}
				res.put("total", total);
				res.put("rows", options);
			} catch (Exception e) {
				
				try {
					res.put("total", 0);
					res.put("rows", new JSONArray());
				} catch (JSONException ex) {
				}
				if (e.getClass() == java.lang.Exception.class) { // 自定义的消息
					log.debug("exception in LocaleMissionServlet-->case 6", e);
				} else {
					log.error("error in LocaleMissionServlet-->case 6", e);
				}
			} finally {
				resp.setContentType("text/json;charset=utf-8");
				
				resp.getWriter().write(res.toString());
			}

			break;
		}
		case 7:// 车辆调度
		{
			JSONObject retObj7 = new JSONObject();
			try {
				String vehiclearrange = req.getParameter("vehiclearrange").trim();// 车辆安排JSON
				JSONArray vehiclearrangeArray = new JSONArray(vehiclearrange);
				String missionarrange = req.getParameter("missionarrange").trim();// 任务安排JSON
				JSONArray missionarrangeArray = new JSONArray(missionarrange);
				Timestamp beginTs;// 调度起始时间
				Timestamp endTs ;// 调度结束时间
			
				String ExactTime = req.getParameter("ExactTime").trim();// 确定时间
				String BeginDate = req.getParameter("BeginDate").trim();// 开始时间（发车时间）
				String AssemblingPlace = req.getParameter("AssemblingPlace");// 集合地点											
			
				
				beginTs = Timestamp.valueOf(BeginDate);
				endTs = Timestamp.valueOf(String.format("%s 17:00:00", ExactTime.trim()));
				
				VehicleManager vehmag = new VehicleManager();
				VehicleMissionManager vmmag = new VehicleMissionManager();
				LocaleMissionManager lmmag = new LocaleMissionManager();
				UserManager umag = new UserManager();
				
				/****************新建出车记录***********/
				String license = vehiclearrangeArray.getJSONObject(0).getString("vehicleid"); // 获取调度车辆的牌照
				DrivingVehicle drivingvehicle=new DrivingVehicle();
				String driverName = req.getParameter("DriverName");// 获取司机姓名
				List<SysUser> driver = umag.findByVarProperty(new KeyValueWithOperator("name", driverName, "="));// 获取司机的SysUser对象
				int driverid = -1;
				if (driver.size() > 0) {
					driverid = driver.get(0).getId();// 获取司机id
				} else {
					throw new Exception("司机姓名错误！");
				}
				String People = req.getParameter("People");// 坐车人员
				
				

				/************ 检查输入的“坐车人员”信息中的用户是否存在 *******************/	
				StringBuffer staffs = new StringBuffer("");
				if(People!=null&&People.length()>0){						
					People = People.replace('；', ';');// 分号若为全角转为半角
					UserManager userMgr = new UserManager();
					KeyValueWithOperator k1 = new KeyValueWithOperator("status", 0, "="); 
					String[] staff = People.split(";+");
					
					for(String user : staff){
						KeyValueWithOperator k2 = new KeyValueWithOperator("name", user, "="); 
						if(userMgr.getTotalCount(k1,k2) <= 0){
							throw new Exception(String.format("现场检测人员 ‘%s’ 不存在或无效！", user));
						}
						staffs.append(user).append(";");
					}
				}
				People=staffs.toString();	
				
				drivingvehicle.setAssemblingPlace(AssemblingPlace);
				drivingvehicle.setBeginDate(beginTs);
				drivingvehicle.setStatus(0);
				drivingvehicle.setSysUserByDriverId(driver.get(0)); // 驾驶员
				drivingvehicle.setEndDate(endTs);					
				drivingvehicle.setPeople(People);
				drivingvehicle.setVehicle(vehmag.findByVarProperty(new KeyValueWithOperator("licence", license,"=")).get(0));// 车辆
				if (!(new DrivingVehicleManager()).save(drivingvehicle)) {
					throw new Exception("新建出车记录失败");
				} 
				
				
				
				/*********************对每一个现场任务，新增一条VehicleMission记录*****************************/
				if (vehiclearrangeArray.length() > 0&& missionarrangeArray.length() > 0) {//，任务安排和车辆安排不为空				
					if (beginTs.after(endTs)) {
						throw new Exception("调度时间错误,任务发车时间和任务确定时间不在同一天");
					}
					for (int i = 0; i < missionarrangeArray.length(); i++) {// 任务JSONArray遍历
						JSONObject misarr = missionarrangeArray.getJSONObject(i);
						int missionId = misarr.getInt("Id");// 获取任务id
						LocaleMission lmission = lmmag.findById(missionId);// 根据任务id获取任务
						if(lmission.getStatus()==2||lmission.getStatus()==3){
							throw new Exception("该现场检测业务'已完工'或‘已注销’,不能修改！");
						}
						String templisences=(lmission.getVehicleLisences()==null?"":lmission.getVehicleLisences());//原任务所乘坐的车的车牌号
						String sssemblingPlace=(AssemblingPlace==null?"":AssemblingPlace);
						if(templisences.indexOf(license)<0){
							lmission.setVehicleLisences(templisences + license +"("+ sssemblingPlace + ");");//更新现场任务中的所乘车辆信息
							//lmission.setVehicleLisences(templisences + license + ";");//更新现场任务中的所乘车辆信息
						}
						VehicleMission vm=new VehicleMission();//新增任务-出车记录信息
						vm.setDrivingVehicle(drivingvehicle);
						vm.setLocaleMission(lmission);	
						if (vmmag.save(vm)) {
							// System.out.println("分配成功");
						} else {
							throw new Exception("分配失败");//新增任务-出车记录信息
						}
						
						lmission.setStatus(1);//更新现场任务的状态(已分配)，（是不是也要更新任务“确定时间”）
						lmission.setExactTime(beginTs);						
						if (lmmag.update(lmission)) {
							//System.out.println("更新成功");
						} else {
							throw new Exception("任务状态更新失败");
						}
					}
				}
				retObj7.put("IsOK", true);
			} catch (Exception e) {
				// System.out.println("update failed");
				try {
					retObj7.put("IsOK", false);
					retObj7.put("msg", String.format("处理失败！错误信息：%s",
							(e != null && e.getMessage() != null) ? e
									.getMessage() : "无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if (e.getClass() == java.lang.Exception.class) { // 自定义的消息
					log.debug("exception in LocaleMissionServlet-->case 7", e);
				} else {
					log.error("error in LocaleMissionServlet-->case 7", e);
				}
			} finally {
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj7.toString());
			}

			break;
		}
		case 8:// 查询现场业务(现场业务管理中使用)
		{
			JSONObject res = new JSONObject();
			try {
				String QueryName = req.getParameter("QueryName");
				String BeginDate = req.getParameter("History_BeginDate");
				String EndDate = req.getParameter("History_EndDate");
				String MissionStatus = req.getParameter("MissionStatus");
				String Department = req.getParameter("Department");
				if (QueryName == null) {// 避免NullPointerException
					QueryName = "";
				}
				if (BeginDate == null) {// 避免NullPointerException
					BeginDate = "";
				}
				if (EndDate == null) {// 避免NullPointerException
					EndDate = "";
				}
				if (MissionStatus == null) {// 避免NullPointerException
					MissionStatus = "";
				}
				if (Department == null) {// 避免NullPointerException
					Department = "";
				}
				int page = 1;// 查询页码
				if (req.getParameter("page") != null)
					page = Integer
							.parseInt(req.getParameter("page").toString());
				int rows = 10;// 每页显示10行
				if (req.getParameter("rows") != null)
					rows = Integer
							.parseInt(req.getParameter("rows").toString());

				String queryStr = "from LocaleMission as model where model.status <> 3 ";
				List<Object> keys = new ArrayList<Object>();
				//List<KeyValueWithOperator> condList = new ArrayList<KeyValueWithOperator>();
				//condList.add(new KeyValueWithOperator("status", 3, "<>"));//注销
				
				if (QueryName != null && QueryName.trim().length() > 0) {
					String Name = URLDecoder.decode(QueryName.trim(),
							"UTF-8");
					
					//condList.add(new KeyValueWithOperator("customerName",Name, "="));
					queryStr=queryStr+" and model.customerName like ?";
					keys.add("%" + Name + "%");
				}
				if (MissionStatus != null && MissionStatus.trim().length() > 0) {
					int status = Integer.parseInt(URLDecoder.decode(MissionStatus.trim(),"UTF-8"));
				
					//condList.add(new KeyValueWithOperator("status",status, "="));
					queryStr=queryStr+" and model.status = ?";
					keys.add(status);
				}
				if (Department != null && Department.trim().length() > 0) {
					String department = URLDecoder.decode(Department.trim(),
							"UTF-8");
					
					//condList.add(new KeyValueWithOperator("department","%"+department+"%", "like"));
					queryStr=queryStr+" and model.department like ?";
					keys.add("%" + department + "%");
				}
				if (BeginDate != null && BeginDate.trim().length() > 0) {
					Timestamp beginTs = Timestamp.valueOf(String.format("%s 00:00:00", BeginDate.trim()));
					//condList.add(new KeyValueWithOperator("tentativeDate",beginTs, ">="));
					queryStr=queryStr+" and (model.tentativeDate >= ? or model.checkDate >= ? or model.exactTime >= ?)";
					keys.add(beginTs);
					keys.add(beginTs);
					keys.add(beginTs);
					
				}
				if (EndDate != null && EndDate.trim().length() > 0) {
					Timestamp endTs = Timestamp.valueOf(String.format(
							"%s 23:59:00", EndDate.trim()));
				   // condList.add(new KeyValueWithOperator("tentativeDate",endTs, "<="));
					queryStr=queryStr+" and (model.tentativeDate <= ? or model.checkDate <= ? or model.exactTime <= ?)";
					keys.add(endTs);
					keys.add(endTs);
					keys.add(endTs);
				}

				List<LocaleMission> result = locmissMgr.findPageAllByHQL(queryStr+" order by model.createTime desc",page,rows, keys);// 查询未被删除的任务
				int total = locmissMgr.getTotalCountByHQL("select count(model)"+queryStr,keys);// 未被删除的任务总数

				JSONArray options = new JSONArray();
				Timestamp today = new Timestamp(System.currentTimeMillis());// 取当前时间
				for (LocaleMission loc : result) {
					// System.out.println("loc.getStatus():"+loc.getStatus());
					
						JSONObject option = new JSONObject();
						option.put("Id", loc.getId());
						option.put("Name", loc.getCustomerName());
						option.put("Code", loc.getCode());
						option.put("CustomerId", loc.getCustomer().getId());
						//option.put("CreatorName", loc.getSysUserByCreatorId().getName());
						option.put("CreateDate",DateTimeFormatUtil.DateTimeFormat.format(loc.getCreateTime())+" "+loc.getSysUserByCreatorId().getName());
						option.put("ZipCode", loc.getZipCode());
						option.put("Brief", loc.getBrief());
						option.put("RegionId", loc.getRegion().getId());
						option.put("Region", loc.getRegion().getName());
						option.put("Address", loc.getAddress());
						option.put("Remark", loc.getRemark()==null?"":loc.getRemark());
						option.put("Tel", loc.getTel());
						option.put("ZipCode", loc.getZipCode());
						option.put("Status", loc.getStatus());
						option.put("Contactor", loc.getContactor());
						option.put("ContactorTel", loc.getContactorTel());
						option.put("Department", loc.getDepartment());
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						option.put("ExactTime", loc.getExactTime()==null?"":DateTimeFormatUtil.DateFormat.format(loc.getExactTime()));
						//option.put("MissionDesc", loc.getMissionDesc());
						option.put("ModificatorDate", loc.getModificatorDate()==null?"":DateTimeFormatUtil.DateFormat.format(loc.getModificatorDate()));
						option.put("ModificatorId", loc.getSysUserByModificatorId().getName());
						option.put("SiteManagerName", loc.getSysUserBySiteManagerId()==null?"":loc.getSysUserBySiteManagerId().getName());
						option.put("SiteManagerId", loc.getSysUserBySiteManagerId()==null?"":loc.getSysUserBySiteManagerId().getId());
						//找现场业务下面的各个器具指派人
						String staffs="";
						String queryString="from LocaleApplianceItem as a where a.localeMission.id = ?";
						List<SysUser>  workStaffs= locAppItemMgr.findByHQL("select distinct a.sysUser "+queryString, loc.getId());
						if(!workStaffs.isEmpty()){						
							for (SysUser user : workStaffs) {
								
								staffs = staffs + user.getName()+";"; 		
								
							}
						}
						if(loc.getSysUserBySiteManagerId()!=null){
							if(staffs.indexOf(loc.getSysUserBySiteManagerId().getName()+";")<0){
								staffs = staffs + loc.getSysUserBySiteManagerId().getName()+";"; 		
							}
						}
						//找现场业务下面的各个器具信息
						String missionDesc="";
						List<LocaleApplianceItem>  locAppList= locAppItemMgr.findByHQL(queryString, loc.getId());
						if(!locAppList.isEmpty()){						
							for (LocaleApplianceItem locApp : locAppList) {
								missionDesc = missionDesc + locApp.getApplianceName()+"  "+locApp.getQuantity()+"<br/>"; 							
							}
						}
						
						option.put("Staffs", staffs);
						option.put("VehicleLisences", loc.getVehicleLisences()==null?"":loc.getVehicleLisences());
						option.put("MissionDesc", missionDesc);
						
						//option.put("Status", loc.getStatus());
						option.put("Tag", "0");//已核定
						option.put("Feedback", loc.getFeedback()==null?"":loc.getFeedback());//反馈
						option.put("CheckDate", loc.getCheckDate()==null?"":DateTimeFormatUtil.DateFormat.format(loc.getCheckDate()));//核定时间
						option.put("TentativeDate", loc.getTentativeDate()==null?"":DateTimeFormatUtil.DateFormat.format(loc.getTentativeDate()));
						if(loc.getStatus()==4&&loc.getTentativeDate()!=null){  //未核定
							if(loc.getTentativeDate().after(today)){//已到暂定日期可是未安排
								option.put("Tag", "1");//未核定；；；已到暂定日期可是未安排
							}else{
								option.put("Tag", "2");//未核定；；；未到暂定日期
								
							}
								
						}	
						options.put(option);
				}
				res.put("total", total);
				res.put("rows", options);
			} catch (Exception e) {
				
				try {
					res.put("total", 0);
					res.put("rows", new JSONArray());
				} catch (JSONException ex) {
				}
				if (e.getClass() == java.lang.Exception.class) { // 自定义的消息
					log.debug("exception in LocaleMissionServlet-->case 8", e);
				} else {
					log.error("error in LocaleMissionServlet-->case 8", e);
				}
			} finally {
				resp.setContentType("text/json;charset=utf-8");
				
				resp.getWriter().write(res.toString());
			}

			break;
		}
		case 9: // 现场任务反馈操作
			JSONObject retJSON9 = new JSONObject();
			try{
				int id = Integer.parseInt(req.getParameter("Id"));
				String Feedback = req.getParameter("Feedback");
				LocaleMission localmission = locmissMgr.findById(id);
			
				SysUser loginuser=(SysUser) req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser);
				Timestamp today = new Timestamp(System.currentTimeMillis());// 取当前时间
				String loginname = (loginuser==null?"":loginuser.getName());
				String now=DateTimeFormatUtil.DateTimeFormat.format(today);
				
				Feedback=String.format("%s (%s %s)", Feedback,loginname,now);			
				localmission.setFeedback(Feedback);
				//localmission.setStatus(2);// 将指定Id任务状态置为2，表示已完工
				
				if(locmissMgr.update(localmission)){
					retJSON9.put("IsOK", true);
				}else{
					throw new Exception("更新数据库失败！");
				}
			}catch(Exception e){
				try{
					retJSON9.put("IsOK", false);
					retJSON9.put("msg", String.format("处理失败！错误信息：%s",(e != null && e.getMessage() != null)?e.getMessage() : "无"));
				}catch(Exception ex){}
				
				if (e.getClass() == java.lang.Exception.class) { // 自定义的消息
					log.debug("exception in LocaleMissionServlet-->case 9", e);
				} else {
					log.error("error in LocaleMissionServlet-->case 9", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON9.toString());
			}
			break;	
		case 10: // 其他交通方式，车辆信息里面添“来车”“自行车”等；
			JSONObject retJSON10 = new JSONObject();
			try{
				int id = Integer.parseInt(req.getParameter("id"));
			
				String ExactTime =req.getParameter("ExactTime");
				String Remark =req.getParameter("Remark");
				String QTway =req.getParameter("QTway");
				
				Timestamp extime=new java.sql.Timestamp(DateTimeFormatUtil.DateFormat.parse(ExactTime).getTime());	
				LocaleMission localmission = locmissMgr.findById(id);
				if(localmission.getStatus()==2||localmission.getStatus()==3){
					throw new Exception("该现场检测业务'已完工'或‘已注销’,不能修改！");
				}
				if(QTway!=null&&QTway.length()>0){
					localmission.setVehicleLisences(URLDecoder.decode(QTway.trim(),"UTF-8"));
				}
				localmission.setStatus(1);// 将指定Id任务状态置为1，表示已分配
				localmission.setExactTime(extime);// 
				if(Remark!=null&&Remark.length()>0){
					localmission.setRemark(URLDecoder.decode(Remark,"UTF-8"));// 
				}
				//List<VehicleMission> veMiList = (new VehicleMissionManager()).findByVarProperty(new KeyValueWithOperator("localeMission",localmission,"="));
				
				if(!locmissMgr.update(localmission))
					throw new Exception("更新数据库失败！");;
				retJSON10.put("IsOK", true);
			}catch(Exception e){
				try{
					retJSON10.put("IsOK", false);
					retJSON10.put("msg", String.format("处理失败！错误信息：%s",(e != null && e.getMessage() != null)?e.getMessage() : "无"));
				}catch(Exception ex){}
				
				if (e.getClass() == java.lang.Exception.class) { // 自定义的消息
					log.debug("exception in LocaleMissionServlet-->case 10", e);
				} else {
					log.error("error in LocaleMissionServlet-->case 10", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON10.toString());
			}
			break;
	   	case 11:// 查询现场业务的器具信息
		
			JSONObject res = new JSONObject();
			try {
				String Id = req.getParameter("Id");
				if(Id!=null&&Id.length()>0){
				    
					List<LocaleApplianceItem> result = locAppItemMgr.findByVarProperty(new KeyValueWithOperator("localeMission.id",Integer.parseInt(Id),"="));// 
					int total = locAppItemMgr.getTotalCount(new KeyValueWithOperator("localeMission.id",Integer.parseInt(Id),"="));// 
					ApplianceSpeciesManager speciesMgr = new ApplianceSpeciesManager();
					ApplianceStandardNameManager standardNameMgr = new ApplianceStandardNameManager();
					JSONArray options = new JSONArray();
					if(result != null && result.size() > 0){
						for (LocaleApplianceItem loc : result) {
							
								JSONObject option = new JSONObject();
								option.put("Id", loc.getId());
								if(loc.getSpeciesType()!=null){
									option.put("ApplianceSpeciesId", loc.getApplianceSpeciesId());	//器具分类Id(或器具标准名称ID)
									if(loc.getSpeciesType()){	//器具授权（分类）名称
										option.put("SpeciesType", "1");	//器具分类类型
										ApplianceSpecies spe = speciesMgr.findById(loc.getApplianceSpeciesId());
										if(spe != null){
											option.put("ApplianceSpeciesName", spe.getName());
											option.put("ApplianceSpeciesNameStatus", spe.getStatus());
										}else{
											continue;
										}
									}else{	//器具标准名称
										option.put("SpeciesType", "0");
										ApplianceStandardName stName = standardNameMgr.findById(loc.getApplianceSpeciesId());
										if(stName != null){
											option.put("ApplianceSpeciesName", stName.getName());
											option.put("ApplianceSpeciesNameStatus", stName.getStatus());
										}else{
											continue;
										}
									}
									option.put("ApplianceName", loc.getApplianceName()==null?"":loc.getApplianceName());	//器具名称（常用名称）
								}else{
									option.put("ApplianceSpeciesId", "");	//器具分类Id(或器具标准名称ID)
									option.put("ApplianceSpeciesName", "");	//器具分类Id(或器具标准名称ID)
									option.put("ApplianceName", "");	//
									option.put("SpeciesType", "");	//器具分类Id(或器具标准名称ID)
									option.put("ApplianceSpeciesNameStatus", "");	//器具分类Id(或器具标准名称ID)
								}
								option.put("ApplianceCode", loc.getAppFactoryCode()==null?"":loc.getAppFactoryCode());	//出厂编号
								option.put("AppManageCode", loc.getAppManageCode()==null?"":loc.getAppManageCode());	//管理编号
								option.put("Model", loc.getApplianceName()==null?"":loc.getModel()==null?"":loc.getModel());	//型号规格
								option.put("Range", loc.getRange()==null?"":loc.getRange());		//测量范围
								option.put("Accuracy", loc.getAccuracy()==null?"":loc.getAccuracy());	//精度等级
								option.put("Manufacturer", loc.getManufacturer()==null?"":loc.getManufacturer());	//制造厂商
								option.put("ReportType", loc.getCertType()==null?"":loc.getCertType());	//报告形式
								option.put("TestFee", loc.getTestCost()==null?"":loc.getTestCost());	//检测费
								option.put("RepairFee", loc.getRepairCost()==null?"":loc.getRepairCost());	//检测费
								option.put("MaterialFee", loc.getMaterialCost()==null?"":loc.getMaterialCost());	//检测费
								option.put("Quantity", loc.getQuantity()==null?"":loc.getQuantity());	//台/件数
								option.put("WorkStaff", loc.getSysUser()==null?"":loc.getSysUser().getName());	//派定人
								option.put("AssistStaff", loc.getAssistStaff()==null?"":loc.getAssistStaff());	//
								option.put("Remark", loc.getRemark()==null?"":loc.getRemark());	//
								
								
								options.put(option);
						}
						res.put("total", total);
						res.put("rows", options);
					}else{
						res.put("total", 0);
						res.put("rows", new JSONArray());
					}
				}else{
					throw new Exception("Id空");
				}
			} catch (Exception e) {
				
				try {
					res.put("total", 0);
					res.put("rows", new JSONArray());
				} catch (JSONException ex) {
				}
				if (e.getClass() == java.lang.Exception.class) { // 自定义的消息
					log.debug("exception in LocaleMissionServlet-->case 11", e);
				} else {
					log.error("error in LocaleMissionServlet-->case 11", e);
				}
			} finally {
				resp.setContentType("text/json;charset=utf-8");
				
				resp.getWriter().write(res.toString());
			}

			break;
	   	case 12: //查找委托单位的现场任务的历史器具信息
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
					//System.out.println(cusName);
					List<KeyValueWithOperator> condList = new ArrayList<KeyValueWithOperator>();
					
					condList.add(new KeyValueWithOperator("localeMission.customerName",cusName,"="));
					if(BeginDate != null && BeginDate.length() > 0){
						condList.add(new KeyValueWithOperator("localeMission.exactTime", new Timestamp(java.sql.Date.valueOf(BeginDate).getTime()), ">="));
					}
					if(EndDate != null && EndDate.length() > 0){
						condList.add(new KeyValueWithOperator("localeMission.exactTime", new Timestamp(java.sql.Date.valueOf(EndDate).getTime()), "<"));
					}
					if(ApplianceName != null && ApplianceName.trim().length() > 0 ){
						String appName = URLDecoder.decode(ApplianceName.trim(), "UTF-8");
						condList.add(new KeyValueWithOperator("applianceName", "%"+appName+"%", "like"));
					}
					totalSize = locAppItemMgr.getTotalCount(condList);
					List<LocaleApplianceItem> retList = locAppItemMgr.findPagedAllBySort(page, rows, "localeMission.exactTime", false, condList);
					if(retList != null && retList.size() > 0){
						ApplianceSpeciesManager speciesMgr = new ApplianceSpeciesManager();
						ApplianceStandardNameManager standardNameMgr = new ApplianceStandardNameManager();
					
						SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
						for(LocaleApplianceItem locApp : retList){
							JSONObject jsonObj = new JSONObject();
							jsonObj.put("CustomerName", locApp.getLocaleMission().getCustomerName());
							
							if(locApp.getSpeciesType()!=null){
								jsonObj.put("ApplianceSpeciesId", locApp.getApplianceSpeciesId());	//器具分类Id(或器具标准名称ID)
							
								if(locApp.getSpeciesType()){	//器具授权（分类）名称
									jsonObj.put("SpeciesType", 1);	//器具分类类型
									ApplianceSpecies spe = speciesMgr.findById(locApp.getApplianceSpeciesId());
									if(spe != null){
										jsonObj.put("ApplianceSpeciesName", spe.getName());
										jsonObj.put("ApplianceSpeciesNameStatus", spe.getStatus());
									}else{
										continue;
									}
								}else{	//器具标准名称
									jsonObj.put("SpeciesType", 0);
									ApplianceStandardName stName = standardNameMgr.findById(locApp.getApplianceSpeciesId());
									if(stName != null){
										jsonObj.put("ApplianceSpeciesName", stName.getName());
										jsonObj.put("ApplianceSpeciesNameStatus", stName.getStatus());
									}else{
										continue;
									}
								}
								jsonObj.put("ApplianceName", locApp.getApplianceName()==null?"":locApp.getApplianceName());	//器具名称（常用名称）
							}else{
								jsonObj.put("ApplianceSpeciesId", "");	//器具分类Id(或器具标准名称ID)
								jsonObj.put("ApplianceSpeciesName", "");	//器具分类Id(或器具标准名称ID)
								jsonObj.put("ApplianceName", "");	//
								jsonObj.put("SpeciesType", "");	//器具分类Id(或器具标准名称ID)
								jsonObj.put("ApplianceSpeciesNameStatus", "");	//器具分类Id(或器具标准名称ID)
							}
							//jsonObj.put("ApplianceName", locApp.getApplianceName()==null?"":locApp.getApplianceName());	//器具名称（常用名称）
							jsonObj.put("ApplianceCode", locApp.getAppFactoryCode()==null?"":locApp.getAppFactoryCode());	//出厂编号
							jsonObj.put("AppManageCode", locApp.getAppManageCode()==null?"":locApp.getAppManageCode());	//管理编号
							jsonObj.put("Model", locApp.getApplianceName()==null?"":locApp.getModel()==null?"":locApp.getModel());	//型号规格
							jsonObj.put("Range", locApp.getRange()==null?"":locApp.getRange());		//测量范围
							jsonObj.put("Accuracy", locApp.getAccuracy()==null?"":locApp.getAccuracy());	//精度等级
							jsonObj.put("Manufacturer", locApp.getManufacturer()==null?"":locApp.getManufacturer());	//制造厂商
							jsonObj.put("ReportType", locApp.getCertType()==null?"":locApp.getCertType());	//报告形式
							jsonObj.put("TestFee", locApp.getTestCost()==null?"":locApp.getTestCost());	//检测费
							jsonObj.put("Quantity", locApp.getQuantity()==null?"":locApp.getQuantity());	//台/件数
							jsonObj.put("WorkStaff", locApp.getSysUser()==null?"":locApp.getSysUser().getName());	//派定人
							jsonObj.put("AssistStaff", locApp.getAssistStaff()==null?"":locApp.getAssistStaff());	//
							jsonObj.put("Remark", locApp.getRemark()==null?"":locApp.getRemark());	//
							jsonArray.put(jsonObj);	
						}
					}
				}else{
					throw new Exception("委托单位名称为空！");
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
					log.debug("exception in LocaleMissionServlet-->case 12", e);
				}else{
					log.error("error in LocaleMissionServlet-->case 12", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				//System.out.println(retJSON.toString());
				resp.getWriter().write(retJSON.toString());
			}
			break;
	   	case 13://查询现场任务条目（用于打印）
			JSONObject resJson7 = new JSONObject();
			try{
				String localeMissionId = req.getParameter("localeMissionId"); 
				List<KeyValueWithOperator> quoList = new ArrayList<KeyValueWithOperator>();
				List<LocaleApplianceItem> locAppItemList;
				CustomerManager cusMgr=new CustomerManager();
				CommissionSheetManager comsheetMgr=new CommissionSheetManager();
				List<CommissionSheet> comsheetList=new ArrayList<CommissionSheet>();
				int total5;
				if(localeMissionId == null)
				{
					throw new Exception("任务书号无效！");
				}
				else
				{
					locAppItemList = locAppItemMgr.findByVarProperty(new KeyValueWithOperator("localeMission.id",Integer.parseInt(localeMissionId),"="));
					total5 = locAppItemMgr.getTotalCount(new KeyValueWithOperator("localeMission.id",Integer.parseInt(localeMissionId),"="));
				}
				JSONArray optionsq = new JSONArray();
				int id=1;
				
				
				for (LocaleApplianceItem locAppItem : locAppItemList) {
					JSONObject option = new JSONObject();
					String applianceInfo="";
					if(locAppItem.getSpeciesType()!=null){
						option.put("Id", id++);
						if(locAppItem.getApplianceName()==null||locAppItem.getApplianceName().length()==0){
							if(!locAppItem.getSpeciesType()){
								ApplianceStandardNameManager standardNameMgr = new ApplianceStandardNameManager();
								ApplianceStandardName stName = standardNameMgr.findById(locAppItem.getApplianceSpeciesId());
								if(stName != null){
									option.put("ApplianceName", stName.getName());								
								}else{
									continue;
								}
							}
						}else{
							option.put("ApplianceName", locAppItem.getApplianceName());
						}
						
						option.put("Quantity", locAppItem.getQuantity());
						
						comsheetList=comsheetMgr.findByVarProperty(new KeyValueWithOperator("localeApplianceItemId", locAppItem.getId(), "="),
									new KeyValueWithOperator("status",FlagUtil.CommissionSheetStatus.Status_YiZhuXiao, "<>"));
						if(comsheetList!=null&&comsheetList.size()>0){
							applianceInfo = applianceInfo + comsheetList.get(0).getCode()+"／";
							
						}
						
						applianceInfo = applianceInfo + option.getString("ApplianceName");
						option.put("Model",locAppItem.getModel()==null?"":locAppItem.getModel());
						option.put("Accuracy", locAppItem.getAccuracy()==null?"":locAppItem.getAccuracy());
						option.put("Range", locAppItem.getRange()==null?"":locAppItem.getRange());						
						option.put("AppFactoryCode", locAppItem.getAppFactoryCode()==null?"":locAppItem.getAppFactoryCode());
						option.put("AppManageCode", locAppItem.getAppManageCode()==null?"":locAppItem.getAppManageCode());
						option.put("Manufacturer", locAppItem.getManufacturer()==null?"":locAppItem.getManufacturer());
						if(option.getString("Model").length()>0){
							applianceInfo = applianceInfo + "／" + option.getString("Model");
						}						
						if(option.getString("Range").length()>0){
							applianceInfo = applianceInfo + "／" + option.getString("Range");
						}
						if(option.getString("Accuracy").length()>0){
							applianceInfo = applianceInfo + "／" + option.getString("Accuracy");
						}
						if(option.getString("AppFactoryCode").length()>0){
							applianceInfo = applianceInfo + "／" + option.getString("AppFactoryCode");
						}
						if(option.getString("AppManageCode").length()>0){
							applianceInfo = applianceInfo + "／" + option.getString("AppManageCode");
						}					
						option.put("applianceInfo", applianceInfo);
						String CertType="";
						if(locAppItem.getCertType()!=null&&locAppItem.getCertType().length()>0){
							if(locAppItem.getCertType().equals("1")){
								CertType="检定";
							}
							else if(locAppItem.getCertType().equals("2")){
								CertType="校准";
							}
							else if(locAppItem.getCertType().equals("3")){
								CertType="检测";
							}
							else if(locAppItem.getCertType().equals("4")){
								CertType="检验";
							}
						}
						option.put("CertType", CertType);					
						option.put("TestCost",locAppItem.getTestCost()==null?"":locAppItem.getTestCost());
						option.put("RepairCost",locAppItem.getRepairCost()==null?"":locAppItem.getRepairCost());
						option.put("MaterialCost",locAppItem.getMaterialCost()==null?"":locAppItem.getMaterialCost());
						option.put("WorkStaff",locAppItem.getSysUser()==null?"":locAppItem.getSysUser().getName());
											
						optionsq.put(option);
					}
				}
				
//				for (int i=0;i<21;i++) {   //为了调试，空21行
//					JSONObject option = new JSONObject();
//					option.put("Id", id++);
//					option.put("ApplianceName", " ");	
//					option.put("applianceInfo", "");
//					option.put("Model"," ");
//					option.put("Accuracy", " ");
//					option.put("Range", " ");
//					option.put("Quantity", " ");
//					option.put("AppFactoryCode","");
//					option.put("AppManageCode", "");
//					option.put("Manufacturer", "");
//					option.put("CertType", " ");
//					option.put("RepairCost","");
//					option.put("MaterialCost","");
//					option.put("TestCost","");
//					option.put("WorkStaff","");
//										
//					optionsq.put(option);
//				}
				
				if(id<=10){
					int temp=id;
					for (int i=0;i<(11-temp%10);i++) {   //首页10行
						JSONObject option = new JSONObject();
						option.put("Id", id++);
						option.put("ApplianceName", " ");	
						option.put("applianceInfo", "");
						option.put("Model"," ");
						option.put("Accuracy", " ");
						option.put("Range", " ");
						option.put("Quantity", " ");
						option.put("AppFactoryCode","");
						option.put("AppManageCode", "");
						option.put("Manufacturer", "");
						option.put("CertType", " ");									
						option.put("TestCost","");
						option.put("RepairCost","");
						option.put("MaterialCost","");
						option.put("WorkStaff","");
											
						optionsq.put(option);
					}					
				}
			
				else{
					int temp=id-10;
					for (int i=0;i<(23-temp%22);i++) {   //每页20行
						JSONObject option = new JSONObject();
						option.put("Id", id++);
						option.put("ApplianceName", " ");
						option.put("applianceInfo", "");
						option.put("Model"," ");
						option.put("Accuracy", " ");
						option.put("Range", " ");
						option.put("Quantity", " ");
						option.put("AppFactoryCode","");
						option.put("AppManageCode", "");
						option.put("Manufacturer", "");
						option.put("CertType", " ");									
						option.put("TestCost","");
						option.put("RepairCost","");
						option.put("MaterialCost","");
						option.put("WorkStaff","");
											
						optionsq.put(option);
					}
				}
				resJson7.put("total", id);
				resJson7.put("rows", optionsq);

				
				
				LocaleMission loc=locmissMgr.findById(Integer.parseInt(localeMissionId));
				resJson7.put("CustomerName", loc.getCustomerName());
				resJson7.put("Code", loc.getCode());
				resJson7.put("Department", loc.getDepartment());
				resJson7.put("Address", loc.getAddress());
				resJson7.put("ZipCode", loc.getZipCode());
				resJson7.put("Contactor", loc.getContactor());
				resJson7.put("ContactorTel", loc.getContactorTel()==null?"":loc.getContactorTel());
				resJson7.put("SiteManager", loc.getSysUserBySiteManagerId()==null?"":loc.getSysUserBySiteManagerId().getName());
				
				
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				if(loc.getExactTime()!=null){
					resJson7.put("ExactTime", sf.format(loc.getExactTime()));//报价时间\
				}else{
					resJson7.put("ExactTime", "");//报价时间\
				}
				resJson7.put("IsOK", true);
				req.getSession().setAttribute("AppItemsList", resJson7);
			
				resp.sendRedirect("/jlyw/TaskManage/LocaleMissionPrint.jsp");
			}catch(Exception e){
				try {
					resJson7.put("total", 0);
					resJson7.put("rows", new JSONArray());

					try {
						resJson7.put("IsOK", false);
						resJson7.put("msg", String.format("查找现场委托书器具条目失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
				} catch (JSONException e1) {}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in LocaleMissionServlet-->case 13", e);
				}else{
					log.error("error in LocaleMissionServlet-->case 13", e);
				}
				req.getSession().setAttribute("AppItemsList", resJson7);
				
				resp.sendRedirect("/jlyw/TaskManage/LocaleMissionPrint.jsp");
			}
			break;	
	   	case 14: // 现场业务书导入到现场委托单时，根据委托单位名查询现场委托书号
			JSONArray jsonArray = new JSONArray();
			try {
				String queryNameStr = req.getParameter("QueryName");	//查询的 “名称”
				
				if(queryNameStr != null && queryNameStr.trim().length() > 0){
					String queryName =  new String(queryNameStr.trim().getBytes("ISO-8859-1"), "UTF-8");	//解决URL传递中文乱码问题
					
					List<LocaleMission> locList = locmissMgr.findPagedAllBySort(0,50,"tentativeDate",false,new KeyValueWithOperator("customerName", queryName, "="),new KeyValueWithOperator("status", 3, "<>"));//
					for(LocaleMission loc : locList){
						JSONObject jsonObj = new JSONObject();						
						jsonObj.put("code", loc.getCode());	//现场委托书号	
						jsonObj.put("Id", loc.getId());	//现场任务ID		
						jsonObj.put("CustomerName", loc.getCustomerName());					
						jsonObj.put("CustomerAddress", loc.getAddress());
						jsonObj.put("CustomerTel", loc.getTel());
						jsonObj.put("CustomerZipCode", loc.getZipCode());
						jsonObj.put("LocaleCommissionDate",loc.getExactTime()==null?"":DateTimeFormatUtil.DateFormat.format(loc.getExactTime()));
						/*if(loc.getExactTime() != null){
							Timestamp eTime = loc.getExactTime();
							eTime.setDate(eTime.getDate() + 7);
							jsonObj.put("PromiseDate",DateTimeFormatUtil.DateFormat.format(eTime));
						}else{
							jsonObj.put("PromiseDate","");
						}*/
						jsonObj.put("ContactPerson", loc.getContactor());
						jsonObj.put("ContactorTel", loc.getContactorTel()==null?"":loc.getContactorTel());
						jsonObj.put("LocaleStaffId", loc.getSysUserBySiteManagerId()==null?"":loc.getSysUserBySiteManagerId().getName());
						jsonArray.put(jsonObj);
					}
					
							
				}
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in LocaleMissionServlet-->case 14", e);
				}else{
					log.error("error in LocaleMissionServlet-->case 14", e);
				}
				
			}finally{
				resp.setContentType(SysStringUtil.ResponseContentType.Type_EasyUICombobox);
				resp.getWriter().write(jsonArray.toString());
			}
			break;
	   	case 15: // 现场委托书号的模糊查询
			JSONArray jsonarray = new JSONArray();
			try {
				String queryNameStr = req.getParameter("QueryName");	//查询的 “名称”
				
				if(queryNameStr != null && queryNameStr.trim().length() > 0){
					String queryName =  new String(queryNameStr.trim().getBytes("ISO-8859-1"), "UTF-8");	//解决URL传递中文乱码问题
			
					List<LocaleMission> locList = locmissMgr.findPagedAllBySort(0,50,"tentativeDate",false,new KeyValueWithOperator("code", "%"+queryName+"%", "like"),new KeyValueWithOperator("status", 3, "<>"));//非注销
					for(LocaleMission loc : locList){
						JSONObject jsonObj = new JSONObject();						
						jsonObj.put("code", loc.getCode());	//现场委托书号	
						jsonObj.put("Id", loc.getId());	//现场任务ID		
						jsonObj.put("CustomerName", loc.getCustomerName());					
						jsonObj.put("CustomerAddress", loc.getAddress());
						jsonObj.put("LocaleCommissionDate",loc.getExactTime()==null?"":DateTimeFormatUtil.DateFormat.format(loc.getExactTime()));
						if(loc.getExactTime() != null){
							Timestamp eTime = loc.getExactTime();
							eTime.setDate(eTime.getDate() + 7);
							jsonObj.put("PromiseDate",DateTimeFormatUtil.DateFormat.format(eTime));
						}else{
							jsonObj.put("PromiseDate","");
						}
						jsonObj.put("CustomerTel", loc.getTel());
						jsonObj.put("CustomerZipCode", loc.getZipCode());
						jsonObj.put("ContactPerson", loc.getContactor());
						jsonObj.put("ContactorTel", loc.getContactorTel()==null?"":loc.getContactorTel());
						jsonObj.put("LocaleStaffId", loc.getSysUserBySiteManagerId()==null?"":loc.getSysUserBySiteManagerId().getName());
						jsonarray.put(jsonObj);
					}
					
							
				}
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in LocaleMissionServlet-->case 15", e);
				}else{
					log.error("error in LocaleMissionServlet-->case 15", e);
				}
				
			}finally{
				resp.setContentType(SysStringUtil.ResponseContentType.Type_EasyUICombobox);
				resp.getWriter().write(jsonarray.toString());
			}
			break;
	   	case 16: // 核定现场任务委托单
			JSONObject retObj16 = new JSONObject();
			try {
				String Id = req.getParameter("Id").trim();
				String CustomerName = req.getParameter("Name");
				int CustomerId=Integer.parseInt(req.getParameter("CustomerId"));
				String ZipCode = req.getParameter("ZipCode");
				String Department = req.getParameter("Department");
				String Address = req.getParameter("Address");
				String Tel = req.getParameter("Tel");
				String ContactorTel = req.getParameter("ContactorTel");
				String TentativeDate = req.getParameter("TentativeDate");
				String CheckDate = req.getParameter("CheckDate");//核定日期
				
				String SiteManagerId = req.getParameter("SiteManagerId");
			
				String Contactor = req.getParameter("Contactor");
				String RegionId = req.getParameter("RegionId");
				String Remark = req.getParameter("Remark");
				String Appliances = req.getParameter("Appliances").trim();	//检验的器具
				
				JSONArray appliancesArray = new JSONArray(Appliances);	//检查的器具
				
				int lid = Integer.parseInt(Id);// 获取Id
				LocaleMission localmission = locmissMgr.findById(lid);// 根据查询现场任务
				if(localmission == null){
					throw new Exception("该现场检测业务不存在！");
				}
				if(localmission.getStatus()==2||localmission.getStatus()==3){
					throw new Exception("该现场检测业务'已完工'或‘已注销’,不能修改！");
				}
				
				Timestamp ts;
                if(TentativeDate==null||TentativeDate.length()==0){
				   ts = null;	//暂定日期
                }else{
			       ts = new Timestamp(DateTimeFormatUtil.DateFormat.parse(TentativeDate).getTime());	//暂定日期
                }
                Timestamp now = new Timestamp(System.currentTimeMillis());// 取当前时间
                
                localmission.setCustomerName(CustomerName==null?"":CustomerName);
				localmission.setAddress(Address==null?"":Address);
				localmission.setZipCode(ZipCode==null?"":ZipCode);
				localmission.setContactor(Contactor==null?"":Contactor);
				localmission.setTel(Tel==null?"":Tel);
				localmission.setContactorTel(ContactorTel==null?"":ContactorTel);
				//localmission.setMissionDesc(MissionDesc);
				localmission.setDepartment(Department);
				//localmission.setStaffs(staffs.toString());
				localmission.setTentativeDate(ts);
				Region region = new Region();
				region.setId(Integer.parseInt(RegionId));
				localmission.setRegion(region);
				localmission.setRemark(Remark);
				/************更新委托单位信息***********/
				Customer cus=(new CustomerManager()).findById(CustomerId);
				if(CustomerName!=null&&CustomerName.length()>0){
					cus.setName(CustomerName);
				}
				if(Address!=null&&Address.length()>0){
					cus.setAddress(Address);
				}
				if(Tel!=null&&Tel.length()>0){
					cus.setTel(Tel);
				}
				if(ZipCode!=null&&ZipCode.length()>0){
					cus.setZipCode(ZipCode);
				}
				if(!(new CustomerManager()).update(cus)){
					throw new Exception("更新委托单位失败！");				
				}
				/************更新委托单位联系人信息***********/	
				CustomerContactorManager cusConMgr = new CustomerContactorManager();
				List<CustomerContactor> cusConList = cusConMgr.findByVarProperty(new KeyValueWithOperator("customerId", CustomerId, "="), new KeyValueWithOperator("name", Contactor, "="));
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
						if(!cusConMgr.update(c))
							throw new Exception("更新单位联系人失败！");		
					}else{
						CustomerContactor c = new CustomerContactor();
						Customer a=new Customer();
						a.setId(CustomerId);
						c.setCustomer(a);
						c.setName(Contactor);
						c.setCellphone1(ContactorTel);
						c.setLastUse(now);
						c.setCount(1);
						if(!cusConMgr.save(c))
							throw new Exception("更新单位联系人失败！");		
					}
				}
								
				if(CheckDate!=null&&CheckDate.length()>0){  //核定
					Timestamp CheckTime = new Timestamp(DateTimeFormatUtil.DateFormat.parse(CheckDate).getTime());
					localmission.setCheckDate(CheckTime);
					if(localmission.getStatus()==4){
						localmission.setStatus(5);//已核定
					}
				}
				
				SysUser newUser = new SysUser();
				if(SiteManagerId!=null&&SiteManagerId.length()>0){  //负责人
					newUser.setId(Integer.parseInt(SiteManagerId));
					localmission.setSysUserBySiteManagerId(newUser);	
				}else{
					localmission.setSysUserBySiteManagerId(null);	
				}
				
				
				localmission.setModificatorDate(now);// 记录时间为当前时间
				localmission.setSysUserByModificatorId((SysUser) req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser));
				
				ApplianceSpeciesManager speciesMgr = new ApplianceSpeciesManager();	//器具分类管理Mgr
				ApplianceStandardNameManager sNameMgr = new ApplianceStandardNameManager();	//器具标准名称管理Mgr
				AppliancePopularNameManager popNameMgr = new AppliancePopularNameManager();	//器具常用名称管理Mgr
				QualificationManager qualMgr = new QualificationManager();	//检测人员资质管理Mgr
				ApplianceManufacturerManager mafMgr = new ApplianceManufacturerManager();	//制造厂管理Mgr
				
				List<LocaleApplianceItem> localeAppItemList = new ArrayList<LocaleApplianceItem>();	//现场业务中要添加的器具
				List<SysUser> alloteeList = new ArrayList<SysUser>();	//委托单列表对应的派定人：
				List<Integer> qualList = new ArrayList<Integer>();
			
				/********************   添加器具信息   ******************/
				for(int i = 0; i < appliancesArray.length(); i++){
					JSONObject jsonObj = appliancesArray.getJSONObject(i);
					LocaleApplianceItem localeAppItem = new LocaleApplianceItem();
					
					/**********************   添加器具信息    ************************/
					String SpeciesType = jsonObj.get("SpeciesType").toString();	//器具分类类型
					String ApplianceSpeciesId = jsonObj.get("ApplianceSpeciesId").toString();	//器具类别ID/标准名称ID
					String ApplianceName = jsonObj.getString("ApplianceName");	//器具名称
					String Manufacturer= jsonObj.getString("Manufacturer");	//制造厂
					if(SpeciesType!=null&&SpeciesType.length()>0){
						if(Integer.parseInt(SpeciesType) == 0){	//0:标准名称；1：分类名称
							localeAppItem.setSpeciesType(false);	
							String stdName = sNameMgr.findById(Integer.parseInt(ApplianceSpeciesId)).getName();
							if(ApplianceName == null || ApplianceName.trim().length() == 0){
								ApplianceName = stdName;	//器具名称未填写，则默认为标准名称或分类名称
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
							localeAppItem.setSpeciesType(true);	
							if(ApplianceName == null || ApplianceName.trim().length() == 0){
								ApplianceName = speciesMgr.findById(Integer.parseInt(ApplianceSpeciesId)).getName();;	//器具名称未填写，则默认为标准名称或分类名称
							}
						}
						localeAppItem.setApplianceSpeciesId(Integer.parseInt(ApplianceSpeciesId));					
						localeAppItem.setApplianceName(ApplianceName);	//存器具名称		
					}
					localeAppItem.setAppFactoryCode(jsonObj.getString("ApplianceCode"));		//出厂编号
					localeAppItem.setAppManageCode(jsonObj.getString("AppManageCode"));		//管理编号
					localeAppItem.setModel(jsonObj.getString("Model"));		//型号规格
					localeAppItem.setRange(jsonObj.getString("Range"));		//测量范围
					localeAppItem.setAccuracy(jsonObj.getString("Accuracy"));	//精度等级
					localeAppItem.setManufacturer(jsonObj.getString("Manufacturer"));		//制造厂商
					localeAppItem.setCertType(jsonObj.getString("ReportType"));	//报告形式
					if(jsonObj.has("TestFee")&&jsonObj.getString("TestFee").length()>0&&jsonObj.getString("TestFee")!=null){
						localeAppItem.setTestCost(Double.valueOf(jsonObj.getString("TestFee")));
					}
					if(jsonObj.has("RepairFee")&&jsonObj.getString("RepairFee").length()>0&&jsonObj.getString("RepairFee")!=null){
						localeAppItem.setRepairCost(Double.valueOf(jsonObj.getString("RepairFee")));
					}
					if(jsonObj.has("MaterialFee")&&jsonObj.getString("MaterialFee").length()>0&&jsonObj.getString("MaterialFee")!=null){
						localeAppItem.setMaterialCost(Double.valueOf(jsonObj.getString("MaterialFee")));
					}
					localeAppItem.setQuantity(Integer.parseInt(jsonObj.get("Quantity").toString()));//台件数	
					if(jsonObj.has("AssistStaff")&&jsonObj.getString("AssistStaff")!=null&&jsonObj.getString("AssistStaff").trim().length()>0)
						localeAppItem.setAssistStaff(jsonObj.getString("AssistStaff"));	//存辅助派定人或替代人		
								
					/**********************  判断派定人是否存在及有效，并加入到alloteeList   ****************************/
					String Allotee = jsonObj.getString("WorkStaff");
					if(Allotee != null && Allotee.trim().length() > 0){
						Allotee = Allotee.trim();
						
						List<Object[]> qualRetList = qualMgr.getQualifyUsers(Allotee, localeAppItem.getApplianceSpeciesId(), localeAppItem.getSpeciesType()?1:0, qualList);
						if(qualRetList != null && qualRetList.size() > 0){
							boolean alloteeChecked = false;
							for(Object[] objArray : qualRetList){
								if(!qualMgr.checkUserQualify((Integer)objArray[0], localeAppItem.getApplianceSpeciesId(), localeAppItem.getSpeciesType()?1:0, FlagUtil.QualificationType.Type_Except)){	//没有该检验项目的检验排外属性
									alloteeChecked = true;
									SysUser tempUser = new SysUser();
									tempUser.setId((Integer)objArray[0]);
									tempUser.setName((String)objArray[1]);
																		
									localeAppItem.setSysUser(tempUser);		//派定人
									break;
								}
							}
							
							if(!alloteeChecked){
								throw new Exception(String.format("派定人 '%s' 不存在或没有资质检验项目：%s，请重新选择！", Allotee, localeAppItem.getApplianceName()));
							}
						}else{
							throw new Exception(String.format("派定人 '%s' 不存在或没有资质检验项目：%s，请重新选择！", Allotee, localeAppItem.getApplianceName()));
						}
					}else{
						localeAppItem.setSysUser(null);		//派定人
						
					}	
					if(jsonObj.has("Id")&&jsonObj.getString("Id")!=null&&jsonObj.getString("Id").trim().length()>0)
						localeAppItem.setId(Integer.parseInt(jsonObj.getString("Id")));
					localeAppItemList.add(localeAppItem);
				}
				
				
				if (locAppItemMgr.updateByBatch(localeAppItemList,localmission)) { // 修改现场业务，删除原现场业务中的器具信息，添加新的器具信息
					retObj16.put("IsOK", true);
				} else {
					throw new Exception("更新数据库失败！");
				}

			} catch (Exception e) {
				try {
					retObj16.put("IsOK", false);
					retObj16.put("msg", String.format("处理失败！错误信息：%s",(e != null && e.getMessage() != null)?e.getMessage() : "无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				
				if (e.getClass() == java.lang.Exception.class) { // 自定义的消息
					log.debug("exception in LocaleMissionServlet-->case 16", e);
				} else {
					log.error("error in LocaleMissionServlet-->case 16", e);
				}
			} finally {
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj16.toString());
			}
			break;
	   	case 17: // 补登现场任务委托单
			JSONObject retObj17 = new JSONObject();
			try{
				String Name = req.getParameter("Name").trim();
				int CustomerId = Integer.parseInt(req.getParameter("CustomerId").trim());
				String ZipCode = req.getParameter("ZipCode");
				String Department = req.getParameter("Department");
				String Address = req.getParameter("Address");
				String Tel = req.getParameter("Tel");
				String ContactorTel = req.getParameter("ContactorTel");
				String TentativeDate = req.getParameter("TentativeDate");
				//String MissionDesc = req.getParameter("MissionDesc").trim();	//检测项目及台件数
				//String Staffs = req.getParameter("Staffs").trim();
				String SiteManagerId = req.getParameter("SiteManagerId");
				String Contactor = req.getParameter("Contactor");
				String RegionId = req.getParameter("RegionId");
				String Remark = req.getParameter("Remark");
				String Appliances = req.getParameter("Appliances").trim();	//检验的器具
				JSONArray appliancesArray = new JSONArray(Appliances);	//检查的器具
				
				String ExactTime = req.getParameter("ExactTime").trim();	//确定检验时间
				String LocaleMissionCode = req.getParameter("LocaleMissionCode").trim();	//委托书号
				String Drivername=req.getParameter("Drivername");
				String Licence=req.getParameter("Licence");
				
				Timestamp beginTs =  Timestamp.valueOf(String.format("%s 08:30:00", ExactTime.trim()));
				Timestamp endTs = Timestamp.valueOf(String.format("%s 17:00:00", ExactTime.trim()));
				
				Timestamp ts;
                if(TentativeDate==null||TentativeDate.length()==0){
				   ts = null;	//暂定日期
                }else{
			       ts = new Timestamp(DateTimeFormatUtil.DateFormat.parse(TentativeDate).getTime());	//暂定日期
                }
                String Brief = com.jlyw.util.LetterUtil.String2Alpha(Name);
				
                Customer cus=new Customer();
                cus.setId(CustomerId);
				LocaleMission localmission = new LocaleMission();
				localmission.setAddress(Address==null?"":Address);
				localmission.setCustomer(cus);
				localmission.setCustomerName(Name);
				localmission.setBrief(Brief);
				localmission.setZipCode(ZipCode==null?"":ZipCode);
				localmission.setContactor(Contactor==null?"":Contactor);
				localmission.setTel(Tel==null?"":Tel);
				localmission.setContactorTel(ContactorTel==null?"":ContactorTel);
				//localmission.setMissionDesc(MissionDesc);
				localmission.setDepartment(Department);
				//localmission.setStaffs(staffs.toString());
				localmission.setTentativeDate(ts);
				Region region = new Region();
				region.setId(Integer.parseInt(RegionId));
				localmission.setRegion(region);
				localmission.setRemark(Remark);
				localmission.setVehicleLisences(URLDecoder.decode(new String(Licence.trim().getBytes("ISO-8859-1")) , "UTF-8"));
				
				String Code=URLDecoder.decode(new String(LocaleMissionCode.trim().getBytes("ISO-8859-1")) , "UTF-8");
				List<LocaleMission> locMissionList = locmissMgr.findByVarProperty(new KeyValueWithOperator("code",Code,"="));
				if(locMissionList==null||locMissionList.size()==0){
					localmission.setCode(Code);
				}else{
					throw new Exception("已存在委托书号"+Code);
				}
				//System.out.println();
				
				SysUser newUser = new SysUser();
				
				if(SiteManagerId!=null&&SiteManagerId.length()>0){
					newUser.setId(Integer.parseInt(SiteManagerId));
					localmission.setSysUserBySiteManagerId(newUser);
				}else{
					localmission.setSysUserBySiteManagerId(null);
				}
				Timestamp CheckTime = new Timestamp(DateTimeFormatUtil.DateFormat.parse(ExactTime).getTime());
				localmission.setCheckDate(CheckTime);
				localmission.setExactTime(CheckTime);
				
				localmission.setStatus(1);// status: 1 已分配 2 已完成 3已删除 4负责人未核定 5负责人已核定				

				
				Timestamp now = new Timestamp(System.currentTimeMillis());// 取当前时间
				localmission.setCreateTime(now);// 创建时间为当前时间
				localmission.setSysUserByCreatorId((SysUser) req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser));
				localmission.setModificatorDate(now);// 记录时间为当前时间
				localmission.setSysUserByModificatorId((SysUser) req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser));
				
				ApplianceSpeciesManager speciesMgr = new ApplianceSpeciesManager();	//器具分类管理Mgr
				ApplianceStandardNameManager sNameMgr = new ApplianceStandardNameManager();	//器具标准名称管理Mgr
				AppliancePopularNameManager popNameMgr = new AppliancePopularNameManager();	//器具常用名称管理Mgr
				QualificationManager qualMgr = new QualificationManager();	//检测人员资质管理Mgr
				ApplianceManufacturerManager mafMgr = new ApplianceManufacturerManager();	//制造厂管理Mgr
				List<LocaleApplianceItem> localeAppItemList = new ArrayList<LocaleApplianceItem>();	//现场业务中要添加的器具
				List<SysUser> alloteeList = new ArrayList<SysUser>();	//委托单列表对应的派定人：
				List<Integer> qualList = new ArrayList<Integer>();
				
				
				/********************   添加器具信息   ******************/
				for(int i = 0; i < appliancesArray.length(); i++){
					JSONObject jsonObj = appliancesArray.getJSONObject(i);
					LocaleApplianceItem localeAppItem = new LocaleApplianceItem();
					
					/**********************   添加器具信息    ************************/
					String SpeciesType = jsonObj.get("SpeciesType").toString();	//器具分类类型
					String ApplianceSpeciesId = jsonObj.get("ApplianceSpeciesId").toString();	//器具类别ID/标准名称ID
					String ApplianceName = jsonObj.getString("ApplianceName");	//器具名称
					String Manufacturer= jsonObj.getString("Manufacturer");	//制造厂
					
					if(SpeciesType!=null&&SpeciesType.length()>0){
						if(Integer.parseInt(SpeciesType) == 0){	//0:标准名称；1：分类名称
							localeAppItem.setSpeciesType(false);	
						
							String stdName = sNameMgr.findById(Integer.parseInt(ApplianceSpeciesId)).getName();
							if(ApplianceName == null || ApplianceName.trim().length() == 0){
								ApplianceName = stdName;	//器具名称未填写，则默认为标准名称或分类名称
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
							localeAppItem.setSpeciesType(true);	
							if(ApplianceName == null || ApplianceName.trim().length() == 0){
								ApplianceName = speciesMgr.findById(Integer.parseInt(ApplianceSpeciesId)).getName();;	//器具名称未填写，则默认为标准名称或分类名称
							}
						}
						localeAppItem.setApplianceSpeciesId(Integer.parseInt(ApplianceSpeciesId));					
						localeAppItem.setApplianceName(ApplianceName);	//存器具名称		
					}
					localeAppItem.setAppFactoryCode(jsonObj.getString("ApplianceCode"));		//出厂编号
					localeAppItem.setAppManageCode(jsonObj.getString("AppManageCode"));		//管理编号
					localeAppItem.setModel(jsonObj.getString("Model"));		//型号规格
					localeAppItem.setRange(jsonObj.getString("Range"));		//测量范围
					localeAppItem.setAccuracy(jsonObj.getString("Accuracy"));	//精度等级
					localeAppItem.setManufacturer(jsonObj.getString("Manufacturer"));		//制造厂商
					localeAppItem.setCertType(jsonObj.getString("ReportType"));	//报告形式
					if(jsonObj.has("TestFee")&&jsonObj.getString("TestFee").length()>0&&jsonObj.getString("TestFee")!=null){
						localeAppItem.setTestCost(Double.valueOf(jsonObj.getString("TestFee")));
					}
					if(jsonObj.has("RepairFee")&&jsonObj.getString("RepairFee").length()>0&&jsonObj.getString("RepairFee")!=null){
						localeAppItem.setRepairCost(Double.valueOf(jsonObj.getString("RepairFee")));
					}
					if(jsonObj.has("MaterialFee")&&jsonObj.getString("MaterialFee").length()>0&&jsonObj.getString("MaterialFee")!=null){
						localeAppItem.setMaterialCost(Double.valueOf(jsonObj.getString("MaterialFee")));
					}
					localeAppItem.setQuantity(Integer.parseInt(jsonObj.get("Quantity").toString()));//台件数	
					if(jsonObj.has("AssistStaff")&&jsonObj.getString("AssistStaff")!=null&&jsonObj.getString("AssistStaff").trim().length()>0){
						localeAppItem.setAssistStaff(jsonObj.getString("AssistStaff"));	//存辅助派定人或替代人		
					}		
					/**********************  判断派定人是否存在及有效，并加入到alloteeList   ****************************/
					String Allotee = jsonObj.getString("WorkStaff");
					if(Allotee != null && Allotee.trim().length() > 0){
						Allotee = Allotee.trim();
						
						List<Object[]> qualRetList = qualMgr.getQualifyUsers(Allotee, localeAppItem.getApplianceSpeciesId(), localeAppItem.getSpeciesType()?1:0, qualList);
						if(qualRetList != null && qualRetList.size() > 0){
							boolean alloteeChecked = false;
							for(Object[] objArray : qualRetList){
								if(!qualMgr.checkUserQualify((Integer)objArray[0], localeAppItem.getApplianceSpeciesId(), localeAppItem.getSpeciesType()?1:0, FlagUtil.QualificationType.Type_Except)){	//没有该检验项目的检验排外属性
									alloteeChecked = true;
									SysUser tempUser = new SysUser();
									tempUser.setId((Integer)objArray[0]);
									tempUser.setName((String)objArray[1]);
																		
									localeAppItem.setSysUser(tempUser);		//派定人
									break;
								}
							}
							
							if(!alloteeChecked){
								throw new Exception(String.format("派定人 '%s' 不存在或没有资质检验项目：%s，请重新选择！", Allotee, localeAppItem.getApplianceName()));
							}
						}else{
							throw new Exception(String.format("派定人 '%s' 不存在或没有资质检验项目：%s，请重新选择！", Allotee, localeAppItem.getApplianceName()));
						}
					}else{
						localeAppItem.setSysUser(null);		//派定人
						
					}					
					localeAppItemList.add(localeAppItem);
				}
				
				//车辆处理
				VehicleManager VeMgr=new VehicleManager();
				List<Vehicle> vehivleList=VeMgr.findByVarProperty(new KeyValueWithOperator("licence",URLDecoder.decode(new String(Licence.trim().getBytes("ISO-8859-1")) , "UTF-8") , "="),
									      new KeyValueWithOperator("status", 0, "="));
				Vehicle vehicle=new Vehicle();
				UserManager userMgr=new UserManager();
				SysUser driver =new SysUser();
				if(Drivername!=null&&Drivername.length()>0){
					driver= userMgr.findByVarProperty(new KeyValueWithOperator("name",URLDecoder.decode(new String(Drivername.trim().getBytes("ISO-8859-1")) , "UTF-8") , "=")).get(0);			
				}
				if(vehivleList!=null&&vehivleList.size()>0){
					vehicle=vehivleList.get(0);
				}else{	
					throw new Exception("找不到车牌号为"+URLDecoder.decode(new String(Licence.trim().getBytes("ISO-8859-1")) , "UTF-8")+"的车，请重新输入！");		
					/*vehicle.setLicence(URLDecoder.decode(new String(Licence.trim().getBytes("ISO-8859-1")) , "UTF-8"));
					
					vehicle.setSysUser(driver);
					vehicle.setBrand("");
					vehicle.setFuelFee(0.0);
					vehicle.setLicenceType("");
					vehicle.setLimit(0);
					vehicle.setModel("");
					vehicle.setStatus(0);					
					VeMgr.save(vehicle);		*/			
				}
				
				//找现场业务下面的各个器具指派人
				String staffs="";
				if(!localeAppItemList.isEmpty()){						
					for (LocaleApplianceItem user : localeAppItemList) {
						String name=(user.getSysUser()==null?"":user.getSysUser().getName());
						if(staffs.indexOf(name+";")<0){
							staffs = staffs + name +";"; 		
						}
					}
				}
				if(localmission.getSysUserBySiteManagerId()!=null){
					if(staffs.indexOf(localmission.getSysUserBySiteManagerId().getName()+";")<0){
						staffs = staffs + localmission.getSysUserBySiteManagerId().getName()+";"; 		
					}
				}
				
				DrivingVehicle drivingvehicle=new DrivingVehicle(); //新建出车记录
				drivingvehicle.setAssemblingPlace("");
				drivingvehicle.setBeginDate(beginTs);
				drivingvehicle.setEndDate(endTs);	
				drivingvehicle.setStatus(0);
				drivingvehicle.setSysUserByDriverId(driver); // 驾驶员							
				drivingvehicle.setPeople(staffs);
				drivingvehicle.setVehicle(vehicle);// 车辆
				
				if (locAppItemMgr.saveByBatchBD(localeAppItemList,localmission,drivingvehicle)) { // 补登成功
					retObj17.put("IsOK", true);
				} else {
					throw new Exception("写入数据库失败！");
				}

			}catch (Exception e) {
				try {
					retObj17.put("IsOK", false);
					retObj17.put("msg", String.format("处理失败！错误信息：%s",(e != null && e.getMessage() != null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if (e.getClass() == java.lang.Exception.class) { // 自定义的消息
					log.debug("exception in LocaleMissionServlet-->case 17", e);
				} else {
					log.error("error in LocaleMissionServlet-->case 17", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj17.toString());
			}
			break;
		}
		
	}

	public String[] timeJudge(List<VehicleMission> vehiclemission,
			Timestamp monday,int[] CCCS) {// 判断车辆是否出车
		String[] available = new String[14];
		int timepart = 0;
		for (; timepart < 14; timepart++) {
			available[timepart] = "空闲";
		}
		int i=0;
		int tempi=0;
		List<DrivingVehicle> drivingVehicletemp=new ArrayList<DrivingVehicle>();
		for (i=0;i<vehiclemission.size();i++) {
			VehicleMission vm=vehiclemission.get(i);
			tempi=i;
			if(drivingVehicletemp.size() > 0){
				if(!drivingVehicletemp.contains(vm.getDrivingVehicle())){
					timepart = 0;
					Calendar caTod = Calendar.getInstance();
					caTod.setTime(monday);
					for (int day = 0; day < 7; day++) {
						Timestamp endDate = vm.getDrivingVehicle().getEndDate();
						Timestamp beginDate = vm.getDrivingVehicle().getBeginDate();
						Date Today = caTod.getTime();
						caTod.add(Calendar.DATE, 1);
						Date Tommorow = caTod.getTime();
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						String timeCompareA = df.format(Today);
						String timeCompareB = df.format(Tommorow);
						if (endDate.compareTo(Timestamp.valueOf(timeCompareA
								+ " 00:00:00")) <= 0
								|| beginDate.compareTo(Timestamp.valueOf(timeCompareA
										+ " 12:00:00")) > 0) {
							;
						} else {
							CCCS[timepart]=CCCS[timepart]+1;
							//System.out.println(vm.getDrivingVehicle().getVehicle().getLicence()+ " "+timepart+"  "+CCCS[timepart]);
							available[timepart] = "出车"+CCCS[timepart];
						}
						timepart++;
						if (endDate.compareTo(Timestamp.valueOf(timeCompareA
								+ " 12:00:00")) <= 0
								|| beginDate.compareTo(Timestamp.valueOf(timeCompareB
										+ " 00:00:00")) > 0) {
							;
						} else {
							CCCS[timepart]=CCCS[timepart]+1;
							//System.out.println(vm.getDrivingVehicle().getVehicle().getLicence()+ " "+timepart+"  "+CCCS[timepart]);
							available[timepart] = "出车"+CCCS[timepart];
						}
						timepart++;					
					}
					drivingVehicletemp.add(vm.getDrivingVehicle());
					//System.out.println(vm.getDrivingVehicle().getId());
				}else{
					
				}
			}else{
				timepart = 0;
				Calendar caTod = Calendar.getInstance();
				caTod.setTime(monday);
				for (int day = 0; day < 7; day++) {
					Timestamp endDate = vm.getDrivingVehicle().getEndDate();
					Timestamp beginDate = vm.getDrivingVehicle().getBeginDate();
					Date Today = caTod.getTime();
					caTod.add(Calendar.DATE, 1);
					Date Tommorow = caTod.getTime();
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					String timeCompareA = df.format(Today);
					String timeCompareB = df.format(Tommorow);
					if (endDate.compareTo(Timestamp.valueOf(timeCompareA
							+ " 00:00:00")) <= 0
							|| beginDate.compareTo(Timestamp.valueOf(timeCompareA
									+ " 12:00:00")) > 0) {
						;
					} else {
						CCCS[timepart]=CCCS[timepart]+1;
						//System.out.println(vm.getDrivingVehicle().getVehicle().getLicence()+ " "+timepart+"  "+CCCS[timepart]);
						available[timepart] = "出车"+CCCS[timepart];
					}
					timepart++;
					if (endDate.compareTo(Timestamp.valueOf(timeCompareA
							+ " 12:00:00")) <= 0
							|| beginDate.compareTo(Timestamp.valueOf(timeCompareB
									+ " 00:00:00")) > 0) {
						;
					} else {
						CCCS[timepart]=CCCS[timepart]+1;
						//System.out.println(vm.getDrivingVehicle().getVehicle().getLicence()+ " "+timepart+"  "+CCCS[timepart]);
						available[timepart] = "出车"+CCCS[timepart];
					}
					timepart++;
				}
				drivingVehicletemp.add(vm.getDrivingVehicle());
				//System.out.println(vm.getDrivingVehicle().getId());
			}
					
			
		}

		return available;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// doPost(req, resp);
	}

}
