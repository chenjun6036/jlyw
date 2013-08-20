package com.jlyw.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.bson.types.ObjectId;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.jlyw.hibernate.Deal;
import com.jlyw.hibernate.DealItem;
import com.jlyw.hibernate.Quotation;
import com.jlyw.hibernate.QuotationItem;
import com.jlyw.hibernate.SysUser;
import com.jlyw.manager.ApplianceStandardNameManager;
import com.jlyw.manager.CustomerManager;
import com.jlyw.manager.DealItemManager;
import com.jlyw.manager.DealManager;
import com.jlyw.manager.UserManager;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.MongoDBUtil;
import com.jlyw.util.SystemCfgUtil;
import com.jlyw.util.UIDUtil;
import com.jlyw.util.mongodbService.DBPoolManager;
import com.jlyw.util.mongodbService.MongoService;
import com.jlyw.util.mongodbService.MongoServiceImpl;
import com.mongodb.gridfs.GridFSDBFile;

public class DealServlet extends HttpServlet {
	private static Log log = LogFactory.getLog(DealServlet.class);
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		int method = Integer.valueOf(req.getParameter("method"));
		DealManager dealMgr = new DealManager();
		DealItemManager dealItemMgr = new DealItemManager();
		switch(method){
		case 1://导入
			JSONObject retJSON1 = new JSONObject();
			InputStream is = null;
			try{
				String FileId = req.getParameter("FileId");
				MongoDBUtil.CollectionType type = MongoDBUtil.CollectionType.Sharing;
				JSONObject retObj1 = MongoDBUtil.getFileInfoById(FileId, type);
				if(retObj1 != null){
					//String filename = retObj1.getString(MongoDBUtil.KEYNAME_FileName);//文件名
					MongoService s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), SystemCfgUtil.BucketSharing);
					ObjectId id = new ObjectId(FileId);
					GridFSDBFile dbFile = s.findOneById(id);
						//dbFile.writeTo(out);
					is=dbFile.getInputStream();
					HSSFWorkbook workbook = new HSSFWorkbook(is);
					HSSFSheet sheet = workbook.getSheetAt(0);
	
					//JSONObject paramsMap = new JSONObject();
	
					JSONArray options = new JSONArray();
					int rowcount = sheet.getLastRowNum();  //取得有效的行数  
	                HSSFRow row;
	                int colcount = 0;    
	                for (int i = 1; i <= rowcount; ++i) {   
	                    	//根据index取得行对象,有了行对象，就可以取得每一个单元对象
						row = sheet.getRow(i);
						if (row.getCell(1) == null||row.getCell(1).toString().equals(""))
							continue;
						if (colcount == 0) {
							colcount = row.getLastCellNum(); // 一个行有多少个单元
						}
						JSONObject option = new JSONObject();
						String name = row.getCell(1).toString();
						
						ApplianceStandardNameManager appStdNameMgr = new ApplianceStandardNameManager();
						String querySQL="select model.Id from "+SystemCfgUtil.DBPrexName+"ApplianceStandardName as model where model.Name=? " +
										" union " +
										" select p.StandardNameId from "+SystemCfgUtil.DBPrexName+"AppliancePopularName as p where p.PopularName=? ";
						List<Integer> result = appStdNameMgr.findBySQL(querySQL,name,name);
						
						if(result!=null&&result.size()>0){
							option.put("StandardName", name);
							option.put("StandardNameId", result.get(0));
							
							option.put("Model", getCellStr(row.getCell(2)));
							option.put("Accuracy",getCellStr(row.getCell(4)));
							option.put("Range", getCellStr(row.getCell(3)));
							option.put("AppFactoryCode", getCellStr(row.getCell(5)));//出厂编号
							option.put("AppManageCode", getCellStr(row.getCell(6)));      //管理编号               	
							option.put("Manufacturer", getCellStr(row.getCell(7)));//制造厂
							option.put("DealPrice", Double.valueOf(row.getCell(8)==null||row.getCell(8).toString().equals("")?"0.0":row.getCell(8).toString()));
							option.put("Remark", "");
						}
						else{
							continue;
						}

	                	options.put(option);
						
					}
	                retJSON1.put("total", options.length());
	                retJSON1.put("rows", options);
				} else {
	                retJSON1.put("total", 0);
	                retJSON1.put("rows", new JSONArray());
				}
			}catch(Exception e){
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in DealServlet-->case 1", e);
				}else{
					log.error("error in DealServlet-->case 1", e);
				}
				try {
					retJSON1.put("total", 0);
	                retJSON1.put("rows", new JSONArray());
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
			}finally{
				is.close();
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON1.toString());
			}
			break;
		case 2://新增协议
			JSONObject retJSON2 = new JSONObject();
			try{
				Deal deal = initDeal(req, null);
				String DealItemStr = req.getParameter("Item");
				JSONArray DealItems = new JSONArray(DealItemStr);
				List<DealItem> list = new ArrayList<DealItem>();
				for(int i = 0; i < DealItems.length(); i++){
					JSONObject Item = DealItems.getJSONObject(i);
					DealItem dealItem = new DealItem();
					dealItem.setDeal(deal);
					dealItem.setStandardName(Item.getString("StandardName"));
					dealItem.setApplianceStandardName((new ApplianceStandardNameManager()).findById(Item.getInt("StandardNameId")));
					dealItem.setModel(Item.getString("Model"));
					dealItem.setAccuracy(Item.getString("Accuracy"));
					dealItem.setRange(Item.getString("Range"));
					dealItem.setAppFactoryCode(Item.getString("AppFactoryCode"));
					dealItem.setAppManageCode(Item.getString("AppManageCode"));
					dealItem.setManufacturer(Item.getString("Manufacturer"));
					dealItem.setRemark(Item.getString("Remark"));
					dealItem.setDealPrice(Double.valueOf(Item.get("DealPrice").toString()));
					
					list.add(dealItem);
				}
				
				if(!dealMgr.savaByBatch(deal, list)){
					throw new Exception("保存协议及条目失败");
				}
				retJSON2.put("IsOK", true);
				retJSON2.put("msg", "新建协议成功！");
				retJSON2.put("Attachment", deal.getAttachment());
			}catch(Exception e){
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in DealServlet-->case 2", e);
				}else{
					log.error("error in DealServlet-->case 2", e);
				}
				try {
					retJSON2.put("IsOK", false);
					retJSON2.put("msg", String.format("新建协议失败！原因：%s", e.getMessage()==null?"":e.getMessage()));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON2.toString());
			}
			break;
		case 3://根据条件查询协议
			JSONObject retJSON3 = new JSONObject();
			try{
				String DealCode = req.getParameter("DealCode");	
				String Customer = req.getParameter("Customer");
				String StartTime = req.getParameter("StartTime");
				String EndTime = req.getParameter("EndTime");
				String Status = req.getParameter("Status");
				
				JSONArray options = new JSONArray();
				List<KeyValueWithOperator> condList = new ArrayList<KeyValueWithOperator>();
				List<Deal> dealList;
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				
				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				int total = 0;
				
				if(DealCode !=null && DealCode.length()>0)
				{
					condList.add(new KeyValueWithOperator("dealCode","%" + URLDecoder.decode(DealCode.trim(), "UTF-8") + "%","like"));
				}
				if(Customer != null && Customer.length()>0){
					String cusName =  URLDecoder.decode(Customer.trim(), "UTF-8");
					condList.add(new KeyValueWithOperator("customer.name","%" + cusName + "%","like"));
				}
				if(Status != null && Status.length()>0){
					Status =  URLDecoder.decode(Status.trim(), "UTF-8");
					condList.add(new KeyValueWithOperator("status",Integer.parseInt(Status),"="));
				}
				Timestamp Start = null;
				if(StartTime!=null && StartTime.trim().length() > 0){		
					 Start = Timestamp.valueOf(String.format("%s 00:00:00",StartTime.trim()));	
					 condList.add(new KeyValueWithOperator("signDate",Start,">="));
				}
				Timestamp End = null;
				if(EndTime!=null && EndTime.trim().length() > 0){		
					End = Timestamp.valueOf(String.format("%s 23:59:00",EndTime.trim()));	
					 condList.add(new KeyValueWithOperator("signDate",End,"<="));
				}
				total = dealMgr.getTotalCount(condList);				
				dealList = dealMgr.findPagedAllBySort(page, rows, "dealCode", false, condList);
				
				if(dealList!=null&&dealList.size()>0){
					for (Deal temp : dealList) {
						JSONObject option = new JSONObject();
						option.put("DealCode", temp.getDealCode());
						option.put("CustomerName", temp.getCustomer().getName());
						option.put("Contactor", temp.getContactorName());
						option.put("ContactorTel", temp.getContactorTel());
						option.put("SignDate", sf.format(temp.getSignDate()));
						option.put("Signer", temp.getSysUserBySignerId()==null?"":temp.getSysUserBySignerId().getName());
						option.put("Validity", sf.format(temp.getValidity()));
						option.put("Status",temp.getStatus());
						option.put("Remark", temp.getRemark());
						option.put("Attachment", temp.getAttachment());
						option.put("Creator", temp.getSysUserByCreatorId().getName());
						option.put("CreateDate", temp.getCreatDate());
							
						options.put(option);
					}
				}
				retJSON3.put("total", total);
				retJSON3.put("rows", options);
				System.out.println(total);
			}catch(Exception e){
				try {
					retJSON3.put("total", 0);
					retJSON3.put("rows", new JSONArray());
				} catch (JSONException e1) {}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in DealServlet-->case 3", e);
				}else{
					log.error("error in DealServlet-->case 3", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON3.toString());
			}
			break;
		case 4://查询协议条目
			JSONObject retJSON4 = new JSONObject();
			try{
				String DealCode = req.getParameter("DealCode"); 
				List<DealItem> dealItemList;

				int total = 0;

				dealItemList = dealItemMgr.findByVarProperty(new KeyValueWithOperator("deal.dealCode",DealCode,"="));
				total = dealItemMgr.getTotalCount(new KeyValueWithOperator("deal.dealCode",DealCode,"="));
				JSONArray options = new JSONArray();
				
				if(dealItemList!=null&&dealItemList.size()>0){
					for (DealItem dealItem : dealItemList) {
						JSONObject option = new JSONObject();
						option.put("Id", dealItem.getId());
						option.put("StandardNameId", dealItem.getApplianceStandardName()==null?"":dealItem.getApplianceStandardName().getId());
						option.put("StandardName", dealItem.getStandardName());
					
						option.put("Model",dealItem.getModel());
						option.put("Accuracy", dealItem.getAccuracy());
						option.put("Range", dealItem.getRange());
						option.put("AppFactoryCode",dealItem.getAppFactoryCode()==null?"":dealItem.getAppFactoryCode());
						option.put("AppManageCode",dealItem.getAppManageCode()==null?"":dealItem.getAppManageCode());
						option.put("Manufacturer",dealItem.getManufacturer()==null?"":dealItem.getManufacturer());
						option.put("DealPrice", dealItem.getDealPrice());
						option.put("Remark", dealItem.getRemark());
	
						options.put(option);
					}
				}
				retJSON4.put("total", total);
				retJSON4.put("rows", options);
			}catch(Exception e){
				try {
					retJSON4.put("total", 0);
					retJSON4.put("rows", new JSONArray());
				} catch (JSONException e1) {}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in DealServlet-->case 4", e);
				}else{
					log.error("error in DealServlet-->case 4", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON4.toString());
			}
			break;
		case 5://注销协议
			JSONObject retJSON5 = new JSONObject();
			try {
				Deal deal = dealMgr.findById(req.getParameter("DealCode"));
				deal.setStatus(1);
				boolean res1 = dealMgr.update(deal);
				retJSON5.put("IsOK", res1);
				retJSON5.put("msg", res1?"注销成功！":"注销失败，请重新注销！");
			} catch (Exception e) {
				try{
					retJSON5.put("IsOK", false);
					retJSON5.put("msg", String.format("注销失败！原因：%s", e.getMessage()==null?"":e.getMessage()));
				}catch(Exception ex){}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in DealServlet-->case 5", e);
				}else{
					log.error("error in DealServlet-->case 5", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON5.toString());
			}
			break;
		case 6://修改协议条目
			JSONObject retJSON6 = new JSONObject();
			try{
				String DealCode = req.getParameter("dealCode");
				String ItemStr = req.getParameter("Item");
				JSONArray DealItems = new JSONArray(ItemStr);
				
				List<DealItem> saveDealItemList = new ArrayList<DealItem>();
				List<DealItem> updateDealItemList = new ArrayList<DealItem>();
				Deal deal = dealMgr.findById(DealCode);
				for(int i = 0; i < DealItems.length(); i++){
					JSONObject Item = DealItems.getJSONObject(i);
					DealItem dealItem;
					if(Item.has("Id")){
						dealItem = dealItemMgr.findById(Item.getInt("Id"));
						updateDealItemList.add(dealItem);
					}
					else{
						dealItem = new DealItem();
						dealItem.setDeal(deal);
						saveDealItemList.add(dealItem);
					}
					
					dealItem.setStandardName(Item.getString("StandardName"));
					dealItem.setApplianceStandardName((new ApplianceStandardNameManager()).findById(Item.getInt("StandardNameId")));
					dealItem.setModel(Item.getString("Model"));
					dealItem.setAccuracy(Item.getString("Accuracy"));
					dealItem.setRange(Item.getString("Range"));
					dealItem.setAppFactoryCode(Item.getString("AppFactoryCode"));
					dealItem.setAppManageCode(Item.getString("AppManageCode"));
					dealItem.setManufacturer(Item.getString("Manufacturer"));
					dealItem.setRemark(Item.getString("Remark"));
					dealItem.setDealPrice(Double.valueOf(Item.get("DealPrice").toString()));
					
					if(!dealItemMgr.updateByBatch(saveDealItemList, updateDealItemList))
						throw new Exception("修改协议条目失败！");
					
					retJSON6.put("IsOK", true);
					retJSON6.put("msg", "修改协议条目成功！");
				}
			}catch(Exception e){
				try{
					retJSON6.put("IsOK", false);
					retJSON6.put("msg", String.format("修改协议条目失败！原因：%s", e.getMessage()==null?"":e.getMessage()));
				}catch(Exception ex){}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in DealServlet-->case 6", e);
				}else{
					log.error("error in DealServlet-->case 6", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON6.toString());
			}
			break;
		case 7://修改协议信息
			JSONObject retJSON7 = new JSONObject();
			try{
				String DealCode = req.getParameter("DealCode");
				Deal deal = initDeal(req, DealCode);
				boolean res1 = dealMgr.update(deal);
				retJSON7.put("IsOK", res1);
				retJSON7.put("msg", res1?"修改成功！":"修改失败，请重新修改！");
			}catch(Exception e){
				try{
					retJSON7.put("IsOK", false);
					retJSON7.put("msg", String.format("修改协议失败！原因：%s", e.getMessage()==null?"":e.getMessage()));
				}catch(Exception ex){}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in DealServlet-->case 7", e);
				}else{
					log.error("error in DealServlet-->case 7", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON7.toString());
			}
			break;
		case 8://删除一条协议条目
			JSONObject retJSON8 = new JSONObject();
			try{
				String Id = req.getParameter("Id");
				boolean res1 = dealItemMgr.deleteById(Integer.valueOf(Id));
				retJSON8.put("IsOK", res1);
				retJSON8.put("msg", res1?"删除成功！":"删除失败，请重新删除！");
			}catch(Exception e){
				try{
					retJSON8.put("IsOK", false);
					retJSON8.put("msg", String.format("删除协议条目失败！原因：%s", e.getMessage()==null?"":e.getMessage()));
				}catch(Exception ex){}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in DealServlet-->case 8", e);
				}else{
					log.error("error in DealServlet-->case 8", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON8.toString());
			}
			break;
		}
	}
	
	private String getCellStr(HSSFCell Cell){
		String CellStr = "";
		if(Cell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC)
		{
			if(Cell.toString().length()>0&&Cell.toString().indexOf('.')!=-1){
				int index = Cell.toString().indexOf('.');
				CellStr=Cell.toString().substring(0, index);
			}
		}
		else{
			CellStr = Cell.toString();
		}
		return CellStr;
	}
	
	private Deal initDeal(HttpServletRequest req, String Id){
		Deal deal;
		DealManager dealMgr = new DealManager();
		if(Id==null){
			deal = new Deal();
			String year = String.format("%04d", Calendar.getInstance().get(Calendar.YEAR));
			String queryString = "select max(model.dealCode) from Deal as model where model.dealCode like ?";
			List<Object> retList = dealMgr.findByHQL(queryString, year+"%");
			Integer codeBeginInt = Integer.parseInt("000001");
			if(retList.size() > 0 && retList.get(0) != null){
				codeBeginInt = Integer.parseInt(retList.get(0).toString().substring(4,10)) + 1;
			}
			Id = year + String.format("%06d", codeBeginInt);
			
			deal.setDealCode(Id);
		}
		else{
			deal = dealMgr.findById(Id);
		}
		
		String CustomerName = req.getParameter("CustomerName");
		String Contactor = req.getParameter("Contactor");
		String ContactorTel = req.getParameter("ContactorTel");
		String Signer = req.getParameter("Signer");
		String SignDate = req.getParameter("SignDate");
		String Validity = req.getParameter("Validity");
		String Status = req.getParameter("Status");
		String Remark = req.getParameter("Remark");
		
		deal.setCustomer((new CustomerManager()).findByVarProperty(new KeyValueWithOperator("name", CustomerName, "=")).get(0));
		deal.setContactorName(Contactor);
		deal.setContactorTel(ContactorTel);
		
		if(Signer!=null){
			List<SysUser> users = (new UserManager()).findByVarProperty(new KeyValueWithOperator("name", Signer, "="));
			if(users!=null&&users.size()>0)
				deal.setSysUserBySignerId(users.get(0));
			else
				deal.setSysUserBySignerId(null);
		}
		else{
			deal.setSysUserBySignerId(null);
		}
		
		deal.setSignDate(SignDate==null?null:Date.valueOf(SignDate));
		deal.setValidity(Validity==null?null:Date.valueOf(Validity));
		deal.setStatus(Integer.valueOf(Status));
		deal.setRemark(Remark);
		
		deal.setSysUserByCreatorId((SysUser) req.getSession().getAttribute("LOGIN_USER"));
		deal.setCreatDate(new Timestamp(System.currentTimeMillis()));
		deal.setAttachment(UIDUtil.get22BitUID());
		
		return deal;
	}
	
}
