package com.jlyw.servlet.user;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
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
import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.jlyw.hibernate.ApplianceStandardName;
import com.jlyw.hibernate.Qualification;
import com.jlyw.hibernate.SysUser;
import com.jlyw.manager.ApplianceSpeciesManager;
import com.jlyw.manager.ApplianceStandardNameManager;
import com.jlyw.manager.QualificationManager;
import com.jlyw.manager.UserManager;
import com.jlyw.util.FlagUtil.QualificationType;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.UIDUtil;

public class QualificationServlet extends HttpServlet {
	private static Log log = LogFactory.getLog(QualificationServlet.class);
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		int method = Integer.valueOf(req.getParameter("method"));
		QualificationManager qualMgr = new QualificationManager();
		switch(method)
		{
		case 1://新增或注销检测人员资质
			JSONObject retObj = new JSONObject();
			try{
				String EmpId = req.getParameter("EmpId");
				String Type = req.getParameter("Type");
				String AuthItemStr = req.getParameter("AuthItemStr");
				String[] AuthItems = AuthItemStr.split("\\|");
				List<KeyValueWithOperator> keys = new ArrayList<KeyValueWithOperator>();
				List<Qualification> result;
				List<Qualification> saveQualList = new ArrayList<Qualification>();
				List<Qualification> updateQualList = new ArrayList<Qualification>();
				for(int i=0;i<AuthItems.length;i++)
				{
					keys.clear();
					String[] AuthItem = AuthItems[i].split("-");
					keys.add(new KeyValueWithOperator("sysUserByUserId.id", Integer.valueOf(EmpId), "="));
					keys.add(new KeyValueWithOperator("type", Integer.valueOf(Type), "="));
					keys.add(new KeyValueWithOperator("authItemType", Integer.valueOf(AuthItem[0]), "="));
					keys.add(new KeyValueWithOperator("authItemId", Integer.valueOf(AuthItem[1]), "="));
					result = qualMgr.findByVarProperty(keys);
					if(result==null||result.size()==0)
					{
						Qualification qual = new Qualification();
						qual.setSysUserByUserId((new UserManager()).findByVarProperty(new KeyValueWithOperator("id", Integer.valueOf(EmpId), "=")).get(0));
						qual.setType(Integer.valueOf(Type));
						qual.setAuthItemType(Integer.valueOf(AuthItem[0]));
						qual.setAuthItemId(Integer.valueOf(AuthItem[1]));
						qual.setStatus(0);
						qual.setSysUserByLastEditorId((SysUser)req.getSession().getAttribute("LOGIN_USER"));
						qual.setLastEditorTime(new Timestamp(System.currentTimeMillis()));
						saveQualList.add(qual);
					}
					else
					{
						for(Qualification qual : result)
						{
							qual.setStatus(qual.getStatus()==0?1:0);
							updateQualList.add(qual);
						}
					}
				}
				if(qualMgr.saveByBatch(saveQualList)||qualMgr.updateByBatch(updateQualList))
				{
					try {
						retObj.put("IsOK", true);
						retObj.put("msg", "操作成功！");
					} catch (Exception ex) {}
				}
				else{
					try {
						retObj.put("IsOK", false);
						retObj.put("msg", "操作失败，请重新操作！");
					} catch (Exception ex) {}
				}

			}catch (Exception e){
				try {
					retObj.put("IsOK", false);
					retObj.put("msg", "操作失败，请重新操作！");
				} catch (Exception ex) {}
				
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in QualificationServlet-->case 1", e);
				}else{
					log.error("error in QualificationServlet-->case 1", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj.toString());
			}
			break;
		case 2://根据员工id查询员工的检验资质
			JSONArray options = new JSONArray();
			try{
				String EmpId = req.getParameter("EmpId");
				EmpId = new String(EmpId.trim().getBytes("ISO-8859-1"), "GBK");
				List<Qualification> result = qualMgr.findByVarProperty(new KeyValueWithOperator("sysUserByUserId.id", Integer.valueOf(EmpId), "="),new KeyValueWithOperator("status", 0, "="));
				for(Qualification temp : result)
				{
					JSONObject option = new JSONObject();
					option.put("treeId", temp.getAuthItemType()+"-"+temp.getAuthItemId());
					option.put("Type", temp.getType());
					option.put("QualId", temp.getId());
					
					options.put(option);
				}
			}catch (Exception e){
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in QualificationServlet-->case 2", e);
				}else{
					log.error("error in QualificationServlet-->case 2", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(options.toString());
			}
			break;
		case 3://导出
			JSONObject retObj3 = new JSONObject();
			try{
				List<Object> keys = new ArrayList<Object>();
				String queryStr = "select model.sysUserByUserId.id, model.authItemType, model.authItemId, model.type from Qualification as model where model.status = 0";
				
				String Emp = req.getParameter("EmpId");
				if(Emp!=null&&!Emp.equals("")){
					queryStr = queryStr + " and model.sysUserByUserId.id = ?";
					keys.add(Integer.valueOf(Emp));
				}
				
				queryStr = queryStr + " group by model.sysUserByUserId.id, model.sysUserByUserId.jobNum, model.authItemType, model.authItemId, model.type order by model.sysUserByUserId.jobNum asc";
				
				List<Object[]> result = qualMgr.findByHQL(queryStr, keys);
				File f = null;
				FileOutputStream os = null;
					
				List<String> title = qualMgr.formatTitle();
					
				HSSFWorkbook wb = new HSSFWorkbook();
				HSSFSheet sheet = wb.createSheet();
				HSSFRow row;
				HSSFCell cell;
				row = sheet.createRow(0);
				for(int i = 0; i < title.size();i++)
				{
					cell = row.createCell(i);
					cell.setCellValue(title.get(i));
				}
				int k = 1;
				String[] cells = new String[9];
				String tempUser = null;
				String tempAuthItemType = null;
				String tempAuthItemId = null;
				for(int j = 0;j<result.size();j++)
				{	
					Object[] temp = result.get(j);
					if(j!=0&&tempUser.equals(temp[0].toString())&&tempAuthItemType.equals(temp[1].toString())&&tempAuthItemId.equals(temp[2].toString())){
						switch(Integer.valueOf(temp[3].toString())){
						case QualificationType.Type_Jianding:
							cells[3] = "√";
							break;
						case QualificationType.Type_Jiaozhun:
							cells[4] = "√";
							break;
						case QualificationType.Type_Jianyan:
							cells[5] = "√";
							break;
						case QualificationType.Type_Except:
							cells[6] = "√";
							break;
						case QualificationType.Type_Heyan:
							cells[7] = "√";
							break;
						case QualificationType.Type_Qianzi:
							cells[8] = "√";
							break;
						}
					}
					else{
						if(j!=0){
							row = sheet.createRow(k++);
							List<String> excel = (List<String>) qualMgr.formatExcel(cells);
							for(int i = 0; i<excel.size(); i++)
							{
								cell = row.createCell(i);
								cell.setCellValue(excel.get(i).toString());
							}
						}
						cells = new String[9];
						cells[0] = (new UserManager()).findById(Integer.valueOf(temp[0].toString())).getName();
						cells[1] = temp[1].toString().equals("0")?"器具标准名称":"器具分类";
						if(temp[1].toString().equals("0"))
						{
							ApplianceStandardName stdName = (new ApplianceStandardNameManager()).findById(Integer.valueOf(temp[2].toString()));
							cells[2] = stdName.getApplianceSpecies().getName() + "---" + stdName.getName();
						}
						else
							cells[2] = (new ApplianceSpeciesManager()).findById(Integer.valueOf(temp[2].toString())).getName();
						switch(Integer.valueOf(temp[3].toString())){
						case QualificationType.Type_Jianding:
							cells[3] = "√";
							break;
						case QualificationType.Type_Jiaozhun:
							cells[4] = "√";
							break;
						case QualificationType.Type_Jianyan:
							cells[5] = "√";
							break;
						case QualificationType.Type_Except:
							cells[6] = "√";
							break;
						case QualificationType.Type_Heyan:
							cells[7] = "√";
							break;
						case QualificationType.Type_Qianzi:
							cells[8] = "√";
							break;
						}
					}
					tempUser = temp[0].toString();
					tempAuthItemType = temp[1].toString();
					tempAuthItemId = temp[2].toString();
				}
				
				f = File.createTempFile(UIDUtil.get22BitUID(), ".xls");
				os = new FileOutputStream(f);
				wb.write(os);
				os.flush();			
				retObj3.put("IsOK", f.length()>0?true:false);
				retObj3.put("Path", f.length()>0?f.getAbsolutePath():"");
			}catch(Exception e){
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in QualificationServlet-->case 3", e);
				}else{
					log.error("error in QualificationServlet-->case 3", e);
				}
				try{
					retObj3.put("IsOK", false);
					retObj3.put("Path", "");
				}catch(Exception e1){}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj3.toString());
			}
			break;
		}
	}

}
