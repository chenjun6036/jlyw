package com.jlyw.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.jlyw.hibernate.ApplianceManufacturer;
import com.jlyw.hibernate.AppliancePopularName;
import com.jlyw.hibernate.TargetAppliance;
import com.jlyw.hibernate.view.ViewApplianceSpecialStandardNamePopularName;
import com.jlyw.manager.ApplianceManufacturerManager;
import com.jlyw.manager.AppliancePopularNameManager;
import com.jlyw.manager.view.ViewAppSpeStandardPopularNameManager;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.LetterUtil;

/**
 * ����Servlet
 * �����ڡ��½�ί�е���ʱ���ߵ�ѡ��
 * @author Zhan
 *
 */
public class ApplianceServlet extends HttpServlet {
	private static Log log = LogFactory.getLog(ApplianceServlet.class);
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Integer method = Integer.parseInt(req.getParameter("method"));	//�ж�����ķ�������
		ViewAppSpeStandardPopularNameManager mainMgr = new ViewAppSpeStandardPopularNameManager();
		switch (method) {
		case 0: // �½�ί�е�ʱ������������Ȩ���Ʋ��ң�AutoComplete������������Ȩ���ơ������ǣ�����������׼���ơ���������
			JSONArray jsonArray = new JSONArray();
			try {
				String queryNameStr = req.getParameter("QueryName");	//��ѯ�� �����ơ�
				
				if(queryNameStr != null && queryNameStr.trim().length() > 0){
					String queryName =  new String(queryNameStr.trim().getBytes("ISO-8859-1"), "UTF-8");	//���URL����������������
//					queryName = LetterUtil.String2Alpha(queryName);	//ת����ƴ������
//					String[] queryNames = cusName.split(" \\s+");	//���ݿո���ָ�
//					if(queryNames.length == 0){
//						return;
//					}
//					queryName = "";
//					for(int i = 0; i < queryNames.length; i++){
//						queryName += queryNames[i];
//						if(i != queryNames.length-1)
//							queryName += "%";
//					}
					
					queryName = "%" + queryName + "%";
					String queryString = "from ViewApplianceSpecialStandardNamePopularName as model " +
						" where (model.name like ? or model.brief like ? ) and model.status <> 1";
					AppliancePopularNameManager popnameMgr = new AppliancePopularNameManager();
					List<ViewApplianceSpecialStandardNamePopularName> vRetList = mainMgr.findPageAllByHQL(queryString, 1, 30, queryName, queryName);
					for(ViewApplianceSpecialStandardNamePopularName vRow : vRetList){
						JSONObject jsonObj = new JSONObject();
						switch(vRow.getId().getType()){
						case 0:	//��׼����
							jsonObj.put("SpeciesType", 0);
							jsonObj.put("ApplianceSpeciesId", vRow.getId().getId());
							jsonObj.put("Name", vRow.getName()+"[��]");
							jsonObj.put("StandName", vRow.getName());//�����ӣ��ֳ�ҵ���л�ȡ��׼���Ƶļ��ѣ�
							jsonObj.put("PopName", "");
							break;
						case 1:	//��������
							jsonObj.put("SpeciesType", 1);
							jsonObj.put("ApplianceSpeciesId", vRow.getId().getId());
							jsonObj.put("Name", vRow.getName()+"[��]");
							jsonObj.put("StandName", vRow.getName());//�����ӣ��ֳ�ҵ���л�ȡ��׼���Ƶļ��ѣ�
							jsonObj.put("PopName", "");
							break;
						case 2:	//��������
							jsonObj.put("SpeciesType", 0);	//���������ñ�׼���ƴ���
							jsonObj.put("ApplianceSpeciesId", popnameMgr.findById(vRow.getId().getId()).getApplianceStandardName().getId());	//��׼���Ƶ�ID
							jsonObj.put("Name", vRow.getName()+"[��]");
							jsonObj.put("StandName", vRow.getName());//�����ӣ��ֳ�ҵ���л�ȡ��׼���Ƶļ��ѣ�
							jsonObj.put("PopName", vRow.getName());
							break;
						default:
							continue;
						}
						jsonArray.put(jsonObj);
					}
				
//					/*****************        ��ѯ�Ƿ��Ƿ�������(��Ҫ���ƣ���׼�����ϼ����ϼ��޷���)               *********************/
//					String queryString = String.format("select model.id,model.name from ApplianceSpecies as model where " +
//							"model.name like ? and model.status = 0 and " +
//							"( " +
//								"model.id in (select aa.id.appSpeId from ViewApplianceSpecialStandardNameProject as aa where aa.appStaNameStatus = 0 group by aa.id.appSpeId having count(DISTINCT aa.id.appStaNameId)<2 or count(DISTINCT aa.id.proTeamId)<2 ) " +  //���������µı�׼��������ֻ��1���Ļ��߼�����Ŀ��ֻ��һ��
//							")");
//					List<Object[]> retAppSpeList = appSpeMgr.findPageAllByHQL(queryString, 1, 30, queryName);
//					if(retAppSpeList != null){
//						for(Object[] objArray : retAppSpeList){
//							JSONObject jsonObj = new JSONObject();
//							jsonObj.put("SpeciesType", 1);	//���߷������ͣ���������
//							jsonObj.put("ApplianceSpeciesId", (Integer)objArray[0]);
//							jsonObj.put("name", objArray[1].toString()+"[��]");	//��Ȩ������ʾ
//							jsonObj.put("ApplianceName", new JSONArray());		//��������
//							jsonObj.put("Model",  new JSONArray());		//�ͺŹ��
//							jsonObj.put("Range", new JSONArray());		//������Χ
//							jsonObj.put("Accuracy", new JSONArray());	//���ȵȼ�
//							jsonObj.put("Manufacturer", new JSONArray()); //���쳧
//							
//							//������Ŀ��ļ�����Ա
//							List<String> alloteeList = appStNameMgr.findByHQL("select DISTINCT b.name from ViewApplianceSpecialStandardNameProject as a, SysUser as b " +
//									"where a.id.appSpeId = ? and a.id.proTeamId = b.projectTeamId and b.status = 0", (Integer)objArray[0]);	//ѡ�����Ŀ����״̬Ϊ��������Ա:δ�����������򡣡���������
//							if(alloteeList != null){
//								JSONArray tempArray = new JSONArray();
//								for(String allotee : alloteeList){
//									JSONObject objTemp = new JSONObject();
//									objTemp.put("name", allotee);
//									tempArray.put(objTemp);
//								}
//								jsonObj.put("Allotee",  tempArray);		//������Ա
//							}else{
//								jsonObj.put("Allotee", new JSONArray());	//������Ա
//							}
//							
//							jsonArray.put(jsonObj);
//						}
//					}
//					
//					/*****************       ��ѯ�Ƿ��Ǳ�׼����             *****************************/
//					List<ApplianceStandardName> retAppStdNameList = appStNameMgr.findPagedAll(1, 30, new KeyValueWithOperator("name",queryName,"like"), new KeyValueWithOperator("status", 0, "="));
//					if(retAppStdNameList != null){
//						for(ApplianceStandardName stdName : retAppStdNameList){
//							JSONObject jsonObj = new JSONObject();
//							jsonObj.put("SpeciesType", 0);	//���߷������ͣ���׼����
//							jsonObj.put("ApplianceSpeciesId", stdName.getId());
//							jsonObj.put("name", String.format("%s-%s[��]", stdName.getApplianceSpecies().getName(), stdName.getName()));	  //��Ȩ������ʾ
//
//							List<AppliancePopularName> appPopNameList = appPopNameMgr.findByVarProperty(new KeyValueWithOperator("applianceStandardName.id",stdName.getId(),"="));
//							if(appPopNameList != null){
//								JSONArray tempArray = new JSONArray();
//								for(AppliancePopularName popName : appPopNameList){
//									JSONObject objTemp = new JSONObject();
//									objTemp.put("name", popName.getPopularName());
//									tempArray.put(objTemp);
//								}
//								jsonObj.put("ApplianceName", tempArray);	//��������
//							}else{
//								jsonObj.put("ApplianceName", new JSONArray());	//��������
//							}
//							
//							//������ͼ���ͺŹ�񡢲�����Χ�����ȵȼ���
//							List<String> modelList = appStNameMgr.findByHQL("select DISTINCT v.model from ViewTargetApplianceModelAccuracyRange as v where v.standardNameId = ?", stdName.getId());
//							if(modelList != null){
//								JSONArray tempArray = new JSONArray();
//								for(String model : modelList){
//									JSONObject objTemp = new JSONObject();
//									objTemp.put("name", model);
//									tempArray.put(objTemp);
//								}
//								jsonObj.put("Model",  tempArray);		//�ͺŹ��
//							}else{
//								jsonObj.put("Model",  new JSONArray());		//�ͺŹ��
//							}
//							
//							List<String> rangeList = appStNameMgr.findByHQL("select DISTINCT v.range from ViewTargetApplianceModelAccuracyRange as v where v.standardNameId = ?", stdName.getId());
//							if(rangeList != null){
//								JSONArray tempArray = new JSONArray();
//								for(String range : rangeList){
//									JSONObject objTemp = new JSONObject();
//									objTemp.put("name", range);
//									tempArray.put(objTemp);
//								}
//								jsonObj.put("Range",  tempArray);		//������Χ
//							}else{
//								jsonObj.put("Range",  new JSONArray());		//������Χ
//							}
//							
//							List<String> accuracyList = appStNameMgr.findByHQL("select DISTINCT v.accuracy from ViewTargetApplianceModelAccuracyRange as v where v.standardNameId = ?", stdName.getId());
//							if(accuracyList != null){
//								JSONArray tempArray = new JSONArray();
//								for(String accuracy : accuracyList){
//									JSONObject objTemp = new JSONObject();
//									objTemp.put("name", accuracy);
//									tempArray.put(objTemp);
//								}
//								jsonObj.put("Accuracy",  tempArray);		//���ȵȼ�
//							}else{
//								jsonObj.put("Accuracy",  new JSONArray());		//���ȵȼ�
//							}
//							
//							//����ί�е������쳧
//							List<String> manufacturerList = appStNameMgr.findByHQL("select DISTINCT c.manufacturer from CommissionSheet as c where c.speciesType = ? and c.applianceSpeciesId = ? and c.status <> 10 ", false, stdName.getId());
//							if(manufacturerList != null){
//								JSONArray tempArray = new JSONArray();
//								for(String manufacturer : manufacturerList){
//									JSONObject objTemp = new JSONObject();
//									objTemp.put("name", manufacturer);
//									tempArray.put(objTemp);
//								}
//								jsonObj.put("Manufacturer",  tempArray);		//���ȵȼ�
//							}else{
//								jsonObj.put("Manufacturer",  new JSONArray());		//���ȵȼ�
//							}
//							
//							//������Ŀ��ļ�����Ա
//							List<String> alloteeList = appStNameMgr.findByHQL("select DISTINCT b.name from ViewApplianceSpecialStandardNameProject as a, SysUser as b " +
//									"where a.id.appStaNameId = ? and a.id.proTeamId = b.projectTeamId and b.status = 0", stdName.getId());	//ѡ�����Ŀ����״̬Ϊ��������Ա:δ�����������򡣡���������
//							if(alloteeList != null){
//								JSONArray tempArray = new JSONArray();
//								for(String allotee : alloteeList){
//									JSONObject objTemp = new JSONObject();
//									objTemp.put("name", allotee);
//									tempArray.put(objTemp);
//								}
//								jsonObj.put("Allotee",  tempArray);		//������Ա
//							}else{
//								jsonObj.put("Allotee", new JSONArray());	//������Ա
//							}
//							
//							jsonArray.put(jsonObj);
//						}
//					}
//					
//					/*****************       ��ѯ�Ƿ��ǳ�������             *****************************/
//					List<AppliancePopularName> retAppPopNameList = appPopNameMgr.findPagedAll(1, 30, new KeyValueWithOperator("popularName",queryName,"like"), new KeyValueWithOperator("status", 0, "="));
//					if(retAppPopNameList != null){
//						for(AppliancePopularName popName : retAppPopNameList){
//							JSONObject jsonObj = new JSONObject();
//							jsonObj.put("SpeciesType", 0);	//���߷������ͣ���׼����
//							jsonObj.put("ApplianceSpeciesId", popName.getApplianceStandardName().getId());
//							jsonObj.put("name", String.format("%s-%s[��]", popName.getApplianceStandardName().getName(), popName.getPopularName()));	  //��Ȩ������ʾ
//							
//							JSONArray appNameTempArray = new JSONArray();
//							JSONObject appNameTempObj = new JSONObject();
//							appNameTempObj.put("name", popName.getPopularName());
//							appNameTempArray.put(appNameTempObj);
//							jsonObj.put("ApplianceName", appNameTempArray);	//��������
//							
//							
//							//������ͼ���ͺŹ�񡢲�����Χ�����ȵȼ���
//							List<String> modelList = appStNameMgr.findByHQL("select DISTINCT v.model from ViewTargetApplianceModelAccuracyRange as v where v.standardNameId = ?", popName.getApplianceStandardName().getId());
//							if(modelList != null){
//								JSONArray tempArray = new JSONArray();
//								for(String model : modelList){
//									JSONObject objTemp = new JSONObject();
//									objTemp.put("name", model);
//									tempArray.put(objTemp);
//								}
//								jsonObj.put("Model",  tempArray);		//�ͺŹ��
//							}else{
//								jsonObj.put("Model",  new JSONArray());		//�ͺŹ��
//							}
//							
//							List<String> rangeList = appStNameMgr.findByHQL("select DISTINCT v.range from ViewTargetApplianceModelAccuracyRange as v where v.standardNameId = ?", popName.getApplianceStandardName().getId());
//							if(rangeList != null){
//								JSONArray tempArray = new JSONArray();
//								for(String range : rangeList){
//									JSONObject objTemp = new JSONObject();
//									objTemp.put("name", range);
//									tempArray.put(objTemp);
//								}
//								jsonObj.put("Range",  tempArray);		//������Χ
//							}else{
//								jsonObj.put("Range",  new JSONArray());		//������Χ
//							}
//							
//							List<String> accuracyList = appStNameMgr.findByHQL("select DISTINCT v.accuracy from ViewTargetApplianceModelAccuracyRange as v where v.standardNameId = ?", popName.getApplianceStandardName().getId());
//							if(accuracyList != null){
//								JSONArray tempArray = new JSONArray();
//								for(String accuracy : accuracyList){
//									JSONObject objTemp = new JSONObject();
//									objTemp.put("name", accuracy);
//									tempArray.put(objTemp);
//								}
//								jsonObj.put("Accuracy",  tempArray);		//���ȵȼ�
//							}else{
//								jsonObj.put("Accuracy",  new JSONArray());		//���ȵȼ�
//							}
//							
//							//����ί�е������쳧
//							List<String> manufacturerList = appStNameMgr.findByHQL("select DISTINCT c.manufacturer from CommissionSheet as c where c.speciesType = ? and c.applianceSpeciesId = ? and c.status <> 10 ", false, popName.getApplianceStandardName().getId());
//							if(manufacturerList != null){
//								JSONArray tempArray = new JSONArray();
//								for(String manufacturer : manufacturerList){
//									JSONObject objTemp = new JSONObject();
//									objTemp.put("name", manufacturer);
//									tempArray.put(objTemp);
//								}
//								jsonObj.put("Manufacturer",  tempArray);		//���ȵȼ�
//							}else{
//								jsonObj.put("Manufacturer",  new JSONArray());		//���ȵȼ�
//							}
//							
//							//������Ŀ��ļ�����Ա
//							List<String> alloteeList = appStNameMgr.findByHQL("select DISTINCT b.name from ViewApplianceSpecialStandardNameProject as a, SysUser as b " +
//									"where a.id.appStaNameId = ? and a.id.proTeamId = b.projectTeamId and b.status = 0", popName.getApplianceStandardName().getId());	//ѡ�����Ŀ����״̬Ϊ��������Ա:δ�����������򡣡���������
//							if(alloteeList != null){
//								JSONArray tempArray = new JSONArray();
//								for(String allotee : alloteeList){
//									JSONObject objTemp = new JSONObject();
//									objTemp.put("name", allotee);
//									tempArray.put(objTemp);
//								}
//								jsonObj.put("Allotee",  tempArray);		//������Ա
//							}else{
//								jsonObj.put("Allotee", new JSONArray());	//������Ա
//							}
//							
//							jsonArray.put(jsonObj);
//						}
//					}
//					
				}
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in ApplianceServlet-->case 0", e);
				}else{
					log.error("error in ApplianceServlet-->case 0", e);
				}
				
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray.toString());
			}
			break;
		case 1:	//��ѯ��׼���ߵĳ�������
			JSONArray jsonArray1 = new JSONArray();
			try{
				String SpeciesType = req.getParameter("SpeciesType");	//����
				String ApplianceSpeciesId = req.getParameter("ApplianceSpeciesId");	//Id
				if(SpeciesType.equals("0")){	//��׼����
					AppliancePopularNameManager popMgr = new AppliancePopularNameManager();
					List<AppliancePopularName>retList = popMgr.findByVarProperty(new KeyValueWithOperator("status", 1, "<>"),
							new KeyValueWithOperator("applianceStandardName.id", Integer.parseInt(ApplianceSpeciesId), "="));
					for(AppliancePopularName popName : retList){
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("id", popName.getId());
						jsonObj.put("name", popName.getPopularName());
						jsonArray1.put(jsonObj);
					}
				}
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in ApplianceServlet-->case 1", e);
				}else{
					log.error("error in ApplianceServlet-->case 1", e);
				}
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray1.toString());
			}
			break;
		case 2:	//��ѯ�ͺŹ��
			//������ͼ���ͺŹ�񡢲�����Χ�����ȵȼ���
			JSONArray jsonArray2 = new JSONArray();
			try{
				String SpeciesType = req.getParameter("SpeciesType");	//����
				String ApplianceSpeciesId = req.getParameter("ApplianceSpeciesId");	//Id
				if(SpeciesType.equals("0")){	//��׼����
					List<String> modelList = mainMgr.findByHQL("select DISTINCT v.model from ViewTargetApplianceModelAccuracyRange as v where v.standardNameId = ?", Integer.parseInt(ApplianceSpeciesId));		
					for(String model : modelList){
						JSONObject objTemp = new JSONObject();
						objTemp.put("name", model);
						jsonArray2.put(objTemp);
					}
				}
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in ApplianceServlet-->case 2", e);
				}else{
					log.error("error in ApplianceServlet-->case 2", e);
				}
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray2.toString());
			}
			break;
		case 3:	//��ѯ������Χ
			//������ͼ���ͺŹ�񡢲�����Χ�����ȵȼ���
			JSONArray jsonArray3 = new JSONArray();
			try{
				String SpeciesType = req.getParameter("SpeciesType");	//����
				String ApplianceSpeciesId = req.getParameter("ApplianceSpeciesId");	//Id
				if(SpeciesType.equals("0")){	//��׼����
					List<String> modelList = mainMgr.findByHQL("select DISTINCT v.range from ViewTargetApplianceModelAccuracyRange as v where v.standardNameId = ?", Integer.parseInt(ApplianceSpeciesId));		
					for(String model : modelList){
						JSONObject objTemp = new JSONObject();
						objTemp.put("name", model);
						jsonArray3.put(objTemp);
					}
				}
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in ApplianceServlet-->case 3", e);
				}else{
					log.error("error in ApplianceServlet-->case 3", e);
				}
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray3.toString());
			}
			break;
		case 4:	//��ѯ���ȵȼ�
			//������ͼ���ͺŹ�񡢲�����Χ�����ȵȼ���
			JSONArray jsonArray4 = new JSONArray();
			try{
				String SpeciesType = req.getParameter("SpeciesType");	//����
				String ApplianceSpeciesId = req.getParameter("ApplianceSpeciesId");	//Id
				if(SpeciesType.equals("0")){	//��׼����
					List<String> modelList = mainMgr.findByHQL("select DISTINCT v.accuracy from ViewTargetApplianceModelAccuracyRange as v where v.standardNameId = ?", Integer.parseInt(ApplianceSpeciesId));		
					for(String model : modelList){
						JSONObject objTemp = new JSONObject();
						objTemp.put("name", model);
						jsonArray4.put(objTemp);
					}
				}
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in ApplianceServlet-->case 4", e);
				}else{
					log.error("error in ApplianceServlet-->case 4", e);
				}
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray4.toString());
			}
			break;
		case 5:	//��ѯ���쳧��
			JSONArray jsonArray5 = new JSONArray();
			try{
				String SpeciesType = req.getParameter("SpeciesType");	//����
				String ApplianceSpeciesId = req.getParameter("ApplianceSpeciesId");	//Id
				if(SpeciesType.equals("0")){	//��׼����
					ApplianceManufacturerManager amfMgr = new ApplianceManufacturerManager();
					List<ApplianceManufacturer>retList = amfMgr.findByVarProperty(new KeyValueWithOperator("status", 1, "<>"),
							new KeyValueWithOperator("applianceStandardName.id", Integer.parseInt(ApplianceSpeciesId), "="));
					for(ApplianceManufacturer am : retList){
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("id", am.getId());
						jsonObj.put("name", am.getManufacturer());
						jsonArray5.put(jsonObj);
					}
				}
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in ApplianceServlet-->case 5", e);
				}else{
					log.error("error in ApplianceServlet-->case 5", e);
				}
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray5.toString());
			}
			break;
		case 6: // �½��ֳ�ҵ��ʱ�������Ŀ�������򣬡�������Ȩ���ơ������ǣ�����������׼���ơ���������
			JSONArray jsonArray6 = new JSONArray();
			try {
				String queryNameStr = req.getParameter("QueryName");	//��ѯ�� �����ơ�
				
				if(queryNameStr != null && queryNameStr.trim().length() > 0){
					String queryName =  new String(queryNameStr.trim().getBytes("ISO-8859-1"), "UTF-8");	//���URL����������������					
					queryName = "%" + queryName + "%";
					String queryString = "from ViewApplianceSpecialStandardNamePopularName as model " +
						" where (model.name like ? or model.brief like ? ) and model.status <> 1";
					List<ViewApplianceSpecialStandardNamePopularName> vRetList = mainMgr.findPageAllByHQL(queryString, 1, 30, queryName, queryName);
					for(ViewApplianceSpecialStandardNamePopularName vRow : vRetList){
						JSONObject jsonObj = new JSONObject();
						switch(vRow.getId().getType()){
						case 0:	//��׼����
							jsonObj.put("SpeciesType", 0);
						
							jsonObj.put("Name", vRow.getName()+"[��]");
							jsonObj.put("Project", vRow.getName());
							break;
						case 1:	//��������
							jsonObj.put("SpeciesType", 1);
							
							jsonObj.put("Name", vRow.getName()+"[��]");
							jsonObj.put("Project", vRow.getName());
							break;
						case 2:	//��������
							jsonObj.put("SpeciesType", 0);	//���������ñ�׼���ƴ���
				
							jsonObj.put("Name", vRow.getName()+"[��]");
							jsonObj.put("Project", vRow.getName());
							break;
						default:
							continue;
						}
						jsonArray6.put(jsonObj);
					}				
				}
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in ApplianceServlet-->case 0", e);
				}else{
					log.error("error in ApplianceServlet-->case 0", e);
				}
				
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray6.toString());
			}
			break;
		case 7:	//ί�е�����ҳ���С�model//��ģ����ѯ
			JSONArray jsonArray7 = new JSONArray();
			try{
				String standardNameId = req.getParameter("standardNameId");	//
				String QueryName = req.getParameter("QueryName");	//
				String queryName =new String(QueryName.trim().getBytes("ISO-8859-1"), "UTF-8");
				String Type = req.getParameter("Type");	//
			
				List<String> List=new ArrayList<String>();
				int type=Integer.parseInt(Type);
				switch(type){
					case 1:	//MOdel
						List = mainMgr.findByHQL("select DISTINCT v.model from ViewTargetApplianceModelAccuracyRange as v where v.targetApplianceStatus=0 and v.standardNameId = ? and v.model like ?", Integer.parseInt(standardNameId),"%"+queryName+"%");		
						break;
					case 2:	//Range
						List = mainMgr.findByHQL("select DISTINCT v.range from ViewTargetApplianceModelAccuracyRange as v where v.targetApplianceStatus=0 and v.standardNameId = ? and v.range like ?", Integer.parseInt(standardNameId),"%"+queryName+"%");	
						break;
					case 3:	//Accuracy
						List = mainMgr.findByHQL("select DISTINCT v.accuracy from ViewTargetApplianceModelAccuracyRange as v where v.targetApplianceStatus=0 and v.standardNameId = ? and v.accuracy like ?", Integer.parseInt(standardNameId),"%"+queryName+"%");			
						break;
				}
				
				for(String name : List){
					JSONObject objTemp = new JSONObject();
					objTemp.put("name", name);
					objTemp.put("Name", name);
					jsonArray7.put(objTemp);
				}
				
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in ApplianceServlet-->case 7", e);
				}else{
					log.error("error in ApplianceServlet-->case 7", e);
				}
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray7.toString());
			}
			break;
		}
	}

}
