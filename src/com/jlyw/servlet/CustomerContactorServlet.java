package com.jlyw.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.jlyw.hibernate.BaseHibernateDAO;
import com.jlyw.hibernate.Customer;
import com.jlyw.hibernate.CustomerContactor;
import com.jlyw.hibernate.Region;
import com.jlyw.hibernate.RegionInsideContactor;
import com.jlyw.hibernate.SysUser;
import com.jlyw.manager.CustomerContactorManager;
import com.jlyw.manager.CustomerManager;
import com.jlyw.manager.crm.RegionContactorManager;
import com.jlyw.util.DateTimeFormatUtil;
import com.jlyw.util.KeyValueWithOperator;
import com.sun.org.apache.regexp.internal.recompile;
import com.sun.swing.internal.plaf.basic.resources.basic;

public class CustomerContactorServlet extends HttpServlet {
	private static final Log log = LogFactory.getLog(CustomerContactorServlet.class);
	private CustomerManager customerManager = new CustomerManager();
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		int method = Integer.valueOf(req.getParameter("method"));
		CustomerContactorManager cusConMgr = new CustomerContactorManager();
		switch(method){
		case 1://新增或修改外部联系人信息
			JSONObject retObj = new JSONObject();
			try{
				String Id = req.getParameter("Id");
				CustomerContactor cusCon = new CustomerContactor();
				if(Id.equals("")){
					cusCon = initCustomerContactor(req, 0);
					boolean res = cusConMgr.save(cusCon);
					retObj.put("IsOK", res);
					retObj.put("msg", res?"添加成功！":"添加失败，请重新添加！");
				}
				else{
					cusCon = initCustomerContactor(req, Integer.valueOf(Id));
					boolean res = cusConMgr.update(cusCon);
					retObj.put("IsOK", res);
					retObj.put("msg", res?"修改成功！":"修改失败，请重新修改！");
				}
			}catch(Exception e){
				try {
					retObj.put("IsOK", false);
					retObj.put("msg", String.format("操作失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CustomerContactorServlet-->case 1", e);
				}else{
					log.error("error in CustomerContactorServlet-->case 1", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj.toString());
			}
			break;
		case 2://根据委托单位id查询联系人
			JSONObject retObj2 = new JSONObject();
			try{
				String CustomerId = req.getParameter("CustomerId");
				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				
				List<CustomerContactor> result;
				int total = 0;
				
				if(CustomerId!=null&&!CustomerId.equals("")){
					result = cusConMgr.findPagedAll(page, rows,new KeyValueWithOperator("status", Integer.valueOf("0"), "="), new KeyValueWithOperator("customerId", Integer.valueOf(CustomerId), "="));
					total = cusConMgr.getTotalCount(new KeyValueWithOperator("customerId", Integer.valueOf(CustomerId), "="));
				}
				else{
					result = cusConMgr.findPagedAll(page, rows);
					total = cusConMgr.getTotalCount();
				}
				JSONArray options = new JSONArray();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				if(result!=null&&result.size()>0){
					for(CustomerContactor cusCon : result){
						JSONObject option = new JSONObject();
						option.put("Id", cusCon.getId());
						option.put("Name", cusCon.getName());
						option.put("Cellphone1", cusCon.getCellphone1());
						option.put("Cellphone2", cusCon.getCellphone2());
						option.put("Email", cusCon.getEmail());
						option.put("LastUse", cusCon.getLastUse()==null?"":cusCon.getLastUse().toLocaleString());
						///////////////////////////////////////////////
						option.put("CurJob", cusCon.getCurJob());
						option.put("Birthday", cusCon.getBirthday()==null?"":formatter.format(cusCon.getBirthday()));
						option.put("Remark", cusCon.getRemark());
						///////////////////////////////////////////////
						options.put(option);
					}
				}
				retObj2.put("total", total);
				retObj2.put("rows", options);
			}catch(Exception e){
				try {
					retObj2.put("total", 0);
					retObj2.put("rows", new JSONArray());
				} catch (JSONException e1) {}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CustomerContactorServlet-->case 2", e);
				}else{
					log.error("error in CustomerContactorServlet-->case 2", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retObj2.toString());
			}
			break;
		case 3://注销
			JSONObject retObj3 = new JSONObject();
			try{
				String Id = req.getParameter("Id");
				String FormerDep=req.getParameter("Name");
				String CurDep=req.getParameter("CurDep");
				String FormerJob=req.getParameter("FormerJob");
				String CurJob=req.getParameter("CurJob");
				String Remark=req.getParameter("Remark");
				CustomerContactor contactor=cusConMgr.findById(Integer.parseInt(Id));
				
				contactor.setFormerDep(FormerDep);
				contactor.setFormerJob(FormerJob);
				contactor.setCurDep(CurDep);
				contactor.setCurJob(CurJob);
				contactor.setRemark(Remark);
				contactor.setStatus(1);//已注销
				boolean res=false;
				if(cusConMgr.save(contactor))
					res=true;
				else
					res=false;
				retObj3.put("IsOK", res);
				retObj3.put("msg", res?"注销成功！":"注销失败，请重试！");
				
				
			}catch(Exception e){
				try {
					retObj3.put("IsOK", false);
					retObj3.put("msg", String.format("操作失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in RegionServlet-->case 3", e);
				}else{
					log.error("error in RegionServlet-->case 3", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj3.toString());
			}
			break;
		case 4://查询
			JSONObject retObj4=new JSONObject();
			try {
				String CustomerName=req.getParameter("CustomerName");
				String Name=req.getParameter("Name");
				String Status=req.getParameter("Status");
				
				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				
				String queryStr = "from CustomerContactor as model where 1=1 ";
				List<Object> keys = new ArrayList<Object>();
				
				if(CustomerName!=null&&!CustomerName.equals(""))
				{
					String cusNameStr = URLDecoder.decode(CustomerName, "UTF-8");
					queryStr = queryStr + " and (model.customer.name like ? or model.customer.brief like ? or model.formerDep like ? or model.curDep like ?)";
					keys.add("%" + cusNameStr + "%");
					keys.add("%" + cusNameStr + "%");
					keys.add("%" + cusNameStr + "%");
					keys.add("%" + cusNameStr + "%");
				}
				if(Name!=null&&!Name.equals(""))
				{
					String cusNameStr = URLDecoder.decode(Name, "UTF-8");
					queryStr = queryStr + " and (model.name like ? )";
					keys.add("%" + cusNameStr + "%");
				}
				if(Status!=null&&!Status.trim().equals(""))
				{
					String cusNameStr = URLDecoder.decode(Status.trim(), "UTF-8");
					queryStr = queryStr + " and (model.status = ? )";
					keys.add(Integer.valueOf(cusNameStr));
				}
				//CustomerContactorManager manager=new CustomerContactorManager();
				//manager.findPagedAll(page, rows, new KeyValueWithOperator(),)
				BaseHibernateDAO dao=new BaseHibernateDAO();
				List<CustomerContactor> lr=dao.findPageAllByHQL(queryStr, page, rows, keys);
				int total=dao.getTotalCountByHQL("select count(*) "+queryStr, keys);
				JSONArray tmp=new JSONArray();
				for(CustomerContactor a:lr)
				{
					JSONObject t=new JSONObject();
					Customer c = customerManager.findById(a.getCustomerId());
					t.put("Name", a.getName()==null?"":a.getName());
					t.put("Birthday", a.getBirthday()==null?"":a.getBirthday());
					t.put("Cellphone1", a.getCellphone1()==null?"":a.getCellphone1());
					t.put("Cellphone2", a.getCellphone2()==null?"":a.getCellphone2());
					t.put("Count", a.getCount()==null?"":a.getCount());
					t.put("CurDep",a.getCurDep()==null?"":a.getCurDep());
					t.put("CurJob", a.getCurJob()==null?"":a.getCurJob());
					t.put("CustomerId", a.getCustomerId());
					t.put("CustomerName2", c==null?"":c.getName());
					t.put("Email", a.getEmail()==null?"":a.getEmail());
					t.put("FormerDep", a.getFormerDep()==null?"":a.getFormerDep());
					t.put("FormerJob", a.getFormerJob()==null?"":a.getFormerJob());
					t.put("Id", a.getId());
					t.put("LastUse", a.getLastUse()==null?"":a.getLastUse().toString().substring(0, 19)/*String.format("%s",a.getLastUse() )*/);
					t.put("Remark", a.getRemark()==null?"":a.getRemark());
					t.put("Status", a.getStatus()==null?"-1":a.getStatus());
					tmp.put(t);
				}
				retObj4.put("total", total);
				retObj4.put("rows", tmp);
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retObj4.toString());
			}
			break;
		case 5://单独管理页面中增加
			JSONObject retObj5=new JSONObject();
			try {
				String Name=req.getParameter("Name");
				String CustomerId=req.getParameter("CustomerId");
				String Cellphone1 = req.getParameter("Cellphone1");
				String Cellphone2 = req.getParameter("Cellphone2");
				String Email = req.getParameter("Email");
				String CurJob=req.getParameter("CurJob");
				String Birthday=req.getParameter("Birthday");
				String Remark=req.getParameter("Remark");
				
				CustomerContactor cc=new CustomerContactor();
				if(Birthday!=null&&!Birthday.trim().equals(""))
				cc.setBirthday(new Timestamp(DateTimeFormatUtil.DateFormat.parse(Birthday).getTime()));
				cc.setCellphone1(Cellphone1);
				cc.setCellphone2(Cellphone2);
				cc.setCount(1);
				cc.setCustomerId(Integer.valueOf(CustomerId));
		
				cc.setCurJob(CurJob);
				cc.setEmail(Email);
				cc.setRemark(Remark);
				cc.setStatus(0);
				cc.setLastUse(new Timestamp(System.currentTimeMillis()));
				CustomerContactorManager ccm=new CustomerContactorManager();
				cc.setName(Name);
				
				if(ccm.save(cc))
				{
					retObj5.put("IsOk", true);
					retObj5.put("msg", "添加成功！");
				}
				else {
					retObj5.put("IsOk", false);
					retObj5.put("msg", "添加失败！");
				}
			
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj5.toString());
			}
			break;
		case 6://查询联系人-区域关系表
			JSONObject retObj6=new JSONObject();
			try {
				String RegionId=req.getParameter("RegionId");
				String Name=req.getParameter("Name");
				String Status=req.getParameter("Status");
				
				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				
				String queryStr = "from RegionInsideContactor as model where 1=1 ";
				List<Object> keys = new ArrayList<Object>();
				
				if(RegionId!=null&&!RegionId.equals(""))
				{
					String cusNameStr = URLDecoder.decode(RegionId, "UTF-8");
					queryStr = queryStr + " and (model.region.id = ? )";
					keys.add( Integer.valueOf(cusNameStr) );
				}
				if(Name!=null&&!Name.equals(""))
				{
					String cusNameStr = URLDecoder.decode(Name, "UTF-8");
					queryStr = queryStr + " and (model.sysUser.name like ? )";
					keys.add("%" + cusNameStr + "%");
				}
				if(Status!=null&&!Status.trim().equals(""))
				{
					String cusNameStr = URLDecoder.decode(Status.trim(), "UTF-8");
					queryStr = queryStr + " and (model.status = ? )";
					keys.add(Integer.valueOf(cusNameStr));
				}
				
				BaseHibernateDAO dao=new BaseHibernateDAO();
				List<RegionInsideContactor> lrContactors=dao.findPageAllByHQL(queryStr, page, rows, keys);
				int total=dao.getTotalCountByHQL("select count(*) "+queryStr, keys);
				JSONArray ja=new JSONArray();
				for(RegionInsideContactor a:lrContactors)
				{
					JSONObject tmp=new JSONObject();
					tmp.put("Id", a.getId());
					tmp.put("RegionName", a.getRegion().getName());
					tmp.put("RegionId", a.getRegion().getId());
					tmp.put("ContactorId", a.getSysUser().getId());
					tmp.put("Name", a.getSysUser().getName());
					tmp.put("JobNum", a.getSysUser().getJobNum());
					tmp.put("Status", a.getStatus());
					tmp.put("LastEditTime", a.getLastEditTime()==null?"":a.getLastEditTime().toLocaleString());
					ja.put(tmp);
				}
				retObj6.put("total", total);
				retObj6.put("rows", ja);
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retObj6.toString());
			}
			break;
		case 7://新增联系人-区域映射关系
			JSONObject retObj7=new JSONObject();
			try {
				String RegionId=req.getParameter("RegionId1");
				String ContactorId=req.getParameter("InsideContactor1");
				RegionInsideContactor r=new RegionInsideContactor();
				
				
				Region re=new Region();
				re.setId(Integer.parseInt(RegionId));
				r.setRegion(re);
				SysUser s=new SysUser();
				s.setId(Integer.parseInt(ContactorId));
				r.setSysUser(s);
				r.setStatus(2);
				r.setLastEditTime(new Timestamp(System.currentTimeMillis()));
				RegionContactorManager rcm=new RegionContactorManager();
				
				List<RegionInsideContactor> ck=rcm.findByVarProperty(new KeyValueWithOperator("sysUser.id",Integer.parseInt(ContactorId),"="),
						new KeyValueWithOperator("region.id",Integer.parseInt(RegionId),"="));
				if(ck.size()==0)
				{
					boolean res=rcm.save(r);
					retObj7.put("IsOk", res);
					retObj7.put("msg", res==true?"添加成功":"添加失败，请重试！");
				}
				else
				{
					retObj7.put("IsOk", false);
					retObj7.put("msg", "已存在该记录！");
				}
				
	
			} catch (Exception e) {
				// TODO: handle exception
			}
			finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj7.toString());
			}
			break;
		case 8://修改
			JSONObject retObj8=new JSONObject();
			try {
				String Status=req.getParameter("Status");
				String Id=req.getParameter("Id");
				String RegionId=req.getParameter("RegionId");
				String ContactorId=req.getParameter("ConId");
				RegionInsideContactor r=new RegionInsideContactor();
				RegionContactorManager rcm=new RegionContactorManager();
				r=rcm.FindById(Integer.parseInt(Id));
				
				List<RegionInsideContactor> r2=rcm.findByVarProperty(new KeyValueWithOperator("region.id",Integer.parseInt(RegionId),"="),new KeyValueWithOperator("id",Integer.parseInt(Id),"<>"),new KeyValueWithOperator("sysUser.id",Integer.parseInt(ContactorId),"="));
				//如果在数据库中找到一条记录其区域Id和ContactorId相同而Id不同的，则不予修改
				if(r2!=null&&r2.size()!=0)
				{
					retObj8.put("IsOk", false);
					retObj8.put("msg", "已存在该记录！");
				}
				else
				{
					if(r.getRegion().getId()!=Integer.parseInt(RegionId))
						{
							Region rg=new Region();
							rg.setId(Integer.parseInt(RegionId));
							r.setRegion(rg);
						}
					if(r.getSysUser().getId()!=Integer.parseInt(ContactorId))
					{
						SysUser s=new SysUser();
						s.setId(Integer.parseInt(ContactorId));
						r.setSysUser(s);
					}
					if(r.getStatus()!=Integer.parseInt(Status))
					{
						r.setStatus(Integer.parseInt(Status));
					}
					r.setLastEditTime(new Timestamp(System.currentTimeMillis()));
					if(rcm.save(r))
					{
						retObj8.put("IsOk", true);
						retObj8.put("msg", "修改成功！");
					}
					else {
						retObj8.put("IsOk", false);
						retObj8.put("msg", "修改失败，请重试！");
					}
					
				}
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj8.toString());
			}
			break;
		case 9:
			JSONArray retObj9=new JSONArray();
			try {
				String RegionId=req.getParameter("RegionId");
				
				String queryStr = "from RegionInsideContactor as model where 1=1 and (model.status <>3) ";
				List<Object> keys = new ArrayList<Object>();
				
				if(RegionId!=null&&!RegionId.equals(""))
				{
					String cusNameStr = RegionId;
					queryStr = queryStr + " and (model.region.id = ? )";
					keys.add( Integer.valueOf(cusNameStr) );
				}
	 
				
				BaseHibernateDAO dao=new BaseHibernateDAO();
				List<RegionInsideContactor> lrContactors=dao.findPageAllByHQL(queryStr, 1, 30, keys);
				//JSONArray ja=new JSONArray();
				for(RegionInsideContactor a:lrContactors)
				{
					JSONObject tmp=new JSONObject();
					tmp.put("Id", a.getId());
					tmp.put("RegionId", a.getRegion().getId());
					tmp.put("ContactorId", a.getSysUser().getId());
					tmp.put("Name", a.getSysUser().getName());
					retObj9.put(tmp);
				}
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj9.toString());
			}
			break;
		}
	}

	private CustomerContactor initCustomerContactor(HttpServletRequest req, int id){
		CustomerContactor cusCon;
		if(id==0){
			cusCon = new CustomerContactor();
		}
		else{
			cusCon = (new CustomerContactorManager()).findById(id);
		}
		String Name = req.getParameter("Name");
		String CustomerId = req.getParameter("CustomerId");
		String Cellphone1 = req.getParameter("Cellphone1");
		String Cellphone2 = req.getParameter("Cellphone2");
		String Email = req.getParameter("Email");
		///////////////////////////////
		String CurJob=req.getParameter("CurJob");
		String CurDep=req.getParameter("CurDep");
		String FormerJob=req.getParameter("FormerJob");
		String FormerDep=req.getParameter("FormerDep");
		String Birthday=req.getParameter("Birthday");
		String Remark=req.getParameter("Remark");
		String Status=req.getParameter("Status");
		///////////////////////////////
		
		cusCon.setName(Name);
		cusCon.setCellphone1(Cellphone1);
		cusCon.setCellphone2(Cellphone2);
		cusCon.setEmail(Email);
		
		
		//////////////////////
		if(Birthday!=null&&!Birthday.equals(""))
		{
		try {
			cusCon.setBirthday(new Timestamp(DateTimeFormatUtil.DateFormat.parse(Birthday).getTime()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}}
		cusCon.setCurJob(CurJob);
	
		/////////////////////
		cusCon.setCustomerId(Integer.valueOf(CustomerId));
		cusCon.setLastUse(new Timestamp(System.currentTimeMillis()));
		cusCon.setCount(1);
		if(Status!=null&&!Status.equals(""))cusCon.setStatus(Integer.parseInt(Status));
		if(id==0)cusCon.setStatus(0);
		cusCon.setRemark(Remark);
		cusCon.setFormerDep(FormerDep);
		cusCon.setFormerJob(FormerJob);
		cusCon.setCurDep(CurDep);
		
		return cusCon;
	}
	
}
